
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
import models.Employes;
import models.EmployesDao;
import static models.EmployesDao.id_user;
import static models.EmployesDao.rol_user;
import views.SystemView;


public class EmployesController implements ActionListener, MouseListener, KeyListener{
    private Employes employes;
    private EmployesDao employesDao;
    private SystemView views;
    
    //rol
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public EmployesController(Employes employes, EmployesDao employesDao, SystemView views) {
        this.employes = employes;
        this.employesDao = employesDao;
        this.views = views;
        //boton de registrar empleados
        this.views.btn_register_employs.addActionListener(this);
        //boton de modificar empleado
        this.views.btn_update_employs.addActionListener(this);
        //boton de eliminar empleados
        this.views.btn_delete_employs.addActionListener(this);
        //Boton de cancelar
        this.views.btn_cancel_employs.addActionListener(this);
        //boton de cambiar contraseña
        this.views.btn_modify_data.addActionListener(this);
        //colocar label en escucha
        this.views.jlabelemplois.addMouseListener(this);
        this.views.employs_table.addMouseListener(this);
        this.views.txt_search_employs.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_register_employs){
            //verificar si los campos estan vacios
            if(views.txt_employs_id.getText().equals("")
                    || views.txt_employs_full_name.getText().equals("")
                    || views.txt_employs_username.getText().equals("")
                    || views.txt_employs_addres.getText().equals("")
                    || views.txt_employs_telefon.getText().equals("")
                    || views.txt_employs_email.getText().equals("")
                    || views.cmb_rol.getSelectedItem().toString().equals("")
                    || String.valueOf(views.txt_employs_paswor.getPassword()).equals("")){
                JOptionPane.showMessageDialog(null, "todos los campos son obligatorios");
            }else{
                //reallizar la insercion
                employes.setId(Integer.parseInt(views.txt_employs_id.getText().trim()));
                employes.setFull_name(views.txt_employs_full_name.getText().trim());
                employes.setAddress(views.txt_employs_addres.getText().trim());
                employes.setUsername(views.txt_employs_username.getText().trim());
                employes.setTelephone(views.txt_employs_telefon.getText().trim());
                employes.setEmail(views.txt_employs_email.getText().trim());
                employes.setPassword(String.valueOf(views.txt_employs_paswor.getPassword()));
                employes.setRol(views.cmb_rol.getSelectedItem().toString());
                
                if(employesDao.registerEmploysQuery(employes)){
                    cleanTable();
                    cleanFields();
                    lisAllEmployes();
                    JOptionPane.showMessageDialog(null, "Empleado registrado con exito");
                }else{
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar al empleado");
                }
            }
        }else if(e.getSource() == views.btn_update_employs){
            if(views.txt_employs_id.equals("")){
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            }else{
                //verificar si los campos estan vacios
                if(views.txt_employs_id.getText().equals("")
                   || views.txt_employs_full_name.getText().equals("")
                   || views.cmb_rol.getSelectedItem().toString().equals("")){
                    
                  JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");  
                }else{
                    employes.setId(Integer.parseInt(views.txt_employs_id.getText().trim()));
                    employes.setFull_name(views.txt_employs_full_name.getText().trim());
                    employes.setAddress(views.txt_employs_addres.getText().trim());
                    employes.setUsername(views.txt_employs_username.getText().trim());
                    employes.setTelephone(views.txt_employs_telefon.getText().trim());
                    employes.setEmail(views.txt_employs_email.getText().trim());
                    employes.setPassword(String.valueOf(views.txt_employs_paswor.getPassword()));
                    employes.setRol(views.cmb_rol.getSelectedItem().toString());
                    
                    if(employesDao.updateEmploysquery(employes)){
                        cleanTable();
                        cleanFields();
                        lisAllEmployes();
                        views.btn_register_employs.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del empleado modificado con exito");
                    }else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar al empleado");
                    }
                }
            }
        }else if(e.getSource() == views.btn_delete_employs){
            int row = views.employs_table.getSelectedRow();
            
            if(row == -1){
               JOptionPane.showMessageDialog(null, "Debes seleccionar un empleado para eliminar"); 
            }else if(views.employs_table.getValueAt(row, 0).equals(id_user)){
               JOptionPane.showMessageDialog(null, "No puede eliminar al usuario autenticado"); 
            }else{
                int id = Integer.parseInt(views.employs_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar a este empleado?");
                
                if(question == 0 && employesDao.deleteEmploysQuery(id) != false){
                    cleanTable();
                    cleanFields();
                    views.btn_register_employs.setEnabled(true);
                    views.txt_employs_paswor.setEnabled(true);
                    lisAllEmployes();
                    JOptionPane.showMessageDialog(null, "Empleado eliminado con exito");
                }
            }
        }else if(e.getSource() == views.btn_cancel_employs){
            cleanFields();
            views.btn_register_employs.setEnabled(true);
            views.txt_employs_paswor.setEnabled(true);
            views.txt_employs_id.setEnabled(true);
        }else if(e.getSource() == views.btn_modify_data){
            //Recoletar informacion de las cajas pasword
            String password = String.valueOf(views.txt_paswor_modify.getPassword());
            String confirm_password = String.valueOf(views.txt_paswor_modify_confirm.getPassword());
            
            //VERIFICAR SI LAS CAJAS DE TEXTO ESTAN VACIAS
            if(!password.equals("") && !confirm_password.equals("")){
                //VERIFICAR QUE LAS CONTRASEÑAS SEAN IGUALES
                if(password.equals(confirm_password)){
                    employes.setPassword(String.valueOf(views.txt_paswor_modify.getPassword()));
                    
                    if(employesDao.updateEmploysPassword(employes) != false){
                        JOptionPane.showMessageDialog(null, "Contraseña modificada con exito");
                    }else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar la contraseña");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            }
        }
    }
    
    //listar todos los empleados
    public void lisAllEmployes(){
        if(rol.equals("Administrador")){
            List<Employes> list = employesDao.ListEmploysQuery(views.txt_search_employs.getText());
            model = (DefaultTableModel) views.employs_table.getModel();
            Object[] row = new Object[7];
            for(int i = 0; i < list.size(); i++){
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getFull_name();
                row[2] = list.get(i).getUsername();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getRol();
                model.addRow(row);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.employs_table){
            int row = views.employs_table.rowAtPoint(e.getPoint());
            
            views.txt_employs_id.setText(views.employs_table.getValueAt(row, 0).toString());
            views.txt_employs_full_name.setText(views.employs_table.getValueAt(row, 1).toString());
            views.txt_employs_username.setText(views.employs_table.getValueAt(row, 2).toString());
            views.txt_employs_addres.setText(views.employs_table.getValueAt(row, 3).toString());
            views.txt_employs_telefon.setText(views.employs_table.getValueAt(row, 4).toString());
            views.txt_employs_email.setText(views.employs_table.getValueAt(row, 5).toString());
            views.cmb_rol.setSelectedItem(views.employs_table.getValueAt(row, 6).toString());
            
            //DESHABILITAR
            views.txt_employs_id.setEditable(false);
            views.txt_employs_paswor.setEnabled(false);
            views.btn_register_employs.setEnabled(false);
            
        }else if(e.getSource() == views.jlabelemplois){
            if(rol.equals("Administrador")){
                views.jTabbedPane1.setSelectedIndex(4);
                //limpiar tabla
                cleanTable();
                //limpiar campos
                cleanFields();
                //listar empleados
                lisAllEmployes();
            }else{
                views.jTabbedPane1.setEnabledAt(4, false);
                views.jlabelemplois.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tienes privilegios de administrador para acceder a esta vista");
            }
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
        if(e.getSource() == views.txt_search_employs){
            cleanTable();
            lisAllEmployes();
        }
    }
    
    public void cleanFields(){
        views.txt_employs_id.setText("");
        views.txt_employs_id.setEditable(true);
        views.txt_employs_full_name.setText("");
        views.txt_employs_username.setText("");
        views.txt_employs_addres.setText("");
        views.txt_employs_telefon.setText("");
        views.txt_employs_email.setText("");
        views.txt_employs_paswor.setText("");
        views.cmb_rol.setSelectedIndex(0);
    }
    
    public void cleanTable(){
        for(int i = 0; i < model.getRowCount(); i++){
            model.removeRow(i);
            i = i - 1;
        }
    }
}
