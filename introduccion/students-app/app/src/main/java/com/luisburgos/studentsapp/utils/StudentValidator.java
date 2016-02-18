package com.luisburgos.studentsapp.utils;

import android.text.TextUtils;

import com.luisburgos.studentsapp.domain.Student;

/**
 * Created by luisburgos on 17/02/16.
 */
public class StudentValidator {

    public boolean validateEnrollmentID(String enrollmentID) {
        return  enrollmentID.length() == 8;
    }

    public boolean validateName(String name) {
        return !TextUtils.isEmpty(name);
    }

    public boolean validateLastName(String lastName) {
        return !TextUtils.isEmpty(lastName);
    }

    public boolean validateBachelorsDegree(String bachelorsDegree) {
        return !TextUtils.isEmpty(bachelorsDegree);
    }

    public boolean isEmpty(Student newStudent) {
        boolean isEnrollmentIDEmpty = TextUtils.isEmpty(newStudent.getEnrollmentID());
        boolean isNameEmpty = TextUtils.isEmpty(newStudent.getName());
        boolean isLastNameEmpty = TextUtils.isEmpty(newStudent.getLastName());
        boolean isDegreeEmpty = TextUtils.isEmpty(newStudent.getBachelorsDegree());
        return (isEnrollmentIDEmpty) && (isNameEmpty) && (isLastNameEmpty) && (isDegreeEmpty);
        /*if(TextUtils.isEmpty(enrollmentID)){
            return true;
        }
        if(TextUtils.isEmpty(name)){
            return true;
        }
        if(TextUtils.isEmpty(lastName)){
            return true;
        }
        if(TextUtils.isEmpty(bachelorsDegree)){
            return true;
        }
        return false;*/
    }
}
