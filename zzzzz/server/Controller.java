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




public class Controller  {

    private int port = 5555;
	private ServerSocket socket;
	private ArrayList<Socket> clients =  new ArrayList<Socket>();
	//private ArrayList<Client_thread> client_threads;
    static Controller myControllerHandle;
	private BufferedReader incomingMessageReader;
	private PrintWriter outgoingMessageWriter;

    public void initialize(URL url, ResourceBundle rb) {
        System.out.print("start init");
        try{
            try {
                ServerSocket server = new ServerSocket(5000);
            } catch (IOException e) {
		    	e.printStackTrace();
		    }
            System.out.print("ceated server init");
            
            new Thread( new Runnable() {
                public void run() {
	                try {
	                	while (true) {
                            scroll_pane_inside.getChildren().add(new Label("started server"));
	                		final Socket clientSocket = socket.accept();
                            scroll_pane_inside.getChildren().add(new Label("connected to server"));
                            
	                		clients.add(clientSocket);
			                incomingMessageReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			                outgoingMessageWriter = new PrintWriter(clientSocket.getOutputStream(), true);
	                		Platform.runLater(new Runnable() {
	                			@Override
	                			public void run() {
                                        System.out.print("Client "+ clientSocket.getRemoteSocketAddress()+ " connected");
	                				//edit ui
                                        //serverLog.add("Client "+ clientSocket.getRemoteSocketAddress()+ " connected");
	                			}
	                		});

                            new Thread( new Runnable() {
                                public void run() {
                                    try {
		                            	String message_to_server;
		                            	while (true) {
		                            		message_to_server = incomingMessageReader.readLine();
                                            System.out.print("client sent : "+message_to_server);
		                                    for (Socket client : clients) {
                                                outgoingMessageWriter.println(message_to_server);
		                                    }
		                            	}
		                            } catch (SocketException e) {
		                            	//baseServer.clientDisconnected(this);
		                            } catch (IOException e) {
		                            	e.printStackTrace();
		                            }
                                }
                            }).start();
	                		//ClientThread clientThreadHolderClass = new ClientThread(clientSocket, this);
	                		//Thread clientThread = new Thread(clientThreadHolderClass);
	                		//clientThreads.add(clientThreadHolderClass);
	                		//clientThread.setDaemon(true);
	                		//clientThread.start();
	                		//ServerApplication.threads.add(clientThread);
	                	}
	                } catch (IOException e) {
	                	e.printStackTrace();
	                }
            
	            }
            }).start();
            
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