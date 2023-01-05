package org.openjfx;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;
import java.io.*;
import java.net.*;
import java.util.Scanner;



public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        URL fxmlLocation = App.class.getResource("fxml/login.fxml");
        System.out.println(fxmlLocation);
        FXMLLoader login_page_loader = new FXMLLoader(App.class.getResource("fxml/login.fxml"));
        Parent login_pane = login_page_loader.load();
        Scene login_scene = new Scene(login_pane);
        
        Login_controller login_controller = (Login_controller) login_page_loader.getController();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
            }
        }); 
        primaryStage.setTitle("java chat group application");
        primaryStage.setScene(login_scene);
        primaryStage.show();
    }
    



    public static void main(String[] args) throws Exception{
        launch(args);
    }


}