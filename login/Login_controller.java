import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import java.sql.*;

public class Login_controller {
    @FXML
    private CheckBox remember_input;

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
       try{
            Sql_connection db = new Sql_connection();
            ResultSet result = db.select_query("SELECT  * from users");
            while (result.next()) {
                System.out.println(result.getString("username"));
            }
            db.closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

}
