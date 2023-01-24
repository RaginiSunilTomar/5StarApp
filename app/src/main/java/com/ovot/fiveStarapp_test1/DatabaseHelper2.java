package com.ovot.fiveStarapp_test1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper2 extends SQLiteOpenHelper {
// This class is the replacement of DatabaseOperation class
    //database name
    public static final String DATABASE_NAME = "db_serviceData";
    //database version
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "tb_serviceData";

    public DatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query,query2;
        //creating table
//        query = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY,  UserId TEXT , UserCode TEXT, UserRole TEXT, ContactNumber  TEXT)";
        query = "CREATE TABLE " + TABLE_NAME + "(SrNo INTEGER primary key autoincrement, service_id TEXT,  status_id TEXT,customer_id TEXT,technician_id TEXT)";
        db.execSQL(query);

    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add the new note
    public void addServiceData(String  service_id,String  status_id,String  customer_id,String  technician_id) {
        SQLiteDatabase sqLiteDatabase = this .getWritableDatabase();

//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        sqLiteDatabase.delete(TABLE_NAME, "ID=" + 1, null);

        Log.e("Databasehelper2", "addIncentive");


        ContentValues values = new ContentValues(); // UserId TEXT , UserCode TEXT, UserRole TEXT, ContactNumber  TEXT
        values.put("service_id", service_id);
        values.put("status_id",status_id );
        values.put("customer_id",customer_id );
        values.put("technician_id" , technician_id);
        //inserting new row
        sqLiteDatabase.insert(TABLE_NAME, null , values);
        //close database connection

        sqLiteDatabase.close();
    }
    public void deleteIncentiveTableData(){
        SQLiteDatabase sqLiteDatabase = this .getWritableDatabase();

//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.delete(TABLE_NAME,null,null);

        sqLiteDatabase.close();
    }

    //get the all notes
    public String[] RetrieveIncentive() {

        // select all query
        String select_query= "SELECT *FROM " + TABLE_NAME;
        Log.e("DatabaseHelper2", "RetrieveIncentive query: "+select_query );

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cr = db.rawQuery(select_query, null);

        String[] array = new String[cr.getCount()];
        int i = 0;

        //ProductSrNo,Model,InvoiceNo,SaleDate,IncentiveAmt,Status,Reason,Customer,ApproveBy,ApproveDate,Dealer,Phone
        while (cr.moveToNext()) {

            @SuppressLint("Range") String srNo = cr.getString(cr.getColumnIndex("SrNo"));
            @SuppressLint("Range") String productSrNo = cr.getString(cr.getColumnIndex("service_id"));
            @SuppressLint("Range") String model = cr.getString(cr.getColumnIndex("status_id"));
            @SuppressLint("Range") String invoiceNo = cr.getString(cr.getColumnIndex("customer_id"));
            @SuppressLint("Range") String saleDate = cr.getString(cr.getColumnIndex("technician_id"));



            array[i] = srNo+","+productSrNo+","+model+","+invoiceNo+","+saleDate;
            i++;

        }


//////////////////////////////////////

        Log.e("Database", "RetrieveRegData: " + array );
        return array;

    }
//    public StringBuilder getReason(String a){
//
//        StringBuilder details= new StringBuilder();
//        String select_query= "SELECT Reason, Remark FROM " + TABLE_NAME+ " WHERE ProductSrNo ='" +a+ "'LIMIT 1";
//        Log.e("DatabaseHelper2", "RetrieveReason query: "+select_query );
////        String select_query2= "SELECT Reason, Remark FROM " + TABLE_NAME+ " WHERE ProductSrNo ="+a+" LIMIT 1";
////        String select_query2= "SELECT Reason, Remark FROM " + TABLE_NAME+ " WHERE ProductSrNo ='" +a+ "'LIMIT 1";
////        String select_query2 =  "SELECT Reason, Remark FROM "+TABLE_NAME+" WHERE ProductSrNo ='" +a+ "'LIMIT 1";
//
//        SQLiteDatabase db = this .getWritableDatabase(); // ProductSrNo
//        Cursor cr = db.rawQuery(select_query, null);
//        cr.moveToFirst();
//        @SuppressLint("Range") String reason = cr.getString(cr.getColumnIndex("Reason"));
//        @SuppressLint("Range") String remark = cr.getString(cr.getColumnIndex("Remark"));
//
//        details.append(reason);
//        details.append(",").append(remark);
//        cr.close();
//        return details;
//    }
//
//    //delete the note
//    public void delete(String ID) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        //deleting row
//        sqLiteDatabase.delete(TABLE_NAME, "ID=" + ID, null);
//        sqLiteDatabase.close();
//    }

    //update the note
    public void updateNote(String service_id,String status_id,String  customer_id,String technician_id) {


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("service_id", service_id);
        values.put("status_id", status_id);
        values.put("customer_id", customer_id);
        values.put("technician_id", technician_id);
        //updating row
//        sqLiteDatabase.update(TABLE_NAME, values, "ID=" + ID, null);
        sqLiteDatabase.update(TABLE_NAME, values, null, null);

        sqLiteDatabase.close();
    }
}
