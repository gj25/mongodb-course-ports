

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hw12 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Mongo m = new Mongo();
            DB db = m.getDB("m101");
            DBCollection coll = db.getCollection("funnynumbers");
            DBCursor cursor = coll.find();

            double magic = 0;

            try {
                while (cursor.hasNext()) {
                    DBObject doc = cursor.next();
                    Double value = (Double) doc.get("value");
                    if (value % 3 == 0) {
                        magic = magic + value;
                    }
                }
            } finally {
                cursor.close();
            }

            System.out.println("The answer to Homework One, Problem 2 is " + magic);

        } catch (UnknownHostException ex) {
            Logger.getLogger(Hw12.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
