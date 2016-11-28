package com.rjil.signatureauthentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCaptureModel, btnCaptureSignature, btnAuthenticate;
    private ImageView imgCaptureModel, imgCaptureSignature;
    private static Bitmap bmp;
    private static Bitmap bmpimg1;
    private static Bitmap bmpimg2;
    private Bitmap yourSelectedImage;
    private static final int SELECT_PHOTO = 100;
    private String TAG = getClass().getSimpleName();
    private Uri selectedImage;
    private int imgNo = 0;
    private InputStream imageStream;

    private String path1, path2;
    /**
     * Callback handler for application OpenCV Manager install
     */
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initComponent();
        initListener();
    }


    private void initComponent() {
        btnCaptureModel = (Button) findViewById(R.id.btn_capture_model);
        btnCaptureSignature = (Button) findViewById(R.id.btn_capture_signature);
        imgCaptureModel = (ImageView) findViewById(R.id.img_capture_model);
        imgCaptureSignature = (ImageView) findViewById(R.id.img_capture_signature);
        btnAuthenticate = (Button) findViewById(R.id.btn_authenticate);

    }

    private void initListener() {
        btnCaptureModel.setOnClickListener(this);
        btnCaptureSignature.setOnClickListener(this);
        btnAuthenticate.setOnClickListener(this);
        imgCaptureSignature.setOnClickListener(this);
        imgCaptureModel.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Call for OpenCV Manager application to load.
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_4, this,
                mLoaderCallback);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_capture_model:
            case R.id.btn_capture_model: {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                imgNo = 1;
            }
            break;
            case R.id.img_capture_signature:
            case R.id.btn_capture_signature: {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                imgNo = 2;
            }
            break;
            case R.id.btn_authenticate: {
                authenticateSignature();
            }
            break;
        }
    }

    /**
     * Compare two images for authentication
     * applying MAT to GrayScale,
     * Converting the MAT to float values,
     * normalizing the pixel array
     * and then comparing if compare value lies between 0 to 10000 calling Verifytask for further authentication
     */
    private void authenticateSignature() {
        if (bmpimg1 != null && bmpimg2 != null) {
            bmpimg1 = Bitmap.createScaledBitmap(bmpimg1, 100, 100, true);
            bmpimg2 = Bitmap.createScaledBitmap(bmpimg2, 100, 100, true);
            Mat img1 = new Mat();
            Utils.bitmapToMat(bmpimg1, img1);
            Mat img2 = new Mat();
            Utils.bitmapToMat(bmpimg2, img2);
            Imgproc.cvtColor(img1, img1, Imgproc.COLOR_RGBA2GRAY);
            Imgproc.cvtColor(img2, img2, Imgproc.COLOR_RGBA2GRAY);
            img1.convertTo(img1, CvType.CV_32F);
            img2.convertTo(img2, CvType.CV_32F);
            Mat hist1 = new Mat();
            Mat hist2 = new Mat();
            MatOfInt histSize = new MatOfInt(180);
            MatOfInt channels = new MatOfInt(0);
            ArrayList<Mat> bgr_planes1 = new ArrayList<Mat>();
            ArrayList<Mat> bgr_planes2 = new ArrayList<Mat>();
            Core.split(img1, bgr_planes1);
            Core.split(img2, bgr_planes2);
            MatOfFloat histRanges = new MatOfFloat(0f, 180f);
            boolean accumulate = false;
            Imgproc.calcHist(bgr_planes1, channels, new Mat(), hist1, histSize, histRanges, accumulate);
            Core.normalize(hist1, hist1, 0, hist1.rows(), Core.NORM_MINMAX, -1, new Mat());
            Imgproc.calcHist(bgr_planes2, channels, new Mat(), hist2, histSize, histRanges, accumulate);
            Core.normalize(hist2, hist2, 0, hist2.rows(), Core.NORM_MINMAX, -1, new Mat());
            img1.convertTo(img1, CvType.CV_32F);
            img2.convertTo(img2, CvType.CV_32F);
            hist1.convertTo(hist1, CvType.CV_32F);
            hist2.convertTo(hist2, CvType.CV_32F);

            double compare = Imgproc.compareHist(hist1, hist2, Imgproc.CV_COMP_CHISQR);
            Log.d("ImageComparator", "compare: " + compare);
            if (compare > 0 && compare < 10000) {

                new VerifyTask(MainActivity.this).execute();
            } else if (compare == 0)
                Toast.makeText(MainActivity.this, "Images are exact duplicates", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this, "Images are not duplicates", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity.this,
                    "You haven't selected images.", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    try {
                        imageStream = getContentResolver().openInputStream(
                                selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    //TODO:Currently set the sampleSize as 4, need to be calculated  based on the size of the image need to be showed in the imageView.
                    options.inSampleSize = 4;
                    yourSelectedImage = BitmapFactory.decodeStream(imageStream, null, options);

                    if (imgNo == 1) {
                        imgCaptureModel.setImageBitmap(yourSelectedImage);
                        path1 = selectedImage.getPath();
                        bmpimg1 = yourSelectedImage;
                        imgCaptureModel.invalidate();

                    } else if (imgNo == 2) {
                        imgCaptureSignature.setImageBitmap(yourSelectedImage);
                        path2 = selectedImage.getPath();
                        bmpimg2 = yourSelectedImage;
                        imgCaptureSignature.invalidate();
                    }
                }
        }
    }

    /**
     * Applying BRISK descriptor extractor for verifying the matches and also drawing the matches for good visible results.
     * */
    public static class VerifyTask extends AsyncTask<Void, Void, Void> implements OnCustomDialogClickListener {
        private static Mat img1, img2, descriptors, dupDescriptors;
        private static FeatureDetector detector;
        private static DescriptorExtractor DescExtractor;
        private static DescriptorMatcher matcher;
        private static MatOfKeyPoint keypoints, dupKeypoints;
        private static MatOfDMatch matches, matches_final_mat;
        private static ProgressDialog pd;
        private MainActivity asyncTaskContext = null;
        private static Scalar RED = new Scalar(255, 0, 0);
        private static Scalar GREEN = new Scalar(0, 255, 0);
        private int min_dist = 100;
        private int min_matches = 20;//TODO: Currently hard coded need to aacept value from the end user
        String message;

        public VerifyTask(MainActivity context) {
            asyncTaskContext = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(asyncTaskContext);
            pd.setIndeterminate(true);
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Images may be possible duplicates, verifying...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            compare();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Mat img3 = new Mat();
                MatOfByte drawnMatches = new MatOfByte();
                Features2d.drawMatches(img1, keypoints, img2, dupKeypoints,
                        matches_final_mat, img3, GREEN, RED, drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);
                bmp = Bitmap.createBitmap(img3.cols(), img3.rows(),
                        Bitmap.Config.ARGB_8888);
                Imgproc.cvtColor(img3, img3, Imgproc.COLOR_BGR2RGB);
                Utils.matToBitmap(img3, bmp);
                Bitmap resize = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * 2), (int) (bmp.getHeight() * 2), true);
                List<DMatch> finalMatchesList = matches_final_mat.toList();
                if (finalMatchesList.size() >= min_matches)// dev discretion for
                // number of matches to
                // be found for an image
                // to be judged as
                // duplicate

                {
                    message = finalMatchesList.size() + " matches were found, Signature can be authenticated";
                    DialogUtils.showDialog(asyncTaskContext, "Results", message, "", "", this, false, resize);
                } else {
                    message = "Signature not authenticated (matches : " + finalMatchesList.size() + ")";
                    DialogUtils.showDialog(asyncTaskContext, "Results", message, "", "", this, true, resize);
                }
                pd.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(asyncTaskContext, e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }

        void compare() {
            try {
                bmpimg1 = bmpimg1.copy(Bitmap.Config.ARGB_8888, true);
                bmpimg2 = bmpimg2.copy(Bitmap.Config.ARGB_8888, true);
                img1 = new Mat();
                img2 = new Mat();
                Utils.bitmapToMat(bmpimg1, img1);
                Utils.bitmapToMat(bmpimg2, img2);
                Imgproc.cvtColor(img1, img1, Imgproc.COLOR_BGR2RGB);
                Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BGR2RGB);
                detector = FeatureDetector.create(FeatureDetector.PYRAMID_FAST);
                DescExtractor = DescriptorExtractor.create(DescriptorExtractor.BRISK);
                matcher = DescriptorMatcher
                        .create(DescriptorMatcher.BRUTEFORCE_HAMMING);

                keypoints = new MatOfKeyPoint();
                dupKeypoints = new MatOfKeyPoint();
                descriptors = new Mat();
                dupDescriptors = new Mat();
                matches = new MatOfDMatch();
                detector.detect(img1, keypoints);
                Log.d("LOG!", "number of query Keypoints= " + keypoints.size());
                detector.detect(img2, dupKeypoints);
                Log.d("LOG!", "number of dup Keypoints= " + dupKeypoints.size());
                // Descript keypoints
                DescExtractor.compute(img1, keypoints, descriptors);
                DescExtractor.compute(img2, dupKeypoints, dupDescriptors);
                Log.d("LOG!", "number of descriptors= " + descriptors.size());
                Log.d("LOG!",
                        "number of dupDescriptors= " + dupDescriptors.size());
                // matching descriptors
                matcher.match(descriptors, dupDescriptors, matches);
                Log.d("LOG!", "Matches Size " + matches.size());
                // New method of finding best matches
                List<DMatch> matchesList = matches.toList();
                List<DMatch> matches_final = new ArrayList<DMatch>();
                for (int i = 0; i < matchesList.size(); i++) {
                    if (matchesList.get(i).distance <= min_dist) {
                        matches_final.add(matches.toList().get(i));
                    }
                }

                matches_final_mat = new MatOfDMatch();
                matches_final_mat.fromList(matches_final);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onCustomDialogClick(CustomAlertDialog.Type type) {

        }
    }

}
