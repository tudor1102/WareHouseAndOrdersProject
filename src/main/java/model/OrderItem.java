package model;
/**
 *Aceasta este entitatea pentru tabelul OrderItem
 * contine gettere si settere
 */
public class OrderItem {
    private int ID;
    private String clientName;
    private String productName;
    private Double price;

    public OrderItem(int ID,String clientName,String productName,Double price)
    {
        this.ID=ID;
        this.clientName=clientName;
        this.productName=productName;
        this.price=price;
    }
    public OrderItem(String clientName,String productName,Double price)
    {
        this.clientName=clientName;
        this.productName=productName;
        this.price=price;
    }
    public int getID() { return ID; }
    public String getClientName()
    {
        return this.clientName;
    }
    public String getProductName()
    {
        return this.productName;
    }
   public Double getPrice()
   {
       return this.price;
   }
}
