package com.alokrathava.traveller.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alokrathava.traveller.R;
import com.alokrathava.traveller.SessionManagement.SessionManagement;
import com.alokrathava.traveller.auth.Login;
import com.alokrathava.traveller.databinding.ActivityDocWalletBinding;
import com.alokrathava.traveller.utils.Config;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class DocWallet extends AppCompatActivity {

    private ActivityDocWalletBinding binding;
    private static final String TAG = "DocWallet";
    private final int REQUEST_CODE_PERMISSION = 101;
    private final String[] REQUIRED_PERMISSION = new String[]{"android.permission.CAMERA" , "android.permission.WRITE_EXTERNAL_STORAGE"};
    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawerLayout;
    NavigationView nav;
    private androidx.appcompat.widget.Toolbar mtoolbar;
    private Context context;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_doc_wallet );
        binding = ActivityDocWalletBinding.inflate ( getLayoutInflater ( ) );
        View view = binding.getRoot ( );
        setContentView ( view );

        mtoolbar = binding.toolbar;
        setSupportActionBar ( mtoolbar );
        nav           = binding.navmenu;
        mDrawerLayout = binding.drawer;

        mToggle = new ActionBarDrawerToggle ( this , mDrawerLayout , mtoolbar , R.string.Open , R.string.Close );
        mDrawerLayout.addDrawerListener ( mToggle );
        mToggle.syncState ( );
        nav.setNavigationItemSelectedListener ( item -> {

            switch (item.getItemId ( )) {

                case R.id.home:
                    startActivity ( new Intent ( DocWallet.this , Dashboard.class ) );
                    Toast.makeText ( DocWallet.this , "Home" , Toast.LENGTH_SHORT ).show ( );
                    mDrawerLayout.closeDrawer ( GravityCompat.START );
                    break;

                case R.id.air:
                    startActivity ( new Intent ( DocWallet.this , AirTicketsWeb.class ) );
                    Config.showToast ( context , "Air" );
                    mDrawerLayout.closeDrawer ( (GravityCompat.START) );
                    break;
                case R.id.train:
                    Config.showToast ( context , "Train" );
                    startActivity ( new Intent ( DocWallet.this , TrainTicketWeb.class ) );
                    mDrawerLayout.closeDrawer ( (GravityCompat.START) );
                    break;
                case R.id.logout:
                    Toast.makeText ( DocWallet.this , "Logout" , Toast.LENGTH_SHORT ).show ( );
                    LogOut ( );
                    mDrawerLayout.closeDrawer ( GravityCompat.START );

            }

            return true;
        } );


        if (allPermissionGranted ( )) {
            startCamera ( );
        } else {
            ActivityCompat.requestPermissions ( this , REQUIRED_PERMISSION , REQUEST_CODE_PERMISSION );
        }


    }

    private void LogOut () {
        SessionManagement sessionManagement = new SessionManagement ( DocWallet.this );
        sessionManagement.removeSession ( );
        MoveToLogin ( );
    }

    private void MoveToLogin () {
        startActivity ( new Intent ( DocWallet.this , Login.class ) );
    }

    private void startCamera () {
        CameraX.unbindAll ( );
        Rational aspectRatio = new Rational ( binding.viewFinder.getWidth ( ) , binding.viewFinder.getHeight ( ) );
        Size screen = new Size ( binding.viewFinder.getWidth ( ) , binding.viewFinder.getHeight ( ) );

        PreviewConfig previewConfig = new PreviewConfig.Builder ( ).setTargetAspectRatio ( aspectRatio ).setTargetResolution ( screen ).build ( );
        Preview preview = new Preview ( previewConfig );
        preview.setOnPreviewOutputUpdateListener (
                output -> {
                    ViewGroup parent = (ViewGroup) binding.viewFinder.getParent ( );
                    parent.removeView ( binding.viewFinder );
                    parent.addView ( binding.viewFinder );

                    binding.viewFinder.setSurfaceTexture ( output.getSurfaceTexture ( ) );
                    updateTransform ( );

                }
        );

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder ( ).setCaptureMode ( ImageCapture.CaptureMode.MIN_LATENCY ).
                setTargetRotation ( getWindowManager ( ).getDefaultDisplay ( ).getRotation ( ) ).build ( );

        final ImageCapture imageCapture = new ImageCapture ( imageCaptureConfig );
        binding.imgCapture.setOnClickListener ( v -> {

            File file = new File ( "sdcard/photos/DCIM(0)/Camera/CameraX_" + System.currentTimeMillis ( ) );
            imageCapture.takePicture ( file , new ImageCapture.OnImageSavedListener ( ) {
                @Override
                public void onImageSaved ( @NonNull File file ) {
                    String msg = "Image Capture" + file.getAbsolutePath ( );
                    Toast.makeText ( DocWallet.this , msg , Toast.LENGTH_SHORT ).show ( );
                }

                @Override
                public void onError ( @NonNull ImageCapture.UseCaseError useCaseError , @NonNull String message , @Nullable Throwable cause ) {
                    String msg = "Capturing Image Failed" + message;
                    Toast.makeText ( DocWallet.this , msg , Toast.LENGTH_SHORT ).show ( );
                    if (cause != null) {
                        cause.printStackTrace ( );
                    }
                }
            } );
        } );
        CameraX.bindToLifecycle ( this , preview , imageCapture );
    }

    private void updateTransform () {
        Matrix mx = new Matrix ( );
        float w = binding.viewFinder.getMeasuredWidth ( );
        float h = binding.viewFinder.getMeasuredHeight ( );

        float cx = w / 2f;
        float cy = w / 2f;

        int rotationDgr;
        int rotation = (int) binding.viewFinder.getRotation ( );

        switch (rotation) {
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }
        mx.postRotate ( (float) rotationDgr , cx , cy );
        binding.viewFinder.setTransform ( mx );
    }

    private boolean allPermissionGranted () {
        for (String permission : REQUIRED_PERMISSION) {
            if (ContextCompat.checkSelfPermission ( this , permission ) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}