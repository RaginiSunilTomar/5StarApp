package com.ovot.fiveStarapp_test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FlashScreen extends AppCompatActivity {
    private int SLEEP_TIMER = 3;
//    TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);


        logic logic = new logic();
        logic.start();
    }

    private class logic extends Thread {
        public void run() {
            try {
//                textView.setText("OVOT-DEV-Version");
                sleep(1000*SLEEP_TIMER);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //getVersionDetails();

            Intent i = new Intent(FlashScreen.this, MainActivity.class);
            startActivity(i);
            FlashScreen.this.finish();
        }
    }
}