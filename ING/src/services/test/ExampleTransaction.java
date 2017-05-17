package services.test;

import modules.exceptions.InvalidParamValueException;
import services.TransactionService;

public class ExampleTransaction {
	public static void main(String[] args) {
		try {
			TransactionService.transfer("NL13370000000001", "NL13370000000003", 10.5, "Testing", "Karl-Heinz");
			System.out.println("Executed example transaction.");
		} catch (InvalidParamValueException e) {
			e.printStackTrace();
		}
	}
}
