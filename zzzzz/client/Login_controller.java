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




    public void initialize() throws IOException {


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
            login_error_message.setText("");
            Socket client_socket = new Socket("localhost", 6000);
            DataInputStream read_message = new DataInputStream(client_socket.getInputStream());
            DataOutputStream write_message = new DataOutputStream(client_socket.getOutputStream());
            System.out.println("sendt");
            write_message.writeUTF(login_email.getText());
            write_message.writeUTF(login_password.getText());
            System.out.println("test \n");
            String response = read_message.readUTF();
            System.out.println("test \n");
            System.out.println(response);
            if(response.equals("success")){
                Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                primaryStage.setScene(main);
            }else if(response.equals("password_error")){
                login_error_message.setText("wrong password");
            }else if(response.equals("email_error")){
                login_error_message.setText("this account does not exist");
            }else if(response.equals("connection_error")){
                login_error_message.setText("network erro please check your connection");
            }
       }else{
            login_error_message.setText("email and password required");
       }
    }

    @FXML
    void submit_register(ActionEvent event) throws IOException  {
        if (!register_username.getText().trim().isEmpty() && !register_email.getText().trim().isEmpty() && !register_password.getText().trim().isEmpty()) {
            register_error_message.setText("");
            register_error_message.setStyle("-fx-text-fill: red;");
            Socket client_socket = new Socket("localhost", 7000);
            DataInputStream read_message = new DataInputStream(client_socket.getInputStream());
            DataOutputStream write_message = new DataOutputStream(client_socket.getOutputStream());
            System.out.println("sendt");
            write_message.writeUTF(register_username.getText());
            write_message.writeUTF(register_email.getText());
            write_message.writeUTF(register_password.getText());
            String response = read_message.readUTF();
            System.out.println(response);
            if(response.equals("success")){
                register_error_message.setStyle("-fx-text-fill: green;");
                register_error_message.setText("registration successful, you can login now");
            }else if(response.equals("account_duplicate")){
                register_error_message.setText("account already exists");
            }else if(response.equals("connection_error")){
                register_error_message.setText("network erro please check your connection");
            }
        }else{
            register_error_message.setText("username, email and password required");
       }
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
