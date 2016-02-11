package com.luisburgos.studentslogin.utils;

import android.content.Context;
import android.database.SQLException;

import com.luisburgos.studentslogin.data.UserDataSource;

/**
 * Created by luisburgos on 11/02/16.
 */
public class Injection {

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
