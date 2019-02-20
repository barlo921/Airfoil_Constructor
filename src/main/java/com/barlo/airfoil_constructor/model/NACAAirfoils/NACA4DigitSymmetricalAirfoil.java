package com.barlo.airfoil_constructor.model.NACAAirfoils;


public class NACA4DigitSymmetricalAirfoil extends NACAAirfoil {

    /**
     * The numbering system for these airfoils is defined by:
     * NACA MPXX
     * where XX is the maximum thickness, t/c, in percent chord.
     * M is the maximum value of the mean line in hundredths of chord,
     * P is the chordwise position of the maximum camber in tenths of the chord.
     * Note that although the numbering system implies integer values, the equations can provide 4 digit foils for arbitrary values of M, P, and XX.
     */

    /*
        Model of NACA 4-Digit Symmetrical Airfoil, for example NACA 0015.
        It convert digit abbreviation into understandable for construction parameters.
     */

    public NACA4DigitSymmetricalAirfoil(String NACACode) {
        super(NACACode);
        convertToParameters();
        determineLeadingEdgeRadius();
    }

    /*
        convertToParameters method convert's 4-digit abbreviation into thickness, camber and camber location parameters.
         For symmetrical airfoil camber is always 0 and maximum thickness at 30% by default.
    */

    @Override
    public void convertToParameters() {
        setM(0);
        setP(30);
        setT(Double.parseDouble(getCode().substring(2,4)));
    }
}
