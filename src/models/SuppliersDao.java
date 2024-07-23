
package models;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;


public class SuppliersDao {
    //instanciar la conexion
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //Registrar proveedores
    public boolean registerSuppliersQuery(Suppliers suppliers){
        String query = "INSERT INTO subppliers(name, description, address, telephone, email, city, created, updated)" 
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setString(1, suppliers.getName());
            pst.setString(2, suppliers.getDescription());
            pst.setString(3, suppliers.getAddress());
            pst.setString(4, suppliers.getTelephone());
            pst.setString(5, suppliers.getEmail());
            pst.setString(6, suppliers.getCity());
            pst.setTimestamp(7, datetime);
            pst.setTimestamp(8, datetime);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al registrar al proveedor " + e);
            return false;
        }
    }
    
    //listar proveedores
    public List ListSuppliersQuery(String value){
        List<Suppliers> list_suppliers = new ArrayList();
        String query = "SELECT * FROM subppliers";
        String query_search_suppliers = "SELECT * FROM subppliers WHERE name LIKE '%" + value + "%'";
        try{
            conn = cn.getConection();
            if(value.equalsIgnoreCase("")){
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            }else{
                pst = conn.prepareStatement(query_search_suppliers);
                rs = pst.executeQuery();
            }
            
         while(rs.next()){
             Suppliers suppliers = new Suppliers();
             suppliers.setId(rs.getInt("id"));
             suppliers.setName(rs.getString("name"));
             suppliers.setDescription(rs.getString("description"));
             suppliers.setAddress(rs.getString("address"));
             suppliers.setTelephone(rs.getString("telephone"));
             suppliers.setEmail(rs.getString("email"));
             suppliers.setCity(rs.getString("city"));
             list_suppliers.add(suppliers);
         }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_suppliers;
    }
    
    //modificar proveedor
    public boolean updateSuppliersquery(Suppliers suppliers){
        String query = "UPDATE subppliers SET name = ?,description = ?, address = ?, telephone = ?,"
                + "email = ?, city = ?, updated = ?"
                + "WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setString(1, suppliers.getName());
            pst.setString(2, suppliers.getDescription());
            pst.setString(3, suppliers.getAddress());
            pst.setString(4, suppliers.getTelephone());
            pst.setString(5, suppliers.getEmail());
            pst.setString(6, suppliers.getCity());
            pst.setTimestamp(7, datetime);
            pst.setInt(8, suppliers.getId());
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del proveedor " + e);
            return false;
        }
    }
    
    //eliminar proveedor
     public boolean deleteSuppliersQuery(int id){
        String query = "DELETE FROM subppliers WHERE id = " + id;
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"No puede eliminar un proveedor que tenga relacion con otra tabla");
            return false;
        }
    }
}
