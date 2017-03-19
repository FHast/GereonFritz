package services;

import services.exceptions.InvalidParameterException;
import sql.exceptions.SQLLayerException;
import sql.services.SQLAccessPermissionService;
import sql.services.SQLBankAccountService;
import sql.services.SQLCustomerService;

public class AccessPermissionService {

	// ADDING

	/**
	 * Adds a permission for a customer to access a bank account.
	 * 
	 * @param customerID
	 *            the customer who gains access.
	 * @param IBAN
	 *            The account which is gained access to.
	 * @throws InvalidParameterException
	 *             if customerID or IBAN is not valid.
	 */
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

		try {
			if (!SQLCustomerService.isCustomerByID(customerID)) {
				throw new InvalidParameterException("CustomerID is invalid. ");
			}
		} catch (SQLLayerException e1) {
			e1.printStackTrace();
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

		// All correct

		try {
			SQLAccessPermissionService.addPermission(customerID, IBAN);
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}

	// REMOVING

	/**
	 * Removes all permissions of this customer.
	 * 
	 * @param customerID
	 *            the customer who loses all permissions.
	 * @throws InvalidParameterException
	 */
	public static void removePermissions(int customerID) throws InvalidParameterException {
		try {
			if (SQLCustomerService.isCustomerByID(customerID)) {
				SQLAccessPermissionService.removePermissions(customerID);
			} else {
				throw new InvalidParameterException("CustomerID is invalid. ");
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes all permissions connected to this bank account.
	 * 
	 * @param IBAN
	 *            The bank account
	 * @throws InvalidParameterException
	 */
	public static void removePermissions(String IBAN) throws InvalidParameterException {
		try {
			if (SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				SQLAccessPermissionService.removePermissions(IBAN);
			} else {
				throw new InvalidParameterException("IBAN is invalid. ");
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes permission for a customer to access a bank account.
	 * 
	 * @param customerID
	 *            the customer who loses access.
	 * @param IBAN
	 *            the bank account
	 * @throws InvalidParameterException
	 */
	public static void removePermission(int customerID, String IBAN) throws InvalidParameterException {
		try {
			if (!SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				throw new InvalidParameterException("IBAN is invalid. ");
			}
			if (!SQLCustomerService.isCustomerByID(customerID)) {
				throw new InvalidParameterException("CustomerID is invalid. ");
			}
			SQLAccessPermissionService.removePermission(customerID, IBAN);
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}
}
