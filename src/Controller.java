import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller  {

    @FXML
    private Button send_message;

    @FXML
    void clicked(ActionEvent event) {
        System.out.print("helloe");
    }

}
