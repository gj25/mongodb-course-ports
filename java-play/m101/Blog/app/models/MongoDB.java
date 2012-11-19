package models;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import play.Logger;

public class MongoDB 
{
    private static Mongo mongo = null;

    private MongoDB() {
    }

    public static void connect() {
        Logger.info("Connecting to mongo db...");
        try {
            // connect to mongoDB, default host and port 
            mongo = new Mongo();

        } catch (UnknownHostException e) {
            throw new RuntimeException("Invalid mongod host and port.", e);
        }
    }

    public static Mongo connection() {
        if (mongo == null) {
            connect();
        }
        return mongo;
    }

    public static DBCollection getCollection(String name) {
        return connection().getDB("blog").getCollection(name);
    }
}

