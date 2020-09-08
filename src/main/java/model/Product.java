package model;
/**
 *Aceasta este entitatea pentru tabelul Product
 * contine gettere si settere
 */
public class Product {
    private int ID;
    private String productName;
    private int quantity;
    private double price;
    public Product(int ID,String name,int quantity,double price)
    {
        this.productName=name;
        this.quantity=quantity;
        this.price=price;
        this.ID=ID;
    }
    public Product( String name,int quantity,double price)
    {
        this.productName=name;
        this.price=price;
        this.quantity=quantity;
    }

    public String getName() { return this.productName; }
    public int getID(){return this.ID;}
    public int getQuantity()
    {
        return this.quantity;
    }
    public double getPrice()
    {
        return this.price;
    }
    public void setName(String name)
    {
        this.productName=name;
    }
    public void setQuantity(int quantity)
    {
        this.quantity=quantity;
    }
    public void setPrice(int price)
    {
        this.price=price;
    }


}
