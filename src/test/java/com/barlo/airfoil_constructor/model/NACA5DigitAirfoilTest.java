package com.barlo.airfoil_constructor.model;

import com.barlo.airfoil_constructor.model.NACAAirfoils.NACA5DigitAirfoil;
import org.junit.Test;

import static org.junit.Assert.*;


public class NACA5DigitAirfoilTest {

    @Test
    public void calculateRCoefficientTest() {

        int i = 1;

        double [] r = {0.0581, 0.1257, 0.2027, 0.2903, 0.3913};

        for (double rCoefficient : r) {

            String nacaCode = "2" + i++ + "012";

            assertEquals(rCoefficient, new NACA5DigitAirfoil(nacaCode).getR(), 0.0);

        }

    }


    @Test
    public void calculateK1CoefficientTest() {

        int i = 1;

        double [] k1 = {350.056, 51.622, 15.916, 6.625, 3.224};

        for (double k1Coefficient : k1) {

            String nacaCode = "2" + i++ + "012";

            assertEquals(k1Coefficient, new NACA5DigitAirfoil(nacaCode).getK1(), 0.0);

        }

    }

}