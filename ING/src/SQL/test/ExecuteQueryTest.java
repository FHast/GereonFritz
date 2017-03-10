package SQL.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import SQL.SQLExecute;

public class ExecuteQueryTest {
	public static void main(String[] args) {
		try {
			ResultSet res = SQLExecute.execute("Select * FROM CustomerAccounts");
			System.out.println("Result:");
			SQLExecute.printResultSet(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
