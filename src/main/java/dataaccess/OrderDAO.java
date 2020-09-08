package dataaccess;
import com.itextpdf.text.DocumentException;
import connection.ConnectionToDB;
import model.Client;
import model.Order;
import presentation.PDFGeneratorOrder;
import presentation.PDFGeneratorProduct;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.logging.Logger;

/**
 *  Aceasta clasa este responsabila pentru accesul datelor din tabelul Orders din baza de date OrderManagement
 *   foloseste pachetul model in care se afla clasa Order
 */
public class OrderDAO {
    private static int numGenfile=0;
    protected static final Logger LOGGER= Logger.getLogger(ClientDAO.class.getName());
    private static final String insertStatement="INSERT INTO orders (clientName,productName,quantity,price) "+" VALUES (?,?,?,?)";
    private static final String findStatement="SELECT * FROM orders where clientName = ? and productName= ?";
    private static final String updateTotalPrice="UPDATE orders set totalPrice=quantity * price where clientName is not null";
    private static final String checkQuantity="SELECT quantity from product where productName = ?";
    private static final String decreaseQuantity="UPDATE product set quantity=quantity - ? where productName = ? ";
    private static final String getNumCols="SELECT * from orders where clientName= ? ";
    private static final String getAll="SELECT * from orders";
    private static final String getNumRows="SELECT count(*) from orders ";
    private static final String deleteClientSt="DELETE FROM orders where clientName= ? ";
    private static final String deleteProductSt="DELETE FROM orders where productName= ?";

    /**
     *  Metoda findByName primeste numele unui client si al unui produs si il cauta in tabelul Orders.
     * In cazul in care comanda  este gasita, atunci se va returna un nou obiect de tipul Order.
     * Pentru executarea query-ului se va folosi String-ul "findStatement".
     * @param cName String
     * @param pName String
     * @return Order
     * @throws SQLException exceptie
     */
    public static Order findByName(String cName,String pName) throws SQLException {
        Order returned=null;
        Connection connection= ConnectionToDB.getConnection();
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        preparedStatement=connection.prepareStatement(findStatement);
        preparedStatement.setString(1,cName);
        preparedStatement.setString(2,pName);
        resultSet=preparedStatement.executeQuery();
        resultSet.next();


        int quantity=resultSet.getInt("quantity");
        Double price=resultSet.getDouble("price");
        String clientName=resultSet.getString("clientName");
        String productName=resultSet.getString("productName");

        returned=new Order(clientName,productName,price,quantity);

        ConnectionToDB.close(resultSet);
        ConnectionToDB.close(preparedStatement);
        ConnectionToDB.close(connection);

        return returned;
    }

    /**
     * Metoda deleteClient va sterge un rand din tabela Orders,unde se gaseste clientul cu numele "name", in cazul in care
     * se doreste stergea unui client din tabele Client sau OrderItem.
     * @param name String
     * @throws SQLException exceptie
     */
    public static void deleteClient(String name) throws SQLException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement st=null;
        st=connection.prepareStatement(deleteClientSt);
        st.setString(1,name);
        st.executeUpdate();

        ConnectionToDB.close(st);
        ConnectionToDB.close(connection);
    }

    /**
     * Metoda deleteClient va sterge un rand din tabela Orders,unde se gaseste produsul cu numele "name", in cazul in care
     * se doreste stergea unui produs din tabele Product sau OrderItem.
     * @param name String
     * @throws SQLException exceptie
     */
    public static void deleteProduct(String name) throws SQLException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement st=null;
        st=connection.prepareStatement(deleteProductSt);
        st.setString(1,name);
        st.executeUpdate();

        ConnectionToDB.close(st);
        ConnectionToDB.close(connection);
    }
    /**
     * Metoda insert primeste ca si parametru un obiect de tipul Order si insereaza datele acestuia
     * in tabelul Orders din baza de date OrderManagement.
     * Pentru executarea update-ului se va folosi String-ul "insertStatement".
     * @param order Order
     * @return int
     * @throws SQLException exceptie
      */
    public static int insert(Order order) throws SQLException {
        Connection connection =ConnectionToDB.getConnection();
        PreparedStatement insertStat=null;
        int insertedId=-1;
        insertStat=connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        insertStat.setString(1,order.getClientName());
        insertStat.setString(2,order.getProductName());
        insertStat.setInt(3,order.getQuantity());
        insertStat.setDouble(4,order.getPrice());
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
     * Metoda updateTotal() va calcula pretul total al comenzii.
     * String-ul folsoit pentru executia query-ului este "updateTotalPrice".
     * @throws SQLException exceptie
     */
    public static void updateTotal() throws SQLException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement updateSt=null;
        updateSt=connection.prepareStatement(updateTotalPrice);
        updateSt.executeUpdate();

        ConnectionToDB.close(updateSt);
        ConnectionToDB.close(connection);
    }

    /**
     * Metoda decreaseQ primeste numele unui produs si o cantitate care va fi scazuta din stocul de produse.
     * In cazul in care se introduce o comanda noua si este finalizata, atunci se va face update automat
     * la cantitatea produsului dorit cu numele "productName" din tabelul Product.
     * @param productName String
     * @param quantityDec int
     * @throws SQLException exceptie
     */
    public static void decreaseQ(String productName,int quantityDec) throws SQLException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement decreaseStatement=null;
        decreaseStatement=connection.prepareStatement(decreaseQuantity);
        decreaseStatement.setInt(1,quantityDec);
        decreaseStatement.setString(2,productName);
        decreaseStatement.executeUpdate();

        ConnectionToDB.close(decreaseStatement);
        ConnectionToDB.close(connection);
    }

    /**
     * Metoda getProductQuantity returneaza cantitatea de produse din tabelul Product pentru un produs
     * cu numele productName.
     * @param productName String
     * @return int
     * @throws SQLException exceptie
     */
    public static int getProductQuantity(String productName) throws SQLException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement checkStatement=null;
        ResultSet resultSet=null;
        checkStatement=connection.prepareStatement(checkQuantity);
        checkStatement.setString(1,productName);
        resultSet=checkStatement.executeQuery();
        int quantity=0;
        while(resultSet.next())
        {
            quantity=resultSet.getInt("quantity");
        }
        return quantity;
    }
    /**
     * Metoda getNumOfCols va returna numarul de coloane ale unui tabel.Ca si parametru va
     * fi trimis numele unui client deja existent in tabelul Client, deoarece asa putem extrage numarul
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
     * Metoda getNumOfRow foloseste un query simplu de "SELECT * count(*) from orders".
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
     * Metoda createPDF creeaza un document PDF in care se afla un tabel cu toate datele din tabelul Orders.
     * Primeste ca parametru un nume de client, deoarece cu ajutorul acestuia se va apela metoda "getNumOfCols".
     * Se extrag numele coloanelor iar mai apoi datele din tabel.La final este format numele PDF-ului pentru a
     * crea documente cu nume diferite.
     * Se va folosi variabila numGenFile pentru a indica numarul fisierului.
     * String-ul folosit pentru executarea query-ului este String-ul "getAll".
     * @param name String
     * @return PDFGeneratorOrder
     * @throws SQLException exceptie
     * @throws FileNotFoundException exceptie
      * @throws DocumentException exceptie
     */
    public static PDFGeneratorOrder createPDF(String name) throws SQLException, FileNotFoundException, DocumentException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement getInfo=null;
        getInfo=connection.prepareStatement(getAll);
        ResultSet resultSet=getInfo.executeQuery();
        String[] columnNames=new String[getNumOfCols(name)];
        String[] clientNames=new String[getNumOfCols(name)];
        String[] productName=new String[getNumOfCols(name)];
        int[] quantities=new int[getNumOfCols(name)];
        float[] totalPrices=new float[getNumOfCols(name)];
        float[] prices=new float[getNumOfCols(name)];
        ResultSetMetaData metaData=resultSet.getMetaData();
        int k=0;
        int m=0;
        int n=0;
        int p=0;
        int t=0;
        for(int i=2;i<=getNumOfCols(name);i++)
        {
            columnNames[i-2]=metaData.getColumnLabel(i);
        }
        while(resultSet.next())
        {
            clientNames[p++]=resultSet.getString("clientName");
            totalPrices[t++]=resultSet.getFloat("totalPrice");
            productName[k++]=resultSet.getString("productName");
            quantities[m++]=resultSet.getInt("quantity");
            prices[n++]=resultSet.getFloat("price");
        }

        PDFGeneratorOrder pdf=new PDFGeneratorOrder(getNumOfCols(name)-1,getNumOfRows(),columnNames,clientNames,productName,quantities,totalPrices,prices,"OrderReport"+numGenfile);
        numGenfile++;
        return pdf;
    }
}
