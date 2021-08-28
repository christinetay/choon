package com.opencv.businesscamera.opencvc1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tay on 11/5/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_EMAIL = "email";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String CONTACTS_COLUMN_ADDRESS = "address";
    private HashMap hp;

    public DBHelper (Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE "+ CONTACTS_TABLE_NAME +
                        "(id INTEGER PRIMARY KEY, name text, email text, phone text ,address text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact (String name,  String email, String phone, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email",email);
        contentValues.put("phone", phone);
        contentValues.put("address", address);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null);
        //Cursor res = db.query(CONTACTS_TABLE_NAME, null, null, null, null, null, CONTACTS_COLUMN_NAME+" ASC");
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name,  String email, String phone, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email",email);
        contentValues.put("phone", phone);
        contentValues.put("address", address);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(  "contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts order by LOWER(name) ASC", null );

        //Cursor res = db.query(CONTACTS_TABLE_NAME, new String[] {*}, CONTACTS_COLUMN_ID + "=?",
          //      new String[] { String.valueOf(id) }, null, null, CONTACTS_COLUMN_NAME, null);

        res.moveToFirst();

        while(res.isAfterLast() == false){
            //array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> contactID(){
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts order by LOWER(name) ASC", null );

        res.moveToFirst();

        while(res.isAfterLast() == false){
            //array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_ID)));
            res.moveToNext();
        }

        return array_list;
    }

}
