package Services;

import java.sql.SQLException;

import SQL.SQLBankAccountService;
import SQL.SQLTransactionService;

public class TransactionService {
	public static void transfer(String senderIBAN, String receiverIBAN, double amount, String usage,
			String receiverName) throws InvalidParameterException {
		try {
			if (senderIBAN == null || receiverIBAN == null || receiverName == null) {
				throw new InvalidParameterException("Values cannot be 0 or null.");
			}

			/**
			 * Check sender
			 */
			
			if (!SQLBankAccountService.isBankAccountByIBAN(senderIBAN)) {
				throw new InvalidParameterException("Invalid Sender IBAN. ");
			}
			if (senderIBAN.equals(receiverIBAN)) {
				throw new InvalidParameterException("senderIBAN cannot equal receiverIBAN");
			}
			
			/**
			 * Check Receiver
			 */
			
			if (!SQLBankAccountService.isBankAccountByIBAN(receiverIBAN)) {
				throw new InvalidParameterException("Invalid Receiver IBAN. ");
			}
			
			/**
			 * Check amount
			 */

			if (amount <= 0) {
				throw new InvalidParameterException("Amount must be positive. ");
			}
			if (SQLBankAccountService.getSaldoByIBAN(senderIBAN) < amount) {
				throw new InvalidParameterException("Amount exceeds sender saldo. ");
			}

			// All correct
			SQLTransactionService.transfer(senderIBAN, receiverIBAN, amount, usage, receiverName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
