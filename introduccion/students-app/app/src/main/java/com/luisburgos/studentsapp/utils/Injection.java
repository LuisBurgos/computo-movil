package com.luisburgos.studentsapp.utils;

import com.luisburgos.studentsapp.data.StudentsRepositories;
import com.luisburgos.studentsapp.data.StudentsRepository;
import com.luisburgos.studentsapp.data.StudentsServiceApiImpl;

/**
 * Created by luisburgos on 2/02/16.
 */
public class Injection {

    public static StudentsRepository provideStudentsRepository() {
        return StudentsRepositories.getInMemoryRepoInstance(new StudentsServiceApiImpl());
    }
}
