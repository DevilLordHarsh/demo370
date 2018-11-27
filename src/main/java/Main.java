import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    // expected params from post requests
    private static final String USER_NAME = "uname";
    private static final String FULL_NAME = "fname";
    private static final String PASSWORD = "psw";
    private static final String EMAIL = "eml";
    private static final String PHONE = "phn";

    private static final String LOGOUT = "logout";

    // Return to homepage button
    private static final String RET_HOME_BTN = "<br>\n" +
            "<form class= \"button\" action=\"/\" method=\"get\">\n" +
            "    <input type=\"submit\" value=\"Return\" />\n" +
            "</form>";

    public static void main(String[] args) {
        String currUser;

        // Create and populate databases to demonstrate working of this application
        CreateMyDatabases.init();

        get("/hello", (request, response) -> showTestPage(), new VelocityTemplateEngine());

        // Main program homepage. Enter in browser, address "localhost:4567"
        get("/", (req, res) -> {

            return showHomePage("myself");
        }, new VelocityTemplateEngine());

        // Handling user login requests
        post("/login", (req, res) -> {
            if (UserHandler.loginUser(req.queryParams(USER_NAME), req.queryParams(PASSWORD))) {
                return "welcome "+req.queryParams(USER_NAME);

            }
            else return "Login Error!" + RET_HOME_BTN;
        });

        // Logging out a user
        post("/logout", (req, res) -> {
            // todo: remove from session
            return showHomePage(null);
        });

        // Handling registration requests for new users
        post("/register", (req, res) -> {
            if (UserHandler.registerUser(req.queryParams(USER_NAME),
                    req.queryParams(FULL_NAME), req.queryParams(PASSWORD),
                    req.queryParams(EMAIL), req.queryParams(PHONE))) {
                return "Registered successfully!" + RET_HOME_BTN;
            }
            else return "Registration Error! Try different unique username." + RET_HOME_BTN;
        });
    }

    // Methods serving webpages dynamically

    static ModelAndView showTestPage() {
        Map<String, Object> model = new HashMap<>();
//        model.put("istrue", true);
        model.put("message", "heyhey");
        return new ModelAndView(model, "templates/hello.vm");
    }

    static ModelAndView showHomePage(String username) {
        Map<String, Object> model = new HashMap<>();
        if (username != null) {
            model.put("showlogged", "templates/logged.vm");
            model.put("username", username);
        }
        else {
            model.put("showlogged", "templates/notlogged.txt");
        }
        return new ModelAndView(model, "templates/index.vm");
    }
}
