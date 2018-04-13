/* Copyright 2017 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.cymaybe.foucssurfaceview.activity;

import android.app.Activity;

import java.io.IOException;

/**
 * This classifier works with the Inception-v3 slim model.
 * It applies floating point inference rather than using a quantized model.
 */
public class ImageClassifierFloatInception extends ImageClassifier {

    /**
     * The inception net requires additional normalization of the used input.
     */
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    /**
     * An array to hold inference results, to be feed into Tensorflow Lite as outputs.
     * This isn't part of the super class, because we need a primitive array here.
     */
    public float[][] labelProbArray = null;

    /**
     * Initializes an {@code ImageClassifier}.
     *
     * @param activity
     */
    public ImageClassifierFloatInception(Activity activity) throws IOException {
        super(activity);
        labelProbArray = new float[1][4];
    }

    @Override
    protected String getModelPath() {
        // you can download this file from
        // https://storage.googleapis.com/download.tensorflow.org/models/tflite/inception_v3_slim_2016_android_2017_11_10.zip
//    return "inceptionv3_slim_2016.tflite";
        return "frozen-icon-detect_after.tflite";
    }

    @Override
    protected String getLabelPath() {
        return "frozen-icon-detect.txt";
    }

    @Override
    protected int getImageSizeX() {
        return 64;
    }

    @Override
    protected int getImageSizeY() {
        return 64;
    }

    @Override
    protected int getNumBytesPerChannel() {
        // a 32bit float value requires 4 bytes
        return 4;
    }

    @Override
    protected void addPixelValue(int pixelValue) {
//        imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
//        imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
//        imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
        imgData.putFloat(((pixelValue & 0xFF)));
        imgData.putFloat(((pixelValue >> 8) & 0xFF));
        imgData.putFloat(((pixelValue >> 16) & 0xFF));

//        float r = Color.red(pixelValue);
//        float g = Color.green(pixelValue);
//        float b = Color.blue(pixelValue);

//        imgData.putFloat(r/255);
//        imgData.putFloat(g/255);
//        imgData.putFloat(b/255);


//        imgData.putFloat((r * 0.299f + 0.587f * g + b * 0.114f) / 255);


    }

    @Override
    protected float getProbability(int labelIndex) {
        return labelProbArray[0][labelIndex];
    }

    @Override
    protected float[] getLabelProbArray() {
        return labelProbArray[0];
    }

    @Override
    protected void setProbability(int labelIndex, Number value) {
        labelProbArray[0][labelIndex] = value.floatValue();
    }

    @Override
    protected float getNormalizedProbability(int labelIndex) {
        // TODO the following value isn't in [0,1] yet, but may be greater. Why?
        return getProbability(labelIndex);
    }

    @Override
    protected void runInference() {
        tflite.run(imgData, labelProbArray);
    }
}
