import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {
    static FXMLDocumentController myControllerHandle;

    @Override
    public void start(Stage primaryStage) throws IOException {

        //Pane root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();
        myControllerHandle = (FXMLDocumentController)loader.getController();


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}