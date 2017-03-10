package SQL;

public abstract class SQLQueries {

	public static final String createAccessPermissions = "CREATE TABLE AccessPermissions ("
			+ "`AccessPermissionID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,"
			+ "`CustomerID`	INTEGER NOT NULL," + "`BankAccountID`	INTEGER NOT NULL,"
			+ "FOREIGN KEY(`CustomerID`) REFERENCES CustomerAccounts ( CustomerAccountID ), "
			+ "FOREIGN KEY(`BankAccountID`) REFERENCES BankAccounts ( BankAccountID ))";
	public static final String getUsers = "SELECT  * FROM CustomerAccounts";

}
