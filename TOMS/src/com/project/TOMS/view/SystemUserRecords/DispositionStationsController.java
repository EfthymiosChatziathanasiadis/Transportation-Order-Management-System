/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.SystemUserRecords;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Graph;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.worldCountryRecords.WorldRegistryDatabase;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerRecords.Customer;
import com.project.TOMS.model.Vehicles.Trailer;
import com.project.TOMS.model.world.City;
import com.project.TOMS.model.world.Country;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class DispositionStationsController implements Initializable {

    @FXML
    private TableView<Vertex> vertexTable;
    @FXML
    private TableColumn<Vertex, String> idCol;
    @FXML
    private TableColumn<Vertex, String> countryCol;
    @FXML
    private TableColumn<Vertex, String> cityCol;
    @FXML
    private TableColumn<Vertex, String> latCol;
    @FXML
    private TableColumn<Vertex, String> longCol;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ComboBox<Country> countryChoiceBox;
    @FXML
    private ComboBox<City> cityChoiceBox;
    
    private WorldRegistryDatabase worldDatabase;
    private ObservableList<Vertex> vertexObservable;
    private graphRegistryDatabase graphDatabase;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        worldDatabase = WorldRegistryDatabase.getInstance();
        vertexObservable = FXCollections.observableArrayList();
        graphDatabase = graphRegistryDatabase.getInstance();
        this.setTable();
        this.setCountryChoiceBox();
        this.outputTableData();
    }
     public void outputTableData(){
        vertexObservable.clear();
        Graph graph = graphDatabase.getGraph();
        ArrayList<Vertex> vertices = graph.getVertexes();
        vertexObservable = FXCollections.observableArrayList(vertices);
        vertexTable.setItems(vertexObservable);            
    }
    private void setTable() {
        
        idCol.setCellValueFactory(new PropertyValueFactory<Vertex, String>("id"));
        countryCol.setCellValueFactory(new PropertyValueFactory<Vertex, String>("country"));
        cityCol.setCellValueFactory(new PropertyValueFactory<Vertex, String>("city"));
        latCol.setCellValueFactory(new PropertyValueFactory<Vertex, String>("latitude"));
        longCol.setCellValueFactory(new PropertyValueFactory<Vertex, String>("longt"));
    }
    private void setCountryChoiceBox(){
       ObservableList<Country> countryObservable = FXCollections.observableArrayList();
       countryObservable = FXCollections.observableArrayList(worldDatabase.getCountries());
       countryChoiceBox.setItems(countryObservable);
       countryChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Country>() {

            @Override
            public void changed(ObservableValue<? extends Country> observable, Country oldValue, Country country) {

                countryChoiceBox.setValue(country);
                ObservableList<City> cityObservable = FXCollections.observableArrayList();
                cityObservable = FXCollections.observableArrayList(worldDatabase.getCitiesWhereCountry(country));
                cityChoiceBox.setItems(cityObservable);
                cityChoiceBox.getSelectionModel().selectFirst();
                

            }
        });
    }
    

    @FXML
    private void addVertex(ActionEvent event) {
        if (countryChoiceBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a country first, then a city and finally add a station!");
            alert.showAndWait();
            return;
        }
        if (cityChoiceBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a city add a station!");
            alert.showAndWait();
            return;
        }
        Country country = countryChoiceBox.getSelectionModel().getSelectedItem();
        City city = cityChoiceBox.getSelectionModel().getSelectedItem();
        if(!graphDatabase.addVertex(country.getCountry(), city.getCity(), city.getLat(), city.getLon())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Station in "+city+", "+country+" exists in the database!");
            alert.showAndWait();
        }else{
             this.outputTableData();
        }
       
    }

    @FXML
    private void deleteHoliday(ActionEvent event) {
    }
    
}
