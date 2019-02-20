package com.barlo.airfoil_constructor.controller.NACAControllers;


import com.barlo.airfoil_constructor.controller.AirfoilCoordinatesInterface;
import com.barlo.airfoil_constructor.controller.support.MathSupport;
import com.barlo.airfoil_constructor.model.AirfoilCoordinates;
import com.barlo.airfoil_constructor.model.NACAAirfoils.NACAAirfoil;

import java.util.regex.Pattern;

public class NACA4DigitCoordinatesController implements AirfoilCoordinatesInterface {

    /**
     NACA Four-Digit Series:

     The first family of airfoils designed using this approach became known as the NACA Four-Digit Series. The first digit specifies the maximum camber (m) in percentage of the chord (nacaAirfoil length), the second indicates the position of the maximum camber (p) in tenths of chord, and the last two numbers provide the maximum thickness (t) of the nacaAirfoil in percentage of chord. For example, the NACA 2415 nacaAirfoil has a maximum thickness of 15% with a camber of 2% located 40% back from the nacaAirfoil leading edge (or 0.4c). Utilizing these m, p, and t values, we can compute the coordinates for an entire nacaAirfoil using the following relationships:

     1. Pick values of x from 0 to the maximum chord c.

     2. Compute the mean camber line coordinates by plugging the values of m and p into the following equations for each of the x coordinates.

     Yc = m/p^2(2px-x^2)    0<=x<=pc
     Yc = m/(1-p)^2[(1-2p)+2px-x^2] pc<=x<=c

     3. Calculate the thickness distribution above (+) and below (-) the mean line by plugging the value of t into the following equation for each of the x coordinates.

     Yt = t/0.2(0.2699sqrt(x)-0.1260x-0.3516x^2+0.2843x^3-0.1036x^4)

     4. Determine the final coordinates for the nacaAirfoil upper surface (xU, yU) and lower surface (xL, yL) using the following relationships.

     Xu = x-Yt sin(Fi)
     Yu = Yc+Yt cos(Fi)
     Xl = x+Yt sin(Fi)
     Yu = Yc-Yt cos(Fi)

     where Fi = arctan(dYc/dX)

     dYc/dX are

     (2m/p^2)(p-x/c)  0<=x<=pc
     (2m/(1-p)^2)(p-x/c)    pc<=x<=c

     */

    /*
        NACA4DigitCoordinatesController is the logic for calculating coordinates for drawing the NACA 4-Digit Airfoil.
     */

    final double COORDINATES_STEP = 0.05;
    final double COORDINATES_LEADING_EDGE_STEP = 0.01;

    protected double m;
    protected double p;
    protected double t;

    double[] dYdX = new double[AirfoilCoordinates.getLevelOfAirfoilDetail()]; // Coefficients needed for arctan.

    AirfoilCoordinates airfoilCoordinates;

    private NACAAirfoil nacaAirfoil;

    public NACA4DigitCoordinatesController(final NACAAirfoil nacaAirfoil, final AirfoilCoordinates airfoilCoordinates) {
        this.m = nacaAirfoil.getM()/100;
        this.p = nacaAirfoil.getP()/100;
        this.t = nacaAirfoil.getT()/100;

        this.airfoilCoordinates = airfoilCoordinates;

        this.nacaAirfoil = nacaAirfoil;

    }


    @Override
    public void calculateYCamberCoordinates() {

        double constantValue = m/Math.pow(p, 2);

        int i = 0;

        for (double x = 0; x<1.0;) {

            if (x<p) {

                double yCoordinate = constantValue*((2*p*x) - (Math.pow(x, 2)));
                airfoilCoordinates.setCamberCoordinates(yCoordinate, i++);

            } else {

                constantValue = m/Math.pow(1-p, 2);

                double yCoordinate = constantValue*((1-2*p)+(2*p*x)-Math.pow(x, 2));
                airfoilCoordinates.setCamberCoordinates(yCoordinate, i++);

            }

            if (x<COORDINATES_STEP) {
                x = MathSupport.round(x+COORDINATES_LEADING_EDGE_STEP, 3);
            } else {
                x = MathSupport.round(x+COORDINATES_STEP, 3);
            }

        }

    }

    @Override
    public void calculateThicknessCoordinates() {

        /*
            First this method calculate's thickness distribution for non-cambered airfoil using formulas.
         */

        calculateDefaultThicknessCoordinates();

        /*
            Then if first digit isn't "0" which means that airfoil cambered.
            Method gets default thickness distribution coordinates for non-cambered airfoil and compute new distribution.
         */

        if (!Pattern.matches("0*", nacaAirfoil.getCode())) {

            int i =0;

            dYdXCalculator();

            for (double x = 0; x<=1.0;) {

                double yT = airfoilCoordinates.getThicknessYUpperCoordinate(i);

                double xUpperCoordinate = x - yT*Math.sin(Math.atan(dYdX[i]));
                double xLowerCoordinate = x + yT*Math.sin(Math.atan(dYdX[i]));

                double yUpperCoordinate = airfoilCoordinates.getYCamberCoordinate(i) + yT*Math.cos(Math.atan(dYdX[i]));
                double yLowerCoordinate = airfoilCoordinates.getYCamberCoordinate(i) - yT*Math.cos(Math.atan(dYdX[i]));

                airfoilCoordinates.setThicknessYUpperCoordinates(yUpperCoordinate, i);
                airfoilCoordinates.setThicknessYLowerCoordinates(yLowerCoordinate, i);
                airfoilCoordinates.setThicknessXUpperCoordinates(xUpperCoordinate, i);
                airfoilCoordinates.setThicknessXLowerCoordinates(xLowerCoordinate, i++);

                if (x<COORDINATES_STEP) {
                    x = MathSupport.round(x+COORDINATES_LEADING_EDGE_STEP, 3);
                } else {
                    x = MathSupport.round(x+COORDINATES_STEP, 3);
                }

            }

        }

    }

    @Override
    public double getCOORDINATES_STEP() {
        return COORDINATES_STEP;
    }

    @Override
    public double getCOORDINATES_LEADING_EDGE_STEP() {
        return COORDINATES_LEADING_EDGE_STEP;
    }


    /*
        calculateDefaultThicknessCoordinates method define thickness distribution for non-cambered airfoil.
        This coordinates will be used in various calculations for different NACA cambered airfoils to define thickness coordinates.
     */
    protected void calculateDefaultThicknessCoordinates() {

        double constantValue = t/0.2;

        int i = 0;

        for (double x = 0; x<=1.0;) {

            double yT = constantValue * (0.2969 * Math.sqrt(x) -
                        0.1260 * x -
                        0.3516 * Math.pow(x, 2) +
                        0.2843 * Math.pow(x, 3) -
                        0.1036 * Math.pow(x, 4));

            airfoilCoordinates.setThicknessDefaultCoordinates(yT, x, i++);

            if (x<COORDINATES_STEP) {
                x = MathSupport.round(x+COORDINATES_LEADING_EDGE_STEP, 3);
            } else {
                x = MathSupport.round(x+COORDINATES_STEP, 3);
            }

        }

    }


    /*
        dYdXNACA4DigitCalculator determine Fi = arctan(dYc/dX) coefficients for NACA 4 digit airfoil.
     */
    protected void dYdXCalculator() {

        double constantValue = 2*m/Math.pow(p, 2);

        int i = 0;

        for (double x = 0; x<=1.0;) {

            if (x<p) {
                dYdX[i++] = constantValue * (p - x);
            } else {
                constantValue = 2*m/Math.pow(1-p, 2);
                dYdX[i++] = constantValue*(p-x);
            }

            if (x<COORDINATES_STEP) {
                x = MathSupport.round(x+COORDINATES_LEADING_EDGE_STEP, 3);
            } else {
                x = MathSupport.round(x+COORDINATES_STEP, 3);
            }
        }

    }

}
