package com.alokrathava.traveller.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alokrathava.traveller.R;
import com.alokrathava.traveller.SessionManagement.SessionManagement;
import com.alokrathava.traveller.auth.Login;
import com.alokrathava.traveller.databinding.ActivityTaxiBookBinding;
import com.alokrathava.traveller.utils.Config;
import com.google.android.material.navigation.NavigationView;

public class TaxiBook extends AppCompatActivity {
    private static final String TAG = "Taxi BOOK";
    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawerLayout;
    NavigationView nav;
    private androidx.appcompat.widget.Toolbar mtoolbar;
    private Context context;
    ProgressBar pb_per;
    WebView mWebView;
    View view;
    private ActivityTaxiBookBinding binding;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_taxi_book );

        binding = ActivityTaxiBookBinding.inflate ( getLayoutInflater ( ) );
        View view = binding.getRoot ( );
        setContentView ( view );

        mtoolbar = binding.toolbar;
        setSupportActionBar ( mtoolbar );
        nav           = binding.navmenu;
        mDrawerLayout = binding.drawer;

        WebView myWebView = findViewById ( R.id.webview );
        myWebView.setWebViewClient ( new WebViewClient ( ) );
        myWebView.loadUrl ( "https://www.olacabs.com/" );

        WebSettings webSettings = myWebView.getSettings ( );
        webSettings.setJavaScriptEnabled ( true );

        mToggle = new ActionBarDrawerToggle ( this , mDrawerLayout , mtoolbar , R.string.Open , R.string.Close );
        mDrawerLayout.addDrawerListener ( mToggle );
        mToggle.syncState ( );
        nav.setNavigationItemSelectedListener ( item -> {

            switch (item.getItemId ( )) {

                case R.id.home:
                    startActivity ( new Intent ( TaxiBook.this , Dashboard.class ) );
                    Toast.makeText ( TaxiBook.this , "Home" , Toast.LENGTH_SHORT ).show ( );
                    mDrawerLayout.closeDrawer ( GravityCompat.START );
                    break;

                case R.id.air:
                    startActivity ( new Intent ( TaxiBook.this , AirTicketsWeb.class ) );
                    Config.showToast ( context , "Air" );
                    mDrawerLayout.closeDrawer ( (GravityCompat.START) );
                    break;
                case R.id.train:
                    Config.showToast ( context , "Train" );
                    startActivity ( new Intent ( TaxiBook.this , TrainTicketWeb.class ) );
                    mDrawerLayout.closeDrawer ( (GravityCompat.START) );
                    break;
                case R.id.history:
                    Toast.makeText ( TaxiBook.this , "History" , Toast.LENGTH_LONG ).show ( );
//                    Intent history = new Intent(Home.this,History.class);
//                    view.getContext().startActivity(history);
                    mDrawerLayout.closeDrawer ( GravityCompat.START );
                    break;

                case R.id.logout:
                    Toast.makeText ( TaxiBook.this , "Logout" , Toast.LENGTH_SHORT ).show ( );
                    LogOut ( );
                    mDrawerLayout.closeDrawer ( GravityCompat.START );

            }

            return true;
        } );

    }

    private void LogOut () {
        SessionManagement sessionManagement = new SessionManagement ( TaxiBook.this );
        sessionManagement.removeSession ( );
        MoveToLogin ( );
    }

    private void MoveToLogin () {
        startActivity ( new Intent ( TaxiBook.this , Login.class ) );
    }

    @Override
    public void onBackPressed () {
        if (mWebView.canGoBack ( )) {
            mWebView.goBack ( );
        } else {
            startActivity ( new Intent ( TaxiBook.this , Dashboard.class ) );
        }
    }


}