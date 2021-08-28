package com.opencv.businesscamera.opencvc1;


import android.content.Intent;
import android.content.res.AssetManager;
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


public class CameraMainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "CameraMainActivity";
    //    JavaCameraView javaCameraView;
    private CameraBridgeViewBase javaCameraView;
    Mat mRgba;
    public static String imgFileName;
    Bitmap myBitmap;
    ImageView imageOcr;
    ArrayList<String> textLine;
    TessOCR tess;
    String resultTess = "*** Lalala test@gmail.com&&^ test2@gmail.com((& PAMERAH VISUVASUM";
    String phoneNumber= "8th Lorong, +6012 7655 617 8th ";
    public static String address="5th Floor Wisma EEC, No. 2 Lorong Dungun Kid,";
    public static String name="";
    public static String email="";
    public static String telNo="";



    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }

            }

        }
    };


    //Used to load the 'native-lib' library on application startup.
    MainActivity main = new MainActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_main_view);

        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);


        ImageButton cameraBtn = (ImageButton) findViewById(R.id.camera_button);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveImage(mRgba);
                Toast mToast = Toast.makeText(CameraMainActivity.this, main.stringFromJNI(), Toast.LENGTH_LONG);
                mToast.show();
                setContentView(R.layout.ocr_layer);

                LoadBitImage();
                // getTextFromImage(imageOcr);
                Button tryBtn = (Button) findViewById(R.id.try_button);
                tryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle dataBundle = new Bundle();
                        dataBundle.putInt("id", 0);
                        //changed
                        Intent i = new Intent(CameraMainActivity.this, CameraContactActivity.class);
                        //Intent i = new Intent(CameraMainActivity.this, ImageProcessing.class);
                        i.putExtras(dataBundle);
                        startActivity(i);
                    }
                });

                Button cancelBtn = (Button) findViewById(R.id.cancel_ocr);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(CameraMainActivity.this, MainActivity.class);
                        //Intent i = new Intent(CameraMainActivity.this, ImageProcessing.class);
                        startActivity(i);
                    }
                });
            }
        });
//
//        //image and text
//
////        Utils.bitmapToMat(bitmap, mRgba);
        AssetManager assetManager = getAssets();
        tess = new TessOCR(assetManager);
        validateAddress(address);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    public void surfaceChanged(){}


    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV loaded succesffully");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.d(TAG, "Unable to load opencv");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallBack);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        return mRgba;
    }

    public void SaveImage(Mat mat) {
        Mat mIntermediateMat = new Mat();
        Imgproc.cvtColor(mRgba, mIntermediateMat, Imgproc.COLOR_RGBA2BGR, 3);

        File folder = new File(Environment.getExternalStorageDirectory() + "/BusinessCardScanner/");
        if (!folder.exists())     //check if file already exists
        {
            folder.mkdirs();     //if not, create it
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = timestamp + ".jpg";
        File imageFile = new File(folder, filename); //create new image

        Boolean bool = null;
        //imgFileName = imageFile.toString();
        imgFileName= imageFile.getAbsolutePath();
        Log.d(TAG,""+imgFileName);

        bool = Imgcodecs.imwrite(imgFileName, mIntermediateMat);
        if (bool == true)
            Log.d(TAG, "SUCCESS writing image to external storage");
        else
            Log.d(TAG, "Fail writing image to external storage");


    }


    public void LoadBitImage()
    {
        File m = new  File(imgFileName);

        if(m.exists()){

            cropImage();
        }

    }
    public Bitmap thresholding(Bitmap img)
    {
        Mat m = new Mat();
        Utils.bitmapToMat(img , m);


        Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2GRAY);

        Mat dstMat = m.clone(); // create dest Mat
        Imgproc.bilateralFilter(m,dstMat, 10, 250 , 10);  //blurring effect
        Imgproc.medianBlur(m,dstMat,5);
        Imgproc.adaptiveThreshold(m,m, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 41, 13);
        Utils.matToBitmap(m, img);
        //Imgproc.threshold(m, dstMat, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
        Imgproc.distanceTransform(m, dstMat, Imgproc.CV_DIST_L2, Imgproc.CV_DIST_MASK_PRECISE);
        return img;


    }
    public void cropImage()
    {
        File img = new File(imgFileName);
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(Uri.fromFile(img), "image/*");
        cropIntent.putExtra("crop", true);
        Uri outputFileUri = Uri.fromFile(img);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cropIntent, 100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 100)
        {
            File m = new  File(imgFileName);
            myBitmap = BitmapFactory.decodeFile(m.getAbsolutePath());
            myBitmap = thresholding(myBitmap);
            ImageView myImage = (ImageView) findViewById(R.id.img_preview);
            myImage.setImageBitmap(myBitmap);
            //myImage.setRotation(90);
            String s =tess.getResults(myBitmap);
            TextView textOcr = (TextView) findViewById(R.id.ocr_text);
            Log.d("Test", s);
            textOcr.setText(s);
            separateString(s);
            tess.onDestroy();
            textOcr.setVisibility(View.INVISIBLE);
        }

    }

    public void separateString(String s)
    {
        String []parts= s.split("\n");
        textLine = new ArrayList<String>();
        for (int i =0; i < parts.length; i++)
        {
            textLine.add(parts[i]);
            Log.d("Test2", i +"."+ parts[i]);
            Log.d("TestPart2",textLine.get(i));
            validateName(textLine.get(i));
            validateEmail(textLine.get(i));
            extractPhoneNumber(textLine.get(i));
            validateAddress(textLine.get(i));

        }



    }
    public void validateName (String arrays){
            String s = arrays;
            Matcher m = Pattern.compile("[A-Z]{3}.[A-Z]+ [A-Z ]+").matcher(s);
            Pattern p= Pattern.compile("\\d{3}");
            //Matcher m = Pattern.compile("(\\D{5})[A-Z ]+[ ]+[A-Z ]+").matcher(s);
            //textLine = new ArrayList<String>();

                while (m.find()) {
                    if(!s.equals(" ")||!s.equals(p.pattern())) {
                        Log.d("TestPartName", s);
                        Log.d("TestName", m.group());

                        name=m.group(0);

                    }
                }
    }

    public void validateEmail(String s){

        Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(s);
        while (m.find()) {
            if(!s.equals(" ")) {
                Log.d("TestPart3", s);
                Log.d("Test3", m.group(0));

                email=m.group(0);

            }
        }
    }

    public static void  extractPhoneNumber(String s){

        Pattern regex = Pattern.compile("[+ ](?:\\d+\\s*)+[0-9.+-. ]{4}+");
        Matcher regexMatcher = regex.matcher(s);
        if (regexMatcher.find()) {
            if(!s.equals(" ")) {
                Log.d("TestPart4", s);
                Log.d("Test4", regexMatcher.group(0));

                telNo=regexMatcher.group(0);

            }
        }
    }

    public void validateAddress(String s){
        StringBuilder builder = new StringBuilder();
        //Matcher m = Pattern.compile("[a-zA-Z0-9&,. ]+(\\d{5})+[a-zA-Z,. ]+").matcher(s);
        Matcher m=Pattern.compile("[\\d]+[A-Za-z0-9\\s,\\.]+?[\\d\\-]+").matcher(s);
        Pattern email= Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        Pattern phone = Pattern.compile("[+ ](?:\\d+\\s*)+[0-9.+-. ]{6,15}+");
        while (m.find()) {
            Log.d("Test5",m.group());
            if(!s.equals(" ")||!s.equals(phone.pattern())||!s.equals(email.pattern())) {
                Log.d("TestPartAddress", s);
                Log.d("TestAddress", m.group(0));

                //builder.append(m.group(0));

            }
        } //address=builder.toString();
    }


//    public void getTextFromImage(View v) {
////        Bitmap bitmap = BitmapFactory.decodeResource(getApplication().getResources(),R.drawable.single_text);
//
//        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
//
//
//        if(!textRecognizer.isOperational())
//        {
//            Log.d(TAG,"OCR is not yet installed");
//        }
//        else
//        {
//            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
//            SparseArray<TextBlock> items = textRecognizer.detect(frame);
//            StringBuilder sb = new StringBuilder();
//
//            for(int i=0 ;i<=items.size();++i)
//            {
//                TextBlock myItem =items.valueAt(i);
//                sb.append(myItem.getValue());
//                sb.append("\n");
//            }
////            textOcr.setText(sb.toString());
//
//        }
//    }

    public void onBackPressed(){
        // super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }


}