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
import com.project.TOMS.controller.DispositionRecords.DispositionRegistryDatabase;
import com.project.TOMS.controller.Drivers.DriversDatabaseRegistry;
import com.project.TOMS.controller.Vehicles.TruckDatabaseRegistry;
import com.project.TOMS.model.CarrierRecords.Carrier;
import com.project.TOMS.model.CarrierRecords.External;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerOrders.Disposition;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.Vehicles.Truck;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class editDispositionController implements Initializable {
    
    @FXML
    private Tab internalTab;
    @FXML
    private Tab externalTab;
    @FXML
    private TabPane pane;
    @FXML
    private ComboBox<Truck> trucksChoiceBox;
    @FXML
    private ComboBox<Driver> driversChoiceBox;
    @FXML
    private ComboBox<Carrier> carrierChoiceBox;
    @FXML
    private Label carrierIdLablel;
    @FXML
    private Label carrierCompanyLabel;
    @FXML
    private Label carrierModeLabel;
    @FXML
    private Label carrierRoleLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label driverIdLabel;
    @FXML
    private Label driverFirstNameLabel;
    @FXML
    private Label driverLastNameLabel;
    @FXML
    private Label truckIdLabel;
    @FXML
    private Label truckPlateNumberLabel;
    @FXML
    private Label truckAvailabilityStatusLabel;
    @FXML
    private Label truckConditionStatusLabel;
    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker finishDate;
        
    @FXML
    private Label driverAvailabilityStatusLabel;
    @FXML
    private ComboBox<Vertex> sourceVertexChoiceBox;
    @FXML
    private ComboBox<Vertex> destinationVertexChoiceBox;

    /**
     * Initializes the controller class.
     */
    private DispositionController parent;
    private CustomerOrdersController grandFather;
    private DriversDatabaseRegistry driversRegistry;
    private TruckDatabaseRegistry trucksRegistry;
    private DispositionRegistryDatabase dispositionRegistry;
    private CarrierRegistryDatabase carriersRegistry;
    private CustomerOrder order;
    private Disposition disposition;
    private CustomerOrderRegistryDatabase ordersDatabase;
    private graphRegistryDatabase graphDatabase;
    private ObservableList<Truck> truckObservable;
    private ObservableList<Driver> driverObservable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void reinit() throws ParseException{
        ordersDatabase = CustomerOrderRegistryDatabase.getInstance();
        driversRegistry = DriversDatabaseRegistry.getInstance();
        trucksRegistry = TruckDatabaseRegistry.getInstance();
        dispositionRegistry = DispositionRegistryDatabase.getInstance();
        carriersRegistry = CarrierRegistryDatabase.getInstance();
        graphDatabase = graphRegistryDatabase.getInstance();
        truckObservable = FXCollections.observableArrayList();
        driverObservable = FXCollections.observableArrayList();
        
        this.setTrucksDropDownMenu();
        this.setDriversDropDownMenu();
        this.setCarrierDropDownMenu();
        this.setSourceVertexChoiceBox();
        this.setDestinationVertexChoiceBox();
        this.orderSetDate();
        this.setDatePickerProperties();
        this.setPane();
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
            availableDriver = driversRegistry.getAvailableDrivers(startDate, finishDate, disposition);
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
                             + " the disposition or navigate to the Drivers system module to add"
                             + " drivers.");
             alert.showAndWait();
             driverObservable.clear();
             return;
        }
        //System.out.println(availableDriver.get(0)+" HE IS HEREEE");
        driverObservable.clear();
        driverObservable = FXCollections.observableArrayList(availableDriver);
        driversChoiceBox.setItems(driverObservable);

        if(availableDriver.contains(disposition.getDriver())){
            driversChoiceBox.getSelectionModel().select(disposition.getDriver());
        }else{
            driversChoiceBox.getSelectionModel().selectFirst();
        }
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
            availableTrucks = trucksRegistry.getAvailableTrucks(startDate, finishDate, 
                    disposition, sourceVertexChoiceBox.getSelectionModel().getSelectedItem());
        } catch (ParseException ex) {
            Logger.getLogger(addCustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(availableTrucks.get(0)+" HE IS HEREEE");
        if(availableTrucks.isEmpty()){
             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Trucks Availability");
             alert.setHeaderText(null);
             alert.setContentText("There are not any available trucks between "+orderDate+""
                     + " - "+deliveryDate+" in the location: "+
                     sourceVertexChoiceBox.getSelectionModel().getSelectedItem().getCity()+
                     ". Change the time frame of the disposition or change the source location of"
                             + " the disposition or navigate to the Trucks system module to add"
                             + " trucks.");
             alert.showAndWait();
             truckObservable.clear();
             return;
        }
       // System.out.println(availableTrucks.get(0)+" HE IS HEREEE");
        truckObservable.clear();
        truckObservable = FXCollections.observableArrayList(availableTrucks);
        trucksChoiceBox.setItems(truckObservable);
        
        if(availableTrucks.contains(disposition.getTruck())){
            trucksChoiceBox.getSelectionModel().select((Truck)disposition.getTruck());
        }else{
            trucksChoiceBox.getSelectionModel().selectFirst();
        }
    }
    private void setPane(){
       if(disposition.getCarrier().getRole() instanceof External){
           pane.getSelectionModel().select(externalTab);
       }else{
           pane.getSelectionModel().select(internalTab);
       }
    }
    @FXML
    public void tabPaneActions(){
        System.out.println("TAB PANE ACTION");
        this.setAvailableDrivers();
        this.setAvailableTrucks();
    }
    private void setCarrierDropDownMenu(){
       ObservableList<Carrier> carriersObservable = 
               FXCollections.observableArrayList(carriersRegistry.getExternalCarriers());
       carrierChoiceBox.setItems(carriersObservable);
       carrierChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Carrier>() {

            @Override
            public void changed(ObservableValue<? extends Carrier> observable, Carrier oldValue, Carrier carrier) {

                carrierChoiceBox.setValue(carrier);
                carrierIdLablel.setText(carrier.getId()+"");
                carrierCompanyLabel.setText(carrier.getCompany());
                carrierModeLabel.setText(carrier.getCarrierMode());
                carrierRoleLabel.setText(carrier.getCarrierRole());
                

            }
        });
       if(disposition.getCarrier().getRole() instanceof External){
            carrierChoiceBox.getSelectionModel().select(disposition.getCarrier());
       }else{
           carrierChoiceBox.getSelectionModel().selectFirst();
       }
        
    }
    private void setSourceVertexChoiceBox(){
       ObservableList<Vertex> verticesObservable = 
               FXCollections.observableArrayList(graphDatabase.getGraph().getVertexes());
       sourceVertexChoiceBox.setItems(verticesObservable);
       sourceVertexChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Vertex>() {

            @Override
            public void changed(ObservableValue<? extends Vertex> observable, Vertex oldValue, Vertex vertex) {

                sourceVertexChoiceBox.setValue(vertex);
                
                

            }
        });
       sourceVertexChoiceBox.getSelectionModel().select(disposition.getOriginAddress());
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
       destinationVertexChoiceBox.getSelectionModel().select(disposition.getDestinationAddress());
    }
    private void orderSetDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale( Locale.UK );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
        LocalDate date1 = LocalDate.parse(disposition.getStartDate(), formatter);
        startDate.setValue(date1);
        LocalDate date2 = LocalDate.parse(disposition.getFinishDate(), formatter);
        finishDate.setValue(date2);
        
    }
    
    
    private void setDatePickerProperties(){
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
    @FXML
    public void datePickerActions(){
        System.out.println("DATE PICKER ACTION");
        this.setAvailableDrivers();
        this.setAvailableTrucks();  
    }
    @FXML
    public void sourceChoiceBoxActions(){
        System.out.println("SOURCE CHOICE BOX ACTION");
        this.setAvailableDrivers();
        this.setAvailableTrucks();  
        
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
            


        }
    });
        
    }
    private void setTrucksDropDownMenu() throws ParseException{
       trucksChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Truck>() {

        @Override
        public void changed(ObservableValue<? extends Truck> observable, Truck oldValue, Truck truck) {

            trucksChoiceBox.setValue(truck);
            truckPlateNumberLabel.setText(truck.getPlateNumber());
            truckAvailabilityStatusLabel.setText(truck.getAvailabilityStatus() == true ? "Available" : "Unavailable");
            truckConditionStatusLabel.setText(truck.getConditionStatus() == true ? "Damaged" : "Not Damaged");
            


        }
    });
       
    }    

    @FXML
    private void editDisposition(ActionEvent event) throws ParseException {
        if (sourceVertexChoiceBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select an origin Address!");
            alert.showAndWait();
            return;
        }
        if (destinationVertexChoiceBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a destination Address!");
            alert.showAndWait();
            return;
        }
        
        String orderDate = startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String deliveryDate = finishDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
       
        if(pane.getSelectionModel().getSelectedItem() == internalTab){
           if (driversChoiceBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a driver!");
            alert.showAndWait();
            return;
           }
            if (trucksChoiceBox.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Please select a truck!");
                alert.showAndWait();
                return;
            }
             dispositionRegistry.editInternalDisposition(disposition, sourceVertexChoiceBox.getSelectionModel().getSelectedItem(),
                destinationVertexChoiceBox.getSelectionModel().getSelectedItem(), carriersRegistry.getInternalCarrier(), 
                orderDate, deliveryDate, 0, order, 
                trucksChoiceBox.getSelectionModel().getSelectedItem(), 
                driversChoiceBox.getSelectionModel().getSelectedItem());
       }else{
           if (carrierChoiceBox.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Please select a carrier!");
                alert.showAndWait();
                return;
            }
           dispositionRegistry.editExternalDisposition(disposition, sourceVertexChoiceBox.getSelectionModel().getSelectedItem(),
                destinationVertexChoiceBox.getSelectionModel().getSelectedItem(),
                carrierChoiceBox.getSelectionModel().getSelectedItem(), orderDate, 
                deliveryDate, 0, order);
           
       }
       
        
       
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
    public void setParentController(DispositionController parent){
        this.parent=parent;
    }
    private LocalDate NOW_LOCALDATE(){
        return LocalDate.now();
    }
    public void setOrder(CustomerOrder order){
        this.order=order;
    }
     public void setDisposition(Disposition disposition){
        this.disposition=disposition;
    }
     public void setGrandFatherController(CustomerOrdersController grandFather){
        this.grandFather = grandFather;
    }
    
}
