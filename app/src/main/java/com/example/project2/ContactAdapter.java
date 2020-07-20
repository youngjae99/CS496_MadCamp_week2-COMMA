package com.example.project2;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Person> {
    private ArrayList<Person> items;
    private final Context mContext;

    private ViewHolder mViewHolder;

    public ContactAdapter(Context context, int textViewResourceId, ArrayList<Person> items)
    {
        super(context, textViewResourceId, items);
        this.mContext = context;
        this.items = items;
    }
    public void clear() { this.items.clear(); }
    public void getItems(Person items){
        this.items.add(items);
    }
    public void deleteItems(int position) { this.items.remove(position); }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.contact_layout, null);
            mViewHolder = new ViewHolder(v);
            v.setTag(mViewHolder);
        }
        else mViewHolder = (ViewHolder) v.getTag();

        Person p = items.get(position);

        Bitmap photo_bitmap = loadPhoto(mContext.getContentResolver(), p.getPhoto_id());

        if (photo_bitmap != null) {
            mViewHolder.photo.setImageBitmap(photo_bitmap);
        }
        else {
            mViewHolder.photo.setImageResource(R.drawable.ic_launcher_foreground);
        }
        // round 이미지
        /*photo.setBackground(new ShapeDrawable(new OvalShape()));
        if (Build.VERSION.SDK_INT>=21) {
            photo.setClipToOutline(true);
        }*/
        mViewHolder.name.setText(p.getName());
        mViewHolder.number.setText(p.getNumber());
        mViewHolder.email.setText(p.getEmail());

        return v;
    }

    public Bitmap loadPhoto(ContentResolver cr, long photo_id) {
        byte[] photoBytes = null;
        Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, photo_id);
        Cursor c = cr.query(photoUri, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO},
                null,null, null);
        try {
            if (c.moveToFirst())
                photoBytes = c.getBlob(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }

        if (photoBytes != null) {
            return resizingBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));
        } else {
            Log.d("<<CONTACT_PHOTO>>", "second try also failed");
        }
        return null;
    }

    public Bitmap resizingBitmap(Bitmap oBitmap) {
        if (oBitmap == null) {
            return null;
        }
        Bitmap rBitmap = null;

        rBitmap = Bitmap.createScaledBitmap(oBitmap, 100, 80, true);
        return rBitmap;
    }
    public class ViewHolder {

        private ImageView photo;
        private TextView name;
        private TextView number;
        private TextView email;


        public ViewHolder(View convertView) {

            photo = (ImageView) convertView.findViewById(R.id.photo);
            name = (TextView) convertView.findViewById(R.id.name);
            number = (TextView) convertView.findViewById(R.id.msg);
            email = (TextView) convertView.findViewById(R.id.email);

        }

    }

}
