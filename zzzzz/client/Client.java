
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
        FXMLLoader login_page_loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent login_pane = login_page_loader.load();
        Scene login_scene = new Scene(login_pane);
        
        Login_controller login_controller = (Login_controller) login_page_loader.getController();

        primaryStage.setTitle("java chat group application");
        primaryStage.setScene(login_scene);
        primaryStage.show();
    }
    



    public static void main(String[] args) throws Exception{
        launch(args);
    }


}