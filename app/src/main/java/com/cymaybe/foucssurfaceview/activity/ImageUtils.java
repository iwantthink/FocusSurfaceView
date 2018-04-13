package com.cymaybe.foucssurfaceview.activity;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by renbo on 2018/4/13.
 */

public class ImageUtils {
    /**
     * 先通过createBitmap进行裁剪，再通过 canvas 去缩放
     *
     * @param classifier
     * @param input
     * @param isTest     是否输出bitmap到文件
     * @return
     */
    public static Bitmap dealBitmap(ImageClassifier classifier, Bitmap input, boolean isTest) {
        int dstWidth = 64;
        int dstHeight = 64;
        if (classifier != null) {
            dstWidth = classifier.getImageSizeX();
            dstHeight = classifier.getImageSizeY();
        }
        int srcWidth = input.getWidth();
        int srcHeight = input.getHeight();


//        Bitmap cliped = Bitmap.createBitmap(input, (srcWidth - srcWidth / 3) / 2,
//                (srcHeight - srcHeight / 3) / 2, srcWidth / 3, srcHeight / 3);

        Bitmap scaled = Bitmap.createScaledBitmap(input, dstWidth, dstHeight, false);
        if (isTest) {
            try {
                saveToLocal(input, "Original_" + System.currentTimeMillis() / 1000);
//                saveToLocal(cliped, "Cliped_" + System.currentTimeMillis() / 1000);
                saveToLocal(scaled, "Scaled_" + System.currentTimeMillis() / 1000);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return scaled;

    }

    public static void saveToLocal(Bitmap bitmap, String bitName) throws IOException {
        File file = new File("/sdcard/DCIM/Camera/" + bitName + ".jpg");

        File dir = new File("/sdcard/DCIM/Camera/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                Log.d("ImageUtils", "saveToLocal Success");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
