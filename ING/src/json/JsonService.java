package json;

import java.util.HashMap;
import java.util.Map;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

public class JsonService {
	
	public static 	JSONRPC2Request composeTransactionRequest(String sourceAccountNumber, String destinationAccountNumber,
			String destinationAccountHolderName, String description, double transactionAmount, String cookie) {
		// TODO change id
		String id = "req-001";
			
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sourceAccountNumber", sourceAccountNumber);
		params.put("destinationAccountNumber", destinationAccountNumber);
		params.put("destinationAccountHolderName", destinationAccountHolderName);
		params.put("description", description);
		params.put("transactionAmount", transactionAmount);
		params.put("cookie", cookie);
		return new JSONRPC2Request("TransactionRequest", params, id);
	}
	
	public static 	JSONRPC2Request composeCustomerCreationRequest(String initials, String name, String surname, String email,
			String telephoneNumber, String address, String dob, int ssn, String username, String password) {
		// TODO change id
		String id = "req-001";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("initials", initials);
		params.put("name", name);
		params.put("surname", surname);
		params.put("email", email);
		params.put("telephoneNumber", telephoneNumber);
		params.put("address", address);
		params.put("dob", dob);
		params.put("ssn", ssn);
		params.put("username", username);
		params.put("password", password);
		return new JSONRPC2Request("CustomerCreationRequest", params, id);
	}
	
	public static 	JSONRPC2Request composeDataRequest(String accountNumber, String type, String cookie) {
		// TODO change id
		String id = "req-001";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("accountNumber", accountNumber);
		params.put("type", type);
		params.put("cookie", cookie);
		return new JSONRPC2Request("DataRequest", params, id);
	}
	
	public static 	JSONRPC2Request composePinTransactionRequest(String sourceAccountNumber, String destinationAccountNumber,
			String destinationAccountHolderName, String pinCode, String cardNumber, double transactionAmount) {
		// TODO change id
		String id = "req-001";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sourceAccountNumber", sourceAccountNumber);
		params.put("destinationAccountNumber", destinationAccountNumber);
		params.put("destinationAccountHolderName", destinationAccountHolderName);
		params.put("pinCode", pinCode);
		params.put("cardNumber", cardNumber);
		params.put("transactionAmount", transactionAmount);
		return new JSONRPC2Request("PinTransactionRequest", params, id);
	}
}