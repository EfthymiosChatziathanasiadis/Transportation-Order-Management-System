/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.view.SystemUserRecords;

import com.project.TOMS.algorithms.dijkstra.controller.graphRegistryDatabase;
import com.project.TOMS.algorithms.dijkstra.model.Edge;
import com.project.TOMS.algorithms.dijkstra.model.Graph;
import com.project.TOMS.view.Authentication.LoginScreenController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author EfthymiosChatziathanasiadis
 */
public class DispositionsEdgesController implements Initializable {

    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Edge> edgesTable;
    @FXML
    private TableColumn<Edge, String> idCol;
    @FXML
    private TableColumn<Edge, String> sourceCol;
    @FXML
    private TableColumn<Edge, String> destCol;
 
    private ObservableList<Edge> edgeObservable;
    private graphRegistryDatabase graphDatabase;
    public Stage edgeStage;
  

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        edgeObservable = FXCollections.observableArrayList();
        graphDatabase = graphRegistryDatabase.getInstance();
        this.setTable();
        this.outputTableData();
    }    
    public void outputTableData() {
        edgeObservable.clear();
        Graph graph = graphDatabase.getGraph();
        ArrayList<Edge> edges = graph.getEdges();
        edgeObservable = FXCollections.observableArrayList(edges);
        edgesTable.setItems(edgeObservable);   
    }
    private void setTable() {
        
        idCol.setCellValueFactory(new PropertyValueFactory<Edge, String>("id"));
        sourceCol.setCellValueFactory(new PropertyValueFactory<Edge, String>("src"));
        destCol.setCellValueFactory(new PropertyValueFactory<Edge, String>("dest"));
        
    }
    @FXML
    private void addEdge(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/TOMS/view/SystemUserRecords/addEdge.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        addEdgeController controller = fxmlLoader.<addEdgeController>getController();
        controller.setParentController(this);
        Scene scene = new Scene(root);
        edgeStage = new Stage();
        edgeStage.setScene(scene);
        edgeStage.centerOnScreen();
        edgeStage.setTitle("ADD NEW EDGE - PATH");
        edgeStage.initModality(Modality.WINDOW_MODAL);
        edgeStage.initOwner(LoginScreenController.stage);
        edgeStage.show();
    }

    @FXML
    private void deleteHoliday(ActionEvent event) {
    }

    
    
}
