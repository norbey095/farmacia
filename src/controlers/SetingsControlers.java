package controlers;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static models.EmployesDao.address_user;
import static models.EmployesDao.email_user;
import static models.EmployesDao.full_name_user;
import static models.EmployesDao.id_user;
import static models.EmployesDao.telephone_user;
import views.SystemView;


public class SetingsControlers implements MouseListener {
    private SystemView views;
    
    public SetingsControlers(SystemView views){
        this.views = views;
        this.views.jlabelproducts.addMouseListener(this);
        this.views.jlabelpuchases.addMouseListener(this);
        this.views.jlabelcustoner.addMouseListener(this);
        this.views.jlabelemplois.addMouseListener(this);
        this.views.jlabelsubplayers.addMouseListener(this);
        this.views.jlabelcategoris.addMouseListener(this);
        this.views.jlabelreports.addMouseListener(this);
        this.views.jlabelseries.addMouseListener(this);
        profile();        
    }    
    
    //asignar el perfil del usuario
    public void profile(){
        this.views.txt_id_profile.setText(""+id_user);
        this.views.txt_name_profile.setText(full_name_user);
        this.views.txt_addres_profile.setText(address_user);
        this.views.txt_telef_profile.setText(telephone_user);
        this.views.txt_email_profile.setText(email_user);        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(e.getSource() == views.jlabelproducts){
            views.jpanelproducts.setBackground(new Color(152, 202, 63));
        }else if(e.getSource() == views.jlabelpuchases){
            views.jpanelpuchases.setBackground(new Color(152, 202, 63));
        }else if(e.getSource() == views.jlabelcustoner){
            views.jpanelCustoner.setBackground(new Color(152, 202, 63));
        }else if(e.getSource() == views.jlabelemplois){
            views.jpanelEmplois.setBackground(new Color(152, 202, 63));
        }else if(e.getSource() == views.jlabelsubplayers){
            views.jpanelsubplayers.setBackground(new Color(152, 202, 63));
        }else if(e.getSource() == views.jlabelcategoris){
            views.jpanelcategoris.setBackground(new Color(152, 202, 63));
        }else if(e.getSource() == views.jlabelreports){
            views.jpanelreports.setBackground(new Color(152, 202, 63));
        }else if(e.getSource() == views.jlabelseries){
            views.jpanelseries.setBackground(new Color(152, 202, 63));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(e.getSource() == views.jlabelproducts){
            views.jpanelproducts.setBackground(new Color(18, 45, 61));
        }else if(e.getSource() == views.jlabelpuchases){
            views.jpanelpuchases.setBackground(new Color(18, 45, 61));
        }else if(e.getSource() == views.jlabelcustoner){
            views.jpanelCustoner.setBackground(new Color(18, 45, 61));
        }else if(e.getSource() == views.jlabelemplois){
            views.jpanelEmplois.setBackground(new Color(18, 45, 61));
        }else if(e.getSource() == views.jlabelsubplayers){
            views.jpanelsubplayers.setBackground(new Color(18, 45, 61));
        }else if(e.getSource() == views.jlabelcategoris){
            views.jpanelcategoris.setBackground(new Color(18, 45, 61));
        }else if(e.getSource() == views.jlabelreports){
            views.jpanelreports.setBackground(new Color(18, 45, 61));
        }else if(e.getSource() == views.jlabelseries){
            views.jpanelseries.setBackground(new Color(18, 45, 61));
        }
    }
    
}
