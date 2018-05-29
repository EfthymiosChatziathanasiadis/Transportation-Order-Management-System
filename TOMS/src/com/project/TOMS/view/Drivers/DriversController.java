/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.Drivers;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.Drivers.DriversDatabaseRegistry;
import com.project.TOMS.model.CustomerRecords.Customer;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.Vehicles.Trailer;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class DriversController implements Initializable {

    @FXML
    private TableView<Driver> driversTable;
    @FXML
    private TableColumn<Driver, String> idCol;
    @FXML
    private TableColumn<Driver, String> firstNameCol;
    @FXML
    private TableColumn<Driver, String> lastNameCol;
    @FXML
    private TableColumn<Driver, String> availabilityStatusCol;
    @FXML
    private TableColumn<Driver, String> locationCol;
    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<String> searchOptions;
    @FXML
    private Button searchButton;
    @FXML
    private TextField firstNameField;
    @FXML
    private Label fieldLabel1;
    @FXML
    private TextField lastNameField;
    @FXML
    private Label fieldLabel21;
    @FXML
    private Label fieldLabel2;
    @FXML
    private TextField countryField;
    @FXML
    private Label fieldLabel3;
    @FXML
    private TextField cityField;
    @FXML
    private Label fieldLabel4;
    @FXML
    private Label fieldLabel5;
    @FXML
    private Label fieldLabel6;
    @FXML
    private RadioButton availableRadio;
    
    private ToggleGroup availabilityGroup;
    @FXML
    private RadioButton unavaialableRadio;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ChoiceBox<Vertex> vertexChoiceBox;
    
    private DriversDatabaseRegistry driversDatabase;
    private ObservableList<Driver> driversObservable;
    private graphRegistryDatabase graphDatabase;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        driversDatabase = DriversDatabaseRegistry.getInstance();
        graphDatabase = graphRegistryDatabase.getInstance();
        this.setTable();
        driversObservable = FXCollections.observableArrayList();
        this.setToggles();
        try {
            this.outputTableData();
        } catch (ParseException ex) {
            Logger.getLogger(DriversController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        unavaialableRadio.setToggleGroup(availabilityGroup);
        availabilityGroup.selectToggle(availableRadio);
    }
    private void setTable() {
        
        idCol.setCellValueFactory(new PropertyValueFactory<Driver, String>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Driver, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Driver, String>("lastName"));
        availabilityStatusCol.setCellValueFactory(new PropertyValueFactory<Driver, String>("availabilityStatus"));
        locationCol.setCellValueFactory(new PropertyValueFactory<Driver, String>("location"));
    
    }
    public void outputTableData() throws ParseException, ParseException{
        driversObservable.clear();
        driversObservable = FXCollections.observableArrayList(driversDatabase.getDrivers());
        driversTable.setItems(driversObservable);
        
    }

    @FXML
    private void clearSearchField(ActionEvent event) {
        
    }

    @FXML
    private void search(ActionEvent event) {
    }

    @FXML
    private void addDriver(ActionEvent event) throws ParseException {
         if (firstNameField.getText() == null || firstNameField.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in driver first name field!");
            alert.showAndWait();
            return;
        }
         if (lastNameField.getText() == null || lastNameField.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in driver last name field!");
            alert.showAndWait();
            return;
        }
         if (vertexChoiceBox.getSelectionModel().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please a Location!");
            alert.showAndWait();
            return;
        }
         
        boolean availability = availableRadio == availabilityGroup.getSelectedToggle()? true : false;
        driversDatabase.addDriver(vertexChoiceBox.getSelectionModel().getSelectedItem(), firstNameField.getText(), lastNameField.getText(), availability);
        clearTextFieldsForAddingCustomer();
        this.outputTableData();
    }
    @FXML
    private void editDriver(ActionEvent event) {
        Driver driver=driversTable.getSelectionModel().getSelectedItem();
        
        if (driver == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer from the table");
            alert.showAndWait();
        }else{
            firstNameField.setText(driver.getFirstName());
            lastNameField.setText(driver.getLastName());
            vertexChoiceBox.getSelectionModel().select(driver.getAddress());
            
            
            
        }
    }
    @FXML
    private void confirmEditDriver(ActionEvent event) throws ParseException {
        Driver driver=driversTable.getSelectionModel().getSelectedItem();
        if (driver == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a driver from the table");
            alert.showAndWait();
        }else{
                if (firstNameField.getText() == null || firstNameField.getText().trim().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill in driver first name field!");
                    alert.showAndWait();
                    return;
                }
                 if (lastNameField.getText() == null || lastNameField.getText().trim().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill in driver last name field!");
                    alert.showAndWait();
                    return;
                }
                
                boolean availability = (availabilityGroup.getSelectedToggle() == availableRadio) 
                        ? true : false;
                driversDatabase.editDriver(driver,vertexChoiceBox.getSelectionModel().getSelectedItem() , availability, firstNameField.getText(), 
                        lastNameField.getText());
                clearTextFieldsForAddingCustomer();
                this.outputTableData();
        }
        
    }
    /*
     * **************************************************
     * CLEAR THE TEXT FIELDS USED TO ADD A NEW CUSTOMER *
     * **************************************************
     */
    public void clearTextFieldsForAddingCustomer() {
        firstNameField.clear();
        lastNameField.clear();
        vertexChoiceBox.getSelectionModel().clearSelection();
        
        
    }
    @FXML
    private void deleteDriver(ActionEvent event) {
    }
    
}
