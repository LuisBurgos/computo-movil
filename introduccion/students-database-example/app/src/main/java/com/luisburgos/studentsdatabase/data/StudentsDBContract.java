package com.luisburgos.studentsdatabase.data;

import android.provider.BaseColumns;

/**
 * Created by luisburgos on 4/02/16.
 */
public class StudentsDBContract implements BaseColumns{

    public static final String TABLE_NAME = "Students";
    public static final String COLUMN_NAME_ENROLLMENT_ID = "enrollmentID";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_LASTNAME = "lastname";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_STUDENTS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_ENROLLMENT_ID + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_LASTNAME + TEXT_TYPE +
            " )";

    public static final String SQL_DELETE_STUDENTS = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
