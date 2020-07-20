package com.example.project2;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Fragment1 extends Fragment{

    private RecyclerView recyclerView;
    private ListView lv;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Fragment1() {
        // Required empty public constructor

    }

    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment1, container, false);
        ArrayList<Person> phone_address= new ArrayList<>();
        ContactAdapter contactAdapter = new ContactAdapter(getContext(), R.layout.contact_layout, phone_address);
        lv = (ListView) v.findViewById(R.id.list);
        lv.setAdapter(contactAdapter);

        /*Call<String> users = iMyService.getUser();
        users.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Test1", ""+response.body());
                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    for (int i=0; i<jsonArray.length(); i++){
                        String name = jsonArray.getJSONObject(i).getString("name");
                        String email = jsonArray.getJSONObject(i).getString("email");
                        String phone_number = jsonArray.getJSONObject(i).getString("phone_number");
                        Log.i("유저 정보", name + " / " + email + " / " + phone_number);
                        phone_address.add(new Person(name, email, phone_number));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });*/

        Log.i("여기", "입장");
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);
        //ArrayList<Person> phone_address = ContactUtil.getAddressBook(getContext());
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(iMyService.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++) {
                            String name = jsonArray.getJSONObject(i).getString("name");
                            String email = jsonArray.getJSONObject(i).getString("email");
                            String phone_number = jsonArray.getJSONObject(i).getString("phone_number");
                            Log.i("유저 정보", name + " / " + email + " / " + phone_number);
                            contactAdapter.getItems(new Person(name, email, phone_number));
                        }
                        contactAdapter.notifyDataSetChanged();
                    }
                }));
        Log.i("End", "Game");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowID)
            {
                doSelectFriend((Person)parent.getItemAtPosition(position));
            }});

        ImageButton newContact = (ImageButton) v.findViewById(R.id.newContact);
        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent
                Context context2 = v.getContext();                                                    // Context 수정
                Intent newPostIntent = new Intent(context2, NewContact.class);
                startActivity(newPostIntent);
            }
        });

        return v;
    }

    public void doSelectFriend(Person p)
    {
        Log.e("####", p.getName() + ", " + p.getNumber());
    }
}