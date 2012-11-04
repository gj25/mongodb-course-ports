package controllers;

import java.util.HashMap;
import models.Users;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok("This is a placeholder for the blog.");
    }

    public static Result presentSignup() {
        return ok(signup.render("", "", "", "", "", ""));
    }

    public static Result presentLogin() {
        return ok(login.render("", "", ""));
    }

    public static Result processLogin() {
        //String username = request().body().asFormUrlEncoded().get("username")[0];
        DynamicForm form = form().bindFromRequest();
        String username = form.get("username");
        String password = form.get("password");

        Users users = new Users();
        if (users.validateLogin(username, password)) {
            String sessionId = users.startSession(username);
            String cookie = users.makeSecureVal(sessionId);
            response().setCookie("session", cookie);
            return redirect("/welcome");
        }
        else {
            return ok(login.render(username, "", "Invalid Login"));
        }
    }

    public static Result processLogout() {
        String cookie = request().cookies().get("session").value();
        if (cookie == null) {
            Logger.info("no cookie...");
            return redirect("/signup");
        }
        else {
            Users users = new Users();
            String sessionId = users.checkSecureVal(cookie);
            if (sessionId == null) {
                Logger.info("no secure session id");
                return redirect("/signup");
            }
            users.endSession(sessionId);

            response().setCookie("session", "");
            return redirect("/signup");
        }
    }

    public static Result processSignup() {
        DynamicForm form = form().bindFromRequest();
        String username = form.get("username");
        String password = form.get("password");
        String verify = form.get("verify");
        String email = form.get("email");

        HashMap<String, String> errors = new HashMap();
        errors.put("username", username);
        errors.put("email", email);

        Users users = new Users();
        if (users.validateSignup(username, password, verify, email, errors)) {
            if (!users.newUser(username, password, email)) {
                errors.put("username_error", "Username already in use. Please choose another");
                return signupError(errors);
            }

            String sessionId = users.startSession(username);
            String cookie = users.makeSecureVal(sessionId);
            response().setCookie("session", cookie);
            
            return redirect("/welcome");
        }
        else {
            Logger.info("User did not validate");
            return signupError(errors);
        }
    }

    private static Result signupError(HashMap<String, String> errors) {
        // Find better way (i.e. pass hashmap to template)
        return ok(signup.render(errors.get("username"),
                errors.get("password_error"),
                errors.get("email"),
                errors.get("username_error"),
                errors.get("email_error"),
                errors.get("verify_error")));
    }
    
    private static String checkLogin(String cookie)
    {
        if (cookie == null) {
            Logger.info("no cookie...");
            return null;
        }
        else {
            Users users = new Users();
            String sessionId = users.checkSecureVal(cookie);
            if (sessionId == null) {
                Logger.info("no secure session id");
                return null;
            }
            return users.getSession(sessionId);
        }
    }

    public static Result presentWelcome() {
        String cookie = request().cookies().get("session").value();
        
        String username = checkLogin(cookie);
        if (username == null)
        {
            Logger.info("welcome: can't identify user...redirecting to signup");
            return redirect("/signup");
        }
        return ok(welcome.render(username));
    }
}