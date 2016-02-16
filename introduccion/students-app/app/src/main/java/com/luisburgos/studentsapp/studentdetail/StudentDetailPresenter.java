package com.luisburgos.studentsapp.studentdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luisburgos.studentsapp.data.StudentsRepository;
import com.luisburgos.studentsapp.domain.Student;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luisburgos on 2/02/16.
 */
public class StudentDetailPresenter implements StudentDetailContract.UserActionsListener{

    private StudentsRepository mStudentsRepository;

    private StudentDetailContract.View mStudentDetailView;

    public StudentDetailPresenter(
            @NonNull StudentsRepository studentsRepository,
            @NonNull StudentDetailContract.View studentDetailView) {
        mStudentDetailView = checkNotNull(studentDetailView, "studentDetailView cannot be null!");
        mStudentsRepository = checkNotNull(studentsRepository, "studentsRepository cannot be null!");
    }

    @Override
    public void openStudent(@Nullable String id) {
        if (null == id || id.isEmpty()) {
            mStudentDetailView.showMissingStudent();
            return;
        }

        mStudentDetailView.setProgressIndicator(true);
        mStudentsRepository.getStudent(id, new StudentsRepository.GetStudentCallback() {
            @Override
            public void onStudentLoaded(Student student) {
                mStudentDetailView.setProgressIndicator(false);
                if (null == student) {
                    mStudentDetailView.showMissingStudent();
                } else {
                    showStudent(student);
                }
            }
        });
    }

    private void showStudent(Student student) {

        String id = student.getId();
        String name = student.getName();
        String bachelorsDegree = student.getBachelorsDegree();

        if (id != null && id.isEmpty()) {
            mStudentDetailView.hideID();
        } else {
            mStudentDetailView.showID(id);
        }

        if (name != null && name.isEmpty()) {
            mStudentDetailView.hideName();
        } else {
            mStudentDetailView.showName(name);
        }

        if (bachelorsDegree != null && bachelorsDegree.isEmpty()) {
            mStudentDetailView.hideBachelorsDegree();
        } else {
            mStudentDetailView.showBachelorsDegree(bachelorsDegree);
        }
    }
}
