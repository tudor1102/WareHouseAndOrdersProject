package connection;

import java.sql.*;
import java.util.logging.Logger;

/**
 * Aceasta clasa este responsabila pentru conexiunea la baza de date.
 * Face parte din pachetul connection si este implementata dupa modelul "Singleton".
 */
public class ConnectionToDB {
    private static final Logger logger = Logger.getLogger(ConnectionToDB.class.getName());
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String dburl = "jdbc:mysql://localhost:3306/ordermanagement";
    private static final String user = "root";
    private static final String pass = "";

    private static ConnectionToDB con;

    static {
        try {
            con = new ConnectionToDB();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ConnectionToDB() throws ClassNotFoundException {
        Class.forName(driver);
    }

    /**
     * Aceasta metoda creeaza o conexiune la baza de date folosind un URL catre o baza de date, un user si o parola.
     * @return Connection
     * @throws SQLException exceptie
     */
    private Connection createConnection() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(dburl, user, pass);
        //System.out.println("Created connection!");
        return connection;
    }

    public static Connection getConnection() throws SQLException {
        return con.createConnection();
    }

    /**
     * Inchide conexiunea la baza de date.
     * @param connection Connection
     * @throws SQLException exceptie
     */
    public static void close(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * Inchide statement-ul.
     * @param statement Statement
     * @throws SQLException exceptie
     */
    public static void close(Statement statement) throws SQLException {
        statement.close();
    }

    /**
     * Inchide ResultSet-ul.
     * @param resultSet ResultSet
     * @throws SQLException exceptie
     */
    public static void close(ResultSet resultSet) throws SQLException {
        resultSet.close();
    }

}
