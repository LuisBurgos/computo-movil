package com.luisburgos.studentsapp.domain;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by luisburgos on 2/02/16.
 */
public class Student implements Serializable {

    private String enrollmentID;
    private String name;
    private String lastName;
    private String bachelorsDegree;

    public Student() {
    }

    public Student(String enrollmentID, String name, String lastName, String bachelorsDegree) {
        this.enrollmentID = enrollmentID;
        this.name = name;
        this.lastName = lastName;
        this.bachelorsDegree = bachelorsDegree;
    }

    public String getEnrollmentID() {
        return enrollmentID;
    }

    public void setEnrollmentID(String enrollmentID) {
        this.enrollmentID = enrollmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBachelorsDegree() {
        return bachelorsDegree;
    }

    public void setBachelorsDegree(String bachelorsDegree) {
        this.bachelorsDegree = bachelorsDegree;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "enrollmentID='" + enrollmentID + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public boolean isEmpty() {
        boolean isEnrollmentIDEmpty = TextUtils.isEmpty(enrollmentID);
        boolean isNameEmpty = TextUtils.isEmpty(name);
        boolean isLastNameEmpty = TextUtils.isEmpty(lastName);
        boolean isDegreeEmpty = TextUtils.isEmpty(bachelorsDegree);
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
