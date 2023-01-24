package com.ovot.fiveStarapp_test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobNoCompletion extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // Created by Ragini Tomar

    private MyFTPClientFunctions ftpclient = null;
    RequestQueue requestQueue;


    ProgressDialog pd;
    String date = "";
    private Spinner spinner;

    String techId = "", serviceId = "", serialIDU = "", serialODU = "", modelNo = "", invoiceNo = "", invoiceDate = "";
    String  status_id="",customer_id="",technician_id="",latLong="";

    File invoiceFile = null;
    String image = "";

    ImageView productImage;

    Uri photoURI = null;
    File photoFile = null;
    private String mCurrentPhotoPath = "";

    List<String> allStatus ;

    private Button submit;
    EditText remarkET;

    String reason,remark;

    HashMap<Integer, String> hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_no_completion);

        ftpclient = new MyFTPClientFunctions();

        allStatus = new ArrayList<>();
        hashMap = new HashMap<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());




        reason ="";
        remark="";

        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        productImage = findViewById(R.id.camera_icon);
        submit = findViewById(R.id.btn_submit);
        spinner = findViewById(R.id.spinner_job_NoCompletion);
        remarkET = findViewById(R.id.remark);

        Intent intent = getIntent();
        serviceId = intent.getStringExtra("ServiceId");
        serialIDU = intent.getStringExtra("SerialIDU");
        serialODU = intent.getStringExtra("SerialODU");
        modelNo = intent.getStringExtra("modelNo");
        invoiceNo = intent.getStringExtra("InvoiceNo");
        invoiceDate = intent.getStringExtra("InvoiceDate");
        image = intent.getStringExtra("InvoiceImage");

        status_id  = intent.getStringExtra("status_id");
        customer_id  = intent.getStringExtra("customer_id");
        technician_id = intent.getStringExtra("technician_id");
        latLong = intent.getStringExtra("latLong");


//        invoiceFile = new File(image);
//        Toast.makeText(this, "Image " + invoiceFile, Toast.LENGTH_SHORT).show();
//        Log.e("JobCompletion", "onCreate: " + invoiceFile);


//        allStatus.add("Select Reason *");
//        allStatus.add("Spare Required");
//        allStatus.add("Gas Charging Required");
//        allStatus.add("Site not ready");
//        allStatus.add("Customer Given Appointment");
//        allStatus.add(" Product not Delivered");
//        allStatus.add("Others");

        getDataOfActionCode();


        ArrayAdapter aa2 = new ArrayAdapter(JobNoCompletion.this, android.R.layout.simple_spinner_item, allStatus);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa2);
        spinner.setOnItemSelectedListener(JobNoCompletion.this);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageForProduct();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                remark = remarkET.getText().toString();

                if (reason.length()<1||reason.equals("Select Reason *"))
                {
                    Toast.makeText(JobNoCompletion.this, "Please select reason for Non-Completion Job", Toast.LENGTH_SHORT).show();
                }
                else if (remark.length()<1)
                {
                    remarkET.setError("Please enter remark here");
                }
                else if (mCurrentPhotoPath.isEmpty())
                {
                    Toast.makeText(JobNoCompletion.this, "Please capture the image for Product", Toast.LENGTH_SHORT).show();

                }
                else {


//                    new JobNoCompletion.Connection().execute();
//                    addJobLog(reason,remark);
                    updateJobStatus();

                    updateJobStatusInDB();
                }

            }
        });
    }




    private void selectImageForProduct() {

        final CharSequence[] options = {"Take Photo for Product"};
        AlertDialog.Builder builder = new AlertDialog.Builder(JobNoCompletion.this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo for Product")) {
                    if (ContextCompat.checkSelfPermission(JobNoCompletion.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(JobNoCompletion.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
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
                                    photoURI = FileProvider.getUriForFile(JobNoCompletion.this, "com.ovot.fiveStarapp_test1.fileprovider", photoFile);

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Toast.makeText(JobNoCompletion.this, "Product Image captured", Toast.LENGTH_SHORT).show();

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

            }
        }
    }
    //// below code for uploading the product image on server
    private class Connection extends AsyncTask<Void, Void, Void>
    {

//        final String host = "202.66.172.185";
//final String host = "ftp.mobileeye.in";
        final String host = "164.52.219.144";


//      final String host="www.ovottechbackoffice.poshsmetal.com/Images/JobSheet";


        final String username = "devtechproduct";
        final String password = "tech#product@2022";


        @Override
        protected void onPreExecute() {

        pd = ProgressDialog.show(JobNoCompletion.this, "", "Image uploading,please wait...",
                true, false);
        super.onPreExecute();
    }


        @Override
        protected Void doInBackground(Void... voids) {

        try {


            if (host.length() < 1) {
                Toast.makeText(JobNoCompletion.this, "Please Enter Host Address!",
                        Toast.LENGTH_LONG).show();
            } else if (username.length() < 1) {
                Toast.makeText(JobNoCompletion.this, "Please Enter User Name!",
                        Toast.LENGTH_LONG).show();
            } else if (password.length() < 1) {
                Toast.makeText(JobNoCompletion.this, "Please Enter Password!",
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
                    if (status == true){
                        Log.e("ProductImage", "Upload success");
                        ftpclient.ftpDisconnect();
//                        pd.dismiss();


                  }

                else {
                        Log.e("ProductImage", "Upload failed");
                        ftpclient.ftpDisconnect();
//                        pd.dismiss();
                    }

                } else {
//                    pd.dismiss();
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
            addJobLog(reason,remark);

            pd.dismiss();


            Intent intent = new Intent(JobNoCompletion.this,MyAllottedJob.class);
            startActivity(intent);
            finish();

        super.onPostExecute(unused);

    }
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        reason = allStatus.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

/////////////////////////////////////
private void getDataOfActionCode() {
        hashMap.clear();
    allStatus.clear();
    allStatus.add("Select Reason *");

    pd = ProgressDialog.show(JobNoCompletion.this, "", "Please wait...",
            true, false);

    String url = Url.ACTION_CODE_API+"REQUEST_TO_ASP";
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

                    hashMap.put(status_Id,status_Code);


                    allStatus.add(status_Code);
//                    Toast.makeText(JobNoCompletion.this, "Data.."+status_Id+", "+status_Code, Toast.LENGTH_SHORT).show();



                }

//                    Toast.makeText(MyAllottedJob.this, "message "+serviceId+","+rescheduled_date+","+company_name+","+city, Toast.LENGTH_SHORT).show();

                pd.dismiss();


//                if (myJobList.size()>=1)
//                {
//
//                    myJobAdapter = new MyJobAdapter(MyAllottedJob.this,myJobList);
//                    mRecyclerView.setLayoutManager(new LinearLayoutManager(MyAllottedJob.this));
//                    mRecyclerView.setAdapter(myJobAdapter);
//
//
//                    sendServiceDataFromCRMtoDatabase(myJobList);
//
//                }





            } catch (JSONException e) {
                pd.dismiss();
                Toast.makeText(JobNoCompletion.this, "Json error..", Toast.LENGTH_SHORT).show();
                Log.e("JOB NO", "INSIDE CATCH" );
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            pd.dismiss();
            Toast.makeText(JobNoCompletion.this, "Volley error..", Toast.LENGTH_SHORT).show();

            new VolleyError();

            // Toast.makeText(getActivity(), "error" + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
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


                        Toast.makeText(JobNoCompletion.this, "Status updated successfully...", Toast.LENGTH_SHORT).show();


                    //}

//                    Toast.makeText(MyAllottedJob.this, "message "+serviceId+","+rescheduled_date+","+company_name+","+city, Toast.LENGTH_SHORT).show();



                } catch (JSONException e) {
//                    pd.dismiss();
                    Toast.makeText(JobNoCompletion.this, "Json error..", Toast.LENGTH_SHORT).show();
                    Log.e("JOBNO", "INSIDE CATCH 2: ");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pd.dismiss();
                Toast.makeText(JobNoCompletion.this, "Volley error..", Toast.LENGTH_SHORT).show();

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



    private void updateJobStatus() {

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
                        if (status_code.equals("REQUEST_TO_ASP"))
                        {


                            new JobNoCompletion.Connection().execute();

                        }





                  //  }




                } catch (JSONException e) {
//                    pd.dismiss();
                    Toast.makeText(JobNoCompletion.this, "Json error..", Toast.LENGTH_SHORT).show();
                    Log.e("JOB NO", "json error: "+e.getMessage() );
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pd.dismiss();
                Toast.makeText(JobNoCompletion.this, "Status can not be updated..", Toast.LENGTH_SHORT).show();

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
                params.put("status_code","REQUEST_TO_ASP");


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

        String url =Url.UPDATE_SERVICE_DETAILS_URL+"?serviceId="+serviceId+"&status_id="+status_id+"&serial1="+serialIDU+"&serial2="+serialODU+"&technicianid="+technician_id+
                "&statuscode=REQUEST_TO_ASP&model_code="+modelNo+"&latlong="+latLong+"&customerid="+customer_id+"&invoiceNo="+invoiceNo+"&invoiceDate="+invoiceDate;

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

                        Log.e("JobNoCompletion", "status update onResponse: "+message);
                        if(message.equals("Fail")){

                            Toast.makeText(JobNoCompletion.this, "Sorry, your details are not updated..", Toast.LENGTH_SHORT).show();
//                            pd.dismiss();
                        }else if ((message.equals("Success"))){

//                            pd.dismiss();
                            Toast.makeText(JobNoCompletion.this, "Your details are updated successfully..", Toast.LENGTH_SHORT).show();


//                            Intent intent = new Intent(JobCompletion.this,Intermediate.class);
//                            startActivity(intent);
//                            finish();

                            String phoneNumber="",technicianName="",serviceId="";

                            phoneNumber = updateISD.optString("phoneNumber");
                            technicianName = updateISD.optString("technicianName");
                            serviceId = updateISD.optString("serviceId");

                            Log.e("JobNoCompletion", "data : "+phoneNumber+","+technicianName+","+serviceId);

//                                    sendRatingSMSToCustomer();
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

}