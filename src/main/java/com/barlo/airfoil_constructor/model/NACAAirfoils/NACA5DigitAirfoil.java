package com.barlo.airfoil_constructor.model.NACAAirfoils;


import com.barlo.airfoil_constructor.controller.support.MathSupport;


public class NACA5DigitAirfoil extends NACA4DigitCamberedAirfoil {

    /**
     * This airfoil is an extension of the 4 digit series which provides additional camber lines. The numbering system for these airfoils is defined by:
     * NACA LPQXX
     * where
     * XX is the maximum thickness, t/c, in percent chord.
     * L is the amount of camber; the design lift coefficient is 3/2 L, in tenths
     * P is the designator for the position of maximum camber, xf
     * where xf = P/2, and P is given in tenths of the chord
     * Q = 0; standard 5 digit foil camber
     * = 1; “reflexed” camber
     */

    /*
        NACA 5 digit airfoil, for example NACA 23012.
     */

    private double cL; // Designed lift coefficient. In tenth of the cord
    private int reflex; // Reflex or non-reflex airfoil. Only 0 or 1.
    private double r;
    private double k1;
    private double k1k2;

    /**
     * For every NACA 5 digit airfoil there is r coefficient which gives longitudinal locations of the maximum ordinate
     * of 5, 10, 15, 20 and 25 percent of the cord.
     * The k1 was adjusted so that a theoretical design of lift coefficient of 0.3 was obtained at the ideal angle of attack.
     *
     * Table for non-reflex airfoils
     *
     * Note that computed coefficients may be differ than tabulated. This coefficients were define in 1930's.
     *
     * Camber-line designation      m       r       k1
     *          210             0.05    0.058    361.4
     *          220             0.10    0.126    51.64
     *          230             0.15    0.2025   15.957
     *          240             0.20    0.29     6.643
     *          250             0.25    0.391    3.23
     *
     * Coefficient k1 can be linearly scaled to give any desired lift coefficient.
     */


    public NACA5DigitAirfoil(String NACACode) {
        super(NACACode);
        convertToParameters();
        calculateR();
        calculateK1();

        if (getReflex() == 1) calculateK2K1();
    }

    @Override
    public void convertToParameters(){
        this.cL = Double.parseDouble(getCode().substring(0, 1))*3/20;
        setP(Double.parseDouble(getCode().substring(1,2))/2*10); // In percents
        this.reflex = Integer.parseInt(getCode().substring(2,3));
        setT(Double.parseDouble(getCode().substring(3,5))); // In percents
    }


    public double getcL() {
        return cL;
    }

    public int getReflex() {
        return reflex;
    }

    public double getR() {
        return r;
    }

    public double getK1() {
        return k1;
    }

    public double getK1k2() {
        return k1k2;
    }

    /**
     * m = r*(1-sqrt(r/3))
     * to find r we need to solve cubic equation r^3 - 3r^2 + 6rp - 3p^2 = 0
     * p is the position of maximum camber of the airfoil
     *
     * r = -2sqrt(Q)*cos(fi-(2/3)*Pi) - a/3
     * where fi = 1/3 * arccos(R/sqrt(Q^3))
     *
     * Q = (a^2 - 3b)/9
     * R = (2a^3 - 9ab + 27c)/54
     *
     */

    private void calculateR() {

        final double p = this.getP()/100;

        double Q = (Math.pow(-3, 2) - (3*6*p))/9;
        double R = (2*Math.pow(-3, 3) - (9*-3*6*p) + (27*-3*Math.pow(p, 2)))/54;

        double x = R/Math.sqrt(Math.pow(Q,3));

        double FI = (Math.acos(x))/3;

        this.r = MathSupport.round(-2*Math.sqrt(Q)*Math.cos(FI - (2*Math.PI)/3) + 1, 4);

    }


    /**
     * K1 is defined to avoid leading edge singularity for a prescribed Cl and r.
     *
     * K1 = 6Cl/Q where
     *
     * Q = (3r - 7r^2 + 8r^3 - 4r^4)/(sqrt(r*(1-r))) - (3/2(1-2r))*(Pi/2-sin^-1(1-2r))
     */

    private void calculateK1() {

        final double r = this.getR();

        double q = (3*r - 7*Math.pow(r, 2) + 8*Math.pow(r, 3) - 4*Math.pow(r, 4))/Math.sqrt(r*(1-r));
        q = q - ((3*(1-2*r))/2)*(Math.PI/2 - Math.asin(1-2*r));

        this.k1 = MathSupport.round((6*cL)/q, 3);

    }

    /**
     * k2/k1 = (3*(r-p)^2 - r^3)/(1-r)^3
     */
    /*
        calculate K2K1 method defines k2/k1 coefficient for calculations of thickness distribution for reflex airfoils.
     */
    private void calculateK2K1() {

        final double p = this.getP()/100;
        final double r = this.getR();

        this.k1k2 = (3*Math.pow(r-p, 2) - Math.pow(r, 3))/Math.pow(1-r, 3);

    }

}
