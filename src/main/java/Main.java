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

    // webpage template file locations
    private static final String INDEX = "templates/index.vm";
    private static final String USER_PROFILE = "templates/userprofile.vm";
    private static final String LOGGED = "templates/logged.vm";
    private static final String NOT_LOGGED = "templates/notlogged.vm";
    private static final String ERROR = "templates/error.vm";


    public static void main(String[] args) {

        // Create and populate databases to demonstrate working of this application
        CreateMyDatabases.init();

        get("/hello", (request, response) -> showTestPage(), new VelocityTemplateEngine());

        // Main program homepage. Enter in browser, address "localhost:4567"
        get("/", (req, res) -> {

            return showPage(null, INDEX);
        }, new VelocityTemplateEngine());

        // Handling user login requests
        post("/login", (req, res) -> {
            if (UserHandler.loginUser(req.queryParams(USER_NAME), req.queryParams(PASSWORD))) {
                return showPage(req.queryParams(USER_NAME), INDEX);
            }
            else return showError("Login Error!");
        }, new VelocityTemplateEngine());

        // Logging out a user
        post("/logout", (req, res) -> {
            // todo: remove from session
            return showPage(null, INDEX);
        }, new VelocityTemplateEngine());

        // Show user their profile page
        post("/userprofile", (req, res) -> {
            return showPage("myself", USER_PROFILE);
        }, new VelocityTemplateEngine());

        // Handling registration requests for new users
        post("/register", (req, res) -> {
            if (UserHandler.registerUser(req.queryParams(USER_NAME),
                    req.queryParams(FULL_NAME), req.queryParams(PASSWORD),
                    req.queryParams(EMAIL), req.queryParams(PHONE))) {
                return showError("Registered successfully!");
            }
            else return showError("Registration Error! Try different unique username.");
        }, new VelocityTemplateEngine());
    }

    // Methods serving webpages dynamically

    static ModelAndView showTestPage() {
        Map<String, Object> model = new HashMap<>();
//        model.put("istrue", true);
        model.put("message", "heyhey");
        return new ModelAndView(model, "templates/hello.vm");
    }

    static ModelAndView showError(String message) {
        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        return new ModelAndView(model, ERROR);
    }

    static ModelAndView showPage(String username, String vmfile) {
        Map<String, Object> model = new HashMap<>();
        if (username != null) {
            model.put("showlogged", LOGGED);
            model.put("username", username);
        }
        else {
            model.put("showlogged", NOT_LOGGED);
        }
        return new ModelAndView(model, vmfile);
    }
}
