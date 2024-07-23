
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


public class CategoriesDao {
    //instanciar la conexion
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //registrar categorias
    public boolean registerCategoriesQuery(Categories categories){
        String query = "INSERT INTO categories(name, created, updated) VALUES(?, ?, ?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setString(1, categories.getName());
            pst.setTimestamp(2, datetime);
            pst.setTimestamp(3, datetime);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al registrar la categoria " + e);
            return false;
        }
    }
    
    //listar categorias
     public List ListCategories(String value){
        List<Categories> list_categories = new ArrayList();
        String query = "SELECT * FROM categories";
        String query_search_categories = "SELECT * FROM categories WHERE name LIKE '%" + value + "%'";
        try{
            conn = cn.getConection();
            if(value.equalsIgnoreCase("")){
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            }else{
                pst = conn.prepareStatement(query_search_categories);
                rs = pst.executeQuery();
            }
            
         while(rs.next()){
             Categories categories = new Categories();
             categories.setId(rs.getInt("id"));
             categories.setName(rs.getString("name"));
             list_categories.add(categories);
         }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_categories;
    }
     
    //modificar categoria
     public boolean updateCategoriesquery(Categories categories){
        String query = "UPDATE categories SET name = ?, updated = ? WHERE id = ?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setString(1, categories.getName());
            pst.setTimestamp(2, datetime);
            pst.setInt(3, categories.getId());
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al modificar los datos la categoria " + e);
            return false;
        }
    }
     
     //eliminar categoria
     public boolean deleteCategoriesQuery(int id){
        String query = "DELETE FROM categories WHERE id = " + id;
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"No puede eliminar una categoria que tenga relacion con otra tabla");
            return false;
        }
    }
}
