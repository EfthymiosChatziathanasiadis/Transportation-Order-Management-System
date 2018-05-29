/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.CustomerOrders;

import com.project.TOMS.controller.CustomerRecords.CustomerRegistryDatabase;
import com.project.TOMS.controller.Vehicles.TrailerDatabaseRegistry;
import com.project.TOMS.controller.systemUserRecords.SystemUserRegistryDatabase;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerRecords.Customer;
import com.project.TOMS.model.Vehicles.Trailer;
import com.project.TOMS.model.systemUserRecords.SystemUser;
import java.net.URL;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class customerOrderDetailsController implements Initializable {

   
    @FXML
    private Button cancelButton;
    @FXML 
    private ToggleGroup radioGroup;
    @FXML
    private RadioButton radioImport;
    @FXML
    private RadioButton radioExport;
    @FXML
    private Label customerIdLablel;
    @FXML
    private Label customerCompanyLabel;
    @FXML
    private Label customerTelephoneLabel;
    @FXML
    private Label customerCountry;
    @FXML
    private Label customerCity;
    @FXML
    private Label customerStreet;
    @FXML
    private Label customerPostCode;
    @FXML
    private Label startDate;
    @FXML
    private Label trailerId;
    @FXML
    private Label trailerPlateNumber;
    @FXML
    private Label trailerAvailabilityStatus;
    @FXML
    private Label trailerConditionStatus;
    @FXML
    private Label trailerCountry;
    @FXML
    private Label trailerCity;
    @FXML
    private Label trailerStreet;
    @FXML
    private Label trailerPostCode;
    @FXML
    private Label finishDate;
    @FXML
    private Label disponentId;
    @FXML
    private Label disponentName;
    @FXML
    private Label disponentSurname;
    @FXML
    private Label idLable;
    @FXML
    private Label originCountry;
    @FXML
    private Label originStreet;
    @FXML
    private Label originCity;
    @FXML
    private Label originPostCode;
    @FXML
    private Label destinationCountry;
    @FXML
    private Label destinationCity;
    @FXML
    private Label destinationStreet;
    @FXML
    private Label destinationPostCode;
    @FXML
    private Label massField;
    
    
    private SystemUser user;
    private CustomerOrder order;

    
    private CustomerRegistryDatabase customerRegistry;
    private TrailerDatabaseRegistry trailerRegistry;
    private SystemUserRegistryDatabase systemUserRegistry;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    public void reinit(){
        customerRegistry = CustomerRegistryDatabase.getInstance();
        trailerRegistry = TrailerDatabaseRegistry.getInstance();
        systemUserRegistry = SystemUserRegistryDatabase.getInstance();
        this.setDisponentLabels();
        this.setOrderTypeToggle();
        this.setCustomerDetails();
        this.setTrailerDetails();
        this.setOrderType();
        this.orderSetDate();
        this.setAddresses();
        this.setDisponentDetails();
        this.setMass();
        this.setID();
        
    }
    
    private void setID(){
        this.idLable.setText(order.getId()+"");
    }
    private void setMass(){
        massField.setText(order.getQuantity()+"");
    }
    private void setDisponentDetails(){
        disponentId.setText(order.getDisponent().getId()+"");
        disponentName.setText(order.getDisponent().getName());
        disponentSurname.setText(order.getDisponent().getSurname());
    }
    private void setAddresses(){
        originCountry.setText(order.getOriginAddress().getCountry());
        originCity.setText(order.getOriginAddress().getCity());
        
        
        destinationCountry.setText(order.getDestinationAddress().getCountry());
        destinationCity.setText(order.getDestinationAddress().getCity());
        
        
    }
    //https://stackoverflow.com/questions/8746084/string-to-localdate
    private void orderSetDate(){
        startDate.setText(order.getOrderDate());
        finishDate.setText(order.getDeliveryDate());
    }
    private void setOrderType(){
        
        radioGroup.selectToggle((order.getType().equals("Import") ? radioImport : radioExport));
    }
    private void setCustomerDetails(){
        Customer customer = order.getCustomer();
        customerIdLablel.setText(customer.getId()+"");
        customerCompanyLabel.setText(customer.getCompany());
        customerTelephoneLabel.setText(customer.getPhone());
        customerCountry.setText(customer.getAddress().getCountry());
        customerCity.setText(customer.getAddress().getCity());
         
    }
    private void setTrailerDetails(){
        Trailer trailer = order.getTrailer();
        trailerId.setText(trailer.getId()+"");
        trailerPlateNumber.setText(trailer.getPlateNumber()); 
    }
    
    private void setOrderTypeToggle(){
        radioGroup=new ToggleGroup();
        radioImport.setToggleGroup(radioGroup);
        radioExport.setToggleGroup(radioGroup);
        radioGroup.selectToggle(radioImport);
    }
    private void setDisponentLabels(){
        user = systemUserRegistry.getCusrrentUser();
        disponentId.setText(user.getId()+"");
        disponentName.setText(user.getName());
        disponentSurname.setText(user.getSurname());
    }
    
    

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    
   
    public void setCustomerOrder(CustomerOrder order){
        this.order = order;
    }
}
