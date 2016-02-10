package com.luisburgos.fragmentsexample.data;

import android.content.Context;

/**
 * Created by luisburgos on 8/02/16.
 */
public class Injection {

    public static StudentDataSource provideStudentsDataSource(Context context) {
        return new StudentDataSource(context);
    }

}
