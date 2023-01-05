package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import java.util.*;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/server_ui.fxml"));
        Parent root = loader.load();
        
        Controller controller = (Controller) loader.getController();


        primaryStage.setTitle("Server application");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();

        
    }
    



    public static void main(String[] args) throws Exception{
        launch(args);
    }
}
