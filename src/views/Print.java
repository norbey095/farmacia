/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import models.Purchases;
import models.PurchasesDao;

/**
 *
 * @author diana
 */
public class Print extends javax.swing.JFrame {
    Purchases purchases = new Purchases();
    PurchasesDao purchasesDao = new PurchasesDao();
    DefaultTableModel model = new DefaultTableModel();
    /**
     * Creates new form Print
     */
    public Print(int id) {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Factura de compra");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        txt_invoice.setText("" + id);
        listAllPurchasesdetails(id);
        calculatepurchase();
    }
       
    public void listAllPurchasesdetails(int id){
        List<Purchases> list = purchasesDao.listPurchasesDetailQuery(id);
        model = (DefaultTableModel) purchase_detalls_table.getModel();
        Object[] row = new Object[7];
        for(int i = 0; i < list.size(); i++){
            row[0] = list.get(i).getProduct_name();
            row[1] = list.get(i).getPurchase_amount();
            row[2] = list.get(i).getPurchase_prace();
            row[3] = list.get(i).getPurchase_subtotal();
            row[4] = list.get(i).getSupplier_name_product();
            row[5] = list.get(i).getPurcharser();
            row[6] = list.get(i).getCreated();
            model.addRow(row);
        }
        purchase_detalls_table.setModel(model);
    }
    
    //calcular el total
    public void calculatepurchase(){
        double total = 0.00;
        int numRow = purchase_detalls_table.getRowCount();
        
        for(int i = 0; i < numRow; i++){
            //pasar el indice de la columna que se sumara
            total = total + Double.parseDouble(String.valueOf(purchase_detalls_table.getValueAt(i, 3)));
        }
        txt_total.setText("" + total);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        form_print = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_invoice = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        purchase_detalls_table = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txt_total = new javax.swing.JTextField();
        btn_print_purchase = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        form_print.setBackground(new java.awt.Color(152, 192, 63));
        form_print.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Recursos/Iconos/farmacia.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 70));

        form_print.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 70));

        jPanel1.setBackground(new java.awt.Color(18, 45, 61));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("VIDA NATURAL");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 210, 20));

        txt_invoice.setEditable(false);
        jPanel1.add(txt_invoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, 110, -1));

        form_print.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 70));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("DETALLES DE LA COMPRA:");
        form_print.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        purchase_detalls_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Precio", "Subtotal", "Proveedor", "Comprado por", "Fecha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(purchase_detalls_table);
        if (purchase_detalls_table.getColumnModel().getColumnCount() > 0) {
            purchase_detalls_table.getColumnModel().getColumn(0).setMinWidth(100);
            purchase_detalls_table.getColumnModel().getColumn(5).setMinWidth(110);
            purchase_detalls_table.getColumnModel().getColumn(6).setMinWidth(80);
        }

        form_print.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 620, 260));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total:");
        form_print.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 450, 60, 20));
        form_print.add(txt_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 450, 120, -1));

        getContentPane().add(form_print, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 520));
        form_print.getAccessibleContext().setAccessibleName("");

        btn_print_purchase.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn_print_purchase.setText("IMPRIMIR");
        btn_print_purchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_print_purchaseActionPerformed(evt);
            }
        });
        getContentPane().add(btn_print_purchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 550, 170, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_print_purchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_print_purchaseActionPerformed
        Toolkit tk = form_print.getToolkit();
        PrintJob pj = tk.getPrintJob(this, null, null);
        Graphics graphics = pj.getGraphics();
        form_print.print(graphics);
        graphics.dispose();
        pj.end();
    }//GEN-LAST:event_btn_print_purchaseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_print_purchase;
    private javax.swing.JPanel form_print;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable purchase_detalls_table;
    private javax.swing.JTextField txt_invoice;
    private javax.swing.JTextField txt_total;
    // End of variables declaration//GEN-END:variables
}
