
import org.scalatra._
import com.mongodb.casbah.Imports._

class HelloWorldApp extends ScalatraFilter {
  get("/hw1/:skip") {
  	val skip = params("skip").toInt 

  	val record = Mongo("m101", "funnynumbers").find()
  		.skip(skip)
  		.sort(MongoDBObject("value" -> true))
  		.next
    record.getAs[Double]("value").get
  }
}

object Mongo {
	lazy val mongo = MongoConnection()
	def apply(db: String) = mongo(db)
	def apply(db: String, collection: String) = mongo(db)(collection)
	def close = mongo.close
}
