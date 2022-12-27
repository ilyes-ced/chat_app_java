import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class Controller  {
	private Socket clientSocket;
	private DataInputStream read_message;
	private DataOutputStream write_message;
	private String name = "starter name";
    private Scene login;




    public void initialize() {
        try{
            this.clientSocket = new Socket("localhost", 5000);
		    this.read_message = new DataInputStream(clientSocket.getInputStream());
		    this.write_message = new DataOutputStream(clientSocket.getOutputStream());
		    //chatLog = FXCollections.observableArrayList();
		    this.name = name;
		    this.write_message.writeUTF(name);

            Thread clientThread = new Thread( new Runnable() {
                public void run() {
                    while (true) {
		    	        try {
		    	        	final String inputFromServer = read_message.readUTF();
		    	        	Platform.runLater(new Runnable() {
		    	        		public void run() {
                                    //add it to ui
		    	        			System.out.print(inputFromServer);
		    	        		}
		    	        	});
		    	        } catch (SocketException e) {
		    	        	Platform.runLater(new Runnable() {
		    	        		public void run() {
                                    //add to ui err
		    	        			System.out.print("Error in server");
		    	        		}
		    	        	});
		    	        	break;
		    	        } catch (IOException e) {
		    	        	e.printStackTrace();
		    	        }
		            }
                }

            });
		    //clientThread.setDaemon(true);
		    clientThread.start();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void send_message_to_server(String input) throws IOException {
        System.out.print("sent messafe \n");
		write_message.writeUTF(name + " : " + input);
	}


    public void set_login_scene(Scene scene){
        login = scene;
    }

 

   @FXML
    private TextField message_content;

    @FXML
    private Button send_message;

    @FXML
    private VBox add_messages;
    
    @FXML
    //private AnchorPane scroll_pane_inside;
    private VBox scroll_pane_inside;

    @FXML
    private Button submit_message;

 

    //@FXML
    //void submit_event(ActionEvent event) throws IOException {
    //    send_message_to_server("hello this is fixed test string ");
    //    //Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    //    //primaryStage.setScene(login);
    //    
    //}

    @FXML
    void submit_event_click(ActionEvent event) throws IOException {
        send_message_to_server(message_content.getText());
    }

       @FXML
    void submit_event(KeyEvent event) throws IOException {
        if(event.getCode().toString().equals("ENTER")){
            send_message_to_server(message_content.getText());
        }
    }

}

