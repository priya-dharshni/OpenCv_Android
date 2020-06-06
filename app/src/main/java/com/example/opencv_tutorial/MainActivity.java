package com.example.opencv_tutorial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  {
    private Bitmap mCameraBitmap;
     ImageView i1;
    private static final String TAG = "MyActivity" ;
    private static final int TAKE_PICTURE_REQUEST_B = 100;








    @Override
    public void onResume()
    {
        super.onResume();
        if(!OpenCVLoader.initDebug())
        {
            Toast.makeText(getApplicationContext(),"problem",Toast.LENGTH_SHORT).show();

        }
        else
        {
            Log.d(TAG, "OpenCV library found inside package. Using it!");

        }



    }



    private View.OnClickListener mCaptureImageButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startImageCapture();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
       i1=findViewById(R.id.imageView);


        findViewById(R.id.gallery).setOnClickListener(mCaptureImageButtonClickListener);


      OpenCVLoader.initDebug();

    }


    private void startImageCapture() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PICTURE_REQUEST_B);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_REQUEST_B) {
            if (resultCode == RESULT_OK) {

                if (mCameraBitmap != null) {
                    mCameraBitmap.recycle();
                    mCameraBitmap = null;
                }
                Bundle extras = data.getExtras();
                mCameraBitmap = (Bitmap) extras.get("data");
                i1.setImageBitmap(mCameraBitmap);
                MyConstants.selectedImageBitmap = mCameraBitmap;
            } else {
                mCameraBitmap = null;
            }
        }
    }




    public void openGallery(View v)
    {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,101);

    }







    public void convertoGray(View v)
    {

        Intent intent = new Intent(getApplicationContext(), ImagePrcocessing.class);
        startActivity(intent);
    }

}
