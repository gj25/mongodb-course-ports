import models.MongoDB;
import play.*;
import play.mvc.*;
import static play.mvc.Results.*;
import views.html.*;

public class Global extends GlobalSettings 
{
  @Override
  public void onStart(Application app) 
  {
    MongoDB.connect();
    Logger.info("Connected to MongoDB");
  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }
  
  @Override
  public Result onError(Http.RequestHeader rh, Throwable t) {
      Logger.error("Internal Server Error", t);
      return internalServerError(error_template.render(t.getMessage()));
  }
}
