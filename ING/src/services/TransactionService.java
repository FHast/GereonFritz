package services;

import java.sql.ResultSet;

import modules.exceptions.InvalidParamValueException;
import sql.actors.SQLBankAccountService;
import sql.actors.SQLTransactionService;
import sql.exceptions.SQLLayerException;

public class TransactionService {

	// ADDING

	/**
	 * Making a money transfer between two bank accounts.
	 * 
	 * @param senderIBAN
	 *            sender bank account
	 * @param receiverIBAN
	 *            receiver bank account
	 * @param amount
	 *            amount
	 * @param usage
	 *            Annotation of usage / message
	 * @param receiverName
	 *            receiver customer name
	 * @throws InvalidParamValueException
	 *             if any input is invalid
	 */
	public static void transfer(String senderIBAN, String receiverIBAN, double amount, String usage,
			String receiverName) throws InvalidParamValueException {
		try {
			if (senderIBAN == null || receiverIBAN == null || receiverName == null) {
				throw new InvalidParamValueException("Values cannot be 0 or null.");
			}

			/**
			 * Check sender
			 */

			if (!SQLBankAccountService.isBankAccountByIBAN(senderIBAN)) {
				throw new InvalidParamValueException("Invalid Sender IBAN. ");
			}
			if (senderIBAN.equals(receiverIBAN)) {
				throw new InvalidParamValueException("senderIBAN cannot equal receiverIBAN");
			}

			/**
			 * Check Receiver
			 */

			if (!SQLBankAccountService.isBankAccountByIBAN(receiverIBAN)) {
				throw new InvalidParamValueException("Invalid Receiver IBAN. ");
			}

			/**
			 * Check amount
			 */

			if (amount <= 0) {
				throw new InvalidParamValueException("Amount must be positive. ");
			}
			if (SQLBankAccountService.getSaldoByIBAN(senderIBAN) < amount) {
				throw new InvalidParamValueException("Amount exceeds sender saldo. ");
			}

			// All correct
			SQLTransactionService.transfer(senderIBAN, receiverIBAN, amount, usage, receiverName);
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
	}
	
	public static ResultSet getTransactionsByIBAN(String IBAN) throws InvalidParamValueException {
		try {
			if (SQLBankAccountService.isBankAccountByIBAN(IBAN)) {
				return SQLTransactionService.getTransactionsByIBAN(IBAN);
			}
		} catch (SQLLayerException e) {
			e.printStackTrace();
		}
		throw new InvalidParamValueException("IBAN is invalid.");
	}
}
