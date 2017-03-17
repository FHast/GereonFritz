package SQL;

import java.sql.SQLException;
import java.time.LocalDateTime;

import Services.InvalidParameterException;

public class SQLTransactionService {

	// ADDING

	public static void transfer(String senderIBAN, String receiverIBAN, double amount, String usage,
			String receiverName) throws SQLLayerException, InvalidParameterException {
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
}
