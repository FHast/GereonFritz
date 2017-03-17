package SQL.test;

import java.sql.SQLException;

import SQL.SQLCustomerService;
import SQL.SQLExecute;

public class RemoveTest {
	public static void main(String[] args) {
		try {
			SQLExecute.execute("UPDATE BankAccounts SET Saldo = 0 WHERE MainCustomerID = 1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		SQLCustomerService.removeCustomer(1);
	}
}
