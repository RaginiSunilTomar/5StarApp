package com.ovot.fiveStarapp_test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobLogReport extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<JobLogs> arrayList = new ArrayList<>();
    RequestQueue requestQueue;

    ProgressDialog pd;

    String serviceId = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_log_report);

      Intent intent = getIntent();
      serviceId = intent.getStringExtra("sid");


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        requestQueue = Volley.newRequestQueue(getApplicationContext());


        pd = new ProgressDialog(JobLogReport.this);


        pd.setTitle("Request sending..");
        pd.setMessage("Please wait...");


//        for (int i = 0; i < 5; i++) {
//            JobLogs jobLogs = new JobLogs(this);
//            jobLogs.setJobLogID(3567943);
//            jobLogs.setUpdateDate("2022-05-04 13:05:56");
//            jobLogs.setStatusCode("MODEL_CHANGED");
//            jobLogs.setDescription("From AM43FHSA4B -> AM43FHSA3");
//            arrayList.add(jobLogs);
//        }
//        for (int i = 0; i < 5; i++) {
//            JobLogs jobLogs = new JobLogs(this);
//            jobLogs.setJobLogID(3364184);
//            jobLogs.setUpdateDate("2022-03-30 15:57:15");
//            jobLogs.setStatusCode("WORK_ALLOCATED");
//            jobLogs.setDescription("APPOINTMENT DONE ");
//            arrayList.add(jobLogs);
//        }
//        for (int i = 0; i < 5; i++) {
//            JobLogs jobLogs = new JobLogs(this);
//            jobLogs.setJobLogID(3363233);
//            jobLogs.setUpdateDate("2022-03-30 15:04:05");
//            jobLogs.setStatusCode("OPEN");
//            jobLogs.setDescription("|Assigned to : Air Fair Air Conditioner And Refrigerator Sales And Service| Display issue");
//            arrayList.add(jobLogs);
//        }


        //Pass the arraylist to recyclerView
//        recyclerViewAdapter = new RecyclerViewAdapter(this, arrayList);
//        recyclerView.setAdapter(recyclerViewAdapter);


        getDataOfJobLog();

    }



    private void getDataOfJobLog() {

        JobLogs jobLogs = new JobLogs(JobLogReport.this);


        String url = Url.JOB_LOG_REPORT_API+serviceId;
        arrayList.clear();
        pd.show();


        /////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(MyAllottedJob.this, "on response ", Toast.LENGTH_SHORT).show();


                try {
                    pd.dismiss();
                    JSONObject j = new JSONObject(response);
                    JSONArray dataArray = j.getJSONArray("data");
//                    JSONObject object = j.getJSONObject("data");


                    for (int i = 0; i <  dataArray.length(); i++) {
//                    for (int i = 0; i <  1; i++) {

                        JSONObject data = dataArray.getJSONObject(i);




//                        jobLogs.setJobLogID(data.optInt("joblog_id"));
//                        jobLogs.setUpdateDate(data.optString("updatedate"));
//                        jobLogs.setStatusCode(data.optString("status_code"));
//                        jobLogs.setDescription(data.optString("description"));


                        int a = data.optInt("joblog_id");
                        String updatedate = data.optString("updatedate");
                        String status_code = data.optString("status_code");
                        String description = data.optString("description");

                        String subhead = data.optString("subhead");
                        String bill_type = data.optString("bill_type");

//                        String subhead = "ACTION_TAKEN";
//                        String bill_type = "type_7";


                        arrayList.add(new JobLogs(JobLogReport.this,updatedate,status_code,description,subhead,bill_type,a));

//                        Toast.makeText(JobLogReport.this, "message... "+data.optInt("joblog_id")+","+data.optString("status_code"), Toast.LENGTH_SHORT).show();

                        if (subhead.equals("ACTION_TAKEN") && !bill_type.equals(null))
                        {
                            addBillType(serviceId,bill_type);
                        }

                    }


                     if (arrayList.size()>=1)
                     {
//                         int a = arrayList.get(0).jobLogID;
//                         int b = arrayList.get(1).jobLogID;
//                         int c = arrayList.get(2).jobLogID;
//
//                         int d = arrayList.get(3).jobLogID;

//                         Toast.makeText(JobLogReport.this, "message... "+a+","+b+","+c+","+d, Toast.LENGTH_SHORT).show();



                         recyclerViewAdapter = new RecyclerViewAdapter(JobLogReport.this, arrayList);
                         recyclerView.setAdapter(recyclerViewAdapter);

                     }







                } catch (JSONException e) {
                    pd.dismiss();
                    Toast.makeText(JobLogReport.this, "Oh..something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(JobLogReport.this, "Server error..", Toast.LENGTH_SHORT).show();

                new VolleyError();

                // Toast.makeText(getActivity(), "error" + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("ProductSRNO",srNo);
//
//                        return params;
//                    }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer " +"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianRpIjoiYzAyZTE3ODgxYzkyMGVhYjUzM2FmMWI0YWZhNTRlZjBmZGZkZWY1NWE3N2FmMGQzMmYzODQ2NmIwZDc3NDNmODgzY2E0NzVlMzZkZmYwODEiLCJpYXQiOjE2NTE2NTk0NjQsIm5iZiI6MTY1MTY1OTQ2NCwiZXhwIjoxNjgzMTk1NDY0LCJzdWIiOiIiLCJzY29wZXMiOltdfQ.OEWSG_bzEy-4UpB9brz_Cin4gXYq9gMB5aLsPQ-7nZVxttcNzbvBjhXi9jqiQoYQxHP11QmxsckkLGxbA8C-Z-XfZK0a9wUIqLy7mlzJYj5Z0mJNcDntyhnRH3m0FnzrNSCGKlXvKbrigTv5e8TPtX4y0FnB3anEIsmSHZTbFWJRwL_tab5QqQesx4tAleg-bNYQDjsIw6PAnblMv_KqhC7eJF2xXBe4PoIoOpTq4u3swHBCq_tJjRh8KqmoA0o0XEG8yA7n7LRxGsLbkg1moD9rAvMvsI-4LFoUn0SQO6y8GAN3pIHSO5DlcxcHlHeKtlZJtPLQ630qbsJZmyQu8rsVXekK1n4f1ClqHIo60uti4LVZ3lGp_DgKauFJjjhUAYe-lOpA8oLVBnE3MRwVMj9ghR_K4iv1WWNRVnVGGCWOIPA6F8o__Q3ET3EvN00xdCsilm8Xg63HSplEek3jIsAGkqMhyujpGHUHHlom96Zr6-JVR1MMw4ACz3olkOTiGGvTETEVx2tzLDn1x9QXXvbNwvx92gjBYRKE1020CRdRQIs9412EfJia4QYlEWVyqOPsKQthsuiO4f3Zeg1zU-xWfnusc5UnlpZezXJCPYpiu0YQI50oie2fDKHTU_pepWJALpSH3JeGm_uGdzLp3UqV9CAPinAWIcG2Rakz1dA");
                headers.put("Authorization", "Bearer " +Url.BEARER_TOKEN);

                return headers;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void addBillType(String serviceId, String bill_type) {

        Log.e("JobLogreport", "addBillType:method called ");
//        String url = Url.INSERT_BILL_TYPE_URL + "?serviceId=2022092600080&bill_type="+bill_type;
        String url = Url.INSERT_BILL_TYPE_URL + "?serviceId="+serviceId+"&bill_type="+bill_type;

        /////////////////////////////////////////
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String userId;

                String message;

                try {
                    pd.dismiss();
//                        JSONObject j = new JSONObject(response);
                    // CustomerResult = j.getJSONArray("customers");
                    JSONArray customerdetail = response.getJSONArray("action_taken");
//                    Toast.makeText(JobLogReport.this, "length "+customerdetail.length(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < customerdetail.length(); i++) {
                        JSONObject isdDetails = customerdetail.getJSONObject(i);
                        //userId = userd.getString("UserId");
                        message = isdDetails.getString("message");


                        Log.e("JobLogReport", "onresponse" );

                        if (message.equals("Success")) {


                            pd.dismiss();

                            Log.e("JobLogReport", "Insideif: " );


                        } else {
                            pd.dismiss();

                            Log.e("JobLogReport", "Insideelse: " );

                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();

                    Toast.makeText(JobLogReport.this, "NoVolleyError" , Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                new VolleyError();
                 Toast.makeText(JobLogReport.this, "VolleyError" + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
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