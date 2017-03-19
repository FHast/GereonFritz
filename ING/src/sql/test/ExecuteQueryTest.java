package sql.test;

import java.sql.SQLException;

import sql.SQLExecute;
import sql.exceptions.InvalidParameterTypeException;
import sql.exceptions.SQLLayerException;
import sql.services.SQLCustomerService;

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

			System.out.println("Adding user... ");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}  catch (SQLLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}