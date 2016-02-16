package com.luisburgos.studentsapp.addstudent;

import android.support.annotation.Nullable;

/**
 * Created by luisburgos on 3/02/16.
 */
public interface AddStudentContract {

    interface View {

        void showEmptyStudentMessage();

        void setUserActionListener(UserActionsListener listener);

    }

    interface UserActionsListener {

        void saveStudent(String id, String name, String bachelorsDegree);

    }


}
