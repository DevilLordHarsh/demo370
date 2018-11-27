import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class to initialize all required databases
 * to fake data simulation, received or sent to
 * other systems.
 */
public class CreateMyDatabases {

    // database file names
    public static final String USERS_DATABASE = "src/main/resources/dbs/users.db";
    public static final String ARCHIVE_DATABASE = "src/main/resources/dbs/archive.db";
    public static final String FLIGHTS_DATABASE = "src/main/resources/dbs/flights.db";

    //locations available for travel
    private static final String VANCOUVER = "Vancouver";
    private static final String AIRDRIE = "Airdrie";
    private static final String BURNABY = "Burnaby";
    private static final String CALGARY = "Calgary";
    private static final String TORONTO = "Toronto";
    private static final String ABBOTSFORD = "Abbotsford";

    //airline companies and available airplanes
    private static final String QUICK_AIRLINES = "QuickAirlines";
      private static final String WINDJET = "WindJet";
      private static final String BLAZEJET = "BlazeJet";

    private static final String CRAZY_AIRLINES = "CrazyAirlines";
      private static final String KNOTCHJET = "KnotchJet";
      private static final String RICOCHETJET = "RicochetJet";

    // creates required databases and tables if they don't already exist
    public static void init() {

        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	name text PRIMARY KEY,\n"
                + "	fname text NOT NULL,\n"
                + "	password text NOT NULL,\n"
                + "	email text NOT NULL,\n"
                + "	phone text NOT NULL\n"
                + ");";

        try (Connection con = DatabaseHandler.createOrConnect(USERS_DATABASE);) {
            con.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection con = DatabaseHandler.createOrConnect(ARCHIVE_DATABASE);) {
            // todo: create table with all columns required for archiving
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //destination, departure, price, airline, airplane
        String[] item1 = {VANCOUVER, AIRDRIE, "412", };
        sql = "CREATE TABLE IF NOT EXISTS flights (\n"
                + "	company text PRIMARY KEY,\n"
                + "	flight text NOT NULL,\n"
                + ");";

        try (Connection con = DatabaseHandler.createOrConnect(FLIGHTS_DATABASE);) {
            // todo: create table with all columns required for archiving
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
