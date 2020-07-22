package com.example.project2;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Fragment1 extends Fragment{
    private ListView lv;
    String user_email;
    String user_name;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_ALBUM2 = 2;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ContactAdapter contactAdapter;

    static Retrofit retrofitClient;
    static IMyService iMyService;

    public Fragment1() {
        // Required empty public constructor

    }

    public static Fragment1 newinstance(String email, String name) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_email = getArguments().getString("email");
            user_name = getArguments().getString("name");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Fragment1","onCreateView");
        final View v = inflater.inflate(R.layout.fragment1, container, false);
        ArrayList<Person> phone_address= new ArrayList<>();
        contactAdapter = new ContactAdapter(getContext(), R.layout.contact_layout, phone_address);
        lv = (ListView) v.findViewById(R.id.list);
        lv.setAdapter(contactAdapter);

        Log.i("여기", "입장");
        retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(iMyService.getContact(user_email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++) {
                            String name = jsonArray.getJSONObject(i).getString("name");
                            String email = jsonArray.getJSONObject(i).getString("email");
                            String phone_number = jsonArray.getJSONObject(i).getString("phone_number");
                            Log.i("유저 정보", name + " / " + email + " / " + phone_number);
                            contactAdapter.getItems(new Person(name, email, phone_number, new Long(0)));
                        }
                        contactAdapter.notifyDataSetChanged();
                    }
                }));
        Log.i("End", "Game");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowID)
            {
                String email = ((Person)parent.getItemAtPosition(position)).getEmail();
                String phone_number = ((Person)parent.getItemAtPosition(position)).getNumber();
                CompositeDisposable compositeDisposable = new CompositeDisposable();
                compositeDisposable.add(iMyService.SendOrNot(email, user_email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) throws Exception {
                                if (response.equals("\"CAN\"")){ // 유저 등록되어있는 사람
                                    doSelectFriend((Person)parent.getItemAtPosition(position));
                                }
                                else{ // 이용자가 아님 -> 가입 권유
                                    Log.e("Clicked", "미가입사용자");
                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                                    View mView = getLayoutInflater().inflate(R.layout.popup_recommend, null);

                                    Button send = (Button) mView.findViewById(R.id.send);
                                    TextView txtText = (TextView)mView.findViewById(R.id.txtText);
                                    EditText msgText = (EditText)mView.findViewById(R.id.messageBox);
                                    txtText.setText(((Person) parent.getItemAtPosition(position)).getName()+" 님은 COMMA에 아직 가입을 안했습니다!\n같이 소통하자고 추천 SMS를 보내보시는건 어떤가요?");

                                    mBuilder.setView(mView);
                                    final AlertDialog dialog = mBuilder.create();
                                    dialog.show();

                                    send.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.d("clicked","button");
                                            Uri smsUri = Uri.parse("tel:" + phone_number);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                                            intent.putExtra("address", phone_number);
                                            intent.putExtra("sms_body", msgText.getText().toString()); // 메세지 내용 불러와서 sms 전달
                                            intent.setType("vnd.android-dir/mms-sms");
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        }));

            }});

        ImageButton newContact = (ImageButton) v.findViewById(R.id.newContact); // ========= 연락처 추가
        newContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPostIntent = new Intent(v.getContext(), NewContact.class);
                newPostIntent.putExtra("user_email", user_email);
                startActivity(newPostIntent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_up);

            }
        });

        ImageButton delete_Contact = (ImageButton) v.findViewById(R.id.delete_Contact); // ========= 연락처 제거
        delete_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPostIntent = new Intent(v.getContext(), delete_Contact.class);
                newPostIntent.putExtra("user_email", user_email);
                startActivity(newPostIntent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_up);
            }
        });

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("DEBUG", "onResume of HomeFragment");
        refreshList();
    }

    public void refreshList(){
        Log.i("여기", "입장");
        //Retrofit retrofitClient = RetrofitClient.getInstance();
        //IMyService iMyService = retrofitClient.create(IMyService.class);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(iMyService.getContact(user_email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        contactAdapter.clear();
                        JSONArray jsonArray = new JSONArray(response);
                        if(jsonArray.length()==0) getView().findViewById(R.id.nothingIcon).setVisibility(getView().VISIBLE);
                        else getView().findViewById(R.id.nothingIcon).setVisibility(getView().INVISIBLE);
                        for (int i=0; i<jsonArray.length(); i++) {
                            String name = jsonArray.getJSONObject(i).getString("name");
                            String email = jsonArray.getJSONObject(i).getString("email");
                            String phone_number = jsonArray.getJSONObject(i).getString("phone_number");
                            Log.i("유저 정보", name + " / " + email + " / " + phone_number);
                            contactAdapter.getItems(new Person(name, email, phone_number, new Long(0)));
                        }
                        contactAdapter.notifyDataSetChanged();
                    }
                }));

        Log.i("End", "Game");
    }

    public void doSelectFriend(Person p) {
        Log.d("Clicked", p.getName() + ", " + p.getNumber() + ", " + p.getEmail());
        //Intent intent = new Intent(getContext(), PopupActivity.class);
        //intent.putExtra("user", p.getName());
        //intent.putExtra("data", "Test Popup");
        //startActivityForResult(intent, 1);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.popup_user, null);
        Button send = (Button) mView.findViewById(R.id.send);
        TextView txtText = (TextView)mView.findViewById(R.id.txtText);
        TextView user = (TextView)mView.findViewById(R.id.userTo);
        EditText msgText = (EditText)mView.findViewById(R.id.messageBox);
        //txtText.setText("익명으로 전달됩니다");
        user.setText("To. "+p.getName());

        img1 = mView.findViewById(R.id.img1);
        img2 = mView.findViewById(R.id.img2);
        img3 = mView.findViewById(R.id.img3);

        set_profile(p.getEmail());

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clicked","button");
                if(msgText.getText().length()<3){ // 메세지 길이가 너무 짧거나 비어있으면
                    Toast.makeText(getContext(), "메세지를 좀 더 길게 써주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    // Send 버튼 눌렀을 때 서버로 저장하는 코드
                    String email = p.getEmail();
                    String msg = msgText.getText().toString();

                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                    compositeDisposable.add(iMyService.SendMsg(email, user_name, user_email, msg)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String response) throws Exception {
                                    Log.e("Send", "" + response);
                                    if (response.equals("\"1\"")){
                                        Toast.makeText(getContext(), "축하합니다! 두분이 서로 호감을 표현했습니다!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getContext(), "익명으로 " + p.getName() + "님께 메세지가 전송되었습니다", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss(); // 팝업 종료
                                }
                            }));

                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //super.onActivityResult(requestCode, resultCode, data);
        Log.e("변화!!", "1->fragment1");
    }

    private void set_profile(String emaillookingfor) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(iMyService.Get_profile(emaillookingfor+"_profile")
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