package Services;

import java.sql.ResultSet;
import java.sql.SQLException;

import SQL.InvalidParameterTypeException;
import SQL.SQLBankAccountService;
import SQL.SQLExecute;

public class BankAccountService {

	public static boolean addBankAccount(int mainCustomer, double startsaldo) throws InvalidParameterException {
		/**
		 * Check main customer
		 */

		try {
			ResultSet mainc = SQLExecute.executeQuery("SELECT * FROM CustomerAccounts WHERE CustomerAccountID = ?",
					new Object[] { mainCustomer });
			if (!mainc.next()) {
				throw new InvalidParameterException("mainCustomer not present. ");
			}
			
			return SQLBankAccountService.addBankAccount(mainCustomer, startsaldo);

		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean addBankAccount(int mainCustomer) throws InvalidParameterException {
		return addBankAccount(mainCustomer, 0);
	}
}
