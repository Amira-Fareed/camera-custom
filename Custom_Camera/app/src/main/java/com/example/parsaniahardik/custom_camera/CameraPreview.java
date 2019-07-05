package com.example.parsaniahardik.custom_camera;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Math.random;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    ImageView iv;
    boolean c ,p ,l;

    public CameraPreview(Context context, Camera camera,ImageView img_view) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        iv = img_view;
        c=true;
        p=true;
        l=true;
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
//        try {
//            // create the surface and start camera preview
//            if (mCamera == null) {
//                mCamera.setPreviewDisplay(holder);
//                mCamera.startPreview();
//            }
//        } catch (IOException e) {
//            Log.d(VIEW_LOG_TAG, "Error setting camera preview: " + e.getMessage());
//        }
        mCamera.setPreviewCallback(previewCallback);
        //mCamera.setPreviewCallbackWithBuffer(previewCallback);

    }



    String image_string;

    private Camera.PreviewCallback  previewCallback= new Camera.PreviewCallback()
    {
        @Override
        public void onPreviewFrame(byte[] data,Camera cam)
        {
            Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
            Camera.Parameters params = mCamera.getParameters();
            if (params.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(params);
            YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21,previewSize.width,previewSize.height, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0,0,previewSize.width,previewSize.height),60,baos);
            byte[] image_byte_array = baos.toByteArray();
            JSONObject input = new JSONObject();
            //image_string = Base64.encodeToString(image_byte_array, Base64.DEFAULT);
            try {
                input = buidJsonObject(image_byte_array, c, l, p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(image_byte_array,0,image_byte_array.length);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            Log.d("amira", input.toString());
            iv.setImageBitmap(rotatedBitmap);
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/"+random()+".png");
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                fos.close();
//                Log.d("amira", image_string.toString());
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    };
    public void refreshCamera(Camera camera) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        refreshCamera(mCamera);
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        mCamera = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // mCamera.release();
    }


    private JSONObject buidJsonObject(byte[] img,boolean car , boolean lane , boolean pedestrian) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image", img);
        jsonObject.put("car", car);
        jsonObject.put("lane", lane);
        jsonObject.put("pedestrian", pedestrian);

        return jsonObject;
    }
}