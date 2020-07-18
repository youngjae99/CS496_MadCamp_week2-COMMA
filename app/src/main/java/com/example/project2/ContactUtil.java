package com.example.project2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ContactUtil {

    /**
     * 주소록의 이름, 전화번호 맵을 가져온다
     * @param context
     * @param isIDD 국제전화 규격 적용 여부
     * @return 이름, 전화번호 map
     */
    public static ArrayList<Person> getAddressBook(Context context, boolean isIDD)
    {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
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
        ArrayList<Person> result = new ArrayList<>(personlist);
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