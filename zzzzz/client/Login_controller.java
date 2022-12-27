import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Login_controller  {
    private Scene main;
    private Scene register;




    public void initialize() {
        
    }



    public void set_main_scene(Scene scene){
        main = scene;
    }

    public void set_register_scene(Scene scene){
        register = scene;
    }
    
    @FXML
    private CheckBox remember_input;

    @FXML
    private Button cancel_button;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button submit_button;

    @FXML
    void register_form(ActionEvent event) {       
        System.out.println("username");
        try{
            Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            primaryStage.setScene(register);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void submit_form(ActionEvent event) {
       try{
            Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            primaryStage.setScene(main);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    

}
