package Services;

import SQL.SQLAccessPermissionService;
import SQL.SQLBankAccountService;
import SQL.SQLCustomerService;
import SQL.SQLLayerException;

public class AccessPermissionService {
	public static void addPermission(int customerID, String IBAN) throws InvalidParameterException {
		
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

		try {
			if (!SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				throw new InvalidParameterException("IBAN not valid. ");
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}

		// All correct

		try {
			SQLAccessPermissionService.addPermission(customerID, IBAN);
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}
}
