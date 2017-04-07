package services;

import java.sql.ResultSet;
import java.sql.SQLException;

import services.exceptions.InvalidParameterException;
import sql.exceptions.SQLLayerException;
import sql.services.SQLAccessPermissionService;
import sql.services.SQLBankAccountService;
import sql.services.SQLCustomerService;
import sql.services.SQLPinCardService;

public class PinCardService {

	// ADDING

	/**
	 * Adding a pin card for an existing access permission.
	 * 
	 * @param customerID
	 *            the card owner
	 * @param IBAN
	 *            the bank account
	 * @throws InvalidParameterException
	 *             if customerID or IBAN is invalid, or the customer has no
	 *             access permission to this account
	 */
	public static void addPinCard(int customerID, String IBAN) throws InvalidParameterException {
		try {
			if (customerID <= 0 || IBAN == null) {
				throw new InvalidParameterException("Values cannot be 0 or null.");
			}

			/**
			 * Check IBAN
			 */

			if (!SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				throw new InvalidParameterException("IBAN is invalid. ");
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

			if (!SQLAccessPermissionService.hasPermission(customerID, IBAN)) {
				throw new InvalidParameterException("Customer has no access permission!");
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}

		SQLPinCardService.addPinCard(customerID, IBAN);
	}
	
	// GETTING
	
	public static ResultSet isCorrectPin(int pinCardID, int pin) throws InvalidParameterException {
		try {
			if (SQLPinCardService.isCorrectPin(pinCardID, pin).next()) {
				return SQLPinCardService.isCorrectPin(pinCardID, pin);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new InvalidParameterException("Pin is invalid.");
	}
	
	public static ResultSet getPinCardByID(int pinCardID) throws InvalidParameterException {
		try {
			if (SQLPinCardService.isPinCardByPinCardID(pinCardID)) {
				return SQLPinCardService.getPinCardByPinCardID(pinCardID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParameterException("PinCard ID is invalid.");
	}

	// REMOVING

	/**
	 * Removing a pin card of a customer for a bank account.
	 * 
	 * @param customerID
	 *            the card owner
	 * @param IBAN
	 *            the bank account
	 * @throws InvalidParameterException
	 *             if customerID or IBAN is invalid.
	 */
	public static void removePinCard(int customerID, String IBAN) throws InvalidParameterException {
		try {
			if (!SQLCustomerService.isCustomerByID(customerID)) {
				throw new InvalidParameterException("CustomerID is invalid. ");
			}
			if (!SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				throw new InvalidParameterException("IBAN is invalid. ");
			}
			SQLPinCardService.removePinCard(customerID, IBAN);
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}
}
