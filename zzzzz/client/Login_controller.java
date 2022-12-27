import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class Login_controller  {
    private Scene main;
    private Scene register;
	private Socket login_client_socket;
	private DataInputStream read_message;
	private DataOutputStream write_message;




    public void initialize() throws IOException {
        this.login_client_socket = new Socket("localhost", 6000);
        this.read_message = new DataInputStream(login_client_socket.getInputStream());
        this.write_message = new DataOutputStream(login_client_socket.getOutputStream());
    }
    
    public void set_main_scene(Scene scene){
        main = scene;
    }

    public void set_register_scene(Scene scene){
        register = scene;
    }
    





    @FXML
    private VBox cover;

    @FXML
    private TextField login_email;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField register_email;

    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_username;

    @FXML
    private Label show_login_form_button;

    @FXML
    private Label register_error_message;

    @FXML
    private Label login_error_message;

    @FXML
    private Label show_register_form_button;

    @FXML
    private Button submit_login_button;

    @FXML
    private Button submit_register_button;


    

    @FXML
    void show_register_form(MouseEvent event) {
        System.out.println("username");
        if(cover.getLayoutX() == 800){
            for (int i = 0; i < 10; i++) {
                cover.setLayoutX(cover.getLayoutX() - 40);
            }
        }else{
            for (int i = 0; i < 10; i++) {
                cover.setLayoutX(cover.getLayoutX() + 40);
            }
        }
    }

    @FXML
    void show_login_form(MouseEvent event) {

    }

    @FXML
    void submit_login(ActionEvent event) throws IOException {
        if (!login_email.getText().trim().isEmpty() && !login_password.getText().trim().isEmpty()) {
            write_message.writeUTF(login_email.getText());
            write_message.writeUTF(login_password.getText());
       }
    }

    @FXML
    void submit_register(ActionEvent event) {

    }




    //void register_form(ActionEvent event) {       
    //    System.out.println("username");
    //    try{
    //        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    //        primaryStage.setScene(register);
    //    } catch (Exception ex) {
    //        ex.printStackTrace();
    //    }
    //}

    //@FXML
    //void submit_form(ActionEvent event) {
    //   try{
    //        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    //        primaryStage.setScene(main);
    //    } catch (Exception ex) {
    //        ex.printStackTrace();
    //    }
    //}
    

}
