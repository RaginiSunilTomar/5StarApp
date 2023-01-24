package com.ovot.fiveStarapp_test1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WorkEntryForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner spinner,spinner2;

    private MyFTPClientFunctions ftpclient = null;

    File photoFile = null;
    Uri photoURI=null;
    private String mCurrentPhotoPath="";

    FusedLocationProviderClient fusedLocationProviderClient;

    String latLong = "";
    String date = "";
    String[]  retrieveIncentiveData;
    DatabaseHelper2 databaseHelper2;

    private ImageView back;
    private Button NextBTN;
    String jobStatus="";
    ImageView InvoiceCam;
    ImageButton serialIDU, serialODU;
    SerialsModel serialsModel;
    EditText ETSerila1,ETSerila2,ETInvoice;

    String serial1 = " ";
    String serial2 = " ";
    String serviceId=" ";
    String statusId=" ";
    String customerId=" ";
    String technicianId=" ";
    String register_id;


    private int year;
    private int month;
    private int day;

    public static final String KEY_SERIAL1 = "serial1";
    private static final String PREF_NAME2 = "AndroidHivePref2";
    public static final String KEY_SERIAL2 = "serial2";

    private ProgressDialog pd;
    RequestQueue requestQueue;

    AppCompatTextView tv_customer,tv_address, productName,productType,TVInvoiceDate,Warranty,modelNo;

    TextView TVSerial1, TVSerial2,TVInvoice;


    LinearLayout layout1,layout2;


    List<String> allModels ;
    List<String> allStatus ;
    String model ="";

    String serialIDU_from_api;
    String serialODU_from_api="";


//    String toSerialIDU="", toSerialODU="",toModelNo="",toInvoiceNo="",toInvoiceDate="";

    String toSerialIDU, toSerialODU,toModelNo,toInvoiceNo,toInvoiceDate;

    boolean a,b;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_entry_form);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        allModels = new ArrayList<>();
        allStatus = new ArrayList<>();

        databaseHelper2 = new DatabaseHelper2(this);
        ftpclient = new MyFTPClientFunctions();

        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        spinner = findViewById(R.id.model_spinner);
        spinner2 = findViewById(R.id.job_status_spinner);

        back = findViewById(R.id.back_btn2);
        NextBTN = findViewById(R.id.next_BTN);
        InvoiceCam = findViewById(R.id.camera);
        serialIDU = findViewById(R.id.add_product_btn);
        serialODU = findViewById(R.id.add_product_btn2);
//        final AutoCompleteTextView customerAutoTV = findViewById(R.id.customerTextView);
        ETSerila1 = findViewById(R.id.et_serial1);
        ETSerila2 = findViewById(R.id.et_serial2);
        TVSerial1 = findViewById(R.id.tv_serial1);
        TVSerial2 = findViewById(R.id.tv_serial2);

        productName = findViewById(R.id.product_name);
        productType = findViewById(R.id.product_type);
        TVInvoice = findViewById(R.id.tv_invoice);
        ETInvoice = findViewById(R.id.invoice);
        TVInvoiceDate = findViewById(R.id.invoice_date);



        tv_customer= findViewById(R.id.cu_name);
        tv_address = findViewById(R.id.address);
        Warranty = findViewById(R.id.warranty);
        modelNo = findViewById(R.id.model_num);

        layout1 = findViewById(R.id.model_linear);

        layout2 = findViewById(R.id.model_linear2);


        requestQueue = Volley.newRequestQueue(getApplicationContext());

        pd = new ProgressDialog(WorkEntryForm.this);
        pd.setTitle("Request sending..");
        pd.setMessage("Please wait...");


        allStatus.add("--Select job status--");
        allStatus.add("Review for completion");
        allStatus.add("Not Completed");


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getTechnicianLocation();

//        allStatus.add("REVIEW_FOR_COMPLETION");
//        allStatus.add("NOT_COMPLETED");

        ArrayAdapter aa2 = new ArrayAdapter(WorkEntryForm.this, android.R.layout.simple_spinner_item, allStatus);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner2.setAdapter(aa2);
        spinner2.setOnItemSelectedListener(WorkEntryForm.this);

//        Intent intent = getIntent();
//        serviceId = intent.getStringExtra("service_id");
//        statusId= intent.getStringExtra("status_id");
//        customerId = intent.getStringExtra("customer_id");
//        technicianId = intent.getStringExtra("technician_id");


        retrieveIncentiveData = databaseHelper2.RetrieveIncentive();
        String[] str = retrieveIncentiveData[0].split(",");

           serviceId = str[1];
        statusId = str[2];
        customerId = str[3];
        technicianId = str[4];


        Log.e("WorkEntry", "ServiceId: "+serviceId );


//        final Spinner customerAutoTV = findViewById(R.id.customerTextView);

        serialsModel = new SerialsModel();
        serialIDU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ScanIntent1=new Intent(WorkEntryForm.this,ScanActivity.class);
                startActivity(ScanIntent1);
            }
        });

//        Intent i=getIntent();
//        String serial1=i.getStringExtra("value1");
//        String serial1= serialsModel.getSerialIDU();
//
//        String serial2=i.getStringExtra("serial2");
//        String serial2= serialsModel.getSerialODU();
        SharedPreferences shared = getSharedPreferences(PREF_NAME2, MODE_PRIVATE);
        serial1 = (shared.getString(KEY_SERIAL1, ""));
        serial2 = (shared.getString(KEY_SERIAL2, ""));


        ETSerila1.setText(serial1);
        ETSerila2.setText(serial2);



        Log.e("Work Entry", "onCreate:"+serial1);

//        Toast.makeText(this, "value1  "+serial1, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "value2  "+serial2, Toast.LENGTH_SHORT).show();

//        Text_view1.setText(a);

        serialODU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ScanIntent2=new Intent(WorkEntryForm.this,ScanActivity2.class);
                startActivity(ScanIntent2);
            }
        });

//        // create list of customer
//        ArrayList<String> customerList = new ArrayList<>();
//        customerList.add("Completed");
//        customerList.add("Not Completed");
////        //Create adapter
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(WorkEntryForm.this, android.R.layout.simple_spinner_item, customerList);
//        //Set adapter
//        customerAutoTV.setAdapter(adapter);
//
//        customerAutoTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                jobStatus = adapter.getItem(i).toString();
//            }
//        });
//        customerAutoTV.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                jobStatus = null;
//
//            }
//            @Override
//            public void afterTextChanged(Editable s) {}
//
//        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkEntryForm.this,MyAllottedJob.class);
                startActivity(intent);
                finish();
            }
        });

        getServiceStatus(serviceId);


        InvoiceCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImageForInvoice();  // here we are creating the image file using the camere


            }
        });


        NextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                toSerialIDU="";
                toSerialODU="";
                toModelNo="";
                toInvoiceNo="";
                toInvoiceDate="";

                // validation for the serial IDU
                if (ETSerila1.getText().toString().length()>0)
                {
                    toSerialIDU = ETSerila1.getText().toString();

                }else if (TVSerial1.getText().toString().length()>0)
                {
                    toSerialIDU = TVSerial1.getText().toString();

                }
                else {
                    ETSerila1.setError("Enter Serial No.");
                    TVSerial1.setError("Enter Serial No.");

                }


                // validation for the serial ODU
                if (ETSerila2.getText().toString().length()>0)
                {
                    toSerialODU = ETSerila2.getText().toString();

                }else if (TVSerial2.getText().toString().length()>0)
                {
                    toSerialODU = TVSerial2.getText().toString();

                }

//                toSerialODU = TVSerial2.getText().toString();



//                else {
//                    ETSerila2.setError("Enter Serial No.");
//                    TVSerial2.setError("Enter Serial No.");
//
//                }

                // validation for the model number

                if (modelNo.getText().toString().length()>0)
                {
                    toModelNo =modelNo.getText().toString();
//                    Toast.makeText(WorkEntryForm.this, " model No.1", Toast.LENGTH_SHORT).show();

                }
                else if (!model.equals("Select the model")&& model.length()>0)
                {
                    toModelNo = model;
                }
                else {
                    Toast.makeText(WorkEntryForm.this, "Please select the model No.", Toast.LENGTH_SHORT).show();
                }

                // validation for the Invoice no.
                if (ETInvoice.getText().toString().length()>1)
                {
                    toInvoiceNo = ETInvoice.getText().toString();
                }
                else if (TVInvoice.getText().toString().length()>1)
                {
                    toInvoiceNo = TVInvoice.getText().toString();
                }
                else {
                    ETInvoice.setError("Enter Invoice No.");
                    TVInvoice.setError("Enter Invoice No.");

                }

                // validation for invoice date
                if (TVInvoiceDate.getText().toString().length()>1)
                {
                    toInvoiceDate = TVInvoiceDate.getText().toString();
                }
                else {
                    Toast.makeText(WorkEntryForm.this, "Please select the date", Toast.LENGTH_SHORT).show();

                }
//
//                boolean a,b=false;
//                if (serial1.length()!=1 && !model.equals("Select the model") )
//                {
//                   a=  validateSerial1WithModel(serial1,model);
//
//                }
//
//
//                if (serial2.length()!=1 && !model.equals("Select the model") )
//                {
//                    b=  validateSerial2WithModel(serial2,model);
//
//                }

                if(mCurrentPhotoPath.isEmpty())
                {
                    Toast.makeText(WorkEntryForm.this, "Please select the image for Invoice", Toast.LENGTH_SHORT).show();
                }


                a=false;
                b=false;

//                if (productType.getText().toString().equals("AC"))
//                {
//
//                }
//                else
//                {


                if (toSerialIDU.length()>0 && toModelNo.length()>0 && toInvoiceNo.length()>0 && toInvoiceDate.length()>0 && !mCurrentPhotoPath.isEmpty())
                {
                    a=  validateSerial1WithModel(toSerialIDU,toModelNo);

                }

                // }



//
//                if (toSerialIDU.length()>0 && toModelNo.length()>0 && toInvoiceNo.length()>0 && toSerialODU.length()>0 && toInvoiceDate.length()>0 )
//                {
//                    a=  validateSerial1WithModel(toSerialIDU,toModelNo);
//                    b=  validateSerial2WithModel(toSerialODU,toModelNo);
//
//
//                    if (a==true && b==true)
//                    {
//                        Toast.makeText(WorkEntryForm.this, "value of a, b  "+a+","+b, Toast.LENGTH_SHORT).show();
//
//                    }
//
//                }





            }
        });



    }




    private void EditProductDetailsInCRM()
    {

//        byte[] fileContent = FileUtils.readFileToByteArray(photoFile);
//        @SuppressLint({"NewApi", "LocalSuppress"}) String encodedString = Base64.getEncoder().encodeToString(fileContent);



//        Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        myBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
//        String path = MediaStore.Images.Media.insertImage(getContentResolver(),myBitmap,"IMG_",null);




//        String url = "https://api.crm.ovot.in/register/549433?invoice_no=10&invoice_date=2022-09-19&serial_number=OVOT99719711&serial_number2=OVOT99719711&image_links=xyz";
        String edit_product_url = Url.EDIT_PRODUCT_API+register_id;

        Log.e("WorkEntry", "getParams: "+toInvoiceDate+","+toInvoiceDate+","+toSerialIDU+","+toSerialODU +",Image..  ");


        StringRequest stringRequest = new StringRequest(Request.Method.PUT, edit_product_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(WorkEntryForm.this, "on response ", Toast.LENGTH_SHORT).show();


                try {
                    pd.dismiss();
                    JSONObject j = new JSONObject(response);
                    JSONObject object = j.getJSONObject("data");
//
//                    serialIDU_from_api = object.optString("serialnumber");

                    Toast.makeText(WorkEntryForm.this, "Product details updated successfully", Toast.LENGTH_SHORT).show();

                    completeRemainingJob(jobStatus);

                } catch (JSONException e) {
                    pd.dismiss();
//                    Toast.makeText(WorkEntryForm.this, "SerialIDU is not valid..", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e("Work Entry", "onResponse: "+e.getMessage() );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
//                Toast.makeText(WorkEntryForm.this, "SerialIDU is not valid..", Toast.LENGTH_SHORT).show();
                Log.e("Work Entry", "onResponse: "+error.getMessage() );
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
                params.put("invoice_no",toInvoiceNo);
                Log.e("WorkEntry", "getParams1: "+toInvoiceNo );
                params.put("invoice_date",toInvoiceDate);
                Log.e("WorkEntry", "getParams2: "+toInvoiceDate );
                params.put("serial_number",toSerialIDU);
                Log.e("WorkEntry", "getParams3: "+toSerialIDU);
                params.put("serial_number2",toSerialODU);
                Log.e("WorkEntry", "getParams4: "+toSerialODU);
                params.put("image_links","mmm");


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

    private void completeRemainingJob(String jobStatus)
    {

        sendInvoiceImageDetails();

//        updateJobStatusinDB();
        //****************  Adding Product data here *************


        if (jobStatus.equals("Review for completion"))
        {
            Intent intent = new Intent(WorkEntryForm.this,JobCompletion.class);

//          intent.putExtra("status_code","REVIEW_FOR_COMPLETION");
            intent.putExtra("ServiceId",serviceId);
            intent.putExtra("SerialIDU",toSerialIDU);
            intent.putExtra("SerialODU",toSerialODU);
            intent.putExtra("modelNo",toModelNo);
            intent.putExtra("InvoiceNo",toInvoiceNo);
            intent.putExtra("InvoiceDate",toInvoiceDate);
            intent.putExtra("status_id",statusId);
            intent.putExtra("customer_id",customerId);
            intent.putExtra("technician_id",technicianId);
            intent.putExtra("latLong",latLong);

//          intent.putExtra("InvoiceImage",photoFile);
            startActivity(intent);
            finish();
        }
        else if (jobStatus.equals("Not Completed")){
            Intent intent = new Intent(WorkEntryForm.this,JobNoCompletion.class);

            intent.putExtra("status_code","REQUEST_TO_ASP");
            intent.putExtra("ServiceId",serviceId);
            intent.putExtra("SerialIDU",toSerialIDU);
            intent.putExtra("SerialODU",toSerialODU);
            intent.putExtra("modelNo",toModelNo);
            intent.putExtra("InvoiceNo",toInvoiceNo);
            intent.putExtra("InvoiceDate",toInvoiceDate);
            intent.putExtra("status_id",statusId);
            intent.putExtra("customer_id",customerId);
            intent.putExtra("technician_id",technicianId);
            intent.putExtra("latLong",latLong);

//          intent.putExtra("InvoiceImage",photoFile);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this, "Select The Job Status.. ", Toast.LENGTH_SHORT).show();


        }


    }


    private void getServiceStatus(String serviceId1) {

        register_id = "";
        pd.show();

//        Toast.makeText(WorkEntryForm.this, "Inside  getServiceStatus method", Toast.LENGTH_SHORT).show();


//        String url =Url.SERVICE_STATUS_API+serviceId1;
        String url =Url.SERVICE_STATUS_API+serviceId1;

        /////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(WorkEntryForm.this, "on response ", Toast.LENGTH_SHORT).show();


                try {
                    pd.dismiss();
                    JSONObject j = new JSONObject(response);
                    JSONObject object = j.getJSONObject("data");

                    register_id = object.optString("register_id");

//                    Toast.makeText(WorkEntryForm.this, "register_id  "+register_id, Toast.LENGTH_SHORT).show();

                    String customer_name = object.optString("customer_name");
                    if (customer_name.length()==0)
                    {
                        customer_name= "NA";
                    }
                    else {
                        tv_customer.setText(customer_name);
                    }
                    String customer_address = object.optString("address1");
                    if (customer_address.length()==0)
                    {
                        customer_address= "NA";
                    }
                    else {
                        tv_address.setText(customer_address);
                    }
                    String serial_idu="";
                    serial_idu = object.optString("product_srno");
                    if (serial_idu.length()!=0)
                    {
//                        serialIDU.setVisibility(View.GONE);
                        ETSerila1.setVisibility(View.GONE);
                        TVSerial1.setVisibility(View.VISIBLE);
                        TVSerial1.setText(serial_idu);
                    }

                    String serial_odu ="";
                    serial_odu = object.optString("product_srno2");

                    if (serial_odu.length()!=0)
                    {
//                        serialODU.setVisibility(View.GONE);
                        ETSerila2.setVisibility(View.GONE);
                        TVSerial2.setVisibility(View.VISIBLE);
                        TVSerial2.setText(serial_idu);
                    }

                    String brand="";

                    brand = object.optString("brand");
                    if (productName.length()!=0)
                    {
                        productName.setText(brand);
                    }

                    String product_type="";
                    product_type = object.optString("producttype_code");
                    if (productType.length()!=0)
                    {
                        productType.setText(product_type);
                    }

                    String model_no = object.optString("model_code");
                    if (model_no.contains("DEFAULT")||model_no.contains("MODEL")||model_no.length()==0)
                    {
                        layout1.setVisibility(View.INVISIBLE);
                        layout2.setVisibility(View.VISIBLE);

                        getModelNumbers(product_type);

                    }
                    else {
                        modelNo.setText(model_no);
                    }


                    String invoice_no = "";
                    invoice_no = object.optString("invoice_no");
                    if (invoice_no.length()!=0)
                    {
                        ETInvoice.setVisibility(View.GONE);
                        TVInvoice.setVisibility(View.VISIBLE);
                        TVInvoice.setText(invoice_no);
                    }

                    String invoice_date="";
                    invoice_date = object.optString("invoice_date");
                    if (invoice_date.contains("0000")||invoice_date.length()==0)
                    {
//                        Toast.makeText(WorkEntryForm.this, "Empty date", Toast.LENGTH_SHORT).show();
//                        Calendar method called

                        TVInvoiceDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                final Calendar c = Calendar.getInstance();
                                year = c.get(Calendar.YEAR);
                                month = c.get(Calendar.MONTH);
                                day = c.get(Calendar.DAY_OF_MONTH);


                                DatePickerDialog datePickerDialog = new DatePickerDialog(WorkEntryForm.this,R.style.DialogTheme,new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        TVInvoiceDate.setText(year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                        String todat = TVInvoiceDate.getText().toString();
                                        java.text.SimpleDateFormat sdf3 = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                        java.text.SimpleDateFormat sdf4 = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                        String tdate;
                                        try {
                                            tdate = sdf4.format(sdf3.parse(todat));
                                            Log.e("Incentice", "From Date: "  + tdate );
                                            TVInvoiceDate.setText(tdate);
                                        } catch (Exception e) {


                                        }
                                    }
                                },year,month,day);
                                datePickerDialog.show();

                            }
                        });

                    }
                    else {
                        String[] a = invoice_date.split(" ");
                        invoice_date = a[0];
                        TVInvoiceDate.setText(invoice_date);
                    }

                    String warranty = "";
                    warranty = object.optString("warrantytype");
                    if (warranty.length()!=0)
                    {
                        Warranty.setText(warranty);
                    }

                    String service_type = object.optString("customer_name");
                    String priority = object.optString("customer_name");

                    Log.e("WorkEntry", "onResponse: "+customer_address+", "+serial_idu+", "+serial_odu+", "+brand+", "+product_type+", "+model_no+
                            ", "+invoice_no+","+invoice_date+", "+warranty);





//                    Log.e("Login", "data insertion: "+technician_id +","+name +","+address1+","+address2+","+area+","+landmark+","+city+","+state+","+country+","+pincode+","+active+","+provider_id+","+company_name );
//                    Toast.makeText(WorkEntryForm.this, "message "+customer_name, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    pd.dismiss();
                    Toast.makeText(WorkEntryForm.this, "Server error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e("Work Entry", "onResponse: "+e.getMessage() );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(WorkEntryForm.this, "You are not registered user..", Toast.LENGTH_SHORT).show();

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

    private void getModelNumbers(String product_type) {
//private List<String> getModelNumbers(String product_type) {


        allModels.clear();

        allModels.add("Select the model");
        String url = "";
        url = Url.GET_MODELS_API+product_type;

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
//                        serviceId = data.optLong("service_id");
                        String  newModel = data.optString("model_code");
                        allModels.add(newModel);

//                        Toast.makeText(WorkEntryForm.this, ""+model, Toast.LENGTH_SHORT).show();





                    }

                    if (allModels.size()>1)
                    {
                        ArrayAdapter aa = new ArrayAdapter(WorkEntryForm.this, android.R.layout.simple_spinner_item, allModels);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        spinner.setAdapter(aa);
                        spinner.setOnItemSelectedListener(WorkEntryForm.this);
                        pd.dismiss();
                    }

                } catch (JSONException e) {
                    pd.dismiss();
                    Toast.makeText(WorkEntryForm.this, "Json Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(WorkEntryForm.this, "Volley Error.."+error.getMessage(), Toast.LENGTH_SHORT).show();

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
//                headers.put("Authorization", "Bearer " +"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianRpIjoiYzAyZTE3ODgxYzkyMGVhYjUzM2FmMWI0YWZhNTRlZjBmZGZkZWY1NWE3N2FmMGQzMmYzODQ2NmIwZDc3NDNmODgzY2E0NzVlMzZkZmYwODEiLCJpYXQiOjE2NTE2NTk0NjQsIm5iZiI6MTY1MTY1OTQ2NCwiZXhwIjoxNjgzMTk1NDY0LCJzdWIiOiIiLCJzY29wZXMiOltdfQ.OEWSG_bzEy-4UpB9brz_Cin4gXYq9gMB5aLsPQ-7nZVxttcNzbvBjhXi9jqiQoYQxHP11QmxsckkLGxbA8C-Z-XfZK0a9wUIqLy7mlzJYj5Z0mJNcDntyhnRH3m0FnzrNSCGKlXvKbrigTv5e8TPtX4y0FnB3anEIsmSHZTbFWJRwL_tab5QqQesx4tAleg-bNYQDjsIw6PAnblMv_KqhC7eJF2xXBe4PoIoOpTq4u3swHBCq_tJjRh8KqmoA0o0XEG8yA7n7LRxGsLbkg1moD9rAvMvsI-4LFoUn0SQO6y8GAN3pIHSO5DlcxcHlHeKtlZJtPLQ630qbsJZmyQu8rsVXekK1n4f1ClqHIo60uti4LVZ3lGp_DgKauFJjjhUAYe-lOpA8oLVBnE3MRwVMj9ghR_K4iv1WWNRVnVGGCWOIPA6F8o__Q3ET3EvN00xdCsilm8Xg63HSplEek3jIsAGkqMhyujpGHUHHlom96Zr6-JVR1MMw4ACz3olkOTiGGvTETEVx2tzLDn1x9QXXvbNwvx92gjBYRKE1020CRdRQIs9412EfJia4QYlEWVyqOPsKQthsuiO4f3Zeg1zU-xWfnusc5UnlpZezXJCPYpiu0YQI50oie2fDKHTU_pepWJALpSH3JeGm_uGdzLp3UqV9CAPinAWIcG2Rakz1dA");
                headers.put("Authorization", "Bearer " +Url.BEARER_TOKEN);

                return headers;
            }
        };
        requestQueue.add(stringRequest);



//        return allModels;
    }
    ////////////////

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
//        Spinner s = findViewById(R.id.model_spinner);
//        Spinner s2 = findViewById(R.id.spinner);

        if(spinner.getId() == R.id.model_spinner)
        {

            model = allModels.get(i);

        }else if (spinner.getId() == R.id.job_status_spinner)
        {
            jobStatus=allStatus.get(i);
        }


    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    /////////////////////
    private boolean validateSerial1WithModel(String serial1, String model)
    {
        serialIDU_from_api="";

//        serial1 = "DUMMY000101997933";
//        model = "AMW193E";

//        serial1 = "AM20PI3DUMMY123456";
//        model = "AM20PI3";


        String url = "";
        url = Url.SERIAL_IDU_WITH_MODEL_API+model+"/"+serial1;

        pd.show();
//        Toast.makeText(WorkEntryForm.this, "Inside  getServiceStatus method", Toast.LENGTH_SHORT).show();


        /////////////////////////////////////////
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(WorkEntryForm.this, "on response ", Toast.LENGTH_SHORT).show();


                try {
//                    pd.dismiss();
                    JSONObject j = new JSONObject(response);
                    JSONObject object = j.getJSONObject("data");

                    serialIDU_from_api = object.optString("serialnumber");

                    if (serialIDU_from_api.length()>0)
                    {
//                        Toast.makeText(WorkEntryForm.this, "serialIDU_from_api.."+serialIDU_from_api, Toast.LENGTH_SHORT).show();


//                       completeRemainingJob(jobStatus);

                        EditProductDetailsInCRM();

                    }



                } catch (JSONException e) {
                    pd.dismiss();
                    Toast.makeText(WorkEntryForm.this, "SerialIDU is not valid..", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e("Work Entry", "onResponse: "+e.getMessage() );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(WorkEntryForm.this, "SerialIDU is not valid..", Toast.LENGTH_SHORT).show();
                Log.e("Work Entry", "onResponse: "+error.getMessage() );
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


        if (serialIDU_from_api.length()!=0)
        {
            return true;
        }
        else
        {
            return false;
        }


    }


    /////////////////////////////////////



    private boolean validateSerial2WithModel(String serial2, String model) {
        serial2 = "TEST90500178";
        model = "AMW193E";


        String url = "";
        url = Url.SERIAL_ODU_WITH_MODEL_API+model+"/"+serial2;

        pd.show();
//        Toast.makeText(WorkEntryForm.this, "Inside  getServiceStatus method", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(WorkEntryForm.this, "on response ", Toast.LENGTH_SHORT).show();


                try {
                    pd.dismiss();
                    JSONObject j = new JSONObject(response);
                    JSONObject object = j.getJSONObject("data");

                    serialODU_from_api = object.optString("serialnumber");

//                    Toast.makeText(WorkEntryForm.this, "serialODU_from_api.."+serialODU_from_api, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    pd.dismiss();
                    Toast.makeText(WorkEntryForm.this, "SerialIDU is not valid..", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e("Work Entry", "onResponse: "+e.getMessage() );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(WorkEntryForm.this, "SerialIDU is not valid..", Toast.LENGTH_SHORT).show();
                Log.e("Work Entry", "onResponse: "+error.getMessage() );
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


        if (serialODU_from_api.length()!=0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    ///////////////////////// Started the Invoice Image code from here
    private void selectImageForInvoice()
    {

        final CharSequence[] options = {"Take Photo for Invoice"};
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkEntryForm.this);
        builder.setIcon(R.drawable.icon);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals("Take Photo for Invoice")) {
                    if (ContextCompat.checkSelfPermission(WorkEntryForm.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WorkEntryForm.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
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
                                    photoURI = FileProvider.getUriForFile(WorkEntryForm.this,"com.ovot.fiveStarapp_test1.fileprovider",photoFile);

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

    protected void onActivityResult(int requestCode,  int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Toast.makeText(WorkEntryForm.this, "Invoice Image captured", Toast.LENGTH_SHORT).show();
            }
//            else if (requestCode == 2){
//                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile2.getAbsolutePath());
//                Toast.makeText(TechnicianReg.this, "Pan Image captured", Toast.LENGTH_SHORT).show();
//            }
//            else if (requestCode == 3){
//                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile3.getAbsolutePath());
//                Toast.makeText(TechnicianReg.this, "Cheque Image captured", Toast.LENGTH_SHORT).show();
//            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                // checkCameraPresent("1");
                selectImageForInvoice();
            }
//            else if (requestCode == 2){
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
//                    // checkCameraPresent("1");
//                    selectImageForPan();
//                }
//            }
//            else if (requestCode == 3){
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
//                    // checkCameraPresent("1");
//                    selectImageForCheque();
//                }
//            }
            else
            {

                Log.e("ISDReg", "onRequestPermissionsResult: "+" Not working" );

            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Intent intentReport = new Intent(WorkEntryForm.this,JobLogReport.class);
                intentReport.putExtra("sid",serviceId);
                startActivity(intentReport);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getTechnicianLocation()
    {
//        latLong = "";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                Log.d(TAG, "onComplete: location : "+String.valueOf(location));
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(WorkEntryForm.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


                        latLong = addresses.get(0).getLatitude()+","+addresses.get(0).getLongitude();
//                        textView1.setText(Html.fromHtml("<font color = #6200EE><b>Latitude :</b<br></font>" + addresses.get(0).getLatitude()));
//                        textView2.setText(Html.fromHtml("<font color = #6200EE><b>Longitude :</b<br></font>" + addresses.get(0).getLongitude()));
//                        textView3.setText(Html.fromHtml("<font color = #6200EE><b>Country :</b<br></font>" + addresses.get(0).getCountryName()));
//                        textView4.setText(Html.fromHtml("<font color = #6200EE><b>Locality :</b<br></font>" + addresses.get(0).getLocality()));
//                        textView5.setText(Html.fromHtml("<font color = #6200EE><b>Address :</b<br></font>" + addresses.get(0).getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    //********************** send invoice image details *******************




    private void sendInvoiceImageDetails() {



        final String host = "ftp.mobileeye.in";


        final String username = "devtechinvoice";
        final String password =  "tech#invoice@2022";


        if (host.length() < 1) {
            Toast.makeText(WorkEntryForm.this, "Please Enter Host Address!",
                    Toast.LENGTH_LONG).show();
        } else if (username.length() < 1) {
            Toast.makeText(WorkEntryForm.this, "Please Enter User Name!",
                    Toast.LENGTH_LONG).show();
        } else if (password.length() < 1) {
            Toast.makeText(WorkEntryForm.this, "Please Enter Password!",
                    Toast.LENGTH_LONG).show();
        } else {


            File compressFile5 = saveBitmapToFile(photoFile);

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
                                        toInvoiceNo+"_" + serviceId + "_" + technicianId);
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




}