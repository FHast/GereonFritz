package sql.actors;

import java.sql.ResultSet;
import java.sql.SQLException;

import modules.exceptions.InvalidParamValueException;
import services.AccessPermissionService;
import services.BankAccountService;
import services.exceptions.BankLogicException;
import sql.SQLExecute;
import sql.exceptions.InvalidParameterTypeException;
import sql.exceptions.SQLLayerException;

public class SQLBankAccountService {

	public static final String COUNTRY = "NL";
	public static final String BLZ = "1337";

	// ADDING

	/**
	 * Adds a bank account to the database. Creates the IBAN according to this
	 * banks BLZ and Country code.
	 * 
	 * @param mainCustomer
	 *            the owner of the bank account.
	 * @param startsaldo
	 *            the initial capital.
	 * @throws SQLLayerException
	 */
	public static String addBankAccount(int mainCustomer, double startsaldo) throws SQLLayerException {

		int bankAccountNumber = -1;
		try {
			ResultSet bankAccounts = SQLExecute
					.executeQuery("SELECT seq FROM sqlite_sequence WHERE name = BankAccounts");
			bankAccountNumber = bankAccounts.getInt(1) + 1;

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
					new Object[] { IBAN, startsaldo, mainCustomer });
			AccessPermissionService.addPermission(mainCustomer, IBAN);
			return IBAN;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParamValueException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	// GETIING

	/**
	 * Returns the bank account which has the given IBAN.
	 * 
	 * @param IBAN
	 *            the IBAN
	 * @return ResultSet containing the bank account information
	 * @throws SQLLayerException
	 */
	public static ResultSet getBankAccountByIBAN(String IBAN) throws SQLLayerException {
		try {
			ResultSet bankacc = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?",
					new Object[] { IBAN });
			return bankacc;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	public static ResultSet getBankAccountsByCustomer(int customerID) throws SQLLayerException {
		try {
			ResultSet bankacc = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE MainCustomerID = ?",
					new Object[] { customerID });
			return bankacc;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	public static boolean hasBankAccounts(int customerID) throws SQLLayerException {

		try {
			ResultSet res = getBankAccountsByCustomer(customerID);
			return res.next();
		} catch (SQLLayerException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	public static boolean isOwner(int customerID, String iban) throws SQLLayerException {
		try {
			return SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN=? AND MainCustomerID=?",
					new Object[] { iban, customerID }).next();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	/**
	 * Checks whether this IBAN is valid or not.
	 * 
	 * @param IBAN
	 *            the IBAN
	 * @return true if a bank account exists with this IBAN, otherwise false
	 * @throws SQLLayerException
	 */
	public static boolean isBankAccountByIBAN(String IBAN) throws SQLLayerException {
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
		throw new SQLLayerException();
	}

	/**
	 * Returns the saldo of a bank account.
	 * 
	 * @param IBAN
	 *            the bank account
	 * @return saldo as double
	 * @throws SQLLayerException
	 */
	public static double getSaldoByIBAN(String IBAN) throws SQLLayerException {
		try {
			ResultSet res = getBankAccountByIBAN(IBAN);
			res.next();
			return res.getDouble(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	// REMOVING

	/**
	 * Removing a bank account from the database. Does not check whether money
	 * will be lost.
	 * 
	 * @param IBAN
	 *            the bank account
	 */
	public static void removeBankAccount(String IBAN) {
		try {
			SQLExecute.execute("DELETE FROM BankAccounts WHERE IBAN = ?", new Object[] { IBAN });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes all bank accounts this customer owns. Does check if saldo equals
	 * 0 in any account.
	 * 
	 * @param customerID
	 *            the customer
	 * @throws BankLogicException
	 *             if saldo of any account equals 0;
	 * @throws SQLLayerException
	 */
	public static void removeBankAccounts(int customerID) throws BankLogicException, SQLLayerException {
		try {
			ResultSet baccs = SQLBankAccountService.getBankAccountsByCustomer(customerID);
			while (baccs.next()) {
				BankAccountService.removeBankAccount(baccs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
