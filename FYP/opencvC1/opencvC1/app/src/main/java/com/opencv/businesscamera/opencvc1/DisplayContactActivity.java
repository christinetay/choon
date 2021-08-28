package com.opencv.businesscamera.opencvc1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Tay on 11/5/2018.
 */

public class DisplayContactActivity extends AppCompatActivity{

    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layer);

        mydb = new DBHelper(this);
        final ArrayList array_list = mydb.getAllCotacts();  //get all contacts from mydb
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        obj = (ListView)findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                ArrayList array_ID= mydb.contactID();

                String id_Temp = String.valueOf(array_ID.get(arg2)); //arg2 : position , assign database id(string) to id_Temp
                int id_To_Search = Integer.parseInt(id_Temp);  //convert id_Temp into int

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);  //assign a number to the bundle of activity + changes everytime

                Intent intent = new Intent(getApplicationContext(),ContactActivity.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId()) {
            case R.id.item1:Bundle dataBundle = new Bundle();
//                Button save = (Button)findViewById(R.id.save_btn);
//                save.setVisibility(View.VISIBLE);
//
//                Button c = (Button)findViewById(R.id.cancel_save_contact);
//                c.setVisibility(View.VISIBLE);
//                dataBundle.putInt("id", 0);

                Intent intent = new Intent(getApplicationContext(),ContactActivity.class);
                intent.putExtras(dataBundle);

                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    public boolean onKeyDown(int keycode, KeyEvent event) {
//        if (keycode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true);
//        }
//        return super.onKeyDown(keycode, event);
//    }



    public void onBackPressed(){
       // super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
