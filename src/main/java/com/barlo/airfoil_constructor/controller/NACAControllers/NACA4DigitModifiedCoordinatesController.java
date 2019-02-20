package com.barlo.airfoil_constructor.controller.NACAControllers;


import com.barlo.airfoil_constructor.controller.support.MathSupport;
import com.barlo.airfoil_constructor.model.AirfoilCoordinates;
import com.barlo.airfoil_constructor.model.NACAAirfoils.NACA4DigitModifiedAirfoil;

public class NACA4DigitModifiedCoordinatesController extends NACA4DigitCoordinatesController {

    /**
     * t is maximum thickness
     * c is cord
     * T - position of maximum thickness in tenth of cord
     * I - designation of the leading edge radius
     *
     * Thickness distribution is given by:
     * yT/c = 5*(t/c)[a0*sqrt(x/c) + a1*(x/c) + a2*(x/c)^2 + a3*(x/c)^3]  0 < x/c < T
     * yT/c = 5*(t/c)[0.002 + d1*(1-x/c) + d2*(1-x/c)^2 + d3*(1-x/c)^3]   T < x/c < c
     *
     * d1 = (2.24 - 5.42T + 12.3T^2)/(10*(1-0.878T))
     * d2 = (0.294 - 2(1-T)*d1)/(1-T)^2
     * d3 = (-0.196 + (1-T)d1)/(1-T)^3
     *
     * a0 = 0.296904*Xle
     * Xle = I/6    I<=8
     * Xle = 10.3933    I=9
     *
     * p1 = (1/5)*((1-T)^2/(0.588 - 2d1*(1-T)))
     *
     * a1 = 0.3/T - ((15/8)*(a0/sqrt(T))) - T/10p1
     * a2 = -0.3/T^2 + ((5/4)*a0/(T^(3/2)) + T/5p1
     * a3 = 0.1/T^3 - (0.375*a0)/T^(5/2) - 1/10p1*T
     */

    private int iLeadingEdge;
    private double thicknessPosition; // T - position of maximum thickness in tenth of cord

    private double [] aCoefficients = new double[4];
    private double [] dCoefficients = new double[3];

    public NACA4DigitModifiedCoordinatesController(NACA4DigitModifiedAirfoil naca4DigitModifiedAirfoil, AirfoilCoordinates airfoilCoordinates) {
        super(naca4DigitModifiedAirfoil, airfoilCoordinates);

        iLeadingEdge = naca4DigitModifiedAirfoil.getILeadingEdge(); // I - designation of the leading edge radius
        thicknessPosition = naca4DigitModifiedAirfoil.getThicknessPosition()/100; // T - position of maximum thickness in tenth of cord

        calculateCoefficients();

    }

    @Override
    protected void calculateDefaultThicknessCoordinates() {

        double constantValue = 5*t;

        int i = 0;

        for (double x = 0; x<=1.0;) {

            double yT;

            if (x<=thicknessPosition) {
                yT = constantValue * (aCoefficients[0] * Math.sqrt(x) + aCoefficients[1] * x + aCoefficients[2] * Math.pow(x, 2) + aCoefficients[3] * Math.pow(x, 3));
            } else {
                yT = constantValue * (0.002 + dCoefficients[0]*(1-x) + dCoefficients[1]*Math.pow(1-x,2) + dCoefficients[2]*Math.pow(1-x,3));
            }

            airfoilCoordinates.setThicknessDefaultCoordinates(yT, x, i++);

            if (x<COORDINATES_STEP) {
                x = MathSupport.round(x+COORDINATES_LEADING_EDGE_STEP, 3);
            } else {
                x = MathSupport.round(x+COORDINATES_STEP, 3);
            }

        }

    }


    //Calculating a & d coefficients for thickness distribution calculations
    private void calculateCoefficients() {

        //First calculating d coefficients because it will be use in a coefficients calculations
        dCoefficients[0] = (2.24 - 5.42*thicknessPosition + 12.3*Math.pow(thicknessPosition, 2))/(10*(1-0.878*thicknessPosition));
        dCoefficients[1] = (0.294 - 2*(1-thicknessPosition)*dCoefficients[0])/Math.pow(1-thicknessPosition, 2);
        dCoefficients[2] = (-0.196 + (1-thicknessPosition)*dCoefficients[0])/Math.pow(1-thicknessPosition, 3);

        //xLe - constant value to calculate a0 coefficient.
        double xLE = iLeadingEdge/6;


        //p1 coefficient will be used in a coefficients calculations
        double p1 = ((0.2*(Math.pow(1-thicknessPosition, 2)))/(0.588 - 2*dCoefficients[0]*(1-thicknessPosition)));


        //a coefficients calculations
        aCoefficients[0] = 0.296904*xLE;
        aCoefficients[1] = (0.3/thicknessPosition) - (1.875*(aCoefficients[0]/Math.sqrt(thicknessPosition))) - thicknessPosition/(10*p1);
        aCoefficients[2] = -(0.3/Math.pow(thicknessPosition, 2)) + (1.25*(aCoefficients[0]/Math.pow(thicknessPosition, 1.5))) + 1/(5*p1);
        aCoefficients[3] = (0.1/Math.pow(thicknessPosition, 3)) - ((0.375*aCoefficients[0])/Math.pow(thicknessPosition, 2.5)) - 1/(10*p1*thicknessPosition);

    }
}
