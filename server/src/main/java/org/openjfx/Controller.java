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


public class Controller {

    private int port = 5555;
    private ServerSocket socket;
    private ArrayList<Socket> clients = new ArrayList<Socket>();
    private List<DataOutputStream> outputs = new ArrayList<>();
    private DataOutputStream outgoingMessageWriter;
    private ServerSocket server;
    private ServerSocket login_server;
    private ServerSocket register_server;
    Map<SocketAddress, String> clients_usernames = new HashMap<SocketAddress, String>();

    public void initialize() {
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
                                System.out.println("/////////////////////////////////////////");
                                System.out.println(result_email.getInt(1));
                                System.out.println(result_username.getInt(1));

                                if (result_email.getInt(1) == 0 & result_username.getInt(1) == 0) {
                                    PreparedStatement query = con.prepareStatement("insert into  users(username, email, password) values(?, ?, ?)");
                                    query.setString(1, username);
                                    query.setString(2, email);
                                    query.setString(3, password);
                                    System.out.println("accoutn created \n");
                                    register_output.writeUTF("success");
                                } else if (result_email.getInt(1) == 1 & result_username.getInt(1) == 1) {
                                    register_output.writeUTF("email_username_duplicate");
                                } else if (result_email.getInt(1) == 1) {
                                    register_output.writeUTF("email_duplicate");
                                } else if (result_username.getInt(1) == 1) {
                                    register_output.writeUTF("username_duplicate");
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

            new Thread(new Runnable() {
                public void run() {
                    //socket.isConnected() && !socket.isClosed();
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
                                                scroll_pane_inside.getChildren().add(new Label("Client "+ clientSocket.getRemoteSocketAddress() + " connected"));
                                            }
                                        });
                                        DataOutputStream current_output = new DataOutputStream(clientSocket.getOutputStream());
                                        Statement get_messages = con.createStatement();
                                        result = get_messages.executeQuery("select * from messages limit 20");
                                        while (result.next()) {
                                            current_output.writeUTF(result.getString("sender"));
                                            current_output.writeUTF(result.getString("message"));
                                            current_output.writeUTF(result.getString("created_at"));
                                        }
                                        
                                        synchronized (outputs) {
                                            System.out.println("///////////////////////////////////////////////////");
                                            System.out.println(clientSocket.getRemoteSocketAddress() + " has joined the chat");
                                            System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
                                            for (DataOutputStream output : outputs) {
                                                output.writeUTF("&B3#aVEyvj#@WqKCTpPfu5d+yneVycy*qhkCh94kqg#3#@Sz66vHn)FA#shFfPpJ&B3#aVEyvj#@WqKCTpPfu5d+yneVycy*qhkCh94kqg#3#@Sz66vHn)FA#shFfPpJ");
                                                output.writeUTF(clients_usernames.get(clientSocket.getRemoteSocketAddress()));
                                                output.writeUTF(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
                                            }
                                        }
                                        synchronized (outputs) {
                                            outputs.add(current_output);
                                        }

                                        new Thread(new Runnable() {
                                            public void run() {
                                                try (DataInputStream incomingMessageReader = new DataInputStream(
                                                    clientSocket.getInputStream())) {
                                                    while (clientSocket.isConnected() && !clientSocket.isClosed()) {
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
                                                                scroll_pane_inside.getChildren().add(new Label("client "+ clientSocket.getRemoteSocketAddress()+ " sent : " + message_to_server));
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
                                                    if(clientSocket.isClosed()){

                                                    }
                                                } catch (SocketException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
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
                                    login_output.writeUTF("email_error");
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
    private TextField message_content;

    @FXML
    private Button send_message;

    @FXML
    private VBox add_messages;

    @FXML
    private VBox scroll_pane_inside;

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

