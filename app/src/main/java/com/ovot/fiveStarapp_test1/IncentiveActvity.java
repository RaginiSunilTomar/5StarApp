package com.ovot.fiveStarapp_test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class IncentiveActvity extends AppCompatActivity {


    // Created by Ragini Tomar

    RecyclerView recyclerView;
    IncentiveAdapter adapter;
    TextView txtfrmDate, txttoDate, userName;
//    String incentiveDatabaseName = Environment.getExternalStorageDirectory().getPath() + "/TWAmstradDB/DDIncentiveData.db";
    String[]  retrieveIncentiveData;
    private ImageView LogOut,Home;
    private Button submit;
    private int year;
    private int month;
    private int day;
    private int toyear;
    private int tomonth;
    private int today;
    ProgressDialog pd;
    public static final String KEY_TECH_ID = "tech_id";
    private static final String PREF_NAME = "AndroidStarPref";
    JsonArrayRequest jsonarrayRequest;
    RequestQueue requestQueue;


    List<IncentiveModel> myIncentive;

   IncentiveAdapter incentiveAdapter;
    RecyclerView mRecyclerView;



//    DatabaseHelper database_helper;
//    DatabaseHelper2 databaseHelper2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incentive_actvity);


        pd = new ProgressDialog(IncentiveActvity.this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        txtfrmDate = findViewById(R.id.fromdateId);
        txttoDate = findViewById(R.id.toDateId);
        recyclerView=findViewById(R.id.incentive_recyclerview);

        submit= findViewById(R.id.incentiveSubmit);
        userName = findViewById(R.id.tv_name);

//        database_helper = new DatabaseHelper(this);
//        databaseHelper2 = new DatabaseHelper2(this);

        myIncentive = new ArrayList<>();
        mRecyclerView = findViewById(R.id.incentive_recyclerview);

        SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String userId = (shared.getString(KEY_TECH_ID, ""));

        userName.setText(userId);




        txtfrmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(IncentiveActvity.this,R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtfrmDate.setText(year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        String todat = txtfrmDate.getText().toString();
                        java.text.SimpleDateFormat sdf3 = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        java.text.SimpleDateFormat sdf4 = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String tdate;
                        try {
                            tdate = sdf4.format(sdf3.parse(todat));
                            Log.e("Incentice", "From Date: "  + tdate );
                            txtfrmDate.setText(tdate);
                        } catch (Exception e) {


                        }
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });



        txttoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                toyear = c.get(Calendar.YEAR);
                tomonth = c.get(Calendar.MONTH);
                today= c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(IncentiveActvity.this,R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txttoDate.setText(year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        String todat = txttoDate.getText().toString();
                        java.text.SimpleDateFormat sdf3 = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        java.text.SimpleDateFormat sdf4 = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String tdate;
                        try {
                            tdate = sdf4.format(sdf3.parse(todat));
                            Log.e("Incentice", "To Date: "  + tdate );
                            txttoDate.setText(tdate);
                        } catch (Exception e) {

                        }
                    }
                },toyear,tomonth,today);
                datePickerDialog.show();
            }
        });





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                databaseHelper2.deleteIncentiveTableData();

                myIncentive.clear();

                pd.setTitle("Request sending..");
                pd.setMessage("Please wait to get details...");
                pd.show();

                try {
                    String frmDAte = txtfrmDate.getText().toString();
                    String toDate = txttoDate.getText().toString();
                    Log.e("Incentive", "to and from date:- "+toDate +", "+frmDAte );

                    if (frmDAte.length()>0&&toDate.length()>0)
                    {
                        getIncentiveDetails(frmDAte,toDate,userId);


                    }
                    else {
                        Toast.makeText(IncentiveActvity.this, "Please select date", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
                catch (Exception e)
                {

                }
                Log.e("Incentive", "userId:- "+userId );


            }
        });
    }

    private void getIncentiveDetails(String fDAte,String tDate, String uId) {


//        String url = Url.INCENTIVE_REPORT_URL;
        Log.e("Incentive", "getIncentiveDetails: "+ "http://ovottechappdev.poshsmetal.com/index.asmx/GetIncentiveReport?TechnicianId=123&FromDate="+fDAte+"&ToDate="+tDate);
//        String url =   Url.GET_INCENTIVE_REPORT+"?techId="+123+"&fromDate="+fDAte+"&toDate="+tDate;
        String url =   Url.GET_INCENTIVE_REPORT+"?techId="+uId+"&fromDate="+fDAte+"&toDate="+tDate;

        Log.e("Inentive", "getIncentiveDetails: url " +url );
        /////////////////////////////////////////
       JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    pd.dismiss();
//                    JSONObject j = new JSONObject(response);
                    JSONArray customerdetail = response.getJSONArray("IncentiveDt");
                    for (int i = 0; i < customerdetail.length(); i++) {
                        JSONObject productd = customerdetail.getJSONObject(i);


                        String message = productd.optString("message");
                        if (message.equals("Success"))
                        {
                            pd.dismiss();


                                String date= productd.optString("date");
                                String ServiceId =  productd.optString("ServiceId");
                                String Servicetype = productd.optString("Servicetype");
                               String CustomerName = productd.optString("CustomerName");
                               String CustomerRating= productd.optString("CustomerRating");
                                String starIncentive= productd.optString("5StarIncentive");
                                String ApproveStatus = productd.optString("ApproveStatus");
                                String IncentiveAmt = productd.optString("UniformIncentiveAmt");
                                String UniformApproval = productd.optString("UniformApproval");
                                String PaidStatus = productd.optString("PaidStatus");
                                String RejectionReason = productd.optString("RejectionReason");



                                myIncentive.add(new IncentiveModel(date,ServiceId,Servicetype,CustomerName,CustomerRating,starIncentive,ApproveStatus,IncentiveAmt,UniformApproval,PaidStatus,RejectionReason));

//                            Toast.makeText(IncentiveActvity.this, "Data available for selected date "+date + ","+ Servicetype, Toast.LENGTH_SHORT).show();


                        }else {

                            Toast.makeText(IncentiveActvity.this, "Data not available for selected date ", Toast.LENGTH_SHORT).show();


                            pd.dismiss();
                        }


                        if (myIncentive.size()>=1)
                        {


//                            Toast.makeText(IncentiveActvity.this, "Inside else", Toast.LENGTH_SHORT).show();

                            incentiveAdapter = new IncentiveAdapter(IncentiveActvity.this,myIncentive);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(IncentiveActvity.this));
                            mRecyclerView.setAdapter(incentiveAdapter);




                        }


                    }




//
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Incentive", "catch1: "+e.getMessage() );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                new VolleyError();
            }
        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded";
//            }
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("techId","123");
//                params.put("fromDate",fDAte);
//                params.put("toDate",tDate);   // TechnicianId=123&FromDate="+fDAte+"&ToDate="+tDate
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

//********************************************8



}