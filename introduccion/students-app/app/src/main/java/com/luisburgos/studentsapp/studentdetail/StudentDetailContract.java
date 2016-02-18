package com.luisburgos.studentsapp.studentdetail;

import android.support.annotation.Nullable;

/**
 * Created by luisburgos on 2/02/16.
 */
public interface StudentDetailContract {

    interface View {

        void setProgressIndicator(boolean active);

        void enableInformationEdition(boolean editionEnable);

        void showEnrollmentID(String enrollmentID);

        void hideEnrollmentID();

        void showName(String name);

        void hideName();

        void showLastName(String lastName);

        void hideLastName();

        void showStudentsList();

        void showMissingStudent();

        void showEmptyStudentError();

        void showBachelorsDegree(String bachelorsDegree);

        void hideBachelorsDegree();

        void setNameErrorMessage();

        void setLastNameErrorMessage();
    }

    interface UserActionsListener {

        void openStudent(@Nullable String id);

        void saveStudentChanges(String id, String name, String lastName, String bachelorsDegree);

        void editStudent();
    }

}
