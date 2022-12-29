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
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.KeyValue;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.animation.Animation;
import javafx.scene.transform.Rotate;
import javafx.scene.effect.Bloom;
public class Login_controller  {
    private Scene main;
    private Scene register;
	private Socket login_client_socket;
    private int co = 0;

    public void animate_button (Button btn) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(103,21,235, 0.5));
        shadow.setSpread(0.75);
        Timeline shadowAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(shadow.radiusProperty(), 0d)),
            new KeyFrame(Duration.seconds(0.15), new KeyValue(shadow.radiusProperty(), 20d))
        );
        shadowAnimation.setAutoReverse(true);
        shadowAnimation.setCycleCount(2);
        Bloom bloom = new Bloom();
        bloom.setThreshold(0.3);
        btn.setEffect(shadow);
        btn.setEffect(bloom);
        shadowAnimation.setOnFinished(evt -> btn.setEffect(bloom));
        //shadowAnimation.play();
    }


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
    void login_button_hover(MouseEvent event) {
        animate_button(submit_login_button);
        //Bloom bloom = new Bloom();
        //bloom.setThreshold(0.3);
        //submit_login_button.setEffect(bloom);
    }
    @FXML
    void login_button_hover_stop(MouseEvent event) {
        submit_login_button.setEffect(null);
    }

    @FXML
    void register_button_hover(MouseEvent event) {
        animate_button(submit_register_button);
    }
    @FXML
    void register_button_hover_stop(MouseEvent event) {
        submit_register_button.setEffect(null);
    }













    

    @FXML
    void toggle_forms(MouseEvent event) {

        //cover.setStyle("-fx-translate-x: 200;");
        //cover.setVisible(true);
        //cover.setLayoutX(cover.getLayoutX() - 200);
        //-fx-background-radius
        //cover.setStyle("-fx-background-radius: 10 10 10 10"); 
        //cover.setStyle("-fx-border-radius: 10 10 10 10"); 
        //cover.setStyle("-fx-background-color: #6715eb"); 
        TranslateTransition transition = new TranslateTransition(new Duration(350), cover);
        if(co == 0){
            //cover.setStyle("-fx-background-radius: 10px");
            //cover.getTransforms().add(new Rotate(0, 1, 0, 180));
            transition.setToX(-800);
            transition.play();
            this.co = co + 1;
        }else{
            //cover.setVisible();
            //cover.setStyle("-fx-background-radius: 10 0 0 10;");
            //cover.setStyle("-fx-background-radius: 10px");
            //cover.getTransforms().add(new Rotate(0, 1, 0, 180));
            transition.setToX(-400);
            transition.play();
            this.co = co - 1;
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
            write_message.writeUTF(login_email.getText());
            write_message.writeUTF(login_password.getText());
            String response = read_message.readUTF();
            String username = read_message.readUTF();
            System.out.println(response + "\n");
            System.out.println(username + "\n");
            //ffffffffffffffffffffffffffffffffff
            if(response.equals("success")){
                FXMLLoader main_page_loader = new FXMLLoader(getClass().getResource("new_client_ui.fxml"));
                Parent main_pane = main_page_loader.load();
                Scene main_scene = new Scene(main_pane);
                Controller main_controller = (Controller) main_page_loader.getController();
                main_controller.set_client_socket(client_socket,username ,login_email.getText());
                Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow(); 
                primaryStage.setScene(main_scene);
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
            write_message.writeUTF(register_username.getText());
            write_message.writeUTF(register_email.getText());
            write_message.writeUTF(register_password.getText());
            String response = read_message.readUTF();
            System.out.println(response+"\n");
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
