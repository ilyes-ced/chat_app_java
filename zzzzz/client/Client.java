
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
        Scene main_scene = new Scene(main_pane, 800, 500);


        FXMLLoader login_page_loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent login_pane = login_page_loader.load();
        Scene login_scene = new Scene(login_pane, 800, 500);



        //FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        //Parent root = loader.load();
        //primaryStage.setScene(new Scene(root));


        Login_controller login_controller = (Login_controller) login_page_loader.getController();
        login_controller.set_main_scene(main_scene);
        Controller main_controller = (Controller) main_page_loader.getController();
        main_controller.set_login_scene(login_scene);

        
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