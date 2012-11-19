import com.mongodb.*;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.types.ObjectId;

public class Hw31 {

    public static void main(String[] args) {
        try {
            Mongo mongo = new Mongo();
            DBCollection students = mongo.getDB("school").getCollection("students");
            DBCursor cursor = students.find();
            try {
                while(cursor.hasNext())
                {
                    DBObject student = cursor.next();
                    List<DBObject> scores = (List<DBObject>) student.get("scores");
                    double minscore = 100000;
                    for(DBObject scoreDb: scores)
                    {
                        String type = (String) scoreDb.get("type");
                        if (type.equals("homework"))
                        {
                            double score = (Double) scoreDb.get("score");
                            if (score < minscore)
                            {
                                minscore = score;
                            }
                        }
                    }
                    System.out.println("Removing score " + minscore + " from student " + student.get("name"));
                    
                    Integer id = (Integer) student.get("_id");
                    DBObject updateObj = new BasicDBObject().append("$pull",
                              new BasicDBObject("scores",new BasicDBObject("score",minscore))); 
                    students.update(new BasicDBObject("_id", id), updateObj);
                }
            } finally {
                cursor.close();
            }
        }
        catch (UnknownHostException ex) {
            Logger.getLogger(Hw31.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
