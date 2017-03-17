package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;

import Services.InvalidParameterException;
import Services.PinCardService;

public class SQLAccessPermissionService {
	
	// ADDING
	
	public static void addPermission(int customerID, String IBAN) throws SQLLayerException {
		try {
			int bankID = SQLBankAccountService.getIDforIBAN(IBAN);
			SQLExecute.execute("INSERT INTO AccessPermissions VALUES(?,?)",
					new Object[] { customerID, bankID });
			PinCardService.addPinCard(customerID, IBAN);
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}

	// GETTING
	
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
	
	public static void removePermissions(String IBAN) throws InvalidIBANException, SQLLayerException {
		int ID = SQLBankAccountService.getIDforIBAN(IBAN);
		try {
			SQLExecute.execute("DELETE FROM AccessPermissions WHERE BankAccountID = ?", new Object[] { ID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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
}
