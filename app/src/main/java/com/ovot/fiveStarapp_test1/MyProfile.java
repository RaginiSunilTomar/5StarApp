package com.ovot.fiveStarapp_test1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {


    String technician_id = " ";
    ProgressDialog pd ;
    RequestQueue requestQueue;
    private TextView isdName,regMob,EmailId,BankName,bankAC,IFSC,UPI,panNo,AdharNo;


    public static final String KEY_TECH_ID = "tech_id";
    private static final String PREF_NAME = "AndroidStarPref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);



        isdName = findViewById(R.id.isd2);
        regMob = findViewById(R.id.isd_mob2);
        EmailId = findViewById(R.id.email2);
        BankName = findViewById(R.id.bank_name2);
        bankAC = findViewById(R.id.bank_ac2);
        IFSC = findViewById(R.id.ifsc2);
        UPI = findViewById(R.id.upi2);
        panNo = findViewById(R.id.pan2);
        AdharNo = findViewById(R.id.adhar2);



        SharedPreferences shared = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        technician_id = (shared.getString(KEY_TECH_ID, ""));

        Log.e("Intermediate", "onCreate: "+technician_id );
        //////////////////////////
        requestQueue = Volley.newRequestQueue(getApplicationContext());





        try {
            pd = new ProgressDialog(MyProfile.this);

            pd.setTitle("Request sending");
            pd.setMessage("Please wait to get details..");
            pd.show();
            //  String url = "http://ovotapplive.poshsmetal.com/index.asmx/GetISDDetails";
//                String url = "http://ovotappdev.poshsmetal.com/index.asmx/GetISDDetails";
            String url =  Url.GET_TECHNICIAN_DETAILS_URL+"?TechId="+technician_id;           /////////////////////////////////////////
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    String userId;

                    String message;

                    try {
                        pd.dismiss();
//                        JSONObject j = new JSONObject(response);
                        // CustomerResult = j.getJSONArray("customers");
                        JSONArray customerdetail = response.getJSONArray("TechnicianDt");
                        for (int i = 0; i < customerdetail.length(); i++) {
                            JSONObject isdDetails = customerdetail.getJSONObject(i);
                            //userId = userd.getString("UserId");
                            message = isdDetails.getString("message");

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
//                    params.put("TechnicianId", technician_id);
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
            Toast.makeText(MyProfile.this, "Oops....Registration Failed Try Later.", Toast.LENGTH_LONG).show();
        }
    }
}