package hw1_3;

import java.net.UnknownHostException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Order;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

/*
This service is accessible via: 
http://localhost:8080/hw1_3/hw1/50
*/ 

@Path("/hw1/{n}")
public class Hw1_3 {
    @GET
    @Produces("text/plain")
    public String get_hw1(@PathParam("n") int n) {
		final String hostName = "localhost",
				 databaseName = "m101";
		final int port = 27017;		
		
		MongoOptions mongoOptions = new MongoOptions();
		mongoOptions.setSafe(true);
		try {
			MongoOperations mongoOperations = new MongoTemplate(new Mongo(new ServerAddress(hostName, port), mongoOptions), databaseName);
			
			Query serchQuery = new Query();
			serchQuery.limit(1);
			serchQuery.skip(n);
			serchQuery.sort().on("value", Order.ASCENDING);
			
			List<FunnyNumbers> funnyNumbers = mongoOperations.find(serchQuery, FunnyNumbers.class);		
			for (FunnyNumbers item : funnyNumbers)
				return item.getValue() + "\n";
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "";
    }
}
