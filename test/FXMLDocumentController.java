import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.application.Platform;
public class FXMLDocumentController implements Initializable {

    private static int PORT_NUMBER = 5555;
    @FXML
    private TextField text1;
    @FXML
    Button stopButton, startButton;

    private ScheduledExecutorService scheduledExecutorService;
    private Model model;
    private Client client;
    private Server server;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        scheduledExecutorService = Executors.newScheduledThreadPool(2);
        model = new Model();
        text1.textProperty().bind(model.getTextProperty());
    }

    private void startServer(){

        try {
            server = new Server(PORT_NUMBER);
            server.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void startClient(){

        try {
            client = new Client(PORT_NUMBER, model);
            client.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void start(){

        scheduledExecutorService.submit(() -> startServer());
        scheduledExecutorService.submit(() -> startClient());

        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    public void stop(){

        client.stop();
        server.stop();

        scheduledExecutorService.shutdown();
        stopButton.setDisable(true);
    }
}

class Model {

    private final ReadOnlyStringWrapper textProperty;

    Model() {
        textProperty = new ReadOnlyStringWrapper();
    }

    synchronized void setText(String s){
        Platform.runLater(()->textProperty.set(s));
    }

    ReadOnlyStringWrapper getTextProperty(){
        return textProperty;
    }
}

class Server {

    private final int portNumber;
    private volatile boolean stop = false;
    private static long REFRESH_TIME = 2;

    Server(int portNumber) {
        this.portNumber = portNumber;
    }

    void start() throws IOException {

        Socket socket;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            socket = serverSocket.accept();
            DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
            while (socket.isConnected() && ! stop) {
                dout.writeUTF(randomText());
                try {
                    TimeUnit.SECONDS.sleep(REFRESH_TIME);
                } catch (InterruptedException ex) {
                    break;
                }
                dout.flush();
            }
        }
    }

    private String randomText()
    {
        DecimalFormat df = new DecimalFormat("#.00");
        StringBuilder sb = new StringBuilder(df.format(Math.random()*10));
        sb.append("#")
        .append(df.format(Math.random()*10)) ;
        return sb.toString();
    }

    void stop(){
        stop = true;
    }
}

class Client {

    private final int portNumber;
    private final Model model;
    private volatile boolean stop = false;

    Client(int portNumber,  Model model) {
        this.portNumber = portNumber;
        this.model = model;
    }

    void start() throws IOException {
        Socket socket = new Socket("localhost",portNumber);
        DataInputStream dIn=new DataInputStream(socket.getInputStream());
        while (socket.isConnected() && ! stop) {
            model.setText(dIn.readUTF());
        }
        socket.close();
    }

    void stop(){
        stop = true;
    }
}