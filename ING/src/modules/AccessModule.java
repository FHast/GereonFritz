package modules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import modules.exceptions.InvalidParamValueException;
import modules.exceptions.InvalidParamsException;
import modules.exceptions.NoEffectException;
import modules.exceptions.NotAuthenticatedException;
import modules.exceptions.OtherRpcException;
import sql.actors.SQLAccessPermissionService;
import sql.actors.SQLBankAccountService;
import sql.actors.SQLCustomerService;
import sql.actors.SQLPinCardService;
import sql.exceptions.SQLLayerException;

public class AccessModule {
	public static Map<String, Object> provideAccess(Map<String, Object> params) throws NotAuthenticatedException,
			InvalidParamValueException, InvalidParamsException, OtherRpcException, NoEffectException {
		try {
			if (params == null || params.size() != 3) {
				throw new InvalidParamsException("Either no or not enough params given.");
			}

			/**
			 * Check token
			 */
			String token = (String) params.get("authToken");
			AuthenticationModule.checkToken(token);

			/**
			 * Check IBAN
			 */
			String iban = (String) params.get("iBAN");
			if (iban == null || iban.equals("")) {
				throw new InvalidParamValueException("Iban must not be empty or null");
			}
			if (!SQLBankAccountService.isBankAccountByIBAN(iban)) {
				throw new InvalidParamValueException("Invalid iBAN");
			}

			/**
			 * Check username
			 */
			String username = (String) params.get("username");
			if (username == null || username.equals("")) {
				throw new InvalidParamValueException("Username must not be empty or null");
			}
			if (!SQLCustomerService.isCustomerByUsername(username)) {
				throw new InvalidParamValueException("Username not valid.");
			}

			/**
			 * Logic
			 */

			ResultSet cust = SQLCustomerService.getCustomerByUsername(username);
			int custID = cust.getInt(cust.findColumn("CustomerID"));

			if (SQLAccessPermissionService.hasPermission(custID, iban)) {
				throw new NoEffectException("User already has permission.");
			}

			// add permission (and card)
			SQLAccessPermissionService.addPermission(custID, iban);
			// get pincard
			ResultSet card = SQLPinCardService.getPinCardByIBAN(iban);
			int cardPin = card.getInt(card.findColumn("PIN"));
			int cardID = card.getInt(card.findColumn("PinCardID"));

			Map<String, Object> res = new HashMap<>();
			res.put("pinCard", cardID);
			res.put("pinCode", cardPin);
			return res;
		} catch (SQLLayerException | SQLException e) {
			throw new OtherRpcException("unexpected error");
		}
	}

	public static boolean revokeAccess(Map<String, Object> params) throws InvalidParamsException,
			NotAuthenticatedException, InvalidParamValueException, NoEffectException, OtherRpcException {
		try {
			if (params == null || params.size() != 3) {
				throw new InvalidParamsException("Either no or not enough params given.");
			}

			/**
			 * Check token
			 */
			String token = (String) params.get("authToken");
			AuthenticationModule.checkToken(token);

			/**
			 * Check IBAN
			 */
			String iban = (String) params.get("iBAN");
			if (iban == null || iban.equals("")) {
				throw new InvalidParamValueException("Iban must not be empty or null");
			}
			if (!SQLBankAccountService.isBankAccountByIBAN(iban)) {
				throw new InvalidParamValueException("Invalid iBAN");
			}

			/**
			 * Check username
			 */
			String username = (String) params.get("username");
			if (username == null || username.equals("")) {
				throw new InvalidParamValueException("Username must not be empty or null");
			}
			if (!SQLCustomerService.isCustomerByUsername(username)) {
				throw new InvalidParamValueException("Username not valid.");
			}

			/**
			 * Logic
			 */

			ResultSet cust = SQLCustomerService.getCustomerByUsername(username);
			int custID = cust.getInt(cust.findColumn("CustomerID"));

			if (!SQLAccessPermissionService.hasPermission(custID, iban)) {
				throw new NoEffectException("User does not have permission.");
			}

			SQLAccessPermissionService.removePermission(custID, iban);
			return true;
		} catch (SQLLayerException | SQLException e) {
			throw new OtherRpcException("unexpected error");
		}
	}
}
