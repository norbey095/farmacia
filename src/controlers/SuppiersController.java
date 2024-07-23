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
import models.Suppliers;
import models.SuppliersDao;
import views.SystemView;

public class SuppiersController implements ActionListener, MouseListener, KeyListener {

    private Suppliers supplier;
    private SuppliersDao suppliersDao;
    private SystemView views;
    String rol = rol_user;

    DefaultTableModel model = new DefaultTableModel();

    public SuppiersController(Suppliers supplier, SuppliersDao suppliersDao, SystemView views) {
        this.supplier = supplier;
        this.suppliersDao = suppliersDao;
        this.views = views;
        //Boton de registrar proveedor
        this.views.btn_register_subplayer.addActionListener(this);
        //boton de modificar proveedor
        this.views.btn_update_subplayer.addActionListener(this);
        //boton de eliminar proveedor
        this.views.btn_delet_subplayer.addActionListener(this);
        //boton de cancelar
        this.views.btn_cancel_subplayer.addActionListener(this);
        this.views.subplayer_table.addMouseListener(this);
        this.views.txt_search_subplayer.addKeyListener(this);
        this.views.jlabelsubplayers.addMouseListener(this);
        getSuppliersName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_subplayer) {
            //verificar si los campos estan vacios
            if (views.txt_subplayer_name.getText().equals("")
                    || views.txt_subplayer_description.getText().equals("")
                    || views.txt_subplayer_addres.getText().equals("")
                    || views.txt_subplayer_telef.getText().equals("")
                    || views.txt_subplayer_email.getText().equals("")
                    || views.cmb_subplayer_siri.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                supplier.setName(views.txt_subplayer_name.getText().trim());
                supplier.setDescription(views.txt_subplayer_description.getText().trim());
                supplier.setAddress(views.txt_subplayer_addres.getText().trim());
                supplier.setTelephone(views.txt_subplayer_telef.getText().trim());
                supplier.setEmail(views.txt_subplayer_email.getText().trim());
                supplier.setCity(views.cmb_subplayer_siri.getSelectedItem().toString());
                if (suppliersDao.registerSuppliersQuery(supplier)) {
                    cleanTable();
                    cleanFields();
                    lisAllSupplier();
                    JOptionPane.showMessageDialog(null, "Proveedor registrado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar al proveedor");
                }
            }
        } else if (e.getSource() == views.btn_update_subplayer) {
            if (views.txt_subplayer_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            } else if (views.txt_subplayer_name.getText().equals("")
                    || views.txt_subplayer_addres.getText().equals("")
                    || views.txt_subplayer_telef.getText().equals("")
                    || views.txt_subplayer_email.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                supplier.setName(views.txt_subplayer_name.getText().trim());
                supplier.setDescription(views.txt_subplayer_description.getText().trim());
                supplier.setAddress(views.txt_subplayer_addres.getText().trim());
                supplier.setTelephone(views.txt_subplayer_telef.getText().trim());
                supplier.setEmail(views.txt_subplayer_email.getText().trim());
                supplier.setCity(views.cmb_subplayer_siri.getSelectedItem().toString());
                supplier.setId(Integer.parseInt(views.txt_subplayer_id.getText().trim()));
                if (suppliersDao.updateSuppliersquery(supplier)) {
                    //limpiar tabla
                    cleanTable();
                    //limpiar acmpos
                    cleanFields();
                    //mostrar lista
                    lisAllSupplier();
                    views.btn_register_customer.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Datos del proveedor modificados con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos del proveedor");
                }
            }
        } else if (e.getSource() == views.btn_delet_subplayer) {
            int row = views.subplayer_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar un proveedor para eliminar");
            } else {
                int id = Integer.parseInt(views.subplayer_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar a este proveedor?");

                if (question == 0 && suppliersDao.deleteSuppliersQuery(id) != false) {
                    cleanTable();
                    cleanFields();
                    views.btn_register_subplayer.setEnabled(true);
                    lisAllSupplier();
                    JOptionPane.showMessageDialog(null, "Proveedor eliminado con exito");
                }
            }
        } else if (e.getSource() == views.btn_cancel_subplayer) {
            views.btn_register_subplayer.setEnabled(true);
            cleanFields();
        }

    }

    //listar provvedores
    public void lisAllSupplier() {
        List<Suppliers> list = suppliersDao.ListSuppliersQuery(views.txt_search_subplayer.getText());
        model = (DefaultTableModel) views.subplayer_table.getModel();
        Object[] row = new Object[7];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getName();
            row[2] = list.get(i).getDescription();
            row[3] = list.get(i).getAddress();
            row[4] = list.get(i).getTelephone();
            row[5] = list.get(i).getEmail();
            row[6] = list.get(i).getCity();
            model.addRow(row);
        }
        views.subplayer_table.setModel(model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.subplayer_table) {
            int row = views.subplayer_table.rowAtPoint(e.getPoint());
            views.txt_subplayer_id.setText(views.subplayer_table.getValueAt(row, 0).toString());
            views.txt_subplayer_name.setText(views.subplayer_table.getValueAt(row, 1).toString());
            views.txt_subplayer_description.setText(views.subplayer_table.getValueAt(row, 2).toString());
            views.txt_subplayer_addres.setText(views.subplayer_table.getValueAt(row, 3).toString());
            views.txt_subplayer_telef.setText(views.subplayer_table.getValueAt(row, 4).toString());
            views.txt_subplayer_email.setText(views.subplayer_table.getValueAt(row, 5).toString());
            views.cmb_subplayer_siri.setSelectedItem(views.subplayer_table.getValueAt(row, 6).toString());
            //deshabilitar botones
            views.btn_register_subplayer.setEnabled(false);
            views.txt_subplayer_id.setEditable(false);
        } else if (e.getSource() == views.jlabelsubplayers) {
            if (rol.equals("Administrador")) {
                views.jTabbedPane1.setSelectedIndex(5);
                //limpiar tabla
                cleanTable();
                //limpiar campos
                cleanFields();
                //listar empleados
                lisAllSupplier();
            } else {
                views.jTabbedPane1.setEnabledAt(5, false);
                views.jlabelsubplayers.setEnabled(false);
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
        if (e.getSource() == views.txt_search_subplayer) {
            //limpiar la tabla
            cleanTable();
            //listar los proveedores
            lisAllSupplier();
        }
    }

    public void cleanFields() {
        views.txt_subplayer_id.setText("");
        views.txt_subplayer_id.setEditable(true);
        views.txt_subplayer_name.setText("");
        views.txt_subplayer_description.setText("");
        views.txt_subplayer_addres.setText("");
        views.txt_subplayer_telef.setText("");
        views.txt_subplayer_email.setText("");
        views.cmb_subplayer_siri.setSelectedIndex(0);
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    //metodo para mostrar el nombre del proveedor
    public void getSuppliersName() {
        List<Suppliers> list = suppliersDao.ListSuppliersQuery(views.txt_search_subplayer.getText());
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            views.cmb_purcharse_subplayers.addItem(new DynamicCombobox(id, name));
        }
    }

}
