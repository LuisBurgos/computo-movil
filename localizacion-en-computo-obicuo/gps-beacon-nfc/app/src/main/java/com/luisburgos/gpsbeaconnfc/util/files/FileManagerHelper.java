package com.luisburgos.gpsbeaconnfc.util.files;

import android.content.Context;
import android.util.Log;


import com.luisburgos.gpsbeaconnfc.GPSBeaconNFCApplication;
import com.luisburgos.gpsbeaconnfc.views.activities.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by luisburgos on 6/04/16.
 */
public class FileManagerHelper {

    private static final String DEFAULT_FILE_NAME = "contentFromServer";

    public static void writeToInternalFile(Context context, String newContent){
        String eol = System.getProperty("line.separator");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(DEFAULT_FILE_NAME, Context.MODE_WORLD_WRITEABLE)
            ));
            writer.write(newContent + eol);
            Log.d(GPSBeaconNFCApplication.TAG, "SAVING ON FILE: " + newContent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String readFileFromInternalStorage(Context context) {
        String eol = System.getProperty("line.separator");
        String contentFromServer = null;
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(context.openFileInput(DEFAULT_FILE_NAME)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line + eol);
            }
            contentFromServer = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return contentFromServer;
        }
    }

}
