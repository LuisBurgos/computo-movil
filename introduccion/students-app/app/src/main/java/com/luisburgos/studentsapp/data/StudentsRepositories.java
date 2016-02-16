package com.luisburgos.studentsapp.data;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luisburgos on 2/02/16.
 */
public class StudentsRepositories {

    private StudentsRepositories() {
        // no instance
    }

    private static StudentsRepository repository = null;

    public synchronized static StudentsRepository getInMemoryRepoInstance(@NonNull StudentsServiceApi studentsServiceApi) {
        checkNotNull(studentsServiceApi);
        if (null == repository) {
            repository = new InMemoryStudentsRepository(studentsServiceApi);
        }
        return repository;
    }

}
