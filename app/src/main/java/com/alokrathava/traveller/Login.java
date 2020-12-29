package com.alokrathava.traveller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alokrathava.traveller.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    private void updateUI(FirebaseUser currentUser) {
        Intent dash = new Intent(Login.this,Dashboard.class);
        startActivity(dash);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        binding.loginbtn.setOnClickListener(v -> {
            Log.v("Login Button","Works");
            String Email = binding.Email.getText().toString();
            String Password = binding.Password.getText().toString();

            if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
                binding.Email.setError("All Fields Are Required");
                binding.Password.setError("All Fields Are Required");
            }

            doLogin(Email,Password);

        });
        binding.forgotpassbtn.setOnClickListener(v -> {
            Log.v("ForgotPassBtn", "Works");
            Intent fgtpass= new Intent(Login.this,ForgotPass.class);
            startActivity(fgtpass);
        });

    }

    private void doLogin(String email, String password) {

        Log.v("Email",email);
        Log.v("password",password);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}