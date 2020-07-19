package com.example.project2;

import android.content.Intent;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        data=new ArrayList<String>();
        user_emailET=(EditText) findViewById(R.id.user_email);
        passwordET=(EditText) findViewById(R.id.password);
        circleImageView = findViewById(R.id.circleImageView);

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
                                    Log.d("MainActivity", "email!!!!");
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


        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        checkLoginStatus();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.v("LoginActivity Response ", response.toString());

                                try {
                                    name = object.getString("name");
                                    email = object.getString("email");
                                    Log.e("페이스북 정보", ""+object);
                                    Log.v("Name = ", " " + name);
                                    Log.v("Email = ", " " + email);
                                    Toast.makeText(getApplicationContext(), "Name " + name, Toast.LENGTH_SHORT).show();

                                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    Log.d("IntentLog", "string - "+name+email+profileImg);
                                    loginIntent.putExtra("UserName", name+"(Facebook)");
                                    loginIntent.putExtra("UserEmail", email);
                                    //loginIntent.putExtra("UserPhoto", profileImg);
                                    startActivity(loginIntent);
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
        startActivity(intent);  // main 화면으로 이동

        finish(); // login 화면은 종료..
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                // null 상황
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
                    user_emailET.setText(email);
                    passwordET.setText(first_name);
                    //RequestOptions requestOptions = new RequestOptions();
                    //requestOptions.dontAnimate();
                    Glide.with(LoginActivity.this).load(image_url).into(circleImageView);
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
        if(AccessToken.getCurrentAccessToken()!=null){
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }

}