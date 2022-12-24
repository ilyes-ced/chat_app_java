import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class Controller  {
	private Socket clientSocket;
	private BufferedReader read_message;
	private PrintWriter write_message;
	private String name = "starter name";
    private Scene login;




    public void initialize() {
        try{
            clientSocket = new Socket("localhost", 5000);
		    read_message = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    write_message = new PrintWriter(clientSocket.getOutputStream(), true);
		    //chatLog = FXCollections.observableArrayList();
		    this.name = name;
		    write_message.println(name);

            Thread clientThread = new Thread( new Runnable() {
                public void run() {
                    while (true) {
		    	        try {
		    	        	final String inputFromServer = read_message.readLine();
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
		    clientThread.setDaemon(true);
		    clientThread.start();
        }catch (Exception ex) {
            ex.printStackTrace();
        }

		
    
    }

    public void send_message_to_server(String input) {
		write_message.println(name + " : " + input);
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
    private AnchorPane scroll_pane_inside;

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
    void submit_event(ActionEvent event) {
        send_message_to_server("hello this is fixed test string ");
    }

}


