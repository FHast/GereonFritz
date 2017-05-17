package services;

import java.sql.ResultSet;

import modules.exceptions.InvalidParamValueException;
import services.exceptions.BankLogicException;
import sql.actors.SQLCustomerService;
import sql.exceptions.SQLLayerException;

public class CustomerService {

	// ADDING

	/**
	 * Checks for valid input, then invokes the actual SQL Service method.
	 * 
	 * @param name
	 * @param surname
	 * @param DOB
	 *            date of birth
	 * @param BSN
	 *            "Bürgerservicenummer"
	 * @param address
	 * @param phone
	 * @param email
	 * @throws InvalidParamValueException
	 */
	public static void addCustomer(String name, String surname, String DOB, int BSN, String address, int phone,
			String email) throws InvalidParamValueException {

		if (name == null || surname == null || DOB == null || address == null || email == null || BSN == 0
				|| phone == 0) {
			throw new InvalidParamValueException("String cannot be null, int cannot be 0");
		}

		/**
		 * Check name: not empty
		 */

		if (name.equals("")) {
			throw new InvalidParamValueException("Name must not be empty");
		}

		/**
		 * Check surname: not empty
		 */

		if (surname.equals("")) {
			throw new InvalidParamValueException("surname must not be empty");
		}

		/**
		 * Check DOB: Format DD.MM.YYYY ; Year 1917 - 2017 ; Month 1 - 12 ; Day
		 * 1 - 31
		 */
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
		if (BSN <= 0) {
			throw new InvalidParamValueException("BSN cannot be 0 or negative");
		}

		/**
		 * Check address:
		 */

		// TODO

		/**
		 * Check Phone: not <= 0 ; at least 9 digits
		 */

		if (phone <= 0) {
			throw new InvalidParamValueException("phone number cannot be 0 or negative");
		} else if (phone < 100000000) {
			throw new InvalidParamValueException("phone number too short");
		}

		/**
		 * Check Email: contains @ ; contains .
		 */

		if (!email.contains("@")) {
			throw new InvalidParamValueException("Email is invalid, must contain @");
		} else if (!email.contains(".")) {
			throw new InvalidParamValueException("Email seems invalid, must contain '.'");
		}

		// Seems valid, execute query!
		//SQLCustomerService.addCustomer(name, surname, DOB, BSN, address, phone, email); // TODO

	}

	// GETTING

	public static ResultSet getCustomerByID(int ID) throws InvalidParamValueException {
		try {
			if (SQLCustomerService.isCustomerByID(ID)) {
				return SQLCustomerService.getCustomerByID(ID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("ID is invalid.");
	}
	
	public static ResultSet getCustomerByBSN(int BSN) throws InvalidParamValueException {
		try {
			if (SQLCustomerService.isCustomerByBSN(BSN)) {
				return SQLCustomerService.getCustomerByBSN(BSN);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("BSN is invalid.");
	}
	
	public static ResultSet getCustomerByEmail(String email) throws InvalidParamValueException {
		try {
			if (SQLCustomerService.isCustomerByEmail(email)) {
				return SQLCustomerService.getCustomerByEmail(email);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("BSN is invalid.");
	}

	// REMOVING

	/**
	 * Removing a customer. This leads to all bank accounts, permissions and pin
	 * cards being deleted if possible. (Cascading!)
	 * 
	 * @param customerID
	 *            the customer who will be deleted
	 * @throws InvalidParamValueException
	 *             if customerID is invalid
	 * @throws BankLogicException
	 *             if the customer has bank accounts with a saldo unequal to 0
	 */
	public static void removeCustomer(int customerID) throws InvalidParamValueException, BankLogicException {
		try {
			if (SQLCustomerService.isCustomerByID(customerID)) {
				BankAccountService.removeBankAccounts(customerID);
				SQLCustomerService.removeCustomer(customerID);
			} else {
				throw new InvalidParamValueException("Invalid customer ID: " + customerID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}
}
