import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login_controller {

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    void cancel_login(ActionEvent event) {
        System.out.print( email.getText() );
    }

    @FXML
    void submit_login(ActionEvent event) {
        System.out.print( password.getText() );
    }

}
