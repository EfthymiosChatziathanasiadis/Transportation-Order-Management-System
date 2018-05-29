package com.project.TOMS.view.Authentication;

import com.project.TOMS.view.mainView.mainScreenController;
import com.project.TOMS.application.TOMS;
import com.project.TOMS.controller.Authentication.Authentication;
import com.project.TOMS.controller.systemUserRecords.SystemUserRegistryDatabase;
import com.project.TOMS.model.systemUserRecords.SystemUser;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginScreenController implements Initializable {

    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessage;
    @FXML
    private Button loginButton;

    public Parent root;

    public static Stage stage;

    SystemUserRegistryDatabase userRegistry;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    loginButton.fire();
                }
            }
        });
    }
    public void initModel(SystemUserRegistryDatabase userRegistry){
        if(this.userRegistry != null){
                throw new IllegalStateException("Model can only be initialized once");        
        }
        this.userRegistry = userRegistry;

    }
    /**
     *
     * @param event
     */
    @FXML
    public void login(ActionEvent event) {

        Authentication authenticationController = new Authentication(userRegistry);
        boolean authenticity = authenticationController.authenticate(userField.getText(), passwordField.getText());

        if (authenticity) {
            Stage stag = (Stage) loginButton.getScene().getWindow();
            stag.close();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/TOMS/view/mainView/mainScreen.fxml"));
                root = (Parent) fxmlLoader.load();

                mainScreenController controller = fxmlLoader.<mainScreenController>getController();
                SystemUser user = userRegistry.getCusrrentUser();
                controller.setUser(user);
                controller.reinit();

                Scene scene = new Scene(root);
                stage = TOMS.stage;
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.setTitle("Main Menu");
                stage.setResizable(false);
                stage.sizeToScene();
                stage.show();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            errorMessage.setText("Invalid user/password");

        }

    }

}
