package com.barlo.airfoil_constructor.model;


public abstract class AbstractAirfoil implements Airfoiled{

    private String Code; // Code of the airfoil in digits, for example 2415.

    private double m; //Maximum camber in percentage of the cord
    private double p; //Position of the maximum camber in tenths of cord
    private double t; //Maximum thickness of the airfoil in percentage of cord
    private double radius; //Radius of the leading edge

    public AbstractAirfoil(String Code) {
        this.Code = Code;
    }

    public String getCode() {
        return Code;
    }

    public double getM() {
        return m;
    }

    public double getP() {
        return p;
    }

    public double getT() {
        return t;
    }

    public double getRadius() {
        return radius;
    }

    public void setM(double m) {
        this.m = m;
    }

    public void setP(double p) {
        this.p = p;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
