package com.example.project2;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Fragment2 extends Fragment implements ImageAdapter.OnListItemSelectedInterface, ImageAdapter.OnListItemLongSelectedInterface {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ImageView imageView;
    RecyclerView recyclerView;
    GridView gridView;
    GridLayoutManager gridLayoutManager;
    private GalleryManager mGalleryManager;
    ImageAdapter dataAdapter;

    public ArrayList<imgFormat> localPhotoList;
    static ArrayList<ImageUrl> imageUrlList = new ArrayList<>();


    public Fragment2() {
        // Required empty public constructor
    }

    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

        mGalleryManager = new GalleryManager(getActivity().getApplicationContext());
        localPhotoList = mGalleryManager.getAllPhotoPathList();
        dataAdapter = new ImageAdapter(getActivity().getApplicationContext(), imageUrlList, localPhotoList, this, this);
        recyclerView.setAdapter(dataAdapter);
/*
        imageView = (ImageView) v.findViewById(R.id.imageView);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView2);
        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        ArrayList imageUrlList = prepareData();
        mGalleryManager = new GalleryManager(getActivity().getApplicationContext());
        localPhotoList = mGalleryManager.getAllPhotoPathList();
        ImageAdapter dataAdapter = new ImageAdapter(getActivity().getApplicationContext(), imageUrlList, localPhotoList, this, this);
        recyclerView.setAdapter(dataAdapter);
*/
        return v;
    }

    private ArrayList prepareData() {
// here you should give your image URLs and that can be a link from the Internet
        String imageUrls[] = {
                "https://image.dongascience.com/Photo/2018/03/c4a9b9c58a79029437f7563bcc9d92e3.jpg"};
        ArrayList imageUrlList = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            ImageUrl imageUrl = new ImageUrl();
            imageUrl.setImageUrl(imageUrls[i]);
            imageUrlList.add(imageUrl);
        }
        Log.d("Fragment2", "List count: " + imageUrlList.size());
        return imageUrlList; // ArrayList : ImageUrl이 저장됨
    }

    @Override
    public void onItemSelected(View v, int position) {

    }

    @Override
    public void onItemLongSelected(View v, int position) {

    }
}