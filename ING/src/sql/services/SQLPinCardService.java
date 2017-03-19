package sql.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import sql.SQLExecute;
import sql.exceptions.InvalidParameterTypeException;
import sql.exceptions.SQLLayerException;

public class SQLPinCardService {

	private static final int VALID_FOR_YEARS = 4;

	// ADDING

	/**
	 * Adds a pin card to the database. Creates the 'valid until' time stamp.
	 * 
	 * @param customerID
	 *            the customer
	 * @param IBAN
	 *            the bank account
	 */
	public static void addPinCard(int customerID, String IBAN) {
		try {
			ResultSet bankacc = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?",
					new Object[] { IBAN });

			LocalDateTime currentTime = LocalDateTime.now();
			String date = currentTime.toLocalDate().plusYears(VALID_FOR_YEARS).toString();
			String pin = "" + (int) (Math.random() * 10) + "" + (int) (Math.random() * 10) + ""
					+ (int) (Math.random() * 10) + "" + (int) (Math.random() * 10);

			SQLExecute.execute("INSERT INTO PinCards VALUES(?,?,?,?,?)",
					new Object[] { null, date, pin, customerID, bankacc.getInt(1) });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// REMOVING

	/**
	 * Removes all pin cards of this customer from the database.
	 * 
	 * @param customerID
	 *            the customer
	 */
	public static void removePinCards(int customerID) {
		try {
			SQLExecute.execute("DELETE FROM PinCards WHERE CustomerID = ?", new Object[] { customerID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes all pin cards connected to this bank account.
	 * 
	 * @param IBAN
	 *            the bank account
	 * @throws SQLLayerException
	 */
	public static void removePinCards(String IBAN) throws SQLLayerException {
		int ID = SQLBankAccountService.getIDforIBAN(IBAN);
		try {
			SQLExecute.execute("DELETE FROM PinCards WHERE BankAccountID = ?", new Object[] { ID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes the pin card of a customer connected to a given bank account.
	 * 
	 * @param customerID
	 *            the customer
	 * @param IBAN
	 *            the bank account
	 * @throws SQLLayerException
	 */
	public static void removePinCard(int customerID, String IBAN) throws SQLLayerException {
		int ID = SQLBankAccountService.getIDforIBAN(IBAN);
		try {
			SQLExecute.execute("DELETE FROM PinCards WHERE BankAccountID = ? AND CustomerID = ?",
					new Object[] { ID, customerID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
