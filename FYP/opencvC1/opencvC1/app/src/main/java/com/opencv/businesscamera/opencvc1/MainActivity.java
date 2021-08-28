package com.opencv.businesscamera.opencvc1;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;



import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.opencv.imgproc.Imgproc.CV_MEDIAN;


public class MainActivity extends AppCompatActivity  {
    private static String TAG="MainActivity ";
    QRDBHelper qrDB;
    int id_To_Update=1;

    //Used to load the 'native-lib' library on application startup.
    static {System.loadLibrary("native-lib");}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scan = (Button)findViewById(R.id.scanning);
        Button contactList = (Button)findViewById(R.id.contactList);
        qrDB = new QRDBHelper(this);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CameraMainActivity.class);
                startActivity(intent);
            }
        });
        contactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DisplayContactActivity.class);
                startActivity(intent);
            }
        });

//        Button qr =(Button)findViewById(R.id.qr_btn);
//        qr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),QRView.class);
//                startActivity(intent);
//            }
//        });


    }

    public native String stringFromJNI();

    public native static void loadImage();



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.qr_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.create_qr:           //edit contact
                Log.d(TAG + "Edit", "Edit page is opened");

                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    int Value = extras.getInt("id");

                    if (Value > 0) {
                        Cursor rs = qrDB.getData(Value);
                        id_To_Update = Value;
                        rs.moveToFirst();

                        final String name = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                        if (name == "") {
                            Intent intent = new Intent(getApplicationContext(), QRView.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), QRDisplayPage.class);
                            startActivity(intent);
                        }
                    }
                }else
                {
                    Intent intent = new Intent(getApplicationContext(), QRView.class);
                    startActivity(intent);
                }
                        return true;
                        default:
                            return super.onOptionsItemSelected(item);



        }
    }





    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(MainActivity.this);
    }



}