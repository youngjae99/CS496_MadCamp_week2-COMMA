package com.example.project2;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.target.SquaringDrawable;

import java.io.ByteArrayOutputStream;

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
        String imageUrl = getIntent().getStringExtra("image_url");
        setImage(imageUrl);
    }
    private void setImage(String imageUrl){
        Log.d("FULL","full screen image to " + imageUrl);
        ImageView img = findViewById(R.id.fullScreenImageView);
        Log.d("FULL","img view success");

        Glide.with(this)
                //.load(imageUrls.get(i).getImageUrl()) // 웹 이미지 로드
                .load(imageUrl) // imageUrl
                .into(img);

        /*
        Drawable d = img.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        Log.d("bitmapTest", BitmapToString(bitmap));

         */

        Bitmap bitmap = null;
        Drawable drawable = img.getDrawable();
        /*
        if (drawable instanceof GlideBitmapDrawable) {
            bitmap = ((GlideBitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof TransitionDrawable) {
            TransitionDrawable transitionDrawable = (TransitionDrawable) drawable;
            int length = transitionDrawable.getNumberOfLayers();
            for (int i = 0; i < length; ++i) {
                Drawable child = transitionDrawable.getDrawable(i);
                if (child instanceof GlideBitmapDrawable) {
                    bitmap = ((GlideBitmapDrawable) child).getBitmap();
                    break;
                } else if (child instanceof SquaringDrawable
                        && child.getCurrent() instanceof GlideBitmapDrawable) {
                    bitmap = ((GlideBitmapDrawable) child.getCurrent()).getBitmap();
                    break;
                }
            }
        } else if (drawable instanceof SquaringDrawable) {
            bitmap = ((GlideBitmapDrawable) drawable.getCurrent()).getBitmap();
        }
        */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (img.getDrawable() == null){
                    //Image doesn´t exist.
                    Log.d("bitmap","null좋아해");
                }else{
                    Drawable d = img.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                    Log.d("bitmapTest", BitmapToString(bitmap));
                }
            }
        }, 500);

        if(bitmap==null) Log.d("bitmap","null좋아해2");
        Log.d("bitmapTest", BitmapToString(bitmap));
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap rbitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        rbitmap.compress(Bitmap.CompressFormat.PNG, 20, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

}
