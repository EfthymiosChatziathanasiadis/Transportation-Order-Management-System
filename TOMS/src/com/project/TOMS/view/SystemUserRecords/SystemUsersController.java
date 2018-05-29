/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.SystemUserRecords;

import com.project.TOMS.controller.Drivers.DriversDatabaseRegistry;
import com.project.TOMS.controller.systemUserRecords.SystemUserRegistryDatabase;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.systemUserRecords.SystemUser;
import com.project.TOMS.view.Authentication.LoginScreenController;
import static com.project.TOMS.view.CustomerOrders.CustomerOrdersController.dispositionStage;
import com.project.TOMS.view.CustomerOrders.DispositionController;
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
public class SystemUsersController implements Initializable {

    @FXML
    private TableView<SystemUser> userTable;
    @FXML
    private TableColumn<SystemUser, String> userIDCol;
    @FXML
    private TableColumn<SystemUser, String> nameCol;
    @FXML
    private TableColumn<SystemUser, String> surnameCol;
    @FXML
    private TableColumn<SystemUser, String> passwordCol;
    @FXML
    private TableColumn<SystemUser, String> isAdminCol;
    @FXML
    private TextField search;
    @FXML
    private ChoiceBox<?> searchOpt;
    @FXML
    private Button searchButton;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField password;
    @FXML
    private RadioButton admin;
    @FXML
    private RadioButton standard;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    
    private SystemUserRegistryDatabase usersDatabase;
    private ObservableList<SystemUser> usersObservable;
    private Stage stationStage;
    private Stage edgeStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usersDatabase = SystemUserRegistryDatabase.getInstance();
        this.setTable();
        usersObservable = FXCollections.observableArrayList();
        this.outputTableData();
    }    
    private void setTable() {
        
        userIDCol.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("surname"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("password"));
        isAdminCol.setCellValueFactory(new PropertyValueFactory<SystemUser, String>("isAdmin"));
    
    }
    public void outputTableData(){
        usersObservable.clear();
        usersObservable = FXCollections.observableArrayList(usersDatabase.getUsers());
        userTable.setItems(usersObservable);
        
    }
    @FXML
    private void handleSearch(ActionEvent event) {
    }

    @FXML
    private void addSystemUser(ActionEvent event) {
    }

    @FXML
    private void editUser(ActionEvent event) {
    }

    @FXML
    private void deleteSystemUser(ActionEvent event) {
    }
    
    @FXML
    private void viewVertices(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/TOMS/view/SystemUserRecords/DispositionStations.fxml"));
        Parent root = (Parent) fxmlLoader.load();


        Scene scene = new Scene(root);
        stationStage = new Stage();
        stationStage.setScene(scene);
        stationStage.centerOnScreen();
        stationStage.setTitle("Stations - Vertices");
        stationStage.initModality(Modality.WINDOW_MODAL);
        stationStage.initOwner(LoginScreenController.stage);
        stationStage.show();
    }
    @FXML
    private void viewEdges(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/TOMS/view/SystemUserRecords/DispositionsEdges.fxml"));
        Parent root = (Parent) fxmlLoader.load();


        Scene scene = new Scene(root);
        edgeStage = new Stage();
        edgeStage.setScene(scene);
        edgeStage.centerOnScreen();
        edgeStage.setTitle("Paths - Edges");
        edgeStage.initModality(Modality.WINDOW_MODAL);
        edgeStage.initOwner(LoginScreenController.stage);
        edgeStage.show();
    }
    
}
