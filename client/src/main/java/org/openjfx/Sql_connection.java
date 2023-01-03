package org.openjfx;

import java.sql.*;

public class Sql_connection {

    private final String database_name = "java";
    private final String database_port = "3306";
    private final String database_username = "root";
    private final String database_password = "11062001";
    private Connection connect;

    
    public Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connect = DriverManager.getConnection("jdbc:mysql://localhost:"+this.database_port+"/"+this.database_name, this.database_username, this.database_password);
        return connect;
    }

    public void closeConnection() throws SQLException{
        connect.close();
    }
}





