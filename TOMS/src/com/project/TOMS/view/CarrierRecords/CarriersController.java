/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.CarrierRecords;

import com.project.TOMS.controller.CarrierRecords.CarrierRegistryDatabase;
import com.project.TOMS.model.CarrierRecords.Carrier;
import com.project.TOMS.view.Authentication.LoginScreenController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class CarriersController implements Initializable {

    @FXML
    private TextField search;
    @FXML
    private ChoiceBox<?> searchOpt;
    @FXML
    private Button searchButton;
    @FXML
    private RadioButton admin;
    @FXML
    private RadioButton standard;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Carrier> carrierTable;
    @FXML
    private TableColumn<Carrier, String> idCol;
    @FXML
    private TableColumn<Carrier, String> companyCol;
    @FXML
    private TableColumn<Carrier, String> modeCol;
    @FXML
    private TableColumn<Carrier, String> roleCol;
    @FXML
    private RadioButton admin1;
    @FXML
    private RadioButton standard1;
    @FXML
    private RadioButton admin2;

    private CarrierRegistryDatabase carrierDatabase;
    private ObservableList<Carrier> carrierObservable;
    private Stage addCarrierStage;
    private Stage editCarrierStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carrierDatabase = CarrierRegistryDatabase.getInstance();
        this.setTable();
        carrierObservable = FXCollections.observableArrayList();
        this.outputTableData();
    }    
    private void setTable() {
        
        idCol.setCellValueFactory(new PropertyValueFactory<Carrier, String>("id"));
        companyCol.setCellValueFactory(new PropertyValueFactory<Carrier, String>("company"));
        modeCol.setCellValueFactory(new PropertyValueFactory<Carrier, String>("carrierMode"));
        roleCol.setCellValueFactory(new PropertyValueFactory<Carrier, String>("carrierRole"));
    
    }
    public void outputTableData(){
        carrierObservable.clear();
        carrierObservable = FXCollections.observableArrayList(carrierDatabase.getCarriers());
        carrierTable.setItems(carrierObservable);
        
    }

    @FXML
    private void handleSearch(ActionEvent event) {
    }

    @FXML
    private void addCarrier(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addCarrier.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        
        addCarrierController controller = fxmlLoader.<addCarrierController>getController();
        controller.setParentController(this);
        
        Scene scene = new Scene(root);
        addCarrierStage = new Stage();
        addCarrierStage.setScene(scene);
        addCarrierStage.centerOnScreen();
        addCarrierStage.setTitle("Add Carrier");
        addCarrierStage.initModality(Modality.WINDOW_MODAL);
        addCarrierStage.initOwner(LoginScreenController.stage);
        addCarrierStage.show();
    }

    @FXML
    private void editCarrier(ActionEvent event) throws IOException {
        Carrier carrier=carrierTable.getSelectionModel().getSelectedItem();
        
        if (carrier == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a carrier from the table");
            alert.showAndWait();
        }else{
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editCarrier.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            addCarrierController controller = fxmlLoader.<addCarrierController>getController();
            controller.setParentController(this);
            controller.setCarrier(carrier);
            controller.reinit();

            Scene scene = new Scene(root);
            editCarrierStage = new Stage();
            editCarrierStage.setScene(scene);
            editCarrierStage.centerOnScreen();
            editCarrierStage.setTitle("Edit Customer Order");
            editCarrierStage.initModality(Modality.WINDOW_MODAL);
            editCarrierStage.initOwner(LoginScreenController.stage);
            editCarrierStage.show();
        }
        
    }

    @FXML
    private void deleteCarrier(ActionEvent event) {
    }
    
}
