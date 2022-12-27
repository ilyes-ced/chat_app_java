import java.sql.*;


public class Sql_connection {

    // Database Variables
    private final String database_name = "java";
    private final String database_port = "3306";
    private final String database_username = "root";
    private final String database_password = "11062001";
    private Connection connect;
    private PreparedStatement sql_statement;

    
    public Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connect = DriverManager.getConnection("jdbc:mysql://localhost:"+this.database_port+"/"+this.database_name, this.database_username, this.database_password);
        return connect;
    }

    //public ResultSet select_query(String givenStatement, String[] params) throws SQLException, ClassNotFoundException{
    //    Class.forName("com.mysql.cj.jdbc.Driver");
    //    this.connect = DriverManager.getConnection("jdbc:mysql://localhost:"+this.database_port+"/"+this.database_name, this.database_username, this.database_password);
    //    this.sql_statement = this.connect.prepareStatement(givenStatement);
    //    ResultSet result = sql_statement.executeQuery();
    //    return result;
    //}
    //    //ps = con.prepareStatement("update books set year=? where title=?");
    //    //ps.setInt(1, 2022);
    //    //ps.setString (2,"Hamlet");
    //    //ps.executeUpdate();

    //public ResultSet update_query(String statment, String[] params, String[] types) throws SQLException, ClassNotFoundException{
    //    PreparedStatement query = connect.prepareStatement(statment);
    //    for (int i = 0; i < params.length; i++) {
    //        if(types[i] == "string"){
    //            query.setString(i, params[i]);
    //        }else if(types[i] == "int"){
    //            query.setString(i, params[i]);
    //        }
    //        System.out.println(params[i]);
    //    }
    //    query.executeUpdate();
    //    Class.forName("com.mysql.cj.jdbc.Driver");
    //    this.connect = DriverManager.getConnection("jdbc:mysql://localhost:"+this.database_port+"/"+this.database_name, this.database_username, this.database_password);
    //    this.sql_statement = this.connect.prepareStatement(statment);
    //    ResultSet result = sql_statement.executeQuery();
    //    return result;
    //}

    public void closeConnection() throws SQLException{
        connect.close();
    }

    //private void set_sql_statement(String givenStatement) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    //}

}


















/*
import java.sql.*;


public class Sql_connection  {

	public Connection connect(String[] args) {
        Connection con = null;
        String url = "jdbc:mysql://localhost:3306/java";
        String driver = "com.mysql.cj.jdbc.Driver";
        String user = "root";
        String pass = "11062001";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, pass);
            if (con == null) {
                System.out.println("Connection cannot be established");
            }
            return con;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
*/
/*
Class.forName("com.mysql.cj.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java","root","11062001");
PreparedStatement ps = con.prepareStatement("select * from users_data where username in (select username from accounts where username=? and password=?)");
ps.setString(1, textField.getText());
ps.setString(2, textField_1.getText());
ResultSet result = ps.executeQuery();
int count = 0;
while(result.next()){
	System.out.print("dddddddddd4444444444444444444 \n");
    count++;
	lblNoResults.setText("sucess");
	label.setText("name is ; "+result.getString(3) + ", surname : "+result.getString(5) );
	label_1.setText("address is ; " +result.getString(5));
	label_2.setText("pone number is :" +result.getString(6));
}

if(count == 0){
	System.out.print("dzdzdzdzdzdzdzdzdzdzdzdzdzdzd \n");
	lblNoResults.setText("failure");
	label.setText("");
	label_1.setText("");
	label_2.setText("");
}*/