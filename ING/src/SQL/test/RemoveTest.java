package SQL.test;

import java.sql.SQLException;

import SQL.SQLCustomerService;
import SQL.SQLExecute;

public class RemoveTest {
	public static void main(String[] args) {
		try {
			System.out.println("Removing Customer[1]");
			SQLExecute.execute("UPDATE BankAccounts SET Saldo = 0 WHERE MainCustomerID = 1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		SQLCustomerService.removeCustomer(2);
	}
}
