package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;

import Services.InvalidParameterException;
import Services.PinCardService;

public class SQLAccessPermissionService {
	public static boolean addPermission(int customerID, String IBAN) {
		try {
			ResultSet bankacc = SQLExecute.executeQuery("SELECT BankAccountID FROM BankAccounts WHERE IBAN = ?",
					new Object[] { IBAN });
			if (!bankacc.next()) {
				return false;
			}
			SQLExecute.execute("INSERT INTO AccessPermissions VALUES(?,?)",
					new Object[] { customerID, bankacc.getInt(1) });
			PinCardService.addPinCard(customerID, IBAN);
			return true;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ResultSet getPermissions(int customerID) {
		try {
			return SQLExecute.executeQuery("SELECT * FROM AccessPermissions WHERE CustomerID = ?",
					new Object[] { customerID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean hasPermission(int customerID, String IBAN) {
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
		return false;
	}

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
