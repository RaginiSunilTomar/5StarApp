package com.ovot.fiveStarapp_test1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class ScanActivity2 extends AppCompatActivity {
    public SurfaceView Surface_view;
    public TextView Text_view;
    public  String barcodeData;
    public ToneGenerator tone1;
    public CameraSource cameraSource;

    SerialsModel serialsModel;
    SessionManager sf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan2);

        serialsModel = new SerialsModel();
        sf = new SessionManager(ScanActivity2.this);


        tone1=new ToneGenerator(AudioManager.STREAM_MUSIC,100);

        Surface_view=findViewById(R.id.surface_view2);
        Text_view=findViewById(R.id.tv2);
        initializeDetector();
    }



    public void initializeDetector(){
        //Toast.makeText(this, "Barcode Scanner Started", Toast.LENGTH_SHORT).show();
        BarcodeDetector barcodeDetector= new  BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource=new  CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(1980,1080)
                .setAutoFocusEnabled(true)
                .build();
        Surface_view.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try{
                    if (ActivityCompat.checkSelfPermission(ScanActivity2.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
                    {
                        cameraSource.start(Surface_view.getHolder());
                    }
                    else{
                        ActivityCompat.requestPermissions(ScanActivity2.this,new String[]{Manifest.permission.CAMERA},1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
                // Intent i=new Intent(ScanActivity.this,MainActivity.class);
                //  ScanActivity.this.startActivity(i);

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections)
            {
                final SparseArray<Barcode> Barcode=detections.getDetectedItems();
                if(Barcode.size()!=0){
                    // Toast.makeText(ScanActivity.this, "code generated", Toast.LENGTH_SHORT).show();

                    Text_view.post(() -> {
                        if(Barcode.valueAt(0).email!=null){
                            Text_view.removeCallbacks(null);
                            barcodeData=Barcode.valueAt(0).email.address;



                            Text_view.setText(barcodeData);


                            tone1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
                        }
                        else {
                            barcodeData=Barcode.valueAt(0).displayValue;
                            Text_view.setText(barcodeData);
//                            serialsModel.setSerialODU(barcodeData);
                            sf.createSerialsSession2(barcodeData);
                            Intent i=new Intent(ScanActivity2.this,WorkEntryForm.class);
//                            i.putExtra("serial2",barcodeData);
                            ScanActivity2.this.startActivity(i);
                            finish();

                            tone1.startTone(ToneGenerator.TONE_CDMA_PIP,150);

                        }

                    });
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
//        Objects.requireNonNull(getSupportActionBar()).hide();
        cameraSource.release();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Objects.requireNonNull(getSupportActionBar()).hide();
        initializeDetector();
    }
}