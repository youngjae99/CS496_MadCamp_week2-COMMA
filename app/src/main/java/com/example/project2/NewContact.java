package com.example.project2;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class NewContact extends AppCompatActivity {

    private ListView lv;
    String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);

        user_email = getIntent().getStringExtra("user_email");

        lv = (ListView) findViewById(R.id.add_user_list);
        ArrayList<Person> phone_address = ContactUtil.getAddressBook(this);
        ContactAdapter contactAdapter = new ContactAdapter(this, R.layout.contact_layout, phone_address);
        lv.setAdapter(contactAdapter);

        TextView signupButton = (TextView) findViewById(R.id.addContact);
        signupButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray checkedItems = lv.getCheckedItemPositions();
                int count = contactAdapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        AddContact(user_email, phone_address.get(i));
                    }
                }

                finish();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowID)
            {
                Log.e("포지션", ""+position);
                if (lv.isItemChecked(position)) view.setBackgroundColor(Color.GRAY);
                else view.setBackgroundColor(Color.WHITE);
            }});
    }

    private void AddContact(String user_email, Person person) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        IMyService iMyService;
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        compositeDisposable.add(iMyService.AddContact(user_email, person.getName(), person.getEmail(), person.getNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.e("contact 추가", ""+response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down,R.anim.slide_down);
    }
}
