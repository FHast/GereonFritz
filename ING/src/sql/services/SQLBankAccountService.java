package sql.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import services.AccessPermissionService;
import services.BankAccountService;
import services.exceptions.BankLogicException;
import services.exceptions.InvalidParameterException;
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
	public static void addBankAccount(int mainCustomer, double startsaldo) throws SQLLayerException {

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

		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}

	// GETIING

	/**
	 * Returns all bank accounts a customer owns.
	 * 
	 * @param maincustomer
	 *            the customer
	 * @return ResultSet of all bank accounts
	 * @throws SQLLayerException
	 */
	public static ResultSet getBankAccounts(int maincustomer) throws SQLLayerException {
		try {
			return SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE MainCustomerID = ?",
					new Object[] { maincustomer });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

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

	/**
	 * Returns the identifier of a bank account connected to a given IBAN.
	 * 
	 * @param IBAN
	 *            the IBAN
	 * @return the bank account ID connected to this IBAN
	 * @throws SQLLayerException
	 */
	public static int getIDforIBAN(String IBAN) throws SQLLayerException {
		ResultSet res = getBankAccountByIBAN(IBAN);
		try {
			if (res.next()) {
				return res.getInt(1);
			}
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
	public static void removeBankAccountByIBAN(String IBAN) {
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
			ResultSet baccs = SQLBankAccountService.getBankAccounts(customerID);
			while (baccs.next()) {
				BankAccountService.removeBankAccount(baccs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
