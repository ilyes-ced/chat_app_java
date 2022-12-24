import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.application.Platform;
import java.io.*;
import java.net.*;

public class Controller  {

    private int port = 5555;
	private ServerSocket socket;
	private ArrayList<Socket> clients;
	private ArrayList<Client_thread> client_threads;
    static Controller myControllerHandle;

    public void initialize() {
        try{
            
            ServerSocket server = new ServerSocket(5555);
            Server server = new Server(port);
			Thread serverThread = (new Thread(server));
			serverThread.setName("Server Thread");
			serverThread.setDaemon(true);
			serverThread.start();
			threads.add(serverThread);
			/* Change the view of the primary stage */
			primaryStage.hide();
			primaryStage.setScene(makeServerUI(server));
			primaryStage.show();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
   @FXML
    private TextField message_content;

    @FXML
    private Button send_message;

    @FXML
    private VBox add_messages;

    @FXML
    void clicked(ActionEvent event) throws IOException {
        dos.writeUTF(message_content.getText());
    }


    @FXML
    void enter_message(KeyEvent event) throws IOException {
        if(event.getCode().toString().equals("ENTER")){
            dos.writeUTF(message_content.getText());
        }
    }

}


/**
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
 */