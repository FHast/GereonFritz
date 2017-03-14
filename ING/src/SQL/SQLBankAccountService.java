package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLBankAccountService {

	public static final String COUNTRY = "NL";
	public static final String BLZ = "1337";

	public static boolean addBankAccount(int mainCustomer, double startsaldo) {

		try {
			ResultSet bankAccounts = SQLExecute.executeQuery("SELECT MAX(BankAccountID) FROM BankAccounts");
			int bankAccountNumber = bankAccounts.getInt(1) + 1;

			String IBAN = COUNTRY + BLZ;
			if (bankAccountNumber < 10) {
				IBAN += "000000000" + bankAccountNumber;
			} else if (bankAccountNumber < 100) {
				IBAN += "00000000" + bankAccountNumber;
			} else if (bankAccountNumber < 1000) {
				IBAN += "0000000" + bankAccountNumber;
			} else if (bankAccountNumber < 10000) {
				IBAN += "000000" + bankAccountNumber;
			} else if (bankAccountNumber < 100000) {
				IBAN += "00000" + bankAccountNumber;
			} else if (bankAccountNumber < 1000000) {
				IBAN += "0000" + bankAccountNumber;
			} else if (bankAccountNumber < 10000000) {
				IBAN += "000" + bankAccountNumber;
			} else if (bankAccountNumber < 100000000) {
				IBAN += "00" + bankAccountNumber;
			} else if (bankAccountNumber < 1000000000) {
				IBAN += "0" + bankAccountNumber;
			} else {
				IBAN += bankAccountNumber;
			}
			
			SQLExecute.execute("INSERT INTO BankAccounts VALUES(?,?,?,?)",
					new Object[] { bankAccountNumber, startsaldo, mainCustomer, IBAN });
			return true;

		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
