package Services;

import SQL.SQLBankAccountService;
import SQL.SQLCustomerService;
import SQL.SQLLayerException;

public class BankAccountService {

	public static void addBankAccount(int mainCustomer, double startsaldo) throws InvalidParameterException {
		/**
		 * Check main customer
		 */

		if (!SQLCustomerService.isCustomerByID(mainCustomer)) {
			throw new InvalidParameterException("mainCustomer not present. ");
		}

		try {
			SQLBankAccountService.addBankAccount(mainCustomer, startsaldo);
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}

	public static void addBankAccount(int mainCustomer) throws InvalidParameterException {
		addBankAccount(mainCustomer, 0);
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
		try {
			if (SQLCustomerService.isCustomerByID(customerID)) {
				SQLBankAccountService.removeBankAccounts(customerID);
			} else {
				throw new InvalidParameterException("Invalid customer ID: " + customerID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}

	}
}
