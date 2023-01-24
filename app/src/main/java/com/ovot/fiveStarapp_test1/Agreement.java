package com.ovot.fiveStarapp_test1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Agreement extends AppCompatActivity {

    ProgressDialog pd ;
    RequestQueue requestQueue;
    String dateWithTime;
    String name;
    private Button IAgrree;

    private TextView textView,textView2,textView3,textView4,textView5,textView6;
    SessionManager sf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String mobile = intent.getStringExtra("mobile");
        String email = intent.getStringExtra("email");
        String pan = intent.getStringExtra("pan");
        String techId = intent.getStringExtra("techId");

        Log.e("Agreement", "onCreate: "+name+","+mobile+","+email+","+pan+","+techId );



        requestQueue = Volley.newRequestQueue(getApplicationContext());
        sf = new SessionManager(getApplicationContext());

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        SimpleDateFormat dt = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault());

        String formattedDate = df.format(c);
        dateWithTime = dt.format(c);

        Log.e("Date", "onCreate: "+formattedDate );

        textView = findViewById(R.id.agreement_content);
        textView2 = findViewById(R.id.agreement_content2);
        textView3 = findViewById(R.id.agreement_content3);
        textView4 = findViewById(R.id.agreement_content4);
        textView5 = findViewById(R.id.agreement_content5);
        textView6 = findViewById(R.id.agreement_content6);
        IAgrree = findViewById(R.id.update_agreement);

        IAgrree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAgreement(techId);
            }
        });
    }



    public void updateAgreement(String new_isd){
        try {
            pd = new ProgressDialog(Agreement.this);

            pd.setTitle("Request sending");
            pd.setMessage("Please wait to get details..");
            pd.show();
            //  String url = "http://ovotapplive.poshsmetal.com/index.asmx/GetISDDetails";
            // String url = "http://ovotappdev.poshsmetal.com/index.asmx/UpdateISDDAgreement";
//            String url = Url.UPDATE_ISD_AGREEMENT_URL;

//

            String url = Url.UPDATE_AGREEMENT_URL+"?TechnicianId="+new_isd+"&AcceptanceStatus=yes&UserCode="+new_isd+"&IPAdd=abc";
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
                        JSONArray customerdetail = response.getJSONArray("TechId");
                        for (int i = 0; i < customerdetail.length(); i++) {
                            JSONObject isdDetails = customerdetail.getJSONObject(i);
                            //userId = userd.getString("UserId");
                            message = isdDetails.getString("message");

                            if (message.equals("Success"))
                            {

//                            new message1(SENDMAIL).execute();

                                sf.createAgreementSession("done");
                                Intent intent = new Intent(Agreement.this,DashboardActivity.class);
                                startActivity(intent);
                                finish();


                                pd.dismiss();


                            }else {
                                pd.dismiss();
                            }





                            ////////////////////////

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
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
//                    params.put("TechnicianId",new_isd);
//                    params.put("AcceptanceStatus","yes");
//                    params.put("UserCode",new_isd);
//                    params.put("IPAdd","abc");
//
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
            Toast.makeText(Agreement.this, "Oops....Registration Failed Try Later.", Toast.LENGTH_LONG).show();
        }
    }
}