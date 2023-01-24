package com.ovot.fiveStarapp_test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    ImageView jobBTN,logOutBTN,productBTN,incentiveBTN,profileBTN;
    private static final String PREF_NAME = "AndroidStarPref";

    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasgboard);



        getLocationPermission();


        jobBTN = findViewById(R.id.my_job);
        logOutBTN = findViewById(R.id.logout_img);
        productBTN=findViewById(R.id.product_btn);
        incentiveBTN = findViewById(R.id.incentive);
        profileBTN = findViewById(R.id.my_profile);

        builder = new AlertDialog.Builder(this);

        jobBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, MyAllottedJob.class);
                startActivity(intent);
            }
        });

//        jobBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DashboardActivity.this, JobCompletion.class);
//                startActivity(intent);
//            }
//        });


        incentiveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent incentiveIntent = new Intent(DashboardActivity.this,IncentiveActvity.class);
                startActivity(incentiveIntent);

            }
        });



        profileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profileIntent = new Intent(DashboardActivity.this,MyProfile.class);
                startActivity(profileIntent);

            }
        });
//
//        productBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(DashboardActivity.this, WorkEntryForm.class);
//                startActivity(intent);
//            }
//        });

        logOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setTitle("Alert..");

               builder
                        .setMessage("Do you want logout the app.??")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                                Toast.makeText(DashboardActivity.this, "done..", Toast.LENGTH_SHORT).show();

                                SharedPreferences preferences =getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                finish();

                                Intent log_intent = new Intent(DashboardActivity.this,LoginActivity.class);
                                startActivity(log_intent);
                                finish();


                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.show();
            }

        });


    }

    private void getLocationPermission() {

        if (ActivityCompat.checkSelfPermission(DashboardActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            GetLocation();
        } else {
            ActivityCompat.requestPermissions(DashboardActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    44);



        }
    }
}