import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.*;

import static spark.Spark.*;

public class Main {

    // expected params from post requests
    private static final String USER_NAME = "uname";
    private static final String FULL_NAME = "fname";
    private static final String PASSWORD = "psw";
    private static final String EMAIL = "eml";
    private static final String PHONE = "phn";
    private static final String CARD_NUM = "cnumber";
    private static final String CVV = "ccvv";
    private static final String EXPIRY = "cexpiry";

    // webpage template file locations
    private static final String INDEX = "templates/index.vm";
    private static final String USER_PROFILE = "templates/useraccount.vm";
    private static final String LOGGED = "templates/logged.vm";
    private static final String NOT_LOGGED = "templates/notlogged.vm";
    private static final String ERROR = "templates/error.vm";

    // variable names in vm files
    private static final String DEST = "destinationlocation";
    private static final String DEP = "departurelocation";
    private static final String TIME = "time";
    private static final String SHOW_LOGGED = "showlogged";
    private static final String TICKETS_HISTORY = "tickets_history";

    private static final int MAX_AGE = 3600;

    public static void main(String[] args) {

        // Create and populate databases to demonstrate working of this application
        CreateMyDatabases.init();

        // Main program homepage. Enter in browser, address "localhost:4567"
        get("/", (req, res) -> {
            setCookies(req, res);
            String[] params = getCookies(req);
            updateParams(req, params);
            return showPage(params[0], params[1], params[2], params[3], INDEX);
        }, new VelocityTemplateEngine());

        // Handling user login requests
        post("/login", (req, res) -> {
            if (AuthorizationSystem.loginUser(req.queryParams(USER_NAME), req.queryParams(PASSWORD))) {
                res.cookie(USER_NAME, req.queryParams(USER_NAME), MAX_AGE);
                res.redirect("/");
            }
            return showError("Login Error!");
        }, new VelocityTemplateEngine());

        // Logging out a user
        post("/logout", (req, res) -> {
            clearUserSession(res);
            res.redirect("/");
        return "Redirect failed!";});

        // Resets selections made by user of flight details
        post("/reset", (req, res) -> {
            clearFlightSelectionDetails(res);
            res.redirect("/");
            return "Redirect failed!";});

        // Show user their profile page
        post("/userprofile", (req, res) -> {
            String[] params = getCookies(req);
            return showPage(params[0], params[1], params[2], params[3], USER_PROFILE);
        }, new VelocityTemplateEngine());

        // Handling registration requests for new users
        post("/register", (req, res) -> {
            if (AuthorizationSystem.registerUser(req.queryParams(USER_NAME),
                    req.queryParams(FULL_NAME), req.queryParams(PASSWORD),
                    req.queryParams(EMAIL), req.queryParams(PHONE))) {
                return showError("Registered successfully!");
            }
            else return showError("Registration Error! Try different unique username.");
        }, new VelocityTemplateEngine());

        // Getting credit card info to process payment
        post("/payment", (req, res) -> {
            String[] params = getCookies(req);
            if (AuthorizationSystem.loginUser(params[0], req.queryParams(PASSWORD))) {
                String price = String.valueOf(DatabaseHandler.getTicketPrice(params[1], params[2]));
                if (PaymentSystem.processPayment(req.queryParams(CARD_NUM), req.queryParams(CVV),
                        req.queryParams(EXPIRY), price)) {
                    DatabaseHandler.addTicket(params[0], params[1], params[2], params[3],
                            req.queryParams(CARD_NUM), req.queryParams(CVV),
                            req.queryParams(EXPIRY), price);
//                    todo: send email and text to email addr and phone num of the user
                    return showPage(params[0], params[1], params[2], params[3], USER_PROFILE);
                }
                else return showError("Payment did not complete successfully!");
            }
            return showError("Wrong password!");
        }, new VelocityTemplateEngine());
    }

    // Methods serving webpages dynamically
    static ModelAndView showError(String message) {
        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        return new ModelAndView(model, ERROR);
    }

    static ModelAndView showPage(String name, String dest, String dep, String time, String vmfile) {
        Map<String, Object> model = new HashMap<>();
        if (name != null) {
            model.put(SHOW_LOGGED, LOGGED);
            model.put(USER_NAME, name);
        }
        else model.put(SHOW_LOGGED, NOT_LOGGED);
        if (dest != null) {
            if (dep != null) {
                model.put("price", DatabaseHandler.getTicketPrice(dest, dep));
                if (time != null) {
                    model.put(TIME, time);
                }
                else model.put("times", DatabaseHandler.getTimes(dest, dep));
                model.put("departurechosen", dep);
            }
            else model.put("departurelocations", DatabaseHandler.getDepartures(dest));
            model.put("destinationchosen", dest);
        }
        else model.put("destinationlocations", DatabaseHandler.getDestinations());

        if (vmfile.equals(USER_PROFILE)) {
            model.put(TICKETS_HISTORY, DatabaseHandler.getTicketHistory(name));
        }
        return new ModelAndView(model, vmfile);
    }


    private static void updateParams(Request req, String[] params) {
        if (req.queryParams(USER_NAME) != null) {
            params[0] = req.queryParams(USER_NAME);
        }
        if (req.queryParams(DEST) != null) {
            params[1] = req.queryParams(DEST);
        }
        if (req.queryParams(DEP) != null) {
            params[2] = req.queryParams(DEP);
        }
        if (req.queryParams(TIME) != null) {
            params[3] = req.queryParams(TIME);
        }

    }

    private static String[] getCookies(Request req) {
        String[] params = new String[4];
        Map cookies = req.cookies();
        if (cookies.containsKey(USER_NAME)) {
            params[0] = req.cookie(USER_NAME);
        } else params[0] = null;
        if (cookies.containsKey(DEST)) {
            params[1] = req.cookie(DEST);
        } else params[1] = null;
        if (cookies.containsKey(DEP)) {
            params[2] = req.cookie(DEP);
        } else params[2] = null;
        if (cookies.containsKey(TIME)) {
            params[3] = req.cookie(TIME);
        } else params[3] = null;
        return params;
    }

    private static void setCookies(Request req, Response res) {
        if (req.queryParams(DEST) != null) res.cookie(DEST, req.queryParams(DEST), MAX_AGE);
        if (req.queryParams(DEP) != null) res.cookie(DEP, req.queryParams(DEP), MAX_AGE);
        if (req.queryParams(TIME) != null) res.cookie(TIME, req.queryParams(TIME), MAX_AGE);
    }

//    deletes all stored cookies, meaning user is logged out
    private static void clearUserSession(Response res) {
        res.removeCookie(USER_NAME);
        clearFlightSelectionDetails(res);
    }

//    reset flight selections made by the user
    private static void clearFlightSelectionDetails(Response res) {
        res.removeCookie(DEST);
        res.removeCookie(DEP);
        res.removeCookie(TIME);
    }

}
