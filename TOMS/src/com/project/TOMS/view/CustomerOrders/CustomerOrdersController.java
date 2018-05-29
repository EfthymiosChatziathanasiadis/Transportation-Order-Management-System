/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.CustomerOrders;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Graph;
import com.project.TOMS.algorithms.dijkstra.model.Vertex;
import com.project.TOMS.algorithms.disjkstra.algorithm.DijkstraAlgorithm;
import com.project.TOMS.controller.CarrierRecords.CarrierRegistryDatabase;
import com.project.TOMS.controller.CustomerOrders.CustomerOrderRegistryDatabase;
import com.project.TOMS.controller.CustomerRecords.CustomerRegistryDatabase;
import com.project.TOMS.controller.DispositionRecords.DispositionRegistryDatabase;
import com.project.TOMS.controller.Drivers.DriversDatabaseRegistry;
import com.project.TOMS.controller.Vehicles.TruckDatabaseRegistry;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerOrders.Disposition;
import com.project.TOMS.view.Authentication.LoginScreenController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class CustomerOrdersController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<CustomerOrder> customerOrderTable;
    @FXML
    private TableColumn<CustomerOrder, String> typeCol;
    @FXML
    private TableColumn<CustomerOrder, String> idCol;
    @FXML
    private TableColumn<CustomerOrder, String> startCol;
    @FXML
    private TableColumn<CustomerOrder, String> finishCol;
    @FXML
    private TableColumn<CustomerOrder, String> originCol;
    @FXML
    private TableColumn<CustomerOrder, String> destinationCol;
    @FXML
    private TableColumn<CustomerOrder, String> trailerPlateCol;
    @FXML
    private TableColumn<CustomerOrder, String> tarriffCol;
    @FXML
    private TableColumn<CustomerOrder, String> customerCol;
    @FXML
    private TableColumn<CustomerOrder, String> disponentCol;
    @FXML
    private TableColumn<CustomerOrder, String> routingStatus;

    @FXML
    private ChoiceBox<String> searchOptions;
    @FXML
    private ChoiceBox<String> viewChoices;
    @FXML
    private RadioButton importRadio;
    @FXML
    private RadioButton exportRadio;
    
    @FXML
    private Button bookings;
    private ToggleGroup radioGroup;
    
    private CustomerOrderRegistryDatabase orderDatabase;
    private DispositionRegistryDatabase dispositionDatabase;
    private ObservableList<CustomerOrder> ordersObservable;
    private graphRegistryDatabase graphRegistry ;
    private CarrierRegistryDatabase carrierRegistry ;
    private TruckDatabaseRegistry trucksRegistry;
    private DriversDatabaseRegistry driverRegistry;
    private CustomerRegistryDatabase customerRegistry;
    @FXML
    private Button resetButton;
    
    public static Stage dispositionStage;
    public static Stage mapStage;
    public  Stage addCustomerOrderStage;
    public static Stage editCustomerOrderStage;
    public static Stage customerDetailsStage;
    


   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        orderDatabase = CustomerOrderRegistryDatabase.getInstance();
        dispositionDatabase = DispositionRegistryDatabase.getInstance();
        graphRegistry = graphRegistryDatabase.getInstance();
        trucksRegistry = TruckDatabaseRegistry.getInstance();
        driverRegistry = DriversDatabaseRegistry.getInstance();
        carrierRegistry = CarrierRegistryDatabase.getInstance();
        customerRegistry = CustomerRegistryDatabase.getInstance();
        this.setTable();
        ordersObservable = FXCollections.observableArrayList();
        this.setSearchOptions();
        this.initToggle();
        this.setViewChoices();
        this.setToggleGroup();
        try {
            this.outputTableData();
        } catch (ParseException ex) {
            Logger.getLogger(CustomerOrdersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    private void setViewChoices(){
       
        viewChoices.setItems(
                FXCollections.observableArrayList( "Routed Orders", "Orders in progress")
        );
         viewChoices.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String veh) {
                
                if(veh.equals("Routed Orders")){
                    try {
                        if(orderDatabase.getRoutedCustomerOrders() == null){
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Information");
                            alert.setHeaderText("INFO");
                            alert.setContentText("There are not any routed orders at the moment");
                            alert.showAndWait();
                        }else{
                            try {
                                outputTableData(orderDatabase.getRoutedCustomerOrders());
                            } catch (ParseException ex) {
                                Logger.getLogger(CustomerOrdersController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (ParseException ex) { 
                        Logger.getLogger(CustomerOrdersController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    try {
                        if(orderDatabase.getOrdersInProgressCustomerOrders()== null){
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Information");
                            alert.setHeaderText("INFO");
                            alert.setContentText("There are not any in progress orders at the moment");
                            alert.showAndWait();
                        }else{
                            outputTableData(orderDatabase.getOrdersInProgressCustomerOrders());
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(CustomerOrdersController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  
                }
             
            
            
            }
            
        });
    }
    private void initToggle(){
        radioGroup=new ToggleGroup();
        importRadio.setToggleGroup(radioGroup);
        exportRadio.setToggleGroup(radioGroup);

       // radioGroup.selectToggle(importRadio);
       
    }
    private void setToggleGroup(){
        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                    if(radioGroup.getSelectedToggle() == importRadio){
                        try {
                            outputTableData(orderDatabase.getImportCustomerOrders());
                        } catch (ParseException ex) {
                            Logger.getLogger(CustomerOrdersController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{
                        try {
                            outputTableData(orderDatabase.getExportCustomerOrders());
                        } catch (ParseException ex) {
                            Logger.getLogger(CustomerOrdersController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
        
            }
        }
        );
    }
    private void setSearchOptions(){
        searchOptions.setItems(
                FXCollections.observableArrayList("ID","Trailer Plate Number" ,"Customer Company","Disponent Surname")
        );
        //First choicebox item is selected by default
        searchOptions.getSelectionModel().selectFirst();
    }
    private void setTable() {
        
        idCol.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("orderDate"));
        finishCol.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("deliveryDate"));
        originCol.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("origin"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("destination"));
        trailerPlateCol.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("trailerPlate"));
        tarriffCol.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("tarrif"));
        customerCol.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("customerCompany"));
        disponentCol.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("disponentSurname"));
        routingStatus.setCellValueFactory(new PropertyValueFactory<CustomerOrder, String>("status"));
        routingStatus.setCellFactory(column -> {
            return new TableCell<CustomerOrder, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                       //  setText();

                        // Style all dates in March with a different color.
                        if (item.equals("IN PROGRESS")) {
                            
                            setStyle("-fx-background-color: yellow");
                            setText("IN PROGRESS");
                            setTextFill(Color.CHOCOLATE);
                        } else {
                            
                            setStyle("-fx-background-color: #76FF03");
                            setText("ROUTING COMPLETE");
                            setTextFill(Color.BLACK);
                        }
                    }
                }
            };
        });
    

    }
    @FXML
    public void outputTableData() throws ParseException{
        ordersObservable.clear();
        ArrayList<CustomerOrder> orders = orderDatabase.getCustomerOrders();
        ordersObservable = FXCollections.observableArrayList(orders);
        customerOrderTable.setItems(ordersObservable);            
    }
    public void outputTableData(CustomerOrder order){
        ordersObservable.clear();
        ArrayList<CustomerOrder> orders = new ArrayList<CustomerOrder>();
        orders.add(order);
        ordersObservable = FXCollections.observableArrayList(orders);
        customerOrderTable.setItems(ordersObservable);            
    }
    public void outputTableData(ArrayList<CustomerOrder> ordersList){
        ordersObservable.clear();
        ArrayList<CustomerOrder> orders = ordersList;
        ordersObservable = FXCollections.observableArrayList(orders);
        customerOrderTable.setItems(ordersObservable);       
    }
    
    @FXML
    public void viewDispositions() throws ClassNotFoundException, SQLException, IOException, ParseException {

        CustomerOrder order=customerOrderTable.getSelectionModel().getSelectedItem();
        
        if (order == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer order from the table");
            alert.showAndWait();
        }else{
            ArrayList<Disposition> dispositions = dispositionDatabase.getDispositionsOfOrder(order.getId());
            
       
            if(!dispositions.isEmpty()){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dispositions.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                DispositionController controller = fxmlLoader.<DispositionController>getController();
                controller.setOrder(order);
                controller.setParentController(this);
                controller.reinit();

                Scene scene = new Scene(root);
                dispositionStage = new Stage();
                dispositionStage.setScene(scene);
                dispositionStage.centerOnScreen();
                dispositionStage.setTitle("ORDER: "+order.getId()+" TRANSPORTATION "
                            + "ROUTE FROM: "+order.getOrigin()+", TO: "+
                            order.getDestination());
                dispositionStage.initModality(Modality.WINDOW_MODAL);
                dispositionStage.initOwner(LoginScreenController.stage);
                dispositionStage.show();
            }else{
                Graph graph = graphRegistry.getGraph(); 
                DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
                dijkstra.dijkstraExecute(order.getOriginAddress());
                ArrayList<Vertex> path = dijkstra.getShortestPath(order.getDestinationAddress());
                if(path == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Transport Order Information");
                    alert.setHeaderText("CUSTOMER ORDER: "+order.getId()+", ROUTE DOES NOT EXIST!");
                    alert.setContentText("Route FROM: "+order.getOrigin()+" , TO: "+order.getDestination()+" does not exist, Contact administrator to add this new route! ");      
                    alert.showAndWait();
                    
                }else{
                    for (int i = 0; i < path.size()-1; i++) {
                        dispositionDatabase.addExternalDisposition(path.get(i), path.get(i+1),
                                carrierRegistry.getExternalCarriers().get(0), order.getOrderDate(), 
                                order.getDeliveryDate(), i, order);

                    }
                    this.outputTableData();//re-compute/update routing status
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dispositions.fxml"));
                    Parent root = (Parent) fxmlLoader.load();

                    DispositionController controller = fxmlLoader.<DispositionController>getController();
                    controller.setOrder(order);
                    controller.setParentController(this);
                    controller.reinit();

                    Scene scene = new Scene(root);
                    dispositionStage = new Stage();
                    dispositionStage.setScene(scene);
                    dispositionStage.centerOnScreen();
                    dispositionStage.setTitle("ORDER: "+order.getId()+" TRANSPORTATION "
                            + "ROUTE FROM: "+order.getOrigin()+", TO: "+
                            order.getDestination());
                    dispositionStage.initModality(Modality.WINDOW_MODAL);
                    dispositionStage.initOwner(LoginScreenController.stage);
                    dispositionStage.show();
                }
                
                
            }

                
            
        }

    }

    @FXML
    private void handleSearch(ActionEvent event) throws ParseException {
        
        
        if (searchField.getText() == null || searchField.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please Fill in the search field!");
            alert.showAndWait();
            return;
        }
        String userSearchInput = searchField.getText();
        String searchOption = searchOptions.getSelectionModel().getSelectedItem();
        
        switch (searchOption) {
            case "ID":
                int id = -1;
                try{
                    id = Integer.parseInt(userSearchInput);
                }catch(NumberFormatException e){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Information");
                    alert.setHeaderText("Illegal Characters");
                    alert.setContentText("Please to search order ID's input numbers only!");
                    alert.showAndWait();
                    break;
                }
                if(orderDatabase.getCustomerOrdersWhereId(id) == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Information");
                    alert.setHeaderText("does not exist");
                    alert.setContentText("Order with id = "+id+" does not exist");
                    alert.showAndWait();
                }else{
                    this.outputTableData(orderDatabase.getCustomerOrdersWhereId(id));
                }
                break;
            case "Trailer Plate Number":
                if(orderDatabase.getCustomerOrdersWhereTrailerPlateNumber(userSearchInput) == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Information");
                    alert.setHeaderText("Trailer Does Not Exist");
                    alert.setContentText("Trailer with plate="+userSearchInput+" does not exist!");
                    alert.showAndWait();
                }else{
                    this.outputTableData(orderDatabase.getCustomerOrdersWhereTrailerPlateNumber(userSearchInput));
                }
                
                break; 
            case "Customer Company":
                if(orderDatabase.getCustomerOrdersWhereCustomerCompany(userSearchInput) == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Information");
                    alert.setHeaderText("Trailer Does Not Exist");
                    alert.setContentText("Customer with company="+userSearchInput+" does not exist!");
                    alert.showAndWait();
                }else{
                    this.outputTableData(orderDatabase.getCustomerOrdersWhereCustomerCompany(userSearchInput));
                }
                
                break;
            case "Disponent Surname":
                if(orderDatabase.getCustomerOrdersWhereDisponent(userSearchInput) == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Information");
                    alert.setHeaderText("Trailer Does Not Exist");
                    alert.setContentText("Customer order with disponent = "+userSearchInput+" does not exist!");
                    alert.showAndWait();
                }else{
                    this.outputTableData(orderDatabase.getCustomerOrdersWhereDisponent(userSearchInput));
                }
                
                break;
            
            
        }
        

    }

    @FXML
    private void addCustomerOrder(ActionEvent event)throws ClassNotFoundException, SQLException, IOException, ParseException  {
        
        if(customerRegistry.getCustomers().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("NOT ANY CUSTOMERS");
            alert.setHeaderText("NO CUSTOMERS");
            alert.setContentText("To add a customer order, use must first have customer records in the system\n"
                    + "To add Customers, navigate to the Customers module!");
            alert.showAndWait();
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addCustomerOrder.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            addCustomerOrderController controller = fxmlLoader.<addCustomerOrderController>getController();
            controller.setParentController(this);
            controller.reinit();

            Scene scene = new Scene(root);
            addCustomerOrderStage = new Stage();
            addCustomerOrderStage.setScene(scene);
            addCustomerOrderStage.centerOnScreen();
            addCustomerOrderStage.setTitle("Add Customer Order");
            addCustomerOrderStage.initModality(Modality.WINDOW_MODAL);
            addCustomerOrderStage.initOwner(LoginScreenController.stage);
            addCustomerOrderStage.show();
        }
        
    }

    

    @FXML
    private void delete(ActionEvent event) throws ParseException {
        CustomerOrder order=customerOrderTable.getSelectionModel().getSelectedItem();
        if (order == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer order from the table");
            alert.showAndWait();
        }else{
            orderDatabase.delete(order);
            this.outputTableData();
        }
       
    }

    @FXML
    private void handleEdit(ActionEvent event) throws IOException, ParseException {
        CustomerOrder order=customerOrderTable.getSelectionModel().getSelectedItem();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date orderDate = this.convertDate(order.getOrderDate());
        Date deliveryDate = this.convertDate(order.getDeliveryDate());
        Date currentDate = this.getCurrentTime();
        if (order == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer order from the table");
            alert.showAndWait();
        }else if(currentDate.after(orderDate) && currentDate.before(deliveryDate)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("The execution of this order has already started. "
                    + "It is not possible to edit an order that has already started. "
                    + "You can view order details by clicking VIEW DETAILS");
            alert.showAndWait();
        }else if(currentDate.after(deliveryDate)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Past orders cannot be edited. "
                    + "You can view the order details by clicking "
                    + "the button VIEW DETAILS");
            alert.showAndWait();
        }else{
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editCustomerOrder.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            editCustomerOrderController controller = fxmlLoader.<editCustomerOrderController>getController();
            controller.setParentController(this);
            controller.setCustomerOrder(order);
            controller.reinit();

            Scene scene = new Scene(root);
            editCustomerOrderStage = new Stage();
            editCustomerOrderStage.setScene(scene);
            editCustomerOrderStage.centerOnScreen();
            editCustomerOrderStage.setTitle("Edit Customer Order");
            editCustomerOrderStage.initModality(Modality.WINDOW_MODAL);
            editCustomerOrderStage.initOwner(LoginScreenController.stage);
            editCustomerOrderStage.show();
        }
        
    }
    private Date convertDate(String dat) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(dat);
        return date;
    }
    private Date getCurrentTime(){
        Date todayDate = Calendar.getInstance().getTime();
       return todayDate;
    }
   

    @FXML
    private void handleVehicleDetails(ActionEvent event) throws IOException {
        CustomerOrder order=customerOrderTable.getSelectionModel().getSelectedItem();
        
        if (order == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer order from the table");
            alert.showAndWait();
        }else{
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customerOrderDetails.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            customerOrderDetailsController controller = fxmlLoader.<customerOrderDetailsController>getController();
            controller.setCustomerOrder(order);
            controller.reinit();

            Scene scene = new Scene(root);
            customerDetailsStage = new Stage();
            customerDetailsStage.setScene(scene);
            customerDetailsStage.centerOnScreen();
            customerDetailsStage.setTitle("Customer Order Details");
            customerDetailsStage.initModality(Modality.WINDOW_MODAL);
            customerDetailsStage.initOwner(LoginScreenController.stage);
            customerDetailsStage.show();
        }
    }

    @FXML
    private void allVehicles(ActionEvent event) {
    }
    
    @FXML
    private void viewMap(ActionEvent event)throws ClassNotFoundException, SQLException, IOException, ParseException  {
         CustomerOrder order=customerOrderTable.getSelectionModel().getSelectedItem();
        
        if (order == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer order from the table");
            alert.showAndWait();
        }else{
            ArrayList<Disposition> dispositions = dispositionDatabase.getDispositionsOfOrder(order.getId());
            if(dispositions.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText("Customer Order: "+order.getId()+", has not been routed!");
                alert.setContentText("To route the order click the button <VIEW ROUTE> in the TRANSPORTATION ROUTING section!");
                alert.showAndWait();
            }else if(order.getStatus().equals("IN PROGRESS")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information");
                alert.setHeaderText("Customer Order: "+order.getId()+", has not "
                        + "been routed correctly(i.e. route does not start/finish "
                        + "at the correct stations");
                alert.setContentText("To route it correctly order click the button"
                        + " <VIEW ROUTE> in the TRANSPORTATION ROUTING section!");
                alert.showAndWait();
            
            }else{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("map.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                MapController controller = fxmlLoader.<MapController>getController();
                controller.setCustomerOrder(order);
                controller.reinitCustomerOrder();               

                Scene scene = new Scene(root);
                mapStage = new Stage();
                mapStage.setScene(scene);
                mapStage.centerOnScreen();
                mapStage.setTitle("CUSTOMER ORDER: "+order.getId()+" FROM: "+order.getOrigin()+", TO: "+order.getDestination() );
                mapStage.initModality(Modality.WINDOW_MODAL);
                mapStage.initOwner(LoginScreenController.stage);
                mapStage.show();
            }
        }
    }
    
    
}
