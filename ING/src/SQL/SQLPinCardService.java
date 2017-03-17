package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SQLPinCardService {
	public static boolean addPinCard(int customerID, String IBAN) {
		try {
			ResultSet bankacc = SQLExecute.executeQuery("SELECT * FROM BankAccounts WHERE IBAN = ?",
					new Object[] { IBAN });

			LocalDateTime currentTime = LocalDateTime.now();
			String date = currentTime.toLocalDate().plusYears(4).toString();
			String pin = "" + (int) (Math.random() * 10) + "" + (int) (Math.random() * 10) + ""
					+ (int) (Math.random() * 10) + "" + (int) (Math.random() * 10);

			SQLExecute.execute("INSERT INTO PinCards VALUES(?,?,?,?,?)",
					new Object[] { null, date, pin, customerID, bankacc.getInt(1) });
			return true;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void removePinCards(int customerID) {
		try {
			SQLExecute.execute("DELETE FROM PinCards WHERE CustomerID = ?", new Object[] { customerID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void removePinCards(String IBAN) throws InvalidIBANException {
		int ID = SQLBankAccountService.getIDforIBAN(IBAN);
		try {
			SQLExecute.execute("DELETE FROM PinCards WHERE BankAccountID = ?", new Object[] { ID });
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
