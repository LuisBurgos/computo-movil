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
        UserSettingsManager settings = new UserSettingsManager(context);
        if(!settings.isStudentsDatabaseInit()){
            //Dummy init block
            try {
                dataSource.insertStudent("10004020", "Luis", "Burgos", "LIS");
                dataSource.insertStudent("10003020", "Juan", "Lopez", "LCC");
                dataSource.insertStudent("99994020", "Elsy", "Pinzon", "LIS");
                dataSource.insertStudent("10003001", "Jesus", "Perez", "LEM");
                dataSource.insertStudent("10014000", "Pedro", "Sanchez", "LIC");;
                dataSource.insertStudent("10004545", "Felix", "Diaz", "LIS");
                dataSource.insertStudent("10009988", "Andres", "Heredia", "LCC");
                settings.registerStudentsDatabaseInit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dataSource;
    }

    public static UserDataSource provideUsersDataSource(Context context) {
        UserDataSource dataSource = new UserDataSource(context);
        UserSettingsManager settings = new UserSettingsManager(context);
        if(!settings.isUsersDatabaseInit()){
            //Dummy init block
            try {
                dataSource.open();
                dataSource.insertUser("admin", "adminadmin");
                dataSource.close();
                settings.registerUsersDatabaseInit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dataSource;
    }
}
