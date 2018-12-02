import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to help easily connect/disconnect to a database
 * given it's file location
 */
public class DatabaseHandler {

    /**
     * Connects to a specified database
     * @param dbFile location of database file to connect to
     * @return a Connection object
     */
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

    /**
     * Closes an open connection to any database
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getDestinations() {
        ArrayList<String> locations = new ArrayList<>();

        String sql = "SELECT "+DB.LOCATION_NAME+" FROM " +
                DB.LOCATIONS_TABLE;

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql);) {
            while(rs.next()) {
                locations.add(rs.getString(DB.LOCATION_NAME));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }

    public static ArrayList<String> getDepartures(String destination) {
        ArrayList<String> locations = new ArrayList<>();
        Map<String, Integer> loc_distance = new HashMap<>();
        int des_dis = 0;

        String sql = "SELECT * FROM " + DB.LOCATIONS_TABLE;

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql);) {
            while(rs.next()) {
                loc_distance.put(rs.getString(DB.LOCATION_NAME), Integer.valueOf(rs.getString(DB.DISTANCE_FROM_WEST)));
                if (rs.getString(DB.LOCATION_NAME).equals(destination)) {
                    des_dis = Integer.valueOf(rs.getString(DB.DISTANCE_FROM_WEST));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> entry : loc_distance.entrySet())
        {
            if (Math.abs(entry.getValue() - des_dis) > DB.MIN_DIS_REQ_DEST_TO_DEP) {
                locations.add(entry.getKey()+" $"+(Math.abs(entry.getValue() - des_dis)*1.2));
            }
        }
        return locations;
    }

    public static ArrayList<String> getTimes(String dest, String dep) {
        ArrayList<String> times = new ArrayList<>();

        String sql = "SELECT "+DB.TIME+" FROM " +
                DB.FLIGHTS_INFO_TABLE+ " WHERE " +
                DB.DESTINATION + " = '" +dest+"' AND "+
                DB.DEPARTURE + " = '" + dep +"'" ;

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql);) {
            while(rs.next()) {
                times.add(rs.getString(DB.TIME));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return times;
    }

    public static ArrayList<String[]> getTicketsHistory(String username) {
        ArrayList<String[]> tickets = new ArrayList<>();

        String sql = "SELECT a."+DB.AIRLINE_NAME+", a."+DB.AIRPLANE_SEATS+", t."+DB.TICKET_ID+", f."+DB.DESTINATION+", f."+DB.DEPARTURE+"," +
                "t."+DB.SEAT+", f."+DB.TIME+", p."+DB.CARD_NUMBER+", p."+DB.AMOUNT+", t."+DB.STATUS+
        "\n FROM "+DB.CUSTOMERS_TABLE+" AS c, "+DB.TICKETS_TABLE+" AS t, "+DB.AIRPLANES_TABLE+" AS a, "+DB.FLIGHTS_INFO_TABLE+" AS f, "+DB.PAYMENTS_TABLE+" AS p"+
        "\n WHERE (c."+DB.USER_NAME_ID+" = t."+DB.USER_NAME_ID+" AND "+
                "f."+DB.FLIGHT_INFO_ID+" = t."+DB.FLIGHT_INFO_ID+" AND "+
                "p."+DB.PAYMENT_ID+" = t."+DB.PAYMENT_ID+" AND "+
                "a."+DB.AIRPLANE_ID+" = f."+DB.AIRPLANE_ID+" AND "+
                "t."+DB.USER_NAME_ID+" = '"+username+"')";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql);) {
            while(rs.next()) {
                String[] ticketAttributes = new String[9];
                ticketAttributes[0] = rs.getString(DB.DESTINATION);
                ticketAttributes[1] = rs.getString(DB.DEPARTURE);
                ticketAttributes[2] = rs.getString(DB.TIME);
                ticketAttributes[3] = rs.getString(DB.AIRLINE_NAME);
                ticketAttributes[4] = rs.getString(DB.CARD_NUMBER);
                ticketAttributes[5] = rs.getString(DB.AMOUNT);
                ticketAttributes[6] = rs.getString(DB.STATUS);
                ticketAttributes[7] = rs.getString(DB.TICKET_ID);
                ticketAttributes[8] = rs.getString(DB.SEAT);

                tickets.add(ticketAttributes);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    // increases or decreases number of seats by 1
    private static void updateSeats(String airplane_id, Boolean incByOne) {
        int currentSeats = 0;

        String seats = "SELECT "+DB.AIRPLANE_SEATS+" FROM "+DB.AIRPLANES_TABLE+" WHERE "
                +DB.AIRPLANE_ID+" = '"+airplane_id+"'";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(seats);) {
            if (rs.next()) {
                currentSeats = Integer.valueOf(rs.getString(DB.AIRPLANE_SEATS));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int addSeatDiff = 0;
        if (incByOne) addSeatDiff = 1;
        else addSeatDiff = -1;
        addSeatDiff += currentSeats;

        String sql = "UPDATE " + DB.AIRPLANES_TABLE + " SET " + DB.AIRPLANE_SEATS + " = ?" +
                "\n WHERE " + DB.AIRPLANE_ID + " = '" + airplane_id + "'";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);) {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, String.valueOf(addSeatDiff));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cancelTicket(String ticketId) {

        String sql = "UPDATE " + DB.TICKETS_TABLE + " SET " + DB.STATUS + " = ?" +
                "\n WHERE " + DB.TICKET_ID + " = '" + ticketId + "'";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);) {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, DB.INACTIVE_TICKET);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String airplaneId = "SELECT f."+DB.AIRPLANE_ID+
                "\n FROM "+DB.FLIGHTS_INFO_TABLE+" AS f, "+DB.TICKETS_TABLE+" AS t "+
                "\n WHERE (t."+DB.FLIGHT_INFO_ID+" = f."+DB.FLIGHT_INFO_ID+" AND "+
                "t."+DB.TICKET_ID+" = '"+ticketId+"')";

//        updateSeats(airplaneId, true);
    }

        public static boolean addTicket(String username, String dest, String dep, String time, String cardnum, String cvv, String expiry, String price) {

        boolean success = false;
        String sqlcreditcardcreation = "INSERT INTO "+DB.CREDIT_CARDS_TABLE+"(" +
                DB.CARD_NUMBER +","+ DB.CVV +","+ DB.EXPIRY +") VALUES(?,?,?)";

        String sqlpaymentcreation = "INSERT INTO "+DB.PAYMENTS_TABLE+"(" +
                DB.AMOUNT +","+ DB.CARD_NUMBER +") VALUES(?,?)";

        String createTicket = "INSERT INTO "+DB.TICKETS_TABLE+"(" +
                DB.USER_NAME_ID +","+ DB.PAYMENT_ID +","+ DB.FLIGHT_INFO_ID +
                ","+DB.SEAT+","+DB.STATUS+") VALUES(?,?,?,?,?)";

        String flightinfoid = "";
        String paymentid = "";
        String seatnum = "";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);) {
            PreparedStatement pst = con.prepareStatement(sqlcreditcardcreation);
            pst.setString(1, cardnum);
            pst.setString(2, String.valueOf(cvv.hashCode()));
            pst.setString(3, String.valueOf(expiry.hashCode()));
            pst.executeUpdate();

            pst = con.prepareStatement(sqlpaymentcreation);
            pst.setString(1, price);
            pst.setString(2, cardnum);
            pst.executeUpdate();

            paymentid = getPaymentId(price, cardnum);
            flightinfoid = getFlightInfoId(dest, dep, time);

            String availableseat = "SELECT "+DB.AIRPLANE_SEATS+" FROM "+DB.AIRPLANES_TABLE+" WHERE "
                    +DB.AIRPLANE_ID+" = ("+"SELECT "+DB.AIRPLANE_ID+" FROM "+DB.FLIGHTS_INFO_TABLE+" WHERE "
                    +DB.FLIGHT_INFO_ID+" = '"+flightinfoid+"'"+")";

            try (Connection con2 = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
                 Statement stm2 = con2.createStatement();
                 ResultSet rs2 = stm2.executeQuery(availableseat);) {
                if (rs2.next()) {
                    seatnum = rs2.getString(DB.AIRPLANE_SEATS);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            pst = con.prepareStatement(createTicket);
            pst.setString(1, username);
            pst.setString(2, paymentid);
            pst.setString(3, flightinfoid);
            pst.setString(4, seatnum);
            pst.setString(5, DB.ACTIVE_TICKET);
            pst.executeUpdate();

            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }

        String airplaneId = "SELECT "+DB.AIRPLANE_ID+" FROM "+DB.FLIGHTS_INFO_TABLE+" WHERE "
                +DB.FLIGHT_INFO_ID+" = '"+flightinfoid+"'";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(airplaneId);) {
            if (rs.next()) {
                airplaneId = rs.getString(DB.AIRPLANE_ID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateSeats(airplaneId, false);

        return success;
    }

    public static String getTicketId(String userNameId, String paymentId, String flightInfoId) {
        String sql = "SELECT " + DB.TICKET_ID + " FROM " +
                        DB.TICKETS_TABLE + " WHERE " +
                        DB.USER_NAME_ID + " = '" + userNameId + "' AND "+
                        DB.PAYMENT_ID + " = '" + paymentId + "' AND "+
                        DB.FLIGHT_INFO_ID + " = '" + flightInfoId + "'";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql);) {
            if (rs.next()) {
                return String.valueOf(rs.getString(DB.TICKET_ID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static double getTicketPrice(String dest, String dep) {
        String[] sqls = {
                "SELECT " + DB.DISTANCE_FROM_WEST + " FROM " +
                        DB.LOCATIONS_TABLE + " WHERE " +
                        DB.LOCATION_NAME + " = '" + dest + "'",
                "SELECT " + DB.DISTANCE_FROM_WEST + " FROM " +
                        DB.LOCATIONS_TABLE + " WHERE " +
                        DB.LOCATION_NAME + " = '" + dep + "'"
        };

        double[] distances = new double[2];

        for (int i = 0; i < 2; i++) {
            try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
                 Statement stm = con.createStatement();
                 ResultSet rs = stm.executeQuery(sqls[i]);) {
                if (rs.next()) {
                    distances[i] = Integer.valueOf(rs.getString(DB.DISTANCE_FROM_WEST));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return (1.2*(Math.abs(distances[0]-distances[1])));
    }

    public static String getFlightInfoId(String dest, String dep, String time) {
        String sql = "SELECT " + DB.FLIGHT_INFO_ID + " FROM " +
                        DB.FLIGHTS_INFO_TABLE + " WHERE " +
                        DB.DESTINATION + " = '" + dest + "' AND "+
                        DB.DEPARTURE + " = '" + dep + "' AND "+
                        DB.TIME + " = '" + time + "'";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql);) {
            if (rs.next()) {
                return String.valueOf(rs.getString(DB.FLIGHT_INFO_ID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getPaymentId(String amount, String cardnum) {
        String sql = "SELECT " + DB.PAYMENT_ID + " FROM " +
                DB.PAYMENTS_TABLE + " WHERE " +
                DB.AMOUNT + " = '" + amount + "' AND "+
                DB.CARD_NUMBER + " = '" + cardnum + "'";

        try (Connection con = DatabaseHandler.createOrConnect(DB.PTBS_DATABASE);
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql);) {
            if (rs.next()) {
                return String.valueOf(rs.getString(DB.PAYMENT_ID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
