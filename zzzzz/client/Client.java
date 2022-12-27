
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.*;
import java.net.*;
import java.util.Scanner;



public class Client extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader main_page_loader = new FXMLLoader(getClass().getResource("new_client_ui.fxml"));
        Parent main_pane = main_page_loader.load();
        Scene main_scene = new Scene(main_pane, 1200, 800);

        FXMLLoader login_page_loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent login_pane = login_page_loader.load();
        Scene login_scene = new Scene(login_pane, 1200, 800);

        FXMLLoader register_page_loader = new FXMLLoader(getClass().getResource("register.fxml"));
        Parent register_pane = register_page_loader.load();
        Scene register_scene = new Scene(register_pane, 1200, 800);


        Login_controller login_controller = (Login_controller) login_page_loader.getController();
        Register_controller register_controller = (Register_controller) register_page_loader.getController();
        Controller main_controller = (Controller) main_page_loader.getController();

        login_controller.set_main_scene(main_scene);
        register_controller.set_main_scene(main_scene);
        login_controller.set_register_scene(register_scene);

        //fix for logout
        //main_controller.set_login_scene(login_scene);

        
        //primaryStage.setTitle("Registration Form FXML Application");
        //primaryStage.show();

        primaryStage.setTitle("Switching scenes");
        primaryStage.setScene(login_scene);
        primaryStage.show();
    }
    



    public static void main(String[] args) throws Exception{
        launch(args);
    }


}