
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;



public class Server extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("server_ui.fxml"));
        primaryStage.setTitle("Registration Form FXML Application");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }
    //test test

    public static void main(String[] args) throws Exception {
        launch(args);
        ServerSocket server = new ServerSocket(5555);
        List<DataOutputStream> doss = new ArrayList<>();
        while (true) {
            Socket client = server.accept();
            synchronized(doss) {
                doss.add(new DataOutputStream(client.getOutputStream()));
            }
            new Thread(new Runnable() {
                public void run() {
                    try (DataInputStream dis = new DataInputStream(client.getInputStream())) {
                        while(true){
                            String message=dis.readUTF();
                            synchronized (doss) {
                                for (DataOutputStream dos : doss)
                                    dos.writeUTF(message);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    }
}



/*




import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(5555);
        List<DataOutputStream> doss = new ArrayList<>();
        while (true) {
            Socket client = server.accept();
            synchronized(doss) {
                doss.add(new DataOutputStream(client.getOutputStream()));
            }
            new Thread(new Runnable() {
                public void run() {
                    try (DataInputStream dis = new DataInputStream(client.getInputStream())) {
                        while(true){
                            String message=dis.readUTF();
                            synchronized (doss) {
                                for (DataOutputStream dos : doss)
                                    dos.writeUTF(message);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

*/
