package com.example.project2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.project2.Retrofit.IMyService;
import com.example.project2.Retrofit.RetrofitClient;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    String TAG = "LoginActivity";
    String uid="", pw="";
    EditText user_emailET,passwordET;
    ImageView circleImageView;

    private CallbackManager callbackManager;

    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;

    TextView signup;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    //== user info ========
    static String name="", email="", profileImg="";
    //=====================

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AccessToken.getCurrentAccessToken()!=null){
            Log.d("GotoMain", "login status-true");
            Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
            loginIntent.putExtra("UserName", name);
            loginIntent.putExtra("UserEmail", email);
            startActivity(loginIntent);
        }

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);
        //setContentView(R.layout.neumorph);

        data=new ArrayList<String>();
        user_emailET=(EditText) findViewById(R.id.user_email);
        passwordET=(EditText) findViewById(R.id.password);

        callbackManager = CallbackManager.Factory.create();

        if(getIntent().getExtras() != null){
            EditText user_email = (EditText)findViewById(R.id.user_email);
            Intent signupIntent = getIntent();
            user_email.setText(signupIntent.getStringExtra("User_email"));
        }

        TextView login = (TextView) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(user_emailET.getText().toString(),
                        passwordET.getText().toString());
            }
        });

        //checkLoginStatus(); // 이미 로그인 되어있는 상태인지 체크 -> 바로 main activity

        signup = (TextView)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View register_layout = LayoutInflater.from(LoginActivity.this)
                        .inflate(R.layout.register_layout, null);

                new MaterialStyledDialog.Builder(LoginActivity.this)
                        .setIcon(R.drawable.ic_user)
                        .setTitle("REGISTRATION")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
                        .setHeaderColor(R.color.color1)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("REGISTER")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText edt_register_email = (MaterialEditText)register_layout.findViewById(R.id.edt_email);
                                MaterialEditText edt_register_name = (MaterialEditText)register_layout.findViewById(R.id.edt_name);
                                MaterialEditText edt_register_password = (MaterialEditText)register_layout.findViewById(R.id.edt_password);
                                MaterialEditText edt_phone_number = (MaterialEditText)register_layout.findViewById(R.id.edt_phone_number);
                                if (TextUtils.isEmpty(edt_register_email.getText().toString()))
                                {
                                    Log.d("MainActivity", "email!");
                                    Toast.makeText(LoginActivity.this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(edt_register_name.getText().toString()))
                                {
                                    Toast.makeText(LoginActivity.this, "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(edt_register_password.getText().toString()))
                                {
                                    Toast.makeText(LoginActivity.this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(edt_phone_number.getText().toString()))
                                {
                                    Toast.makeText(LoginActivity.this, "Phone number cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                registerUser(edt_register_email.getText().toString(),
                                        edt_register_name.getText().toString(),
                                        edt_register_password.getText().toString(),
                                        edt_phone_number.getText().toString());
                            }
                        }).show();
            }
        });

        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_loginBtn);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        /*
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }
            @Override
            public void onCancel() {
                Log.d("LoginCancel","login canceled");
            }
            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr",error.toString());
            }
        });
        */

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity Response ", response.toString());
                        try {
                            name = object.getString("name");
                            email = object.getString("email");
                            profileImg = "https://graph.facebook.com/"+object.getString("id")+"/picture?type=normal";
                            Log.e("페이스북 정보", ""+object);
                            Log.v("Name = ", " " + name);
                            Log.v("Email = ", " " + email);
                            Log.v("ProfileImg = ", " " + profileImg);

                            compositeDisposable.add(iMyService.facebookLogin(email, name)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<String>() {
                                        @Override
                                        public void accept(String response) throws Exception {
                                            String[] res_list = response.split("/");
                                            if (res_list[0].equals("\"Ok")) { //이미 등록된 페북 계정이면
                                                String[] res_list2 = res_list[1].split("\"");
                                                Toast.makeText(LoginActivity.this, "Welcome "+res_list2[0], Toast.LENGTH_LONG).show();

                                                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                Log.d("GotoMain", "already registered fb "+name+" "+email+" "+profileImg);
                                                loginIntent.putExtra("UserName", name+"(Facebook)");
                                                loginIntent.putExtra("UserEmail", email);
                                                loginIntent.putExtra("profileImgURL", profileImg);
                                                //loginIntent.putExtra("UserPhoto", profileImg);
                                                startActivity(loginIntent);
                                                //finish();
                                            }
                                            else{
                                                //폰번호 만들기
                                                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();

                                                final View register_layout = LayoutInflater.from(LoginActivity.this)
                                                        .inflate(R.layout.fb_register_layout, null);

                                                new MaterialStyledDialog.Builder(LoginActivity.this)
                                                        .setIcon(R.drawable.ic_user)
                                                        .setTitle("REGISTRATION")
                                                        .setDescription("Please enter your phone number")
                                                        .setCustomView(register_layout)
                                                        .setNegativeText("CANCEL")
                                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .setPositiveText("REGISTER")
                                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                            //Log.d("Dialog","created");
                                                            @Override
                                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                Log.d("GotoMain", "in");
                                                                MaterialEditText edt_phone_number = (MaterialEditText)register_layout.findViewById(R.id.edt_phone_number);
                                                                MaterialEditText edt_register_password = (MaterialEditText)register_layout.findViewById(R.id.edt_password);
                                                                if (TextUtils.isEmpty(edt_phone_number.getText().toString()))
                                                                {
                                                                    Toast.makeText(LoginActivity.this, "Phone number cannot be null or empty", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                                compositeDisposable.add(iMyService.facebookRegister(email, edt_phone_number.getText().toString(), edt_register_password.getText().toString())
                                                                        .subscribeOn(Schedulers.io())
                                                                        .observeOn(AndroidSchedulers.mainThread())
                                                                        .subscribe(new Consumer<String>() {
                                                                            @Override
                                                                            public void accept(String response) throws Exception {
                                                                                Log.d("GotoMain", "new register and login fb "+ name + email);
                                                                                Toast.makeText(LoginActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                                                                                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                                                loginIntent.putExtra("UserName", name+"(Facebook)");
                                                                                loginIntent.putExtra("UserEmail", email);
                                                                                loginIntent.putExtra("profileImgURL", profileImg);
                                                                                startActivity(loginIntent);
                                                                                //finish();
                                                                            }
                                                                        }));
                                                            }
                                                        }).show();
                                            }
                                        }
                                    }));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                Log.d("LoginCancel","login canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr",error.toString());
            }
        });
        Profile.getCurrentProfile();
    }

    private void registerUser(String email, String name, String password, String phone_number) {
        compositeDisposable.add(iMyService.registerUser(email, name, password, phone_number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(LoginActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void loginUser(final String email, final String password) {
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(iMyService.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        String[] res_list = response.split("/");
                        if (res_list[0].equals("\"Login success")) {
                            String[] res_list2 = res_list[1].split("\"");
                            LoginSuccess(email, res_list2[0]);
                            Toast.makeText(LoginActivity.this, "Welcome "+res_list2[0] +"!!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                }));

    }

    private void LoginSuccess(String email, String name) {
        Log.e("################", "Accesss!!!!!!!!!!!!!");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("UserEmail", email);
        intent.putExtra("UserName", name);
        intent.putExtra("profileImgURL", "local");
        startActivity(intent);  // main 화면으로 이동

        finish(); // login 화면은 종료..
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("변화!!!", "Login Activity!!!!");
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                // null 상황
                Log.d("LoginActivity","currentAccessToken == null");
            }
            else{
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try{
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/"+id+"/picture?type=normal";

                    Log.d("userData",first_name);
                    Log.d("userData",last_name);
                    Log.d("userData",id);
                    Log.d("userData",email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkLoginStatus(){
        Log.d("LoginActivity", "startcheck login status");
        if(AccessToken.isCurrentAccessTokenActive()){
            Log.d("LoginActivity", "active");
        }
        if(AccessToken.getCurrentAccessToken()!=null){
            Log.d("LoginActivity", "already login");
            Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
            Log.d("IntentLog", "string - "+name+email+profileImg);
            loginIntent.putExtra("UserName", name+"(Facebook)");
            loginIntent.putExtra("UserEmail", email);
            startActivity(loginIntent);
        }
    }

}