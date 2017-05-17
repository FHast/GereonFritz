package services;

import java.sql.ResultSet;

import modules.exceptions.InvalidParamValueException;
import sql.actors.SQLAccessPermissionService;
import sql.actors.SQLBankAccountService;
import sql.actors.SQLCustomerService;
import sql.exceptions.SQLLayerException;

public class AccessPermissionService {

	// ADDING

	/**
	 * Adds a permission for a customer to access a bank account.
	 * 
	 * @param customerID
	 *            the customer who gains access.
	 * @param IBAN
	 *            The account which is gained access to.
	 * @throws InvalidParamValueException
	 *             if customerID or IBAN is not valid.
	 */
	public static void addPermission(int customerID, String IBAN) throws InvalidParamValueException {

		/**
		 * Check Parameters
		 */

		if (customerID <= 0 || IBAN == null) {
			throw new InvalidParamValueException("String cannot be null, int cannot be 0");
		}

		/**
		 * Check customer
		 */

		try {
			if (!SQLCustomerService.isCustomerByID(customerID)) {
				throw new InvalidParamValueException("CustomerID is invalid. ");
			}
		} catch (SQLLayerException e1) {
			e1.printStackTrace();
		}

		/**
		 * Check IBAN
		 */

		try {
			if (!SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				throw new InvalidParamValueException("IBAN is invalid. ");
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
	
	// GETTING
	
	public static ResultSet getPermissionsByCustomer(int ID) throws InvalidParamValueException {
		try {
			if (SQLCustomerService.isCustomerByID(ID)) {
				return SQLAccessPermissionService.getPermissions(ID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("Customer ID is invalid.");
	}
	
	public static ResultSet getPermissionsByIBAN(String IBAN) throws InvalidParamValueException {
		try {
			if (SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				return SQLAccessPermissionService.getPermissionsByIBAN(IBAN);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("IBAN is invalid.");
	}

	// REMOVING

	/**
	 * Removes all permissions of this customer.
	 * 
	 * @param customerID
	 *            the customer who loses all permissions.
	 * @throws InvalidParamValueException
	 */
	public static void removePermissions(int customerID) throws InvalidParamValueException {
		try {
			if (SQLCustomerService.isCustomerByID(customerID)) {
				SQLAccessPermissionService.removePermissions(customerID);
			} else {
				throw new InvalidParamValueException("CustomerID is invalid. ");
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
	 * @throws InvalidParamValueException
	 */
	public static void removePermissions(String IBAN) throws InvalidParamValueException {
		try {
			if (SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				SQLAccessPermissionService.removePermissions(IBAN);
			} else {
				throw new InvalidParamValueException("IBAN is invalid. ");
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
	 * @throws InvalidParamValueException
	 */
	public static void removePermission(int customerID, String IBAN) throws InvalidParamValueException {
		try {
			if (!SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				throw new InvalidParamValueException("IBAN is invalid. ");
			}
			if (!SQLCustomerService.isCustomerByID(customerID)) {
				throw new InvalidParamValueException("CustomerID is invalid. ");
			}
			SQLAccessPermissionService.removePermission(customerID, IBAN);
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}
}
