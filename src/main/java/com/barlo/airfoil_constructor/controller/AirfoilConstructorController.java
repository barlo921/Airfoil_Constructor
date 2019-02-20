package com.barlo.airfoil_constructor.controller;


import com.barlo.airfoil_constructor.exception.AbstractAirfoilConstructorException;
import com.barlo.airfoil_constructor.exception.MissMatchAirfoilTypeAndCodeException;
import com.barlo.airfoil_constructor.model.AirfoilType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;

public class AirfoilConstructorController {

    @FXML
    private TextField textField;

    @FXML
    private LineChart lineChart;

    @FXML
    private ComboBox<AirfoilType> airfoilSelector;


    //initialize method invokes on program start
    @FXML
    public void initialize() {

        //Fill ComboBox with Airfoil Types with specified values and ides
        airfoilSelector.getItems().addAll(new AirfoilType("NACA 4-Digit", 4),
                                            new AirfoilType("NACA 4-Digit Modified", 41),
                                            new AirfoilType("NACA 5-Digit", 5),
                                            new AirfoilType("NACA 16 Series", 16));

        //Set Default value for ComboBox
        airfoilSelector.setValue(new AirfoilType("NACA 4-Digit", 4));

        //Add converter to interpret String values of AirfoilType
        airfoilSelector.setConverter(new StringConverter<AirfoilType>(){

            @Override
            public String toString(AirfoilType object) {
                return object.getValue();
            }

            @Override
            public AirfoilType fromString(String string) {
                return null;
            }
        });

    }

    //On action event for Build button. This method drives forward all calculations and show airfoil in Line Chart
    @FXML
    public void drawAirfoil() {

        lineChart.getData().clear();

        AirfoilSeriesController airfoilSeriesController = new AirfoilSeriesController(airfoilSelector.getValue(), textField.getText(), lineChart);

        try {
            airfoilSeriesController.determineNACAAirfoilSeries();
        } catch (MissMatchAirfoilTypeAndCodeException e) {
            showAlert(e);
        }



    }

    //This method creates window with information about airfoil constructor
    @FXML
    public void newAboutWindow() throws IOException {
        //Load Window GIU from FXML file
        newWindow(new FXMLLoader(getClass().getResource("/about.fxml")));
    }

    //This method creates window with airfoils database information
    @FXML
    public void airfoilsDBWindow() throws IOException {
        newWindow(new FXMLLoader(getClass().getResource("/airfoilsDB.fxml")));
    }

    //Method to create new window for different purpose. It parameter FXMLLoader with URL to FXML file
    private void newWindow(final FXMLLoader loader) throws IOException {
        Parent root = loader.load();

        //Set new Stage
        Stage newWindow = new Stage();
        //Set title for window
        newWindow.setTitle("About");
        //Set owner for this new window. It makes primary stage unavailable while new window is opened
        newWindow.initOwner(com.barlo.airfoil_constructor.model.Window.getPrimaryStage());

        Scene scene = new Scene(root);
        newWindow.setScene(scene);
        newWindow.showAndWait();
    }

    //Alert window if exception occurred. It uses information from exception to form message for user
    private void showAlert(final AbstractAirfoilConstructorException e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(e.getException());

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText(e.getMessage());

        textField.clear();

        alert.showAndWait();
    }

}
