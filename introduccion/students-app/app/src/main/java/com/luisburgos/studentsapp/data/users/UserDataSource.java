package com.luisburgos.studentsapp.data.users;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.luisburgos.studentsapp.domain.User;

public class UserDataSource {

    private SQLiteDatabase database;
    private UserDBHelper studentDBHelper;
    private String[] allColumns = {
            UsersDBContract.COLUMN_USERNAME,
            UsersDBContract.COLUMN_PASSWORD
    };

    public UserDataSource(Context context){
        studentDBHelper = new UserDBHelper(context);
    }

    public void open() throws SQLException {
        database = studentDBHelper.getWritableDatabase();
    }

    public void close(){
        studentDBHelper.close();
    }

    /**
     * Function oriented to insertStudent a new User into Database
     * @param username
     * @param password
     * @return row ID of the newly User inserted row, or -1
     */
    public long insertUser(String username, String password){
        ContentValues values = new ContentValues();
        values.put(UsersDBContract.COLUMN_USERNAME, username);
        values.put(UsersDBContract.COLUMN_PASSWORD, password);
        long newRowId;
        newRowId = database.insert(UsersDBContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public User getUser(String username) {

        Cursor cursor = database.query(
                UsersDBContract.TABLE_NAME,
                allColumns,
                UsersDBContract.COLUMN_USERNAME + "=?",
                new String[] { String.valueOf(username) }, null, null, null, null);

        User user = null;
        if (cursor != null && (cursor.getCount() > 0)){
            cursor.moveToFirst();
            user = cursorToUser(cursor);
        }

        return user;
    }

    public int updateUser(User user){
        ContentValues values = new ContentValues();
        values.put(UsersDBContract.COLUMN_USERNAME, user.getUsername());
        values.put(UsersDBContract.COLUMN_PASSWORD, user.getPassword());

        return database.update(
                UsersDBContract.TABLE_NAME,
                values,
                UsersDBContract.COLUMN_USERNAME + " =?",
                new String[]{String.valueOf(user.getUsername()) });
    }

    private User cursorToUser(Cursor cursor){
        User user = new User();
        user.setUsername(cursor.getString(0));
        user.setPassword(cursor.getString(1));
        return user;
    }

}
