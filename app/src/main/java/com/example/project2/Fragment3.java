package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

import org.json.JSONArray;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Fragment3 extends Fragment{

    MessageAdapter messageAdapter;
    RecyclerView recyclerView;
    String user_email;

    public Fragment3() {
        // Required empty public constructor
    }

    public static Fragment3 newinstance(String email){
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putString("email", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_email = getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment3, container, false);

        ArrayList<MessageFormat> messagelist= new ArrayList<>();
        messageAdapter = new MessageAdapter(getActivity().getApplicationContext(), messagelist);
        recyclerView = (RecyclerView) v.findViewById(R.id.messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setAdapter(messageAdapter);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(iMyService.Get_Message(user_email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++) {
                            String name = jsonArray.getJSONObject(i).getString("name");
                            String msg = jsonArray.getJSONObject(i).getString("msg");
                            String hide = jsonArray.getJSONObject(i).getString("visible");

                            Log.e("메세지 정보", name +" / "+msg +" / "+hide);
                            messageAdapter.getItems(new MessageFormat(name, msg, hide));
                            Log.e("messageCNT", String.valueOf(messageAdapter.getItemCount()));
                        }
                        messageAdapter.notifyDataSetChanged();
                    }
                }));
        Log.i("End", "Game");

        return v;
    }
}