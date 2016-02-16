package com.luisburgos.studentsdatabase.domain;

import android.text.TextUtils;

/**
 * Created by luisburgos on 2/02/16.
 */
public class Student {

    private String enrollmentID;
    private String name;
    private String lastName;

    public Student() {

    }

    public Student(String enrollmentID, String name, String lastName) {
        this.enrollmentID = enrollmentID;
        this.name = name;
        this.lastName = lastName;
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

        return (isEnrollmentIDEmpty) && (isNameEmpty) && (isLastNameEmpty);
    }
}
