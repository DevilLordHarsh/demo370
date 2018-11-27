import spark.ModelAndView;
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
            if (req.session().attributes().isEmpty()) {
                return showPage(null, INDEX);
            }
            else return showPage(req.session().attribute(USER_NAME), INDEX);
        }, new VelocityTemplateEngine());

        // Handling user login requests
        post("/login", (req, res) -> {

            if (UserHandler.loginUser(req.queryParams(USER_NAME), req.queryParams(PASSWORD))) {
                req.session().attribute(USER_NAME, req.queryParams(USER_NAME));
                Set<String> trackuser = req.session().attributes();
                Set<String> trackuserValues = new HashSet<>();
                for (String s: trackuser) {
                    trackuserValues.add(req.session().attribute(s));
                }
                req.session().invalidate();
                Iterator iterator1 = trackuser.iterator();
                Iterator iterator2 = trackuserValues.iterator();
                while (iterator1.hasNext()){
                    req.session().attribute((String) iterator1.next(), iterator2.next());
                }
                return showPage(req.session().attribute(USER_NAME), INDEX);
            }
            else return showError("Login Error!");
        }, new VelocityTemplateEngine());

        // Logging out a user
        post("/logout", (req, res) -> {
            req.session().invalidate();
            return showPage(null, INDEX);
        }, new VelocityTemplateEngine());

        // Show user their profile page
        post("/userprofile", (req, res) -> {
            if (req.session().attributes().isEmpty()) {
                return showPage(null, USER_PROFILE);
            }
            else return showPage(req.session().attribute(USER_NAME), USER_PROFILE);
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
        model.put("istrue", true);
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
