package services;

import java.sql.ResultSet;

import services.exceptions.BankLogicException;
import services.exceptions.InvalidParameterException;
import sql.exceptions.SQLLayerException;
import sql.services.SQLBankAccountService;
import sql.services.SQLCustomerService;

public class BankAccountService {

	// ADDING

	/**
	 * Adds a bank account with an owner and an initial capital.
	 * 
	 * @param mainCustomer
	 *            the owner
	 * @param startsaldo
	 *            the starting capital
	 * @throws InvalidParameterException
	 *             if the customerID is invalid
	 */
	public static int addBankAccount(int mainCustomer, double startsaldo) throws InvalidParameterException {
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
		throw new InvalidParameterException("mainCustomer not present. ");
	}

	/**
	 * Adds a bank account with an owner and an initial capital of 0.
	 * 
	 * @param mainCustomer
	 *            the owner
	 * @throws InvalidParameterException
	 *             if the customerID is invalid
	 */
	public static void addBankAccount(int mainCustomer) throws InvalidParameterException {
		addBankAccount(mainCustomer, 0);
	}

	// GETTING

	public static ResultSet getBankAccountByIBAN(String IBAN) throws InvalidParameterException {
		try {
			if (SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				return SQLBankAccountService.getBankAccountByIBAN(IBAN);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParameterException("IBAN is invalid.");
	}

	public static ResultSet getBankAccountByID(int ID) throws InvalidParameterException {
		try {
			if (SQLBankAccountService.isBankAccountByID(ID)) {
				return SQLBankAccountService.getBankAccountByID(ID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParameterException("ID is invalid.");
	}
	
	public static ResultSet getBankAccountsByCustomer(int customerID) throws InvalidParameterException {
		try {
			if (SQLCustomerService.isCustomerByID(customerID)) {
				return SQLBankAccountService.getBankAccountsByCustomer(customerID);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParameterException("Customer ID is invalid.");
	}

	// REMOVING

	/**
	 * Removes a bank account.
	 * 
	 * @param IBAN
	 *            the bank account
	 * @throws BankLogicException
	 *             if the saldo is not 0.
	 * @throws InvalidParameterException
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
	 * @throws InvalidParameterException
	 *             if customerID is invalid
	 * @throws BankLogicException
	 *             if the saldo of any bank account is not 0
	 */
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
