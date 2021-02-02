package com.alokrathava.traveller.dashboard;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alokrathava.traveller.R;
import com.alokrathava.traveller.databinding.ActivityBaggageBinding;

public class Baggage extends AppCompatActivity {

    private ActivityBaggageBinding binding;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_baggage );

        binding = ActivityBaggageBinding.inflate ( getLayoutInflater ( ) );
        View view = binding.getRoot ( );
        setContentView ( view );


    }
}