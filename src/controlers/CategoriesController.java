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
import models.Categories;
import models.CategoriesDao;
import models.DynamicCombobox;
import static models.EmployesDao.rol_user;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import views.SystemView;

public class CategoriesController implements ActionListener, MouseListener, KeyListener {

    private Categories category;
    private CategoriesDao categoryDao;
    private SystemView views;
    String rol = rol_user;

    DefaultTableModel model = new DefaultTableModel();

    public CategoriesController(Categories category, CategoriesDao categoryDao, SystemView views) {
        this.category = category;
        this.categoryDao = categoryDao;
        this.views = views;
        //boton de registrar categorias
        this.views.btn_register_category.addActionListener(this);
        //boton de modificar catedorias
        this.views.btn_update_category.addActionListener(this);
        //eliminar categoria
        this.views.btn_delet_category.addActionListener(this);
        //Boton de cancelar categoria
        this.views.btn_cancel_category.addActionListener(this);
        this.views.category_table.addMouseListener(this);
        this.views.txt_search_category.addKeyListener(this);
        this.views.jlabelcategoris.addMouseListener(this);
        getCategoryName();
        AutoCompleteDecorator.decorate(views.cmb_products_categori);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_category) {
            //verificar si los campos estan vacios
            if (views.txt_category_name.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                category.setName(views.txt_category_name.getText().trim());

                if (categoryDao.registerCategoriesQuery(category)) {
                    cleanTable();
                    cleanFields();
                    lisAllCategories();
                    JOptionPane.showMessageDialog(null, "categoria registrada con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar la categoria");
                }
            }
        }else if(e.getSource() == views.btn_update_category){
            if(views.txt_category_id.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Selecciona una fila para continuar");
            }else{
                //verificar si los campos estan vacios
                if(views.txt_category_id.getText().equals("")
                   || views.txt_category_name.getText().equals("")){                  
                  JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");  
                }else{
                    category.setId(Integer.parseInt(views.txt_category_id.getText()));
                    category.setName(views.txt_category_name.getText().trim());                    
                    if(categoryDao.updateCategoriesquery(category)){
                        cleanTable();
                        cleanFields();
                        views.btn_register_category.setEnabled(true);
                        lisAllCategories();                        
                        JOptionPane.showMessageDialog(null, "Datos de la categoria modificadas con exito");
                    }else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar los datos de la categoria");
                    }
                }
            }
        }else if(e.getSource() == views.btn_delet_category){
            int row = views.category_table.getSelectedRow();            
            if(row == -1){
               JOptionPane.showMessageDialog(null, "Debes seleccionar una categoria para eliminar"); 
            }else{
                int id = Integer.parseInt(views.category_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "¿En realidad quieres eliminar esta categoria?");
                
                if(question == 0 && categoryDao.deleteCategoriesQuery(id) != false){
                    cleanTable();
                    cleanFields();
                    views.btn_register_category.setEnabled(true);
                    lisAllCategories();
                    JOptionPane.showMessageDialog(null, "Categoria eliminada con exito");
                }
            }
        }else if(e.getSource() == views.btn_cancel_category){            
            views.btn_register_category.setEnabled(true);
            cleanFields();
        }
    }

    //listar categorias
    public void lisAllCategories() {
        if (rol.equals("Administrador")) {
            List<Categories> list = categoryDao.ListCategories(views.txt_search_category.getText());
            model = (DefaultTableModel) views.category_table.getModel();
            Object[] row = new Object[2];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                model.addRow(row);
            }
            views.category_table.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.category_table) {
            int row = views.category_table.rowAtPoint(e.getPoint());
            views.txt_category_id.setText(views.category_table.getValueAt(row, 0).toString());
            views.txt_category_name.setText(views.category_table.getValueAt(row, 1).toString());
            //deshabilitar botones
            views.btn_register_category.setEnabled(false);
            views.txt_category_id.setEditable(false);
        } else if (e.getSource() == views.jlabelcategoris){
            if(rol.equals("Administrador")){
                views.jTabbedPane1.setSelectedIndex(6);
                //limpiar tabla
                cleanTable();
                //limpiar campos
                cleanFields();
                //listar empleados
                lisAllCategories();
            }else{
                views.jTabbedPane1.setEnabledAt(6, false);
                views.jlabelcategoris.setEnabled(false);
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
        if (e.getSource() == views.txt_search_category) {
            //limpiar la tabla
            cleanTable();
            //listar los clientes
            lisAllCategories();
        }
    }

    public void cleanFields() {
        views.txt_category_id.setText("");
        views.txt_category_id.setEditable(true);
        views.txt_category_name.setText("");
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
    
    //metodo para mostrar el nombre de las categorias
    public void getCategoryName(){
        List<Categories> list = categoryDao.ListCategories(views.txt_search_category.getText());
        for(int i = 0; i < list.size(); i++){
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            views.cmb_products_categori.addItem(new DynamicCombobox(id, name));
        }
    }

}
