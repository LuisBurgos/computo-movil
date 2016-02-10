package com.luisburgos.fragmentsexample.data;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by luisburgos on 8/02/16.
 */
public class StudentFileWriter {

    public static void replaceContent(Context context, LinkedList<String> newStudents) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(StudentDataSource.DEFAULT_FILE_NAME, Context.MODE_WORLD_WRITEABLE)
            ));
            for(String line : newStudents){
                writer.write(line);
            }
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

    public static void replaceLineToInternalFile(String newLine){

    }

    public static void appendToInternalFile(Context context, String newLine){
        String eol = System.getProperty("line.separator");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(StudentDataSource.DEFAULT_FILE_NAME, Context.MODE_APPEND)
            ));
            writer.write(newLine + eol);
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

    public static void initFileToInternalStorage(Context context) {
        String eol = System.getProperty("line.separator");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(StudentDataSource.DEFAULT_FILE_NAME, Context.MODE_WORLD_WRITEABLE)
            ));
            writer.write("10004020,Luis,Burgos,Varguez,28/01/1995,LIS" + eol);
            writer.write("10003010,Juan,Lopez,Lopez,18/03/1995,LIC" + eol);
            writer.write("10001000,Jose,Diaz,Lopez,10/10/1995,LCC" + eol);
            writer.write("10009899,Maria,Dominguez,Gonzalez,20/02/1990,LIS" + eol);
            writer.write("10000000,Jesus,Rodriguez,Pool,11/06/1985,LIS" + eol);
            writer.write("99991040,Perla,Dominguez,Sanchez,27/12/2000,LM" + eol);
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

}
