package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NewContact extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);

        lv = (ListView) findViewById(R.id.add_user_list);
        ArrayList<Person> phone_address = ContactUtil.getAddressBook(this);
        ContactAdapter contactAdapter = new ContactAdapter(this, R.layout.contact_layout, phone_address);
        lv.setAdapter(contactAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowID)
            {
                doSelectFriend((Person)parent.getItemAtPosition(position));
            }});
    }
    // 연락처 list click했을 경우
    public void doSelectFriend(Person p)
    {
        /*
        Intent intent = new Intent(this, CallOrMsgSelect.class);
        intent.putExtra("number", p.getNumber());
        startActivity(intent);

        Log.e("####", p.getName() + ", " + p.getNumber());

         */
    }

}
