/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.Vehicles;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.Vehicles.TruckDatabaseRegistry;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class addTruckController implements Initializable {

    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private  TextField countryField;
    @FXML
    private  TextField cityField;
    @FXML
    private  TextField streetField;
    @FXML
    private  TextField postCodeField; 
    @FXML
    private RadioButton availableRadio;
    @FXML
    private RadioButton notAvailableRadio;
    @FXML
    private RadioButton damagedRadio;
    @FXML
    private RadioButton notDamagedRadio;
    @FXML
    private TextField trailerPlateNumber;
    @FXML
    private ChoiceBox<Vertex> vertexChoiceBox;
    
    private ToggleGroup availabilityGroup;
    private ToggleGroup conditionGroup;
    
    private VehiclesController parent;
    private TruckDatabaseRegistry truckDatabase;
    private graphRegistryDatabase graphDatabase;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.setToggles();
        truckDatabase = TruckDatabaseRegistry.getInstance();
        graphDatabase = graphRegistryDatabase.getInstance();
        this.setVertexChoiceBox();
    } 
    private void setVertexChoiceBox(){
       ObservableList<Vertex> verticesObservable = FXCollections.observableArrayList();
       verticesObservable = FXCollections.observableArrayList(graphDatabase.getGraph().getVertexes());
       vertexChoiceBox.setItems(verticesObservable);
       vertexChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vertex>() {

            @Override
            public void changed(ObservableValue<? extends Vertex> observable, Vertex oldValue, Vertex vertex) {

                vertexChoiceBox.setValue(vertex);
                
            }
        });
       vertexChoiceBox.getSelectionModel().selectFirst();
    }
    private void setToggles(){
        availabilityGroup=new ToggleGroup();
        availableRadio.setToggleGroup(availabilityGroup);
        notAvailableRadio.setToggleGroup(availabilityGroup);
        availabilityGroup.selectToggle(availableRadio);
        
        conditionGroup=new ToggleGroup();
        damagedRadio.setToggleGroup(conditionGroup);
        notDamagedRadio.setToggleGroup(conditionGroup);
        conditionGroup.selectToggle(notDamagedRadio);
    }

    @FXML
    private void addEntry(ActionEvent event) throws ParseException {
        if (trailerPlateNumber.getText() == null || trailerPlateNumber.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in trailer plate number!");
            alert.showAndWait();
            return;
        }
        
        boolean availability = (availabilityGroup.getSelectedToggle() == availableRadio) 
                ? true : false;
        boolean condition = (conditionGroup.getSelectedToggle() == damagedRadio) 
                ? true : false;
        truckDatabase.addTruck(vertexChoiceBox.getSelectionModel().getSelectedItem(),  availability, 
                trailerPlateNumber.getText(), condition);
        this.parent.outputTruckTable();
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    public void setParentController(VehiclesController parent){
        this.parent=parent;
    }
    
}
