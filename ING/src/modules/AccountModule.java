package modules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import modules.exceptions.InvalidParamValueException;
import modules.exceptions.InvalidParamsException;
import modules.exceptions.NotAuthenticatedException;
import modules.exceptions.OtherRpcException;
import modules.security.HashService;
import sql.actors.SQLBankAccountService;
import sql.actors.SQLCustomerService;
import sql.actors.SQLPinCardService;
import sql.exceptions.SQLLayerException;

public class AccountModule {
	public static Map<String, Object> openAccount(Map<String, Object> params)
			throws InvalidParamsException, InvalidParamValueException, OtherRpcException {
		// Check params
		if (params == null || params.size() != 10) {
			throw new InvalidParamsException("Either no or not enough params given.");
		}

		/**
		 * Check name: not empty
		 */
		String name = (String) params.get("name");
		if (name == null || name.equals("")) {
			throw new InvalidParamValueException("Name must not be empty or null");
		}

		/**
		 * Check surname: not empty
		 */
		String surname = (String) params.get("surname");
		if (surname == null || surname.equals("")) {
			throw new InvalidParamValueException("Name must not be empty or null");
		}

		/**
		 * Initials
		 */
		String initials = (String) params.get("initials");
		if (initials == null || initials.equals("")) {
			throw new InvalidParamValueException("Name must not be empty or null");
		}

		/**
		 * Check DOB: Format DD.MM.YYYY ; Year 1917 - 2017 ; Month 1 - 12 ; Day
		 * 1 - 31
		 */
		String DOB = (String) params.get("dob");
		if (DOB == null || DOB.equals("")) {
			throw new InvalidParamValueException("Name must not be empty or null");
		}
		String[] dob = DOB.split("[.]");
		if (dob.length != 3) {
			throw new InvalidParamValueException("DOB must be of form DD.MM.YYYY ");
		} else if (Integer.parseInt(dob[2]) > 2017 || Integer.parseInt(dob[2]) < 1917) {
			throw new InvalidParamValueException("DOB Year is invalid, must be between 1917 and 2017");
		} else if (Integer.parseInt(dob[1]) > 12 || Integer.parseInt(dob[1]) < 1) {
			throw new InvalidParamValueException("DOB Month is invalid, must be between 1 and 12");
		} else if (Integer.parseInt(dob[0]) > 31 || Integer.parseInt(dob[1]) < 1) {
			// maybe check for the different month?
			throw new InvalidParamValueException("DOB Day is invalid, must be between 1 and 31");
		}

		/**
		 * Check BSN: not <= 0
		 */
		String bsn = (String) params.get("ssn");
		if (bsn == null) {
			throw new InvalidParamValueException("bsn must not be null");
		}
		Long BSN;
		try {
			BSN = Long.parseLong(bsn);
			if (BSN <= 0) {
				throw new InvalidParamValueException("BSN cannot be 0 or negative");
			}
		} catch (NumberFormatException e) {
			throw new InvalidParamValueException("BSN must be an Integer");
		}

		/**
		 * Check address:
		 */
		String address = (String) params.get("address");
		if (address == null || address.equals("")) {
			throw new InvalidParamValueException("Name must not be empty or null");
		}

		/**
		 * Check Phone: not <= 0 ; at least 9 digits
		 */
		String phoneString = (String) params.get("telephoneNumber");
		if (phoneString == null) {
			throw new InvalidParamValueException("phoneString must not be null");
		}
		Long phone;
		try {
			phone = Long.parseLong(phoneString);
			if (phone <= 0) {
				throw new InvalidParamValueException("phone number cannot be 0 or negative");
			} else if (phone < 1000000) {
				throw new InvalidParamValueException("phone number too short");
			}
		} catch (NumberFormatException e) {
			throw new InvalidParamValueException("Phone number must be an Integer");
		}

		/**
		 * Check Email: contains @ ; contains .
		 */
		String email = (String) params.get("email");
		if (email == null || email.equals("")) {
			throw new InvalidParamValueException("Name must not be empty or null");
		}
		if (!email.contains("@")) {
			throw new InvalidParamValueException("Email is invalid, must contain @");
		} else if (!email.contains(".")) {
			throw new InvalidParamValueException("Email seems invalid, must contain '.'");
		}

		/**
		 * Check username: not empty
		 */
		String username = (String) params.get("username");
		if (username == null || username.equals("")) {
			throw new InvalidParamValueException("Name must not be empty or null");
		}

		/**
		 * Check password: not empty
		 */
		String password = (String) params.get("password");
		if (password == null || password.equals("")) {
			throw new InvalidParamValueException("Name must not be empty or null");
		}
		params.put("password", HashService.hash(password));

		// Add to database
		try {
			SQLCustomerService.addCustomer(params);
			int custID = SQLCustomerService.getCustomerByEmail(email).getInt(1);
			String iban = SQLBankAccountService.addBankAccount(custID, 0);
			ResultSet pincard = SQLPinCardService.getPinCardByIBAN(iban);
			int cardID = pincard.getInt(1);
			int cardPin = pincard.getInt(3);
			Map<String, Object> res = new HashMap<>();
			res.put("iBAN", iban);
			res.put("pinCard", cardID);
			res.put("cardPin", cardPin);
			return res;
		} catch (SQLLayerException | SQLException e) {
			throw new OtherRpcException("SQLlayerException");
		}
	}

	public static Map<String, Object> openAdditionalAccount(Map<String, Object> params)
			throws InvalidParamsException, NotAuthenticatedException, OtherRpcException {
		if (params == null || params.size() != 1) {
			throw new InvalidParamsException("Either no or not enough params given.");
		}

		/**
		 * Check token
		 */
		String token = (String) params.get("authToken");
		String username = AuthenticationModule.checkToken(token);

		try {
			ResultSet cust = SQLCustomerService.getCustomerByUsername(username);
			int custID = cust.getInt(cust.findColumn("CustomerID"));
			String iban = SQLBankAccountService.addBankAccount(custID, 0);
			ResultSet pincard = SQLPinCardService.getPinCardByIBAN(iban);
			int cardID = pincard.getInt(1);
			int cardPin = pincard.getInt(3);
			Map<String, Object> res = new HashMap<>();
			res.put("iBAN", iban);
			res.put("pinCard", cardID);
			res.put("cardPin", cardPin);
			return res;
		} catch (SQLLayerException | SQLException e) {
			throw new OtherRpcException("SQLlayerException");
		}
	}

	public static boolean closeAccount(Map<String, Object> params)
			throws NotAuthenticatedException, InvalidParamsException, OtherRpcException, InvalidParamValueException {
		if (params == null || params.size() != 1) {
			throw new InvalidParamsException("Either no or not enough params given.");
		}

		/**
		 * Check token
		 */
		String token = (String) params.get("authToken");
		String username = AuthenticationModule.checkToken(token);

		/**
		 * Check IBAN
		 */
		String iban = (String) params.get("iBAN");
		if (username == null || username.equals("")) {
			throw new InvalidParamValueException("Iban must not be empty or null");
		}

		try {
			ResultSet cust = SQLCustomerService.getCustomerByUsername(username);
			int custID = cust.getInt(cust.findColumn("CustomerID"));
			SQLBankAccountService.removeBankAccount(iban); // TODO not empty
															// accounts

			// Check customer

			if (!SQLBankAccountService.hasBankAccounts(custID)) {
				SQLCustomerService.removeCustomer(custID);
			}

			return true;
		} catch (SQLLayerException | SQLException e) {
			throw new OtherRpcException("SQLlayerException");
		}

	}
}
