package com.ovot.fiveStarapp_test1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String Tag = "Permissions";
    SessionManager sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sf = new SessionManager(getApplicationContext());

        boolean perVal=checkAndRequestPermissions();
        if(perVal==true){
//            Intent intent = new Intent(CheckPermission.this,LoginActivity.class);
//            startActivity(intent);
//            this.finish();
            sf.checkLogin();
            this.finish();;
        }

    }

    private boolean checkAndRequestPermissions() {
        return true;
    }


}