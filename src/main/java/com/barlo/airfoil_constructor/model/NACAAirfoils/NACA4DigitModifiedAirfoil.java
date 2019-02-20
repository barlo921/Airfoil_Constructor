package com.barlo.airfoil_constructor.model.NACAAirfoils;


import java.util.regex.Pattern;

public class NACA4DigitModifiedAirfoil extends NACA4DigitCamberedAirfoil {

    /**
     * This airfoil is an extension of the 4-digit series to allow for a variation of leading edge radius and location of maximum thickness.
     * The numbering system is defined by:
     * NACA MPXX-IT
     * where MPXX is the standard 4-digit designation and the IT appended at the end describes the modification to the thickness distribution.
     * They are defined as:
     * I - designation of the leading edge radius
     * T - chordwise position of maximum thickness in tenths of chord
     */

    private int iLeadingEdge; // designation of leading edge radius from 0 to 9
    private double thicknessPosotion; // chordwise position of maximum thickness in tenths of chord

    public NACA4DigitModifiedAirfoil(String NACACode) {
        super(NACACode);
        convertToParameters();
    }

    @Override
    public void convertToParameters() {

        super.convertToParameters();

        iLeadingEdge = Integer.parseInt(getCode().substring(5, 6));
        thicknessPosotion = Double.parseDouble(getCode().substring(6))*10;

        if (Pattern.matches("00*", getCode())){
            setM(0);
            setP(30);
        }

    }

    public int getILeadingEdge() {
        return iLeadingEdge;
    }

    public double getThicknessPosition() {
        return thicknessPosotion;
    }
}
