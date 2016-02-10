package com.luisburgos.studentsapp.studentdetail;

import android.support.annotation.Nullable;

/**
 * Created by luisburgos on 2/02/16.
 */
public interface StudentDetailContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showID(String id);

        void hideID();

        void showName(String name);

        void hideName();

        void showImage(String imageUrl);

        void hideImage();

        void showBachelorsDegree(String bachelorsDegree);

        void hideBachelorsDegree();

        void showMissingStudent();
    }

    interface UserActionsListener {

        void openStudent(@Nullable String id);
    }

}
