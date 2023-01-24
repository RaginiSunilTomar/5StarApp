package com.ovot.fiveStarapp_test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;

public class MyAllottedJob extends AppCompatActivity {

    Long serviceId;
    int  status_id, freeze, registerId, customerId, pinCode, providerId, technicianId;
    String statusCode, stage, complaintype_code,customer_name, phone1, phone2, address1,address2, city
            ,state,product_name, producttype_code, model_code, company_name, provider_phone1, circle_head, call_date,
            description, appointment_date, rescheduled_date, complete_date;


    String serviceId2, status_id2, freeze2, registerId2, customerId2, pinCode2, providerId2, technicianId2
    , statusCode2, stage2, complaintype_code2,customer_name2, phone12, phone22, address12,address22, city2
            ,state2,product_name2, producttype_code2, model_code2, company_name2, provider_phone12, circle_head2, call_date2,
            description2, appointment_date2, rescheduled_date2, complete_date2;


    RequestQueue requestQueue;
    String technician_id, url;
    ProgressDialog pd;



    List<MyJobModel> myJobList;
    MyJobAdapter myJobAdapter;
    RecyclerView mRecyclerView;
    ImageView back;

    public static final String KEY_TECH_ID = "tech_id";
    private static final String PREF_NAME = "AndroidStarPref";
    private static final String PREF_NAME2 = "AndroidHivePref2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_allotted_job);

        SharedPreferences preferences =getSharedPreferences(PREF_NAME2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
//        finish();


        back = findViewById(R.id.back_btn);


        getLocation();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAllottedJob.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mRecyclerView = findViewById(R.id.my_allotJob_recycler);

//        technician_id = "8";
        SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        technician_id  = (shared.getString(KEY_TECH_ID, ""));



        url = Url.SERVICE_LIST_API+technician_id;

        myJobList = new ArrayList<>();


        requestQueue = Volley.newRequestQueue(getApplicationContext());

        pd = new ProgressDialog(MyAllottedJob.this);


        pd.setTitle("Request sending..");
        pd.setMessage("Please wait...");


        getDataOfMyJob();


    }



    private void getDataOfMyJob() {
        myJobList.clear();
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
                        JSONObject data = dataArray.getJSONObject(i);
                        serviceId = data.optLong("service_id");


                        Log.e("MyAllottedJob", "service_id: "+serviceId );

                        status_id = data.optInt("status_id");
                        freeze = data.optInt("freeze");
                        registerId = data.optInt("register_id");
                        customerId= data.optInt("customer_id");
                        pinCode= data.optInt("pincode");
                        providerId = data.optInt("provider_id");
                        technicianId= data.optInt("technician_id");

                        statusCode = data.optString("status_code");
                        stage = data.optString("stage");
                        complaintype_code = data.optString("complaintype_code");
                        customer_name = data.optString("customer_name");
                        phone1 = data.optString("phone1");
                        phone2 = data.optString("phone2");
                        address1 = data.optString("address1");
                        address2 = data.optString("address2");
                        state = data.optString("state");
                        city = data.optString("city");
                        state = data.optString("state");
                        product_name = data.optString("product_name");
                        producttype_code= data.optString("producttype_code");
                        model_code = data.optString("model_code");
                        company_name = data.optString("company_name");
                        provider_phone1= data.optString("provider_phone1");
                        circle_head =data.optString("circle_head");
                        call_date= data.optString("call_date");
                        description = data.optString("description");
                        appointment_date= data.optString("appointment_date");
                        rescheduled_date = data.optString("rescheduled_date");
                        complete_date = data.optString("complete_date");

                        myJobList.add(new MyJobModel(serviceId, status_id, freeze, registerId, customerId, pinCode, providerId, technicianId,statusCode, stage, complaintype_code,customer_name, phone1, phone2, address1,address2, city
                                ,state,product_name, producttype_code, model_code, company_name, provider_phone1, circle_head, call_date,
                                description, appointment_date, rescheduled_date, complete_date));

                    }

//                    Toast.makeText(MyAllottedJob.this, "message "+serviceId+","+rescheduled_date+","+company_name+","+city, Toast.LENGTH_SHORT).show();



                    if (myJobList.size()>=1)
                    {

                        myJobAdapter = new MyJobAdapter(MyAllottedJob.this,myJobList);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyAllottedJob.this));
                        mRecyclerView.setAdapter(myJobAdapter);


                        sendServiceDataFromCRMtoDatabase(myJobList);

                    }





                } catch (JSONException e) {
                    pd.dismiss();
                    Toast.makeText(MyAllottedJob.this, "You are not registered user", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(MyAllottedJob.this, "You are not registered user..", Toast.LENGTH_SHORT).show();

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

    private void sendServiceDataFromCRMtoDatabase(List<MyJobModel> myJobList)
    {

//        String url2 = "http://ovottechappdev.poshsmetal.com/index.asmx";





//        Toast.makeText(MyAllottedJob.this, "Inside methode callled", Toast.LENGTH_SHORT).show();


        try {


            Log.e("CustomInvoice", "sendInvoiceDetails:length- " + myJobList.size());
            if (myJobList.size() >= 1) {
//                Toast.makeText(MyAllottedJob.this, "Inside if loop", Toast.LENGTH_SHORT).show();


//                int i=1;
                for (int i=0; i < myJobList.size(); i++) {

//                    Toast.makeText(MyAllottedJob.this, "Inside for loop", Toast.LENGTH_SHORT).show();

                    serviceId2 = String.valueOf(myJobList.get(i).getServiceId());

                    if (serviceId2.length()<1)
                     {
                         serviceId2 = "0";
                     }
                     status_id2 = String.valueOf(myJobList.get(i).getStatus_id());
                    if ((status_id2.length()<1))
                    {
                        status_id2 = "0";
                    }
                      freeze2 = String.valueOf(myJobList.get(i).getFreeze());
                    if (freeze2.length()<1)
                    {
                        freeze2 = "0";
                    }
                    registerId2 = String.valueOf(myJobList.get(i).getRegisterId());
                    if (String.valueOf(registerId2).length()<1)
                    {
                        registerId2 = "0";
                    }
                     customerId2 = String.valueOf(myJobList.get(i).getCustomerId());
                    if (customerId2.length()<1)
                    {
                        customerId2= "0";
                    }
                      pinCode2= String.valueOf(myJobList.get(i).getPinCode());
                    if (pinCode2.length()<1)
                    {
                        pinCode2 = "0";
                    }
                     providerId2 = String.valueOf(myJobList.get(i).getProviderId());
                    if (providerId2.length()<1)
                    {
                      providerId2 = "0";
                    }
                     technicianId2 = String.valueOf(myJobList.get(i).getTechnicianId());
                    if (technicianId2.length()<1)
                    {
                        technicianId2 = "0";
                    }
                      statusCode2 = myJobList.get(i).getStatusCode();
                    if (statusCode2.length()<1)
                    {
                        statusCode2= "NA";
                    }
                     stage2 = myJobList.get(i).getStage();
                    if (stage2.length()<1)
                    {
                        stage2= "NA";
                    }
                  complaintype_code2 = myJobList.get(i).getComplaintype_code();
                    if (complaintype_code2.length()<1)
                    {
                        complaintype_code2= "NA";
                    }
                   customer_name2 = myJobList.get(i).getCustomer_name();
                    if (customer_name2.length()<1)
                    {
                        customer_name2= "NA";
                    }
                     phone12 = myJobList.get(i).getPhone1();
                    if (phone12.length()<1)
                    {
                        phone12= "00";
                    }
                    phone22 = myJobList.get(i).getPhone2();

                    if (phone22.length()<1)
                    {
                        phone22= "00";
                    }
                     address12 = myJobList.get(i).getAddress1();
                    if (address12.length()<1)
                    {
                        address12= "NA";
                    }
                      address22 = myJobList.get(i).getAddress2();
                    if (address22.length()<1)
                    {
                        address22= "NA";
                    }
                     city2 = myJobList.get(i).getCity();
                    if (city2.length()<1)
                    {
                        city2= "NA";
                    }
                     state2 = myJobList.get(i).getState();
                    if (state2.length()<1)
                    {
                        state2= "NA";
                    }
                    product_name2 = myJobList.get(i).getProduct_name();

                    if (product_name2.length()<1)
                    {
                        product_name2= "NA";
                    }
                     producttype_code2 = myJobList.get(i).getProductType_code();
                    if (producttype_code2.length()<1)
                    {
                        producttype_code2= "NA";
                    }
                     model_code2 = myJobList.get(i).getModel_code();
                    if (model_code2.length()<1)
                    {
                        model_code2= "NA";
                    }
                      company_name2 = myJobList.get(i).getCompany_name();
                    if (company_name2.length()<1)
                    {
                        company_name2= "NA";
                    }
                    provider_phone12 = myJobList.get(i).getProvider_phone1();
                    if (provider_phone12 .length()<1)
                    {
                        provider_phone12 = "00";
                    }
                     circle_head2 = myJobList.get(i).getCircle_head();
                    if (circle_head2.length()<1)
                    {
                        circle_head2= "NA";
                    }
                      call_date2 = myJobList.get(i).getCall_date();
//                    if (call_date.length()<1)
//                    {
//                        call_date= "NA";
//                    }
                      description2 = myJobList.get(i).getDescription();
                    if (description2.length()<1)
                    {
                        description2= "NA";
                    }
                    appointment_date2 = myJobList.get(i).getAppointment_date();

                     rescheduled_date2 = myJobList.get(i).getRescheduled_date();

                     complete_date2 = myJobList.get(i).getComplete_date();



                    Log.e("MyAllottedJob", "sendServiceDetails:length- " + myJobList.size());

                    String url2 = Url.INSERT_SERVICE_LIST_URL+"?serviceId="+serviceId2+"&status_id="+status_id2+"&freeze="+freeze2+"&registerid="+registerId2+"&customerid="+customerId2+"&pincode="+pinCode2+"&providerid="+providerId2+"&technicianid="+technicianId2+"&statuscode="+statusCode2+"&stage="+stage2+"&complainttype_code="+complaintype_code2+"&customer_name="+customer_name2+"&phone1="+phone12+"&phone2="+phone22+"&address1="+address12+"&address2="+address22+"&city="+city2+"&state="+state2+"&product_name="+product_name2+"&producttype_code="+producttype_code2+"&model_code="+model_code2+"&company_name="+company_name2+"&provider_phone1="+provider_phone12+"&circle_head="+circle_head2+"&call_date="+call_date2+"&description="+description2+"&appointment_date="+appointment_date2+"&reschedule_date="+rescheduled_date2+"&complete_date="+complete_date2+"&UserCode="+technicianId2;

                    Log.e("CustomInvoice", "sendInvoiceDetails:url- " + url2);

                    Log.e("CustomInvoice", "sendInvoiceDetails:URL- " + url);
                    /////////////////////////////////////////
                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

//                            Toast.makeText(MyAllottedJob.this, "Inside on response", Toast.LENGTH_SHORT).show();


                            try {
//                                JSONObject j = new JSONObject(response);
                                // CustomerResult = j.getJSONArray("customers");
                                JSONArray customerdetail = response.getJSONArray("ServiceDt");
                                JSONObject productd = customerdetail.getJSONObject(0);
                                String message = productd.optString("message");

//                                Toast.makeText(MyAllottedJob.this, "Service Data message "+message, Toast.LENGTH_SHORT).show();

                                if (message.equals("Success"))
                                {
//                                    Toast.makeText(MyAllottedJob.this, "Service Data sent Successfully", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();


                                Toast.makeText(MyAllottedJob.this, "Inside catch "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            new VolleyError();
                            Toast.makeText(MyAllottedJob.this, "Inside volley error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("MyAllottedJob", "onErrorResponse: "+error.getMessage());
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded";
                        }

//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("serviceId", serviceId2);
//                            params.put("status_id",status_id2);
//                            params.put("freeze",freeze2);
//                            params.put("registerid",registerId2);
//                            params.put("customerid",customerId2);
//                            params.put("pincode",pinCode2);
//                            params.put("providerid",providerId2);
//                            params.put("technicianid",technicianId2);
//                            params.put("statuscode",statusCode2);
//                            params.put("stage",stage2);
//                            params.put("complainttype_code",complaintype_code2);
//                            params.put("customer_name",customer_name2);
//                            params.put("phone1",phone12);
//                            params.put("phone2",phone22);
//                            params.put("address1",address12);
//                            params.put("address2",address22);
//                            params.put("city",city2);
//                            params.put("state",state2);
//                            params.put("product_name",product_name2);
//                            params.put("producttype_code",producttype_code2);
//                            params.put("model_code",model_code2);
//                            params.put("company_name",company_name2);
//                            params.put("provider_phone1",provider_phone12);
//                            params.put("circle_head",circle_head2);
//                            params.put("call_date",call_date2);
//                            params.put("description",description2);
//                            params.put("appointment_date",appointment_date2);
//                            params.put("reschedule_date",rescheduled_date2);
//                            params.put("complete_date",complete_date2);
//                            params.put("UserCode",technicianId2);
//
//
//
//
//
//                            return params;
//                        }


                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                    requestQueue.add(stringRequest);



                }



            }
            else
            {
                Toast.makeText(MyAllottedJob.this, "data not sent..", Toast.LENGTH_SHORT).show();
            }


        }
        catch (Exception e)
        {
            pd.dismiss();
            Log.e("MyAllottedJob", "SendSeviceData...catch"+e.getMessage());


        }


    }


    private void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(MyAllottedJob.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            GetLocation();
        } else {
            ActivityCompat.requestPermissions(MyAllottedJob.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    44);



        }
    }
}