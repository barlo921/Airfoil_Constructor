package com.barlo.airfoil_constructor.controller.support;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathSupport {

    /*
        Support class for mathematics operations
     */

    /*
        round is a method for round double values to prevent inaccuracy in calculation
     */

    public static double round(final double value, final int place) {
        if (place < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(place, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double notNegative(double value) {

        if (value < 0) {
            return -value;
        }

        return value;

    }

}
