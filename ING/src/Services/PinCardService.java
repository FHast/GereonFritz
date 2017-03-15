package Services;

import java.sql.ResultSet;
import java.sql.SQLException;

import SQL.SQLExecute;
import SQL.SQLPinCardService;

public class PinCardService {
	public static boolean addPinCard(int customerID, String IBAN) throws InvalidParameterException {
		try {
			if (customerID <= 0 || IBAN == null) {
				throw new InvalidParameterException("Values cannot be 0 or null.");
			}

			/**
			 * Check IBAN
			 */

			ResultSet bankacc = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?",
					new Object[] { IBAN });
			if (!bankacc.next()) {
				throw new InvalidParameterException("IBAN is invalid. ");
			}

			/**
			 * Check customer
			 */

			ResultSet customer = SQLExecute.executeQuery("SELECT * FROM CustomerAccounts WHERE CustomerAccountID = ?",
					new Object[] { customerID });
			if (!customer.next()) {
				throw new InvalidParameterException("CustomerID is invalid");
			}

			/**
			 * Cehckl permissions
			 */

			ResultSet permission = SQLExecute.executeQuery(
					"SELECT * FROM AccessPermissions WHERE CustomerID = ? AND BankAccountID = ?",
					new Object[] { customerID, bankacc.getInt(1) });
			if (!permission.next()) {
				throw new InvalidParameterException("Customer has no access permission!");
			}

			return SQLPinCardService.addPinCard(customerID, IBAN);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
