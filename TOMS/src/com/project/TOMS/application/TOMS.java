/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.TOMS.application;

import com.project.TOMS.view.Authentication.LoginScreenController;
import com.project.TOMS.controller.systemUserRecords.SystemUserRegistryDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author EfthymiosChatziathanasiadis
 */
public class TOMS extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader login = new FXMLLoader(getClass().getResource("/com/project/TOMS/view/Authentication/loginScreen.fxml"));
        Parent root = (Parent) login.load();

        LoginScreenController loginController = login.<LoginScreenController>getController();
        SystemUserRegistryDatabase userRegistry = SystemUserRegistryDatabase.getInstance();
        loginController.initModel(userRegistry);
        
        Scene scene = new Scene(root);
        this.stage = stage;
        stage.setScene(scene);
        stage.setTitle("Login Menu");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
