/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.CustomerOrders;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.controller.CustomerOrders.CustomerOrderRegistryDatabase;
import com.project.TOMS.controller.CustomerRecords.CustomerRegistryDatabase;
import com.project.TOMS.controller.Vehicles.TrailerDatabaseRegistry;
import com.project.TOMS.controller.systemUserRecords.SystemUserRegistryDatabase;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerRecords.Customer;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.Vehicles.Trailer;
import com.project.TOMS.model.systemUserRecords.SystemUser;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class editCustomerOrderController implements Initializable {

    
    @FXML
    private Button confirmButton;
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
    private DatePicker startDate;
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
    private DatePicker finishDate;
    @FXML
    private Label disponentId;
    @FXML
    private Label disponentName;
    @FXML
    private Label disponentSurname;
    @FXML
    private Label idLable;
    
    @FXML
    private TextField massField;
    @FXML
    private ComboBox<Trailer> trailersChoiceBox;
    @FXML
    private ComboBox<Customer> customersChoiceBox;
    @FXML
    private ComboBox<Vertex> sourceVertexChoiceBox;
    @FXML
    private ComboBox<Vertex> destinationVertexChoiceBox;
    
    private SystemUser user;
    private CustomerOrdersController parent;
    private CustomerOrder order;

    
    private CustomerRegistryDatabase customerRegistry;
    private TrailerDatabaseRegistry trailerRegistry;
    private CustomerOrderRegistryDatabase customerOrderRegistry;
    private SystemUserRegistryDatabase systemUserRegistry;
    private graphRegistryDatabase graphDatabase;
    ObservableList<Trailer> trailerObservable;
   
           

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
 
    }  
    public void reinit() throws ParseException{
        customerRegistry = CustomerRegistryDatabase.getInstance();
        customerOrderRegistry = CustomerOrderRegistryDatabase.getInstance();
        trailerRegistry = TrailerDatabaseRegistry.getInstance();
        customerOrderRegistry = CustomerOrderRegistryDatabase.getInstance();
        systemUserRegistry = SystemUserRegistryDatabase.getInstance();
        graphDatabase = graphRegistryDatabase.getInstance();
        trailerObservable = FXCollections.observableArrayList();

        this.setCustomerDropDownMenu();
        this.setDisponentLabels();
        this.setOrderTypeToggle();
        this.setCustomerDetails();
        this.setOrderType();
        this.setDisponentDetails();
        this.setMass();
        this.setID();
        this.setSourceVertexChoiceBox();
        this.setDestinationVertexChoiceBox();
        this.setTrailerDropDown();
        this.setDatapickerDateProperties();
        this.orderSetDate();
        this.setAvaialableTrailers();
        
        
        
    }
    private void setAvaialableTrailers(){
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
        ArrayList<Trailer> availableTrailer = new ArrayList<Trailer>();
        try {
            availableTrailer = trailerRegistry.getAvailableTrailers(startDate, finishDate, 
                    order, sourceVertexChoiceBox.getSelectionModel().getSelectedItem() );
        } catch (ParseException ex) {
            Logger.getLogger(addCustomerOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(availableTrailer.isEmpty()){
             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Trailer Availability");
             alert.setHeaderText(null);
             alert.setContentText("There are not any available trailers between "+orderDate+""
                     + " - "+deliveryDate+" in the location: "+
                     sourceVertexChoiceBox.getSelectionModel().getSelectedItem().getCity()+
                     ". Change the time frame of the order or change the source location of"
                             + " the order or navigate to the Vehicles system module to add"
                             + " trailers.");
             alert.showAndWait();
             trailerObservable.clear();
             return;
        }
        System.out.println(availableTrailer.get(0)+" HE IS HEREEE");
        trailerObservable.clear();
        trailerObservable = FXCollections.observableArrayList(availableTrailer);
        trailersChoiceBox.setItems(trailerObservable);
        if(availableTrailer.contains(order.getTrailer())){
            trailersChoiceBox.getSelectionModel().select(order.getTrailer());
        }else{
            trailersChoiceBox.getSelectionModel().selectFirst();
        }
    }
    //https://stackoverflow.com/questions/8746084/string-to-localdate
    private void orderSetDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale( Locale.UK );  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
        LocalDate date1 = LocalDate.parse(order.getOrderDate(), formatter);
        startDate.setValue(date1);
        LocalDate date2 = LocalDate.parse(order.getDeliveryDate(), formatter);
        finishDate.setValue(date2);
    }
    private void setTrailerDropDown(){
        trailersChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Trailer>() {

            @Override
            public void changed(ObservableValue<? extends Trailer> observable, Trailer oldValue, Trailer trailer) {

                trailersChoiceBox.setValue(trailer);
                trailerId.setText(trailer.getId()+"");
                trailerPlateNumber.setText(trailer.getPlateNumber());
                trailerAvailabilityStatus.setText(trailer.getAvailabilityStatus() == true ? "Available" : "Unavailable");
                trailerConditionStatus.setText(trailer.getConditionStatus() == true ? "Damaged" : "Not Damaged");
                


            }
        });
    }
    @FXML
    public void datePickerAction(){
        this.setAvaialableTrailers();
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
       sourceVertexChoiceBox.getSelectionModel().select(order.getOriginAddress());
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
       destinationVertexChoiceBox.getSelectionModel().select(order.getDestinationAddress());
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
   
    
    private void setOrderType(){
        
        radioGroup.selectToggle((order.getType().equals("Import") ? radioImport : radioExport));
    }
    private void setCustomerDetails(){
        customersChoiceBox.getSelectionModel().select(order.getCustomer());
    }
    @FXML
    public void sourceChoiceBoxActions() {
        this.setAvaialableTrailers();
    }
    private void setDatapickerDateProperties(){
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
    private void setCustomerDropDownMenu(){
       ObservableList<Customer> customersObservable = FXCollections.observableArrayList();
       customersObservable = FXCollections.observableArrayList(customerRegistry.getCustomers());
       customersChoiceBox.setItems(customersObservable);
       customersChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {

            @Override
            public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer customer) {

                customersChoiceBox.setValue(customer);
                customerIdLablel.setText(customer.getId()+"");
                customerCompanyLabel.setText(customer.getCompany());
                customerTelephoneLabel.setText(customer.getPhone());
                customerCountry.setText(customer.getAddress().getCountry());
                customerCity.setText(customer.getAddress().getCity());
                

            }
        });
    }
    private Date convertDate(String dat) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(dat);
        return date;
    }
    

    @FXML
    private void editEntry(ActionEvent event) throws ParseException {
        if (customersChoiceBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer!");
            alert.showAndWait();
            return;
        }
        if (trailersChoiceBox.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Trailer!");
            alert.showAndWait();
            return;
        }
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
        
        if (massField.getText() == null || massField.getText().trim().isEmpty() ) {
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in Mass details");
            alert.showAndWait();
            return;
        }
        try{
            Double.valueOf(massField.getText());
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in Mass details");
            alert.showAndWait();
            return;
                
        }
        String orderType = (radioGroup.getSelectedToggle() == radioImport) ? "Import" : "Export";
        String orderDate = startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String deliveryDate = finishDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        double mass = Double.parseDouble(massField.getText());
        customerOrderRegistry.editCustomerOrder(order, sourceVertexChoiceBox.getSelectionModel().getSelectedItem(),
                destinationVertexChoiceBox.getSelectionModel().getSelectedItem(), orderType, 
                orderDate, deliveryDate, mass, trailersChoiceBox.getSelectionModel().getSelectedItem(), 
                0, customersChoiceBox.getSelectionModel().getSelectedItem(), user);
        this.parent.outputTableData();
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
        
    
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    private LocalDate NOW_LOCALDATE(){
        return LocalDate.now();
    }
    //https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
    public void setParentController(CustomerOrdersController parent){
        this.parent=parent;
    }
    public void setCustomerOrder(CustomerOrder order){
        this.order = order;
    }
    
}
