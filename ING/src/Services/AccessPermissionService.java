package Services;

import SQL.SQLAccessPermissionService;
import SQL.SQLBankAccountService;
import SQL.SQLCustomerService;

public class AccessPermissionService {
	public static boolean addPermission(int customerID, String IBAN) throws InvalidParameterException {
		
		/**
		 * Check Parameters
		 */
		
		if (customerID <= 0 || IBAN == null) {
			throw new InvalidParameterException("String cannot be null, int cannot be 0");
		}

		/**
		 * Check customer
		 */

		if (!SQLCustomerService.isCustomerByID(customerID)) {
			throw new InvalidParameterException("CustomerID not valid. ");
		}

		/**
		 * Check IBAN
		 */

		if (!SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
			throw new InvalidParameterException("IBAN not valid. ");
		}

		// All correct

		return SQLAccessPermissionService.addPermission(customerID, IBAN);
	}
}
