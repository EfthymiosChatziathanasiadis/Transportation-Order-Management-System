/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.CarrierRecords;

import com.project.TOMS.controller.CarrierRecords.CarrierRegistryDatabase;
import com.project.TOMS.model.CarrierRecords.Carrier;
import com.project.TOMS.model.CarrierRecords.CarrierRole;
import com.project.TOMS.model.CarrierRecords.External;
import com.project.TOMS.model.CarrierRecords.Internal;
import com.project.TOMS.model.CarrierRecords.Mode;
import com.project.TOMS.model.CarrierRecords.Rail;
import com.project.TOMS.model.CarrierRecords.Road;
import com.project.TOMS.model.CarrierRecords.Sea;
import com.project.TOMS.view.CustomerOrders.CustomerOrdersController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class addCarrierController implements Initializable {

    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private RadioButton roadRadio;
    @FXML
    private RadioButton railRadio;
    @FXML
    private RadioButton seaRadio;
    @FXML
    private RadioButton internalRadio;
    @FXML
    private RadioButton externalRadio;
    @FXML
    private TextField carrierCompanyField;
    @FXML
    private Label idLabel;
    private ToggleGroup radioGroupMode;
    private ToggleGroup radioGroupCarrierType;
    private CarriersController parent;
    private Carrier carrier;

    private CarrierRegistryDatabase carrierDatabase;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carrierDatabase = CarrierRegistryDatabase.getInstance(); 
        this.setToggle();
    } 
    public void reinit(){
        this.setCurrentCarrierDetails();
    }
    private void setCurrentCarrierDetails(){
        Mode mode = carrier.getMode();
        CarrierRole role = carrier.getRole();
        int id = carrier.getId();
        String company = carrier.getCompany(); 
        
        if(mode instanceof Road){
            radioGroupMode.selectToggle(roadRadio);
        }else if(mode instanceof Rail){
            radioGroupMode.selectToggle(railRadio);
        }else{
            radioGroupMode.selectToggle(seaRadio);
           
        }
        
        carrierCompanyField.setText(company);
        
        if(mode instanceof Internal){
            radioGroupCarrierType.selectToggle(internalRadio);
        }else{
            radioGroupCarrierType.selectToggle(externalRadio);
        }
        
        idLabel.setText(id +"");
        
        
    }
    private void setToggle(){
        radioGroupMode=new ToggleGroup();
        roadRadio.setToggleGroup(radioGroupMode);
        railRadio.setToggleGroup(radioGroupMode);
        seaRadio.setToggleGroup(radioGroupMode);
        radioGroupMode.selectToggle(roadRadio);
        
        radioGroupCarrierType=new ToggleGroup();
        internalRadio.setToggleGroup(radioGroupCarrierType);
        externalRadio.setToggleGroup(radioGroupCarrierType);
        radioGroupCarrierType.selectToggle(internalRadio);
    }
    @FXML
    private void addEntry(ActionEvent event) {
        if (carrierCompanyField.getText() == null || carrierCompanyField.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in carrier company details!");
            alert.showAndWait();
            return;
        }
        Mode carrierMode;  
        
        if(radioGroupMode.getSelectedToggle() == roadRadio){
            carrierMode = new Road();
        }else if(radioGroupMode.getSelectedToggle() == railRadio){
            carrierMode = new Rail();
        }else{
            carrierMode = new Sea();
        }
        CarrierRole role = (radioGroupCarrierType.getSelectedToggle() == internalRadio) ? (new Internal()) : (new External());

        carrierDatabase.addCarrier(carrierCompanyField.getText(), carrierMode, role);
        this.parent.outputTableData();
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
        
    }
    @FXML
    private void editEntry(ActionEvent event) {
        if (carrierCompanyField.getText() == null || carrierCompanyField.getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in carrier company details!");
            alert.showAndWait();
            return;
        }
        Mode carrierMode;  
        
        if(radioGroupMode.getSelectedToggle() == roadRadio){
            carrierMode = new Road();
        }else if(radioGroupMode.getSelectedToggle() == railRadio){
            carrierMode = new Rail();
        }else{
            carrierMode = new Sea();
        }
        CarrierRole role = (radioGroupCarrierType.getSelectedToggle() == internalRadio) ? (new Internal()) : (new External());
        carrierDatabase.editCarrier(carrier, carrierCompanyField.getText(), carrierMode, role);
        this.parent.outputTableData();
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
        
    }
    

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    public void setParentController(CarriersController parent){
        this.parent=parent;
    }
    public void setCarrier(Carrier carrier){
        this.carrier=carrier;
    }
    
}
