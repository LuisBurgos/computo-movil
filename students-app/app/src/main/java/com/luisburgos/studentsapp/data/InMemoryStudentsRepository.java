package com.luisburgos.studentsapp.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableList;
import com.luisburgos.studentsapp.domain.Student;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luisburgos on 3/02/16.
 */
public class InMemoryStudentsRepository implements StudentsRepository{

    private final StudentsServiceApi mStudentsServiceApi;

    @VisibleForTesting
    List<Student> mCachedStudents;

    public InMemoryStudentsRepository(@NonNull StudentsServiceApi studentsServiceApi) {
        mStudentsServiceApi = checkNotNull(studentsServiceApi);
    }


    @Override
    public void getStudents(final @NonNull LoadStudentsCallback callback) {
        checkNotNull(callback);
        // Load from API only if needed.
        if (mCachedStudents == null) {
            mStudentsServiceApi.getAllStudents(new StudentsServiceApi.StudentsServiceCallback<List<Student>>() {
                @Override
                public void onLoaded(List<Student> students) {
                    mCachedStudents = ImmutableList.copyOf(students);
                    callback.onStudentsLoaded(mCachedStudents);
                }
            });
        } else {
            callback.onStudentsLoaded(mCachedStudents);
        }
    }

    @Override
    public void getStudent(final @NonNull String id, final @NonNull GetStudentCallback callback) {
        checkNotNull(id);
        checkNotNull(callback);
        // Load notes matching the id always directly from the API.
        mStudentsServiceApi.getStudent(id, new StudentsServiceApi.StudentsServiceCallback<Student>() {
            @Override
            public void onLoaded(Student student) {
                callback.onStudentLoaded(student);
            }
        });
    }

    @Override
    public void saveStudent(@NonNull Student student) {
        checkNotNull(student);
        mStudentsServiceApi.saveStudent(student);
        refreshData();
    }

    @Override
    public void refreshData() {
        mCachedStudents = null;
    }
}
