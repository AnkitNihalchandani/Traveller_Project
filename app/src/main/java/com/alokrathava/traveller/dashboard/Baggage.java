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
import com.alokrathava.traveller.databinding.ActivityBaggageBinding;
import com.alokrathava.traveller.network.Api;
import com.alokrathava.traveller.network.AppConfig;
import com.alokrathava.traveller.network.ServerResponse;
import com.alokrathava.traveller.utils.Config;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Baggage extends AppCompatActivity {

    private ActivityBaggageBinding binding;
    /*---------------------------------Variable Declaration----------------------------------------*/
    private static final String TAG = "Baggage";
    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawerLayout;
    NavigationView nav;
    private androidx.appcompat.widget.Toolbar mtoolbar;
    private Context context;
    private String TerminalNo, NoOfBags, TicketNo;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_baggage );
        binding = ActivityBaggageBinding.inflate ( getLayoutInflater ( ) );
        View view = binding.getRoot ( );
        setContentView ( view );

        mtoolbar = binding.toolbar;
        setSupportActionBar ( mtoolbar );

        nav           = binding.navmenu;
        mDrawerLayout = binding.drawer;

        binding.reminderbtn.setOnClickListener ( v -> {
            TerminalNo = binding.TermainalId.getText ( ).toString ( );
            NoOfBags   = binding.NoOfBags.getText ( ).toString ( );
            TicketNo   = binding.TicketNo.getText ( ).toString ( );
            remindMe ( TerminalNo , NoOfBags , TicketNo );
        } );

        mToggle = new ActionBarDrawerToggle ( this , mDrawerLayout , mtoolbar , R.string.Open , R.string.Close );
        mDrawerLayout.addDrawerListener ( mToggle );
        mToggle.syncState ( );
        nav.setNavigationItemSelectedListener ( item -> {

            switch (item.getItemId ( )) {

                case R.id.home:
                    Toast.makeText ( Baggage.this , "Home" , Toast.LENGTH_SHORT ).show ( );
                    mDrawerLayout.closeDrawer ( GravityCompat.START );
                    break;

                case R.id.air:
                    startActivity ( new Intent ( Baggage.this , AirTicketsWeb.class ) );
                    Config.showToast ( context , "Air" );
                    mDrawerLayout.closeDrawer ( (GravityCompat.START) );
                    break;
                case R.id.train:
                    Config.showToast ( context , "Train" );
                    startActivity ( new Intent ( Baggage.this , TrainTicketWeb.class ) );
                    mDrawerLayout.closeDrawer ( (GravityCompat.START) );
                    break;
                case R.id.history:
                    Toast.makeText ( Baggage.this , "History" , Toast.LENGTH_LONG ).show ( );
//                    Intent history = new Intent(Home.this,History.class);
//                    view.getContext().startActivity(history);
                    mDrawerLayout.closeDrawer ( GravityCompat.START );
                    break;

                case R.id.logout:
                    Toast.makeText ( Baggage.this , "Logout" , Toast.LENGTH_SHORT ).show ( );
                    LogOut ( );
                    mDrawerLayout.closeDrawer ( GravityCompat.START );

            }

            return true;
        } );


    }

    private void LogOut () {
        SessionManagement sessionManagement = new SessionManagement ( Baggage.this );
        sessionManagement.removeSession ( );
        MoveToLogin ( );
    }

    private void MoveToLogin () {
        startActivity ( new Intent ( Baggage.this , Login.class ) );
    }

    private void remindMe ( String terminalNo , String noOfBags , String ticketNo ) {
        /*---------------------------------Form Validation----------------------------------------*/
        if (TextUtils.isEmpty ( terminalNo ) && TextUtils.isEmpty ( noOfBags ) && TextUtils.isEmpty ( ticketNo )) {
            binding.TicketNo.setError ( "All Fields Are Required" );
            binding.NoOfBags.setError ( "All Fields Are Required" );
            binding.TermainalId.setError ( "All Fields Are Required" );
        }
        if (TextUtils.isDigitsOnly ( noOfBags )) {
            binding.TermainalId.setError ( "Numerical Value Only" );
        }
        executeBaggage ( terminalNo , noOfBags , ticketNo );
    }

    private void executeBaggage ( String terminalNo , String noOfBags , String ticketNo ) {
        Log.e ( TAG , terminalNo );
        Log.e ( TAG , noOfBags );
        Log.e ( TAG , ticketNo );

        Retrofit retrofit = AppConfig.getRetrofit ( );
        Api service = retrofit.create ( Api.class );

        Call<ServerResponse> call = service.baggage ( terminalNo , noOfBags , ticketNo );
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
                Config.showToast ( context , t.getMessage ( ) );
            }
        } );

    }
}