package sql.test;

import java.sql.SQLException;

import services.CustomerService;
import services.exceptions.BankLogicException;
import services.exceptions.InvalidParameterException;
import sql.SQLExecute;

public class RemoveTest {
	public static void main(String[] args) {
		try {
			System.out.println("Removing Customer[1]");
			SQLExecute.execute("UPDATE BankAccounts SET Saldo = 0 WHERE MainCustomerID = 1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			CustomerService.removeCustomer(4);
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (BankLogicException e) {
			System.out.println("Cancelled: Bank accounts not empty.");
		}
	}
}
