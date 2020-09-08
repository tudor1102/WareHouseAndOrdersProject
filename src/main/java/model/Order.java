package model;
/**
 *Aceasta este entitatea pentru tabelul Order
 * contine gettere si settere
 */
public class Order {
    int ID;
    private String clientName;
    private String productName;
    private int quantity;
    private Double price;
    private Double totalPrice;
    public Order(int ID,String clientName,String productName,Double price,int quantity)
    {
        this.ID=ID;
        this.price=price;
        this.quantity=quantity;
        this.clientName=clientName;
        this.productName=productName;
        this.totalPrice=price*quantity;
    }
    public Order(String clientName,String productName,Double price,int quantity)
    {
        this.quantity=quantity;
        this.price=price;
        this.clientName=clientName;
        this.productName=productName;
        this.totalPrice=price*quantity;
    }

    public int getID() { return this.ID; }
    public Double getPrice(){return this.price;}
    public int getQuantity() { return this.quantity; }
    public Double getTotalPrice()
    {
        return this.totalPrice;
    }
    public String getClientName()
    {
        return this.clientName;
    }

    public String getProductName()
    {
        return this.productName;
    }
}
