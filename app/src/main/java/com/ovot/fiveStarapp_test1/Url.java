package com.ovot.fiveStarapp_test1;
// created by Ragini-Tomar
public interface Url {

    // For the Webservices
//    String BASE_URL = "http://ovottechappdev.poshsmetal.com/index.asmx/";

    String BASE_URL  =    "http://twtech.in/FiveStarApp/rest/";

    String INSERT_TECHNICIAN_DATA_URL = BASE_URL+"InsertTechnicianDetails";
    String GET_TECHNICIAN_DETAILS_URL = BASE_URL+"GetTechnicianDetails";
    String UPDATE_TECHNICIAN_DETAILS_URL= BASE_URL+"UpdateTechnicianDetails";
    String UPDATE_AGREEMENT_URL = BASE_URL+"UpdateTechnicianAgreement";
    String GET_INCENTIVE_REPORT = BASE_URL+"GetIncentiveReport";
    String INSERT_SERVICE_LIST_URL = BASE_URL+"InsertServiceDetails";

    String INSERT_BILL_TYPE_URL = BASE_URL+"Insert_billType";
    String UPDATE_SERVICE_DETAILS_URL = BASE_URL+"updateServiceDetails";


    // For the Api's
    String BASE_API = "https://api.crm.ovot.in";
    String LOGIN_API = BASE_API+"/technician/rmn/";
    String SERVICE_LIST_API = BASE_API+"/technician/servicelist/";
    String GET_MODELS_API = BASE_API+"/info/models_by_type/";

    String SERVICE_STATUS_API = BASE_API+"/service/";

    String SERIAL_IDU_WITH_MODEL_API = BASE_API+"/info/serialnumberIDU/";
    String SERIAL_ODU_WITH_MODEL_API = BASE_API+"/info/serialnumberODU/";
    String EDIT_PRODUCT_API = BASE_API+"/register/";

    String SMS_API = BASE_API+"/sms";
    String ACTION_CODE_API = BASE_API+"/info/actioncode_for_technician/";
    String ADD_JOB_LOG_API = BASE_API+"/service/joblog/";
    String JOB_STATUS_UPDATE_API = BASE_API+"/service/status/";

    String JOB_LOG_REPORT_API = BASE_API+"/service/joblog/";



    String BEARER_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianRpIjoiYzAyZTE3ODgxYzkyMGVhYjUzM2FmMWI0YWZhNTRlZjBmZGZkZWY1NWE3N2FmMGQzMmYzODQ2NmIwZDc3NDNmODgzY2E0NzVlMzZkZmYwODEiLCJpYXQiOjE2NTE2NTk0NjQsIm5iZiI6MTY1MTY1OTQ2NCwiZXhwIjoxNjgzMTk1NDY0LCJzdWIiOiIiLCJzY29wZXMiOltdfQ.OEWSG_bzEy-4UpB9brz_Cin4gXYq9gMB5aLsPQ-7nZVxttcNzbvBjhXi9jqiQoYQxHP11QmxsckkLGxbA8C-Z-XfZK0a9wUIqLy7mlzJYj5Z0mJNcDntyhnRH3m0FnzrNSCGKlXvKbrigTv5e8TPtX4y0FnB3anEIsmSHZTbFWJRwL_tab5QqQesx4tAleg-bNYQDjsIw6PAnblMv_KqhC7eJF2xXBe4PoIoOpTq4u3swHBCq_tJjRh8KqmoA0o0XEG8yA7n7LRxGsLbkg1moD9rAvMvsI-4LFoUn0SQO6y8GAN3pIHSO5DlcxcHlHeKtlZJtPLQ630qbsJZmyQu8rsVXekK1n4f1ClqHIo60uti4LVZ3lGp_DgKauFJjjhUAYe-lOpA8oLVBnE3MRwVMj9ghR_K4iv1WWNRVnVGGCWOIPA6F8o__Q3ET3EvN00xdCsilm8Xg63HSplEek3jIsAGkqMhyujpGHUHHlom96Zr6-JVR1MMw4ACz3olkOTiGGvTETEVx2tzLDn1x9QXXvbNwvx92gjBYRKE1020CRdRQIs9412EfJia4QYlEWVyqOPsKQthsuiO4f3Zeg1zU-xWfnusc5UnlpZezXJCPYpiu0YQI50oie2fDKHTU_pepWJALpSH3JeGm_uGdzLp3UqV9CAPinAWIcG2Rakz1dA";
}
