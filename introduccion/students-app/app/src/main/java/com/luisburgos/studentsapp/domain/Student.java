package com.luisburgos.studentsapp.domain;

/**
 * Created by luisburgos on 2/02/16.
 */
public class Student {

    private String id;
    private String name;
    private String bachelorsDegree;

    public Student(String id, String name, String bachelorsDegree) {
        this.id = id;
        this.name = name;
        this.bachelorsDegree = bachelorsDegree;
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
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bachelorsDegree='" + bachelorsDegree + '\'' +
                '}';
    }
}
