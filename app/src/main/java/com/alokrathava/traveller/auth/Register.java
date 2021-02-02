package com.alokrathava.traveller.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alokrathava.traveller.R;
import com.alokrathava.traveller.databinding.ActivityRegisterBinding;
import com.alokrathava.traveller.network.Api;
import com.alokrathava.traveller.network.AppConfig;
import com.alokrathava.traveller.network.ServerResponse;
import com.alokrathava.traveller.utils.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register extends AppCompatActivity {

    private final Context context = this;
    private ActivityRegisterBinding mBinding;

    //    private FirebaseAuth mAuth;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_register );

        mBinding = ActivityRegisterBinding.inflate ( getLayoutInflater ( ) );
        View view = mBinding.getRoot ( );
        setContentView ( view );
//        mAuth = FirebaseAuth.getInstance();

        mBinding.registerbtn.setOnClickListener ( v -> {
            String Name = mBinding.Name.getText ( ).toString ( );
            String Email = mBinding.Email.getText ( ).toString ( );
            String Password = mBinding.Password.getText ( ).toString ( );
            String Phone = mBinding.Phone.getText ( ).toString ( );

            if (TextUtils.isEmpty ( Name ) || TextUtils.isEmpty ( Email ) || TextUtils.isEmpty ( Password ) || TextUtils.isEmpty ( Phone )) {
                mBinding.Name.setError ( "All Fields Are Required" );
                mBinding.Email.setError ( "" );
                mBinding.Password.setError ( "" );
                mBinding.Phone.setError ( "" );
            }
            doRegister ( Name , Email , Password , Phone );
        } );

    }

    private void doRegister ( String name , String email , String password , String phone ) {

        Retrofit retrofit = AppConfig.getRetrofit ( );
        Api service = retrofit.create ( Api.class );

        Call<ServerResponse> call = service.register ( name , email , password , phone );
        call.enqueue ( new Callback<ServerResponse> ( ) {
            @Override
            public void onResponse ( Call<ServerResponse> call , Response<ServerResponse> response ) {

                if (response.body ( ) != null) {
                    ServerResponse serverResponse = response.body ( );
                    if (!serverResponse.getError ( )) {
                        Config.showToast ( context , serverResponse.getMessage ( ) );
                        Intent home = new Intent ( Register.this , Login.class );
                        startActivity ( home );
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