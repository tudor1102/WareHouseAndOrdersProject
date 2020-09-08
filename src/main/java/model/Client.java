package model;

/**
 *Aceasta este entitatea pentru tabelul Client
 * contine gettere si settere
 */
public class Client {
    private int ID;
    private String clientName;
    private String clientAddress;
    public Client(int ID,String name,String address)
    {
        this.ID=ID;
        this.clientName=name;
        this.clientAddress=address;
    }

    public Client(String name,String address)
    {
        this.clientName=name;
        this.clientAddress=address;
    }


    public String getName()
    {
        return this.clientName;
    }
    public String getAddress()
    {
        return this.clientAddress;
    }
    public int getID() {return this.ID;}

    public void setName(String name) {
        this.clientName = name;
    }
    public void setAddress(String address)
    {
        this.clientAddress=address;
    }



}
