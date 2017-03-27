package sql.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import sql.SQLExecute;
import sql.exceptions.InvalidParameterTypeException;
import sql.exceptions.SQLLayerException;

public class SQLTransactionService {

	// ADDING

	/**
	 * Adding a transaction entry to the database and transferring money
	 * accordingly. Does not check any parameter!
	 * 
	 * @param senderIBAN
	 *            sender bank account
	 * @param receiverIBAN
	 *            receiver bank account
	 * @param amount
	 *            amount
	 * @param usage
	 *            Text containing information about usage
	 * @param receiverName
	 *            receiver customer name
	 * @throws SQLLayerException
	 */
	public static void transfer(String senderIBAN, String receiverIBAN, double amount, String usage,
			String receiverName) throws SQLLayerException {
		try {
			LocalDateTime currentTime = LocalDateTime.now();
			String datetime = currentTime.toString();

			SQLExecute.execute("UPDATE BankAccounts SET saldo = ? WHERE IBAN = ?",
					new Object[] { SQLBankAccountService.getSaldoByIBAN(senderIBAN) - amount, senderIBAN });
			SQLExecute.execute("UPDATE BankAccounts SET saldo = ? WHERE IBAN = ?",
					new Object[] { SQLBankAccountService.getSaldoByIBAN(receiverIBAN) + amount, receiverIBAN });
			SQLExecute.execute("INSERT INTO Transactions VALUES(?,?,?,?,?,?,?)",
					new Object[] { null, amount, usage, datetime, receiverName, senderIBAN, receiverIBAN });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
	}

	public static ResultSet getTransactionsByIBAN(String IBAN) throws SQLLayerException {
		try {
			ResultSet res = SQLExecute.executeQuery("SELECT * FROM Transactions WHERE ReceiverIBAN = ? OR SenderIBAN = ?",
					new Object[] { IBAN, IBAN });
			return res;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}
}
