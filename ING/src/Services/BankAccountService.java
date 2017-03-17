package Services;

import SQL.SQLBankAccountService;
import SQL.SQLCustomerService;

public class BankAccountService {

	public static boolean addBankAccount(int mainCustomer, double startsaldo) throws InvalidParameterException {
		/**
		 * Check main customer
		 */
		
		if (!SQLCustomerService.isCustomerByID(mainCustomer)) {
			throw new InvalidParameterException("mainCustomer not present. ");
		}

		return SQLBankAccountService.addBankAccount(mainCustomer, startsaldo);
	}

	public static boolean addBankAccount(int mainCustomer) throws InvalidParameterException {
		return addBankAccount(mainCustomer, 0);
	}
}
