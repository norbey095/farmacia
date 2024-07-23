package models;


public class Sales {
    private int id;
    private String sale_date;
    private double total_to_pay;
    private int customer_id;
    private String customer_name;
    private int employes_id;
    private String employes_name;

    public Sales() {
    }

    public Sales(int id, String sale_date, double total_to_pay, int customer_id, String customer_name, int employes_id, String employes_name) {
        this.id = id;
        this.sale_date = sale_date;
        this.total_to_pay = total_to_pay;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.employes_id = employes_id;
        this.employes_name = employes_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSale_date() {
        return sale_date;
    }

    public void setSale_date(String sale_date) {
        this.sale_date = sale_date;
    }

    public double getTotal_to_pay() {
        return total_to_pay;
    }

    public void setTotal_to_pay(double total_to_pay) {
        this.total_to_pay = total_to_pay;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getEmployes_id() {
        return employes_id;
    }

    public void setEmployes_id(int employes_id) {
        this.employes_id = employes_id;
    }

    public String getEmployes_name() {
        return employes_name;
    }

    public void setEmployes_name(String employes_name) {
        this.employes_name = employes_name;
    }
    
    
}
