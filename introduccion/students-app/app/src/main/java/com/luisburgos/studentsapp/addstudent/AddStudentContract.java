package com.luisburgos.studentsapp.addstudent;

import android.support.annotation.Nullable;

/**
 * Created by luisburgos on 3/02/16.
 */
public interface AddStudentContract {

    interface View {

        void setProgressIndicator(boolean loading);

        void showEmptyStudentMessage();

        void showStudentsList();

        void setEnrollmentIDErrorMessage(String message);

        void setNameErrorMessage();

        void setLastNameErrorMessage();

        void setBachelorsDegreeErrorMessage();
    }

    interface UserActionsListener {

        void saveStudent(String id, String name, String lastName, String bachelorsDegree);

    }


}
