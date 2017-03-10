package SQL;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLExecute {

	public static Connection con;
	public static boolean hasData = false;

	/**
	 * Executes a given SQLite Query without parameters.
	 * 
	 * @param query
	 *            query of SQLite type.
	 * @return The query's ResultSet
	 * @throws SQLException
	 *             If the SQLite Query is incorrect.
	 */
	public static ResultSet execute(String query) throws SQLException {
		if (con == null) {
			try {
				getConnection();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		Statement state = con.createStatement();
		ResultSet res = state.executeQuery(query);

		return res;
	}

	/**
	 * Executes a given SQLite Query with the given parameters.
	 * 
	 * @param query
	 *            SQLite Query
	 * @param parameters
	 *            Parameters for the '?'s in the Query, ordered
	 * @return The ResultSet for the query
	 * @throws SQLException
	 *             If Query syntax is incorrect.
	 */
	public static ResultSet execute(String query, String[] parameters) throws SQLException {
		if (con == null) {
			try {
				getConnection();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		PreparedStatement prep = con.prepareStatement(query);
		for (int i = 0; i < parameters.length; i++) {
			prep.setString(i + 1, parameters[i]);
		}
		ResultSet res = prep.executeQuery();

		return res;
	}

	public static void printResultSet(ResultSet resultSet) throws SQLException {
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		while (resultSet.next()) {
			for (int i = 1; i <= columnsNumber; i++) {
				if (i > 1)
					System.out.print(" | ");
				String columnValue = resultSet.getString(i);
				System.out.print("[" + rsmd.getColumnName(i) + "] " + columnValue);
			}
			System.out.println("");
		}
	}

	private static void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:Userdata.db");
		initialize();
	}

	private static void initialize() {
		if (!hasData) {
			hasData = true;

			Statement state;
			try {
				state = con.createStatement();
				ResultSet res = state
						.executeQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name='PinCards'");

				if (!res.next()) {
					System.out.println("Creating database tables...");
					for (String s : SQLfileReader.getQueries("src/SQL/createDatabase.sql")) {
						state = con.createStatement();
						state.execute(s);
					}
					System.out.println("Creation finished.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NoQueryFoundException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// createDatabase.sql is missing
				e.printStackTrace();
			}
		}
	}

}
