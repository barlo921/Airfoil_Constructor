package com.barlo.airfoil_constructor.model;

public class AirfoilCoordinates {

    /*
        Class for storing coordinates that will be used in line chart.
        Coordinates need to visualize airfoil.
     */

    //TODO: user dynamic set's detail level of the airfoil.

    private static int LEVEL_OF_AIRFOIL_DETAIL = 25; // Number of point in Line Chart. More point - smoother airfoil.

    private double[] camberYCoordinates = new double[LEVEL_OF_AIRFOIL_DETAIL]; // This array storing Y coordinates for camber line.
    private double[] thicknessXUpperCoordinates = new double[LEVEL_OF_AIRFOIL_DETAIL]; //This array storing X upper coordinates. It may be default or computed by thickness coordinates and sin(Fi) parameter
    private double[] thicknessXLowerCoordinates = new double[LEVEL_OF_AIRFOIL_DETAIL]; //This array storing X lower coordinates. It may be default or computed by thickness coordinates and sin(Fi) parameter
    private double[] thicknessYUpperCoordinates = new double[LEVEL_OF_AIRFOIL_DETAIL]; //This array storing Y upper coordinates. It may be default or computed by thickness coordinates and cos(Fi) parameter
    private double[] thicknessYLowerCoordinates = new double[LEVEL_OF_AIRFOIL_DETAIL]; //This array storing Y lower coordinates. It may be default or computed by thickness coordinates and cos(Fi) parameter

    //Set coordinates of the mean line of the airfoil
    public void setCamberCoordinates(final double yCoordinate, final int index) {
        this.camberYCoordinates[index] = yCoordinate;
    }

    //Set default thickness coordinates means that airfoil isn't cambered
    public void setThicknessDefaultCoordinates(final double yCoordinate, final double x, final int index) {
        this.thicknessXUpperCoordinates[index] = x;
        this.thicknessYUpperCoordinates[index] = yCoordinate;

        this.thicknessXLowerCoordinates[index] = x;
        this.thicknessYLowerCoordinates[index] = -yCoordinate;
    }

    //Set thickness for cambered airfoils upper and lower X Axis
    public void setThicknessXUpperCoordinates(final double xCoordinate, final int index) {
        this.thicknessXUpperCoordinates[index] = xCoordinate;
    }

    public void setThicknessXLowerCoordinates(final double xCoordinate, final int index) {
        this.thicknessXLowerCoordinates[index] = xCoordinate;
    }

    public void setThicknessYUpperCoordinates(final double yCoordinate, final int index) {
        this.thicknessYUpperCoordinates[index] = yCoordinate;
    }

    public void setThicknessYLowerCoordinates(final double yCoordinate, final int index) {
        this.thicknessYLowerCoordinates[index] = yCoordinate;
    }


    public double getYCamberCoordinate(final int index) {
        return camberYCoordinates[index];
    }

    public double getThicknessXUpperCoordinate(final int index) {
        return thicknessXUpperCoordinates[index];
    }

    public double getThicknessXLowerCoordinate(final int index) {
        return thicknessXLowerCoordinates[index];
    }

    public double getThicknessYUpperCoordinate(final int index) {
        return thicknessYUpperCoordinates[index];
    }

    public double getThicknessYLowerCoordinate(final int index) {
        return thicknessYLowerCoordinates[index];
    }

    public static int getLevelOfAirfoilDetail() {
        return LEVEL_OF_AIRFOIL_DETAIL;
    }

}
