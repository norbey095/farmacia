
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
import models.Customers;
import models.CustomersDao;
import views.SystemView;


public class CustomersController implements ActionListener, MouseListener, KeyListener{
    private Customers customer;
    private CustomersDao customerDao;
    private SystemView views;
    
    DefaultTableModel model = new DefaultTableModel();

    public CustomersController(Customers customer, CustomersDao customerDao, SystemView views) {
        this.customer = customer;
        this.customerDao = customerDao;
        this.views = views;
        //Boton de registrar cliente
        this.views.btn_register_customer.addActionListener(this);
        //boton de modificar cliente
        this.views.btn_update_customer.addActionListener(this);
        //boton de eliminar empleados
        this.views.btn_delete_customer.addActionListener(this);
        //Boton de cancelar
        this.views.btn_cancel_customer.addActionListener(this);
        //colocar label en escucha
        this.views.jlabelemplois.addMouseListener(this);
        //buscador
        this.views.txt_search_customer.addKeyListener(this);
        this.views.jlabelcustoner.addMouseListener(this);
        this.views.customer_table.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_register_customer){
            //verificar si los campos estan vacios
            if(views.txt_customer_Id.getText().equals("")
                    || views.txt_customer_full_name.getText().equals("")
                    || views.txt_customer_addres.getText().equals("")
                    || views.txt_customer_telefon.getText().equals("")
                    || views.txt_customer_email.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            }else{
                customer.setId(Integer.parseInt(views.txt_customer_Id.getText().trim()));
                customer.setFull_name(views.txt_customer_full_name.getText().trim());
                customer.setAddress(views.txt_customer_addres.getText().trim());
                customer.setTelephone(views.txt_customer_telefon.getText().trim());
                customer.setEmail(views.txt_customer_email.getText().trim());
                
                if(customerDao.registerCustomersQuery(customer)){
                    cleanTable();
                    lisAllCustomers();
                    JOptionPane.showMessageDialog(null, "Cliente registrado con exito");
                }else{
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar al cliente");
                }
            }
        }else if(e.getSource() == views.btn_update_customer){
            if(views.txt_customer_Id.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            }else{
                //verificar si los campos estan vacios
                if(views.txt_customer_Id.getText().equals("")
                   || views.txt_customer_full_name.getText().equals("")
                   || views.txt_customer_addres.getText().equals("")
                   || views.txt_customer_telefon.getText().equals("")
                   || views.txt_customer_email.getText().equals("")){                    
                  JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");  
                }else{
                customer.setId(Integer.parseInt(views.txt_customer_Id.getText().trim()));    
                    customer.setFull_name(views.txt_customer_full_name.getText().trim());
                    customer.setAddress(views.txt_customer_addres.getText().trim());
                    customer.setTelephone(views.txt_customer_telefon.getText().trim());
                    customer.setEmail(views.txt_customer_email.getText().trim());                    
                    if(customerDao.updateCustomersquery(customer)){
                        cleanTable();
                        cleanFields();
                        lisAllCustomers();
                        views.btn_register_customer.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del cliente modificados con exito");
                    }else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos del cliente");
                    }
                }
            }
        }else if(e.getSource() == views.btn_delete_customer){
            int row = views.customer_table.getSelectedRow();            
            if(row == -1){
               JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente para eliminar"); 
            }else{
                int id = Integer.parseInt(views.customer_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar a este cliente?");
                
                if(question == 0 && customerDao.deleteCustomersQuery(id) != false){
                    cleanTable();
                    cleanFields();
                    views.btn_register_customer.setEnabled(true);
                    lisAllCustomers();
                    JOptionPane.showMessageDialog(null, "Cliente eliminado con exito");
                }
            }
        }else if(e.getSource() == views.btn_cancel_customer){            
            views.btn_register_customer.setEnabled(true);
            cleanFields();
        }
    }
    
    //listar clientes
    public void lisAllCustomers(){
        List<Customers> list = customerDao.ListCustomersQuery(views.txt_search_customer.getText());
        model = (DefaultTableModel) views.customer_table.getModel();
        Object[] row = new Object[5];
        for(int i = 0; i < list.size(); i++){
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getFull_name();
            row[2] = list.get(i).getAddress();
            row[3] = list.get(i).getTelephone();
            row[4] = list.get(i).getEmail();
            model.addRow(row);            
        }
        views.customer_table.setModel(model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.customer_table){
            int row = views.customer_table.rowAtPoint(e.getPoint());
            views.txt_customer_Id.setText(views.customer_table.getValueAt(row, 0).toString());
            views.txt_customer_full_name.setText(views.customer_table.getValueAt(row, 1).toString());
            views.txt_customer_addres.setText(views.customer_table.getValueAt(row, 2).toString());
            views.txt_customer_telefon.setText(views.customer_table.getValueAt(row, 3).toString());
            views.txt_customer_email.setText(views.customer_table.getValueAt(row, 4).toString());
            //deshabilitar botones
            views.btn_register_customer.setEnabled(false);
            views.txt_customer_Id.setEditable(false);
        }else if(e.getSource() == views.jlabelcustoner){
                views.jTabbedPane1.setSelectedIndex(3);
                //limpiar tabla
                cleanTable();
                //limpiar campos
                cleanFields();
                //listar empleados
                lisAllCustomers();
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
        if(e.getSource() == views.txt_search_customer){
            //limpiar la tabla
            cleanTable();
            //listar los clientes
            lisAllCustomers();
        }
    }
    
    public void cleanFields(){
        views.txt_customer_Id.setText("");
        views.txt_customer_Id.setEditable(true);
        views.txt_customer_full_name.setText("");
        views.txt_customer_addres.setText("");
        views.txt_customer_telefon.setText("");
        views.txt_customer_email.setText("");
    }
    
    public void cleanTable(){
        for(int i = 0; i < model.getRowCount(); i++){
            model.removeRow(i);
            i = i - 1;
        }
    }
}
