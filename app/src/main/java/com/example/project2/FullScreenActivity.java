package com.example.project2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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

    }

}
