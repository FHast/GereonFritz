package sql.actors;

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
			LocalDateTime currentTime = LocalDateTime.now();
			String date = currentTime.toLocalDate().plusYears(VALID_FOR_YEARS).toString();
			String pin = "" + (int) (Math.random() * 10) + "" + (int) (Math.random() * 10) + ""
					+ (int) (Math.random() * 10) + "" + (int) (Math.random() * 10);

			SQLExecute.execute("INSERT INTO PinCards VALUES(?,?,?,?,?)",
					new Object[] { null, date, pin, customerID, IBAN });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// GETTING

	/**
	 * Returns the PinCard which has the given PinCard Id.
	 * 
	 * @param pinCardID
	 *            the PinCard Id
	 * @return ResultSet containing the PinCard information
	 * @throws SQLLayerException
	 */
	public static ResultSet getPinCardByPinCardID(int pinCardID) throws SQLLayerException {
		try {
			ResultSet pinCard = SQLExecute.executeQuery("SELECT * FROM PinCards WHERE PinCardID = ?",
					new Object[] { pinCardID });
			return pinCard;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	public static ResultSet getPinCardByIBAN(String iban) throws SQLLayerException {
		try {
			ResultSet pinCard = SQLExecute.executeQuery("SELECT * FROM PinCards WHERE iban = ?", new Object[] { iban });
			return pinCard;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	/**
	 * Checks whether this PinCard Id is valid or not.
	 * 
	 * @param pinCardID
	 *            the PinCard Id
	 * @return true if a PinCard exists with this PinCardID, otherwise false
	 * @throws SQLLayerException
	 */
	public static boolean isPinCard(int pinCardID) throws SQLLayerException {
		try {
			ResultSet res = getPinCardByPinCardID(pinCardID);
			if (!res.next()) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	/**
	 * Returns the PinCard which if the Pin is correct.
	 * 
	 * @param pin
	 *            the Pin
	 * @return ResultSet containing the PinCard information
	 * @throws SQLLayerException
	 */
	public static boolean isCorrectPin(int pinCardID, int pin) throws SQLLayerException { // TODO sql injection
		try {
			ResultSet pinCard = SQLExecute.executeQuery("SELECT * FROM PinCards WHERE PinCardID = ? AND PIN = ?",
					new Object[] { pinCardID, pin });
			return pinCard.next();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
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
		try {
			SQLExecute.execute("DELETE FROM PinCards WHERE IBAN = ?", new Object[] { IBAN });
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
		try {
			SQLExecute.execute("DELETE FROM PinCards WHERE IBAN = ? AND CustomerID = ?",
					new Object[] { IBAN, customerID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
