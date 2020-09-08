package dataaccess;
import com.itextpdf.text.DocumentException;
import connection.ConnectionToDB;
import model.Client;
import presentation.PDFGeneratorClient;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.logging.Logger;

/**
 * Aceasta clasa este responsabila pentru accesul datelor din tabelul Client din baza de date OrderManagement
 * foloseste pachetul model in care se afla clasa Client
 */
public class ClientDAO {

    private static int numGenfile=0;
    protected static final Logger LOGGER= Logger.getLogger(ClientDAO.class.getName());
    private static final String insertStatement="INSERT INTO client (clientName,clientAddress) VALUES (?,?)";
    private static final String findStatement="SELECT * FROM client where clientName = ?";
    private static final String deleteStatement="DELETE FROM client where clientName = ? and clientAddress = ?";
    private static final String getNumCols="SELECT * from client where clientName= ? ";
    private static final String getAll="SELECT * from client";
    private static final String getNumRows="SELECT count(*) from client ";

    /**
     * Metoda findByName primeste numele unui client si il cauta in tabelul Client.
     * In cazul in care este gasit, atunci se va returna un nou obiect de tipul Client.
     * Pentru executarea query-ului se va folosi String-ul "findStatement".
     * @param name String
     * @return Client
     * @throws SQLException exceptie
     */
    public static Client findByName(String name) throws SQLException {
            Client returned=null;
            Connection connection= ConnectionToDB.getConnection();
            PreparedStatement preparedStatement=null;
            ResultSet resultSet=null;
            preparedStatement=connection.prepareStatement(findStatement);
            preparedStatement.setString(1,name);
            resultSet=preparedStatement.executeQuery();
            resultSet.next();


            String clientName=resultSet.getString("clientName");
            String clientAddress=resultSet.getString("clientAddress");

            returned=new Client(clientName,clientAddress);

            ConnectionToDB.close(resultSet);
            ConnectionToDB.close(preparedStatement);
            ConnectionToDB.close(connection);

            return returned;
    }

    /**
     * Metoda insert primeste ca si parametru un obiect de tipul Client si insereaza datele acestuia
     * in tabelul Client din baza de date OrderManagement.
     * Pentru executarea update-ului se va folosi String-ul "insertStatement".
     * @param client Client
     * @return int
     * @throws SQLException exceptie
     */
    public static int insert(Client client) throws SQLException {
        Connection connection =ConnectionToDB.getConnection();
        PreparedStatement insertStat=null;
        int insertedId=-1;
        insertStat=connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
        insertStat.setString(1,client.getName());
        insertStat.setString(2,client.getAddress());
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
     * Metoda delete primeste ca si parametrii numele unui client si adresa sa.Acesta va fi formatul cu
     * care se va sterge un client din tabela de clienti pe tot parcursul proiectului.
     * Pentru executarea acestui update la tabelul Client se va folosi String-ul "deleteStatement".
     * @param clientName String
     * @param clientAddress String
     * @throws SQLException exceptie
     */
    public static void delete(String clientName,String clientAddress) throws SQLException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement deleteStat=null;
        deleteStat=connection.prepareStatement(deleteStatement,deleteStat.RETURN_GENERATED_KEYS);
        deleteStat.setString(1,clientName);
        deleteStat.setString(2,clientAddress);
        deleteStat.executeUpdate();

        ConnectionToDB.close(deleteStat);
        ConnectionToDB.close(connection);
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
     * Metoda getNumOfRow foloseste un query simplu de "SELECT * count(*) from client".
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
     * Metoda createPDF creeaza un document PDF in care se afla un tabel cu toate datele din tabelul Client.
     * Primeste ca parametru un nume de client, deoarece cu ajutorul acestuia se va apela metoda "getNumOfCols".
     * Se extrag numele coloanelor iar mai apoi datele din tabel.La final este format numele PDF-ului pentru a
     * crea documente cu nume diferite.
     * Se va folosi variabila numGenFile pentru a indica numarul fisierului.
     * String-ul folosit pentru executarea query-ului este String-ul "getAll".
     * @param name String
     * @return PDFGeneratorClient
     * @throws SQLException exceptie
     * @throws FileNotFoundException exceptie
     * @throws DocumentException exceptie
     */
       public static PDFGeneratorClient createPDF(String name) throws SQLException, FileNotFoundException, DocumentException {
        Connection connection=ConnectionToDB.getConnection();
        PreparedStatement getInfo=null;
        getInfo=connection.prepareStatement(getAll);
        ResultSet resultSet=getInfo.executeQuery();
        String[] columnNames=new String[getNumOfCols(name)];
        String[] clientName=new String[getNumOfCols(name)];
        String[] clientAddress=new String[getNumOfCols(name)];
        ResultSetMetaData metaData=resultSet.getMetaData();
        int k=0;
        int m=0;
        for(int i=2;i<=getNumOfCols(name);i++)
        {
            columnNames[i-2]=metaData.getColumnLabel(i);
        }
        while(resultSet.next())
        {
            clientName[k++]=resultSet.getString("clientName");
            clientAddress[m++]=resultSet.getString("clientAddress");
        }
       for(int i=0;i<k;i++)
       {
           System.out.println(clientName[i]+" "+clientAddress[i]);
       }
        PDFGeneratorClient pdf=new PDFGeneratorClient(getNumOfCols(name)-1,getNumOfRows(),columnNames,clientName,clientAddress,"ClientReport"+numGenfile);
        numGenfile++;
        return pdf;
    }
}
