package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    //private OnItemClickListener onItemClickListener;
    private ArrayList<MessageFormat> Messagelist;

    public MessageAdapter(Context context, ArrayList<MessageFormat> Messagelist) {
        this.context = context;
        this.Messagelist = Messagelist;
    }

    public void getItems(MessageFormat items){
        this.Messagelist.add(items);
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_layout, viewGroup, false);

        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder viewHolder, int i) {
        //Glide.with(context).load(imageUrls.get(i).getImageUrl()).centerCrop().into(viewHolder.img);
        //final imgFormat cur = mPhotoList.get(i).getImgPath();

        MessageFormat msg = Messagelist.get(i);
        String hide = msg.getHide();

        if (hide.equals("1")) viewHolder.name.setText(msg.getUsername());
        else viewHolder.name.setText("익명");
        viewHolder.message_body.setText(msg.getMessage());

    }

    @Override
    public int getItemCount() {
        return Messagelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message_body;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            message_body = view.findViewById(R.id.message_body);

            view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    /*
                    int position = getAdapterPosition();
                    mListener.onItemSelected(v, position); // getAdapterPosition이었음

                    Log.d("Recyclerview", "position="+getAdapterPosition());

                    Intent fullScreenIntent=new Intent(context, FullScreenActivity.class);
                    fullScreenIntent.putExtra("bitmap", BitmapToString(PhotoList.get(position)));
                    context.startActivity(fullScreenIntent);*/
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

}
