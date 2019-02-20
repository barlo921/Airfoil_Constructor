package com.barlo.airfoil_constructor.model;


public interface Airfoiled {

    /*
        convertToParameters method transform's digit abbreviation of the airfoil into program understandable parameters
     */
    void convertToParameters();

    /*
        determineLeadingEdge method compute cylinder radius for coordinates of the leading edge of the airfoil.
     */
    void determineLeadingEdgeRadius();
}
