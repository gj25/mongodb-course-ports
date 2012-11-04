import com.mongodb.*;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hw22 {

    public static void main(String[] args) {
        try {
            Mongo mongo = new Mongo();
            DBCollection grades = mongo.getDB("students").getCollection("grades");
            DBObject sort = new BasicDBObject();
            sort.put("student_id", 1);
            sort.put("score", 1);
            DBCursor cursor = grades.find().sort(sort);
            int prevId = -1;
            try {
                while(cursor.hasNext())
                {
                    DBObject grade = cursor.next();
                    int newStudentId = (Integer) grade.get("student_id");
                    if (newStudentId != prevId)
                    {
                        grades.remove(grade);
                        prevId = newStudentId;
                        System.out.println("Removed: " + grade.toString());
                    }
                }
            } finally {
                cursor.close();
            }
        }
        catch (UnknownHostException ex) {
            Logger.getLogger(Hw22.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
