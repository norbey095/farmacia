package controlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.Employes;
import models.EmployesDao;
import views.LoginViews;
import views.SystemView;

public class LoginController implements ActionListener {

    private Employes employes;
    private EmployesDao employes_dao;
    private LoginViews login_views;

    public LoginController(Employes employes, EmployesDao employes_dao, LoginViews login_views) {
        this.employes = employes;
        this.employes_dao = employes_dao;
        this.login_views = login_views;
        this.login_views.btn_enter.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //obtener los datos de la vista
        String user = login_views.txt_username.getText().trim();
        String pass = String.valueOf(login_views.txt_paswor.getPassword());

        if (e.getSource() == login_views.btn_enter) {
            //validar que los campos no esten vacios
            if (!user.equals("") || !pass.equals("")) {
                //pasar los parametros al metodo login
                employes = employes_dao.loginQuery(user, pass);
                //verificar la existencia del usuario
                if (employes.getUsername() != null) {
                    if (employes.getRol().equals("Administrador")) {
                        SystemView admin = new SystemView();
                        admin.setVisible(true);
                    } else {
                        SystemView aux = new SystemView();
                        aux.setVisible(true);
                    }
                    this.login_views.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecta");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Los campos estan vacios");
            }
        }
    }

}
