package com.luisburgos.temperatureconverter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by luisburgos on 21/01/16.
 */
public class MainPresenter implements MainContract.UserActionsListener {

    private final MainContract.View mMainView;
    private final DegreesConverter converter;

    public MainPresenter(@NonNull MainContract.View mainView) {
        mMainView = mainView;
        converter = new DegreesConverter();
    }

    @Override
    public void doConversion() {
        String degreesValue = mMainView.getDegrees();
        boolean isValidDegree = validateDegreeValue(degreesValue);

        if(isValidDegree){
            executeConversionOperation(Double.parseDouble(degreesValue));
        }
    }

    private void executeConversionOperation(double degreesValue) {
        double conversionResult = 0;
        String postFix = "";

        if(mMainView.getTypeConversion() == R.id.radioCelsius){
            conversionResult = converter.convertCelsiusToFahrenheit(degreesValue);
            postFix = "°F";
        };

        if(mMainView.getTypeConversion() == R.id.radioFahrenheit){
            conversionResult = converter.convertFahrenheitToCelsius(degreesValue);
            postFix = "°C";
        };

        mMainView.setResult(String.valueOf(conversionResult) + " " + postFix);
    }

    private boolean validateDegreeValue(String degrees) {
        if(TextUtils.isEmpty(degrees)){
            mMainView.clearResult();
            mMainView.showMissingDegreeParameter();
            return false;
        }
        return true;
    }
}
