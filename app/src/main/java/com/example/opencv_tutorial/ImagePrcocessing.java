package com.example.opencv_tutorial;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.utils.Converters;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static java.util.Collections.sort;

public class ImagePrcocessing extends AppCompatActivity {

    private static final String TAG = ImagePrcocessing.class.getSimpleName();
      ImageView imageView;
      Button btnImageEnhance;
      Bitmap selectedImageBitmap;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_processing);


        initializeElement();


    }
    private void initializeElement() {

        btnImageEnhance = findViewById(R.id.process);

        imageView = findViewById(R.id.imageView);
        selectedImageBitmap = MyConstants.selectedImageBitmap;
        btnImageEnhance.setOnClickListener(btnImageEnhanceClick);
        imageView.setImageBitmap(selectedImageBitmap);


    }



    private View.OnClickListener btnImageEnhanceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        selectedImageBitmap=MyConstants.selectedImageBitmap;
            Mat tempor = new Mat();
            Mat src = new Mat();
            Utils.bitmapToMat(selectedImageBitmap, tempor);

            Imgproc.cvtColor(tempor, src, Imgproc.COLOR_BGR2RGB);

            Mat blurred = src.clone();
            Imgproc.medianBlur(src, blurred, 9);

            Mat gray0 = new Mat(blurred.size(), CvType.CV_8U), gray = new Mat();

            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

            List<Mat> blurredChannel = new ArrayList<Mat>();
            blurredChannel.add(blurred);
            List<Mat> gray0Channel = new ArrayList<Mat>();
            gray0Channel.add(gray0);

            MatOfPoint2f approxCurve;

            double maxArea = 0;
            int maxId = -1;

            for (int c = 0; c < 3; c++) {
                int ch[] = { c, 0 };
                Core.mixChannels(blurredChannel, gray0Channel, new MatOfInt(ch));

                int thresholdLevel = 1;
                for (int t = 0; t < thresholdLevel; t++) {
                    if (t == 0) {
                        Imgproc.Canny(gray0, gray, 10, 20, 3, true);
                        Imgproc.dilate(gray, gray, new Mat(), new Point(-1, -1), 1);

                    } else {
                        Imgproc.adaptiveThreshold(gray0, gray, thresholdLevel,
                                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                                Imgproc.THRESH_BINARY,
                                (src.width() + src.height()) / 200, t);
                    }

                    Imgproc.findContours(gray, contours, new Mat(),
                            Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

                    for (MatOfPoint contour : contours) {
                        MatOfPoint2f temp = new MatOfPoint2f(contour.toArray());

                        double area = Imgproc.contourArea(contour);
                        approxCurve = new MatOfPoint2f();
                        Imgproc.approxPolyDP(temp, approxCurve,
                                Imgproc.arcLength(temp, true) * 0.02, true);

                        if (approxCurve.total() == 4 && area >= maxArea) {
                            double maxCosine = 0;

                            List<Point> curves = approxCurve.toList();
                            for (int j = 2; j < 5; j++) {

                                double cosine = Math.abs(angle(curves.get(j % 4),
                                        curves.get(j - 2), curves.get(j - 1)));
                                maxCosine = Math.max(maxCosine, cosine);
                            }

                            if (maxCosine < 0.3) {
                                maxArea = area;
                                maxId = contours.indexOf(contour);
                            }
                        }
                    }
                }
            }

            if (maxId >= 0) {
                Rect rect = Imgproc.boundingRect(contours.get(maxId));

                Imgproc.rectangle(src, rect.tl(), rect.br(), new Scalar(255, 0, 0,
                        .8), 4);


                int mDetectedWidth = rect.width;
                int mDetectedHeight = rect.height;

                Log.d(TAG, "Rectangle width :"+mDetectedWidth+ " Rectangle height :"+mDetectedHeight);

            }

            Bitmap bmp;
            bmp = Bitmap.createBitmap(src.cols(), src.rows(),
                    Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(src, bmp);
           imageView.setImageBitmap(bmp);















        }
    };


    private static double angle(Point p1, Point p2, Point p0) {
        double dx1 = p1.x - p0.x;
        double dy1 = p1.y - p0.y;
        double dx2 = p2.x - p0.x;
        double dy2 = p2.y - p0.y;
        return (dx1 * dx2 + dy1 * dy2)
                / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2)
                + 1e-10);
    }
}
