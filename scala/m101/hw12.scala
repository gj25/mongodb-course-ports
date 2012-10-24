// @author: Victor Igbokwe
import com.mongodb.casbah.Imports._

object hw12 extends App {
  val coll = MongoConnection(/*host*/)(/*db*/"m101")(/*collection*/"funnynumbers")

  val entries = coll.find()

  // TODO I don't know what happens if there are billions of records, hopefully casbah is smart enough to make maps and filter lazy
  // If it's true, data will be read from data base only during invocation of '.sum'
  // Otherwise data first will be all retrieved, then mapped, then mapped once more, 
  // then filtered and only then summed, which may lead to OutOfMemoryError
  // This is surelly a thing to verify. Anyway, syntax is concise and sexy
  val sum = entries
    .map(_.getAs[Double]("value")) // Map cursor to list (hopefully lazy) of Option[Double], where None means record doesn't have value assigned
    .flatMap(_.toList) // just an idiom to filter out None's and map from list of Some's to list of actual objects
    .filter(_ % 3 == 0) // filter out numbers which are not funny
    .sum // well.. just a sum

  println("The answer to Homework One, Problem 2 is " + sum.asInstanceOf[Int])
}
