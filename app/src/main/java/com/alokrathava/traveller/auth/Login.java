package com.alokrathava.traveller.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alokrathava.traveller.R;
import com.alokrathava.traveller.SessionManagement.SessionManagement;
import com.alokrathava.traveller.SessionManagement.User;
import com.alokrathava.traveller.dashboard.Dashboard;
import com.alokrathava.traveller.databinding.ActivityLoginBinding;
import com.alokrathava.traveller.network.Api;
import com.alokrathava.traveller.network.AppConfig;
import com.alokrathava.traveller.network.ServerResponse;
import com.alokrathava.traveller.utils.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity {

    private final Context context = this;
    private ActivityLoginBinding binding;
    /*--------------------------------Variable Declaration----------------------------------------*/


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );

        binding = ActivityLoginBinding.inflate ( getLayoutInflater ( ) );
        View view = binding.getRoot ( );
        setContentView ( view );

        binding.loginbtn.setOnClickListener ( v -> {
            Log.v ( "Login Button" , "Works" );
            String Email = binding.Email.getText ( ).toString ( );
            String Password = binding.Password.getText ( ).toString ( );

            if (TextUtils.isEmpty ( Email ) || TextUtils.isEmpty ( Password )) {
                binding.Email.setError ( "All Fields Are Required" );
                binding.Password.setError ( "All Fields Are Required" );
            }

            doLogin ( Email , Password );

        } );
        binding.forgotpassbtn.setOnClickListener ( v -> {
            startActivity ( new Intent ( Login.this , ForgotPass.class ) );
        } );

    }

    @Override
    protected void onStart () {
        super.onStart ( );
        CheckSession ( );
    }

    private void CheckSession () {
        //   Check if user logged in
        //   If yes then move to dashboard
        SessionManagement sessionManagement = new SessionManagement ( Login.this );
        int UsedId = sessionManagement.getSession ( );

        if (UsedId != -1) {
            MoveToActivity ( );
        } else {
            //   Do Nothing
        }
    }

    private void MoveToActivity () {
        startActivity ( new Intent ( Login.this , Dashboard.class ) );
    }

    private void doLogin ( String email , String password ) {

        Log.v ( "Email" , email );
        Log.v ( "password" , password );


        Retrofit retrofit = AppConfig.getRetrofit ( );
        Api service = retrofit.create ( Api.class );

        Call<ServerResponse> call = service.login ( email , password );
        call.enqueue ( new Callback<ServerResponse> ( ) {
            @Override
            public void onResponse ( Call<ServerResponse> call , Response<ServerResponse> response ) {

                if (response.body ( ) != null) {

                    ServerResponse serverResponse = response.body ( );

                    if (!serverResponse.getError ( )) {
                        Config.showToast ( context , serverResponse.getMessage ( ) );
                        Call<ServerResponse> data = service.login_data ( email , password );
                        data.enqueue ( new Callback<ServerResponse> ( ) {
                            @Override
                            public void onResponse ( Call<ServerResponse> call , Response<ServerResponse> response ) {
                                if (response.body ( ) != null) {
                                    ServerResponse serverResponse1 = response.body ( );
                                    Log.e ( "" , String.valueOf ( serverResponse1 ) );
                                }
                            }

                            @Override
                            public void onFailure ( Call<ServerResponse> call , Throwable t ) {

                            }
                        } );
                        User user = new User ( 1 , email );
                        SessionManagement sessionManagement = new SessionManagement ( Login.this );
                        sessionManagement.saveSession ( user );
                        MoveToActivity ( );
                    } else {
                        Config.showToast ( context , serverResponse.getMessage ( ) );

                    }
                }
            }

            @Override
            public void onFailure ( Call<ServerResponse> call , Throwable t ) {
                Config.showToast ( context , t.getMessage ( ) );
            }
        } );


    }
}