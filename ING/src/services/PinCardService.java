package services;

import java.sql.ResultSet;
import java.sql.SQLException;

import modules.exceptions.InvalidParamValueException;
import sql.actors.SQLAccessPermissionService;
import sql.actors.SQLBankAccountService;
import sql.actors.SQLCustomerService;
import sql.actors.SQLPinCardService;
import sql.exceptions.SQLLayerException;

public class PinCardService {

	// ADDING

	/**
	 * Adding a pin card for an existing access permission.
	 * 
	 * @param customerID
	 *            the card owner
	 * @param IBAN
	 *            the bank account
	 * @throws InvalidParamValueException
	 *             if customerID or IBAN is invalid, or the customer has no
	 *             access permission to this account
	 */
	public static void addPinCard(int customerID, String IBAN) throws InvalidParamValueException {
		try {
			if (customerID <= 0 || IBAN == null) {
				throw new InvalidParamValueException("Values cannot be 0 or null.");
			}

			/**
			 * Check IBAN
			 */

			if (!SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				throw new InvalidParamValueException("IBAN is invalid. ");
			}

			/**
			 * Check customer
			 */

			if (!SQLCustomerService.isCustomerByID(customerID)) {
				throw new InvalidParamValueException("CustomerID is invalid");
			}

			/**
			 * Check permissions
			 */

			if (!SQLAccessPermissionService.hasPermission(customerID, IBAN)) {
				throw new InvalidParamValueException("Customer has no access permission!");
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}

		SQLPinCardService.addPinCard(customerID, IBAN);
	}
	
	// GETTING
	
	public static ResultSet isCorrectPin(int pinCardID, int pin) throws InvalidParamValueException {
		try {
			if (SQLPinCardService.isCorrectPin(pinCardID, pin).next()) {
				return SQLPinCardService.isCorrectPin(pinCardID, pin);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("Pin is invalid.");
	}
	
	public static ResultSet getPinCardByID(int pinCardID) throws InvalidParamValueException {
		try {
			if (SQLPinCardService.isPinCard(pinCardID)) {
				return SQLPinCardService.getPinCardByPinCardID(pinCardID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("PinCard ID is invalid.");
	}

	// REMOVING

	/**
	 * Removing a pin card of a customer for a bank account.
	 * 
	 * @param customerID
	 *            the card owner
	 * @param IBAN
	 *            the bank account
	 * @throws InvalidParamValueException
	 *             if customerID or IBAN is invalid.
	 */
	public static void removePinCard(int customerID, String IBAN) throws InvalidParamValueException {
		try {
			if (!SQLCustomerService.isCustomerByID(customerID)) {
				throw new InvalidParamValueException("CustomerID is invalid. ");
			}
			if (!SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				throw new InvalidParamValueException("IBAN is invalid. ");
			}
			SQLPinCardService.removePinCard(customerID, IBAN);
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}
}
