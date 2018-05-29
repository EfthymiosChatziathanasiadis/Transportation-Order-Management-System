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
import com.project.TOMS.model.Address.Address;
import com.project.TOMS.model.CarrierRecords.Carrier;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerOrders.Disposition;
import com.project.TOMS.model.Drivers.Driver;
import com.project.TOMS.model.Vehicles.Trailer;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class addExternalDispositionController implements Initializable {

    @FXML
    private ChoiceBox<Carrier> carrierChoiceBox;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label carrierIdLablel;
    @FXML
    private Label carrierCompanyLabel;
    @FXML
    private Label carrierModeLabel;
    @FXML
    private Label carrierRoleLabel;
    @FXML 
    private Label trailerIdLabel;
    @FXML
    private Label trailerPlateNumber;
    @FXML
    private Label trailerAvailabilityStatus;
    @FXML
    private Label trailerConditionStatus;
    @FXML
    private DatePicker startDate;
    @FXML
    private Label trailerCountry;
   
    @FXML
    private Label trailerCity;
    @FXML
    private DatePicker finishDate;
    
    @FXML
    private ChoiceBox<Vertex> sourceVertexChoiceBox;
    @FXML
    private ChoiceBox<Vertex> destinationVertexChoiceBox;
    private DispositionController parent;
    private CustomerOrdersController grandFather;
    private CustomerOrder order;
    
    private CarrierRegistryDatabase carriersRegistry;
    private DispositionRegistryDatabase dispositionsRegistry;
    private CustomerOrderRegistryDatabase ordersDatabase;
    private graphRegistryDatabase graphDatabase;
    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    public  void reinit() throws ParseException{
        ordersDatabase = CustomerOrderRegistryDatabase.getInstance();
        ordersDatabase.configureAvailability();
        carriersRegistry = CarrierRegistryDatabase.getInstance();
        dispositionsRegistry = DispositionRegistryDatabase.getInstance();
        graphDatabase = graphRegistryDatabase.getInstance();
        this.setCarrierDropDownMenu();
        this.setDatapickerDate();
        this.setTrailerDetails();
        this.setSourceVertexChoiceBox();
        this.setDestinationVertexChoiceBox();
        this.setAddresses();
    }
    private void setAddresses() throws ParseException, ParseException{
        ArrayList<Disposition> dispositions = dispositionsRegistry.getDispositionsOfOrder(order.getId());
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
    private void setTrailerDetails(){
        Trailer trailer = order.getTrailer();
        trailerIdLabel.setText(trailer.getId()+"");
        trailerPlateNumber.setText(trailer.getPlateNumber());
        trailerAvailabilityStatus.setText(trailer.getAvailabilityStatus() == true ? "Available" : "Unavailable");
        trailerConditionStatus.setText((trailer.getConditionStatus() == true ? "Damaged" : "Not Damaged"));
        trailerCountry.setText(trailer.getAddress().getCountry());
        trailerCity.setText(trailer.getAddress().getCity());
        
    }
    private void setDatapickerDate(){
        startDate.setEditable(false);
        finishDate.setEditable(false);
        startDate.setValue(this.NOW_LOCALDATE());
        LocalDate date = startDate.getValue();
        finishDate.setValue(date.plusDays(3));
    }
    private void setCarrierDropDownMenu(){
       ObservableList<Carrier> carriersObservable = FXCollections.observableArrayList();
       carriersObservable = FXCollections.observableArrayList(carriersRegistry.getExternalCarriers());
       System.out.println(carriersRegistry.getCarriers());
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
       carrierChoiceBox.getSelectionModel().selectFirst();
        
    }
     
    @FXML
    private void addEntry(ActionEvent event) throws ParseException {
        
        
        String orderDate = startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String deliveryDate = finishDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        
        dispositionsRegistry.addExternalDisposition(sourceVertexChoiceBox.getSelectionModel().getSelectedItem(),
                destinationVertexChoiceBox.getSelectionModel().getSelectedItem(),
                carrierChoiceBox.getSelectionModel().getSelectedItem(), orderDate, 
                deliveryDate, 0, order);
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
    public void setOrder(CustomerOrder order){
        this.order=order;
    }
    private LocalDate NOW_LOCALDATE(){
        return LocalDate.now();
    }
    public void setGrandFatherController(CustomerOrdersController grandFather){
        this.grandFather = grandFather;
    }
    
}
