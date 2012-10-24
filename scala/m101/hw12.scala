// @author: Victor Igbokwe
import com.mongodb.casbah.Imports._

object hw12 extends App {
  var mongo = MongoConnection(/*host*/)
  val coll = dbHost(/*db*/"m101")(/*collection*/"funnynumbers")

  val entries = coll.find()

  // We shouldn't be afraid of filtering here. In fact, entries is an Iterator, and when we apply
  // map or filter, it doesn't do filtering - instead it returns new Iterator, which decorates first one
  // Data are read from DB only when we really do need them - i.e., if we call .foreach or, in our case, if we call .sum
  // You can verify it by extracting variable below which has filtered numbers, closing dbHost and trying to execute numbers.sum
  // It will throw an exception - as we have no data on a client yet, only a filtered and mapped iterator
  val sum = entries
    .map(_.getAs[Double]("value")) // Map cursor to list (hopefully lazy) of Option[Double], where None means record doesn't have value assigned
    .flatMap(_.toList) // just an idiom to filter out None's and map from list of Some's to list of actual objects
    .filter(_ % 3 == 0) // filter out numbers which are not funny
    .sum // well.. just a sum
  mongo.close
  
  println("The answer to Homework One, Problem 2 is " + sum.asInstanceOf[Int])
}
