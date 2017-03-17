package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;

import Services.BankLogicException;
import Services.InvalidParameterException;

public class SQLCustomerService {

	public static ResultSet getCustomerByName(String name, String surname) {
		try {
			return SQLExecute.executeQuery("SELECT * FROM CustomerAccounts WHERE Name=? AND Surname=?",
					new Object[] { name, surname });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ResultSet getCustomerByBSN(int BSN) {
		try {
			return SQLExecute.executeQuery("SELECT * FROM CustomerAccounts WHERE BSN=?", new Object[] { BSN });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isCustomerByBSN(int BSN) {
		try {
			ResultSet res = getCustomerByBSN(BSN);
			if (!res.next()) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static ResultSet getCustomerByID(int ID) {
		try {
			return SQLExecute.executeQuery("SELECT * FROM CustomerAccounts WHERE CustomerAccountID=?",
					new Object[] { ID });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isCustomerByID(int ID) {
		try {
			ResultSet res = getCustomerByID(ID);
			if (!res.next()) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean addCustomer(String name, String surname, String DOB, int BSN, String address, int phone,
			String email) {
		String initials = "";
		for (String s : name.split(" ")) {
			initials += s.charAt(0);
		}
		initials += surname.charAt(0);

		Object[] parameters = new Object[] { null, name, surname, initials, DOB, BSN, address, phone, email };
		try {
			SQLExecute.execute("INSERT INTO CustomerAccounts values(?,?,?,?,?,?,?,?,?)", parameters);
			return true;
		} catch (SQLException e) {
			return false;
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void removeCustomer(int customerID) {
		try {
			SQLExecute.execute("DELETE FROM CustomerAccounts WHERE CustomerAccountID = ?", new Object[] { customerID });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}
}
