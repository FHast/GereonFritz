package Services;

import SQL.SQLBankAccountService;
import SQL.SQLCustomerService;
import SQL.SQLLayerException;

public class BankAccountService {

	public static boolean addBankAccount(int mainCustomer, double startsaldo) throws InvalidParameterException {
		/**
		 * Check main customer
		 */

		if (!SQLCustomerService.isCustomerByID(mainCustomer)) {
			throw new InvalidParameterException("mainCustomer not present. ");
		}

		try {
			return SQLBankAccountService.addBankAccount(mainCustomer, startsaldo);
		} catch (SQLLayerException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean addBankAccount(int mainCustomer) throws InvalidParameterException {
		return addBankAccount(mainCustomer, 0);
	}

	public static void removeBankAccount(String IBAN) throws BankLogicException, InvalidParameterException {
		try {
			if (SQLBankAccountService.getSaldoByIBAN(IBAN) > 0) {
				throw new BankLogicException("Saldo of bankaccount not 0. IBAN: " + IBAN);
			} else {
				SQLBankAccountService.removeBankAccountByIBAN(IBAN);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}

	public static void removeBankAccounts(int customerID) throws InvalidParameterException, BankLogicException {
		if (SQLCustomerService.isCustomerByID(customerID)) {
			SQLBankAccountService.removeBankAccounts(customerID);
		} else {
			throw new InvalidParameterException("Invalid customer ID: " + customerID);
		}
	}
}
