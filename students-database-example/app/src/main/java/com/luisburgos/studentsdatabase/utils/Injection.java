package com.luisburgos.studentsdatabase.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.luisburgos.studentsdatabase.data.StudentDataSource;

/**
 * Created by luisburgos on 2/02/16.
 */
public class Injection {

    public static StudentDataSource provideStudentsDataSource(Context context) {
        return new StudentDataSource(context);
    }
}
