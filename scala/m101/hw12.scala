// @author: Victor Igbokwe
import com.mongodb._

object hw12 extends App {
  val mongo = new Mongo
  val db = mongo.getDB("m101") // create a connection to the database
  // retrieve/create a reference to the "funnynumbers" collection.
  val coll = db.getCollection("funnynumbers")

  /*
   * A recursive function to make a sum of some of the values in the "funnynumbers" collection.
   * This function is done this way to minimize/eliminate side-effects as Scala encourages.
   */
  def traverseCollection(cursor: DBCursor, accumulator: Double): Double = {
    // a control statement: find out if there is more data to process.
    if (!cursor.hasNext) accumulator
    else {
      // retrieve the next value.
      val doc = cursor.next
      // make sure the document has a value called "value".
      if (doc.containsField("value")) {
        // convert the retrieved value to a double.
        val item = doc.get("value").asInstanceOf[Double]
        traverseCollection(cursor, accumulator + (if (item % 3 == 0) item else 0))
      } else traverseCollection(cursor, accumulator)
    }
  }

  // retrieve the magic value by calling the recursive function
  // with the result of calling find on the collection.
  val magic = traverseCollection(coll.find(), 0)
  // close the database.
  mongo.close
  println("The answer to Homework One, Problem 2 is " + magic.asInstanceOf[Int])
}
