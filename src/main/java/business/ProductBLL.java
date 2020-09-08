package business;

import com.itextpdf.text.DocumentException;
import dataaccess.ClientDAO;
import dataaccess.ProductDAO;
import model.Product;
import presentation.PDFGeneratorClient;
import presentation.PDFGeneratorProduct;

import java.io.FileNotFoundException;
import java.sql.SQLException;
/**
 * Aceasta clasa face parte din pachetul de business si este responsabila de apelul metodelor din
 *clasa ProductDAO pentru accesul la baza de date.
 */

public class ProductBLL {

    /**
     * Apeleaza metoda findByName din ProductDAO.
     * @param name String
     * @return Product
     * @throws SQLException exceptie
     */
    public Product findByName(String name) throws SQLException {
        Product P= ProductDAO.findByName(name);
        if (P==null)
        {
            //System.out.println("Product with name "+name+ " was not found!");
        }
        return P;
    }
    /**
     * Apeleaza metoda insert si metoda updateQuantity din ProductDAO.
     * @param p Product
     * @throws SQLException exceptie
     */
    public void insert(Product p) throws SQLException {
        if (findByName(p.getName())==null)
        {
           // System.out.println("Inserting product "+p.getName());
            ProductDAO.insert(p);
        }
        else
            ProductDAO.updateQuantity(p.getName(),p.getQuantity());
    }
    /**
     * Apeleaza metoda delete din ProductDAO
     * @param pName String
     * @throws SQLException exceptie
     */
    public void delete(String pName) throws SQLException {
        ProductDAO.delete(pName);
    }
    /**
     * Apeleaza metoda getNumOfCols din ProductDAO
     * @param name String
     * @return int
     * @throws SQLException exceptie
     */
    public static int getNumOfCols(String name) throws SQLException {
        return ProductDAO.getNumOfCols(name);
    }
    /**
     * Apeleaza metoda createPDF din ProductDAO
     * @param name String
     * @return PDFGeneratorClient
     * @throws SQLException exceptie
     * @throws DocumentException exceptie
     * @throws FileNotFoundException exceptie
     */
    public static PDFGeneratorProduct genPDF(String name) throws FileNotFoundException, DocumentException, SQLException {
        return ProductDAO.createPDF(name);
    }

}
