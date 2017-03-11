package SQL.test;

import java.sql.SQLException;

import SQL.InvalidParameterTypeException;
import SQL.SQLCustomerService;
import SQL.SQLExecute;
import Services.CustomerService;
import Services.InvalidParameterException;

public class ExecuteQueryTest {
	public static void main(String[] args) {
		try {
			System.out.println("TESTING QUERIES... \n");

			System.out.println("Simple query:");
			SQLExecute.printResultSet(SQLExecute.executeQuery("Select * FROM CustomerAccounts"));

			System.out.println("Parameter query:");
			SQLExecute.printResultSet(SQLExecute.executeQuery("Select * FROM CustomerAccounts WHERE Name = ?",
					new String[] { "Gereon" }));

			System.out.println("SQLCustomerService by Name");
			SQLExecute.printResultSet(SQLCustomerService.getCustomerByName("Gereon", "Mendler"));

			System.out.println("SQLCustomerService by iD");
			SQLExecute.printResultSet(SQLCustomerService.getCustomerByID(1));

			System.out.println("Adding user... " + CustomerService.addCustomer("Fritz", "Hast", "23.02.1997", 1294919,
					"am Waier 14, 48599 Gronau", 1728323882, "f.hast@student.utwente.nl"));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}

	}
}