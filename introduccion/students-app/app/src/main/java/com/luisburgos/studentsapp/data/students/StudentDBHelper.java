package com.luisburgos.studentsapp.data.students;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luisburgos on 4/02/16.
 */
public class StudentDBHelper extends SQLiteOpenHelper {

    public StudentDBHelper(Context context){
        super(context, DatabaseContract.DATABASE_NAME,null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(StudentsDBContract.SQL_CREATE_STUDENTS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(StudentsDBContract.SQL_DELETE_STUDENTS);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db,newVersion, oldVersion);
    }

}
