package com.barlo.airfoil_constructor.model.NACAAirfoils;


import com.barlo.airfoil_constructor.model.AbstractAirfoil;

public class NACAAirfoil extends AbstractAirfoil {

    // Basic class for all NACA profiles.

    public NACAAirfoil(String Code) {
        super(Code);
    }

    @Override
    public void convertToParameters() {

    }

    /**
     * The leading edge approximates a cylinder with a radius of:
     * r = 1.1019*t^2*c
     */

    /*
        Radius in percents
     */

    @Override
    public void determineLeadingEdgeRadius() {
        setRadius(1.1019*(Math.pow(getT(), 2))/100);
    }
}
