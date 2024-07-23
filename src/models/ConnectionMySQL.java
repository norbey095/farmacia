
package models;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {
    
    private String database_name = "farmacy_database";
    private String user = "root";
    private String password = "root";
    private String url = "jdbc:mysql://localhost:3306/" + database_name;
    Connection conn = null;
    
    public Connection getConection(){
        try{
            //obtener valor del diver
            Class.forName("com.mysql.cj.jdbc.Driver");
            //obtener la conexion
            conn = DriverManager.getConnection(url, user, password);
        }catch(ClassNotFoundException e){
            System.err.println("ha ocurrido un ClassNotFoundException " + e.getMessage());
        }catch(SQLException e){
           System.err.println("ha ocurrido un SQLException " + e.getMessage());
        }
        return conn;
    }
}
