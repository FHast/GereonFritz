package Services;

import java.sql.ResultSet;
import java.sql.SQLException;

import SQL.SQLExecute;
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
			
			ResultSet sender = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?",
					new Object[] { senderIBAN });
			if (!sender.next()) {
				throw new InvalidParameterException("Invalid Sender IBAN. ");
			}
			if (senderIBAN.equals(receiverIBAN)) {
				throw new InvalidParameterException("senderIBAN cannot equal receiverIBAN");
			}
			
			/**
			 * Check Receiver
			 */
			
			ResultSet receiver = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?",
					new Object[] { receiverIBAN });
			if (!receiver.next()) {
				throw new InvalidParameterException("Invalid Receiver IBAN. ");
			}
			
			/**
			 * Check amount
			 */

			if (amount <= 0) {
				throw new InvalidParameterException("Amount must be positive. ");
			}
			if (sender.getInt(2) < amount) {
				throw new InvalidParameterException("Amount exceeds sender saldo. ");
			}

			// All correct
			SQLTransactionService.transfer(senderIBAN, receiverIBAN, amount, usage, receiverName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
