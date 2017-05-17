package json;

import java.util.HashMap;
import java.util.Map;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

public class JsonService {
	
	public static void main(String[] args) {
		System.out.println(composeOpenAccount("sdf", "dgyrrgd",
				"Peter", "Überweisung", 100, "Keks", "desd", "sfd", "sfe", "sfeaw"));
	}
	
	public static int id() {
		return (int) (Math.random() * 10000);
	}
	
	public static 	JSONRPC2Request composeOpenAccount(String name, String surname,
			String initials, String dob, int ssn, String address, String telephoneNumber, String email, String username, String password) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("name", name);
		params.put("surname", surname);
		params.put("initials", initials);
		params.put("dob", dob);
		params.put("ssn", ssn);
		params.put("address", address);
		params.put("telephoneNumber", telephoneNumber);
		params.put("email", email);
		params.put("username", username);
		params.put("password", password);
		
		return new JSONRPC2Request("openAccount", params, id());
	}
	
	public static 	JSONRPC2Request composeOpenAdditionalAccount(String iBAN, int pinCard, int pinCode) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("iBAN", iBAN);
		params.put("pinCard", pinCard);
		params.put("pinCode", pinCode);
		
		return new JSONRPC2Request("openAdditionalAccount", params, id());
	}

	

	public static 	JSONRPC2Request composeCloseAccount(String authToken, String iBAN) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("authToken", authToken);
		params.put("iBAN", iBAN);
		
		return new JSONRPC2Request("closeAccount", params, id());
	}

	public static 	JSONRPC2Request composeProvideAccess(String authToken, String iBAN, String username) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("authToken", authToken);
		params.put("iBAN", iBAN);
		params.put("username", username);
		
		return new JSONRPC2Request("provideAccess", params, id());
	}

	public static 	JSONRPC2Request composeRevokeAccess(String authToken, String iBAN, String username) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("authToken", authToken);
		params.put("iBAN", iBAN);
		params.put("username", username);
		
		return new JSONRPC2Request("revokeAccess", params, id());
	}

	public static 	JSONRPC2Request composeDepositIntoAccount(String iBAN, int pinCard, int pinCode, double amount) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("iBAN", iBAN);
		params.put("pinCard", pinCard);
		params.put("pinCode", pinCode);
		params.put("amount", amount);
		
		return new JSONRPC2Request("depositIntoAccount", params, id());
	}

	public static 	JSONRPC2Request composePayFromAccount(String sourceIBAN, String targetIBAN, int pinCard, int pinCode, double amount) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("sourceIBAN", sourceIBAN);
		params.put("targetIBAN", targetIBAN);
		params.put("pinCard", pinCard);
		params.put("pinCode", pinCode);
		params.put("amount", amount);
		
		return new JSONRPC2Request("payFromAccount", params, id());
	}

	public static 	JSONRPC2Request composeTransferMoney(String authToken, String sourceIBAN, String targetIBAN, String targetName, double amount, String description) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("authToken", authToken);
		params.put("sourceIBAN", sourceIBAN);
		params.put("targetIBAN", targetIBAN);
		params.put("targetName", targetName);
		params.put("amount", amount);
		params.put("description", description);
		
		return new JSONRPC2Request("transferMoney", params, id());
	}

	public static 	JSONRPC2Request composeGetAuthToken(String username, String password) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("username", username);
		params.put("password", password);
		
		return new JSONRPC2Request("getAuthToken", params, id());
	}

	public static 	JSONRPC2Request composeGetBalance(String authToken, String iBAN) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("authToken", authToken);
		params.put("iBAN", iBAN);
		
		return new JSONRPC2Request("getBalance", params, id());
	}

	public static 	JSONRPC2Request composeGetTransactionsOverview(String authToken, String iBAN, int nrOfTransactions) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("authToken", authToken);
		params.put("iBAN", iBAN);
		params.put("nrOfTransactions", nrOfTransactions);
		
		return new JSONRPC2Request("getTransactionsOverview", params, id());
	}

	public static 	JSONRPC2Request composeGetUserAccess(String authToken, String username) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("authToken", authToken);
		params.put("username", username);
		
		return new JSONRPC2Request("getUserAccess", params, id());
	}

	public static 	JSONRPC2Request composeGetBankAccountAccess(String authToken, String iBAN) {		
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("authToken", authToken);
		params.put("iBAN", iBAN);
		
		return new JSONRPC2Request("getBankAccountAccess", params, id());
	}
	
}