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
import java.util.Random;
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


            selectedImageBitmap = MyConstants.selectedImageBitmap;
            Mat tempor = new Mat();
            Mat src = new Mat();
            Utils.bitmapToMat(selectedImageBitmap, tempor);


            Mat grayMat = new Mat();
            Mat cannyEdges = new Mat();
            Mat hierarchy = new Mat();

            List<MatOfPoint> contourList = new ArrayList<MatOfPoint>();

            //Converting the image to grayscale
            Imgproc.cvtColor(tempor, grayMat, Imgproc.COLOR_BGR2GRAY);

            Imgproc.Canny(grayMat, cannyEdges, 10, 100);

            //finding contours
            Imgproc.findContours(cannyEdges, contourList, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            //Drawing contours on a new image
            Mat contours = new Mat();
            contours.create(cannyEdges.rows(), cannyEdges.cols(), CvType.CV_8UC3);

            double maxVal = 0;
            int maxValIdx = 0;
            for (int contourIdx = 0; contourIdx < contourList.size(); contourIdx++) {
                double contourArea = Imgproc.contourArea(contourList.get(contourIdx));
                if (maxVal < contourArea) {
                    maxVal = contourArea;
                    maxValIdx = contourIdx;
                }
            }

            Imgproc.drawContours(tempor, contourList, maxValIdx, new Scalar(0, 255, 0), 3);


            Bitmap currentBitmap;
            currentBitmap = Bitmap.createBitmap(tempor.cols(), tempor.rows(),
                    Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tempor, currentBitmap);
            imageView.setImageBitmap(currentBitmap);


            //perspective transformation


//            selectedImageBitmap=MyConstants.selectedImageBitmap;
//            Mat tempor = new Mat();
//            Mat src = new Mat();
//            Utils.bitmapToMat(selectedImageBitmap, tempor);
//            Utils.bitmapToMat(selectedImageBitmap, src);
//
//
//            Mat grayMat = new Mat();
//            Mat cannyEdges = new Mat();
//            Mat hierarchy = new Mat();
//
//            List<MatOfPoint> contourList = new ArrayList<MatOfPoint>(); //A list to store all the contours
//
//            //Converting the image to grayscale
//            Imgproc.cvtColor(tempor,grayMat,Imgproc.COLOR_BGR2GRAY);
//
//            Imgproc.Canny(grayMat, cannyEdges,10, 100);
//
//            //finding contours
//            Imgproc.findContours(cannyEdges,contourList,hierarchy,Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
//
//
//
//            double maxVal = 0;
//            int maxValIdx = 0;
//            MatOfPoint temp_contour = contourList.get(0); //the largest is at the index 0 for starting point
//            MatOfPoint2f approxCurve = new MatOfPoint2f();
//            MatOfPoint largest_contour = contourList.get(0);
//            List<MatOfPoint> largest_contours = new ArrayList<MatOfPoint>();
//            for (int contourIdx = 0; contourIdx < contourList.size(); contourIdx++) {
//                temp_contour = contourList.get(contourIdx);
//                double contourArea = Imgproc.contourArea(contourList.get(contourIdx));
//                if (maxVal < contourArea) {
//                    MatOfPoint2f new_mat = new MatOfPoint2f(temp_contour.toArray());
//                    int contourSize = (int) temp_contour.total();
//                    MatOfPoint2f approxCurve_temp = new MatOfPoint2f();
//                    Imgproc.approxPolyDP(new_mat, approxCurve_temp, contourSize * 0.10, true);
//                    if (approxCurve_temp.total() == 4) {
//                        maxVal = contourArea;
//                        maxValIdx = contourIdx;
//                        approxCurve=approxCurve_temp;
//                        largest_contour = temp_contour;
//                    }
//                }
//            }
//
//
//
//            Imgproc.cvtColor(cannyEdges, cannyEdges, Imgproc.COLOR_BayerBG2RGB);
//            double[] temp_double;
//            temp_double = approxCurve.get(0,0);
//            Point p1 = new Point(temp_double[0], temp_double[1]);
//
//
//            temp_double = approxCurve.get(1,0);
//            Point p2 = new Point(temp_double[0], temp_double[1]);
//
//            temp_double = approxCurve.get(2,0);
//            Point p3 = new Point(temp_double[0], temp_double[1]);
//
//            temp_double = approxCurve.get(3,0);
//            Point p4 = new Point(temp_double[0], temp_double[1]);
//
//            List<Point> source = new ArrayList<Point>();
//            source.add(p1);
//            source.add(p2);
//            source.add(p3);
//            source.add(p4);
//            Mat startM = Converters.vector_Point2f_to_Mat(source);
//
//            Log.d("points","points p1:" +p1+ " p2: "  +p2+ " p3  : "  +p3+ " p4 :" +p4);
//
//
////
//           Imgproc.drawContours(tempor, contourList, maxValIdx, new Scalar(0,255,0), 5);
//
////
//            Bitmap currentBitmap;
//          currentBitmap = Bitmap.createBitmap(tempor.cols(), tempor.rows(),
//                 Bitmap.Config.ARGB_8888);
//         Utils.matToBitmap(tempor, currentBitmap);
//           imageView.setImageBitmap(currentBitmap);
//
//
//
//


//
        }
    };


    public Mat warp(Mat inputMat, Mat startM) {
        int resultWidth = 720;
        int resultHeight = 1280;

        Mat outputMat = new Mat(resultWidth, resultHeight, CvType.CV_8UC4);


        Point ocvPOut1 = new Point(0, 0);
        Point ocvPOut2 = new Point(0, resultHeight);
        Point ocvPOut3 = new Point(resultWidth, resultHeight);
        Point ocvPOut4 = new Point(resultWidth, 0);
        List<Point> dest = new ArrayList<Point>();
        dest.add(ocvPOut1);
        dest.add(ocvPOut2);
        dest.add(ocvPOut3);
        dest.add(ocvPOut4);
        Mat endM = Converters.vector_Point2f_to_Mat(dest);

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);

        Imgproc.warpPerspective(inputMat,
                outputMat,
                perspectiveTransform,
                new Size(resultWidth, resultHeight),
                Imgproc.INTER_CUBIC);

        return outputMat;

    }

}
