package com.ovot.fiveStarapp_test1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
private Button loginBTN, otpBTN;
private EditText mobET,OTP;
DatabaseHelper database_helper;
    SessionManager sf;
    String mobileNo="";
    int otp1;
    int getOTPFromUser ;

    RequestQueue requestQueue;
    private ProgressDialog pd;
    String technician_id,name,phone1,phone2,address1,address2,area,landmark,city, state,country,pincode,active,provider_id,
            company_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database_helper = new DatabaseHelper(this);
        sf = new SessionManager(LoginActivity.this);


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        pd = new ProgressDialog(LoginActivity.this);

        mobET = findViewById(R.id.et_mob);
        otpBTN = findViewById(R.id.btn_otp);
        OTP = findViewById(R.id.et_otp);

        pd.setTitle("Request sending..");
        pd.setMessage("Please wait...");



        otpBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNo = mobET.getText().toString();
                if (mobileNo.length()!=10)
                {
                    mobET.setError("Invalid mobile no.");
                }
                else {
                    validateTechnician(mobileNo);
                }
            }
        });


        loginBTN = findViewById(R.id.login);
        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, Intermediate.class);
//                startActivity(intent);
                getOTPFromUser = Integer.parseInt (OTP.getText().toString());

                if (getOTPFromUser==otp1)
                {
                    pd.show();

                    Toast.makeText(LoginActivity.this, "valid OTP", Toast.LENGTH_SHORT).show();

                    insertTechnicianData(technician_id,name,phone1,phone2,address1,address2,area,landmark,city,state,country,pincode,provider_id,company_name,active);

//                            sf.createLoginSession(mobileNo, userId);
//
//                            Intent intent = new Intent(LoginActivity.this,DashboardAcitivity.class);
//                            startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "please enter valid OTP", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }
        });
    }



    private void validateTechnician(String mobileNo) {
//        Toast.makeText(this, "method called", Toast.LENGTH_SHORT).show();


        pd.show();

        String url =Url.LOGIN_API+mobileNo;   // 9045711356

        /////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(LoginActivity.this, "on response ", Toast.LENGTH_SHORT).show();


                try {
                    pd.dismiss();
                    JSONObject j = new JSONObject(response);
//                            JSONArray customerdetail = j.getJSONArray("data");
                    JSONObject object = j.getJSONObject("data");

//                    String technician_id,name,phone1,phone2,address1,address2,area,landmark,city, state,country,pincode,active,provider_id,
//                            company_name;
                    technician_id = object.optString("technician_id");
                    name = object.optString("name");
                    if (name.length()==0)
                    {
                        name = "NA";
                    }
                    phone1 = object.optString("phone1");
                    if (phone1.length()==0)
                    {
                        phone1 = "NA";
                    }
                    phone2 = object.optString("phone2");
                    if (phone2.length()==0)
                    {
                        phone2 = "NA";
                    }
                    address1 = object.optString("address1");
                    if (address1.length()==0)
                    {
                        address1 = "NA";
                    }
                    address2 = object.optString("address2");
                    if (address2.length()==0)
                    {
                        address2 = "NA";
                    }
                    area = object.optString("area");
                    if (area.length() == 0 || area.equals("null"))
                    {
                        area = "NA";
                    }
                    landmark = object.optString("landmark");
                    if (landmark.length()==0)
                    {
                        landmark = "NA";
                    }
                    city = object.optString("city");
                    if (city.length()==0)
                    {
                        city = "NA";
                    }
                    state = object.optString("state");
                    if (state.length()==0)
                    {
                        state = "NA";
                    }
                    country = object.optString("country");
                    if (country.length()==0)
                    {
                        country = "NA";
                    }
                    pincode = object.optString("pincode");
                    if (pincode.length()==0)
                    {
                        pincode = "NA";
                    }
                    active = object.optString("active");
                    if (active.length()==0)
                    {
                        active = "NA";
                    }
                    provider_id = object.optString("provider_id");
                    if (provider_id.length()==0)
                    {
                        provider_id = "NA";
                    }
                    company_name = object.optString("company_name");
                    if (company_name.length()==0)
                    {
                        company_name = "NA";
                    }

                    database_helper.addNotes(technician_id,name,phone1,phone2,address1,address2,area,landmark,city, state,country,pincode,active,provider_id, company_name);
                    otp1 = generateRandomNumber();

//                    new Connection().execute();
                    getSMS_OTP();

                    Log.e("Login", "data insertion: "+technician_id +","+name +","+address1+","+address2+","+area+","+landmark+","+city+","+state+","+country+","+pincode+","+active+","+provider_id+","+company_name );
//                    Toast.makeText(LoginActivity.this, "message "+technician_id+name +city+pincode, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "You are not registered user", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "You are not registered user..", Toast.LENGTH_SHORT).show();

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
                headers.put("Authorization", "Bearer " +Url.BEARER_TOKEN);
                return headers;
            }
        };
        requestQueue.add(stringRequest);



    }

    /////////////////////////////////


    private class Connection extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String apiKey = "key=" + "ua9087wh32424UAWH39087";
                String numbers = "&ph=" + mobileNo;
                Log.e("Login", "mobile no: " + numbers );
                String sender = "&sndr=" + "TWOVOT";
                Log.e("LoginActivity", "OTP: "+otp1 );
                String message = "&text= " + otp1+" is your One Time Password for online Registration of Promax App Dont share this with anyone - TWOVOT";
                String data = apiKey + numbers + sender + message;
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) new URL("https://SMS.VRINFOSOFT.CO.IN/unified.php?").openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Login", "onClick:1 " + e.getMessage() );
                }

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                conn.getOutputStream().write(data.getBytes("UTF-8"));
                final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    stringBuffer.append(line);
                    // Toast.makeText(LoginActivity.this,line,Toast.LENGTH_LONG).show();
                }
                Log.e("Login", "SMS sent successfully.." );
                rd.close();
                // pd.dismiss();
            } catch (NetworkOnMainThreadException | IOException e) {
                //IOException e
                System.out.println("Error SMS "+e);
                Log.e("Login", "onClick:2 " + e.getMessage());
                // pd.dismiss();

            }
            // pd.dismiss();

            return null;
        }



    }

    public int generateRandomNumber() {
        int randomNumber;
        int range = 9;  // to generate a single number with this range, by default its 0..9
        int length = 4; // by default length is 4
        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }
        randomNumber = Integer.parseInt(s);
        return randomNumber;
    }


    private void insertTechnicianData(String technician_id, String name, String phone1, String phone2, String address1, String address2, String area, String landmark, String city, String state, String country, String pincode, String provider_id, String company_name, String active) {
        try {

            if (active.equals("1"))
            {
                active = "TRUE";
            }

            Log.e("Login", "insertTechnicianData: "+technician_id+","+name+","+phone1+","+phone2+","+address1+","+address2+","+area+","+landmark+","+city+","+state+","+pincode+","+provider_id+","+company_name+","+active+","+country);
//            String url = Url.URL_Login;


//            params.put("technician_id",technician_id);
//            params.put("Name",name);
//            params.put("phone1",phone1);
//            params.put("Phone2",phone2);
//            params.put("Address1",address1);
//            params.put("Address2",address2);
//            params.put("Area",area);
//            params.put("LandMark",landmark);
//            params.put("City",city);
//            params.put("State",state);
//            params.put("Country",country);
//            params.put("Pincode",pincode);
//            params.put("provider_id",provider_id);
//            params.put("company_name","abc");
//            params.put("Active",active);
//            params.put("LoginUserCode",technician_id);


//            String url = Url.INSERT_TECHNICIAN_DATA_URL+"?technician_id="+technician_id+"&Name="+name+"&phone1="+phone1+"&Phone2="+phone2
//                    +"&Address1="+address1+"&Address2="+address2+"&Area="+area+"&LandMark="+landmark+"&City="+city+"&state="+state+"&Country="+
//                    "&Pincode="+pincode+"&company_name=abc"+"&Active="+active+"&LoginUserCode="+technician_id;


            String url = Url.INSERT_TECHNICIAN_DATA_URL+"?technician_id="+technician_id+"&Name="+name+"&phone1="+phone1+"&Phone2="+phone2
                    +"&Address1="+address1+"&Address2="+address2+"&Area="+area+"&LandMark="+landmark+"&City="+city+"&state="+state+"&Country="+
                    "&Pincode="+pincode+"&provider_id="+provider_id+"&company_name=abc"+"&Active="+active+"&LoginUserCode="+technician_id;

            Log.e("LoginActivity", "insertTechnicianDataUrl: " +url);  //provider_id=1234
            /////////////////////////////////////////
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()  {
                @Override
                public void onResponse(JSONObject response) {


                    try {
                        pd.dismiss();
//                        JSONObject j = new JSONObject(response);
                        // CustomerResult = j.getJSONArray("customers");
//                        JSONArray customerdetail = j.getJSONArray("TechnicianId");
                        JSONArray customerdetail = response.getJSONArray("TechnicianDt");
                        for (int i = 0; i < customerdetail.length(); i++) {
                            JSONObject userd = customerdetail.getJSONObject(i);
                           String message= userd.getString("message");

                            Log.e("LoginActivity", "message: " +message+", "+technician_id);

                           if (message.equals("Success"))
                           {
                               Toast.makeText(LoginActivity.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                               sf.createLoginSession( technician_id);
                               Intent intermediate_intent = new Intent(LoginActivity.this,Intermediate.class);
                               startActivity(intermediate_intent);
                               finish();
                           }
                           else if (message.equals("Already Added"))
                           {
                               Toast.makeText(LoginActivity.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                               sf.createLoginSession(technician_id);

                               String agreement_status = userd.getString("AggreementAccepted");
                               Log.e("Login", "agreement_status: "+agreement_status );
                               if (agreement_status.equalsIgnoreCase("No"))
                               {
                                   Intent intermediate_intent = new Intent(LoginActivity.this,Intermediate.class);
                                   startActivity(intermediate_intent);
                                   finish();
                               }
                               else {
                                   sf.createAgreementSession("done");
                                   Intent dashboard_intent = new Intent(LoginActivity.this,DashboardActivity.class);
                                   startActivity(dashboard_intent);
                                   finish();
                               }


                           }else
                           {
                               Toast.makeText(LoginActivity.this, "Failed..", Toast.LENGTH_SHORT).show();

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
                    Log.e("Login", "onErrorResponse: "+error.getMessage() );
                     Toast.makeText(LoginActivity.this, "Server error.." + error.toString() + error.networkResponse, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("technician_id",technician_id);
//                    params.put("Name",name);
//                    params.put("phone1",phone1);
//                    params.put("Phone2",phone2);
//                    params.put("Address1",address1);
//                    params.put("Address2",address2);
//                    params.put("Area",area);
//                    params.put("LandMark",landmark);
//                    params.put("City",city);
//                    params.put("State",state);
//                    params.put("Country",country);
//                    params.put("Pincode",pincode);
//                    params.put("provider_id",provider_id);
//                    params.put("company_name","abc");
//                    params.put("Active",active);
//                    params.put("LoginUserCode",technician_id);
//
//
//                    return params;
//                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
            requestQueue.add(stringRequest);


        } catch (Exception e) {
            Log.e("Register Activity ", ": Exception while " + "Sending data to server : " + e.getMessage());
            Toast.makeText(LoginActivity.this, "Oops....Failed Try Later.", Toast.LENGTH_LONG).show();
        }
    }
////////////////////////////////////


    private void getSMS_OTP()
    {
        Log.e("LoginActivity", "otp1 : "+otp1);
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

                    pd.dismiss();
                    if (object.equals("Invalid template"))
                    {
                        Toast.makeText(LoginActivity.this, "SMS Server error", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    pd.dismiss();
                    Log.e("Login", "SMSError1: "+e.getMessage() );
                    Toast.makeText(LoginActivity.this, "SMS Server error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "SMS Server error..", Toast.LENGTH_SHORT).show();
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
                params.put("number",mobileNo);
                params.put("message",otp1+" is your One Time Password for online Registration of the Amstrad 5Star App. Please don't share this with anyone. -Amstrad");


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