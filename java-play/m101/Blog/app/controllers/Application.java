package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import models.PostData;
import models.Posts;
import models.Users;
//import org.apache.commons.lang3.StringEscapeUtils;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.*;
import play.mvc.Http.Cookie;
import play.mvc.Http.RequestHeader;
import scala.actors.threadpool.Arrays;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        String username = checkLogin(request());
        if (username == null) {
            return redirect("/login");
        }
        
        Posts postMgr = new Posts();
        List<PostData> posts = postMgr.getPosts();
        return ok(blog_template.render(username, posts));
    }
    
    public static Result showPost(String permalink) {
        String username = checkLogin(request());
//        if (username == null) {
//            return redirect("/login");
//        } 

        Posts postMgr = new Posts();
        PostData post = postMgr.getByPermalink(permalink);
        if(post == null) {
            return redirect("/post_not_found");
        }
        
        return ok(entry_template.render(username, post, "", new PostData.Comment()));
    }

    public static Result postNewcomment() {
        String username = checkLogin(request());
        if (username == null) {
            return redirect("/login");
        }
        
        DynamicForm form = form().bindFromRequest();
        String commentName = form.get("commentName");
        String commentBody = form.get("commentBody");
        String permalink = form.get("permalink");
        
        PostData.Comment comment = new PostData.Comment();
        comment.setAuthor(commentName);
        comment.setBody(commentBody);
        comment.setEmail(form.get("commentEmail"));
            
        Posts postMgr = new Posts();
        PostData post = postMgr.getByPermalink(permalink);
        if(post == null) {
            return redirect("/post_not_found");
        }
        
        if(commentName == null || commentName.equals("") ||
           commentBody == null || commentBody.equals(""))
        {
            String errors="Post must contain your name and an actual comment.";
            Logger.info("newcomment: comment contained error..returning form with errors");
            // should be a badRequest()
            return ok(entry_template.render(username, post, errors, comment));
        }
        else
        {
            postMgr.addComment(permalink, comment);
            Logger.info("newcomment: added the comment....redirecting to post");
            return redirect("/post/"+permalink);
        }
    }

    public static Result postNotFound() {
        // should be notFound()
        return ok("Sorry, post not found");
    }

    public static Result getNewpost() {
        String username = checkLogin(request());
        if (username == null) {
            return redirect("/login");
        }
        return ok(newpost_template.render("", "", "", "", username));
    }
    
    public static Result postNewpost() {
        String username = checkLogin(request());
        if (username == null) {
            return redirect("/login");
        }
 
        DynamicForm form = form().bindFromRequest();
        String title = form.get("subject");
        String post = form.get("body");
        String tags = form.get("tags");
        
        if (title.equals("") || post.equals("")) {
            String errors="Post must contain a title and blog entry";
            return badRequest(newpost_template.render(username, errors, 
                                                      title, 
                                                      post, 
                                                      tags));
        }
        
        List<String> tagList = (tags != null) ? extractTags(tags) : null;
        Posts postMgr = new Posts();
        String permalink = postMgr.insertEntry(username, title, post, tagList);
        Logger.info("newcomment: added the comment....redirecting to post");
        return redirect("/post/"+permalink);
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

        HashMap<String, String> errors = new HashMap<String, String>();
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
    
    public static Result presentWelcome() {
        String username = checkLogin(request());
        if (username == null) {
            Logger.info("welcome: can't identify user...redirecting to signup");
            return redirect("/signup");
        }
        return ok(welcome.render(username));
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
    
    private static String checkLogin(RequestHeader request)
    {
        Cookie cookie = request.cookies().get("session");
        if (cookie == null || cookie.value() == null) {
            Logger.info("no cookie...");
            return null;
        }
        else {
            String cookieVal = cookie.value();
            Users users = new Users();
            String sessionId = users.checkSecureVal(cookieVal);
            if (sessionId == null) {
                Logger.info("no secure session id");
                return null;
            }
            return users.getSession(sessionId);
        }
    }
    
    private static List<String> extractTags(String tags)
    {
        String tagsNoWhite = tags.replaceAll("\\s", "");
        return new ArrayList(Arrays.asList(tagsNoWhite.split(",")));
    }
    
//    private static String replaceNewLines(String input)
//    {
//        return input.replaceAll("\\r?\\n", "<p>");
//    }
}