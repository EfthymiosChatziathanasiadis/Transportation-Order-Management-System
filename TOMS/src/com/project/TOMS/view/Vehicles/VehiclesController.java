/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.Vehicles;

import com.project.TOMS.controller.CustomerOrders.CustomerOrderRegistryDatabase;
import com.project.TOMS.controller.Vehicles.TrailerDatabaseRegistry;
import com.project.TOMS.controller.Vehicles.TruckDatabaseRegistry;
import com.project.TOMS.model.Vehicles.Trailer;
import com.project.TOMS.model.Vehicles.Truck;
import com.project.TOMS.view.Authentication.LoginScreenController;
import static com.project.TOMS.view.CustomerOrders.CustomerOrdersController.mapStage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class VehiclesController implements Initializable {
    @FXML
    private TableView<Trailer> trailerTable;
    
    @FXML
    private TableColumn<Trailer, String> colTrailerId;
    @FXML
    private TableColumn<Trailer, String> colTrailerPlate;
    @FXML
    private TableColumn<Trailer, String> colTrailerCondition;
    @FXML
    private TableColumn<Trailer, String> colTrailerAvailability;
    @FXML
    private TableColumn<Trailer, String> colTrailerLocation;
    
    @FXML
    private TableView<Truck> truckTable;
    
    @FXML
    private TableColumn<Truck, String> colTruckId;
    @FXML
    private TableColumn<Truck, String> colTruckPlate;
    @FXML
    private TableColumn<Truck, String> colTruckCondition;
    @FXML
    private TableColumn<Truck, String> colTruckAvailability;
    @FXML
    private TableColumn<Truck, String> colTruckLocation;
    

    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<?> searchOptions;
    @FXML
    private Button searchButton;
    
    private TrailerDatabaseRegistry trailerDatabase;
    private TruckDatabaseRegistry truckDatabase;
    private CustomerOrderRegistryDatabase orderDatabase;
    private ObservableList<Trailer> trailersObservable;
    private ObservableList<Truck> truckObservable;
    private Stage addTruckStage;
    private Stage addTrailerStage;
    private Stage editTruckStage;
    private Stage editTrailerStage;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        orderDatabase = CustomerOrderRegistryDatabase.getInstance();
        try {
            orderDatabase.configureAvailability();
        } catch (ParseException ex) {
        }
        trailerDatabase = TrailerDatabaseRegistry.getInstance();
        truckDatabase =  TruckDatabaseRegistry.getInstance();
        this.setTrailerTable();
        this.setTruckTable();
        trailersObservable = FXCollections.observableArrayList();
        truckObservable = FXCollections.observableArrayList();
        try {
            this.outputTrailerTable();
        } catch (ParseException ex) {
            Logger.getLogger(VehiclesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.outputTruckTable();
        } catch (ParseException ex) {
            Logger.getLogger(VehiclesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
     private void setTrailerTable() {
        
        colTrailerId.setCellValueFactory(new PropertyValueFactory<Trailer, String>("id"));
        colTrailerPlate.setCellValueFactory(new PropertyValueFactory<Trailer, String>("plateNumber"));
        colTrailerCondition.setCellValueFactory(new PropertyValueFactory<Trailer, String>("condition"));
        colTrailerAvailability.setCellValueFactory(new PropertyValueFactory<Trailer, String>("availability"));
        colTrailerLocation.setCellValueFactory(new PropertyValueFactory<Trailer, String>("location"));
    
    }
    
     private void setTruckTable() {
        
        colTruckId.setCellValueFactory(new PropertyValueFactory<Truck, String>("id"));
        colTruckPlate.setCellValueFactory(new PropertyValueFactory<Truck, String>("plateNumber"));
        colTruckCondition.setCellValueFactory(new PropertyValueFactory<Truck, String>("condition"));
        colTruckAvailability.setCellValueFactory(new PropertyValueFactory<Truck, String>("availability"));
        colTruckLocation.setCellValueFactory(new PropertyValueFactory<Truck, String>("location"));
        
    }
    public void outputTrailerTable() throws ParseException, ParseException{
        trailersObservable.clear();
        try {
            orderDatabase.configureAvailability();
        } catch (ParseException ex) {
        }
        trailersObservable = FXCollections.observableArrayList(trailerDatabase.getTrailers());
        trailerTable.setItems(trailersObservable);
        
    }
    public void outputTruckTable() throws ParseException{
        truckObservable.clear();
        try {
            orderDatabase.configureAvailability();
        } catch (ParseException ex) {
        }
        truckObservable = FXCollections.observableArrayList(truckDatabase.getTrucks());
        truckTable.setItems(truckObservable);
    } 
    @FXML
    private void search(ActionEvent event) {
    }

    @FXML
    private void addTruck(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addTruck.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        
        addTruckController controller = fxmlLoader.<addTruckController>getController();
        controller.setParentController(this);
        
        Scene scene = new Scene(root);
        addTruckStage = new Stage();
        addTruckStage.setScene(scene);
        addTruckStage.centerOnScreen();
        addTruckStage.setTitle("Add Truck");
        addTruckStage.initModality(Modality.WINDOW_MODAL);
        addTruckStage.initOwner(LoginScreenController.stage);
        addTruckStage.show();
        
    }

    @FXML
    private void editTruck(ActionEvent event) throws IOException {
        Truck truck=truckTable.getSelectionModel().getSelectedItem();
        
        if (truck == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a truck from the Truck Table!!");
            alert.showAndWait();
        }else{
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editTruck.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            editTruckController controller = fxmlLoader.<editTruckController>getController();
            controller.setParentController(this);
            controller.setTruck(truck);
            controller.reinit();

            Scene scene = new Scene(root);
            editTruckStage = new Stage();
            editTruckStage.setScene(scene);
            editTruckStage.centerOnScreen();
            editTruckStage.setTitle("Edit Truck");
            editTruckStage.initModality(Modality.WINDOW_MODAL);
            editTruckStage.initOwner(LoginScreenController.stage);
            editTruckStage.show();
        }
    }

    @FXML
    private void deleteTruck(ActionEvent event) {
    }
    @FXML
    private void addTrailer(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addTrailer.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        
        addTrailerController controller = fxmlLoader.<addTrailerController>getController();
        controller.setParentController(this);
        
        Scene scene = new Scene(root);
        addTrailerStage = new Stage();
        addTrailerStage.setScene(scene);
        addTrailerStage.centerOnScreen();
        addTrailerStage.setTitle("Add Trailer");
        addTrailerStage.initModality(Modality.WINDOW_MODAL);
        addTrailerStage.initOwner(LoginScreenController.stage);
        addTrailerStage.show();
        
        
    }

    @FXML
    private void editTrailer(ActionEvent event) throws IOException {
        Trailer trailer=trailerTable.getSelectionModel().getSelectedItem();
        
        if (trailer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a trailer from the Trailer Table!!");
            alert.showAndWait();
        }else{
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editTrailer.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            editTrailerController controller = fxmlLoader.<editTrailerController>getController();
            controller.setParentController(this);
            controller.setTrailer(trailer);
            controller.reinit();

            Scene scene = new Scene(root);
            editTrailerStage = new Stage();
            editTrailerStage.setScene(scene);
            editTrailerStage.centerOnScreen();
            editTrailerStage.setTitle("Edit Trailer");
            editTrailerStage.initModality(Modality.WINDOW_MODAL);
            editTrailerStage.initOwner(LoginScreenController.stage);
            editTrailerStage.show();
        }
    }

    @FXML
    private void deleteTrailer(ActionEvent event) {
    }
    @FXML
    private void viewTrailerLocationMap(ActionEvent event)throws ClassNotFoundException, SQLException, IOException, ParseException  {
            ArrayList<Trailer> trailers = trailerDatabase.getTrailers();
            if(trailers.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Trailer Fleet Information");
                alert.setHeaderText("Trailer Fleet");
                alert.setContentText("There are not any trailers to display!");
                alert.showAndWait();
            }else{
            
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("trailerMap.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                TrailerMapController controller = fxmlLoader.<TrailerMapController>getController();
                controller.trailerReinit();

                Scene scene = new Scene(root);
                mapStage = new Stage();
                mapStage.setScene(scene);
                mapStage.centerOnScreen();
                mapStage.setTitle("Trailer Locations");
                mapStage.initModality(Modality.WINDOW_MODAL);
                mapStage.initOwner(LoginScreenController.stage);
                mapStage.show();
            }
    }
    @FXML
    private void viewTruckLocationMap(ActionEvent event)throws ClassNotFoundException, SQLException, IOException, ParseException  {
        
            ArrayList<Truck> trucks = truckDatabase.getTrucks();
            if(trucks.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Truck Fleet Information");
                alert.setHeaderText("Truck Fleet");
                alert.setContentText("There are not any trucks to display!");
                alert.showAndWait();
            }else{
            
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("trailerMap.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                TrailerMapController controller = fxmlLoader.<TrailerMapController>getController();
                controller.truckReinit();

                Scene scene = new Scene(root);
                mapStage = new Stage();
                mapStage.setScene(scene);
                mapStage.centerOnScreen();
                mapStage.setTitle("Truck Locations");
                mapStage.initModality(Modality.WINDOW_MODAL);
                mapStage.initOwner(LoginScreenController.stage);
                mapStage.show();
            }
    }
}

