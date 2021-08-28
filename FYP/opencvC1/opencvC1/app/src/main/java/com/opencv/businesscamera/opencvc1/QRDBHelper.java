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

public class QRDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "QR_DBName.db";
    public static final String CONTACTS_TABLE_NAME = "qr_record";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_COMPANY = "company";
    public static final String CONTACTS_COLUMN_POSITION = "position";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String CONTACTS_COLUMN_BITMAP = "bitmap";
    private HashMap hp;

    public QRDBHelper (Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE "+ CONTACTS_TABLE_NAME +
                        "(id INTEGER PRIMARY KEY, name text, company text, position text ,phone text,bitmap text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+ CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertQR (String name, String company, String position, String phone, String bitmap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_COMPANY,company);
        contentValues.put(CONTACTS_COLUMN_POSITION, position);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_BITMAP, bitmap);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from qr_record where id="+id+"", null);
        //Cursor res = db.query(CONTACTS_TABLE_NAME, null, null, null, null, null, CONTACTS_COLUMN_NAME+" ASC");
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateQR (Integer id, String name, String company, String position, String phone, String bitmap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_COMPANY,company);
        contentValues.put(CONTACTS_COLUMN_POSITION, position);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_BITMAP, bitmap);
        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTACTS_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public String qrLink(){
        String qr="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select bitmap from qr_record where id=1", null );

        res.moveToFirst();
    return qr;
    }


}
