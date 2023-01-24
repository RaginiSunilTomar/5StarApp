package com.ovot.fiveStarapp_test1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;



/**
 * Created by Ragini Tomar 2022.
 */

public class SessionManager {
    // Shared Preferences
    public  SharedPreferences pref,pref1,pref2;
    // Editor for Shared preferences
    public SharedPreferences.Editor editor;
    public SharedPreferences.Editor editor1;
    public SharedPreferences.Editor editor2;

    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
//    DatabaseOperation databaseOperation;

//    ArrayList<UserModel> arrayList;
//    DatabaseHelper database_helper;

    private static final String PREF_NAME = "AndroidStarPref";
    private static final String PREF_NAME1 = "AndroidHivePref1";
    private static final String PREF_NAME2 = "AndroidHivePref2";

    //    private static final String IS_REGISTER = "IsRegistered";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_AGREEMENT = "IsAgreement";
    private static final String IS_SERIAL = "IsSerials";

    String userId;


    public static final String KEY_TECH_ID = "tech_id";
    public static final String KEY_AGREEMENT = "agreement";


    public static final String KEY_SERIAL1 = "serial1";
    public static final String KEY_SERIAL2 = "serial2";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref1 = _context.getSharedPreferences(PREF_NAME1, PRIVATE_MODE);
        pref2 = _context.getSharedPreferences(PREF_NAME2,PRIVATE_MODE);
        editor = pref.edit();
        editor1 = pref1.edit();
        editor2 = pref2.edit();


//        database_helper = new DatabaseHelper(context.getApplicationContext());
//        arrayList = new ArrayList<>(database_helper.getUserData());


    }

    public void createLoginSession(String tech_id){
        Log.e("createLoginSession ","method called");
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_TECH_ID, tech_id);
        Log.e("createLoginSession ","mobile and userId.." +tech_id );
        editor.commit();
        Log.e("createLoginSession ","session created");
    }

    public void createAgreementSession(String status){
        Log.e("createLoginSession ","method called");
        editor1.putBoolean(IS_AGREEMENT, true);
        editor1.putString(KEY_AGREEMENT, status);
        Log.e("createAgreementSession ","mobile and userId.." +status );
        editor1.commit();
        Log.e("createLoginSession ","session created");
    }


    public void createSerialsSession1(String serial1){
        Log.e("createLoginSession ","method called");
        editor2.putBoolean(IS_SERIAL, true);
        editor2.putString(KEY_SERIAL1, serial1);
        Log.e("createAgreementSession ","mobile and userId.." +serial1 );
        editor2.commit();
        Log.e("createLoginSession ","session created");
    }
    public void createSerialsSession2(String serial2){
        Log.e("createLoginSession ","method called");
        editor2.putBoolean(IS_SERIAL, true);
        editor2.putString(KEY_SERIAL2, serial2);
        Log.e("createAgreementSession ","mobile and userId.." +serial2 );
        editor2.commit();
        Log.e("createLoginSession ","session created");
    }



    public void checkLogin(){

        if(!this.isLoggedIn()){
            //Log.e("Entered in ","isLoggedIn");
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Log.e("createLoginSession ","Not logged in");
            _context.startActivity(i);


        }else {
            //Log.e("Entered in ","checkLoginElse");


                Intent inMain=new Intent(_context,Intermediate.class);
                inMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                inMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Log.e("createLoginSession ","Logged in");
                _context.startActivity(inMain);


        }
    }
    ////////////////////////////

    public void checkAgreement(){

        if(this.isAgreement()){
            //Log.e("Entered in ","isLoggedIn");
            Intent i = new Intent(_context, DashboardActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Log.e("createLoginSession ","Not logged in");
            _context.startActivity(i);


        }

    }


    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    public boolean isAgreement(){
        return pref1.getBoolean(IS_AGREEMENT, false);
    }

}
