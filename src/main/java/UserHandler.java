import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to handle user's registration and login
 */
public class UserHandler {

    /**
     * Registers, and adds a new user to the database
     * @param name
     * @param fname
     * @param password
     * @param email
     * @param phone
     * @return true if user is successfully registered in database,
     * false otherwise
     */
    public static boolean registerUser(String name, String fname, String password, String email, String phone) {
        boolean success = false;
        String sql = "INSERT INTO users(name,fname,password,email,phone) VALUES(?,?,?,?,?)";

        try (Connection con = DatabaseHandler.createOrConnect(CreateMyDatabases.USERS_DATABASE);
             PreparedStatement pst = con.prepareStatement(sql);) {
            pst.setString(1, name);
            pst.setString(2, fname);
            pst.setString(3, password);
            pst.setString(4, email);
            pst.setString(5, phone);
            pst.executeUpdate();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * Authenticates user using username and password from database
     * @param name
     * @param password
     * @return true if user is valid and authenticated, false otherwise
     */
    public static boolean loginUser(String name, String password) {
        boolean success = false;
        String sql = "SELECT name, password FROM users WHERE name = \""+name+"\"";

        try (Connection con = DatabaseHandler.createOrConnect(CreateMyDatabases.USERS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql);) {
            while(rs.next()) {
                if (name.equals(rs.getString("name")) && (password.equals(rs.getString("password")))) {
                    success = true;
                    break;
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

}
