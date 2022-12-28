
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author felipe
 */
public class Leftslidemenusample extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader main_page_loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent main_pane = main_page_loader.load();
        Scene main_scene = new Scene(main_pane, 1200, 800);
        FXMLDocumentController login_controller = (FXMLDocumentController) main_page_loader.getController();


        //Parent FXMLLoader = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        //FXMLDocumentController login_controller = (FXMLDocumentController) root.getController();
        //Scene scene = new Scene(root);
        
        stage.setScene(main_scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}