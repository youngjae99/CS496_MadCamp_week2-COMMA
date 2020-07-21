package com.example.project2;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.target.SquaringDrawable;

import java.io.ByteArrayOutputStream;
import java.net.URI;

public class FullScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);
        Log.d("FullScreenActivity","onCreate:started");
        getIncomingIntent();
    }
    private void getIncomingIntent(){
        Log.d("FULL", "getIncomingIntent: check");
        String bitmap = getIntent().getStringExtra("bitmap");
        setImage(StringToBitmap(bitmap));
    }
    private void setImage(Bitmap bitmap){
        ImageView img = findViewById(R.id.fullScreenImageView);
        Log.d("FULL","img view success");
        img.setImageBitmap(bitmap);
    }
    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
