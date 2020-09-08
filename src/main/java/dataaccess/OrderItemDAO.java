package dataaccess;
import model.OrderItem;
import connection.ConnectionToDB;


import java.sql.*;
import java.util.logging.Logger;

/**
 *  Aceasta clasa este responsabila pentru accesul datelor din tabelul OrderItem din baza de date OrderManagement
 *   foloseste pachetul model in care se afla clasa OrderItem
 */
public class OrderItemDAO {
    protected static final Logger LOGGER= Logger.getLogger(ProductDAO.class.getName());
    private static final String insertStatement="INSERT INTO orderitem (price,clientName,productName) "+" VALUES (?,?,?)";
    private static final String findStatement="SELECT * FROM orderitem where productName = ? and clientName= ? ";
    private static final String getNumCols="SELECT * from orderitem where clientName= ? ";
    private static final String deleteClientSt="DELETE FROM orderitem where clientName= ? ";
    private static final String deleteProductSt="DELETE FROM orderitem where productName= ?";
    /**
     *  Metoda findByName primeste numele unui client si al unui produs si il cauta in tabelul OrderItem.
     * In cazul in care elementul din tabel  este gasit, atunci se va returna un nou obiect de tipul OrderItem.
     * Pentru executarea query-ului se va folosi String-ul "findStatement".
     * @param cName String
     * @param pName String
     * @return OrderItem
     * @throws SQLException exceptie
     */
    public static OrderItem findByName(String cName,String pName) throws SQLException {
        OrderItem returned=null;
        Connection connection= ConnectionToDB.getConnection();
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        preparedStatement=connection.prepareStatement(findStatement);
        preparedStatement.setString(1,cName);
        preparedStatement.setString(2,pName);
        resultSet=preparedStatement.executeQuery();
        resultSet.next();


        Double price=resultSet.getDouble("price");
        String clientName=resultSet.getString("clientName");
        String productName=resultSet.getString("productName");

        returned=new OrderItem(clientName,productName,price);

        ConnectionToDB.close(resultSet);
        ConnectionToDB.close(preparedStatement);
        ConnectionToDB.close(connection);

        return returned;
    }
    /**
     * Metoda deleteClient va sterge un rand din tabela OrderItem,unde se gaseste clientul cu numele "name", in cazul in care
     * se doreste stergea unui client din tabele Client.
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
     * Metoda deleteClient va sterge un rand din tabela OrderItem,unde se gaseste produsul cu numele "name", in cazul in care
     * se doreste stergea unui produs din tabele Product.
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
     * Metoda insert primeste ca si parametru un obiect de tipul OrderItem si insereaza datele acestuia
     * in tabelul OrderItem din baza de date OrderManagement.
     * Pentru executarea update-ului se va folosi String-ul "insertStatement".
     * @param orderItem OrderItem
     * @return int
     * @throws SQLException exceptie
     */
    public static int insert(OrderItem orderItem) throws SQLException {
        Connection connection =ConnectionToDB.getConnection();
        PreparedStatement insertStat=null;
        int insertedId=-1;
        insertStat=connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        insertStat.setDouble(1,orderItem.getPrice());
        insertStat.setString(2,orderItem.getClientName());
        insertStat.setString(3,orderItem.getProductName());

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
}
