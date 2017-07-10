package modules;

import java.util.Map;

import modules.exceptions.InvalidParamValueException;
import modules.exceptions.InvalidParamsException;
import modules.exceptions.NotAutorizedException;
import modules.exceptions.OtherRpcException;
import sql.actors.SQLBankAccountService;
import sql.actors.SQLPinCardService;
import sql.actors.SQLTransactionService;
import sql.exceptions.SQLLayerException;

public class TransferModule {
	public static boolean depositIntoAccount(Map<String, Object> params)
			throws InvalidParamsException, InvalidParamValueException, OtherRpcException {
		try {
			// Check params.
			if (params == null || params.size() != 10) {
				throw new InvalidParamsException("Either no or not enough params given.");
			}

			/**
			 * Check amount.
			 */
			double amount = Double.parseDouble((String) params.get("amount"));
			if (amount <= 0) {
				throw new InvalidParamValueException("Amount must be positive. ");
			}

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
			 * Check pinCard
			 */
			int pinCard = Integer.parseInt((String) params.get("pinCard"));
			if (SQLPinCardService.isPinCard(pinCard)) {
				throw new InvalidParamValueException("Pincard invalid.");
			}

			/**
			 * Authorize pincard
			 */
			int pinCode = Integer.parseInt((String) params.get("pinCode"));
			if (!SQLPinCardService.isCorrectPin(pinCard, pinCode)) {
				throw new NotAutorizedException("Incorrect pin.");
			}
			
			// TODO continue working

			// Add to database.

			SQLTransactionService.deposit((String) params.get("iBAN"), amount, "Deposit");
			return true;
		} catch (SQLLayerException e) {
			throw new OtherRpcException("SQLlayerException");
		}
	}

	public static boolean payFromAccount(Map<String, Object> params)
			throws InvalidParamsException, InvalidParamValueException, OtherRpcException {
		// Check params.
		if (params == null || params.size() != 10) {
			throw new InvalidParamsException("Either no or not enough params given.");
		}

		/**
		 * Check amount.
		 */

		double amount = (double) params.get("amount");
		if (amount <= 0) {
			throw new InvalidParamValueException("Amount must be positive. ");
		}

		try {
			/**
			 * Check Sender.
			 */
			String senderIBAN = (String) params.get("sourceIBAN");
			if (!SQLBankAccountService.isBankAccountByIBAN(senderIBAN)) {
				throw new InvalidParamValueException("Invalid sender IBAN. ");
			}

			/**
			 * Check Receiver.
			 */
			String receiverIBAN = (String) params.get("targetIBAN");
			if (!SQLBankAccountService.isBankAccountByIBAN(receiverIBAN)) {
				throw new InvalidParamValueException("Invalid receiver IBAN. ");
			}

			// Add to database.

			SQLTransactionService.transfer(senderIBAN, receiverIBAN, amount, "", receiverName);
			return true;
		} catch (SQLLayerException e) {
			throw new OtherRpcException("SQLlayerException");
		}
	}

	public static boolean transferMoney(Map<String, Object> params)
			throws InvalidParamsException, InvalidParamValueException, OtherRpcException {
		// Check params.
		if (params == null || params.size() != 10) {
			throw new InvalidParamsException("Either no or not enough params given.");
		}

		/**
		 * Check amount.
		 */

		double amount = (double) params.get("amount");
		if (amount <= 0) {
			throw new InvalidParamValueException("Amount must be positive. ");
		}

		try {
			/**
			 * Check Sender.
			 */
			String senderIBAN = (String) params.get("sourceIBAN");
			if (!SQLBankAccountService.isBankAccountByIBAN(senderIBAN)) {
				throw new InvalidParamValueException("Invalid sender IBAN. ");
			}

			/**
			 * Check Receiver.
			 */
			String receiverIBAN = (String) params.get("targetIBAN");
			if (!SQLBankAccountService.isBankAccountByIBAN(receiverIBAN)) {
				throw new InvalidParamValueException("Invalid receiver IBAN. ");
			}

			// Add to database.

			SQLTransactionService.transfer(senderIBAN, receiverIBAN, amount, "", receiverName);
			return true;
		} catch (SQLLayerException e) {
			throw new OtherRpcException("SQLlayerException");
		}
	}
}
