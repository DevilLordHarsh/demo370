import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to handle user's registration,
 * and to authorize valid users
 */
public class AuthorizationSystem {

    /**
     * Registers, and adds a new user to the database
     * @param name user's unique name as account id
     * @param fname user's full name
     * @param password user's account password
     * @param email
     * @param phone
     * @return true if user is successfully registered in database,
     * false otherwise
     */
    public static boolean registerUser(String name, String fname, String password, String email, String phone) {
        boolean success = false;
        String sql = "INSERT INTO "+DB.CUSTOMERS_TABLE+"(" +
                DB.USER_NAME_ID +","+ DB.FULL_NAME +","+ DB.PASSWORD +
                ","+ DB.EMAIL +","+ DB.PHONE +") VALUES(?,?,?,?,?)";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             PreparedStatement pst = con.prepareStatement(sql);) {
            pst.setString(1, name);
            pst.setString(2, fname);
            pst.setString(3, String.valueOf(password.hashCode()));
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
        String sql = "SELECT "+DB.USER_NAME_ID+", "+DB.PASSWORD+" FROM " +
                DB.CUSTOMERS_TABLE +" WHERE "+ DB.USER_NAME_ID +" = \""+name+"\"";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql);) {
            while(rs.next()) {
                if (name.equals(rs.getString(DB.USER_NAME_ID)) && (String.valueOf(password.hashCode()).equals(rs.getString(DB.PASSWORD)))) {
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
