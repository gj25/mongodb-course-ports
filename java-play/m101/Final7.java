import com.mongodb.*;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Final7 {

    public static void main(String[] args) {
        try {
            Mongo mongo = new Mongo();
            DBCollection images = mongo.getDB("m101").getCollection("images");
            DBCollection albums = mongo.getDB("m101").getCollection("albums");
            
            DBCursor cursor = images.find();
            long totalId = 0;
            int left = 0;
            try {
                while(cursor.hasNext())
                {
                    DBObject image = cursor.next();
                    Integer _id = (Integer) image.get("_id");
                    
                    DBObject query = new BasicDBObject();
                    query.put("images", _id);
                    
                    long found = albums.count(query);
                    
                    
                    if(found == 0)
                    {
                        DBObject rQuery = new BasicDBObject();
                        rQuery.put("_id", _id);
                        images.remove(rQuery, WriteConcern.SAFE);
                        System.out.println("Removing image " + _id );
                    }
                    else
                    {
                        left++;
                        totalId = totalId + _id;
                    }
                }
                System.out.println("Images left: " + left + ", total id:" + totalId);
                
            } finally {
                cursor.close();
            }
        }
        catch (UnknownHostException ex) {
            Logger.getLogger(Hw31.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
