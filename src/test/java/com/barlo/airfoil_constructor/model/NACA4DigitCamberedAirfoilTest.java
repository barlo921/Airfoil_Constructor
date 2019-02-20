package com.barlo.airfoil_constructor.model;

import com.barlo.airfoil_constructor.model.NACAAirfoils.NACA4DigitCamberedAirfoil;
import org.junit.Test;

import static org.junit.Assert.*;


public class NACA4DigitCamberedAirfoilTest {

    @Test
    public void NACA4DigitConvertParametersTest() throws Exception {
        String NACACode = "2415";

        int m = 2; // Maximum thickness
        int p = 40; // Camber
        int t = 15; //Camber location

        NACA4DigitCamberedAirfoil naca4DigitCamberedAirfoil = new NACA4DigitCamberedAirfoil(NACACode);

        assertEquals(m, (int) naca4DigitCamberedAirfoil.getM());
        assertEquals(p, (int) naca4DigitCamberedAirfoil.getP());
        assertEquals(t, (int) naca4DigitCamberedAirfoil.getT());

    }
}