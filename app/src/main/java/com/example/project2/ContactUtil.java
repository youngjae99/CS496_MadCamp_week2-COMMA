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
    public static ArrayList<Person> getAddressBook(Context context, boolean isIDD)
    {
        /*Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, sortOrder);
        LinkedHashSet<Person> personlist = new LinkedHashSet<>();
        while(cursor.moveToNext())
        {
            Person Item = new Person();
            if (isIDD)
                Item.setNumber(getIDD(cursor.getString(0)));
            else
                Item.setNumber(cursor.getString(0));
            Item.setName(cursor.getString(1));
            Item.setPhoto_id(cursor.getLong(2));
            Item.setPerson_id(cursor.getLong(3));

            personlist.add(Item);
        }
        ArrayList<Person> result = new ArrayList<>(personlist);*/
        /*
        ArrayList<Person> result = new ArrayList<Person>();
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(iMyService.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++){
                            String name = jsonArray.getJSONObject(i).getString("name");
                            String email = jsonArray.getJSONObject(i).getString("email");
                            String phone_number = jsonArray.getJSONObject(i).getString("phone_number");
                            Log.i("유저 정보", name + " / " + email + " / " + phone_number);
                            result.add(new Person(name, email, phone_number));
                        }
                    }
                }));*/

        return null;
        /*Call<String> users = iMyService.getUser();
        users.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Test1", response.body());
                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    for (int i=0; i<jsonArray.length(); i++){
                        String name = jsonArray.getJSONObject(i).getString("name");
                        String email = jsonArray.getJSONObject(i).getString("email");
                        Log.i("유저 정보", name + " / " + email);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        return result;*/
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