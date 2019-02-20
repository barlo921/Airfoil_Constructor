package com.barlo.airfoil_constructor.model;


import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window extends Application {

    @FXML
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Save primary stage to static field
        setPrimaryStage(primaryStage);

        //Load FXML file with GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Airfoil_Constructor_FXML_2_0.fxml"));
        Parent root = loader.load();

        //Set new scene to Stage and show it
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //Static method to get Primary Stage for other classes
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
