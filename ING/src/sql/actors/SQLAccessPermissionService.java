package sql.actors;

import java.sql.ResultSet;
import java.sql.SQLException;

import modules.exceptions.InvalidParamValueException;
import services.PinCardService;
import sql.SQLExecute;
import sql.exceptions.InvalidParameterTypeException;
import sql.exceptions.SQLLayerException;

public class SQLAccessPermissionService {

	// ADDING

	/**
	 * Adds an access permission for a customer to a bank account into the
	 * database.
	 * 
	 * @param customerID
	 *            the customer
	 * @param IBAN
	 *            the bank account
	 * @throws SQLLayerException
	 */
	public static void addPermission(int customerID, String IBAN) throws SQLLayerException {
		try {
			int bankID = SQLBankAccountService.getIDforIBAN(IBAN);
			SQLExecute.execute("INSERT INTO AccessPermissions VALUES(?,?)", new Object[] { customerID, bankID });
			PinCardService.addPinCard(customerID, IBAN);
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParamValueException e) {
			e.printStackTrace();
		}
	}

	// GETTING

	/**
	 * Returns the ResultSet of all permission a customer has.
	 * 
	 * @param customerID
	 *            the customer
	 * @return ResultSet of all permissions connected to this customer
	 * @throws SQLLayerException
	 */
	public static ResultSet getPermissions(int customerID) throws SQLLayerException {
		try {
			return SQLExecute.executeQuery("SELECT * FROM AccessPermissions WHERE CustomerID = ?",
					new Object[] { customerID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}
	
	public static ResultSet getPermissionsByIBAN(String IBAN) throws SQLLayerException {
		int ID = SQLBankAccountService.getIDforIBAN(IBAN);
		try {
			return SQLExecute.executeQuery("SELECT * FROM AccessPermissions WHERE BankAccountID = ?",
					new Object[] { ID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	/**
	 * Checks whether a customer has got permission to a bank account.
	 * 
	 * @param customerID
	 *            the customer
	 * @param IBAN
	 *            the bank account
	 * @return true if customer has permission, otherwise false
	 * @throws SQLLayerException
	 */
	public static boolean hasPermission(int customerID, String IBAN) throws SQLLayerException {
		try {
			ResultSet permission = SQLExecute.executeQuery("SELECT * FROM AccessPermissions "
					+ "JOIN BankAccounts ON BankAccounts.BankAccountID = AccessPermissions.BankAccountID "
					+ "WHERE CustomerID = ? AND IBAN = ?", new Object[] { customerID, IBAN });
			if (!permission.next()) {
				return false;
			} else {
				return true;
			}
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	// REMOVING

	/**
	 * Removes all permissions connected to a bank account.
	 * 
	 * @param IBAN
	 *            the bank account
	 * @throws SQLLayerException
	 */
	public static void removePermissions(String IBAN) throws SQLLayerException {
		int ID = SQLBankAccountService.getIDforIBAN(IBAN);
		try {
			SQLExecute.execute("DELETE FROM AccessPermissions WHERE BankAccountID = ?", new Object[] { ID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes all permissions connected to a customer.
	 * 
	 * @param customerID
	 *            the customer
	 */
	public static void removePermissions(int customerID) {
		try {
			SQLPinCardService.removePinCards(customerID);
			SQLExecute.execute("DELETE FROM AccessPermissions WHERE CustomerID = ?", new Object[] { customerID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes the access permission of a customer to a bank account.
	 * 
	 * @param customerID
	 *            the customer
	 * @param IBAN
	 *            the bank account
	 * @throws SQLLayerException
	 */
	public static void removePermission(int customerID, String IBAN) throws SQLLayerException {
		int ID = SQLBankAccountService.getIDforIBAN(IBAN);
		try {
			SQLExecute.execute("DELETE FROM AccessPermissions WHERE BankAccountID = ? AND CustomerID = ?",
					new Object[] { ID, customerID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
