package org.openjfx;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.*;
import javafx.scene.control.ScrollPane;
import java.net.*;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Cursor;
import javafx.scene.control.Control;
import javafx.scene.Parent;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.geometry.Pos;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
//import javafx.scene.Scene.setFill;



public class Controller  {
	private Socket client_socket;
	private DataInputStream read_message;
	private DataOutputStream write_message;
	private String username;
	private String email;
    private Scene login;

    
    @FXML
    private ScrollPane main_scroll_pane;

    @FXML
    private MenuButton username_label;
    @FXML
    private TextField message_content;

    @FXML
    private Button send_message;

    @FXML
    private VBox add_messages;

    @FXML
    private VBox list_of_users;
    
    @FXML
    private VBox main_message_box;

    @FXML
    private Button submit_message;
    @FXML
    private MenuItem logout_item;
    
 

    public void close_connection() {
        System.out.println("closedd inside th controller methid");
        try{
            this.client_socket.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void send_message_to_server(String input) throws IOException {
		write_message.writeUTF(input);
	}


    public void set_login_scene(Scene scene){
        login = scene;
    }
 
    



    public void initialize(){
        main_scroll_pane.widthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("hi man  im resized to "+newVal);
            main_message_box.setPrefWidth(newVal.doubleValue() - 2.0);
        });
        main_scroll_pane.heightProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("hi man  im resized to "+newVal);
            main_message_box.setPrefHeight(newVal.doubleValue() - 2.0);
        });
        main_message_box.heightProperty().addListener(observable -> main_scroll_pane.setVvalue(1D));
        main_scroll_pane.getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
        message_content.setStyle("-fx-text-fill: white;"+message_content.getStyle());
    }








    public void set_client_socket(Socket main_socket, String username, String email) {
        try{
            this.username = username;
            this.email = email;
            username_label.setText(username);
            this.client_socket = main_socket;
		    this.read_message = new DataInputStream(client_socket.getInputStream());
		    this.write_message = new DataOutputStream(client_socket.getOutputStream());
            Thread clientThread = new Thread( new Runnable() {
                public void run() {
                    while (true) {
		    	        try {
		    	        	String recieved_message_username = read_message.readUTF();
		    	        	String recieved_message = read_message.readUTF();
		    	        	String recieved_message_time = read_message.readUTF();
                            if( recieved_message_username.equals("&B3#aVEyvj#@WqKCTpPfu5d+yneVycy*qhkCh94kqg#3#@Sz66vHn)FA#shFfPpJ&B3#aVEyvj#@WqKCTpPfu5d+yneVycy*qhkCh94kqg#3#@Sz66vHn)FA#shFfPpJ")){
                                if(recieved_message.equals("QHX)w+#T4WatEZHyaL(8kzdRFS$ezJ2DLWnzT&wy*n*bhLFAE!heC2+YL%2jaP(d4IEsEm$cPye^aqVUs6G85e$z$L)ue+fv9U+WpYG)@U93a^jN*z)+bPstFvPSVVXM")){
                                    Platform.runLater(new Runnable() {
		    	        	            public void run() {
                                            //removec from list
                                            
                                            System.out.println(list_of_users.getChildren().get(0));
                                         
                                            
                                         
                                            Label username_ll = new Label(recieved_message_time + " left the chat");
                                            username_ll.setStyle("-fx-text-fill: white;");
                                            HBox new_user_notification = new HBox(username_ll );
                                            new_user_notification.setPadding(new Insets(10, 10, 10, 10));
                                            new_user_notification.setAlignment(Pos.CENTER);
                                            new_user_notification.setStyle(" -fx-background-color: #8544ef; -fx-border-color: rgba(200,200,200,0.4);");
                                            new_user_notification.setPrefHeight(30.0);
                                            new_user_notification.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                            main_message_box.getChildren().add(new_user_notification);
                                        }
                                    });
                                }else if(recieved_message.equals("UmlOcZueqgpO89ecoH232na5GrHKEP6Kr8uipTSjS9HVTBEpkAQRGJBvi7X50WDpkLkWtaQ0gik1voPxBBphbh0eSKuZAJuBlYpWB9jGKzYsU5uB3AVU5A5L95ugeHBp")){
                                    System.out.print("//////////////////////////////////////////////////");
                                    Label new_user_label = new Label(recieved_message_time);
                                    new_user_label.setStyle("-fx-text-fill: white;");
		    	        	        HBox new_user = new HBox(new_user_label);
                                    new_user.setPadding(new Insets(0, 0, 0, 10));
                                    new_user.setPrefHeight(30.0);
                                    new_user.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                    new_user.setAlignment(Pos.CENTER_LEFT);
                                    list_of_users.getChildren().add(new_user);
                                }else{
                                    Platform.runLater(new Runnable() {
		    	        	            public void run() {
                                            Label new_user_label = new Label(recieved_message);
                                            new_user_label.setStyle("-fx-text-fill: white;");
		    	        	            	HBox new_user = new HBox(new_user_label);
                                            new_user.setPadding(new Insets(0, 0, 0, 10));
                                            new_user.setPrefHeight(30.0);
                                            new_user.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                            new_user.setAlignment(Pos.CENTER_LEFT);
                                            list_of_users.getChildren().add(new_user);

                                            Label username_ll = new Label(recieved_message + " joined the chat");
                                            username_ll.setStyle("-fx-text-fill: white;");
                                            HBox new_user_notification = new HBox(username_ll );
                                            new_user_notification.setPadding(new Insets(10, 10, 10, 10));
                                            new_user_notification.setAlignment(Pos.CENTER);
                                            new_user_notification.setStyle(" -fx-background-color: #8544ef; -fx-border-color: rgba(200,200,200,0.4);");
                                            new_user_notification.setPrefHeight(30.0);
                                            new_user_notification.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                            main_message_box.getChildren().add(new_user_notification);
                                        }
                                    });
                                }
                            }else{
		    	        	    Platform.runLater(new Runnable() {
		    	        	    	public void run() {

                                        SVGPath svg = new SVGPath();
                                        String path = "M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z";
                                        svg.setContent(path);
                                        svg.setScaleX(2.0);
                                        svg.setScaleY(2.0);
                                        svg.setStyle("-fx-text-fill: white;");
                                        svg.setFill(Color.rgb(255,255,255));
                                        //main_message_box.getChildren().add(new Label(recieved_message_username +" : "+recieved_message));
    
                                        Label username_label = new Label(recieved_message_username);
                                        Label time_label = new Label(recieved_message_time);
                                        Label message_label = new Label(recieved_message);
                                        time_label.setPadding(new Insets(0, 0, 0, 10));


                                        username_label.setStyle("-fx-text-fill: white; -fx-font-weight: bold");
                                        time_label.setStyle("-fx-text-fill: white;");
                                        message_label.setStyle("-fx-text-fill: white;");
		    	        	    		VBox message = new VBox();
		    	        	    		HBox message_username = new HBox(username_label, time_label );
		    	        	    		HBox message_content = new HBox(message_label);


                                        message_label.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                        message_label.setPrefHeight(Control.USE_COMPUTED_SIZE);
                                        message.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                        message.setPrefHeight(Control.USE_COMPUTED_SIZE);
                                        message_username.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                        message_username.setPrefHeight(Control.USE_COMPUTED_SIZE);
                                        message_content.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                        message_content.setPrefHeight(Control.USE_COMPUTED_SIZE);
                                        message_username.setPadding(new Insets(5, 20, 5, 20));
                                        message_content.setPadding(new Insets(0, 20, 5, 20));
                                        message.getChildren().addAll(message_username, message_content);


                                        HBox main_message = new HBox();
                                        main_message.setPadding(new Insets(20, 20, 20, 20));
                                        main_message.setSpacing(20);
                                        if(recieved_message_username.equals(username)){
                                            message.setStyle("-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-color: rgba(200,200,200,0.4); -fx-background-color:  #14101a"); 
                                            main_message.setAlignment(Pos.CENTER_RIGHT);
                                            main_message.getChildren().addAll(message, svg);
                                        }else{
                                            message.setStyle("-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-color: rgba(200,200,200,0.4); -fx-background-color: #8544ef"); 
                                            main_message.setAlignment(Pos.CENTER_LEFT);
                                            main_message.getChildren().addAll(svg, message);
                                        }
                                        main_message_box.getChildren().add(main_message);
		    	        	    	}
		    	        	    });
                            }
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
		    clientThread.start();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    
    @FXML
    void logout(ActionEvent event) throws IOException {
        close_connection();
        FXMLLoader login_loader = new FXMLLoader(App.class.getResource("fxml/login.fxml"));
        Parent login_pane = login_loader.load();
        Scene login_scene = new Scene (login_pane);
        Login_controller login_controller = (Login_controller) login_loader.getController();
        Stage primaryStage = (Stage)logout_item.getParentPopup().getOwnerWindow().getScene().getWindow(); 
        primaryStage.setScene(login_scene);
    }


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
            submit_message.setStyle("-fx-background-color: linear-gradient(to right bottom, rgba(143,10,228,1) 6%, rgba(103,21,235,1) 55%, rgba(143,10,228,1) 100%);-fx-border-radius: 10; -fx-background-radius: 10;");
            submit_message.setCursor(Cursor.HAND);
        }else{
            submit_message.setStyle("-fx-background-color: transparent;");
            submit_message.setCursor(Cursor.DEFAULT);
        }
    }

}

