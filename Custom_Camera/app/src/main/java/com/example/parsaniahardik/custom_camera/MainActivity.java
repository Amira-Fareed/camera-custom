package com.example.parsaniahardik.custom_camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    public static Bitmap bitmap;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        buttons_listenner();
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
        ImageView iv = (ImageView) findViewById(R.id.iv);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;

        mCamera =  Camera.open();
        mCamera.setDisplayOrientation(90);
        cameraPreview = (LinearLayout) findViewById(R.id.cPreview);
        mPreview = new CameraPreview(myContext, mCamera,iv);
        cameraPreview.addView(mPreview);



    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }}//end onRequestPermissionsResult

    public void buttons_listenner() {
        ToggleButton car_toggle = (ToggleButton) findViewById(R.id.car_icon);
        ToggleButton lane_toggle = (ToggleButton) findViewById(R.id.lane_icon);
        ToggleButton ped_toggle = (ToggleButton) findViewById(R.id.ped_icon);
        car_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Toast.makeText(myContext, "car enabled", Toast.LENGTH_LONG).show();
                    mPreview.c = true;
                } else {
                    // The toggle is disabled
                    Toast.makeText(myContext, "car disabled", Toast.LENGTH_LONG).show();
                    mPreview.c = false;
                }
            }
        });

        lane_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Toast.makeText(myContext, "lane enabled", Toast.LENGTH_LONG).show();
                    mPreview.l = true;
                } else {
                    // The toggle is disabled
                    Toast.makeText(myContext, "lane disabled", Toast.LENGTH_LONG).show();
                    mPreview.l = false;
                }
            }
        });

        ped_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Toast.makeText(myContext, "ped enabled", Toast.LENGTH_LONG).show();
                    mPreview.p = true;
                } else {
                    // The toggle is disabled
                    Toast.makeText(myContext, "ped disabled", Toast.LENGTH_LONG).show();
                    mPreview.p = false;
                }
            }
        });

    }

}
