package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;

import Services.AccessPermissionService;
import Services.BankLogicException;
import Services.InvalidParameterException;

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
			AccessPermissionService.addPermission(mainCustomer, IBAN);
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

	public static ResultSet getBankAccountByIBAN(String IBAN) {
		try {
			ResultSet bankacc = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?",
					new Object[] { IBAN });
			return bankacc;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isBankAccountByIBAN(String IBAN) {
		try {
			ResultSet res = getBankAccountByIBAN(IBAN);
			if (!res.next()) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static double getSaldoByIBAN(String IBAN) throws InvalidParameterException, BankLogicException {
		try {
			ResultSet res = getBankAccountByIBAN(IBAN);
			if (!res.next()) {
				throw new InvalidParameterException("IBAN Invalid.");
			} else {
				return res.getDouble(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new BankLogicException("Method Corrupted. ");
	}

	public static void removeBankAccountByIBAN(String IBAN) throws BankLogicException, InvalidParameterException {
		if (getSaldoByIBAN(IBAN) > 0) {
			throw new BankLogicException("Saldo of bankaccount not 0. ");
		} else {
			SQLAccessPermissionService.removePermissions(IBAN);
			try {
				SQLExecute.execute("DELETE FROM BankAccounts WHERE IBAN = ?", new Object[] { IBAN });
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static int getIDforIBAN(String IBAN) throws InvalidIBANException {
		ResultSet res = getBankAccountByIBAN(IBAN);
		try {
			if (res.next()) {
				return res.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new InvalidIBANException("IBAN not present.");
	}

	public static ResultSet getBankAccounts(int maincustomer) {
		try {
			return SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE MainCustomerID = ?",
					new Object[] { maincustomer });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
