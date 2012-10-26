package hw1_2;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

public class Hw1_2 {
	public static void main(String[] args) {
		final String hostName = "localhost",
					 databaseName = "m101";
		final int port = 27017;		
		
		MongoOptions mongoOptions = new MongoOptions();
		mongoOptions.setSafe(true);
		try {
			MongoOperations mongoOperations = 
					new MongoTemplate(new Mongo(new ServerAddress(hostName, port), mongoOptions), databaseName);
			List<FunnyNumbers> funnyNumbers = mongoOperations.findAll(FunnyNumbers.class);			
			int magic = 0;
			for (FunnyNumbers item : funnyNumbers)
				if(item.getValue() % 3 == 0)
					magic += item.getValue();
			System.out.println("The answer to Homework One, Problem 2 is " + magic);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
