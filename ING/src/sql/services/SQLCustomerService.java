package sql.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import services.exceptions.InvalidParameterException;
import sql.SQLExecute;
import sql.exceptions.InvalidParameterTypeException;
import sql.exceptions.SQLLayerException;

public class SQLCustomerService {
	
	// ADDING
	
	public static void addCustomer(String name, String surname, String DOB, int BSN, String address, int phone,
			String email) {
		String initials = "";
		for (String s : name.split(" ")) {
			initials += s.charAt(0);
		}
		initials += surname.charAt(0);

		Object[] parameters = new Object[] { null, name, surname, initials, DOB, BSN, address, phone, email };
		try {
			SQLExecute.execute("INSERT INTO CustomerAccounts values(?,?,?,?,?,?,?,?,?)", parameters);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
	}
	
	// GETTING

	public static ResultSet getCustomerByName(String name, String surname) throws SQLLayerException {
		try {
			return SQLExecute.executeQuery("SELECT * FROM CustomerAccounts WHERE Name=? AND Surname=?",
					new Object[] { name, surname });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	public static ResultSet getCustomerByBSN(int BSN) throws SQLLayerException {
		try {
			return SQLExecute.executeQuery("SELECT * FROM CustomerAccounts WHERE BSN=?", new Object[] { BSN });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}
	
	public static ResultSet getCustomerByID(int ID) throws SQLLayerException {
		try {
			return SQLExecute.executeQuery("SELECT * FROM CustomerAccounts WHERE CustomerAccountID=?",
					new Object[] { ID });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	public static boolean isCustomerByBSN(int BSN) throws SQLLayerException {
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
		throw new SQLLayerException();
	}

	public static boolean isCustomerByID(int ID) throws SQLLayerException {
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
		throw new SQLLayerException();
	}

	// REMOVING
	
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
