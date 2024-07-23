
package models;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class EmployesDao {
    //instanciar la conexion
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //VARIABLES PARA ENVIAR DATOS ENTRE INTERFACES
    public static int id_user = 0;
    public static String full_name_user = "";
    public static String username_user = "";
    public static String address_user = "";
    public static String rol_user = "";
    public static String email_user = "";
    public static String telephone_user = "";

   
    //metodo del login
    public Employes loginQuery(String user, String password){
        String query = "SELECT * FROM employs WHERE username = ? AND password = ?";
        Employes employes = new Employes();
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            //enviar parametros
            pst.setString(1, user);
            pst.setString(2, password);
            rs = pst.executeQuery();
            
            if(rs.next()){
                employes.setId(rs.getInt("id"));
                id_user = employes.getId();
                employes.setFull_name(rs.getString("full_name"));
                full_name_user = employes.getFull_name();
                employes.setUsername(rs.getString("username"));
                username_user = employes.getUsername();
                employes.setAddress(rs.getString("address"));
                address_user = employes.getAddress();
                employes.setRol(rs.getString("rol"));
                rol_user = employes.getRol();
                employes.setEmail(rs.getString("email"));
                email_user = employes.getEmail();
                employes.setTelephone(rs.getString("telephone"));
                telephone_user = employes.getTelephone();              
                
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al obtener al empleado" + e);
        }
        return employes;
    }
    
    //Registrar empleados
    public boolean registerEmploysQuery(Employes employs){
        String query = "INSERT INTO employs(id, full_name, username, address, telephone, email, password, rol, created,"
                +"updated) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, employs.getId());
            pst.setString(2, employs.getFull_name());
            pst.setString(3, employs.getUsername());
            pst.setString(4, employs.getAddress());
            pst.setString(5, employs.getTelephone());
            pst.setString(6, employs.getEmail());
            pst.setString(7, employs.getPassword());
            pst.setString(8, employs.getRol());
            pst.setTimestamp(9, datetime);
            pst.setTimestamp(10, datetime);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al registrar el empleado " + e);
            return false;
        }
    }
    
    //listar empleados
    public List ListEmploysQuery(String value){
        List<Employes> list_employs = new ArrayList();
        String query = "SELECT * FROM employs ORDER BY rol ASC";
        String query_search_employs = "SELECT * FROM employs WHERE id LIKE '%" + value + "%'";
        try{
            conn = cn.getConection();
            if(value.equalsIgnoreCase("")){
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            }else{
                pst = conn.prepareStatement(query_search_employs);
                rs = pst.executeQuery();
            }
            
         while(rs.next()){
             Employes employs = new Employes();
             employs.setId(rs.getInt("id"));
             employs.setFull_name(rs.getString("full_name"));
             employs.setUsername(rs.getString("username"));
             employs.setAddress(rs.getString("address"));
             employs.setTelephone(rs.getString("telephone"));
             employs.setEmail(rs.getString("email"));
             employs.setRol(rs.getString("rol"));
             list_employs.add(employs);
         }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_employs;
    }
    
    //modificar empleados
    public boolean updateEmploysquery(Employes employs){
        String query = "UPDATE employs SET full_name = ?, username = ?, address = ?, telephone = ?, email = ?, rol = ?, updated = ?"
                + "WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setString(1, employs.getFull_name());
            pst.setString(2, employs.getUsername());
            pst.setString(3, employs.getAddress());
            pst.setString(4, employs.getTelephone());
            pst.setString(5, employs.getEmail());
            pst.setString(6, employs.getRol());
            pst.setTimestamp(7, datetime);
            pst.setInt(8, employs.getId());
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del el empleado " + e);
            return false;
        }
    }
    
    //Eliminar empleado
    public boolean deleteEmploysQuery(int id){
        String query = "DELETE FROM employs WHERE id = " + id;
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"No puede eliminar un empleado que tenga relacion con otra tabla");
            return false;
        }
    }
    
    //cambiar la contraseña
    public boolean updateEmploysPassword(Employes employs){
        String query = "UPDATE employs SET password = ? WHERE username = '" + username_user + "'";
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setString(1, employs.getPassword());
            pst.executeUpdate();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar la contraseña" + e);
            return false;
        }
    }    
}

