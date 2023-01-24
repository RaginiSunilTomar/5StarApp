package com.ovot.fiveStarapp_test1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
// This class is the replacement of DatabaseOperation class
    //database name
    public static final String DATABASE_NAME = "db_technician_data";
    //database version
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "tb_technician_data";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query,query2;
        //creating table
//        String technician_id,name,phone1,phone2,address1,address2,area,landmark,city, state,country,pincode,active,provider_id,
//                            company_name;
        query = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY,  technician_id TEXT , name TEXT, phone1 TEXT, phone2  TEXT,address1 TEXT,address2 TEXT,area TEXT,landmark TEXT,city TEXT,state TEXT,country TEXT,pincode TEXT, active TEXT, provider_id TEXT, company_name TEXT  )";
        db.execSQL(query);

    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add the new note
    public void addNotes(String technician_id, String name,String phone1,String phone2,String address1,String address2,String area,String landmark,String city, String state,String country,String pincode,String active,String provider_id,String company_name
    ) {
        SQLiteDatabase sqLiteDatabase = this .getWritableDatabase();

//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.delete(TABLE_NAME, "ID=" + 1, null);

        ContentValues values = new ContentValues(); // UserId TEXT , UserCode TEXT, UserRole TEXT, ContactNumber  TEXT
        values.put("technician_id", technician_id);
        values.put("name", name);
        values.put("phone1", phone1);
        values.put("phone2", phone2);
        values.put("address1", address1);
        values.put("address2", address2);
        values.put("area", area);
        values.put("landmark", landmark); //landmark
        values.put("city", city);
        values.put("state", state);
        values.put("country", country);
        values.put("pincode", pincode);
        values.put("active", active);
        values.put("provider_id", provider_id);
        values.put("company_name", company_name);


        Log.e("Databasehelper", "addNotes: called");
        //inserting new row
        sqLiteDatabase.insert(TABLE_NAME, null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    //get the all notes
//    public ArrayList<UserModel> getUserData() {
//        ArrayList<UserModel> arrayList = new ArrayList<>();
//
//        // select all query
//        String select_query= "SELECT *FROM " + TABLE_NAME;
//
//        SQLiteDatabase db = this .getWritableDatabase();
//        Cursor cursor = db.rawQuery(select_query, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                UserModel noteModel = new UserModel();
//                noteModel.setID(cursor.getString(0));
//                noteModel.setUserId(cursor.getString(1));
//                noteModel.setUserCode(cursor.getString(2));
//                noteModel.setUserRole(cursor.getString(3));
//                noteModel.setContactNumber(cursor.getString(4));
//                arrayList.add(noteModel);
//            }while (cursor.moveToNext());
//        }
//        return arrayList;
//    }

    //delete the note
    public void delete(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(TABLE_NAME, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

    //update the note
    public void updateNote(String title, String des, String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("Title", title);
        values.put("Description", des);
        //updating row
        sqLiteDatabase.update(TABLE_NAME, values, "ID=" + ID, null);
        sqLiteDatabase.close();
    }
}
