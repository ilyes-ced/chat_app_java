import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.*;
import javafx.scene.control.ScrollPane;
import java.net.*;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Cursor;
import javafx.scene.control.Control;
import javafx.scene.Parent;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

public class Controller  {
	private Socket clientSocket;
	private DataInputStream read_message;
	private DataOutputStream write_message;
	private String username;
	private String email;
    private Scene login;

    @FXML
    private ScrollPane main_scroll_pane;

    @FXML
    private Label username_label;
    @FXML
    private TextField message_content;

    @FXML
    private Button send_message;

    @FXML
    private VBox add_messages;
    
    @FXML
    //private AnchorPane main_message_box;
    private VBox main_message_box;

    @FXML
    private Button submit_message;


    public void send_message_to_server(String input) throws IOException {
		write_message.writeUTF(input);
	}


    public void set_login_scene(Scene scene){
        login = scene;
    }
 
    //



    public void initialize(){
            //@FXML
    //void submit_event(ActionEvent event) throws IOException {
    //    send_message_to_server("hello this is fixed test string ");
    //    //Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    //    //primaryStage.setScene(login);
    //    
    //}
        
        //main_scroll_pane.widthProperty().addListener(new ChangeListener<Number>() {
        //    @Override
        //    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        //        if (newValue.floatValue()!=oldValue.floatValue()) resizeKids(newValue);
        //    }
        //});
        main_scroll_pane.widthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("hi man  im resized to "+newVal);
            main_message_box.setPrefWidth(newVal.doubleValue() - 2.0);
        });
        main_scroll_pane.heightProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("hi man  im resized to "+newVal);
            main_message_box.setPrefHeight(newVal.doubleValue() - 2.0);
        });
        //main_message_box.prefWidthProperty().bind(<parentControl>.USE_PREF_SIZE);
        //System.out.println(parentControl.USE_PREF_SIZE);
        //main_message_box.setPrefWidth(Control.USE_PREF_SIZE);
    }
    public void set_client_socket(Socket mainsocket, String username, String email) {
        try{
            this.username = username;
            this.email = email;
            username_label.setText(username);
            this.clientSocket = mainsocket;
		    this.read_message = new DataInputStream(clientSocket.getInputStream());
		    this.write_message = new DataOutputStream(clientSocket.getOutputStream());
		    this.write_message.writeUTF(username);

            Thread clientThread = new Thread( new Runnable() {
                public void run() {
                    while (true) {
		    	        try {
		    	        	final String recieved_message_username = read_message.readUTF();
		    	        	final String recieved_message = read_message.readUTF();
		    	        	Platform.runLater(new Runnable() {
		    	        		public void run() {
                                    //add it to ui
                                    main_message_box.getChildren().add(new Label(recieved_message_username +" : "+recieved_message));
		    	        			HBox main_message = new HBox();
		    	        			VBox message = new VBox();
		    	        			HBox message_username = new HBox(new Label("username"));
		    	        			HBox message_time = new HBox(new Label("8:30"));



                                    message.getChildren().addAll(message_username, message_time);
                                    
                                    main_message.setStyle("-fx-background-color:  #8544ef"); 
                                    main_message.setPrefWidth(200.0);
                                    main_message.setPrefHeight(200.0);
                                    main_message.setPadding(new Insets(10, 10, 10, 10));
                                    main_message.getChildren().add(message);
                                    System.out.print(recieved_message_username +" : "+recieved_message+"\n");
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


    //@FXML
    //void submit_event(ActionEvent event) throws IOException {
    //    send_message_to_server("hello this is fixed test string ");
    //    //Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
    //    //primaryStage.setScene(login);
    //    
    //}
    
    

    @FXML
    void submit_event_click(ActionEvent event) throws IOException {
        if(!message_content.getText().equals("")){
            send_message_to_server(message_content.getText());
            message_content.setText("");
        }
    }

       @FXML
    void submit_event(KeyEvent event) throws IOException {
        if(event.getCode().toString().equals("ENTER")){
            send_message_to_server(message_content.getText());
            submit_message.setStyle("-fx-background-color: transparent;");
        }
        if(!message_content.getText().equals("")){
            submit_message.setStyle("-fx-background-color: linear-gradient(to right bottom, rgba(143,10,228,1) 6%, rgba(103,21,235,1) 55%, rgba(143,10,228,1) 100%);");
            submit_message.setCursor(Cursor.HAND);
        }else{
            submit_message.setStyle("-fx-background-color: transparent;");
        }
    }

}

