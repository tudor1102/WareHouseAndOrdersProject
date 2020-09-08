package dataaccess;
import com.itextpdf.text.DocumentException;
import connection.ConnectionToDB;
import model.Product;
import presentation.PDFGeneratorClient;
import presentation.PDFGeneratorProduct;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.logging.Logger;
/**
 *  Aceasta clasa este responsabila pentru accesul datelor din tabelul Product din baza de date OrderManagement
 *   foloseste pachetul model in care se afla clasa Product
 */
public class ProductDAO {
    private static int numGenfile=0;
    protected static final Logger LOGGER= Logger.getLogger(ProductDAO.class.getName());
    private static final String insertStatement="INSERT INTO product (productName,quantity,price)"+"VALUES (?,?,?)";
    private static final String findStatement="SELECT * FROM product where productName = ?";
    private static final String deleteStatement="DELETE FROM product where productName = ?";
    private static final String quantityUpdate="CALL update_quantity_product(?,?)";
    private static final String getNumCols="SELECT * from product where productName= ? ";
    private static final String getAll="SELECT * from product";
    private static final String getNumRows="SELECT count(*) from product ";
    /**
     *  Metoda findByName primeste numele unui produs si il cauta in tabelul Product.
     * In cazul in care elementul din tabel  este gasit, atunci se va returna un nou obiect de tipul Product.
     * Pentru executarea query-ului se va folosi String-ul "findStatement".
     * @param name String
     * @return OrderItem
     * @throws SQLException exceptie
     */
    public static Product findByName(String name) throws SQLException {
        Product returned=null;
        Connection connection= ConnectionToDB.getConnection();
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        preparedStatement=connection.prepareStatement(findStatement);
        preparedStatement.setString(1,name);
        resultSet=preparedStatement.executeQuery();
        String productName="";
        int quantity=0;
        double price=0;
        while(resultSet.next()) {
           productName = resultSet.getString("productName");
            quantity= resultSet.getInt("quantity");
            price = resultSet.getDouble("price");
        }
        if (productName!=null && quantity!=0 && price!=0)
        returned=new Product(productName,quantity,price);

        ConnectionToDB.close(resultSet);
        ConnectionToDB.close(preparedStatement);
        ConnectionToDB.close(connection);

        return returned;
    }
    /**
     * Metoda insert primeste ca si parametru un obiect de tipul Product si insereaza datele acestuia
     * in tabelul Product din baza de date OrderManagement.
     * Pentru executarea update-ului se va folosi String-ul "insertStatement".
     * @param product Product
     * @return int
     * @throws SQLException exceptie
     */
    public static int insert(Product product) throws SQLException {
        Connection connection =ConnectionToDB.getConnection();
        PreparedStatement insertStat=null;
        int insertedId=-1;
        insertStat=connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        insertStat.setString(1,product.getName());
        insertStat.setInt(2,product.getQuantity());
        insertStat.setDouble(3,product.getPrice());

        insertStat.executeUpdate();
        ResultSet rs=insertStat.getGeneratedKeys();
        if (rs.next())
        {
            insertedId=rs.getInt(1);
        }
        ConnectionToDB.close(insertStat);
        ConnectionToDB.close(connection);

        return insertedId;
    }
    /**
     * Metoda delete primeste ca si parametru numele unui produs.Acesta va fi formatul cu
     * care se va sterge un produs din tabela de produse pe tot parcursul proiectului.
     * Pentru executarea acestui update la tabelul Product se va folosi String-ul "deleteStatement".
     * @param productName String
     * @throws SQLException exceptie
     */
    public static void delete(String productName) throws SQLException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement deleteStat=null;
        deleteStat=connection.prepareStatement(deleteStatement);
        deleteStat.setString(1,productName);
        deleteStat.executeUpdate();

        ConnectionToDB.close(deleteStat);
        ConnectionToDB.close(connection);
    }

    /**
     * In cazul in care este introdus un produs cu un nume deja existent, atunci va fi apelata procedura stocata
     * update_quantity_product prin intermediul careia se va face update la cantitatea produsului cu numele productName.
     * @param productName String
     * @param quantity int
     * @throws SQLException exceptie
     */
    public static void updateQuantity(String productName, int quantity) throws SQLException {
        Connection connection=ConnectionToDB.getConnection();
        CallableStatement callableStatement=null;
        callableStatement=connection.prepareCall(quantityUpdate);
        callableStatement.setString(1,productName);
        callableStatement.setInt(2,quantity);
        callableStatement.executeUpdate();

        ConnectionToDB.close(callableStatement);
        ConnectionToDB.close(connection);
    }
    /**
     * Metoda getNumOfCols va returna numarul de coloane ale unui tabel.Ca si parametru va
     * fi trimis numele unui produs deja existent in tabelul Product, deoarece asa putem extrage numarul
     * de coloane din metadatele obiectului ResultSet.Se va folosi String-ul "getNumCols".
     * @param name String
     * @return int
     * @throws SQLException exceptie
     */
    public static int getNumOfCols(String name) throws SQLException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement getStatement=null;
        ResultSet resultSet=null;
        getStatement=connection.prepareStatement(getNumCols);
        getStatement.setString(1,name);
        resultSet=getStatement.executeQuery();

        int numCols=resultSet.getMetaData().getColumnCount();
        return numCols;
    }
    /**
     * Metoda getNumOfRow foloseste un query simplu de "SELECT * count(*) from product".
     * Returneaza numarul de randuri ale unui tabel.String-ul folosit in query este "getNumRows".
     * @return int
     * @throws SQLException exceptie
     */
    public static int getNumOfRows() throws SQLException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement getRows=null;
        getRows=connection.prepareStatement(getNumRows);
        ResultSet resultSet=getRows.executeQuery();
        int num=0;
        while(resultSet.next())
        {
            num=resultSet.getInt(1);
        }
        return num;
    }
    /**
     * Metoda createPDF creeaza un document PDF in care se afla un tabel cu toate datele din tabelul Product.
     * Primeste ca parametru un nume de produs, deoarece cu ajutorul acestuia se va apela metoda "getNumOfCols".
     * Se extrag numele coloanelor iar mai apoi datele din tabel.La final este format numele PDF-ului pentru a
     * crea documente cu nume diferite.
     * Se va folosi variabila numGenFile pentru a indica numarul fisierului.
     * String-ul folosit pentru executarea query-ului este String-ul "getAll".
     * @param name String
     * @return PDFGeneratorProduct
     * @throws SQLException exceptie
     * @throws FileNotFoundException exceptie
     * @throws DocumentException exceptie
     */
    public static PDFGeneratorProduct createPDF(String name) throws SQLException, FileNotFoundException, DocumentException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement getInfo=null;
        getInfo=connection.prepareStatement(getAll);
        ResultSet resultSet=getInfo.executeQuery();
        String[] columnNames=new String[getNumOfCols(name)];
        String[] productName=new String[getNumOfCols(name)];
        int[] quantities=new int[getNumOfCols(name)];
        float[] prices=new float[getNumOfCols(name)];
        ResultSetMetaData metaData=resultSet.getMetaData();
        int k=0;
        int m=0;
        int n=0;
        for(int i=2;i<=getNumOfCols(name);i++)
        {
            columnNames[i-2]=metaData.getColumnLabel(i);
        }
        while(resultSet.next())
        {
           productName[k++]=resultSet.getString("productName");
            quantities[m++]=resultSet.getInt("quantity");
            prices[n++]=resultSet.getFloat("price");
        }

        PDFGeneratorProduct pdf=new PDFGeneratorProduct(getNumOfCols(name)-1,getNumOfRows(),columnNames,productName,quantities,prices,"ProductReport"+numGenfile);
        numGenfile++;
        return pdf;
    }

}
