package business;

import dataaccess.ClientDAO;
import dataaccess.OrderDAO;
import dataaccess.OrderItemDAO;
import model.OrderItem;

import java.sql.SQLException;
/**
 * Aceasta clasa face parte din pachetul de business si este responsabila de apelul metodelor din
 *clasa OrderItemDAO pentru accesul la baza de date.
 */
public class OrderItemBLL {
    /**
     * Apeleaza metoda findByName din OrderItemDAO.
     * @param cName String
     * @param pName String
     * @return OrderItem
     * @throws SQLException exceptie
     */
    public OrderItem findByName(String cName,String pName) throws SQLException {
        OrderItem o= OrderItemDAO.findByName(cName,pName);
        if (o==null)
        {
            System.out.println("Comanda cu numele clientului "+cName+" si produsul "+pName+" nu a fost gasita");
        }
        return o;
    }
    /**
     * Apeleaza metoda deleteClient din OrderItemDAO.
     * @param name String
     * @throws SQLException exceptie
     */
    public static void deleteClient(String name) throws SQLException {
        OrderItemDAO.deleteClient(name);
    }
    /**
     * Apeleaza mertoda deleteProduct din OrderItemDAO.
     * @param name String
     * @throws SQLException exceptie
     */
    public static void deleteProduct(String name) throws SQLException {
        OrderItemDAO.deleteProduct(name);
    }
    /**
     * Apeleaza metoda insert din OrderItemDAO
     * @param o OrderItem
     * @return OrderItem
     * @throws SQLException exceptie
     */
    public int insertOrderItem(OrderItem o) throws SQLException {
        return OrderItemDAO.insert(o);
    }
    /**
     * Apeleaza metoda getNumOfCols din OrderItemDAO.
     * @param name String
     * @return int
     * @throws SQLException exceptie
     */
    public static int getNumOfCols(String name) throws SQLException {
        return OrderItemDAO.getNumOfCols(name);
    }
}
