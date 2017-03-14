package Services;

import java.sql.ResultSet;
import java.sql.SQLException;

import SQL.InvalidParameterTypeException;
import SQL.SQLAccessPermissionService;
import SQL.SQLExecute;

public class AccessPermissionService {
	public static boolean addPermission(int customerID, String IBAN) throws InvalidParameterException {
		try {
			if (customerID <= 0 || IBAN == null) {
				throw new InvalidParameterException("String cannot be null, int cannot be 0");
			}

			/**
			 * Check customer
			 */

			ResultSet customer = SQLExecute.executeQuery("SELECT * FROM CustomerAccounts WHERE CustomerAccountID = ?",
					new Object[] { customerID });
			if (!customer.next()) {
				throw new InvalidParameterException("CustomerID not valid. ");
			}

			/**
			 * Check IBAN
			 */

			ResultSet bankacc = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?",
					new Object[] { IBAN });
			if (!bankacc.next()) {
				throw new InvalidParameterException("IBAN not valid. ");
			}

			// All correct

			return SQLAccessPermissionService.addPermission(customerID, IBAN);
			
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
