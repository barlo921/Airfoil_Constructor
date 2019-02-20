package com.barlo.airfoil_constructor.model;


public class AirfoilType {

    //AirfoilType is a class for storing value and id of airfoil type used in ComboBox

    private String value;
    private int id;

    public AirfoilType(String value, int id) {
        this.value = value;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public int getId() {
        return id;
    }
}
