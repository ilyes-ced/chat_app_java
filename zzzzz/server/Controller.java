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




public class Controller  {

    private int port = 5555;
	private ServerSocket socket;
	private ArrayList<Socket> clients =  new ArrayList<Socket>();
    private List<DataOutputStream> outputs = new ArrayList<>();
	private DataOutputStream outgoingMessageWriter;
    private ServerSocket server;
    private ServerSocket login_server;
    private ServerSocket register_server;
    private boolean is_auth = false;
    Map<SocketAddress, String> clients_usernames = new HashMap<SocketAddress, String>();



    public void initialize(){
        try{

            
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


            new Thread( new Runnable() {
                public void run() {
                    while(true){
                        try{
                            System.out.println("started register thread \n");
                            final Socket clientSocket = register_server.accept();
                            System.out.println("accepted register thread \n");
                            
                            DataOutputStream register_output = new DataOutputStream(clientSocket.getOutputStream());
                            DataInputStream register_input = new DataInputStream(clientSocket.getInputStream());
                            String username = register_input.readUTF();
                            String email = register_input.readUTF();
                            String password = register_input.readUTF();
                            
                            try{
                                Sql_connection db = new Sql_connection();
                                Connection con = db.connect();
                                PreparedStatement query = con.prepareStatement("select * from users where email=?");
                                query.setString(1, email);
                                ResultSet result = query.executeQuery();
                                System.out.println(result);
                                while(result.next()){
                                    System.out.println(result.getString("email"));
                                }
                                if(!result.next()){
                                    query = con.prepareStatement("insert into  users(username, email, password) values(?, ?, ?)");
                                    query.setString(1, username);
                                    query.setString(2, email);
                                    query.setString(3, password);
                                    System.out.println("accoutn created \n");
                                    register_output.writeUTF("success");
                                }else{
                                    System.out.println("accoutn account_duplicate \n");
                                    register_output.writeUTF("account_duplicate");
                                }
                                db.closeConnection();
                            } catch (Exception ex) {
                                register_output.writeUTF("connection_error");
                                ex.printStackTrace();
                            }
                            
                        }catch( IOException e ){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();





            new Thread( new Runnable() {
                public void run() {
                    while(true){
                        try{
                            final Socket clientSocket = login_server.accept();
                            
                            DataOutputStream login_output = new DataOutputStream(clientSocket.getOutputStream());
                            DataInputStream login_input = new DataInputStream(clientSocket.getInputStream());
                            
                            String email = login_input.readUTF();
                            String password = login_input.readUTF();
                            
                            try{
                                Sql_connection db = new Sql_connection();
                                Connection con = db.connect();
                                PreparedStatement query = con.prepareStatement("select * from users where email =?");
                                query.setString(1, email);
                                ResultSet result = query.executeQuery();
                                if (result.next()) {
                                    if(result.getString("password").equals(password)){
                                        clients_usernames.put(clientSocket.getRemoteSocketAddress(), result.getString("username"));
                                        is_auth = true;
                                        login_output.writeUTF("success");
                                        login_output.writeUTF(result.getString("username"));
                                        Platform.runLater(new Runnable() {
	                		            	@Override
	                		            	public void run() {
                                                scroll_pane_inside.getChildren().add(new Label("Client "+ clientSocket.getRemoteSocketAddress()+ " connected"));
	                		            	}
	                		            });
                                        synchronized(outputs) {
                                            outputs.add(new DataOutputStream(clientSocket.getOutputStream()));
                                        }

                                        
                                        new Thread( new Runnable() {
                                            public void run() {
                                                try (DataInputStream incomingMessageReader = new DataInputStream(clientSocket.getInputStream())) {
		                                        	while (true) {
		                                        		String message_to_server = incomingMessageReader.readUTF();
                                                        //insert message in database
                                                        Platform.runLater(new Runnable() {
	                		                            	@Override
	                		                            	public void run() {
                                                                scroll_pane_inside.getChildren().add(new Label("client "+clientSocket.getRemoteSocketAddress()+" sent : "+message_to_server));
                                                                //scroll_pane_inside.getChildren().add(new Label(clientSocket.getRemoteSocketAddress().getClass().getName()));
                                                            }
	                		                            });
                                                        System.out.println("test \n");
                                                        synchronized (outputs) {
		                                                    for (DataOutputStream output : outputs) {
                                                                output.writeUTF(clients_usernames.get(clientSocket.getRemoteSocketAddress()));
                                                                output.writeUTF(message_to_server);
		                                                    }
                                                        }
		                                        	}
		                                        } catch (SocketException e) {
		                                        	e.printStackTrace();
		                                        	//baseServer.clientDisconnected(this);
		                                        } catch (IOException e) {
		                                        	e.printStackTrace();
		                                        }
                                            }
                                        }).start();
                                    }else{
                                        System.out.println("passwird error \n");
                                        login_output.writeUTF("password_error");
                                        //send passwod error message
                                    }
                                }else{
                                    System.out.println("email err error \n");
                                    login_output.writeUTF("email_error");
                                    //send error message
                                }
                                db.closeConnection();
                            } catch (Exception ex) {
                                login_output.writeUTF("connection_error");
                                ex.printStackTrace();
                            }
                            
                        }catch( IOException e ){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            
            //Thread pp = new Thread( new Runnable() {
            //    public void run() {
	        //        try {
	        //        	while (true) {
            //                //scroll_pane_inside.getChildren().add(new Label("started server"));
	        //        		  final Socket clientSocket = server.accept();
            //                Platform.runLater(new Runnable() {
	        //        			@Override
	        //        			public void run() {
            //                        scroll_pane_inside.getChildren().add(new Label("Client "+ clientSocket.getRemoteSocketAddress()+ " connected"));
	        //        			}
	        //        		  });
            //                synchronized(outputs) {
            //                    outputs.add(new DataOutputStream(clientSocket.getOutputStream()));
            //                }
	        //        
            //                new Thread( new Runnable() {
            //                    public void run() {
            //                        try (DataInputStream incomingMessageReader = new DataInputStream(clientSocket.getInputStream())) {
		    //                        	while (true) {
		    //                        		String message_to_server = incomingMessageReader.readUTF();
            //                                System.out.println("Number of active threads from the given thread: " + Thread.activeCount()+"\n");
            //                                Platform.runLater(new Runnable() {
	        //        		                	@Override
	        //        		                	public void run() {
            //                                        scroll_pane_inside.getChildren().add(new Label("client "+clientSocket.getRemoteSocketAddress()+" sent : "+message_to_server));
            //                                    }
	        //        		                });
            //                                
            //                                synchronized (outputs) {
		    //                                    for (DataOutputStream output : outputs) {
            //                                        output.writeUTF(message_to_server);
		    //                                    }
            //                                }
		    //                        	}
		    //                        } catch (SocketException e) {
		    //                        	//baseServer.clientDisconnected(this);
		    //                        } catch (IOException e) {
		    //                        	e.printStackTrace();
		    //                        }
            //                    }
            //                }).start();
	        //        	}
	        //        } catch (IOException e) {
	        //        	e.printStackTrace();
	        //        }
            //
	        //    }
            //});
            //pp.start();
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
    private VBox scroll_pane_inside;


    @FXML
    void clicked(ActionEvent event) throws IOException {
        //dos.writeUTF(message_content.getText());
    }


    @FXML
    void enter_message(KeyEvent event) throws IOException {
        if(event.getCode().toString().equals("ENTER")){
            //dos.writeUTF(message_content.getText());
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