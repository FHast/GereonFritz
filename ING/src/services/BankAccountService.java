package services;

import java.sql.ResultSet;

import modules.exceptions.InvalidParamValueException;
import services.exceptions.BankLogicException;
import sql.actors.SQLBankAccountService;
import sql.actors.SQLCustomerService;
import sql.exceptions.SQLLayerException;

public class BankAccountService {

	// ADDING

	/**
	 * Adds a bank account with an owner and an initial capital.
	 * 
	 * @param mainCustomer
	 *            the owner
	 * @param startsaldo
	 *            the starting capital
	 * @throws InvalidParamValueException
	 *             if the customerID is invalid
	 */
	public static int addBankAccount(int mainCustomer, double startsaldo) throws InvalidParamValueException {
		/**
		 * Check main customer
		 */
		try {
			if (SQLCustomerService.isCustomerByID(mainCustomer)) {
				return SQLBankAccountService.addBankAccount(mainCustomer, startsaldo);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("mainCustomer not present. ");
	}

	/**
	 * Adds a bank account with an owner and an initial capital of 0.
	 * 
	 * @param mainCustomer
	 *            the owner
	 * @throws InvalidParamValueException
	 *             if the customerID is invalid
	 */
	public static void addBankAccount(int mainCustomer) throws InvalidParamValueException {
		addBankAccount(mainCustomer, 0);
	}

	// GETTING

	public static ResultSet getBankAccountByIBAN(String IBAN) throws InvalidParamValueException {
		try {
			if (SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				return SQLBankAccountService.getBankAccountByIBAN(IBAN);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("IBAN is invalid.");
	}

	public static ResultSet getBankAccountByID(int ID) throws InvalidParamValueException {
		try {
			if (SQLBankAccountService.isBankAccountByID(ID)) {
				return SQLBankAccountService.getBankAccountByID(ID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("ID is invalid.");
	}
	
	public static ResultSet getBankAccountsByCustomer(int customerID) throws InvalidParamValueException {
		try {
			if (SQLCustomerService.isCustomerByID(customerID)) {
				return SQLBankAccountService.getBankAccountsByCustomer(customerID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("Customer ID is invalid.");
	}
	
	public static boolean isBankAccountByIBAN(String IBAN) {
		try {
			return SQLBankAccountService.isBankAccountByIBAN(IBAN);
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		return false;
	}

	// REMOVING

	/**
	 * Removes a bank account.
	 * 
	 * @param IBAN
	 *            the bank account
	 * @throws BankLogicException
	 *             if the saldo is not 0.
	 * @throws InvalidParamValueException
	 *             if the IBAN is invalid
	 */
	public static void removeBankAccount(String IBAN) throws BankLogicException {
		try {
			if (SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				if (SQLBankAccountService.getSaldoByIBAN(IBAN) > 0) {
					throw new BankLogicException("Saldo of bankaccount not 0. IBAN: " + IBAN);
				} else {
					SQLBankAccountService.removeBankAccountByIBAN(IBAN);
				}
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes all bank accounts owned by this customer.
	 * 
	 * @param customerID
	 *            the owner / customer
	 * @throws InvalidParamValueException
	 *             if customerID is invalid
	 * @throws BankLogicException
	 *             if the saldo of any bank account is not 0
	 */
	public static void removeBankAccounts(int customerID) throws InvalidParamValueException, BankLogicException {
		try {
			if (SQLCustomerService.isCustomerByID(customerID)) {
				SQLBankAccountService.removeBankAccounts(customerID);
			} else {
				throw new InvalidParamValueException("Invalid customer ID: " + customerID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}

	}
}
