package com.luisburgos.studentsapp.utils;

import android.content.Context;
import android.database.SQLException;

import com.luisburgos.studentsapp.data.students.StudentDataSource;
import com.luisburgos.studentsapp.data.users.UserDataSource;

/**
 * Created by luisburgos on 2/02/16.
 */
public class Injection {

    public static StudentDataSource provideStudentsDataSource(Context context) {
        StudentDataSource dataSource = new StudentDataSource(context);

        //Dummy init block
        /*try {
            dataSource.open();
            dataSource.insertStudent("10004020", "Luis", "Burgos", "LIS");
            dataSource.insertStudent("10003020", "Juan", "Mendez", "LCC");
            dataSource.insertStudent("99994020", "Elsy", "Garcia", "LIS");
            dataSource.insertStudent("10003000", "Jesus", "Perez", "LIC");
            dataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        return dataSource;
    }

    public static UserDataSource provideUsersDataSource(Context context) {
        UserDataSource dataSource = new UserDataSource(context);

        //Dummy init block
        /*try {
            dataSource.open();
            dataSource.insertUser("admin", "adminadmin");
            dataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

        return dataSource;
    }
}
