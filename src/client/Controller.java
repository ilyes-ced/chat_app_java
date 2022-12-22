import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.*;
import java.net.*;

public class Controller  {


    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public void initialize() {
        try{
            socket = new Socket("localhost", 5555);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true)                            
                            System.out.println(dis.readUTF());
                            // here we inject the ui elements
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
            dos.writeUTF("has joined the conversation");
        }catch (Exception ex) {
                    ex.printStackTrace();
        }
    }
   @FXML
    private TextField message_content;

    @FXML
    private Button send_message;

    @FXML
    void clicked(ActionEvent event) {
        System.out.print("hellefsfsefsefoe");
        System.out.print(message_content.getText());
    }

}

