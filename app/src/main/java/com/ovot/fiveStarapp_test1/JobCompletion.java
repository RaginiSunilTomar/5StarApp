package com.ovot.fiveStarapp_test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobCompletion extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // Created by Ragini Tomar

    ProgressDialog pd;
    String date = "";

    String techId = "", serviceId = "", serialIDU = "", serialODU = "", modelNo = "", invoiceNo = "", invoiceDate = ""
           , status_id="",customer_id="",technician_id="",latLong="";
    File invoiceFile = null;
   String image = null;

    File photoFile = null;
    File photoFile2 = null;
    File photoFile3 = null;
    File photoFile4 = null;

    Uri photoURI = null;
    Uri photoURI2 = null;
    Uri photoURI3 = null;
    Uri photoURI4 = null;

    private String mCurrentPhotoPath = "";
    private String mCurrentPhotoPath2 = "";
    private String mCurrentPhotoPath3 = "";
    private String mCurrentPhotoPath4 = "";

    String reason,remark;


    ImageView productImage, jobSheetImage, uniformImage, shoeCoverImage;

    private Button submit;
    EditText remarkET;


    private MyFTPClientFunctions ftpclient = null;
    RequestQueue requestQueue;

    List<String> allStatus ;

    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_completion);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        allStatus = new ArrayList<>();

        reason ="";
        remark="";


        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        spinner = findViewById(R.id.spinner_job_Completion);

        submit = findViewById(R.id.job_completion_btn);

        productImage = findViewById(R.id.camera_job_done1);
        jobSheetImage = findViewById(R.id.camera_job_done2);
        uniformImage = findViewById(R.id.camera_job_done3);
        shoeCoverImage = findViewById(R.id.camera_job_done4);
        remarkET = findViewById(R.id.remark1);

        ftpclient = new MyFTPClientFunctions();


        Intent intent = getIntent();
        serviceId = intent.getStringExtra("ServiceId");
//        serviceId = "2665";
        serialIDU = intent.getStringExtra("SerialIDU");
        serialODU = intent.getStringExtra("SerialODU");
        modelNo = intent.getStringExtra("modelNo");
        invoiceNo = intent.getStringExtra("InvoiceNo");
        invoiceDate = intent.getStringExtra("InvoiceDate");
        status_id  = intent.getStringExtra("status_id");
        customer_id  = intent.getStringExtra("customer_id");
        technician_id = intent.getStringExtra("technician_id");
        latLong = intent.getStringExtra("latLong");

//
//        Bundle bundle = intent.getExtras();
////        image = intent.getStringExtra("InvoiceImage");
//        image = bundle.getInt("InvoiceImage");



      ;



         invoiceFile = new File(String.valueOf(image));
//        Toast.makeText(this, "Image " + invoiceFile, Toast.LENGTH_SHORT).show();
        Log.e("JobCompletion", "onCreate: " + invoiceFile);


        getDataOfActionCode();

        ArrayAdapter aa2 = new ArrayAdapter(JobCompletion.this, android.R.layout.simple_spinner_item, allStatus);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa2);
        spinner.setOnItemSelectedListener(JobCompletion.this);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageForProduct();
            }
        });


        jobSheetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageForJobSheet();
            }
        });


        shoeCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImageForShoeCover();
            }
        });


        uniformImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImageForUniform();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                remark = remarkET.getText().toString();


                if (mCurrentPhotoPath.isEmpty()) {
                    Toast.makeText(JobCompletion.this, "Please upload the Product image", Toast.LENGTH_SHORT).show();
                } else if (mCurrentPhotoPath2.isEmpty()) {
                    Toast.makeText(JobCompletion.this, "Please upload the JobSheet image", Toast.LENGTH_SHORT).show();

                } else if (mCurrentPhotoPath3.isEmpty()) {
                    Toast.makeText(JobCompletion.this, "Please upload the image For Amstrad T-shirt,Mask and Gloves", Toast.LENGTH_SHORT).show();

                } else if (mCurrentPhotoPath4.isEmpty()) {
                    Toast.makeText(JobCompletion.this, "Please upload the ShoeCover image", Toast.LENGTH_SHORT).show();

                } else
                    if (reason.length()<1||reason.equals("Select Reason *"))
                {
                    Toast.makeText(JobCompletion.this, "Please select action taken by you", Toast.LENGTH_SHORT).show();
                }
                else if (remark.length()<1)
                {
                    remarkET.setError("Please enter remark here");
                }else {
                    new Connection().execute();

                    updateJobStatusInCRM();

                    updateJobStatusInDB();
                }

            }
        });

    }




    ///////////////////////// Started the Invoice Image code from here
    private void selectImageForProduct() {

        final CharSequence[] options = {"Take Photo for Product"};
        AlertDialog.Builder builder = new AlertDialog.Builder(JobCompletion.this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo for Product")) {
                    if (ContextCompat.checkSelfPermission(JobCompletion.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(JobCompletion.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


                            try {
                                photoFile = createImageFile();
                                // displayMassage(getBaseContext(),photoFile.getAbsolutePath());
                                Log.e("Ragini", photoFile.getAbsolutePath());


                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    Log.e("filepro", "filepro1");
                                    photoURI = FileProvider.getUriForFile(JobCompletion.this, "com.ovot.fiveStarapp_test1.fileprovider", photoFile);

                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, 1);
                                    Log.e("filepro", "filepro2");
                                }
                            } catch (IOException ex) {
                                // displayMassage(getBaseContext(),ex.getMessage().toString());
                                Log.e("ISDPanImage", "error: " + ex.getMessage());
                            }
                        }

                    }


                }

            }

        });

        builder.show();

    }

    private void selectImageForJobSheet() {


        final CharSequence[] options = {"Take Photo for JobSheet"};
        AlertDialog.Builder builder = new AlertDialog.Builder(JobCompletion.this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo for JobSheet")) {
                    if (ContextCompat.checkSelfPermission(JobCompletion.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(JobCompletion.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


                            try {
                                photoFile2 = createImageFile2();
                                // displayMassage(getBaseContext(),photoFile.getAbsolutePath());
                                Log.e("Ragini", photoFile2.getAbsolutePath());


                                // Continue only if the File was successfully created
                                if (photoFile2 != null) {
                                    Log.e("filepro", "filepro1");
                                    photoURI2 = FileProvider.getUriForFile(JobCompletion.this, "com.ovot.fiveStarapp_test1.fileprovider", photoFile2);

                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI2);
                                    startActivityForResult(takePictureIntent, 2);
                                    Log.e("filepro", "filepro2");
                                }
                            } catch (IOException ex) {
                                // displayMassage(getBaseContext(),ex.getMessage().toString());
                                Log.e("ISDPanImage", "error: " + ex.getMessage());
                            }
                        }

                    }


                }

            }

        });

        builder.show();
    }

    private void selectImageForUniform() {


        final CharSequence[] options = {"Take Photo for Amstrad T-shirt,Mask and Gloves"};
        AlertDialog.Builder builder = new AlertDialog.Builder(JobCompletion.this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo for Amstrad T-shirt,Mask and Gloves")) {
                    if (ContextCompat.checkSelfPermission(JobCompletion.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(JobCompletion.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


                            try {
                                photoFile3 = createImageFile3();
                                // displayMassage(getBaseContext(),photoFile.getAbsolutePath());
                                Log.e("Ragini", photoFile3.getAbsolutePath());


                                // Continue only if the File was successfully created
                                if (photoFile3 != null) {
                                    Log.e("filepro", "filepro1");
                                    photoURI3 = FileProvider.getUriForFile(JobCompletion.this, "com.ovot.fiveStarapp_test1.fileprovider", photoFile3);

                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI3);
                                    startActivityForResult(takePictureIntent, 3);
                                    Log.e("filepro", "filepro2");
                                }
                            } catch (IOException ex) {
                                // displayMassage(getBaseContext(),ex.getMessage().toString());
                                Log.e("ISDPanImage", "error: " + ex.getMessage());
                            }
                        }

                    }


                }

            }

        });

        builder.show();


    }

    private void selectImageForShoeCover() {


        final CharSequence[] options = {"Take Photo for Shoe-Cover"};
        AlertDialog.Builder builder = new AlertDialog.Builder(JobCompletion.this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo for Shoe-Cover")) {
                    if (ContextCompat.checkSelfPermission(JobCompletion.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(JobCompletion.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


                            try {
                                photoFile4 = createImageFile4();
                                // displayMassage(getBaseContext(),photoFile.getAbsolutePath());
                                Log.e("Ragini", photoFile4.getAbsolutePath());


                                // Continue only if the File was successfully created
                                if (photoFile4 != null) {
                                    Log.e("filepro", "filepro1");
                                    photoURI4 = FileProvider.getUriForFile(JobCompletion.this, "com.ovot.fiveStarapp_test1.fileprovider", photoFile4);

                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI4);
                                    startActivityForResult(takePictureIntent, 4);
                                    Log.e("filepro", "filepro2");
                                }
                            } catch (IOException ex) {
                                // displayMassage(getBaseContext(),ex.getMessage().toString());
                                Log.e("ISDPanImage", "error: " + ex.getMessage());
                            }
                        }

                    }


                }

            }

        });

        builder.show();
    }

    public File saveBitmapToFile(File file) {


        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "FV_Vehicle No_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("File Capture ", ":" + mCurrentPhotoPath);
        return image;
    }

    private File createImageFile2() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "FV_Vehicle No_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath2 = image.getAbsolutePath();
        Log.e("File Capture ", ":" + mCurrentPhotoPath2);
        return image;
    }

    private File createImageFile3() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "FV_Vehicle No_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath3 = image.getAbsolutePath();
        Log.e("File Capture ", ":" + mCurrentPhotoPath3);
        return image;
    }

    private File createImageFile4() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "FV_Vehicle No_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath4 = image.getAbsolutePath();
        Log.e("File Capture ", ":" + mCurrentPhotoPath4);
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Toast.makeText(JobCompletion.this, "Product Image captured", Toast.LENGTH_SHORT).show();
            } else if (requestCode == 2) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile2.getAbsolutePath());
                Toast.makeText(JobCompletion.this, "Pan Image captured", Toast.LENGTH_SHORT).show();
            } else if (requestCode == 3) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile3.getAbsolutePath());
                Toast.makeText(JobCompletion.this, "Cheque Image captured", Toast.LENGTH_SHORT).show();
            }
            if (requestCode == 4) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile4.getAbsolutePath());
                Toast.makeText(JobCompletion.this, "Cheque Image captured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // checkCameraPresent("1");
                selectImageForProduct();
            } else if (requestCode == 2) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // checkCameraPresent("1");
                    selectImageForJobSheet();
                }
            } else if (requestCode == 3) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // checkCameraPresent("1");
                    selectImageForUniform();
                }
            }
            if (requestCode == 4) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // checkCameraPresent("1");
                    selectImageForShoeCover();
                }
            } else {

                Log.e("ISDReg", "onRequestPermissionsResult: " + " Not working");

            }
        }
    }

    //////////////////Code started for Uploading images on server

    ///////////////
    private class Connection extends AsyncTask<Void, Void, Void> {

        final String host = "ftp.mobileeye.in";
//      final String host="www.ovottechbackoffice.poshsmetal.com/Images/JobSheet";


        final String username = "devtechproduct";
        final String password = "tech#product@2022";


        @Override
        protected void onPreExecute() {

            pd = ProgressDialog.show(JobCompletion.this, "", "Images uploading,please wait...",
                    true, false);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {


                if (host.length() < 1) {
                    Toast.makeText(JobCompletion.this, "Please Enter Host Address!",
                            Toast.LENGTH_LONG).show();
                } else if (username.length() < 1) {
                    Toast.makeText(JobCompletion.this, "Please Enter User Name!",
                            Toast.LENGTH_LONG).show();
                } else if (password.length() < 1) {
                    Toast.makeText(JobCompletion.this, "Please Enter Password!",
                            Toast.LENGTH_LONG).show();
                } else {


                    File compressFile = saveBitmapToFile(photoFile);

                    boolean status = false;


                    status = ftpclient.ftpConnect(host, username, password, 21);
                    if (status == true) {
                        Log.e("Main1", "Connection Success");


                        status = false;
                        status = ftpclient.ftpUpload(
                                compressFile,
                                "ProductImg_" + serviceId + "_" + technician_id);
//                        pd.dismiss();
                        if (status == true) {

                            status = false;
                            Log.e("FTPIMAGE status", "Upload success");
                          status = ftpclient.ftpDisconnect();

                          if (status==true)
                          {
                              status = false;
                              final String username2 = "devtechjobsheet";
                              final String password2 = "tech#jobsheet@2022";

                              status= ftpclient.ftpConnect(host,username2,password2,21);

                              if (status==true)
                              {
                                  status =false;
                                  File compressFile2 = saveBitmapToFile(photoFile2);
                                  status = ftpclient.ftpUpload(compressFile2,"JobSheet_" + serviceId + "_" +technician_id);

                                  Log.e("", "Job sheet Image successfully: ");
                                  if (status==true)
                                  {
                                      status = false;
                                      status = ftpclient.ftpDisconnect();

                                      if (status==true)
                                      {
                                          status = false;

                                          final String username3 = "devtechuniform";
                                          final String password3 = "tech#uniform@2022";
                                          status = ftpclient.ftpConnect(host,username3,password3,21);

                                          if (status==true)
                                          {
                                              File compressFile3 = saveBitmapToFile(photoFile3);
                                               status = false;
                                             status = ftpclient.ftpUpload(compressFile3,"Uniform_" + serviceId + "_" + technician_id);

                                             if (status==true)
                                             {
                                                 Log.e("", "Uniform image uploaded successfully: ");

                                                 status = false;
                                               status =  ftpclient.ftpDisconnect();

                                               if (status==true)
                                               {
                                                   status = false;

                                                   final String username4 = "devtechshoecover";
                                                   final String password4 = "tech#shoecover@2022";
                                                   status = ftpclient.ftpConnect(host,username4,password4,21);

                                                   if (status==true)
                                                   {
                                                       status =false;
                                                       File compressFile4 = saveBitmapToFile(photoFile4);

                                                       status = ftpclient.ftpUpload(compressFile4,"ShoeCover_" + serviceId + "_" + technician_id);

                                                       if (status==true)
                                                       {
                                                           Log.e("", "ShoeCover image uploaded successfully: ");
                                                           status = false;
                                                           status =ftpclient.ftpDisconnect();
                                                       }
                                                       else {
                                                           ftpclient.ftpDisconnect();
                                                           Log.e("JobCompletion", "ShoeCover Image: upload Failed" );
                                                       }
                                                   }
                                               }
                                             }
                                             else {
                                                 ftpclient.ftpDisconnect();
                                                 Log.e("JobCompletion", "Uniform Image: upload Failed" );

                                             }
                                          }
                                      }


                                  }
                                  else {
                                      ftpclient.ftpDisconnect();
                                      Log.e("JobCompletion", "JobSheet Image: upload Failed" );

                                  }
                              }
                          }
//                            pd.dismiss();

                        } else {
                            Log.e("ProductImage", "Upload failed");
                            ftpclient.ftpDisconnect();
                            pd.dismiss();
                        }
                        // }
                        //}).start();
                        pd.dismiss();
                    } else {
                        pd.dismiss();
                        Log.e("Main1", "Connection failed");

                    }
                }


            } catch (NetworkOnMainThreadException e) {

                Log.e("Connection", "doInBackground: " + e.getMessage());
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void unused) {
            pd.dismiss();
//            pd = ProgressDialog.show(JobCompletion.this, "", "Process running...",
//                    true, false);
//
//                        sendInvoiceImageDetails();
//
//            updateJobStatusInDB();
            super.onPostExecute(unused);

        }
    }






    private void sendInvoiceImageDetails() {



        final String host = "ftp.mobileeye.in";


        final String username = "devtechinvoice";
        final String password =  "tech#invoice@2022";


        if (host.length() < 1) {
            Toast.makeText(JobCompletion.this, "Please Enter Host Address!",
                    Toast.LENGTH_LONG).show();
        } else if (username.length() < 1) {
            Toast.makeText(JobCompletion.this, "Please Enter User Name!",
                    Toast.LENGTH_LONG).show();
        } else if (password.length() < 1) {
            Toast.makeText(JobCompletion.this, "Please Enter Password!",
                    Toast.LENGTH_LONG).show();
        } else {


            File compressFile5 = saveBitmapToFile(invoiceFile);

            new Thread(new Runnable() {
                public void run() {
                    boolean status = false;
                    status = ftpclient.ftpConnect2(host, username, password, 21);
                    if (status == true) {
                        Log.e("Main2", "Connection Success");


                        new Thread(new Runnable() {
                            public void run() {
                                boolean status = false;
                                status = ftpclient.ftpUpload2(
                                        compressFile5,
                                        invoiceNo+"_" + serviceId + "_" + date);
                                pd.dismiss();
                                if (status == true) {

                                    Log.e("JobCompletion", "Invoice Image uploaded successfully: ");
                                    ftpclient.ftpDisconnect2();
                                    pd.dismiss();

                                } else {
                                    Log.e("FTPIMAGE status2", "Upload failed");
                                    ftpclient.ftpDisconnect2();
                                    pd.dismiss();
                                }
                            }
                        }).start();
                        pd.dismiss();
                    } else {
                        Log.e("Main2", "Connection failed");
                        pd.dismiss();
                    }
                }
            }).start();
        }
    }

/////////////

    private void getDataOfActionCode() {
//        hashMap.clear();
        allStatus.clear();
        allStatus.add("Action Taken *");

        pd = ProgressDialog.show(JobCompletion.this, "", "Please wait...",
                true, false);

        String url = Url.ACTION_CODE_API+"REVIEW_FOR_COMPLETION";
        /////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MyAllottedJob.this, "on response ", Toast.LENGTH_SHORT).show();


                try {
                    JSONObject j = new JSONObject(response);
                    JSONArray dataArray = j.getJSONArray("data");
//                    JSONObject object = j.getJSONObject("data");


                    for (int i = 0; i <  dataArray.length(); i++) {
                        JSONObject data = dataArray.getJSONObject(i);


                        Log.e("MyAllottedJob", "service_id: "+serviceId );

                        Integer status_Id = data.optInt("status_id");
                        String status_Code  = data.optString("status_code");


                        allStatus.add(status_Code);
//                    Toast.makeText(JobCompletion.this, "Data.."+status_Id+", "+status_Code, Toast.LENGTH_SHORT).show();



                    }


                    pd.dismiss();




                } catch (JSONException e) {
                    pd.dismiss();
                    Toast.makeText(JobCompletion.this, "Json error..", Toast.LENGTH_SHORT).show();
                    Log.e("JOB NO", "INSIDE CATCH" );
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(JobCompletion.this, "Volley error..", Toast.LENGTH_SHORT).show();

                new VolleyError();


            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " +Url.BEARER_TOKEN);

                return headers;
            }
        };
        requestQueue.add(stringRequest);

    }

    /////

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        reason = allStatus.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    ///////////



    private void updateJobStatusInCRM() {

        String url = Url.JOB_STATUS_UPDATE_API+serviceId;
        /////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MyAllottedJob.this, "on response ", Toast.LENGTH_SHORT).show();

//                pd.dismiss();

                try {
                    JSONObject j = new JSONObject(response);
//                    JSONArray dataArray = j.getJSONArray("data");
                    JSONObject dataArray = j.getJSONObject("data");


//                    for (int i = 0; i <  dataArray.length(); i++) {
//                        JSONObject data = dataArray.getJSONObject(i);


                    Log.e("MyAllottedJob", "service_id: "+serviceId );

//                        Integer status_Id = data.optInt("status_id");
                    String status_code = "";
                    status_code  = dataArray.optString("status_code");
                    if (status_code.equals("REVIEW_FOR_COMPLETION"))
                    {

//                        Toast.makeText(JobCompletion.this, "done...", Toast.LENGTH_SHORT).show();

                        addJobLog(reason,remark);

                    }





                    //  }




                } catch (JSONException e) {
//                    pd.dismiss();
                    Toast.makeText(JobCompletion.this, "Json error..", Toast.LENGTH_SHORT).show();
                    Log.e("JOB NO", "json error: "+e.getMessage() );
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pd.dismiss();
                Toast.makeText(JobCompletion.this, "Status update failed..", Toast.LENGTH_SHORT).show();

                new VolleyError();

                Log.e("JOB NO", "json error: "+error.getMessage() );


                // Toast.makeText(getActivity(), "error" + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("status_code","REVIEW_FOR_COMPLETION");


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " +Url.BEARER_TOKEN);

                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }


    //////////////////


    private void addJobLog(String reason, String remark)
    {
//     ProgressDialog   pd = ProgressDialog.show(JobNoCompletion.this, "", "Please wait...",
//                true, false);

        String url = Url.ADD_JOB_LOG_API+serviceId;

        /////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MyAllottedJob.this, "on response ", Toast.LENGTH_SHORT).show();

//                pd.dismiss();

                try {
                    JSONObject j = new JSONObject(response);
                    JSONArray dataArray = j.getJSONArray("data");
//                    JSONObject object = j.getJSONObject("data");


//                    for (int i = 0; i <  dataArray.length(); i++) {
//                        JSONObject data = dataArray.getJSONObject(i);
//
//
//                        Log.e("MyAllottedJob", "service_id: "+serviceId );

//                        Integer status_Id = data.optInt("status_id");
//                        String joblog_id = "";
//                        joblog_id  = data.optString("joblog_id");
//                        if (joblog_id.length()>1)
//                        {
//                            Toast.makeText(JobNoCompletion.this, "Successfully..", Toast.LENGTH_SHORT).show();
//
//                        }


                    Toast.makeText(JobCompletion.this, "Status updated successfully..", Toast.LENGTH_SHORT).show();


                    //}

//                    Toast.makeText(MyAllottedJob.this, "message "+serviceId+","+rescheduled_date+","+company_name+","+city, Toast.LENGTH_SHORT).show();



                } catch (JSONException e) {
//                    pd.dismiss();
                    Toast.makeText(JobCompletion.this, "Json error..", Toast.LENGTH_SHORT).show();
                    Log.e("JOBNO", "INSIDE CATCH 2: ");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pd.dismiss();
                Log.e("JobCompletion", "AddJobLog_Volleyerror: "+error.getMessage() );
                Toast.makeText(JobCompletion.this, "Volley error...", Toast.LENGTH_SHORT).show();

                new VolleyError();

                // Toast.makeText(getActivity(), "error" + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("status_code",reason);
                params.put("description",remark);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " +Url.BEARER_TOKEN);

                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void updateJobStatusInDB()
    {

//        Log.e("ISDDetails", "updateKYCDetails: " +technician_id + " ,"+newAdharNo  +", "+newPan+","+newBank+" ,"+", +"+newBankAC+", "+newIFSC +", "+newUPINo+" ,"+newIFSC+", "+newIP);

//        String url =  "http://twtech.in:8080/FiveStarApp/rest/updateServiceDetails?serviceId=2022092600080&status_id=452&serial1=888888&serial2=222222&technicianid=2665&statuscode=REQUEST_TO_ASP& model_code=AM20PI3&latlong=8338838.334234,473.554&customerid=384977&invoiceNo=3333&invoiceDate=2022-11-07";


        String url =Url.UPDATE_SERVICE_DETAILS_URL+"?serviceId="+serviceId+"&status_id="+status_id+"&serial1="+serialIDU+"&serial2="+serialODU+"&technicianid="+technician_id+
                "&statuscode=REVIEW_FOR_COMPLETION&model_code="+modelNo+"&latlong="+latLong+"&customerid="+customer_id+"&invoiceNo="+invoiceNo+"&invoiceDate="+invoiceDate;

        Log.e("JobCompletion", "updateServiceDetails:" +url);
        /////////////////////////////////////////
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //String userId;

                String message;

                try {
//                    pd.dismiss();
//                    JSONObject j = new JSONObject(response);
                    // CustomerResult = j.getJSONArray("customers");
                    JSONArray customerdetail = response.getJSONArray("ServiceEntry");
                    for (int i = 0; i < customerdetail.length(); i++) {
                        JSONObject updateISD = customerdetail.getJSONObject(i);

                        message = updateISD.getString("message");

                        Log.e("JobCompletion", "status update onResponse: "+message);
                        if(message.equals("Fail")){

                            Toast.makeText(JobCompletion.this, "Sorry, your details are not updated..", Toast.LENGTH_SHORT).show();
//                            pd.dismiss();
                        }else if ((message.equals("Success"))){

//                            pd.dismiss();
                            Toast.makeText(JobCompletion.this, "Your details are updated successfully..", Toast.LENGTH_SHORT).show();


//                            Intent intent = new Intent(JobCompletion.this,Intermediate.class);
//                            startActivity(intent);
//                            finish();

                                    String phoneNumber="",technicianName="",serviceId="";

                                    phoneNumber = updateISD.optString("phoneNumber");
                                    technicianName = updateISD.optString("technicianName");
                                    serviceId = updateISD.optString("serviceId");

                            Log.e("JobCompletion", "data : "+phoneNumber+","+technicianName+","+serviceId);

                                    sendRatingSMSToCustomer(phoneNumber,technicianName,serviceId);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("UpdateISD", "JSONException: "+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                new VolleyError();
                Log.e("ISDError", "onErrorResponse: "+error.getMessage() );
                // Toast.makeText(getActivity(), "error" + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void sendRatingSMSToCustomer(String phoneNumber, String technicianName, String serviceId)
    {
        pd.show();


        String url =Url.SMS_API;   // 9045711356

        /////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(LoginActivity.this, "on response ", Toast.LENGTH_SHORT).show();


                try {
                    pd.dismiss();
                    JSONObject j = new JSONObject(response);
//                    JSONObject object = j.getJSONObject("data");

                    String object = j.optString("data");

                    Log.e("Login", "SMSResponse: "+object);
                    Log.e("Login", "SMSResponse: "+" Dear Sir/Madam \nYour Service Id "+serviceId+" has been completed successfully by Mr. "+technicianName+". Kindly rate your experience here @ http://twtech.in/FiveStarRating/5StarRating.jsp?cid=" +customer_id+ "&sid="+serviceId+"\n\nAmstrad is happy to serve you. Thank you. -Amstrad");

                    pd.dismiss();
                    if (object.equals("Invalid template"))
                    {
                        Toast.makeText(JobCompletion.this, "SMS Server error", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(JobCompletion.this,MyAllottedJob.class);
                        startActivity(intent);
                        finish();
                    }



                } catch (JSONException e) {
                    pd.dismiss();
                    Log.e("Login", "SMSError1: "+e.getMessage() );
                    Toast.makeText(JobCompletion.this, "SMS Server error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(JobCompletion.this, "SMS Server error2..", Toast.LENGTH_SHORT).show();
                Log.e("Login", "SMSError2: "+error.getMessage() );
                new VolleyError();

                // Toast.makeText(getActivity(), "error" + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("number",phoneNumber);
                params.put("number","9511613397");
                params.put("message","Dear Sir/Madam \nYour Service Id "+serviceId+" has been completed successfully by Mr. "+technicianName+". Kindly rate your experience here @ http://twtech.in/FiveStarRating/5StarRating.jsp?cid=" +customer_id+ "&sid="+serviceId+"\n\nAmstrad is happy to serve you. Thank you. -Amstrad");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " +Url.BEARER_TOKEN);
                return headers;
            }
        };
        requestQueue.add(stringRequest);

    }



}