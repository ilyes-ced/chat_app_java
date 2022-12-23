
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
        System.out.print("//////starting ui////////////// \n");
        launch(args);
        System.out.print("//////starting server////////////// \n");

        ServerSocket server = new ServerSocket(5555);
        List<DataOutputStream> doss = new ArrayList<>();
        while (true) {
            Socket client = server.accept();
            System.out.print("//////user connected////////////// \n");
            synchronized(doss) {
                doss.add(new DataOutputStream(client.getOutputStream()));
            }
            new Thread(new Runnable() {
                public void run() {
                    try (DataInputStream dis = new DataInputStream(client.getInputStream())) {
                        while(true){
                            String message=dis.readUTF();
                            System.out.print("reciever : "+message+"///////////////////////");
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
            Sql_connection db = new Sql_connection();
            String[] params = {email.getText(), password.getText()};
            ResultSet result = db.select_query("SELECT  * from users where email=? and password=?", params);
            while (result.next()) {
                System.out.println(result.getString("username"));
            }
            db.closeConnection();
*/


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
