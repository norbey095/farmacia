
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


public class CustomersDao {
    //instanciar la conexion
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //Registrar clliente
     public boolean registerCustomersQuery(Customers customers){
        String query = "INSERT INTO customers(id, full_name, address, telephone, email, created, updated)" 
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, customers.getId());
            pst.setString(2, customers.getFull_name());
            pst.setString(3, customers.getAddress());
            pst.setString(4, customers.getTelephone());
            pst.setString(5, customers.getEmail());
            pst.setTimestamp(6, datetime);
            pst.setTimestamp(7, datetime);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al registrar al Cliente " + e);
            return false;
        }
    }
     
     //listar clientes
     public List ListCustomersQuery(String value){
        List<Customers> list_customers = new ArrayList();
        String query = "SELECT * FROM customers";
        String query_search_customer = "SELECT * FROM customers WHERE id LIKE '%" + value + "%'";
        try{
            conn = cn.getConection();
            if(value.equalsIgnoreCase("")){
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            }else{
                pst = conn.prepareStatement(query_search_customer);
                rs = pst.executeQuery();
            }
            
         while(rs.next()){
             Customers customers = new Customers();
             customers.setId(rs.getInt("id"));
             customers.setFull_name(rs.getString("full_name"));
             customers.setAddress(rs.getString("address"));
             customers.setTelephone(rs.getString("telephone"));
             customers.setEmail(rs.getString("email"));
             list_customers.add(customers);
         }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_customers;
    }
     
     //modificar clientes
     public boolean updateCustomersquery(Customers customers){
        String query = "UPDATE customers SET full_name = ?, address = ?, telephone = ?, email = ?, updated = ?"
                + "WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setString(1, customers.getFull_name());
            pst.setString(2, customers.getAddress());
            pst.setString(3, customers.getTelephone());
            pst.setString(4, customers.getEmail());
            pst.setTimestamp(5, datetime);
            pst.setInt(6, customers.getId());
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del cliente " + e);
            return false;
        }
    }
     
     //eliminar clientes
      public boolean deleteCustomersQuery(int id){
        String query = "DELETE FROM customers WHERE id = " + id;
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"No puede eliminar un cliente que tenga relacion con otra tabla");
            return false;
        }
    }
      
      //buscar un  cliente por id
     public Customers searchCustomer(int value){
        String query = "SELECT * FROM customers WHERE id = " + value;
         Customers customers = new Customers();
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
         while(rs.next()){             
             customers.setId(rs.getInt("id"));
             customers.setFull_name(rs.getString("full_name"));
             customers.setAddress(rs.getString("address"));
             customers.setTelephone(rs.getString("telephone"));
             customers.setEmail(rs.getString("email"));
         }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return customers;
    }

}
