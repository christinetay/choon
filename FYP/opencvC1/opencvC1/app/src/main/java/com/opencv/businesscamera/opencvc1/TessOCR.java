package com.opencv.businesscamera.opencvc1;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.googlecode.tesseract.android.PageIterator;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Tay on 30/4/2018.
 */

public class TessOCR {

    public static final String PACKAGE_NAME = "com.opencv.businesscamera.opencvc1";
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/AndroidOCR/";
    public static final String lang = "eng";

    private static final String TAG = "TESSERACT";
    private AssetManager assetManager;

    private TessBaseAPI mTess;

    public TessOCR(AssetManager assetManager) {

        Log.i(TAG, DATA_PATH);

        this.assetManager = assetManager;

        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/"};

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }
        }

        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {
                InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
                OutputStream out = new FileOutputStream(new File(DATA_PATH + "tessdata/", lang + ".traineddata"));

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }

        mTess = new TessBaseAPI();
        mTess.setDebug(true);
        mTess.init(DATA_PATH, lang);
        mTess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ1234567890',.?;/@+()& ");

    }


    public String getResults(Bitmap bitmap)
    {
        mTess.setImage(bitmap);
        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO_OSD);
        String result = mTess.getUTF8Text();
        return result;
    }

    public void onDestroy() {
        if (mTess != null)
            mTess.end();
    }
}
