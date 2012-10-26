package controllers;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import play.Logger;
import play.mvc.*;

public class Application extends Controller {

    public static Result index() {
        return ok("Your new application is ready.");
    }

    public static Result getHw1(Integer n) {
        Logger.info("Received request. n=" + n);
        
        try {
            String result = null;
                        
            Mongo m = new Mongo();
            DB db = m.getDB("m101");
            DBCollection coll = db.getCollection("funnynumbers");               
            DBCursor cursor = coll.find().limit(1).skip(n).sort(new BasicDBObject("value", 1));
            try {
                result = cursor.next().get("value") + "\n";
            } finally {
                cursor.close();
            }

            return ok(result);
        } catch (UnknownHostException ex) {
            Logger.error("Failed to connect to mongoDB", ex);
            return internalServerError("Failed to connect to mongoDB");
        } catch (Exception ex) {
            Logger.error("Unknown error", ex);
            return internalServerError("Unknown error");
        }
    }
}