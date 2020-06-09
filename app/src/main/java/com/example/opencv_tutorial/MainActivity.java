package com.example.opencv_tutorial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

public class MainActivity extends AppCompatActivity {
    private Bitmap mCameraBitmap;
    ImageView i1;
    private static final String TAG = "MyActivity";
    private static final int TAKE_PICTURE_REQUEST_B = 100;
    private static final int MY_CAMERA_REQUEST_CODE = 101;


    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(getApplicationContext(), "problem", Toast.LENGTH_SHORT).show();

        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");

        }


    }


    private View.OnClickListener mCaptureImageButtonClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            startImageCapture();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        i1 = findViewById(R.id.imageView);


        findViewById(R.id.gallery).setOnClickListener(mCaptureImageButtonClickListener);


        OpenCVLoader.initDebug();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startImageCapture() {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void openGallery(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);

    }


    public void convertoGray(View v) {

        Intent intent = new Intent(getApplicationContext(), ImagePrcocessing.class);
        startActivity(intent);
    }

}
