import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.*;
import java.net.*;
import java.util.*;




public class Server extends Application {
    static Controller myControllerHandle;

    private Socket client;
    private ServerSocket server;
    private ObjectInputStream dis2;
    private List<DataOutputStream> doss ;




    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("client_ui.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("server_ui.fxml"));
        Parent root = loader.load();
        myControllerHandle = (Controller)loader.getController();
        
        primaryStage.setTitle("Registration Form FXML Application");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }
    



    public static void main(String[] args) throws Exception{
        launch(args);
        

        server = new ServerSocket(5555);
        doss = new ArrayList<>();
        while (true) {
            client = server.accept();
            dis2 = new ObjectInputStream(client.getInputStream());
            final Map<String, String> message_object = (Map)dis2.readObject();
            if(message_object.get("method") == "start_chat"){
                start_new_thread();
            }else if(message_object.get("method") == "start_chat"){
                System.out.print("//////login attempt////////////// \n");
            }
        }
    }



    public void start_new_thread() throws IOException{
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

    public void login() throws IOException{
        
    }
    public void register() throws IOException{
        
    }





}
