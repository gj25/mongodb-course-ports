package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
  public static Result index() {
    return ok("This is a placeholder for the blog.");
  }
  
  public static Result presentSignup()
  {
      return ok(signup.render("Signup", ""));
  }
  
  public static Result presentLogin()
  {
      return ok(login.render("Login", ""));
  }
  
  public static Result processLogin()
  {
      return ok();
  }
}