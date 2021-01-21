package com.alokrathava.traveller.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {


    String my_url = "Api.php?apicall=";

    @FormUrlEncoded
    @POST(my_url + "login")
    Call<ServerResponse> login (
            @Field("email") String mobile_no ,
            @Field("pwd") String password
    );

    @FormUrlEncoded
    @POST(my_url + "register")
    Call<ServerResponse> register (
            @Field("name") String mName ,
            @Field("email") String mEmail ,
            @Field("pwd") String mPwd ,
            @Field("mobile") String mphone

    );

    @FormUrlEncoded
    @POST(my_url + "newtrip")
    Call<ServerResponse> newtrip (
            @Field("name") String mName ,
            @Field("email") String mEmail ,
            @Field("source") String mSource ,
            @Field("destination") String mDestination ,
            @Field("date") String mDate

    );

    @FormUrlEncoded
    @POST(my_url + "forgotpass")
    Call<ServerResponse> forgotpass (
            @Field("email") String mEmail
    );

    @FormUrlEncoded
    @POST(my_url + "login_data")
    Call<ServerResponse> login_data (
            @Field("pwd") String mPwd ,
            @Field("email") String mEmail
    );
}
