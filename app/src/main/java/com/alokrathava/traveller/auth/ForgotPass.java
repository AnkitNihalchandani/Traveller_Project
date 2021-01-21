package com.alokrathava.traveller.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alokrathava.traveller.R;
import com.alokrathava.traveller.databinding.ActivityForgotPassBinding;
import com.alokrathava.traveller.network.Api;
import com.alokrathava.traveller.network.AppConfig;
import com.alokrathava.traveller.network.ServerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForgotPass extends AppCompatActivity {

    private ActivityForgotPassBinding binding;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_forgot_pass );

        binding = ActivityForgotPassBinding.inflate ( getLayoutInflater ( ) );
        View view = binding.getRoot ( );
        setContentView ( view );

        binding.forgotpassbtnbtn.setOnClickListener ( v -> {
            String Email = binding.Email.getText ( ).toString ( );
            Log.v ( "Email" , Email );

            doUpdatePass ( Email );

        } );


    }

    private void doUpdatePass ( String email ) {

        Retrofit retrofit = AppConfig.getRetrofit ( );
        Api service = retrofit.create ( Api.class );

        Call<ServerResponse> call = service.forgotpass ( email );
        call.enqueue ( new Callback<ServerResponse> ( ) {
            @Override
            public void onResponse ( Call<ServerResponse> call , Response<ServerResponse> response ) {

            }

            @Override
            public void onFailure ( Call<ServerResponse> call , Throwable t ) {

            }
        } );


    }
}