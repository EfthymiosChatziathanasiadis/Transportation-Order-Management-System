/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.mainView;

import com.project.TOMS.controller.CustomerOrders.CustomerOrderRegistryDatabase;
import com.project.TOMS.controller.systemUserRecords.SystemUserRegistryDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import com.project.TOMS.model.systemUserRecords.SystemUser;
import com.project.TOMS.model.systemUserRecords.TransportPlanner;
import com.project.TOMS.view.Authentication.LoginScreenController;
import com.project.TOMS.view.CarrierRecords.CarriersController;
import com.project.TOMS.view.CustomerOrders.CustomerOrdersController;
import com.project.TOMS.view.CustomerRecords.CustomersController;
import com.project.TOMS.view.Drivers.DriversController;
import com.project.TOMS.view.SystemUserRecords.SystemUsersController;
import com.project.TOMS.view.Vehicles.VehiclesController;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */


public class mainScreenController implements Initializable {
   @FXML
   private Tab adminTab;

   @FXML
   private TabPane pane;
      
   private Map<String, Object> moduleControllers = new HashMap<String, Object>();
   
   @FXML 
   private javafx.scene.control.Button closeButton;
   
   private SystemUser  user;
   
   private Parent root;

   
    @Override
   public void initialize(URL url, ResourceBundle rb){
             pane.getSelectionModel().clearSelection();
             pane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

             @Override
             public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                 FXMLLoader fXMLLoader = new FXMLLoader();
                 if (newValue.getContent() == null) {
                     try {
                         // Loading content on demand
                         fXMLLoader.setLocation(this.getClass().getResource(newValue.getId() + ".fxml"));
                         //root = (Parent) fXMLLoader.load(this.getClass().getResource(newValue.getId() + ".fxml").openStream());
                         root = (Parent) fXMLLoader.load();
                         newValue.setContent(root);

                         // Store the controller to be used later for calling refresh methods for each module selected on demand.
                         moduleControllers.put(newValue.getId(), fXMLLoader.getController());

                     } catch (IOException ex) {
                         ex.printStackTrace();
                     }
                 } else {
                     // Content is already loaded. Update/refresh methods of each module will be called.
                     root = (Parent) newValue.getContent();
                     //get the controller from Map and manipulate and refresh table views of the selected module
                     // via its controller.
                     Object module=moduleControllers.get(newValue.getId());

                     if(module instanceof VehiclesController){
                         VehiclesController veh=(VehiclesController)module;
                         
                         try {
                             veh.outputTrailerTable();
                             veh.outputTruckTable();
                         } catch (ParseException ex) {
                             Logger.getLogger(mainScreenController.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }else if(module instanceof DriversController){
                         DriversController drivers=(DriversController)module;
                         try {
                             drivers.outputTableData();
                         } catch (ParseException ex) {
                             Logger.getLogger(mainScreenController.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }else if(module instanceof CustomersController){
                         CustomersController customers=(CustomersController)module;
                         customers.outputTableData();
                     }else if(module instanceof SystemUsersController){
                         SystemUsersController users=(SystemUsersController)module;
                         users.outputTableData();
                     }else if(module instanceof CarriersController){
                         CarriersController carriers=(CarriersController)module;
                         carriers.outputTableData();
                     }else if(module instanceof CustomerOrdersController){
                         CustomerOrdersController orders=(CustomerOrdersController)module;
                         try {
                             orders.outputTableData();
                         } catch (ParseException ex) {
                             Logger.getLogger(mainScreenController.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }else{
                         
                     }

                 }
             }
         });
     // By default, select 1st tab and load its content.
        pane.getSelectionModel().selectFirst();

       
       
   }
   public void reinit(){
       if(user instanceof TransportPlanner)adminTab.setDisable(true);
   }
    
    @FXML
    public void logout() throws IOException{
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            
            FXMLLoader login = new FXMLLoader(getClass().getResource("/com/project/TOMS/view/Authentication/loginScreen.fxml"));
            Parent root = (Parent) login.load();
            
            LoginScreenController loginController = login.<LoginScreenController>getController();
            SystemUserRegistryDatabase userRegistry = SystemUserRegistryDatabase.getInstance();
            loginController.initModel(userRegistry);
            
            Scene scene = new Scene(root);
            Stage stag = new Stage();
            stag.setScene(scene);
            stag.centerOnScreen();
            stag.setTitle("Login Menu");
            stag.show(); 
         
    }
    
    public void setUser(SystemUser user){
        this.user=user;
    }
    

    
}
