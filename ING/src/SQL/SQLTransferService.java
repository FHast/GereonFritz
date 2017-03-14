package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Services.InvalidParameterException;

public class SQLTransferService {

	public static void transfer(String senderIBAN, String receiverIBAN, double amount, String usage, String receiverName) throws InvalidParameterException {
		if (senderIBAN == null || receiverIBAN == null || receiverName == null ) {
			throw new InvalidParameterException("Values cannot be 0 or null.");
		}
		if (amount <= 0) {
			throw new InvalidParameterException("Amount must be positive. ");
		}
		try {
			ResultSet sender = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?", new Object[] {senderIBAN});
			if (!sender.next()) {
				throw new InvalidParameterException("Invalid Sender IBAN. ");
			}
			ResultSet receiver = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?", new Object[] {receiverIBAN});
			if (!receiver.next()) {
				throw new InvalidParameterException("Invalid Receiver IBAN. ");
			}
			if (sender.getInt(2) < amount) {
				throw new InvalidParameterException("Amount exceeds sender saldo. ");
			}
			
			// All correct
			
			SQLExecute.execute("UPDATE BankAccounts SET saldo = ? WHERE IBAN = ?", new Object[] {sender.getInt(2) - amount, senderIBAN});
			SQLExecute.execute("UPDATE BankAccounts SET saldo = ? WHERE IBAN = ?", new Object[] {receiver.getInt(2) + amount, receiverIBAN});
			SQLExecute.execute("INSERT INTO Transactions VALUES(?,?,?,?,?,?,?)", new Object[] {null,amount,usage,new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),receiverName,senderIBAN,receiverIBAN});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
