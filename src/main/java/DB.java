/**
 * Class to store finals variables
 * for our databases and its tables
 */
public class DB {

    // database file locations
    public static final String PTBS_DATABASE = "src/main/resources/dbs/users.db";
    public static final String ARCHIVE_DATABASE = "src/main/resources/dbs/archive.db";

    // table names and attributes
    public static final String CUSTOMERS_TABLE = "customers";
    public static final String USER_NAME_ID = "user_name_id";
    public static final String FULL_NAME = "full_name";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";

    public static final String FLIGHTS_INFO_TABLE = "flights_info";
    public static final String FLIGHT_INFO_ID = "flight_info_id";
    public static final String DESTINATION = "destination";
    public static final String DEPARTURE = "departure";
    public static final String TIME = "time";

    public static final String TICKETS_TABLE = "tickets";
    public static final String TICKET_ID = "ticket_id";

    public static final String CREDIT_CARDS_TABLE = "credit_cards";
    public static final String CARD_NUMBER = "card_number";
    public static final String CVV = "cvv";
    public static final String EXPIRY = "expiry";

    public static final String PAYMENTS_TABLE = "payments";
    public static final String PAYMENT_ID = "payment_id";
    public static final String AMOUNT = "amount";
    public static final String STATUS = "status";

    public static final String AIRPLANES_TABLE = "airplanes";
    public static final String AIRPLANE_ID = "airplane_id";
    public static final String AIRLINE_NAME = "airline_name";

}
