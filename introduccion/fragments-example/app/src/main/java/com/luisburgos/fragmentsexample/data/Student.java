package com.luisburgos.fragmentsexample.data;

import android.text.TextUtils;

/**
 * Created by luisburgos on 26/01/16.
 */
public class Student {

    private String id;
    private String name;
    private String bachelorsDegree;
    private String lastName;
    private String secondLastName;
    private String birthDate;

    public Student(String id, String name, String lastName, String secondLastName, String birthDate, String bachelorsDegree) {
        this.id = id;
        this.name = name;
        this.bachelorsDegree = bachelorsDegree;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isEmpty() {
        if(TextUtils.isEmpty(id)){
            return true;
        }
        if(TextUtils.isEmpty(name)){
            return true;
        }
        if(TextUtils.isEmpty(lastName)){
            return true;
        }
        if(TextUtils.isEmpty(secondLastName)){
            return true;
        }
        if(TextUtils.isEmpty(birthDate)){
            return true;
        }
        if(TextUtils.isEmpty(bachelorsDegree)){
            return true;
        }

        return false;
    }
}
