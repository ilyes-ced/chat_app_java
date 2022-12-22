import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
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
        System.out.print(message_content.getText());
        try{
            dos.writeUTF(message_content.getText());
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    void enter_message(KeyEvent event) {
        if(event.getCode().toString().equals("ENTER")){
            System.out.print("test \n");
            try{
                dos.writeUTF(message_content.getText());
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}


