package com.example.project2.Retrofit;

import io.reactivex.Observable;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IMyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("password") String password,
                                    @Field("phone_number") String phone_number);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                 @Field("password") String password);

    @POST("facebook_login_check")
    @FormUrlEncoded
    Observable<String> facebookLogin(@Field("email") String email,
                                 @Field("name") String name);

    @POST("facebook_login_number_push")
    @FormUrlEncoded
    Observable<String> facebookRegister(@Field("email") String email,
                                     @Field("phone_number") String number,
                                    @Field("password") String password);

    @GET("print_all")
    Observable<String> getUser();
    /*
    @GET("print_all")
    Call<String> getUser();
    */
    @POST("getcontact")
    @FormUrlEncoded
    Observable<String> getContact(@Field("email") String email);
}
