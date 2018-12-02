import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class to initialize all required databases
 * to fake data simulation, received or sent to
 * other systems.
 */
public class CreateMyDatabases {

    // creates required databases and tables if they don't already exist
    public static void init() {

        String customers = "CREATE TABLE IF NOT EXISTS "+ DB.CUSTOMERS_TABLE+" (\n"
                + DB.USER_NAME_ID + " text PRIMARY KEY,\n"
                + DB.FULL_NAME + " text NOT NULL,\n"
                + DB.PASSWORD + " text NOT NULL,\n"
                + DB.EMAIL + " text NOT NULL,\n"
                + DB.PHONE + " text NOT NULL\n"
                + ");";

        String flights = "CREATE TABLE IF NOT EXISTS "+ DB.FLIGHTS_INFO_TABLE+" (\n"
                + DB.FLIGHT_INFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + DB.DESTINATION + " text NOT NULL,\n"
                + DB.DEPARTURE + " text NOT NULL,\n"
                + DB.TIME + " text NOT NULL,\n"
                + DB.AIRPLANE_ID + " text NOT NULL,\n"
                + "FOREIGN KEY ("+ DB.AIRPLANE_ID +") REFERENCES "
                + DB.AIRPLANES_TABLE + "("+ DB.AIRPLANE_ID +")"
                + ");";

        String tickets = "CREATE TABLE IF NOT EXISTS "+ DB.TICKETS_TABLE+" (\n"
                + DB.TICKET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + DB.USER_NAME_ID + " text NOT NULL,\n"
                + DB.PAYMENT_ID + " text NOT NULL,\n"
                + DB.FLIGHT_INFO_ID + " text NOT NULL,\n"
                + DB.SEAT + " text,\n"
                + DB.STATUS + " text NOT NULL,\n"
                + "FOREIGN KEY ("+ DB.USER_NAME_ID +") REFERENCES "
                + DB.CUSTOMERS_TABLE + "("+ DB.USER_NAME_ID +"),\n"
                + "FOREIGN KEY ("+ DB.PAYMENT_ID +") REFERENCES "
                + DB.PAYMENTS_TABLE + "("+ DB.PAYMENT_ID +"),\n"
                + "FOREIGN KEY ("+ DB.FLIGHT_INFO_ID +") REFERENCES "
                + DB.FLIGHTS_INFO_TABLE + "("+ DB.FLIGHT_INFO_ID +")\n"
                + ");";

        String payments = "CREATE TABLE IF NOT EXISTS "+ DB.PAYMENTS_TABLE+" (\n"
                + DB.PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + DB.AMOUNT + " text NOT NULL,\n"
                + DB.CARD_NUMBER + " text NOT NULL,\n"
                + "FOREIGN KEY ("+ DB.CARD_NUMBER +") REFERENCES "
                + DB.CREDIT_CARDS_TABLE + "("+ DB.CARD_NUMBER +")\n"
                + ");";

        String credit_cards = "CREATE TABLE IF NOT EXISTS "+ DB.CREDIT_CARDS_TABLE+" (\n"
                + DB.CARD_NUMBER + " text PRIMARY KEY,\n"
                + DB.CVV + " text NOT NULL,\n"
                + DB.EXPIRY + " text NOT NULL\n"
                + ");";

        String airplanes = "CREATE TABLE IF NOT EXISTS "+ DB.AIRPLANES_TABLE+" (\n"
                + DB.AIRPLANE_ID + " text PRIMARY KEY,\n"
                + DB.AIRLINE_NAME + " text NOT NULL,\n"
                + DB.AIRPLANE_SEATS + " text NOT NULL\n"
                + ");";

        String locations = "CREATE TABLE IF NOT EXISTS "+ DB.LOCATIONS_TABLE+" (\n"
                + DB.LOCATION_NAME + " text PRIMARY KEY,\n"
                + DB.DISTANCE_FROM_WEST + " text NOT NULL\n"
                + ");";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);) {
            con.createStatement().execute(customers);
            con.createStatement().execute(flights);
            con.createStatement().execute(tickets);
            con.createStatement().execute(payments);
            con.createStatement().execute(credit_cards);
            con.createStatement().execute(airplanes);
            con.createStatement().execute(locations);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String archive = "CREATE TABLE IF NOT EXISTS transactions (\n"
                + DB.TICKET_ID + " text NOT NULL,\n"
                + "date_time text NOT NULL,\n"
                + "FOREIGN KEY ("+ DB.TICKET_ID +") REFERENCES "
                + DB.TICKETS_TABLE + "("+ DB.TICKET_ID +")\n"
                + ");";

//        populate tables in database with fake data
        populateFakeData();

    }

//    fill tables with fake data by uncommenting if needed
    private static void populateFakeData() {
//        populateLocations();
//        populateAirplanes();
//        populateFlights();
//        populateUsers();
    }

    private static void populateUsers() {
        String sql = "INSERT INTO "+DB.CUSTOMERS_TABLE+"(" +
                DB.USER_NAME_ID +","+ DB.FULL_NAME +
                ","+ DB.PASSWORD +","+ DB.EMAIL+","+ DB.PHONE+
                ") VALUES(?,?,?,?,?)";

        for (int i = 0; i<DB.CUSTOMERS.length; i++) {
            try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
                 PreparedStatement pst = con.prepareStatement(sql);) {
                String[] parts = DB.CUSTOMERS[i].split(",");
                pst.setString(1, parts[0]);
                pst.setString(2, parts[1]);
                pst.setString(3, String.valueOf(parts[2].hashCode()));
                pst.setString(4, parts[3]);
                pst.setString(5, parts[4]);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private static void populateFlights() {
        String sql = "INSERT INTO "+DB.FLIGHTS_INFO_TABLE+"(" +
                DB.DESTINATION +","+ DB.DEPARTURE +
                ","+ DB.TIME +","+ DB.AIRPLANE_ID+
                ") VALUES(?,?,?,?)";

        for (int i = 0; i<DB.FLIGHT_INFOS.length; i++) {
            try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
                 PreparedStatement pst = con.prepareStatement(sql);) {
                String[] parts = DB.FLIGHT_INFOS[i].split(",");
                pst.setString(1, parts[0]);
                pst.setString(2, parts[1]);
                pst.setString(3, parts[2]);
                pst.setString(4, parts[3]);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void populateAirplanes() {
        String sql = "INSERT INTO "+DB.AIRPLANES_TABLE+"(" +
                DB.AIRPLANE_ID +","+ DB.AIRLINE_NAME +","+ DB.AIRPLANE_SEATS +
                ") VALUES(?,?,?)";

        for (int i = 0; i<DB.AIRPLANES.length; i++) {
            try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
                 PreparedStatement pst = con.prepareStatement(sql);) {
                String[] parts = DB.AIRPLANES[i].split(",");
                pst.setString(1, parts[0]);
                pst.setString(2, parts[1]);
                pst.setString(3, parts[2]);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void populateLocations() {
        String sql = "INSERT INTO "+DB.LOCATIONS_TABLE+"(" +
                DB.LOCATION_NAME +","+ DB.DISTANCE_FROM_WEST +
                ") VALUES(?,?)";

        for (int i = 0; i<DB.LOCATIONS.length; i++) {
            try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
                 PreparedStatement pst = con.prepareStatement(sql);) {
                pst.setString(1, DB.LOCATIONS[i]);
                pst.setString(2, DB.DISTANCE[i]);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
