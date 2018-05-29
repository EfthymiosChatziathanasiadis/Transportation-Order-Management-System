/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.CustomerOrders;


import com.project.TOMS.controller.DispositionRecords.DispositionRegistryDatabase;
import com.project.TOMS.model.CarrierRecords.External;
import com.project.TOMS.model.CustomerOrders.CustomerOrder;
import com.project.TOMS.model.CustomerOrders.Disposition;
import com.project.TOMS.view.Authentication.LoginScreenController;
import static com.project.TOMS.view.CustomerOrders.CustomerOrdersController.mapStage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class DispositionController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Disposition> dispositionTable;
    @FXML
    private TableColumn<Disposition, String> idCol;
    @FXML
    private TableColumn<Disposition, String> startCol;
    @FXML
    private TableColumn<Disposition, String> finishCol;
    @FXML
    private TableColumn<Disposition, String> originCol;
    @FXML
    private TableColumn<Disposition, String> destinationCol;
    @FXML
    private TableColumn<Disposition, String> carrierCompanyCol;
    @FXML
    private TableColumn<Disposition, String> carrierModeCol;
    @FXML
    private TableColumn<Disposition, String> carrierRoleCol;
    @FXML
    private TableColumn<Disposition, String> truckPlateCol;
    
    @FXML
    private TableColumn<Disposition, String> driverIdCol;
    @FXML
    private TableColumn<Disposition, String> driverSurnameCol;
    @FXML
    private ChoiceBox<?> searchOpt;
    
    @FXML
    private Button bookings;
    
    
    private ToggleGroup radioGroupViewDispositions;
    @FXML
    private RadioButton internalRadio;
    @FXML
    private RadioButton externalRadio; 
    
    private ToggleGroup radioGroupAddDispositions;
    @FXML
    private RadioButton addInternalRadio;
    @FXML
    private RadioButton addExternalRadio;
    @FXML
    private Label idLabel;
    @FXML
    private Label fromLabel;
    @FXML
    private Label toLabel;
    @FXML
    private Label startLabel;
    @FXML
    private Label finishLabel;
    @FXML
    private Label trailerLabel;
    
    
    private DispositionRegistryDatabase dispositionDatabase;
    private ObservableList<Disposition> dispositionObservable;
        
    private CustomerOrder order;
    private CustomerOrdersController parent;
    
    private Stage addInternalDispositionStage;
    private Stage addExternalDispositionStage;
    private Stage editExternalDispositionStage;
    private Stage editInternalDispositionStage;
    
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  
    public void reinit() throws ParseException{
        
        dispositionDatabase = DispositionRegistryDatabase.getInstance();
        this.setTable();
        dispositionObservable = FXCollections.observableArrayList();
        this.outputTableData();
        this.setAddDispositionToggle();
        this.setLabels();

    }
    private void setLabels(){
        idLabel.setText(order.getId()+"");
        fromLabel.setText(order.getOrigin());
        toLabel.setText(order.getDestination());
        startLabel.setText(order.getOrderDate());
        finishLabel.setText(order.getDeliveryDate());
        trailerLabel.setText(order.getTrailerPlate());
    }
    private void setAddDispositionToggle(){
        radioGroupAddDispositions=new ToggleGroup();
        addInternalRadio.setToggleGroup(radioGroupAddDispositions);
        addExternalRadio.setToggleGroup(radioGroupAddDispositions);
        radioGroupAddDispositions.selectToggle(addInternalRadio);
    }
   /* private void setDispositionTypeToggle(){
        radioGroupViewDispositions=new ToggleGroup();
        internalRadio.setToggleGroup(radioGroupViewDispositions);
        externalRadio.setToggleGroup(radioGroupViewDispositions);
        radioGroupViewDispositions.selectToggle(internalRadio);
    }(*/
    private void setTable() {
        
        idCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("id"));
        startCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("startDate"));
        finishCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("finishDate"));
        originCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("origin"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("destination"));
        carrierCompanyCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("carrierCompany"));
        carrierModeCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("carrierMode"));
        carrierRoleCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("carrierRole"));
        truckPlateCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("truckPlateNumber")); 
        driverIdCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("driverId"));
        driverSurnameCol.setCellValueFactory(new PropertyValueFactory<Disposition, String>("driverSurname"));
                
    }
    public void outputTableData() throws ParseException{
        dispositionObservable.clear();
        ArrayList<Disposition> orderDispositions = dispositionDatabase.getDispositionsOfOrder(order.getId());
        //printList(orderDispositions);
        dispositionObservable = FXCollections.observableArrayList(orderDispositions);
        dispositionTable.setItems(dispositionObservable);
        
    }
    private void printList(ArrayList<Disposition> dispositions){
        System.out.println("LIST SIZE : "+ dispositions.size());
        for(int i = 0; i < dispositions.size(); i++){
            System.out.println(dispositions.get(i).getId());
        }
        
    }
    

    @FXML
    private void handleSearch(ActionEvent event) {
    }

    @FXML
    private void addDisposition(ActionEvent event) throws IOException, ParseException {
        if(radioGroupAddDispositions.getSelectedToggle() == addInternalRadio){
        
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addInternalDisposition.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            addInternalDispositionController controller = fxmlLoader.<addInternalDispositionController>getController();
            controller.setParentController(this);
            controller.setGrandFatherController(parent);
            controller.setOrder(order);
            controller.reinit();

            Scene scene = new Scene(root);
            addInternalDispositionStage = new Stage();
            addInternalDispositionStage.setScene(scene);
            addInternalDispositionStage.centerOnScreen();
            addInternalDispositionStage.setTitle("Add Internal Disposition");
            addInternalDispositionStage.initModality(Modality.WINDOW_MODAL);
            addInternalDispositionStage.initOwner(LoginScreenController.stage);
            addInternalDispositionStage.show();
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addExternalDisposition.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            addExternalDispositionController controller = fxmlLoader.<addExternalDispositionController>getController();
            controller.setParentController(this);
            controller.setGrandFatherController(parent);
            controller.setOrder(order);
            controller.reinit();

            Scene scene = new Scene(root);
            addExternalDispositionStage = new Stage();
            addExternalDispositionStage.setScene(scene);
            addExternalDispositionStage.centerOnScreen();
            addExternalDispositionStage.setTitle("Add External Disposition");
            addExternalDispositionStage.initModality(Modality.WINDOW_MODAL);
            addExternalDispositionStage.initOwner(LoginScreenController.stage);
            addExternalDispositionStage.show();
        }
    }

    @FXML
    private void deleteDisposition(ActionEvent event) throws ParseException {
       Disposition disposition=dispositionTable.getSelectionModel().getSelectedItem(); 
       if (disposition == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a disposition from the table");
            alert.showAndWait();
        }else{
           dispositionDatabase.delete(disposition);
           this.outputTableData();
           parent.outputTableData();
       }
    }

    @FXML
    private void editDisposition(ActionEvent event) throws IOException, ParseException {
        Disposition disposition=dispositionTable.getSelectionModel().getSelectedItem();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date orderDate = this.convertDate(order.getOrderDate());
        Date deliveryDate = this.convertDate(order.getDeliveryDate());
        Date currentDate = this.getCurrentTime();
        if (disposition == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a disposition from the table");
            alert.showAndWait();
        }else if(currentDate.after(orderDate) && currentDate.before(deliveryDate)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("The execution of the transportation of this order "
                    + "has already started. It is not possible to edit an order's "
                    + "route that has already started.");
            alert.showAndWait();
        }else if(currentDate.after(deliveryDate)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("It is not possible to edit the transportation route "
                    + "of a past order.");
            alert.showAndWait();
        }else{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                        getResource("editInternalExternalDisposition.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                editDispositionController controller = fxmlLoader.<editDispositionController>getController();
                controller.setParentController(this);
                controller.setDisposition(disposition);
                controller.setGrandFatherController(parent);
                controller.setOrder(order);
                controller.reinit();

                Scene scene = new Scene(root);
                editExternalDispositionStage = new Stage();
                editExternalDispositionStage.setScene(scene);
                editExternalDispositionStage.centerOnScreen();
                editExternalDispositionStage.setTitle("Edit Disposition");
                editExternalDispositionStage.initModality(Modality.WINDOW_MODAL);
                editExternalDispositionStage.initOwner(LoginScreenController.stage);
                editExternalDispositionStage.show();
             
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
    private void dispositionDetails(ActionEvent event) {
    }
    
    @FXML
    private void viewDispositionMap(ActionEvent event)throws ClassNotFoundException, SQLException, IOException  {
         Disposition disposition=dispositionTable.getSelectionModel().getSelectedItem();
        
        if (disposition == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Disposition from the table");
            alert.showAndWait();
        }else{
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("map.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            MapController controller = fxmlLoader.<MapController>getController();
            controller.setDisposition(disposition);
            controller.reinitDisposition();               

            Scene scene = new Scene(root);
            mapStage = new Stage();
            mapStage.setScene(scene);
            mapStage.centerOnScreen();
            mapStage.setTitle("Disposition Route FROM: "+disposition.getOrigin()+" TO: "+disposition.getDestination());
            mapStage.initModality(Modality.WINDOW_MODAL);
            mapStage.initOwner(LoginScreenController.stage);
            mapStage.show();
        }
    }
    
    public void setOrder(CustomerOrder order){
        this.order=order;
    }
    public void setParentController(CustomerOrdersController parent){
        this.parent = parent;
    }
    
}
