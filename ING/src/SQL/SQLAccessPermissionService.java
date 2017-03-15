package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAccessPermissionService {
	public static boolean addPermission(int customerID, String IBAN) {
		try {
			ResultSet bankacc = SQLExecute.executeQuery("SELECT BankAccountID FROM BankAccounts WHERE IBAN = ?",
					new Object[] { IBAN });
			if (!bankacc.next()) {
				return false;
			}
			SQLExecute.execute("INSERT INTO AccessPermissions VALUES(?,?)", new Object[] { customerID, bankacc.getInt(1) });
			SQLPinCardService.addPinCard(customerID, IBAN);
			return true;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
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
}
