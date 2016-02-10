package com.luisburgos.temperatureconverter;

/**
 * Created by luisburgos on 21/01/16.
 */
public interface MainContract {

    interface View {

        void showMissingDegreeParameter();

        String getDegrees();

        int getTypeConversion();

        void setResult(String newValue);

        void clearResult();

    }

    interface UserActionsListener {

        void doConversion();

    }

}
