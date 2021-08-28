package com.opencv.businesscamera.opencvc1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tay on 10/5/2018.
 */

public class CameraContactActivity extends AppCompatActivity {
    int from_Where_I_Am_Coming = 0;
    private DBHelper mydb ;

    TextView inputName ;
    TextView inputPhone;
    TextView inputEmail;
    TextView inputAddress;
    int id_To_Update = 0;
    CameraMainActivity cmain;
    private static final String TAG="Contact Activity";
    String nameCaptured=cmain.name;
    String emailCaptured=cmain.email;
    String telCaptured=cmain.telNo;
    String addressCaptured;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_field);

        Button b = (Button)findViewById(R.id.save_btn);
        b.setVisibility(View.VISIBLE);
        Button cancel = (Button)findViewById(R.id.cancel_save_contact);
        cancel.setVisibility(View.VISIBLE);
        Button share = (Button)findViewById(R.id.share_button);
        share.setVisibility(View.INVISIBLE);

        inputName = (TextView) findViewById(R.id.input1);
        inputPhone = (TextView) findViewById(R.id.input3);
        inputEmail = (TextView) findViewById(R.id.input2);
        inputAddress = (TextView) findViewById(R.id.input4);

        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        Log.d(TAG,"details="+bundle2string(extras));
        Log.d(TAG,nameCaptured);
        if(nameCaptured!=null){  inputName.setText(nameCaptured);  }
        if(emailCaptured!=null){  inputEmail.setText(emailCaptured);  }

        if(telCaptured!=null){  inputPhone.setText(telCaptured);  }

        if(addressCaptured!=null){  inputAddress.setText(addressCaptured);  }


    }


    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                //do nothing
            } else{
                if(mydb.insertContact(inputName.getText().toString(), inputEmail.getText().toString(),
                        inputPhone.getText().toString(), inputAddress.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Save contact successfully",
                            Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(), "not done",
                            Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(),DisplayContactActivity.class);
                startActivity(intent);
            }
        }
    }

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }


}

