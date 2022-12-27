import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import java.sql.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Register_controller {
    private Scene main;
    private Scene register;

    public void set_main_scene(Scene scene){
        main = scene;
    }

    @FXML
    private Button cancel_button;

    @FXML
    private CheckBox remember_input;

    @FXML
    private Button register_button;

    @FXML
    private TextField username;

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
    void register_form(ActionEvent event) {
        System.out.println("username");
        //try{
        //    Sql_connection db = new Sql_connection();
        //    String[] params = {email.getText(), password.getText()};
        //    ResultSet result = db.select_query("SELECT  * from users", params);
        //    while (result.next()) {
        //        System.out.println(result.getString("username"));
        //    }
        //    db.closeConnection();
        //} catch (Exception ex) {
        //    ex.printStackTrace();
        //}

        try{
            Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            primaryStage.setScene(main);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    
    }

}





