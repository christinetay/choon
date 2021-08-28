package com.opencv.businesscamera.opencvc1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.StringTokenizer;

public class QRView extends AppCompatActivity {

    private static final String TAG = "QRViewActivity";
    QRDBHelper qrDB;
    int id_To_Update = 1;
    ImageView imageView;
    Button button;
    EditText editName;
    EditText editCompany;
    EditText editPosition;
    EditText editPhone;
    String EditTextValue ;
    Thread thread ;
    public final static int QRcodeWidth = 500 ;
    public final static int QRcodeHeight = 500;
    public static Bitmap bitmap ;
    public static String bitmapLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_edit_page);

        qrDB = new QRDBHelper(this);

        imageView = (ImageView)findViewById(R.id.imageView);
        editName = (EditText)findViewById(R.id.edit_Name);
        editCompany = (EditText)findViewById(R.id.edit_Company);
        editPosition = (EditText)findViewById(R.id.edit_Position);
        editPhone = (EditText)findViewById(R.id.edit_phone);
        button = (Button)findViewById(R.id.button);

        imageView.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                imageView.setVisibility(View.VISIBLE);
                EditTextValue = editName.getText().toString() + "\n" + editCompany.getText().toString() + "\n" + editPosition.getText().toString() + "\n" + editPhone.getText().toString();
                Log.d(TAG, EditTextValue);

                try {
                    bitmap = TextToImageEncode(EditTextValue);
                    SaveImage(bitmap);

//                    imageView.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

                Log.d(TAG,Integer.toString(qrDB.numberOfRows()));
                qrDB.insertQR(editName.getText().toString(),
                        editCompany.getText().toString(), editPosition.getText().toString(),
                        editPhone.getText().toString(), bitmapLink);
                Intent intent = new Intent(getApplicationContext(),QRDisplayPage.class);
                startActivity(intent);

            }
        });
    }


    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeHeight, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        //File myDir = new File(Environment.getExternalStorageDirectory() + "/BusinessCardScanner/QR/");
        File myDir = new File(root + "/BusinessCardScanner/QR/");
        if (!myDir.exists())     //check if file already exists
        {
            myDir.mkdirs();     //if not, create it
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File(myDir, fname);
        bitmapLink= file.getAbsolutePath();
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
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

//    public void createQR(View view) {
//
//        imageView.setVisibility(View.VISIBLE);
//        EditTextValue = editName.getText().toString()+"\n"+editCompany.getText().toString()+"\n"+editPosition.getText().toString()+"\n"+editPhone.getText().toString();
//        Log.d(TAG,EditTextValue);
//
//        try {
//            bitmap = TextToImageEncode(EditTextValue);
//            SaveImage(bitmap);
//
//            //imageView.setImageBitmap(bitmap);
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//        Bundle extras = getIntent().getExtras();
//        if(extras !=null) {
//            int Value = extras.getInt("id");
//            if(Value>0){
//                if(qrDB.updateQR(1,editName.getText().toString(),
//                        editCompany.getText().toString(), editPosition.getText().toString(),
//                        editPhone.getText().toString(), bitmapLink)){
//                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(),DisplayContactActivity.class);
//                    startActivity(intent);
//                } else{
//                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
//                }
//            } else{
//                if(qrDB.insertContact(editName.getText().toString(),
//                        editCompany.getText().toString(), editPosition.getText().toString(),
//                        editPhone.getText().toString(), bitmapLink)){
//                    Toast.makeText(getApplicationContext(), "Save contact successfully",
//                            Toast.LENGTH_LONG).show();
//                } else{
//                    Toast.makeText(getApplicationContext(), "not done",
//                            Toast.LENGTH_SHORT).show();
//                }
//                Intent intent = new Intent(getApplicationContext(),QRDisplayPage.class);
//                startActivity(intent);
//            }
//        }
//
//    }

}
