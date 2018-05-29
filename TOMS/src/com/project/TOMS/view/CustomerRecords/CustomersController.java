/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.CustomerRecords;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.CustomerRecords.CustomerRegistryDatabase;
import com.project.TOMS.controller.Drivers.DriversDatabaseRegistry;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerRecords.Customer;
import com.project.TOMS.model.Drivers.Driver;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class CustomersController implements Initializable {

    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, String> idCol;
    @FXML
    private TableColumn<Customer, String> companyCol;
    @FXML
    private TableColumn<Customer, String> phoneCol;
    @FXML
    private TableColumn<Customer, String> countryCol;
    @FXML
    private TableColumn<Customer, String> cityCol;
    
   
    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<?> searchOptions;
    
    @FXML
    private TextField companyField;
   
    @FXML
    private TextField phoneField;
    
   
   
    
     //Inject FX Labels
    @FXML
    private Label fieldLabel1;
    @FXML
    private Label fieldLabel2;
    @FXML
    private Label fieldLabel3;
    
    @FXML
    private ChoiceBox<Vertex> vertexChoiceBox;
    
    
    
    
    private CustomerRegistryDatabase customersDatabase;
    private ObservableList<Customer> customersObservable;
    private graphRegistryDatabase graphDatabase;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customersDatabase = CustomerRegistryDatabase.getInstance();
        graphDatabase = graphRegistryDatabase.getInstance();

        this.setTable();
        customersObservable = FXCollections.observableArrayList();
        this.outputTableData();
        this.setSourceVertexChoiceBox();
    }  
    private void setSourceVertexChoiceBox(){
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
     private void setTable() {
        
        idCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("id"));
        companyCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("company"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        countryCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        cityCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));

    }
    public void outputTableData(){
        customersObservable.clear();
        customersObservable = FXCollections.observableArrayList(customersDatabase.getCustomers());
        customersTable.setItems(customersObservable);
        
    }

    @FXML
    private void clearSearchField(ActionEvent event) {
    }
    
    

    @FXML
    private void search(ActionEvent event) {
    }

    @FXML
    private void addCustomer(ActionEvent event) {
        
        boolean passCheck = checkTextFieldsAreFilledBeforeAdding();
        if (!passCheck) {
            return;
        }
        boolean passCheck2 = checkTextFieldsAreValidBeforeAdding();
        if (!passCheck2) {
            return;
        }
        String company = companyField.getText();
        String phone = phoneField.getText();
        
        
        customersDatabase.addCustomer(vertexChoiceBox.getSelectionModel().getSelectedItem(), company, phone);
        clearTextFieldsForAddingCustomer();
        hideRequiredFieldLabels();
        this.outputTableData();
    }

    @FXML
    private void editCustomer(ActionEvent event) {
        Customer customer=customersTable.getSelectionModel().getSelectedItem();
        
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer from the table");
            alert.showAndWait();
        }else{
            companyField.setText(customer.getCompany());
            phoneField.setText(customer.getPhone());
            vertexChoiceBox.getSelectionModel().select(customer.getAddress());
            
            
            
        }
    }
    
    @FXML
    private void confirmEdit(ActionEvent event){
        Customer customer=customersTable.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer from the table");
            alert.showAndWait();
        }else{
                boolean passCheck = checkTextFieldsAreFilledBeforeAdding();
                if (!passCheck) {
                    return;
                }
                boolean passCheck2 = checkTextFieldsAreValidBeforeAdding();
                if (!passCheck2) {
                    return;
                }
                String company = companyField.getText();
                String phone = phoneField.getText();
                
                

                customersDatabase.editCustomer(customer, vertexChoiceBox.getSelectionModel().getSelectedItem(), 
                        company, phone);
                clearTextFieldsForAddingCustomer();
                hideRequiredFieldLabels();
                this.outputTableData();
            
        }
    }

    @FXML
    private void deleteCustomer(ActionEvent event) {
        Customer customer=customersTable.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer from the table");
            alert.showAndWait();
        }else{
            customersDatabase.delete(customer);
            this.outputTableData();
        }
    }
    
    // ensure there are no empty text fields when adding a new customer
    public boolean checkTextFieldsAreFilledBeforeAdding() {
        

        boolean atLeastOneFieldEmpty = false;
        hideRequiredFieldLabels();

        if (companyField.getText() == null || companyField.getText().trim().isEmpty()) {
            fieldLabel1.setText("The company field is required.");
            fieldLabel1.setVisible(true);
            atLeastOneFieldEmpty = true;
        }
        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            fieldLabel2.setText("The phone field is required.");
            fieldLabel2.setVisible(true);
            atLeastOneFieldEmpty = true;
        }
        if (vertexChoiceBox.getSelectionModel().isEmpty()) {
            fieldLabel3.setText("The location selection is required.");
            fieldLabel3.setVisible(true);
            atLeastOneFieldEmpty = true;
        }
       
        

        if (atLeastOneFieldEmpty) {
            showInformationBox("Add Customer", "Ensure all required fields are filled in before attempting to add a new customer.");
            return false;
        }
        return true;
    }
    
    
    
     /*
     * *****************************************
     * CHECK THE TEXT FIELDS HAVE VALID INPUTS *
     * *****************************************
     */
    private boolean checkTextFieldsAreValidBeforeAdding() {
        String company = companyField.getText();
        String phone = phoneField.getText();
        
        

        String regex_letttersOnly = "[A-Za-z']+";
        String regex_numbersOnly = "[0-9]+";

        String validName = "Please enter only letters for this field";
        String validNumber = "Please enter only numbers for this field";

        boolean atLeastOneInvalidField = false;

        if (!company.matches(regex_letttersOnly)) {
            fieldLabel1.setText(validName);
            fieldLabel1.setVisible(true);
            atLeastOneInvalidField = true;
        }
        if (!phone.matches(regex_numbersOnly)) {
            fieldLabel2.setText(validNumber);
            fieldLabel2.setVisible(true);
            atLeastOneInvalidField = true;
        }
        
       
         
        

        if (atLeastOneInvalidField) {
            showInformationBox("Add Customer", "Ensure all required fields have valid inputs before attempting to add a new customer.");
            return false;
        }
        return true;
    }

    
    
    
    /*
     * *******************************************
     * USED TO DISPLAY A DIALOG BOX WITH A GIVEN *
     * STRING AS THE MESSAGE DISPLAYED           *
    **********************************************
     */
    public void showInformationBox(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

     /*
     * ***************************************************
     * HIDE THE LABELS USED TO INDICATE REQUIRED FIELDS *
     * ***************************************************
     */
    public void hideRequiredFieldLabels() {
        ArrayList<Label> labels = new ArrayList<>();
        labels.add(fieldLabel1);
        labels.add(fieldLabel2);
        labels.add(fieldLabel3);
        
        for (Label l : labels) {
            l.setVisible(false);
        }

    }
    /*
     * **************************************************
     * CLEAR THE TEXT FIELDS USED TO ADD A NEW CUSTOMER *
     * **************************************************
     */
    public void clearTextFieldsForAddingCustomer() {
        companyField.clear();
        phoneField.clear();
        vertexChoiceBox.getSelectionModel().clearSelection();
        
        
        
    }

    
}
