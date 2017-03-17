package Services;

import SQL.SQLAccessPermissionService;
import SQL.SQLBankAccountService;
import SQL.SQLCustomerService;
import SQL.SQLLayerException;
import SQL.SQLPinCardService;

public class PinCardService {
	public static void addPinCard(int customerID, String IBAN) throws InvalidParameterException {
		if (customerID <= 0 || IBAN == null) {
			throw new InvalidParameterException("Values cannot be 0 or null.");
		}

		/**
		 * Check IBAN
		 */

		try {
			if (!SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				throw new InvalidParameterException("IBAN is invalid. ");
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}

		/**
		 * Check customer
		 */

		if (!SQLCustomerService.isCustomerByID(customerID)) {
			throw new InvalidParameterException("CustomerID is invalid");
		}

		/**
		 * Check permissions
		 */

		try {
			if (!SQLAccessPermissionService.hasPermission(customerID, IBAN)) {
				throw new InvalidParameterException("Customer has no access permission!");
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}

		SQLPinCardService.addPinCard(customerID, IBAN);
	}
}
