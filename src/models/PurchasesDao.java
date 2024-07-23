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


public class PurchasesDao {
        //instanciar la conexion
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    //registrar compra
    public boolean registerPurchaseQuery(int supplier_id, int employes_id, double total){
        String query = "INSERT INTO purchases(subplier_id, employe_id, total, created)"
                + "VALUES(?, ?, ?, ?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, supplier_id);
            pst.setInt(2, employes_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, datetime);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al insertar la compra " + e);
            return false;
        }
    }
    
    //registrar los detalles de la compra
    public boolean registerPurchaseDetailQuery(int purchase_id, double purchase_price, int purchase_amount,
                                               double purchase_subtotal, int product_id){
        String query = "INSERT INTO purchase_detalls(purchase_id, purchase_price, purchase_amount,"
                + "purchase_subtotal, products_id) VALUES(?, ?, ?, ?, ?)";
        
         Timestamp datetime = new Timestamp(new Date().getTime());
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, purchase_id);
            pst.setDouble(2, purchase_price);
            pst.setInt(3, purchase_amount);
            pst.setDouble(4, purchase_subtotal);
            pst.setInt(5, product_id);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al registrar los detalles de la compra " + e);
            return false;
        }
    }
    
    //obtener Id de la compra
    public int purchaseId(){
        int id = 0;
        String query = "SELECT MAX(id) AS id FROM purchases";
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
    
    //Listar todas las comprar realizadas
    public List listAllPurchasesQuery(){
        List<Purchases> list_purchases = new ArrayList();
        String query = "SELECT pu.*, su.name AS subppliers_name FROM purchases pu, subppliers su "
                +"WHERE pu.subplier_id = su.id ORDER BY pu.id ASC";
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                Purchases purchases = new Purchases();
                purchases.setId(rs.getInt("id"));
                purchases.setSupplier_name_product(rs.getString("subppliers_name"));
                purchases.setTotal(rs.getDouble("total"));
                purchases.setCreated(rs.getString("created"));
                list_purchases.add(purchases);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_purchases;
    }
    
    //listar compras para imprimir facturas
    public List listPurchasesDetailQuery(int id){
        List<Purchases> list_purchases = new ArrayList();
        String query = "SELECT pu.created, pude.purchase_price, pude.purchase_amount, pude.purchase_subtotal, su.name AS subpplier_name, pro.name AS "
                + "products_name, em.full_name FROM purchases pu INNER JOIN purchase_detalls pude ON pu.id = pude.purchase_id "
                + "INNER JOIN products pro On pude.products_id = pro.id INNER JOIN subppliers su ON pu.subplier_id = su.id "
                + "INNER JOIN  employs em ON pu.employe_id = em.id WHERE pu.id = ?";
        try{
            conn = cn.getConection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while(rs.next()){
                Purchases purchases = new Purchases();
                purchases.setProduct_name(rs.getString("products_name"));
                purchases.setPurchase_amount(rs.getInt("purchase_amount"));
                purchases.setPurchase_prace(rs.getDouble("purchase_price"));
                purchases.setPurchase_subtotal(rs.getDouble("purchase_subtotal"));
                purchases.setSupplier_name_product(rs.getString("subpplier_name"));
                purchases.setCreated(rs.getString("created"));
                purchases.setPurcharser(rs.getString("full_name"));
                list_purchases.add(purchases);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_purchases;
    }
}
