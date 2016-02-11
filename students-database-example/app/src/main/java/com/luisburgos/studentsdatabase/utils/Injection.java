package com.luisburgos.studentsdatabase.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.luisburgos.studentsdatabase.data.StudentDataSource;

import java.sql.SQLException;

/**
 * Created by luisburgos on 2/02/16.
 */
public class Injection {

    public static StudentDataSource provideStudentsDataSource(Context context) {
        StudentDataSource dataSource = new StudentDataSource(context);

        //Dummy init block
        /*try {
            dataSource.open();
            dataSource.insertStudent("10004020", "Luis", "Burgos");
            dataSource.insertStudent("10003020", "Juan", "Mendez");
            dataSource.insertStudent("99994020", "Elsy", "Garcia");
            dataSource.insertStudent("10003000", "Jesus", "Perez");
            dataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        return dataSource;
    }
}
