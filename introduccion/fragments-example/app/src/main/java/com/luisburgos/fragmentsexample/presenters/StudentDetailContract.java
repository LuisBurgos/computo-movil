package com.luisburgos.fragmentsexample.presenters;

import android.support.annotation.Nullable;

/**
 * Created by luisburgos on 2/02/16.
 */
public interface StudentDetailContract {

    interface View {

        void enableInformationEdition(boolean editionEnable);

        void showEnrollmentID(String enrollmentID);

        void hideEnrollmentID();

        void showName(String name);

        void hideName();

        void showLastName(String lastName);

        void hideLastName();

        void showSecondLastName(String secondLastName);

        void hideSecondLastName();

        void showBirthDate(String birthDate);

        void hideBirthDate();

        void showBachelorsDegree(String bachelorsDegree);

        void hideBachelorsDegree();

        void showStudentsList(); //Reload list when update

        void showMissingStudent();

        void showEmptyStudentError();

        void loseFocus();

        UserActionsListener getActionsListener();
    }

    interface UserActionsListener {

        void openStudent(@Nullable String id);

        void saveStudentChanges(String id, String name, String lastName, String secondLast, String birthDate, String degree);

        void editStudent();
    }

}
