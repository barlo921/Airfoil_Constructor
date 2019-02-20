package com.barlo.airfoil_constructor.exception;


public class MissMatchAirfoilTypeAndCodeException extends AbstractAirfoilConstructorException {

    private String message;

    public MissMatchAirfoilTypeAndCodeException(final String value) {
        message = "Code you entered does not satisfy " + value + " airfoil";
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getException() {
        return "Wrong Airfoil code";
    }
}
