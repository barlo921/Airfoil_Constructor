package com.barlo.airfoil_constructor.controller;


public interface AirfoilCoordinatesInterface {

    /*
        calculateYCamberCoordinate method determine Y coordinates of the airfoil camber line.
        It put's coordinates to Object of AirfoilCoordinates Class which store's it.
     */
    void calculateYCamberCoordinates();


    /*
        calculateThicknessCoordinates method determine Xu, Yu, Xl and Yl coordinates of the airfoil thickness.
        It put's coordinates to Object of AirfoilCoordinates Class which store's it.
     */
    void calculateThicknessCoordinates();


    /*
        Returns step between points at x axis.
     */
    double getCOORDINATES_STEP();


    /*
        Returns step between points at x axis for leading edge.
     */
    double getCOORDINATES_LEADING_EDGE_STEP();

}
