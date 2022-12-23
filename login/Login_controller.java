import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;


public class Login_controller {
    
    @FXML
    private CheckBox remember_input;

    @FXML
    private Button cancel_button;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button submit_button;

    @FXML
    void cancel_form(ActionEvent event) {
        System.out.print( email.getText() );
        
    }

    @FXML
    void submit_form(ActionEvent event) {
       try{
            Socket socket = new Socket("localhost", 5555);
            DataInputStream dis= new DataInputStream(socket.getInputStream());
            ObjectOutputStream dos= new ObjectOutputStream(socket.getOutputStream());


            Map<String, String> data = new HashMap<String, String>();
            data.put("method", "login");
            data.put("email", email.getText());
            data.put("password", password.getText());


            dos.writeObject(data);
            //String str = data.toString();
            //dos.writeUTF(str);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

}
