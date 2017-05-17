package sql.actors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import modules.exceptions.InvalidParamValueException;
import sql.SQLExecute;
import sql.exceptions.InvalidParameterTypeException;
import sql.exceptions.SQLLayerException;

public class SQLCustomerService {

	// ADDING

	/**
	 * Adds a customer to the database.
	 * 
	 * @param name
	 * @param surname
	 * @param DOB
	 *            date of birth
	 * @param BSN
	 *            "Bürgerservicenummer"
	 * @param address
	 * @param phone
	 * @param email
	 * @throws SQLLayerException 
	 */
	public static void addCustomer(Map<String, Object> params) throws SQLLayerException {
		Object[] parameters = new Object[] { null, params.get("name"), params.get("surname"), params.get("initials"),
				params.get("dob"), params.get("ssn"), params.get("address"), params.get("phone"), params.get("email") };
		try {
			SQLExecute.execute("INSERT INTO CustomerAccounts values(?,?,?,?,?,?,?,?,?)", parameters);
		} catch (SQLException e) {
			throw new SQLLayerException();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
	}

	// GETTING

	/**
	 * Returns all customers having the given name.
	 * 
	 * @param name
	 *            first name
	 * @param surname
	 *            last name / family name
	 * @return ResultSet of all customers having this name
	 * @throws SQLLayerException
	 */
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

	/**
	 * Returns the customer having the given BSN.
	 * 
	 * @param BSN
	 *            the BSN
	 * @return ResultSet containing the customer with this BSN
	 * @throws SQLLayerException
	 */
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

	public static ResultSet getCustomerByEmail(String email) throws SQLLayerException {
		try {
			return SQLExecute.executeQuery("SELECT * FROM CustomerAccounts WHERE Email=?", new Object[] { email });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParameterTypeException e) {
			e.printStackTrace();
		}
		throw new SQLLayerException();
	}

	/**
	 * Returns the customer having the given ID.
	 * 
	 * @param ID
	 *            the customer ID
	 * @return ResultSet containing the customer with this ID
	 * @throws SQLLayerException
	 */
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

	/**
	 * Checks whether there is a customer with this BSN.
	 * 
	 * @param BSN
	 *            the BSN
	 * @return true if there is a customer with this BSN
	 * @throws SQLLayerException
	 */
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

	/**
	 * Checks if there is a customer with this ID.
	 * 
	 * @param ID
	 *            the customer ID
	 * @return true if there is a customer with this ID
	 * @throws SQLLayerException
	 */
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

	public static boolean isCustomerByEmail(String email) throws SQLLayerException {
		try {
			ResultSet res = getCustomerByEmail(email);
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

	/**
	 * Removes a customer from the database. Will delete any connected table
	 * entries. (Cascading!)
	 * 
	 * @param customerID
	 *            the customer which should be deleted.
	 */
	public static void removeCustomer(int customerID) {
		try {
			SQLExecute.execute("DELETE FROM CustomerAccounts WHERE CustomerAccountID = ?", new Object[] { customerID });
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidParamValueException e) {
			e.printStackTrace();
		}
	}
}
