
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.*;
import java.net.*;
import java.util.Scanner;



public class Client extends Application {
    //static Controller myControllerHandle;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("client_ui.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("client_ui.fxml"));
        //Parent root = loader.load();
        //myControllerHandle = (Controller)loader.getController();
        
        primaryStage.setTitle("Registration Form FXML Application");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }
    



    public static void main(String[] args) throws Exception{
        launch(args);
        Scanner s = new Scanner(System.in);
        System.out.print("Enter your nick: ");
        String name = s.nextLine();
        Socket socket = new Socket("localhost", 5555);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true)                            
                        System.out.println(dis.readUTF());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        dos.writeUTF(name + " has joined the conversation");
        while (true)   
            dos.writeUTF(name + ": " + s.nextLine());
    }
}



/*
import java.io.*;
import java.net.*;
import java.util.Scanner;






public class Client {
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter your nick: ");
        String name = s.nextLine();
        Socket socket = new Socket("localhost", 5555);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true)                            
                        System.out.println(dis.readUTF());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        dos.writeUTF(name + " has joined the conversation");
        while (true)        
                                             
            dos.writeUTF(name + ": " + s.nextLine());
    }
}*/