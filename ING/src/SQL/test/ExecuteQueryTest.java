package SQL.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import SQL.InvalidParameterTypeException;
import SQL.SQLExecute;

public class ExecuteQueryTest {
	public static void main(String[] args) {
		try {
			ResultSet res = SQLExecute.execute("Select * FROM CustomerAccounts");
			System.out.println("Result simple query:");
			SQLExecute.printResultSet(res);
			res = SQLExecute.execute("Select * FROM CustomerAccounts WHERE Name = ?", new String[]  {"Mendler"});
			System.out.println("Result query with parameter:");
			SQLExecute.printResultSet(res);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
		
	}
}
