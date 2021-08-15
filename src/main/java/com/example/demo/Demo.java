package com.example.demo;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Demo {

    public static void main(String[] args) throws Exception {
        new Demo().run();
        
    }

    /**
     * https://stackoverflow.com/questions/56905592/automatic-contrast-and-brightness-adjustment-of-a-color-photo-of-a-sheet-of-pape
     * 
     * https://stackoverflow.com/questions/51642465/java-opencv-convert-hsv-back-to-bgr-after-inrange
     */
    private void run() throws Exception {

        OpenCV.loadShared();

        URL url = this.getClass().getClassLoader().getResource("testpage.png");
        
        File f = new File(url.getFile());
        Mat in = loadImage(url.getFile());
        
        
        System.out.println(CvType.typeToString(in.type()));
        Mat hsv = new Mat();
        Imgproc.cvtColor(in, hsv, Imgproc.COLOR_BGR2HSV);

        System.out.println(CvType.typeToString(hsv.type()));


        Mat mask = new Mat();
        Core.inRange(hsv, new Scalar(-180,20,25),new Scalar(180,255,255), mask);

        Mat out2 = new Mat();
        in.copyTo(out2, mask);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE); 

        
        for(MatOfPoint matOfPoint : contours) {
            System.out.println("matOfPpoint : " + matOfPoint);

            Rect rect = Imgproc.boundingRect(matOfPoint);

            System.out.println(rect);
            Scalar color = new Scalar(0, 0, 255);
            Imgproc.rectangle(in, rect, color);
            
        }
        
        Imgcodecs imageCodecs = new Imgcodecs();
        imageCodecs.imwrite("out_" + f.getName(), in);
   
        
        
    }

    public static Mat loadImage(String imagePath) {
        Imgcodecs imageCodecs = new Imgcodecs();
        return imageCodecs.imread(imagePath);
    }

   
}
