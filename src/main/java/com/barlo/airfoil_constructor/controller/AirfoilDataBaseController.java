package com.barlo.airfoil_constructor.controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.json.*;
import java.io.*;

public class AirfoilDataBaseController {

    //This class adds data from JSON to TableView

    //TableColumn fields injected from FXML file
    @FXML TableView<AirfoilModel> airfoilDataBaseTable;
    @FXML TableColumn<AirfoilModel, String> airplane;
    @FXML TableColumn<AirfoilModel, String> rootAirfoil;
    @FXML TableColumn<AirfoilModel, String> tipAirfoil;

    //List to add data to TableView
    private ObservableList<AirfoilModel> data;

    //This method invokes when Airfoil Data Base window would initialize
    @FXML
    public void initialize() {

        //Initiate ObservableList
        data = FXCollections.observableArrayList();

        //First we need to add all data from JSON (AirfoilDB.json) to ObservableList
        addAirfoilsToObservableList();

        //Value Factories for TableColumns. It's important that parameter be exactly as get method signature e.g. "getAirplaneName()"
        airplane.setCellValueFactory(new PropertyValueFactory<>("AirplaneName"));
        rootAirfoil.setCellValueFactory(new PropertyValueFactory<>("RootAirfoil"));
        tipAirfoil.setCellValueFactory(new PropertyValueFactory<>("TipAirfoil"));

        //Setting items to TableView
        airfoilDataBaseTable.setItems(data);

    }

    //Get data from JSON and add it to ObservableList
    private void addAirfoilsToObservableList(){

        //Because we can get JsonObject only by String name we will use String id to cast int i to String
        String id = "";
        //Get JSON data base
        JsonObject dataBase = new JSONController().getAirfoilsDB();

        for (int i=1; i<=dataBase.size(); i++) {

            //Cast int i to String
            id += i;

            //Get object by specified name
            JsonObject tempObject = (JsonObject) dataBase.get(id);

            AirfoilModel airfoilModel = new AirfoilModel(tempObject.getString("airfoil"),
                                                            tempObject.getString("root"),
                                                            tempObject.getString("tip"));

            data.add(airfoilModel);

            //Flush String id
            id = "";

        }

    }

    //Inner class is model for airfoil that will be add to TableView
    public class AirfoilModel{

        private SimpleStringProperty airplaneName;
        private SimpleStringProperty rootAirfoil;
        private SimpleStringProperty tipAirfoil;

        public AirfoilModel(final String airplaneName,
                            final String rootAirfoil,
                            final String tipAirfoil) {
            this.airplaneName = new SimpleStringProperty(airplaneName);
            this.rootAirfoil = new SimpleStringProperty(rootAirfoil);
            this.tipAirfoil = new SimpleStringProperty(tipAirfoil);
        }

        public String getAirplaneName() {
            return airplaneName.get();
        }


        public String getRootAirfoil() {
            return rootAirfoil.get();
        }


        public String getTipAirfoil() {
            return tipAirfoil.get();
        }

    }

    //Inner class to parse AirfoilsDB and get information
    public class JSONController {

        private static final String JSON_SOURCE_FILE = "/AirfoilDB.json";

        private JsonObject jsonObject;

        public JSONController() {

            InputStream inputStream;
            inputStream = this.getClass().getResourceAsStream(JSON_SOURCE_FILE);


            JsonReader jsonReader = Json.createReader(inputStream);
            jsonObject = jsonReader.readObject();
            jsonReader.close();

        }

        public JsonObject getAirfoilsDB() {
            return jsonObject;
        }

    }


}
