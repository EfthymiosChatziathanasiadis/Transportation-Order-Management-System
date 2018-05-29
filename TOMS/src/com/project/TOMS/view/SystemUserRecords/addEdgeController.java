/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.SystemUserRecords;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.worldCountryRecords.WorldRegistryDatabase;
import com.project.TOMS.model.world.City;
import com.project.TOMS.model.world.Country;
import com.project.TOMS.view.CustomerOrders.CustomerOrdersController;
import java.net.URL;
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
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class addEdgeController implements Initializable {

    @FXML
    private Button addButton;
    @FXML
    private ChoiceBox<Vertex> sourceChoiceBox;
    @FXML
    private ChoiceBox<Vertex> destChoiceBox;

    private graphRegistryDatabase graphDatabase;
    private DispositionsEdgesController parent;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphDatabase = graphRegistryDatabase.getInstance();
        this.setSourceChoiceBox();
        this.setDestChoiceBox();
    }    
    private void setSourceChoiceBox(){
       ObservableList<Vertex> countryObservable = FXCollections.observableArrayList();
       countryObservable = FXCollections.observableArrayList(graphDatabase.getGraph().getVertexes());
       sourceChoiceBox.setItems(countryObservable);
       sourceChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vertex>() {

            @Override
            public void changed(ObservableValue<? extends Vertex> observable, Vertex oldValue, Vertex vertex) {

                sourceChoiceBox.setValue(vertex);
                
                

            }
        });
       
    }
    private void setDestChoiceBox(){
       ObservableList<Vertex> countryObservable = FXCollections.observableArrayList();
       countryObservable = FXCollections.observableArrayList(graphDatabase.getGraph().getVertexes());
       destChoiceBox.setItems(countryObservable);
       destChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vertex>() {

            @Override
            public void changed(ObservableValue<? extends Vertex> observable, Vertex oldValue, Vertex vertex) {

                destChoiceBox.setValue(vertex);
                
                

            }
        });
    }
    @FXML
    private void addEdge(ActionEvent event) {
        if (sourceChoiceBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText("MISSING SOURCE STATION");
            alert.setContentText("Please select a  source country");
            alert.showAndWait();
            return;
        }
        if (destChoiceBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText("MISSING DESTINATION STATION");
            alert.setContentText("Please select a destination station!");
            alert.showAndWait();
            return;
        }
        
      
        graphDatabase.addEdge(sourceChoiceBox.getSelectionModel().getSelectedItem(), 
                destChoiceBox.getSelectionModel().getSelectedItem());
        parent.outputTableData();
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
        
    }
    public void setParentController(DispositionsEdgesController parent){
        this.parent = parent;
    }
    
}