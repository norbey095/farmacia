
package controlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.DynamicCombobox;
import static models.EmployesDao.rol_user;
import models.Products;
import models.ProductsDao;
import views.SystemView;


public class ProductsController implements ActionListener, MouseListener, KeyListener{
    private Products product;
    private ProductsDao productsDao;
    private SystemView views;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public ProductsController(Products product, ProductsDao productsDao, SystemView views) {
        this.product = product;
        this.productsDao = productsDao;
        this.views = views;
        //boton de registrar productos
        this.views.btn_registro_products.addActionListener(this);
        //botn de modificar productos
        this.views.btn_update_products.addActionListener(this);
        //boton de eliminar productos
        this.views.btn_delete_products.addActionListener(this);
        //Boton de cancelar
        this.views.btn_cancel_products.addActionListener(this);
        this.views.products_table.addMouseListener(this);
        this.views.txt_search_products.addKeyListener(this);
        this.views.jlabelproducts.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_registro_products){
        //verificar si los campos estan vacios
            if(views.txt_product_code.getText().equals("")
                    || views.txt_products_name.getText().equals("")
                    || views.txt_products_descrption.getText().equals("")
                    || views.txt_products_price.getText().equals("")
                    || views.cmb_products_categori.getSelectedItem().toString().equals("")){
                JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
            }else{
                //reallizar la insercion
                product.setCode(Integer.parseInt(views.txt_product_code.getText()));
                product.setName(views.txt_products_name.getText().trim());
                product.setDescription(views.txt_products_descrption.getText().trim());
                product.setUnit_price(Double.parseDouble(views.txt_products_price.getText()));
                DynamicCombobox category_id = (DynamicCombobox) views.cmb_products_categori.getSelectedItem();
                product.setCategory_id(category_id.getId());
                if(productsDao.registerProductsQuery(product)){
                    cleanTable();
                    cleanFields();
                    listAllProducts();
                    JOptionPane.showMessageDialog(null, "Producto registrado con exito");
                }else{
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar el producto");
                }
            }
        }else if(e.getSource() == views.btn_update_products){
            //verificar si los campos estan vacios
            if(views.txt_product_code.getText().equals("")
               || views.txt_products_name.getText().equals("")
               || views.txt_products_descrption.getText().equals("")
               || views.txt_products_price.getText().equals("")
               || views.cmb_products_categori.getSelectedItem().toString().equals("")){                    
              JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");  
            }else{
                product.setCode(Integer.parseInt(views.txt_product_code.getText()));
                product.setName(views.txt_products_name.getText().trim());
                product.setDescription(views.txt_products_descrption.getText().trim());
                product.setUnit_price(Double.parseDouble(views.txt_products_price.getText()));
                //obtener el id de la categoria
                DynamicCombobox category_id = (DynamicCombobox) views.cmb_products_categori.getSelectedItem();
                product.setCategory_id(category_id.getId());
                product.setId(Integer.parseInt(views.txt_products_id.getText()));
                if(productsDao.updateProductsQuery(product)){
                    cleanTable();
                    cleanFields();
                    listAllProducts();
                    views.btn_registro_products.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Datos del producto modificados con exito");
                }else{
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos del producto");
                }
            }
        }else if(e.getSource() == views.btn_delete_products){
            int row = views.products_table.getSelectedRow();            
            if(row == -1){
               JOptionPane.showMessageDialog(null, "Debes seleccionar un producto para eliminar"); 
            }else{
                int id = Integer.parseInt(views.products_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar a este producto?");
                
                if(question == 0 && productsDao.deleteProductsQuery(id) != false){
                    cleanTable();
                    cleanFields();
                    views.btn_registro_products.setEnabled(true);
                    listAllProducts();
                    JOptionPane.showMessageDialog(null, "Producto eliminado con exito");
                }
            }
        }else if(e.getSource() == views.btn_cancel_products){            
            views.btn_registro_products.setEnabled(true);
            cleanFields();
        }
    }
    
    //listar productos
    public void listAllProducts(){
        if(rol.equals("Administrador") || rol.equals("Auxiliar")){
            List<Products> list = productsDao.ListProductsQuery(views.txt_search_products.getText());
            model = (DefaultTableModel) views.products_table.getModel();
            Object[] row = new Object[7];
            for(int i = 0; i < list.size(); i++){
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCode();
                row[2] = list.get(i).getName();
                row[3] = list.get(i).getDescription();
                row[4] = list.get(i).getUnit_price();
                row[5] = list.get(i).getProduct_quantity();
                row[6] = list.get(i).getCategory_name();
                model.addRow(row);
            }
            views.products_table.setModel(model);
            
            if (rol.equals("Auxiliar")){
                views.btn_registro_products.setEnabled(false);
                views.btn_update_products.setEnabled(false);
                views.btn_delete_products.setEnabled(false);
                views.btn_cancel_products.setEnabled(false);
                views.txt_product_code.setEnabled(false);
                views.txt_products_descrption.setEnabled(false);
                views.txt_products_id.setEditable(false);
                views.txt_products_name.setEditable(false);
                views.txt_products_price.setEditable(false);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.products_table){
            int row = views.products_table.rowAtPoint(e.getPoint());
            
            views.txt_products_id.setText(views.products_table.getValueAt(row, 0).toString());
            product = productsDao.searchProduct(Integer.parseInt(views.txt_products_id.getText()));
            views.txt_product_code.setText(""+ product.getCode());
            views.txt_products_name.setText(product.getName());
            views.txt_products_descrption.setText(product.getDescription());
            views.txt_products_price.setText(""+product.getUnit_price());
            views.cmb_products_categori.setSelectedItem(new DynamicCombobox(product.getCategory_id(), product.getCategory_name()));
            //DESHABILITAR
            views.btn_registro_products.setEnabled(false);            
        }else if(e.getSource() == views.jlabelproducts){
                views.jTabbedPane1.setSelectedIndex(0);
                //limpiar tabla
                cleanTable();
                //limpiar campos
                cleanFields();
                //listar PRODUCTOS
                listAllProducts();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    
    }

    @Override
    public void mouseExited(MouseEvent e) {
    
    }

    @Override
    public void keyTyped(KeyEvent e) {
    
    }

    @Override
    public void keyPressed(KeyEvent e) {
    
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource() == views.txt_search_products){
            cleanTable();
            listAllProducts();
        }
    }
    
    public void cleanFields(){
        views.txt_products_id.setText("");
        views.txt_products_id.setEditable(true);
        views.txt_products_name.setText("");
        views.txt_product_code.setText("");
        views.txt_products_descrption.setText("");
        views.txt_products_price.setText("");;
    }
    
    public void cleanTable(){
        for(int i = 0; i < model.getRowCount(); i++){
            model.removeRow(i);
            i = i - 1;
        }
    }
    
    
}
