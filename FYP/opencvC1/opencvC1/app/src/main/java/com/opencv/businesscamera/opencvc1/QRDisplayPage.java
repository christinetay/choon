package com.opencv.businesscamera.opencvc1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Tay on 25/5/2018.
 */

public class QRDisplayPage extends AppCompatActivity{
    ImageView imageView ;
    QRView qr;
    QRDBHelper qrDB;
    Bitmap qrBitmap;
    String bitmapLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_view_page);

        imageView = (ImageView)findViewById(R.id.qr_displayed);
        imageView.setImageBitmap(qr.bitmap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_qr, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.delete_qr:
                int id_To_Updated =0;
                Bundle extras = getIntent().getExtras();
                if(extras !=null) {
                    int Value = extras.getInt("id");
                    if(Value>0){
                qrDB.deleteContact(id_To_Updated);}}
                Intent i = new Intent(getApplicationContext(),QRView.class);
                startActivity(i);
                return true;
            case R.id.share_qr:
                /**
                 * Show share dialog BOTH image and text
                 */
                Uri imageUri = Uri.parse(qr.bitmapLink);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                //Target whatsapp:
                shareIntent.setPackage("com.whatsapp");
                //Add text and then Image URI
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Please scan QR code for details");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }



    public void onBackPressed(){
        // super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
