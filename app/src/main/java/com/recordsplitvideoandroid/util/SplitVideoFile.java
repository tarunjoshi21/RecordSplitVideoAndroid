package com.recordsplitvideoandroid.util;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tarun on 10/2/17.
 */

public class SplitVideoFile extends AsyncTask<String, Integer, String>{
    @Override
    protected String doInBackground(String... params) {

        try {
            File file = new File(params[0]);//File read from Source folder to Split.
            if (file.exists()) {

                String videoFileName = file.getName().substring(0, file.getName().lastIndexOf(".")); // Name of the videoFile without extension
                File splitFile = new File(Environment.getExternalStorageDirectory()+File.separator+"Videos_Split/"+ videoFileName);//Destination folder to save.
                if (!splitFile.exists()) {
                    splitFile.mkdirs();
                    System.out.println("Directory Created -> "+ splitFile.getAbsolutePath());
                }

                int i = 1;// Files count starts from 1
                InputStream inputStream = new FileInputStream(file);
                String videoFile = splitFile.getAbsolutePath() +"/"+ String.format("%02d", i) +"_"+ file.getName();// Location to save the files which are Split from the original file.
                OutputStream outputStream = new FileOutputStream(videoFile);
                System.out.println("File Created Location: "+ videoFile);
                int totalPartsToSplit = 20;// Total files to split.
                int splitSize = inputStream.available() / totalPartsToSplit;
                int streamSize = 0;
                int read = 0;
                while ((read = inputStream.read()) != -1) {

                    if (splitSize == streamSize) {
                        if (i != totalPartsToSplit) {
                            i++;
                            String fileCount = String.format("%02d", i); // output will be 1 is 01, 2 is 02
                            videoFile = splitFile.getAbsolutePath() +"/"+ fileCount +"_"+ file.getName();
                            outputStream = new FileOutputStream(videoFile);
                            System.out.println("File Created Location: "+ videoFile);
                            streamSize = 0;
                        }
                    }
                    outputStream.write(read);
                    streamSize++;
                }

                inputStream.close();
                outputStream.close();
                System.out.println("Total files Split ->"+ totalPartsToSplit);
            } else {
                System.err.println(file.getAbsolutePath() +" File Not Found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
   /* public static void main(String[] args) {

    }*/
}
