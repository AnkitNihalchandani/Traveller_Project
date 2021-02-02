package com.alokrathava.traveller.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alokrathava.traveller.R;
import com.alokrathava.traveller.SessionManagement.SessionManagement;
import com.alokrathava.traveller.auth.Login;
import com.alokrathava.traveller.databinding.ActivityDashboardBinding;
import com.alokrathava.traveller.network.Api;
import com.alokrathava.traveller.network.AppConfig;
import com.alokrathava.traveller.network.ServerResponse;
import com.alokrathava.traveller.utils.Config;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Dashboard extends AppCompatActivity {

    private final Context context = this;
    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawerLayout;
    NavigationView nav;
    private ActivityDashboardBinding binding;
    private androidx.appcompat.widget.Toolbar mtoolbar;
    /*------------------------------------Variable Declaration-------------------------------------*/
    private String TripDate;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_dashboard );
        binding = ActivityDashboardBinding.inflate ( getLayoutInflater ( ) );
        View view = binding.getRoot ( );
        setContentView ( view );

        mtoolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( mtoolbar );

        nav           = findViewById ( R.id.navmenu );
        mDrawerLayout = findViewById ( R.id.drawer );


        mToggle = new ActionBarDrawerToggle ( this , mDrawerLayout , mtoolbar , R.string.Open , R.string.Close );
        mDrawerLayout.addDrawerListener ( mToggle );
        mToggle.syncState ( );

        nav.setNavigationItemSelectedListener ( item -> {

            switch (item.getItemId ( )) {

                case R.id.home:
                    Toast.makeText ( Dashboard.this , "Home" , Toast.LENGTH_SHORT ).show ( );
                    mDrawerLayout.closeDrawer ( GravityCompat.START );
                    break;

                case R.id.air:
                    startActivity ( new Intent ( Dashboard.this , AirTicketsWeb.class ) );
                    Config.showToast ( context , "Air" );
                    mDrawerLayout.closeDrawer ( (GravityCompat.START) );
                    break;
                case R.id.train:
                    Config.showToast ( context , "Train" );
                    startActivity ( new Intent ( Dashboard.this , TrainTicketWeb.class ) );
                    mDrawerLayout.closeDrawer ( (GravityCompat.START) );
                    break;
                case R.id.history:
                    Toast.makeText ( Dashboard.this , "History" , Toast.LENGTH_LONG ).show ( );
//                    Intent history = new Intent(Home.this,History.class);
//                    view.getContext().startActivity(history);
                    mDrawerLayout.closeDrawer ( GravityCompat.START );
                    break;

                case R.id.logout:
                    Toast.makeText ( Dashboard.this , "Logout" , Toast.LENGTH_SHORT ).show ( );
                    LogOut ( );
                    mDrawerLayout.closeDrawer ( GravityCompat.START );

            }

            return true;
        } );

        /*--------------------------------------Material Date Picker-------------------------------*/
        binding.selectdate.setOnClickListener ( v -> {
            MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker ( );
            materialDateBuilder.setTitleText ( "Start Date" );
            final MaterialDatePicker materialDatePicker = materialDateBuilder.build ( );
            materialDatePicker.show ( getSupportFragmentManager ( ) , "MATERIAL_DATE_PICKER" );
            materialDatePicker.addOnPositiveButtonClickListener (
                    selection -> {
                        SimpleDateFormat spf = new SimpleDateFormat ( "MMM dd, yyyy" );
                        Date newDate = null;
                        try {
                            newDate = spf.parse ( materialDatePicker.getHeaderText ( ) );
                        } catch ( ParseException e ) {
                            e.printStackTrace ( );
                        }
                        spf      = new SimpleDateFormat ( "dd/MM/yyyy" );
                        TripDate = spf.format ( newDate );
                        binding.selectdate.setText ( TripDate );
                    } );

        } );
        /*--------------------------------------Buttons-------------------------------------------*/
        binding.BagInfobtn.setOnClickListener ( v -> {
            startActivity ( new Intent ( Dashboard.this , Baggage.class ) );
            Log.e ( "" , "BagBtn" );
        } );

        binding.Docbtn.setOnClickListener ( v -> {
            startActivity ( new Intent ( Dashboard.this , DocWallet.class ) );
            Log.e ( "" , "BagBtn" );
        } );
        binding.taxibtn.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                startActivity ( new Intent ( Dashboard.this , TaxiBook.class ) );
            }
        } );

        binding.addnewtrip.setOnClickListener ( v -> {
            newTrip ( );
        } );


    }

    private void newTrip () {
        String Name = binding.name.getText ( ).toString ( );
        String Email = binding.email.getText ( ).toString ( );
        String Source = binding.Source.getText ( ).toString ( );
        String Destination = binding.Destination.getText ( ).toString ( );

        if (!(!TextUtils.isEmpty ( Name ) && !TextUtils.isEmpty ( Email ) && !TextUtils.isEmpty ( Source ) && !TextUtils.isEmpty ( Destination ) && !TextUtils.isEmpty ( TripDate ))) {
            binding.name.setError ( "All fields are required!!" );
            binding.email.setError ( "All fields are required!!" );
            binding.Source.setError ( "All fields are required!!" );
            binding.Destination.setError ( "All fields are required!!" );
        }

        Retrofit retrofit = AppConfig.getRetrofit ( );
        Api service = retrofit.create ( Api.class );
        Call<ServerResponse> call = service.newtrip ( Name , Email , Source , Destination , TripDate );
        call.enqueue ( new Callback<ServerResponse> ( ) {
            @Override
            public void onResponse ( Call<ServerResponse> call , Response<ServerResponse> response ) {
                if (response.body ( ) != null) {
                    ServerResponse serverResponse = response.body ( );
                    if (!serverResponse.getError ( )) {
                        Config.showToast ( context , serverResponse.getMessage ( ) );
                    } else {
                        Config.showToast ( context , serverResponse.getMessage ( ) );

                    }

                }
            }

            @Override
            public void onFailure ( Call<ServerResponse> call , Throwable t ) {

            }
        } );

    }

    private void LogOut () {
        SessionManagement sessionManagement = new SessionManagement ( Dashboard.this );
        sessionManagement.removeSession ( );
        MoveToLogin ( );
    }

    private void MoveToLogin () {
        startActivity ( new Intent ( Dashboard.this , Login.class ) );
    }
}