
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


public class SalesDao {
    //instanciar la conexion
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    public boolean registerSaleQuery(int customer_id, int employes_id, double total){
        String query = "INSERT INTO sales(customer_id, employes_id, total, sale_date) VALUES(?, ?, ?, ?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, customer_id);
            pst.setInt(2, employes_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, datetime);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
    
    //metodo registrar en la tabla sales detail
    public boolean registerSaleDetailQuery(int product_id, double sale_id, int sale_quantity, double sale_price, double sale_subtotal){
        String query = "INSERT INTO sale_details(product_id, sale_id, sale_quantity, sale_price, sale_subtotal)"
                + "VALUES(?, ?, ?, ?, ?)";
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, product_id);
            pst.setDouble(2, sale_id);
            pst.setInt(3, sale_quantity);
            pst.setDouble(4, sale_price);
            pst.setDouble(5, sale_subtotal);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
    
    //obtener id maximo de las ventas relacionando las tablas sales y sales_details
    public int salesId(){
        int id = 0;
        String query = "SELECT MAX(id) AS id FROM sales";
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            if(rs.next()){
                id = rs.getInt("id");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());        
        }
        return id;
    }
    
    //listar las ventas realizadas
    public List listAllSalesQuery(){
        List<Sales> list_sales = new ArrayList();
        String query = "SELECT s.id AS invoice, c.full_name AS customer_name, e.full_name AS employes_name, s.total,s.sale_date FROM sales s INNER JOIN "
                +"customers c ON s.customer_id = c.id INNER JOIN employs e ON s.employes_id = e.id ORDER BY s.id ASC";
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                Sales sales = new Sales();
                sales.setId(rs.getInt("invoice"));
                sales.setCustomer_name(rs.getString("customer_name"));
                sales.setEmployes_name(rs.getString("employes_name"));
                sales.setTotal_to_pay(rs.getDouble("total"));
                sales.setSale_date(rs.getString("sale_date"));
                list_sales.add(sales);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_sales;
    }
}
