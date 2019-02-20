package com.barlo.airfoil_constructor.model.NACAAirfoils;


public class NACA4DigitCamberedAirfoil extends NACA4DigitSymmetricalAirfoil {

    /*
        Model of NACA 4-Digit Cambered Airfoil, for example NACA 2415.
        It convert digit abbreviation into understandable for construction parameters.
     */

    public NACA4DigitCamberedAirfoil(String NACACode) {
        super(NACACode);
    }

    /* convertToParameters method convert's 4-digit abbreviation into thickness, camber and camber location parameters */

    @Override
    public void convertToParameters() {
        setM(Double.parseDouble(getCode().substring(0,1)));
        setP(Double.parseDouble(getCode().substring(1,2))*10);
        setT(Double.parseDouble(getCode().substring(2,4)));
    }

}
