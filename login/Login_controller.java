import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;

public class Login_controller {

    @FXML
    private Button cancel_button;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button submit_button;

    @FXML
    void cancel_form(ActionEvent event) {
        System.out.print( email.getText() );
        
    }

    @FXML
    void submit_form(ActionEvent event) {
        System.out.print( password.getText() );
        Sql_connection db = new Sql_connection();
        Connection query = db.connect();
        query.executeQuery("SELECT VERSION()");
    }

}
