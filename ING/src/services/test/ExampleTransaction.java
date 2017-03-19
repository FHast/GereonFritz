package services.test;

import services.TransactionService;
import services.exceptions.InvalidParameterException;

public class ExampleTransaction {
	public static void main(String[] args) {
		try {
			TransactionService.transfer("NL13370000000001", "NL13370000000003", 10.5, "Testing", "Karl-Heinz");
			System.out.println("Executed example transaction.");
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}
}
