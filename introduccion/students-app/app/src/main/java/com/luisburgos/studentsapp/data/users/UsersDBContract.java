package com.luisburgos.studentsapp.data.users;

import android.provider.BaseColumns;

public class UsersDBContract implements BaseColumns {

    public static final String TABLE_NAME = "Users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_USERNAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_PASSWORD + TEXT_TYPE +
            " )";

    public static final String SQL_DELETE_USERS = "DROP TABLE IF EXISTS " + TABLE_NAME;

}