package com.luisburgos.temperatureconverter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by luisburgos on 21/01/16.
 */
public class DegreesConverter {

    public double convertCelsiusToFahrenheit(double celsius){
        return  roundTwoDecimals(celsius * 1.8 + 32);
    }

    public double convertFahrenheitToCelsius(double fahrenheit){
        return roundTwoDecimals((fahrenheit - 32) / 1.8);
    }

    private double roundTwoDecimals(double d) {
        return round(d, 2);
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
