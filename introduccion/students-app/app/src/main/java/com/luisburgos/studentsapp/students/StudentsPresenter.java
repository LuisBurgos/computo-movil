package com.luisburgos.studentsapp.students;

import android.support.annotation.NonNull;

import com.luisburgos.studentsapp.data.StudentsRepository;
import com.luisburgos.studentsapp.domain.Student;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luisburgos on 2/02/16.
 */
public class StudentsPresenter implements StudentsContract.UserActionsListener {

    private final StudentsRepository mStudentsRepository;
    private final StudentsContract.View mStudentsView;

    public StudentsPresenter(
            @NonNull StudentsRepository studentsRepository,
            @NonNull StudentsContract.View studentsView) {
        mStudentsView = checkNotNull(studentsView, "studentsView cannot be null!");
        mStudentsRepository = checkNotNull(studentsRepository, "studentsRepository cannot be null!");
    }

    @Override
    public void loadStudents(boolean forceUpdate) {
        mStudentsView.setProgressIndicator(true);
        if (forceUpdate) {
            mStudentsRepository.refreshData();
        }

        mStudentsRepository.getStudents(new StudentsRepository.LoadStudentsCallback() {
            @Override
            public void onStudentsLoaded(List<Student> students) {
                mStudentsView.setProgressIndicator(false);
                mStudentsView.showStudents(students);
            }
        });
    }

    @Override
    public void addNewStudent() {
        mStudentsView.showAddStudent();
    }

    @Override
    public void openStudentDetails(@NonNull Student requestedStudent) {
        checkNotNull(requestedStudent, "requestedNote cannot be null!");
        mStudentsView.showStudentDetailUi(requestedStudent.getId());
    }
}
