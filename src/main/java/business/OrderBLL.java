package business;

import com.itextpdf.text.DocumentException;
import dataaccess.ClientDAO;
import dataaccess.OrderDAO;
import dataaccess.OrderItemDAO;
import dataaccess.ProductDAO;
import model.Client;
import model.Order;
import model.OrderItem;
import presentation.PDFGeneratorOrder;
import presentation.PDFGeneratorProduct;

import java.io.FileNotFoundException;
import java.sql.SQLException;
/**
 * Aceasta clasa face parte din pachetul de business si este responsabila de apelul metodelor din
 *clasa OrderDAO pentru accesul la baza de date.
 */
public class OrderBLL {
    /**
     * Apeleaza metoda findByName din OrderDAO.
     * @param cName String
     * @param pName String
     * @return Order
     * @throws SQLException exceptie
     */
    public Order findByName(String cName, String pName) throws SQLException {
        Order o= OrderDAO.findByName(cName,pName);
        if (o==null)
        {
            System.out.println("Comanda cu clientul "+cName+" si produsul "+pName+" nu a fost gaista");
        }
        return o;
    }
    /**
     * Apeleaza metoda insert si metoda updateTotal din ClientDAO
     * @param o Order
     * @throws SQLException exceptie
     */
    public void insertOrder(Order o) throws SQLException {
        OrderDAO.insert(o);
         OrderDAO.updateTotal();
    }

    /**
     * Apeleaza metoda decreaseQuantity din OrderDAO
     * @param o Order
     * @throws SQLException exceptie
     */
    public static void decreaseQuantity(Order o) throws SQLException {
        OrderDAO.decreaseQ(o.getProductName(),o.getQuantity());
    }

    /**
     * Verifica daca o comanda este corect facuta pentru a putea fi creata o chitanta.
     * @param o Order
     * @return boolean
     * @throws SQLException exceptie
     */
    public static boolean okToBill(Order o) throws SQLException {
        boolean ok=false;
        if (o.getQuantity()<OrderDAO.getProductQuantity(o.getProductName()))
        {
            ok=true;
        }
        return ok;
    }

    /**
     * Apeleaza metoda getProductQuantity din OrderDAO
     * @param productName String
     * @return int
     * @throws SQLException exceptie
     */
    public static int getQuantityForProd(String productName) throws SQLException {
        return OrderDAO.getProductQuantity(productName);
    }

    /**
     * Apeleaza metoda deleteClient din OrderDAO.
     * @param name String
     * @throws SQLException exceptie
     */
    public static void deleteClient(String name) throws SQLException {
       OrderDAO.deleteClient(name);
    }

    /**
     * Apeleaza mertoda deleteProduct din OrderDAO.
     * @param name String
     * @throws SQLException exceptie
     */
    public static void deleteProduct(String name) throws SQLException {
        OrderDAO.deleteProduct(name);
    }

    /**
     * Apeleaza metoda getNumOfCols din OrderDAO.
     * @param name String
     * @return int
     * @throws SQLException exceptie
     */
    public static int getNumOfCols(String name) throws SQLException {
        return OrderDAO.getNumOfCols(name);
    }

    /**
     * Apeleaza metoda createPDF din OrderDAO.
     * @param name String
     * @return PDFGeneratorOrder
     * @throws FileNotFoundException exceptie
     * @throws DocumentException exceptie
     * @throws SQLException exceptie
     */
    public static PDFGeneratorOrder genPDF(String name) throws FileNotFoundException, DocumentException, SQLException {
        return OrderDAO.createPDF(name);
    }
}
