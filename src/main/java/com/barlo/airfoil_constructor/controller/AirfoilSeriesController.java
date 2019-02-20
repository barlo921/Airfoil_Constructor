package com.barlo.airfoil_constructor.controller;


import com.barlo.airfoil_constructor.controller.NACAControllers.NACA4DigitCoordinatesController;
import com.barlo.airfoil_constructor.controller.NACAControllers.NACA4DigitModifiedCoordinatesController;
import com.barlo.airfoil_constructor.controller.NACAControllers.NACA5DigitCoordinatesController;
import com.barlo.airfoil_constructor.controller.support.MathSupport;
import com.barlo.airfoil_constructor.exception.MissMatchAirfoilTypeAndCodeException;
import com.barlo.airfoil_constructor.model.*;
import com.barlo.airfoil_constructor.model.NACAAirfoils.*;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.regex.Pattern;

public class AirfoilSeriesController {

    private String code;
    private AirfoilType airfoilType;

    private XYChart.Series<Number, Number> thicknessUpper = new XYChart.Series<>();
    private XYChart.Series<Number, Number> thicknessLower = new XYChart.Series<>();
    private XYChart.Series<Number, Number> camber = new XYChart.Series<>();

    @FXML
    private LineChart lineChart;

    public AirfoilSeriesController(final AirfoilType airfoilType, final String code, final LineChart lineChart) {
        this.airfoilType = airfoilType;
        this.code = code;
        this.lineChart = lineChart;
    }

    //This method determine which NACA type we shall draw
    public void determineNACAAirfoilSeries() throws MissMatchAirfoilTypeAndCodeException {

        switch (airfoilType.getId()) {
            case 4:
                if (checkAirfoilTypeAndCode(AirfoilTypePattern.getNACA4DigitPattern())) draw4DigitAirfoil();
                break;
            case 41:
                if (checkAirfoilTypeAndCode(AirfoilTypePattern.getNACA4DigitModifiedPattern())) draw4DigitModifiedAirfoil();
                break;
            case 16:
                if (checkAirfoilTypeAndCode(AirfoilTypePattern.getNACA16SeriesPattern())) {

                    /**
                     * The NACA 16 series is a special case of modified 4-Digit airfoil
                     * with leading edge radius index I = 4
                     * and the maximum thickness located at x/c = 0.5 (T = 5)
                     * As an example, the NACA 16-012 is equivalent an NACA 0012-45
                     *
                     */

                    code = "0" + code.substring(3, 6) + "-45";
                    draw4DigitModifiedAirfoil();
                }
                break;
            case 5:
                if (checkAirfoilTypeAndCode(AirfoilTypePattern.getNACA5DigitPattern())) draw5DigitAirfoil();
                break;
        }

    }


    // checkAirfoilTypeAndCode method check's whether Code satisfy specified Type
    private boolean checkAirfoilTypeAndCode(final String pattern) throws MissMatchAirfoilTypeAndCodeException {

        //Delete all spaces in code
        code = code.replaceAll("\\s", "");

        if (!Pattern.matches(pattern, code)) {
            throw new MissMatchAirfoilTypeAndCodeException(airfoilType.getValue());
        } else {
            return true;
        }

    }

    private void draw4DigitAirfoil() {

        NACAAirfoil nacaAirfoil;

        AirfoilCoordinates airfoilCoordinates = new AirfoilCoordinates();
        NACA4DigitCoordinatesController naca4DigitCoordinatesController;


        //Choose model (cambered or non-cambered airfoil)
        if (Pattern.matches("00*", code)) {
            nacaAirfoil = new NACA4DigitSymmetricalAirfoil(code);
        } else {
            nacaAirfoil = new NACA4DigitCamberedAirfoil(code);
        }

        naca4DigitCoordinatesController = new NACA4DigitCoordinatesController(nacaAirfoil, airfoilCoordinates);

        naca4DigitCoordinatesController.calculateYCamberCoordinates();
        naca4DigitCoordinatesController.calculateThicknessCoordinates();

        draw(naca4DigitCoordinatesController.getCOORDINATES_LEADING_EDGE_STEP(), naca4DigitCoordinatesController.getCOORDINATES_STEP(), airfoilCoordinates);

    }

    private void draw4DigitModifiedAirfoil() {

        NACA4DigitModifiedAirfoil naca4DigitModifiedAirfoil = new NACA4DigitModifiedAirfoil(code);

        AirfoilCoordinates airfoilCoordinates = new AirfoilCoordinates();
        NACA4DigitModifiedCoordinatesController naca4DigitModifiedCoordinatesController = new NACA4DigitModifiedCoordinatesController(naca4DigitModifiedAirfoil, airfoilCoordinates);

        naca4DigitModifiedCoordinatesController.calculateYCamberCoordinates();
        naca4DigitModifiedCoordinatesController.calculateThicknessCoordinates();

        draw(naca4DigitModifiedCoordinatesController.getCOORDINATES_LEADING_EDGE_STEP(),
                naca4DigitModifiedCoordinatesController.getCOORDINATES_STEP(),
                airfoilCoordinates);

    }

    private void draw5DigitAirfoil() {

        NACA5DigitAirfoil naca5DigitAirfoil;

        AirfoilCoordinates airfoilCoordinates = new AirfoilCoordinates();
        NACA5DigitCoordinatesController naca5DigitCoordinatesController;

        naca5DigitAirfoil = new NACA5DigitAirfoil(code);

        naca5DigitCoordinatesController = new NACA5DigitCoordinatesController(naca5DigitAirfoil, airfoilCoordinates);

        naca5DigitCoordinatesController.calculateYCamberCoordinates();
        naca5DigitCoordinatesController.calculateThicknessCoordinates();

        draw(naca5DigitCoordinatesController.getCOORDINATES_LEADING_EDGE_STEP(), naca5DigitCoordinatesController.getCOORDINATES_STEP(), airfoilCoordinates);

    }

    private void draw6DigitAirfoil() {
        //TODO: Add NACA 6-Digit airfoil logic
    }


    //Common method to add airfoil points to Axis of the LineChart
    private void draw(final double leadingEdgeStep, final double coordinatesStep, final AirfoilCoordinates airfoilCoordinates) {

        int i = 0;

        for (double x = 0; x<=1.0;) {

            camber.getData().add(new XYChart.Data<>(x, airfoilCoordinates.getYCamberCoordinate((i++))));

            if (x<coordinatesStep) {
                x = MathSupport.round(x+leadingEdgeStep, 3);
            } else {
                x = MathSupport.round(x+coordinatesStep, 3);
            }

        }


        i = 0;

        for (; i<AirfoilCoordinates.getLevelOfAirfoilDetail(); i++) {
            thicknessUpper.getData().add(new XYChart.Data<>(airfoilCoordinates.getThicknessXUpperCoordinate(i), airfoilCoordinates.getThicknessYUpperCoordinate(i)));
            thicknessLower.getData().add(new XYChart.Data<>(airfoilCoordinates.getThicknessXLowerCoordinate(i), airfoilCoordinates.getThicknessYLowerCoordinate(i)));
        }

        lineChart.getData().addAll(thicknessUpper, thicknessLower, camber);

    }

    //Static nested class for storing patterns of different airfoils
    private static class AirfoilTypePattern {

        //Code patterns for checks
        private static String NACA4DigitPattern = "[0-9][0-9][0-9][0-9]";
        private static String NACA4DigitModifiedPattern = "[0-9][0-9][0-9][0-9][-][0-9][0-9]";
        private static String NACA16SeriesPattern = "16-0[0-9][0-9]";
        private static String NACA5DigitPattern = "[0-9][0-9][0-1][0-9][0-9]";

        public static String getNACA4DigitPattern() {
            return NACA4DigitPattern;
        }

        public static String getNACA4DigitModifiedPattern() {
            return NACA4DigitModifiedPattern;
        }

        public static String getNACA16SeriesPattern() {
            return NACA16SeriesPattern;
        }

        public static String getNACA5DigitPattern() {
            return NACA5DigitPattern;
        }
    }

}
