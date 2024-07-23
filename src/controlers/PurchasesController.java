
package controlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.DynamicCombobox;
import static models.EmployesDao.id_user;
import static models.EmployesDao.rol_user;
import models.Products;
import models.ProductsDao;
import models.Purchases;
import models.PurchasesDao;
import views.Print;
import views.SystemView;


public class PurchasesController implements KeyListener, ActionListener, MouseListener{
    private Purchases purchases;
    private PurchasesDao purchasesDao;
    private SystemView views;
    private int getIdSupplier = 0;
    private int item = 0;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;    
    //instanciar el modelo productos
    Products product = new Products();
    ProductsDao productsDao = new ProductsDao();
    String rol = rol_user;

    public PurchasesController(Purchases purchases, PurchasesDao purchasesDao, SystemView views) {
        this.purchases = purchases;
        this.purchasesDao = purchasesDao;
        this.views = views;
        //boton de agregar
        this.views.btn_add_products_to_buy.addActionListener(this);
        //boton de comprar
        this.views.btn_config_purchase.addActionListener(this);
        //botn de eliminar compra
        this.views.btn_removie_purchase.addActionListener(this);
        this.views.txt_purchase_products_code.addKeyListener(this);
        this.views.txt_purchase_price.addKeyListener(this);
        this.views.btn_new_purchase.addActionListener(this);
        this.views.jlabelpuchases.addMouseListener(this);
        this.views.jlabelreports.addMouseListener(this);

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_add_products_to_buy){
            DynamicCombobox supplier_cmb = (DynamicCombobox) views.cmb_purcharse_subplayers.getSelectedItem();
            int supplier_id = supplier_cmb.getId();
            
            if(getIdSupplier == 0){
                getIdSupplier = supplier_id;
            }else{
                if(getIdSupplier != supplier_id){
                    JOptionPane.showMessageDialog(null, "No puede realizar una misma compra a varios proveedores");
                }else{
                    int amount = Integer.parseInt(views.txt_purchase_amaut.getText());
                    String product_name = views.txt_purchase_products_name.getText();
                    double price = Double.parseDouble(views.txt_purchase_price.getText());
                    int purchase_id = Integer.parseInt(views.txt_purchase_id.getText());
                    String supplier_name = views.cmb_purcharse_subplayers.getSelectedItem().toString();
                    
                    if(amount > 0){
                        temp = (DefaultTableModel) views.purchase_table.getModel();
                        for(int i = 0; i < views.purchase_table.getRowCount(); i++){
                            if(views.purchase_table.getValueAt(i, 1).equals(views.txt_purchase_products_name.getText())){
                                JOptionPane.showMessageDialog(null, "El producto ya esta registrado en la tabla de compras");
                                return;
                            }
                        }
                        
                        ArrayList list = new ArrayList();
                        item = 1;
                        list.add(item);
                        list.add(purchase_id);
                        list.add(product_name);
                        list.add(amount);
                        list.add(price);
                        list.add(amount * price);
                        list.add(supplier_name);
                        
                        Object[] obj = new Object[6];
                        obj[0] = list.get(1);
                        obj[1] = list.get(2);
                        obj[2] = list.get(3);
                        obj[3] = list.get(4);
                        obj[4] = list.get(5);
                        obj[5] = list.get(6);
                        temp.addRow(obj);
                        views.purchase_table.setModel(temp);
                        cleanFieldspurchases();
                        views.cmb_purcharse_subplayers.setEditable(false);
                        views.txt_purchase_products_code.requestFocus();
                        calculatepurchase();
                    }
                }
            }
        }else if(e.getSource() == views.btn_config_purchase){
            insertPurchase();
        }else if(e.getSource() == views.btn_removie_purchase){
            model = (DefaultTableModel) views.purchase_table.getModel();
            model.removeRow(views.purchase_table.getSelectedRow());
            calculatepurchase();
            views.txt_purchase_products_code.requestFocus();
        }else if(e.getSource() == views.btn_new_purchase){
            cleanTableTemp();
            cleanFieldspurchases();
        }
    }
    
    
    private void insertPurchase(){
        double total = Double.parseDouble(views.txt_purchase_total_type.getText());
        int employes_id = id_user;
        
        if(purchasesDao.registerPurchaseQuery(getIdSupplier, employes_id, total)){
            int purchase_id = purchasesDao.purchaseId();
            for(int i = 0; i < views.purchase_table.getRowCount(); i++){
                int product_id = Integer.parseInt(views.purchase_table.getValueAt(i, 0).toString());
                int purchase_amount = Integer.parseInt(views.purchase_table.getValueAt(i, 2).toString());
                double purchase_price = Double.parseDouble(views.purchase_table.getValueAt(i, 3).toString());
                double purchase_subtotal = purchase_price * purchase_amount;
                
                //registrar detalles de la compra
                purchasesDao.registerPurchaseDetailQuery(purchase_id, purchase_price, purchase_amount, purchase_subtotal, product_id);
                
                //traer la cantidad de productos
                product = productsDao.searchId(product_id);
                int amount = product.getProduct_quantity() + purchase_amount;
                
                productsDao.updateStockQuery(amount, product_id);
            }
            cleanTableTemp();
            JOptionPane.showMessageDialog(null, "Compra generada con exito");
            cleanFieldspurchases();
            Print print = new Print(purchase_id);
            print.setVisible(true);
        }
    }
    
    //metodo para listar las compras realizadas
    public void listAllPurchases(){
        if(rol.equals("Administrador") || rol.equals("Auxiliar")){
            List<Purchases> list = purchasesDao.listAllPurchasesQuery();
            model = (DefaultTableModel) views.table_alf_pulchases.getModel();
            Object[] row = new Object[4];
            //recorrer con ciclo for
            for(int i = 0; i < list.size(); i++){
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getSupplier_name_product();
                row[2] = list.get(i).getTotal();
                row[3] = list.get(i).getCreated();
                model.addRow(row);
            }
            views.table_alf_pulchases.setModel(model);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.jlabelpuchases){
            if(rol.equals("Administrador")){
                views.jTabbedPane1.setSelectedIndex(1);
                cleanTable();
            }else{
                views.jTabbedPane1.setEnabledAt(1, false);
                views.jlabelpuchases.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tiene privilegios de Administrador para ingresar a esta vista");
            }
        }else if(e.getSource() == views.jlabelreports){
            views.jTabbedPane1.setSelectedIndex(7);
            cleanTable();
            listAllPurchases();
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
        if(e.getSource() == views.txt_purchase_products_code){
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                if(views.txt_purchase_products_code.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Ingresa el codigo del producto a comprar");
                }else{
                    int id = Integer.parseInt(views.txt_purchase_products_code.getText());
                    product = productsDao.searchCode(id);
                    views.txt_purchase_products_name.setText(product.getName());
                    views.txt_purchase_id.setText("" + product.getId());
                    views.txt_purchase_amaut.requestFocus();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource() == views.txt_purchase_price){
            int quantity;
            double price = 0.0;
            
            if(views.txt_purchase_amaut.getText().equals("")){
                quantity = 1;
                views.txt_purchase_price.setText("" + price);
            }else{
                quantity = Integer.parseInt(views.txt_purchase_amaut.getText());
                price = Double.parseDouble(views.txt_purchase_price.getText());
                views.txt_purchase_subtotal.setText("" + quantity * price);
            }
        }
    }

     public void cleanFieldspurchases() {
        views.txt_purchase_products_name.setText("");
        views.txt_purchase_price.setText("");
        views.txt_purchase_amaut.setText("");
        views.txt_purchase_products_code.setText("");
        views.txt_purchase_subtotal.setText("");
        views.txt_purchase_id.setText("");
        views.txt_purchase_total_type.setText("");
    }
    
     //calcular total a pagar
    public void calculatepurchase(){
        double total = 0.00;
        int numRow = views.purchase_table.getRowCount();
        
        for(int i = 0; i < numRow; i++){
            //pasar el indice de la columna que se sumara
            total = total + Double.parseDouble(String.valueOf(views.purchase_table.getValueAt(i, 4)));
        }
        views.txt_purchase_total_type.setText("" + total);
    }
    
    //limpiar tabla temporal
    public void cleanTableTemp() {
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }
    }
    
    //limpiar tabla
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
}
