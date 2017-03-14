package Services.test;

import Services.InvalidParameterException;
import Services.TransactionService;

public class ExampleTransaction {
	public static void main(String[] args) {
		try {
			TransactionService.transfer("NL13370000000001", "NL13370000000003", 1000, "Testing", "Karl-Heinz");
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}
}
