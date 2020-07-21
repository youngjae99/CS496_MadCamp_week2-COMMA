package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }
    private OnListItemSelectedInterface mListener;
    private OnListItemLongSelectedInterface mLongListener;

    private Context context;
    //private OnItemClickListener onItemClickListener;
    private ArrayList<Bitmap> PhotoList;

    public ImageAdapter(Context context, ArrayList<Bitmap> PhotoList, OnListItemSelectedInterface listener, OnListItemLongSelectedInterface longListener) {
        this.context = context;
        this.PhotoList = PhotoList;
        this.mListener = listener;
        this.mLongListener = longListener;
    }

    public void getItems(Bitmap bitmap){
        this.PhotoList.add(bitmap);
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        //Glide.with(context).load(imageUrls.get(i).getImageUrl()).centerCrop().into(viewHolder.img);
        //final imgFormat cur = mPhotoList.get(i).getImgPath();

        Bitmap bitmap = PhotoList.get(i);

        viewHolder.img.setImageBitmap(bitmap);

        //Log.d("item_print", PhotoList.get(i));
        /*Glide.with(context)
                //.load(imageUrls.get(i).getImageUrl()) // 웹 이미지 로드
                .load(PhotoList.get(i).getImgPath()) // 이미지 로드
                //.load("/storage/emulated/0/Download/Domestic_Goose.jpg")
                .override(500,500) //해상도 최적화
                .thumbnail(0.3f) //섬네일 최적화. 지정한 %만큼 미리 이미지를 가져와 보여주기
                .centerCrop() // 중앙 크롭
                .into(viewHolder.img);*/
    }

    @Override
    public int getItemCount() {
        //return imageUrls.size();
        return PhotoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imageView);
            //view.setOnCreateContextMenuListener(this);

            view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){

                    int position = getAdapterPosition();
                    mListener.onItemSelected(v, position); // getAdapterPosition이었음

                    Log.d("Recyclerview", "position="+getAdapterPosition());

                    Intent fullScreenIntent=new Intent(context, FullScreenActivity.class);
                    fullScreenIntent.putExtra("bitmap", BitmapToString(PhotoList.get(position)));
                    context.startActivity(fullScreenIntent);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    /*mLongListener.onItemLongSelected(v, getAdapterPosition());
                    Log.d("Recyclerview", "position="+getAdapterPosition());*/
                    return false;
                }
            });
        }
    }
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float resizing_size = 180;
        if (width > resizing_size) {
            float mWidth = (float)(width / 100);
            float fScale = (float)(resizing_size / mWidth);
            width *= (fScale / 100);
            height *= (fScale / 100);

        } else if (height > resizing_size) {
            float mHeight = (float)(height / 100);
            float fScale = (float)(resizing_size / mHeight);
            width *= (fScale / 100);
            height *= (fScale / 100);
        }
        Bitmap rbitmap = Bitmap.createScaledBitmap(bitmap, (int)width, (int)height, true);

        rbitmap.compress(Bitmap.CompressFormat.PNG, 20, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

}
