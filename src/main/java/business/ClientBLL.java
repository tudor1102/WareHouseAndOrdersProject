package business;

import com.itextpdf.text.DocumentException;
import dataaccess.ClientDAO;
import model.Client;
import presentation.PDFGeneratorClient;

import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * Aceasta clasa face parte din pachetul de business si este responsabila de apelul metodelor din
 *clasa ClientDAO pentru accesul la baza de date.
 */

public class ClientBLL {

    /**
     * Apeleaza metoda findByName din ClientDAO.
     * @param name String
     * @return Client
     * @throws SQLException exceptie
     */
    public Client findByName(String name) throws SQLException {
        Client c= ClientDAO.findByName(name);
        if (c==null)
        {
            System.out.println("Client with name "+name +" was not found !");
        }
        return c;
    }

    /**
     * Apeleaza metoda insert din ClientDAO.
     * @param c Client
     * @return int
     * @throws SQLException exceptie
     */
    public int insertClient(Client c) throws SQLException {
        return ClientDAO.insert(c);
    }

    /**
     * Apeleaza metoda delete din ClientDAO
     * @param cName String
     * @param cAddress String
     * @throws SQLException exceptie
     */
    public void delete(String cName,String cAddress) throws SQLException {
            ClientDAO.delete(cName,cAddress);
    }
    /**
     * Apeleaza metoda getNumOfCols din ClientDAO
     * @param name String
     * @return int
     * @throws SQLException exceptie
     */
    public static int getNumOfCols(String name) throws SQLException {
        return ClientDAO.getNumOfCols(name);
    }
    /**
     * Apeleaza metoda createPDF din ClientDAO
     * @param name String
     * @return PDFGeneratorClient
     * @throws SQLException exceptie
     * @throws DocumentException exceptie
     * @throws FileNotFoundException exceptie
     */
    public static PDFGeneratorClient genPDF(String name) throws FileNotFoundException, DocumentException, SQLException {
        return ClientDAO.createPDF(name);
    }

}
