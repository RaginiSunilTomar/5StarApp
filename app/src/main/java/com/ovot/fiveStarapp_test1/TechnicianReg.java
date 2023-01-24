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
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.Map;

public class TechnicianReg extends AppCompatActivity {
private Button kycBtn;



    // Created by the Ragini tomar
    String technician_id = " ";
    ProgressDialog pd ;
    RequestQueue requestQueue;
    private EditText isdName,regMob,EmailId,BankName,bankAC,IFSC,UPI,panNo,AdharNo;
    private Button regSubmission;
    //    DatabaseOperation databaseOperation;

    boolean isvaild;
    File photoFile = null;
    File photoFile2 = null;
    ImageButton panCam,adharCam,chequeCam;
    Uri photoURI=null;
    Uri photoURI2=null;
    private String mCurrentPhotoPath="";
    private String mCurrentPhotoPath2="";
    private MyFTPClientFunctions ftpclient = null;
    File photoFile3 = null;
    private String mCurrentPhotoPath3="";
    Uri photoURI3=null;
    public static final String KEY_TECH_ID = "tech_id";
    private static final String PREF_NAME = "AndroidStarPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_reg);


        isdName = findViewById(R.id.isd);
        regMob = findViewById(R.id.isd_mob);
        EmailId = findViewById(R.id.email);
        BankName = findViewById(R.id.bank_name);
        bankAC = findViewById(R.id.bank_ac);
        IFSC = findViewById(R.id.ifsc);
        UPI = findViewById(R.id.upi);
        panNo = findViewById(R.id.pan);
        AdharNo = findViewById(R.id.adhar);
        regSubmission = findViewById(R.id.kyc_btn);

        panCam = findViewById(R.id.pan_cam);
        adharCam = findViewById(R.id.adhar_cam);
        chequeCam = findViewById(R.id.cheque_cam);

        ftpclient = new MyFTPClientFunctions();

        SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        technician_id = (shared.getString(KEY_TECH_ID, ""));


//        technician_id = "2665";

        Log.e("Intermediate", "TechId.. "+technician_id );
        //////////////////////////

//      getData(technician_id);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        try {
            pd = new ProgressDialog(TechnicianReg.this);

            pd.setTitle("Request sending");
            pd.setMessage("Please wait to get details..");
            pd.show();
            //  String url = "http://ovotapplive.poshsmetal.com/index.asmx/GetISDDetails";
//                String url = "http://ovotappdev.poshsmetal.com/index.asmx/GetISDDetails";
            String url = Url.GET_TECHNICIAN_DETAILS_URL+"?TechId="+technician_id;

            Log.e("TechnicianReg", "url1: "+url );
            /////////////////////////////////////////
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String userId;

                    String message;

//                    Toast.makeText(TechnicianReg.this, "a", Toast.LENGTH_SHORT).show();
                    try {
                        pd.dismiss();
//                        JSONObject j = new JSONObject(response);
                        // CustomerResult = j.getJSONArray("customers");
                        JSONArray customerdetail = response.getJSONArray("TechnicianDt");
                        for (int i = 0; i < customerdetail.length(); i++) {
                            JSONObject isdDetails = customerdetail.getJSONObject(i);
                            //userId = userd.getString("UserId");
                            message = isdDetails.getString("message");

//                            Toast.makeText(TechnicianReg.this, "b", Toast.LENGTH_SHORT).show();


                            if (message.equals("Success"))
                            {

                                String name = isdDetails.getString("Name");
                                String mobileNo = isdDetails.getString("Phone2");
                                String emailId = isdDetails.getString("EmailId");
                                String bankName = isdDetails.getString("BankName");
                                String bankACNo = isdDetails.getString("BankACNo");
                                String ifscCode = isdDetails.getString("IFSCCode");
                                String upiNo = isdDetails.getString("UPINo");
                                String panno = isdDetails.getString("PANNO");
                                String adharNo = isdDetails.getString("AdharNo");

                                if (name.length()>1 && !name.equals(null) && !name.equals("null") &&  !name.equals(""))
                                {
                                    isdName.setText(name);
                                }
                                if (mobileNo.length()>1 && !mobileNo.equals(null) && !mobileNo.equals("null") &&  !mobileNo.equals(""))
                                {
                                    regMob.setText(mobileNo);
                                }
                                if (emailId.length()>1 && !emailId.equals(null) && !emailId.equals("null") &&  !emailId.equals(""))
                                {
                                    EmailId.setText(emailId);
                                }
                                if (bankName.length()>1 && !bankName.equals(null) && !bankName.equals("null") &&  !bankName.equals(""))
                                {
                                    BankName.setText(bankName);
                                }

                                if (bankACNo.length()>1 && !bankACNo.equals(null) && !bankACNo.equals("null") &&  !bankACNo.equals(""))
                                {
                                    bankAC.setText(bankACNo);
                                }
                                if (ifscCode.length()>1 && !ifscCode.equals(null) && !ifscCode.equals("null") &&  !ifscCode.equals(""))
                                {
                                    IFSC.setText(ifscCode);
                                }
                                if (upiNo.length()>1 && !upiNo.equals(null) && !upiNo.equals("null") &&  !upiNo.equals(""))
                                {
                                    UPI.setText(upiNo);
                                }
                                if (panno.length()>1 && !panno.equals(null) && !panno.equals("null") &&  !panno.equals(""))
                                {
                                    panNo.setText(panno);
                                }
                                if (adharNo.length()>1 && !adharNo.equals(null) && !adharNo.equals("null") &&  !adharNo.equals(""))
                                {
                                    AdharNo.setText(adharNo);
                                }

                                pd.dismiss();


                            }else {
                                pd.dismiss();
//                                Toast.makeText(TechnicianReg.this, "c", Toast.LENGTH_SHORT).show();

                                Toast.makeText(TechnicianReg.this, "message "+message, Toast.LENGTH_SHORT).show();


                            }





                            ////////////////////////

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TechnicianReg", "error1 "+e.getMessage() );
//                        Toast.makeText(TechnicianReg.this, "d", Toast.LENGTH_SHORT).show();

                        pd.dismiss();
                    }
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Log.e("TechnicianReg", "error2 "+error.getMessage() );

//                    Toast.makeText(TechnicianReg.this, "e", Toast.LENGTH_SHORT).show();

                    new VolleyError();
                    // Toast.makeText(getActivity(), "error" + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("TechId", technician_id);
//                    return params;
//                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);


        } catch (Exception e) {
            Log.e("Register Activity ", ": Exception while " + "Sending data to server : " + e.getMessage());
            Toast.makeText(TechnicianReg.this, "Oops....Registration Failed Try Later.", Toast.LENGTH_LONG).show();
        }
        ////////////////// camera click-listener


        panCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageForPan();
            }
        });
        ///////////////////////////////////////


        adharCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageForAdhar();
            }
        });
        ///////////////////////////////////

        chequeCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageForCheque();
            }
        });
        
        
        regSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  newEmailId, newAdharNo,newPan,newBank,newBankAC,newUPINo,newIFSC,newIP;
                newIP = "abc";
                
                //*******************

                newEmailId = EmailId.getText().toString();
                newAdharNo = AdharNo.getText().toString();
                newPan = panNo.getText().toString();
                newBank = BankName.getText().toString();
                newBankAC = bankAC.getText().toString();
                newUPINo = UPI.getText().toString();
                newIFSC = IFSC.getText().toString();

                if(newAdharNo.isEmpty()) {
                    AdharNo.setError("Enter vaild adhar number");
                    isvaild = false;

                } else if(!VerhoeffAlgorithm(newAdharNo)) {

                    AdharNo.setError("Enter vaild adhar number");
                    isvaild = false;

                }
//                else  if(!newPan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}"))
//                {
//                    panNo.requestFocus();
//                    panNo.setError("Invalid Pan");
//                }
                else if (newEmailId.isEmpty())
                {
                    EmailId.setError("Email Id can not be empty");
                }
                else if (newBank.isEmpty())
                {
                    BankName.setError("Bank name can not be empty");
                }
                else if (newBankAC.isEmpty())
                {
                    bankAC.setError("Account No. can not be empty");
                }
                else if (newUPINo.isEmpty())
                {
                    UPI.setError("UPI can not be empty");

                }
                else if (newIFSC.isEmpty())
                {
                    UPI.setError("IFSC Code can not be empty");

                } else if (mCurrentPhotoPath.isEmpty())
                {
                    Toast.makeText(TechnicianReg.this, "Please select image for Aadhar", Toast.LENGTH_SHORT).show();

                }
                else if (mCurrentPhotoPath2.isEmpty())
                {
                    Toast.makeText(TechnicianReg.this, "Please select image for Pan", Toast.LENGTH_SHORT).show();

                }
                else if (mCurrentPhotoPath3.isEmpty())
                {
                    Toast.makeText(TechnicianReg.this, "Please select image for Cheque", Toast.LENGTH_SHORT).show();
//
                }
                else
                {
                    isvaild = true;
                    updateTechnicianDetails(technician_id,newEmailId, newAdharNo,newPan,newBank,newBankAC,newUPINo,newIFSC,newIP);
//
                    new Connection().execute();

                }


                //********************
                
            }
        });

    }




    private boolean VerhoeffAlgorithm(String adhar_number) {

        int[][] d  = new int[][]
                {
                        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                        {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
                        {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
                        {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
                        {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
                        {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
                        {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
                        {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
                        {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
                        {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
                };
        int[][] p = new int[][]
                {
                        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                        {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
                        {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
                        {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
                        {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
                        {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
                        {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
                        {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
                };
        int[] inv = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};
        int c = 0;
        int[] myArray = StringToReversedIntArray(adhar_number);
        for (int i = 0; i < myArray.length; i++){
            c = d[c][p[(i % 8)][myArray[i]]];
        }

        return (c == 0);
        //return true;
    }

    private static int[] StringToReversedIntArray(String num){
        int[] myArray = new int[num.length()];
        for(int i = 0; i < num.length(); i++){
            myArray[i] = Integer.parseInt(num.substring(i, i + 1));
        }
        myArray = Reverse(myArray);
        return myArray;
    }
    private static int[] Reverse(int[] myArray){
        int[] reversed = new int[myArray.length];
        for(int i = 0; i < myArray.length ; i++){
            reversed[i] = myArray[myArray.length - (i + 1)];
        }
        return reversed;
    }



    private void updateTechnicianDetails(String technician_id, String newEmailId, String newAdharNo, String newPan, String newBank, String newBankAC, String newUPINo, String newIFSC, String newIP) {

        Log.e("ISDDetails", "updateKYCDetails: " +technician_id + " ,"+newAdharNo  +", "+newPan+","+newBank+" ,"+", +"+newBankAC+", "+newIFSC +", "+newUPINo+" ,"+newIFSC+", "+newIP);




        String url =Url.UPDATE_TECHNICIAN_DETAILS_URL+"?TechnicianId="+technician_id+"&EmailId="+newEmailId+"&AdharNo="+newAdharNo+"&PANNO="+newPan+"&BankName="+newBank+
                "&BankACNo="+newBankAC+"&UPINo="+newUPINo+"&IFSCCode="+newIFSC+"&UserCode="+technician_id+"&IPAdd="+newIP;
        /////////////////////////////////////////
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //String userId;

                String message;

                try {
                    pd.dismiss();
//                    JSONObject j = new JSONObject(response);
                    // CustomerResult = j.getJSONArray("customers");
                    JSONArray customerdetail = response.getJSONArray("Techniciandt");
                    for (int i = 0; i < customerdetail.length(); i++) {
                        JSONObject updateISD = customerdetail.getJSONObject(i);

                        message = updateISD.getString("message");

                        Log.e("ISDUpdate", "onResponse: "+message);
                        if(message.equals("Fail")){

                            Toast.makeText(TechnicianReg.this, "Sorry, your details are not updated..", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }else{

                            pd.dismiss();
                            Toast.makeText(TechnicianReg.this, "Your details are updated successfully..", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TechnicianReg.this,Intermediate.class);
                            startActivity(intent);
                            finish();


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

//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
////                Log.e("ISDDetails", "updateKYCDetails: " +upISD + " ,"+upEmailId  +", "+upAdharNo+","+upPan+" ,"+", +"+upBank+", "+upBankAC +", "+upUPINo+" ,"+upIFSC+", "+upIP);
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("TechnicianId",technician_id);
//                params.put("EmailId",newEmailId);
//                params.put("AdharNo",newAdharNo);
//                params.put("PANNO",newPan);
//                params.put("BankName",newBank);
//                params.put("BankACNo",newBankAC);
//                params.put("UPINo",newUPINo);
//                params.put("IFSCCode",newIFSC);
//                params.put("UserCode",technician_id);
//                params.put("IPAdd",newIP);
//                //ISDCode=ISD0000006&EmailId=abc@gmail.com&AdharNo=728848432284&PANNO=AKYPT6843Q&BankName=ORIENTAL BANK OF COMMERCE&BankACNo=50081131001185&UPINo=12345&IFSCCode=ORBC0105008&UserCode=ISD0000006&IPAdd=abc
//
//                return params;
//            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    ///////////////
    private class Connection extends AsyncTask<Void, Void, Void> {

//        final String host = "202.66.172.185";
        final String host = "ftp.mobileeye.in";


//      final String username = "DEVISDADHAAR";
//      final String password =  "Adhaar@2021#FTP";

//        final String username = "LIVEISDADHAAR";
//        final String password =  "AdhaarLive@2021#FTP";


      final String username = "devtechadhaar";
      final String password =  "tech#Adhaar@2022";


        @Override
        protected void onPreExecute() {

            pd = ProgressDialog.show(TechnicianReg.this, "", "Connecting...",
                    true, false);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            try {




                if (host.length() < 1) {
                    Toast.makeText(TechnicianReg.this, "Please Enter Host Address!",
                            Toast.LENGTH_LONG).show();
                } else if (username.length() < 1) {
                    Toast.makeText(TechnicianReg.this, "Please Enter User Name!",
                            Toast.LENGTH_LONG).show();
                } else if (password.length() < 1) {
                    Toast.makeText(TechnicianReg.this, "Please Enter Password!",
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
                                "ADHAAR_"+technician_id);
                        pd.dismiss();
                        if (status == true) {

                            Log.e("FTPIMAGE status", "Upload success");
                            ftpclient.ftpDisconnect();
                            pd.dismiss();

                        } else {
                            Log.e("FTPIMAGE status", "Upload failed");
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
                //  }).start();
                // }



            } catch (NetworkOnMainThreadException e) {

                Log.e("Connection", "doInBackground: "+e.getMessage() );
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void unused) {
            sendPanImageDetails(technician_id);
            super.onPostExecute(unused);
        }


    }

    /////////////////

    public void sendPanImageDetails(String new_isdCode)
    {

//        final String host = "202.66.172.185";
        final String host = "ftp.mobileeye.in";


//        final String username = "DEVISDPAN";
//        final String password =  "Pan@2021#FTP";

//        final String username = "LIVEISDPAN";
//        final String password =  "PanLive@2021#FTP";

        final String username = "devtechpan";
        final String password =  "tech#Pan@2022";


        if (host.length() < 1) {
            Toast.makeText(TechnicianReg.this, "Please Enter Host Address!",
                    Toast.LENGTH_LONG).show();
        } else if (username.length() < 1) {
            Toast.makeText(TechnicianReg.this, "Please Enter User Name!",
                    Toast.LENGTH_LONG).show();
        } else if (password.length() < 1) {
            Toast.makeText(TechnicianReg.this, "Please Enter Password!",
                    Toast.LENGTH_LONG).show();
        } else {

//            pd = ProgressDialog.show(ISDReg.this, "", "Connecting...",
//                    true, false);

            File compressFile = saveBitmapToFile(photoFile2);
            File compressFile3 = saveBitmapToFile(photoFile3);

            new Thread(new Runnable() {
                public void run() {
                    boolean status = false;
                    status = ftpclient.ftpConnect2(host, username, password, 21);
                    if (status == true) {
                        Log.e("Main2", "Connection Success");



                        // pd = ProgressDialog.show(CustomeInvoice.this, "", "Uploading...",
                        //  true, false);
                        new Thread(new Runnable() {
                            public void run() {
                                boolean status = false;
                                status = ftpclient.ftpUpload2(
                                        compressFile,
                                        "PAN_"+new_isdCode);
                                pd.dismiss();
                                if (status == true) {
                                    Log.e("FTPIMAGE status2", "Upload success");
//                                    ftpclient.ftpDisconnect2();
//                                    pd.dismiss();
                                    boolean status2 = false;
                                    //Log.e("FTPIMAGE status2", "Upload success");
                                    status2=   ftpclient.ftpDisconnect2();
                                    if (status2 == true)
                                    {
                                        boolean status3 = false;
//                                        final String username3 = "DEVISDBANK";
//                                        final String password3 =  "Isd@2021#FTP";
//                                        final String username3 = "LIVEISDBANK";
//                                        final String password3 =  "IsdLive@2021#FTP";
                                        final String username3 = "devtechbank";
                                        final String password3 =  "tech#Bank@2022";
                                        status3 = ftpclient.ftpConnect3(host, username3, password3, 21);

                                        if (status3 == true)
                                        {
                                            boolean status4 = false;
                                            status4 = ftpclient.ftpUpload3(
                                                    compressFile3,
                                                    "BANK_"+new_isdCode);
                                            if (status4 == true) {
                                                Log.e("FTPIMAGE status3", "Upload success");
                                                ftpclient.ftpDisconnect3();
                                            }else {
                                                Log.e("FTPIMAGE status3", "Upload failed");
                                                ftpclient.ftpDisconnect3();
                                            }

                                        }

                                    }
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
    //////////////////////

    public File saveBitmapToFile(File file){


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
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
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

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }


    }
    // selecting images

    public void selectImageForPan() {
        final CharSequence[] options = {"Take Photo for Pan"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TechnicianReg.this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals("Take Photo for Pan")) {
                    if (ContextCompat.checkSelfPermission(TechnicianReg.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(TechnicianReg.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                    else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


                            try {
                                photoFile2 = createImageFileforPan();
                                // displayMassage(getBaseContext(),photoFile.getAbsolutePath());
                                Log.e("Ragini", photoFile2.getAbsolutePath());



                                // Continue only if the File was successfully created
                                if (photoFile2 != null) {
                                    Log.e("filepro", "filepro1");
                                    photoURI2 = FileProvider.getUriForFile(TechnicianReg.this,"com.ovot.fiveStarapp_test1.fileprovider",photoFile2);

                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI2);
                                    startActivityForResult(takePictureIntent, 2);
                                    Log.e("filepro", "filepro2");
                                }
                            } catch (IOException ex) {
                                // displayMassage(getBaseContext(),ex.getMessage().toString());
                                Log.e("ISDPanImage", "error: "+ ex.getMessage());
                            }
                        }

                    }


                }

            }

        });

        builder.show();


    }
    public void selectImageForAdhar()
    {
        final CharSequence[] options = {"Take Photo for Adhar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TechnicianReg.this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals("Take Photo for Adhar")) {
                    if (ContextCompat.checkSelfPermission(TechnicianReg.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(TechnicianReg.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                    else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


                            try {
                                photoFile = createImageFile();
                                // displayMassage(getBaseContext(),photoFile.getAbsolutePath());
                                Log.e("Ragini", photoFile.getAbsolutePath());



                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    Log.e("filepro", "filepro1");
                                    photoURI = FileProvider.getUriForFile(TechnicianReg.this,"com.ovot.fiveStarapp_test1.fileprovider",photoFile);

                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, 1);
                                    Log.e("filepro", "filepro2");
                                }
                            } catch (IOException ex) {
                                // displayMassage(getBaseContext(),ex.getMessage().toString());
                                Log.e("ISDPanImage", "error: "+ ex.getMessage());
                            }
                        }

                    }


                }

            }

        });

        builder.show();

    }
    public void selectImageForCheque()
    {
        final CharSequence[] options = {"Take Photo for Cheque"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TechnicianReg.this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals("Take Photo for Cheque")) {
                    if ((ContextCompat.checkSelfPermission(TechnicianReg.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED )||(ContextCompat.checkSelfPermission(TechnicianReg.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )) {
                        ActivityCompat.requestPermissions(TechnicianReg.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                    else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


                            try {
                                photoFile3 = createImageFileforCheque();
                                // displayMassage(getBaseContext(),photoFile.getAbsolutePath());
                                Log.e("Ragini", photoFile3.getAbsolutePath());



                                // Continue only if the File was successfully created
                                if (photoFile3 != null) {
                                    Log.e("filepro", "filepro1");
                                    photoURI3 = FileProvider.getUriForFile(TechnicianReg.this,"com.ovot.fiveStarapp_test1.fileprovider",photoFile3);

                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI3);
                                    startActivityForResult(takePictureIntent, 3);
                                    Log.e("filepro", "filepro2");
                                }
                            } catch (IOException ex) {
                                // displayMassage(getBaseContext(),ex.getMessage().toString());
                                Log.e("ISDPanImage", "error: "+ ex.getMessage());
                            }
                        }

                    }


                }

            }

        });

        builder.show();

    }
    ///////////////creating image
    private File createImageFileforCheque() throws IOException {
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

    private File createImageFileforPan() throws IOException {
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

    //////////////
    protected void onActivityResult(int requestCode,  int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Toast.makeText(TechnicianReg.this, "Adhar Image captured", Toast.LENGTH_SHORT).show();
            }
            else if (requestCode == 2){
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile2.getAbsolutePath());
                Toast.makeText(TechnicianReg.this, "Pan Image captured", Toast.LENGTH_SHORT).show();
            }
            else if (requestCode == 3){
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile3.getAbsolutePath());
                Toast.makeText(TechnicianReg.this, "Cheque Image captured", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                // checkCameraPresent("1");
                selectImageForAdhar();
            }
            else if (requestCode == 2){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                    // checkCameraPresent("1");
                    selectImageForPan();
                }
            }
            else if (requestCode == 3){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                    // checkCameraPresent("1");
                    selectImageForCheque();
                }
            }
            else
            {

                Log.e("ISDReg", "onRequestPermissionsResult: "+" Not working" );

            }
        }
    }


    private void getData(String technician_id) {
  String url = Url.GET_TECHNICIAN_DETAILS_URL;
        JsonObjectRequest jsonReq = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        Toast.makeText(TechnicianReg.this, "Inside on response", Toast.LENGTH_SHORT).show();
                    }
                },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Log.d("Server","onErrorResponse");
                            }
                        })

            {
                @Override
                public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TechId", technician_id);
                return params;
            }
            };
            jsonReq.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonReq);

    };


}
