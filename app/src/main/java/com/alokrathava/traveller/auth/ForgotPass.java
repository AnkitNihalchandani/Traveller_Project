package com.alokrathava.traveller.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alokrathava.traveller.R;
import com.alokrathava.traveller.databinding.ActivityForgotPassBinding;

public class ForgotPass extends AppCompatActivity {

    private ActivityForgotPassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        binding = ActivityForgotPassBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.forgotpassbtnbtn.setOnClickListener(v -> {
            String Email = binding.Email.getText().toString();
            String NewPassword = binding.NewPassword.getText().toString();
            Log.v("Email", Email);
            Log.v("Password", NewPassword);

            doUpdatePass(Email, NewPassword);

        });


    }

    private void doUpdatePass(String email, String newPassword) {


    }
}