package com.barlo.airfoil_constructor.controller.NACAControllers;

import com.barlo.airfoil_constructor.controller.support.MathSupport;
import com.barlo.airfoil_constructor.model.AirfoilCoordinates;
import com.barlo.airfoil_constructor.model.NACAAirfoils.NACA5DigitAirfoil;


public class NACA5DigitCoordinatesController extends NACA4DigitCoordinatesController {

    /**
     * NACA Five-Digit Series:
     *
     * An example NACA 23012, is a 12% thick airfoil, the design lift coefficient is 0.3, the position of the maximum camber is located at 15% cord, and this is non reflex camber airfoil.
     *
     * The thickness distribution is the same as NACA Four-Digit Series.
     *
     * Non reflex camber line is given by:
     *
     * Yc = k1/6*(x^3 - 3r*x^2 + r^2*(3-r)*x)   for 0<=x<=r
     * Yc = k1/6*m^3*(1-x)  for r<=x<=cord
     *
     * dYdX will be differ then 4 digit NACA airfoil:
     *
     * dYdX = k1/6*(3x^2 - 6rx + r^2*(3-r))     for 0<=x<=r
     * dYdX = (-k1/6)*r^3       for r<=x<=cord
     *
     * Reflex camber line airfoil for example 23112 is given by:
     *
     * Yc = k1/6*((x-r)^3 - k2/k1*(1-r)^3*x - r^3*x + r^3)      for 0<=x<=r
     * Yc = k1/6*(k2/k1*(x-r)^3 - k2/k1*(1-r)^3*x - r^3*x + r^3)        for r<=x<=cord
     *
     * where
     *
     * k2/k1 = (3*(r-p)^2 - r^3)/(1-r)^3
     */

    /*
        NACA5DigitCoordinatesController is the logic for calculating coordinates for drawing the NACA 5-Digit Airfoil.
     */

    private int reflex;
    private double k1;
    private double k2k1;
    private double r;

    public NACA5DigitCoordinatesController(final NACA5DigitAirfoil naca5DigitAirfoil, final AirfoilCoordinates airfoilCoordinates) {
        super(naca5DigitAirfoil, airfoilCoordinates);

        this.reflex = naca5DigitAirfoil.getReflex();
        this.k1 = naca5DigitAirfoil.getK1();
        this.k2k1 = naca5DigitAirfoil.getK1k2();
        this.r = naca5DigitAirfoil.getR();
    }


    @Override
    public void calculateYCamberCoordinates() {

        if (reflex != 1) {
            nonReflexYCamberCoordinates();
        } else {
            reflexYCamberCoordinates();
        }

    }


    /*
        dxdyNACA5DigitCalculator method determine Fi = arctan(dYc/dX) for NACA 5 digit airfoil.
        That calculation differ then 4 digit calculation.
     */
    @Override
    protected void dYdXCalculator() {

        double constantValue = k1/6;

        int i = 0;

        for (double x=0; x<=1.0;) {

            if (x<r) {
                dYdX[i++] = constantValue*(3*Math.pow(x, 2) - 6*r*x + Math.pow(r, 2)*(3-r));
            } else {
                dYdX[i++] = - constantValue*Math.pow(r, 3);
            }

            if (x<COORDINATES_STEP) {
                x = MathSupport.round(x+COORDINATES_LEADING_EDGE_STEP, 3);
            } else {
                x = MathSupport.round(x+COORDINATES_STEP, 3);
            }

        }

    }


    /*
        nonReflexYCamberCoordinates method define thickness distribution for non reflex airfoil.
        The third digit is 0 for NACA 5 digit airfoils.
        For Example NACA 23012
     */
    private void nonReflexYCamberCoordinates() {

        int i = 0;
        double constantValue = k1/6;

        for (double x = 0; x<=1.0;) {

            double yCoordinate;

            if (x<r) {
                yCoordinate = constantValue*(Math.pow(x, 3) - 3*r*Math.pow(x, 2) + Math.pow(r, 2)*(3-r)*x);
            } else {
                yCoordinate = constantValue*Math.pow(r, 3)*(1-x);
            }

            airfoilCoordinates.setCamberCoordinates(yCoordinate, i++);

            if (x<COORDINATES_STEP) {
                x = MathSupport.round(x+COORDINATES_LEADING_EDGE_STEP, 3);
            } else {
                x = MathSupport.round(x+COORDINATES_STEP, 3);
            }

        }

    }


    /*
        reflexYCamberCoordinates method define thickness distribution for reflex airfoil.
        The third digit is 1 for NACA 5 digit airfoils.
        For Example NACA 23112
     */
    private void reflexYCamberCoordinates() {

        int i = 0;
        double constantValue = k1/6;

        for (double x = 0; x<=1.0;) {

            if (x<r) {

                double yCoordinate = constantValue*(Math.pow(x-r, 3) - k2k1*Math.pow(1-r, 3)*x - Math.pow(r, 3)*x + Math.pow(r, 3));

                airfoilCoordinates.setCamberCoordinates(yCoordinate, i++);
            } else {

                double yCoordinates = constantValue*(k2k1*Math.pow(x-r, 3) - k2k1*Math.pow(1-r, 3)*x - Math.pow(r, 3)*x + Math.pow(r, 3));

                airfoilCoordinates.setCamberCoordinates(yCoordinates, i++);

            }

            if (x<COORDINATES_STEP) {
                x = MathSupport.round(x+COORDINATES_LEADING_EDGE_STEP, 3);
            } else {
                x = MathSupport.round(x+COORDINATES_STEP, 3);
            }

        }

    }
}

