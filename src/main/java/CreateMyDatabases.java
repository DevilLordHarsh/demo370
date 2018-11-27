import java.sql.Connection;
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
                + DB.FLIGHT_INFO_ID + " text PRIMARY KEY,\n"
                + DB.DESTINATION + " text NOT NULL,\n"
                + DB.DEPARTURE + " text NOT NULL,\n"
                + DB.TIME + " text NOT NULL,\n"
                + DB.AIRPLANE_ID + " text NOT NULL,\n"
                + "FOREIGN KEY ("+ DB.AIRPLANE_ID +") REFERENCES "
                + DB.AIRPLANES_TABLE + "("+ DB.AIRPLANE_ID +")"
                + ");";

        String tickets = "CREATE TABLE IF NOT EXISTS "+ DB.TICKETS_TABLE+" (\n"
                + DB.TICKET_ID + " text PRIMARY KEY,\n"
                + DB.USER_NAME_ID + " text NOT NULL,\n"
                + DB.PAYMENT_ID + " text NOT NULL,\n"
                + DB.FLIGHT_INFO_ID + " text NOT NULL,\n"
                + "FOREIGN KEY ("+ DB.USER_NAME_ID +") REFERENCES "
                + DB.CUSTOMERS_TABLE + "("+ DB.USER_NAME_ID +"),\n"
                + "FOREIGN KEY ("+ DB.PAYMENT_ID +") REFERENCES "
                + DB.PAYMENTS_TABLE + "("+ DB.PAYMENT_ID +"),\n"
                + "FOREIGN KEY ("+ DB.FLIGHT_INFO_ID +") REFERENCES "
                + DB.FLIGHTS_INFO_TABLE + "("+ DB.FLIGHT_INFO_ID +")\n"
                + ");";

        String payments = "CREATE TABLE IF NOT EXISTS "+ DB.PAYMENTS_TABLE+" (\n"
                + DB.PAYMENT_ID + " text PRIMARY KEY,\n"
                + DB.AMOUNT + " text NOT NULL,\n"
                + DB.STATUS + " text NOT NULL,\n"
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
                + DB.AIRLINE_NAME + " text NOT NULL\n"
                + ");";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);) {
            con.createStatement().execute(customers);
            con.createStatement().execute(flights);
            con.createStatement().execute(tickets);
            con.createStatement().execute(payments);
            con.createStatement().execute(credit_cards);
            con.createStatement().execute(airplanes);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String archive = "CREATE TABLE IF NOT EXISTS transactions (\n"
                + DB.TICKET_ID + " text NOT NULL,\n"
                + "date_time text NOT NULL,\n"
                + "FOREIGN KEY ("+ DB.TICKET_ID +") REFERENCES "
                + DB.TICKETS_TABLE + "("+ DB.TICKET_ID +")\n"
                + ");";

        try (Connection con = DatabaseHandler.createOrConnect(DB.ARCHIVE_DATABASE);) {
            con.createStatement().execute(archive);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // populate tables in database with fake data
        populateFakeData();

    }

    private static void populateFakeData() {
        // todo: fill tables with fake data

        //locations available for travel
        String VANCOUVER = "Vancouver";
        String AIRDRIE = "Airdrie";
        String BURNABY = "Burnaby";
        String CALGARY = "Calgary";
        String TORONTO = "Toronto";
        String ABBOTSFORD = "Abbotsford";

        //airline companies and available airplanes
        String QUICK_AIRLINES = "QuickAirlines";
        String WINDJET = "WindJet";
        String BLAZEJET = "BlazeJet";

        String CRAZY_AIRLINES = "CrazyAirlines";
        String KNOTCHJET = "KnotchJet";
        String RICOCHETJET = "RicochetJet";
    }

}
