import java.util.ArrayList;

/**
 * Class to store finals variables
 * for our databases and its tables
 */
public class DB {

    // database file locations
    public static final String PTBS_DATABASE = "src/main/resources/dbs/PTBS.db";

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
    public static final String ACTIVE_TICKET = "Active";
    public static final String INACTIVE_TICKET = "Cancelled";
    public static final String SEAT = "seat";
    public static final String STATUS = "status";

    public static final String CREDIT_CARDS_TABLE = "credit_cards";
    public static final String CARD_NUMBER = "card_number";
    public static final String CVV = "cvv";
    public static final String EXPIRY = "expiry";

    public static final String PAYMENTS_TABLE = "payments";
    public static final String PAYMENT_ID = "payment_id";
    public static final String AMOUNT = "amount";

    public static final String AIRPLANES_TABLE = "airplanes";
    public static final String AIRPLANE_ID = "airplane_id";
    public static final String AIRLINE_NAME = "airline_name";
    public static final String AIRPLANE_SEATS = "airplane_seats";

    public static final String LOCATIONS_TABLE = "locations";
    public static final String LOCATION_NAME = "location_name";
    public static final String DISTANCE_FROM_WEST = "distance_from_west";

    public static final String[] LOCATIONS = {"Vancouver", "Burnaby", "Abbotsford", "Airdrie",
            "Calgary", "Toronto"};
    public static final String[] DISTANCE = {"78", "100", "150", "480", "550", "700"};
    public static final int MIN_DIS_REQ_DEST_TO_DEP = 100;

    public static final String[] AIRPLANES = {"1001,QuickAirlines,60", "1002,QuickAirlines,70",
    "1003,JetWays,58", "1004,CuisineLights,90"};

    public static final String[] FLIGHT_INFOS = {"Abbotsford,Airdrie,27Dec/2018,1001", "Abbotsford,Calgary,2Dec/2019,1002", "Abbotsford,Toronto,7Dec/2019,1001",
    "Airdrie,Vancouver,5Dec/2019,1002", "Airdrie,Burnaby,8June/2019,1001", "Airdrie,Abbotsford,20May/2019,1003", "Airdrie,Toronto,23Jan/2019,1002",
    "Burnaby,Airdrie,22Jan/2019,1003", "Burnaby,Calgary,8July/2019,1002", "Burnaby,Toronto,6Mar/2019,1004",
    "Calgary,Vancouver,20Jan/2019,1004", "Calgary,Burnaby,15Feb/2019,1003", "Calgary,Abbotsford,1Mar/2019,1003", "Calgary,Toronto,12April/2019,1001",
    "Toronto,Vancouver,12Jan/2019,1001", "Toronto,Abbotsford,18Feb/2019,1004", "Toronto,Calgary,10Mar/2019,1002", "Toronto,Airdrie,21June/2019,1002", "Toronto,Burnaby,27Dec/2019,1004",
    "Vancouver,Airdrie,14Jan/2019,1002", "Vancouver,Calgary,12Feb/2019,1003", "Vancouver,Toronto,12Mar/2019,1001"};

    public static final String[] CUSTOMERS = {"userb,user B,hashed,user@B.com,7789324981"};
}
