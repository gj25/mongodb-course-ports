package hw1_2;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "funnynumbers")
public class FunnyNumbers {
	private int value;

	public int getValue() { return value; }
	public void setValue(int value) { this.value = value; }	
}
