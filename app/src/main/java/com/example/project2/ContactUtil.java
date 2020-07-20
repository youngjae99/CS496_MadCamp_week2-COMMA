package com.example.project2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;

import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactUtil {

    /**
     * 주소록의 이름, 전화번호 맵을 가져온다
     * @param context
     * @param isIDD 국제전화 규격 적용 여부
     * @return 이름, 전화번호 map
     */
    public static ArrayList<Person> getAddressBook(Context context, boolean isIDD) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String email="none";
        if(ContactsContract.CommonDataKinds.Email.CONTENT_URI!=null){
            email = String.valueOf(ContactsContract.CommonDataKinds.Email.CONTENT_URI);
        }
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                email,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, sortOrder);
        LinkedHashSet<Person> personlist = new LinkedHashSet<>();
        while (cursor.moveToNext()) {
            Person Item = new Person();
            if (isIDD)
                Item.setNumber(getIDD(cursor.getString(0)));
            else
                Item.setNumber(cursor.getString(0));
            Item.setName(cursor.getString(1));
            Item.setEmail(cursor.getString(2));
            personlist.add(Item);
        }
        ArrayList<Person> result = new ArrayList<>(personlist);

        return null;
    }

    /**
     * 주소록의 이름, 전화번호 맵을 가져온다
     * @param context
     * @return 이름, 전화번호 map
     */
    public static ArrayList<Person> getAddressBook(Context context)
    {
        return getAddressBook(context, false);
    }

    /**
     * 국제전화 형식으로 변경한다.
     * @param phone
     * @return 국제전화번호 규격
     */
    public static String getIDD(String phone)
    {
        String result = phone;

        try {
            result = "82" + Long.parseLong(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}