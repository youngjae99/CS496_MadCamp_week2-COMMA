package com.example.project2;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Fragment2 extends Fragment implements ImageAdapter.OnListItemSelectedInterface, ImageAdapter.OnListItemLongSelectedInterface {

    ImageView imageView;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    ImageAdapter dataAdapter;
    private static final int PICK_FROM_ALBUM1 = 1;
    private static final int PICK_FROM_ALBUM2 = 2;
    private static final int PICK_FROM_ALBUM3 = 3;
    String user_email;

    Retrofit retrofitClient;
    IMyService iMyService;

    ImageView img1;
    ImageView img2;
    ImageView img3;

    public ArrayList<imgFormat> FriendPhotoList = new ArrayList<>();
    static ArrayList<ImageUrl> imageUrlList = new ArrayList<>();


    public Fragment2() {
        // Required empty public constructor
    }

    public static Fragment2 newinstance(String email) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString("email", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_email = getArguments().getString("email");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment2, container, false);

        imageView = (ImageView) v.findViewById(R.id.imageView);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView2);
        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        img1 = v.findViewById(R.id.my_profile1);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                Log.e("fragment2", "출발");
                startActivityForResult(intent, PICK_FROM_ALBUM1);
            }
        });
        img1.setOnLongClickListener(new View.OnLongClickListener() { // 롱클릭 -> 이미지 삭제
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("Are you sure to delete 1st Profile photo?").setCancelable(false).setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes
                                Log.d("img1","long clicked!!");
                                img1.setImageURI(null);
                                change_profile(1, "");
                            }
                        }).setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();

                return true;
            }
        });

        img2 = v.findViewById(R.id.my_profile2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                Log.e("fragment2", "출발");
                startActivityForResult(intent, PICK_FROM_ALBUM2);
            }
        });
        img2.setOnLongClickListener(new View.OnLongClickListener() { // 롱클릭 -> 이미지 삭제
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("Are you sure to delete 2nd Profile photo?").setCancelable(false).setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes
                                Log.d("img2","long clicked!!");
                                img2.setImageURI(null);
                                change_profile(2, "");
                            }
                        }).setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();

                return true;
            }
        });

        img3 = v.findViewById(R.id.my_profile3);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                Log.e("fragment2", "출발");
                startActivityForResult(intent, PICK_FROM_ALBUM3);
            }
        });
        img3.setOnLongClickListener(new View.OnLongClickListener() { // 롱클릭 -> 이미지 삭제
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("Are you sure to delete 3rd Profile photo?").setCancelable(false).setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes
                                Log.d("img3","long clicked!!");
                                img3.setImageURI(null);
                                change_profile(3, "");
                            }
                        }).setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
                return true;
            }
        });

        set_profile();

        //로컬 사진 받아오기
        //localPhotoList = mGalleryManager.getAllPhotoPathList();
        //dataAdapter = new ImageAdapter(getActivity().getApplicationContext(), imageUrlList, localPhotoList, this, this);

        //친구 profile1 사진 받아오기
        FriendPhotoList = new ArrayList<>();
        dataAdapter = new ImageAdapter(getActivity().getApplicationContext(), imageUrlList, FriendPhotoList, this, this);
        recyclerView.setAdapter(dataAdapter);

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(iMyService.Get_All_profile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++) {
                            String profile = jsonArray.getJSONObject(i).getString("profile");
                            Log.e("################", profile);
                            //contactAdapter.getItems(new Person(name, email, phone_number, new Long(0)));
                        }
                    }
                }));

        return v;
    }

    private void set_profile() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(iMyService.Get_profile(user_email+"_profile")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.e("profile", "successss");
                        //response는 user_email_profile의 모든 정보
                        JSONArray jsonArray = new JSONArray(response);
                        String bitmap1 = null, bitmap2=null, bitmap3=null;
                        for (int i=0; i<3; i++){
                            String number = jsonArray.getJSONObject(i).getString("number");
                            if (number.equals("1")) bitmap1=jsonArray.getJSONObject(i).getString("bitmap");
                            if (number.equals("2")) bitmap2=jsonArray.getJSONObject(i).getString("bitmap");
                            if (number.equals("3")) bitmap3=jsonArray.getJSONObject(i).getString("bitmap");
                        }
                        if (bitmap1 != null) img1.setImageBitmap(StringToBitmap(bitmap1));
                        if (bitmap2 != null) img2.setImageBitmap(StringToBitmap(bitmap2));
                        if (bitmap3 != null) img3.setImageBitmap(StringToBitmap(bitmap3));
                    }
                }));
    }

    private void delete_profile(){
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(iMyService.Get_profile(user_email+"_profile")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.e("profile", "successss");
                        //response는 user_email_profile의 모든 정보
                        JSONArray jsonArray = new JSONArray(response);
                        String bitmap1 = null, bitmap2=null, bitmap3=null;
                        for (int i=0; i<3; i++){
                            String number = jsonArray.getJSONObject(i).getString("number");
                            if (number.equals("1")) bitmap1=jsonArray.getJSONObject(i).getString("bitmap");
                            if (number.equals("2")) bitmap2=jsonArray.getJSONObject(i).getString("bitmap");
                            if (number.equals("3")) bitmap3=jsonArray.getJSONObject(i).getString("bitmap");
                        }
                        if (bitmap1 != null) img1.setImageBitmap(StringToBitmap(bitmap1));
                        if (bitmap2 != null) img2.setImageBitmap(StringToBitmap(bitmap2));
                        if (bitmap3 != null) img3.setImageBitmap(StringToBitmap(bitmap3));
                    }
                }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            Log.e("Not","no image");
            return ;
        }
        Uri uri = data.getData();
        Drawable d;
        Bitmap bitmap;
        String result;
        if (requestCode == PICK_FROM_ALBUM1){
            img1.setImageURI(uri);
            d = img1.getDrawable();
            bitmap = ((BitmapDrawable)d).getBitmap();
            result = BitmapToString(bitmap);
            change_profile(1, result);
            Log.e("변화!!", "1111111111");
        }
        else if (requestCode == PICK_FROM_ALBUM2){
            img2.setImageURI(uri);
            d = img2.getDrawable();
            bitmap = ((BitmapDrawable)d).getBitmap();
            result = BitmapToString(bitmap);
            change_profile(2, result);
            Log.e("변화!!", "2222222222222");
        }
        else{
            img3.setImageURI(uri);
            d = img3.getDrawable();
            bitmap = ((BitmapDrawable)d).getBitmap();
            result = BitmapToString(bitmap);
            change_profile(3, result);
            Log.e("변화!!", "3333333333333");
        }
    }

    private void change_profile(int number, String result) {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService = retrofitClient.create(IMyService.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(iMyService.Change_profile(user_email, ""+number, result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap rbitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        rbitmap.compress(Bitmap.CompressFormat.PNG, 20, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
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

    @Override
    public void onItemSelected(View v, int position) {

    }

    @Override
    public void onItemLongSelected(View v, int position) {

    }
}