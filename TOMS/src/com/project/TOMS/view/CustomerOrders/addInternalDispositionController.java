/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.CustomerOrders;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.CarrierRecords.CarrierRegistryDatabase;
import com.project.TOMS.controller.CustomerOrders.CustomerOrderRegistryDatabase;
import com.project.TOMS.controller.Drivers.DriversDatabaseRegistry;
import com.project.TOMS.controller.Vehicles.TruckDatabaseRegistry;
import com.project.TOMS.controller.DispositionRecords.DispositionRegistryDatabase;
import com.project.TOMS.model.CarrierRecords.Carrier;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerOrders.Disposition;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.Vehicles.Trailer;
import com.project.TOMS.model.Vehicles.Truck;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class addInternalDispositionController implements Initializable {


    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    
    
    @FXML
    private ChoiceBox<Driver> driversChoiceBox;
    @FXML
    private Label driverIdLabel;
    @FXML
    private Label driverFirstNameLabel;
    @FXML
    private Label driverLastNameLabel;
    @FXML
    private Label driverAvailabilityStatusLabel;
    @FXML
    private Label driverCountryLabel;
    @FXML
    private Label driverCityLabel;
    @FXML
    private Label driverStreetLabel;
    @FXML
    private Label driverPostCodeLabel;
    
    
    
    @FXML
    private ChoiceBox<Truck> trucksChoiceBox;
    @FXML
    private Label truckIdLabel;
    @FXML
    private Label truckPlateNumberLabel;
    @FXML
    private Label truckAvailabilityStatusLabel;
    @FXML
    private Label truckConditionStatusLabel;
    @FXML
    private Label truckCountryLabel;
    @FXML
    private Label truckCityLabel;
    
    
    
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker finishDate;
    
    
    @FXML
    private Label carrierIdLabel;
    @FXML
    private Label carrierCompanyLabel;
    @FXML
    private Label carrierModeLabel;
    @FXML
    private Label carrierRoleLabel;
   
    @FXML
    private ComboBox<Vertex> sourceVertexChoiceBox;
    @FXML
    private ComboBox<Vertex> destinationVertexChoiceBox;
   
    private DispositionController parent;
    private CustomerOrdersController grandFather;
    private DriversDatabaseRegistry driversRegistry;
    private TruckDatabaseRegistry trucksRegistry;
    private DispositionRegistryDatabase dispositionRegistry;
    private CarrierRegistryDatabase carriersRegistry;
    private CustomerOrderRegistryDatabase ordersDatabase;
    private CustomerOrder order;
    private graphRegistryDatabase graphDatabase;
    ObservableList<Truck> truckObservable;
    ObservableList<Driver> driverObservable;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        driverObservable = FXCollections.observableArrayList();
        truckObservable = FXCollections.observableArrayList();
    }    
    public void reinit() throws ParseException{
        driversRegistry = DriversDatabaseRegistry.getInstance();
        graphDatabase = graphRegistryDatabase.getInstance();        
        trucksRegistry = TruckDatabaseRegistry.getInstance();
        dispositionRegistry = DispositionRegistryDatabase.getInstance();
        carriersRegistry = CarrierRegistryDatabase.getInstance();
        ordersDatabase = CustomerOrderRegistryDatabase.getInstance();
        
        this.setDatapickerProperties();
        this.setDatapickerDate();
        this.setDriversDropDownMenu();
        this.setTrucksDropDownMenu();
        this.setCarrierDetails();
        this.setSourceVertexChoiceBox();
        this.setDestinationVertexChoiceBox();
        this.setAddresses();
        this.setAvailableDrivers();
        this.setAvailableTrucks();
    }
    @FXML
    public void  datepickerAction(){
            this.setAvailableDrivers();
            this.setAvailableTrucks();
        
    }
    @FXML
    public void  sourceBoxAction(){
            this.setAvailableDrivers();
            this.setAvailableTrucks();
        
    }
    
    private void setAvailableDrivers(){
        String orderDate = startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String deliveryDate = finishDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date startDate = null;
        Date finishDate = null;
        try {
            startDate = format.parse(orderDate);
            finishDate = format.parse(deliveryDate);
        } catch (ParseException ex) {
            Logger.getLogger(addCustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<Driver> availableDriver = new ArrayList<Driver>();
        try {
            availableDriver = driversRegistry.getAvailableDrivers(startDate, finishDate, sourceVertexChoiceBox.getSelectionModel().getSelectedItem());
        } catch (ParseException ex) {
            Logger.getLogger(addCustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(availableDriver.isEmpty()){
             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Driver Availability");
             alert.setHeaderText(null);
             alert.setContentText("There are not any available drivers between "+orderDate+""
                     + " - "+deliveryDate+" in the location: "+
                     sourceVertexChoiceBox.getSelectionModel().getSelectedItem().getCity()+
                     ". Change the time frame of the order or change the source location of"
                             + " the order or navigate to the Drivers system module to add"
                             + " drivers.");
             alert.showAndWait();
             
             
        }
        driverObservable.clear();
        driverObservable = FXCollections.observableArrayList(availableDriver);
        driversChoiceBox.setItems(driverObservable);

        driversChoiceBox.getSelectionModel().selectFirst();
    }
    private void setAvailableTrucks(){
        String orderDate = startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String deliveryDate = finishDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date startDate = null;
        Date finishDate = null;
        try {
            startDate = format.parse(orderDate);
            finishDate = format.parse(deliveryDate);
        } catch (ParseException ex) {
            Logger.getLogger(addCustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ArrayList<Truck> availableTrucks = new ArrayList<Truck>();
        try {
            availableTrucks = trucksRegistry.getAvailableTrucks(startDate, finishDate, sourceVertexChoiceBox.getSelectionModel().getSelectedItem());
        } catch (ParseException ex) {
            Logger.getLogger(addCustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(availableTrucks.isEmpty()){
             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Trucks Availability");
             alert.setHeaderText(null);
             alert.setContentText("There are not any available trucks between "+orderDate+""
                     + " - "+deliveryDate+" in the location: "+
                     sourceVertexChoiceBox.getSelectionModel().getSelectedItem().getCity()+
                     ". Change the time frame of the disposition or change the source location of"
                             + " the order or navigate to the Trucks system module to add"
                             + " trucks.");
             alert.showAndWait();
             
             
        }
        truckObservable.clear();
        truckObservable = FXCollections.observableArrayList(availableTrucks);
        trucksChoiceBox.setItems(truckObservable);

        trucksChoiceBox.getSelectionModel().selectFirst();
    }
    
    private void setSourceVertexChoiceBox(){
       ObservableList<Vertex> verticesObservable = FXCollections.observableArrayList();
       verticesObservable = FXCollections.observableArrayList(graphDatabase.getGraph().getVertexes());
       sourceVertexChoiceBox.setItems(verticesObservable);
       sourceVertexChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vertex>() {

            @Override
            public void changed(ObservableValue<? extends Vertex> observable, Vertex oldValue, Vertex vertex) {

                sourceVertexChoiceBox.setValue(vertex);
                
                

            }
        });
       sourceVertexChoiceBox.getSelectionModel().selectFirst();
    }
    private void setDestinationVertexChoiceBox(){
       ObservableList<Vertex> verticesObservable = FXCollections.observableArrayList();
       verticesObservable = FXCollections.observableArrayList(graphDatabase.getGraph().getVertexes());
       destinationVertexChoiceBox.setItems(verticesObservable);
       destinationVertexChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vertex>() {

            @Override
            public void changed(ObservableValue<? extends Vertex> observable, Vertex oldValue, Vertex vertex) {

                destinationVertexChoiceBox.setValue(vertex);
                
                

            }
        });
       destinationVertexChoiceBox.getSelectionModel().selectFirst();
    }
    
    private void setDatapickerProperties(){
        
         final Callback<DatePicker, DateCell> startCellFactory = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                           
                            if (item.isBefore(LocalDate.now())){
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                            } else if(item.isAfter(finishDate.getValue())){
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                            }  
                    }
                };
            }
        };
         startDate.setDayCellFactory(startCellFactory);
         final Callback<DatePicker, DateCell> finishCellFactory = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                           
                            if (item.isBefore(startDate.getValue())){
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                            }   
                    }
                };
            }
        };
        finishDate.setDayCellFactory(finishCellFactory);
    }
    private void setAddresses() throws ParseException{
        ArrayList<Disposition> dispositions = dispositionRegistry.getDispositionsOfOrder(order.getId());
        Vertex orderStartAddress = order.getOriginAddress();
        Vertex orderFinishAddress = order.getDestinationAddress();
        
        Disposition last = dispositions.get(dispositions.size()-1);
        Vertex lastDispositionFinish = last.getDestinationAddress();


        if(lastDispositionFinish == orderFinishAddress){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText("It seems that the trip has been already planned");
            alert.setContentText("But if you feel customising further the route go ahead!");
            alert.showAndWait();
        }


        sourceVertexChoiceBox.getSelectionModel().select(lastDispositionFinish);

       // this.disableOriginTextFields();
            
    }
    
    private void disableOriginTextFields(){
       //sourceVertexChoiceBox.getSelectionModel().
    }
    private void setCarrierDetails(){
        Carrier carrier = carriersRegistry.getInternalCarrier();
        carrierIdLabel.setText(carrier.getId()+"");
        carrierCompanyLabel.setText(carrier.getCompany());
        carrierModeLabel.setText(carrier.getCarrierMode());
        carrierRoleLabel.setText(carrier.getCarrierRole());
    }
    private void setDatapickerDate() throws ParseException{
        startDate.setEditable(false);
        finishDate.setEditable(false);
        startDate.setValue(this.convertLocalDate(order.getOrderDate()));
        LocalDate date = startDate.getValue();
        finishDate.setValue(this.convertLocalDate(order.getDeliveryDate()));
    }
    
    private LocalDate convertLocalDate(String dat) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(dat);
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    private Date convertDate(String dat) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(dat);
        return date;
    }
    private void setDriversDropDownMenu() throws ParseException{
       
       driversChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Driver>() {

            @Override
            public void changed(ObservableValue<? extends Driver> observable, Driver oldValue, Driver driver) {

                driversChoiceBox.setValue(driver);
                driverIdLabel.setText(driver.getId()+"");
                driverFirstNameLabel.setText(driver.getFirstName());
                driverLastNameLabel.setText(driver.getLastName());
                driverAvailabilityStatusLabel.setText(driver.getAvailabilityStatus()==true?"Available":"Not Available");
                driverCountryLabel.setText(driver.getAddress().getCountry());
                driverCityLabel.setText(driver.getAddress().getCity());
                

            }
        });
       driversChoiceBox.getSelectionModel().selectFirst();
        
    }
    private void setTrucksDropDownMenu() throws ParseException{
      
       trucksChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Truck>() {

            @Override
            public void changed(ObservableValue<? extends Truck> observable, Truck oldValue, Truck truck) {

                trucksChoiceBox.setValue(truck);
                truckIdLabel.setText(truck.getId()+"");
                truckPlateNumberLabel.setText(truck.getPlateNumber());
                truckAvailabilityStatusLabel.setText(truck.getAvailabilityStatus() == true ? "Available" : "Unavailable");
                truckConditionStatusLabel.setText(truck.getConditionStatus() == true ? "Damaged" : "Not Damaged");
                truckCountryLabel.setText(truck.getAddress().getCountry());
                truckCityLabel.setText(truck.getAddress().getCity());
                

            }
        });
       trucksChoiceBox.getSelectionModel().selectFirst();
    }
    @FXML
    private void addInternalDisposition(ActionEvent event) throws ParseException {
        

        
        
        String orderDate = startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String deliveryDate = finishDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        dispositionRegistry.addInternalDisposition(sourceVertexChoiceBox.getSelectionModel().getSelectedItem(),
                destinationVertexChoiceBox.getSelectionModel().getSelectedItem(),
                carriersRegistry.getInternalCarrier(), 
                orderDate, deliveryDate, 0, order, 
                trucksChoiceBox.getSelectionModel().getSelectedItem(), 
                driversChoiceBox.getSelectionModel().getSelectedItem());
        this.parent.outputTableData();
        this.grandFather.outputTableData();
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
        
    }
    

   @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
     private void cancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    public void setParentController(DispositionController parent){
        this.parent=parent;
    }
    public void setGrandFatherController(CustomerOrdersController grandFather){
        this.grandFather = grandFather;
    }
    private LocalDate NOW_LOCALDATE(){
        return LocalDate.now();
    }
    public void setOrder(CustomerOrder order){
        this.order=order;
    }

}
