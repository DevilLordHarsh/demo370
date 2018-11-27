import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class to help easily connect/disconnect to a database
 * given it's file location
 */
public class DatabaseHandler {

    public static Connection createOrConnect(String dbFile) {
        Connection connection = null;
        String db = "jdbc:sqlite:" + dbFile;
        try {
            connection = DriverManager.getConnection(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
