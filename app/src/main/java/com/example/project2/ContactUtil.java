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
        /*Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.Contacts._ID
        };
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, sortOrder);*/
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[] {
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.PHOTO_ID,
                },
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");

        LinkedHashSet<Person> personlist = new LinkedHashSet<>();
        //String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

        while (cursor.moveToNext()){
            try {
                Person Item = new Person();
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                Long  photo_id = cursor.getLong(2);
                String number = contactsPhone(context, id);
                String email = contactsEmail(context, id);
                if (isIDD)
                    Item.setNumber(getIDD(number));
                else
                    Item.setNumber(number);
                Item.setName(name);
                Item.setPhoto_id(photo_id);
                Item.setEmail(email);

                Log.e("여기여기여기 number", number);
                Log.e("여기여기여기 name  ", name);
                Log.e("여기여기여기 photo ", ""+photo_id);
                Log.e("여기여기여기 email ", email);
                personlist.add(Item);
            }catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        cursor.close();
        /*while (cursor.moveToNext()) {
            Person Item = new Person();
            if (isIDD)
                Item.setNumber(getIDD(cursor.getString(0)));
            else
                Item.setNumber(cursor.getString(0));
            Item.setName(cursor.getString(1));
            Item.setPhoto_id(cursor.getLong(2));
            Item.setEmail(cursor.getString(3));
            Log.e("여기여기여기 number", cursor.getString(0));
            Log.e("여기여기여기 name  ", cursor.getString(1));
            Log.e("여기여기여기 photo ", ""+cursor.getLong(2));
            Log.e("여기여기여기 email ", cursor.getString(3));
            personlist.add(Item);
        }
        cursor.close();*/
        ArrayList<Person> result = new ArrayList<>(personlist);

        return result;
    }

    public static String contactsPhone(Context mcontext, String p_id){
        String result = null;

        if(p_id==null || p_id.trim().equals("")){
            return result;
        }

        Cursor cursor = mcontext.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] {
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + p_id,
                null,
                null
        );
        while (cursor.moveToNext()){
            try {
                result = cursor.getString(0);
            }catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        cursor.close();

        return result;
    }
    public static String contactsEmail(Context mcontext, String p_id){
        String result = null;

        if(p_id==null || p_id.trim().equals("")){
            return result;
        }

        Cursor cursor = mcontext.getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[] {
                        ContactsContract.CommonDataKinds.Email.DATA
                },
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + p_id,
                null,
                null
        );
        while (cursor.moveToNext()){
            try {
                result = cursor.getString(0);
            }catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        cursor.close();

        return result;
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