package org.openjfx;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.application.Platform;
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Cursor;
import javafx.scene.control.Control;
import javafx.scene.Parent;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.ScrollPane;

public class Controller {

    private int port = 5555;
    private ServerSocket socket;
    //private ArrayList<Socket> clients = new ArrayList<Socket>();
    private List<DataOutputStream> outputs = new ArrayList<>();
    private DataOutputStream outgoingMessageWriter;
    private ServerSocket server;
    private ServerSocket login_server;
    private ServerSocket register_server;
    Map<SocketAddress, String> clients_usernames = new HashMap<SocketAddress, String>();

    public void initialize() {

        main_scroll_pane.widthProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("hi man  im resized to "+newVal);
            main_message_box.setPrefWidth(newVal.doubleValue() - 2.0);
        });
        main_scroll_pane.heightProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("hi man  im resized to "+newVal);
            main_message_box.setPrefHeight(newVal.doubleValue() - 2.0);
        });
        main_message_box.heightProperty().addListener(observable -> main_scroll_pane.setVvalue(1D));
        //message_content.setStyle("-fx-text-fill: white;"+message_content.getStyle());
        main_scroll_pane.getStylesheets().add(App.class.getResource("css/style.css").toExternalForm());
        try {

            try {
                server = new ServerSocket(5000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                login_server = new ServerSocket(6000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                register_server = new ServerSocket(7000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //register thread socket
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            System.out.println("started register thread \n");
                            final Socket clientSocket = register_server.accept();
                            System.out.println("accepted register thread \n");

                            DataOutputStream register_output = new DataOutputStream(clientSocket.getOutputStream());
                            DataInputStream register_input = new DataInputStream(clientSocket.getInputStream());
                            String username = register_input.readUTF();
                            String email = register_input.readUTF();
                            String password = register_input.readUTF();

                            try {
                                Sql_connection db = new Sql_connection();
                                Connection con = db.connect();
                                PreparedStatement query_email = con.prepareStatement("select count(*) from users where email=?");
                                PreparedStatement query_username = con.prepareStatement("select count(*) from users where username=?");
                                query_email.setString(1, email);
                                query_username.setString(1, username);
                                ResultSet result_email = query_email.executeQuery();
                                ResultSet result_username = query_username.executeQuery();
                                result_email.next();
                                result_username.next();
                                if (result_email.getInt(1) == 0 & result_username.getInt(1) == 0) {
                                    PreparedStatement query = con.prepareStatement("insert into  users(username, email, password) values(?, ?, ?);");
                                    query.setString(1, username);
                                    query.setString(2, email);
                                    query.setString(3, password);
                                    query.executeUpdate();
                                    System.out.println("accoutn created \n");
                                    register_output.writeUTF("success");
                                } else if (result_email.getInt(1) == 1 & result_username.getInt(1) == 1) {
                                    register_output.writeUTF("email_username_duplicate");
                                } else if (result_email.getInt(1) == 1) {
                                    register_output.writeUTF("email_duplicate");
                                } else if (result_username.getInt(1) == 1) {
                                    register_output.writeUTF("username_duplicate");
                                }else{
                                    register_output.writeUTF("no_account");
                                }
                                db.closeConnection();
                            } catch (Exception ex) {
                                register_output.writeUTF("connection_error");
                                ex.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            //login+messaging thread socket
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            final Socket clientSocket = login_server.accept();

                            DataOutputStream login_output = new DataOutputStream(clientSocket.getOutputStream());
                            DataInputStream login_input = new DataInputStream(clientSocket.getInputStream());

                            String email = login_input.readUTF();
                            String password = login_input.readUTF();

                            try {
                                Sql_connection db = new Sql_connection();
                                Connection con = db.connect();
                                PreparedStatement query = con.prepareStatement("select * from users where email =?");
                                query.setString(1, email);
                                ResultSet result = query.executeQuery();
                                if (result.next()) {
                                    if (result.getString("password").equals(password)) {
                                        clients_usernames.put(clientSocket.getRemoteSocketAddress(), result.getString("username"));
                                        login_output.writeUTF("success");
                                        login_output.writeUTF(result.getString("username"));
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                Label new_user_label = new Label(clients_usernames.get(clientSocket.getRemoteSocketAddress()));
                                                new_user_label.setStyle("-fx-text-fill: white;");
		    	        	                    HBox new_user = new HBox(new_user_label);
                                                new_user.setPadding(new Insets(10, 10, 10, 10));
                                                new_user.setPrefHeight(30.0);
                                                new_user.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                                new_user.setAlignment(Pos.CENTER_LEFT);
                                                list_of_users.getChildren().add(new_user);
                                                Label ll =  new Label(clientSocket.getRemoteSocketAddress() + "/" + clients_usernames.get(clientSocket.getRemoteSocketAddress() ) +" joined the chat");
                                                ll.setStyle("-fx-text-fill: white;");
                                                HBox new_user_notification = new HBox(ll);
                                                new_user_notification.setPadding(new Insets(10, 10, 10, 10));
                                                new_user_notification.setAlignment(Pos.CENTER);
                                                new_user_notification.setStyle(" -fx-background-color: #8544ef; -fx-border-color: rgba(200,200,200,0.4);");
                                                new_user_notification.setPrefHeight(30.0);
                                                new_user_notification.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                                main_message_box.getChildren().add(new_user_notification);
                                            }
                                        });
                                        DataOutputStream current_output = new DataOutputStream(clientSocket.getOutputStream());
                                        Statement get_messages = con.createStatement();
                                        result = get_messages.executeQuery("select * from messages limit 20");
                                        while (result.next()) {
                                            current_output.writeUTF(result.getString("sender"));
                                            current_output.writeUTF(result.getString("message"));
                                            current_output.writeUTF(result.getString("created_at").substring(11));
                                        }
                                        

                                        synchronized (outputs) {
                                            for (DataOutputStream output : outputs) {
                                                output.writeUTF("&B3#aVEyvj#@WqKCTpPfu5d+yneVycy*qhkCh94kqg#3#@Sz66vHn)FA#shFfPpJ&B3#aVEyvj#@WqKCTpPfu5d+yneVycy*qhkCh94kqg#3#@Sz66vHn)FA#shFfPpJ");
                                                output.writeUTF(clients_usernames.get(clientSocket.getRemoteSocketAddress()));
                                                output.writeUTF(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
                                            }
                                        }


                                        for (Map.Entry<SocketAddress, String> set : clients_usernames.entrySet()) {
                                            current_output.writeUTF("&B3#aVEyvj#@WqKCTpPfu5d+yneVycy*qhkCh94kqg#3#@Sz66vHn)FA#shFfPpJ&B3#aVEyvj#@WqKCTpPfu5d+yneVycy*qhkCh94kqg#3#@Sz66vHn)FA#shFfPpJ");
                                            current_output.writeUTF("UmlOcZueqgpO89ecoH232na5GrHKEP6Kr8uipTSjS9HVTBEpkAQRGJBvi7X50WDpkLkWtaQ0gik1voPxBBphbh0eSKuZAJuBlYpWB9jGKzYsU5uB3AVU5A5L95ugeHBp");
                                            current_output.writeUTF(set.getValue());
                                        }


                                        synchronized (outputs) {
                                            outputs.add(current_output);
                                        }

                                        new Thread(new Runnable() {
                                            public void run() {
                                                try (DataInputStream incomingMessageReader = new DataInputStream(clientSocket.getInputStream())) {
                                                    while (clientSocket.isConnected() && !clientSocket.isClosed()) {
                                                        incomingMessageReader.available();
                                                        String message_to_server = incomingMessageReader.readUTF();
                                                        try {
                                                            Sql_connection db = new Sql_connection();
                                                            Connection con = db.connect();
                                                            PreparedStatement insert_message = con.prepareStatement("insert into messages(sender, message) values(?, ?)");
                                                            insert_message.setString(1, clients_usernames.get(clientSocket.getRemoteSocketAddress()));
                                                            insert_message.setString(2, message_to_server);
                                                            insert_message.executeUpdate();
                                                            db.closeConnection();
                                                        } catch (Exception ex) {
                                                            ex.printStackTrace();
                                                        }
                                                        Platform.runLater(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Label username_label = new Label(clients_usernames.get(clientSocket.getRemoteSocketAddress()));
                                                                Label message_label = new Label(message_to_server);
                                                                Label time_label = new Label(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
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
                                                                message.setStyle("-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-color: rgba(200,200,200,0.4); -fx-background-color: #8544ef"); 
                                                                main_message.setAlignment(Pos.CENTER_LEFT);
                                                                main_message.getChildren().addAll(message);
                                                                main_message_box.getChildren().add(main_message); 
                                                            }
                                                        });
                                                        synchronized (outputs) {
                                                            for (DataOutputStream output : outputs) {
                                                                output.writeUTF(clients_usernames.get(clientSocket.getRemoteSocketAddress()));
                                                                output.writeUTF(message_to_server);
                                                                output.writeUTF(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
                                                            }
                                                        }
                                                    }
                                                    System.out.println(clientSocket.isClosed());
                                                    System.out.println(clientSocket.isConnected());
                                                    System.out.println("connection probably lost");
                                                } catch (SocketException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    String user_name = clients_usernames.get(clientSocket.getRemoteSocketAddress());
                                                    clients_usernames.remove(clientSocket.getRemoteSocketAddress());
                                                    Platform.runLater(new Runnable() {
		    	        	                            public void run() {
                                                            Label ll = new Label(clientSocket.getRemoteSocketAddress() + "/" + user_name + " left the chat");
                                                            ll.setStyle("-fx-text-fill: white;");
                                                            System.out.print(clients_usernames.get(clientSocket.getRemoteSocketAddress())+"\n");
                                                            HBox new_user_notification = new HBox(ll);
                                                            //new_user_notification.setMargin(ll, new Insets(10, 10, 10, 10));
                                                            new_user_notification.setPadding(new Insets(10, 10, 10, 10));
                                                            new_user_notification.setAlignment(Pos.CENTER);
                                                            new_user_notification.setStyle("-fx-background-color: #8544ef; -fx-border-color: rgba(200,200,200,0.4);");
                                                            new_user_notification.setPrefHeight(30.0);
                                                            new_user_notification.setPrefWidth(Control.USE_COMPUTED_SIZE);
                                                            main_message_box.getChildren().add(new_user_notification);

                                                            Node nodeOut = list_of_users;
                                                            if(nodeOut instanceof VBox){
                                                                for(Node nodeIn:((VBox)nodeOut).getChildren()){
                                                                    System.out.println("hello from the inside");
                                                                    System.out.println(user_name);
                                                                    System.out.println(((Label)((HBox)nodeIn).getChildren().get(0)).getText());
                                                                    if(((Label)((HBox)nodeIn).getChildren().get(0)).getText().equals(user_name)){
                                                                        list_of_users.getChildren().remove(((HBox)nodeIn));
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                                    outputs.remove(current_output);
                                                    //for (DataOutputStream output : outputs) {
                                                    //    System.out.print(output+"\n");
                                                    //}
                                                    try{
                                                        synchronized (outputs) {
                                                            for (DataOutputStream output : outputs) {
                                                                    System.out.println("hello from the wapssssssssssssssssssssss");
                                                                output.writeUTF("&B3#aVEyvj#@WqKCTpPfu5d+yneVycy*qhkCh94kqg#3#@Sz66vHn)FA#shFfPpJ&B3#aVEyvj#@WqKCTpPfu5d+yneVycy*qhkCh94kqg#3#@Sz66vHn)FA#shFfPpJ");
                                                                output.writeUTF("QHX)w+#T4WatEZHyaL(8kzdRFS$ezJ2DLWnzT&wy*n*bhLFAE!heC2+YL%2jaP(d4IEsEm$cPye^aqVUs6G85e$z$L)ue+fv9U+WpYG)@U93a^jN*z)+bPstFvPSVVXM");
                                                                output.writeUTF(user_name);
                                                            }
                                                        }
                                                    }catch (IOException ef) {
                                                        ef.printStackTrace();
                                                    }
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    } else {
                                        System.out.println("passwird error \n");
                                        login_output.writeUTF("password_error");
                                    }
                                } else {
                                    System.out.println("email err error \n");
                                    System.out.println(login_output);
                                    System.out.println();
                                    login_output.writeUTF("email_error");
                                    login_output.writeUTF("");
                                }
                                db.closeConnection();
                            } catch (Exception ex) {
                                login_output.writeUTF("connection_error");
                                ex.printStackTrace();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start(); 


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
    private VBox list_of_users;
    
    @FXML
    private VBox main_message_box;

    @FXML
    private Button submit_message;
    
    @FXML
    void clicked(ActionEvent event) throws IOException {
        // dos.writeUTF(message_content.getText());
    }

    @FXML
    void enter_message(KeyEvent event) throws IOException {
        if (event.getCode().toString().equals("ENTER")) {
            // dos.writeUTF(message_content.getText());
        }
    }

}

