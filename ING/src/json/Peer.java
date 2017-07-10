package json;

import java.util.Map;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

import modules.AccessModule;
import modules.AccountModule;
import modules.exceptions.AuthenticationException;
import modules.exceptions.InvalidParamValueException;
import modules.exceptions.InvalidParamsException;
import modules.exceptions.NoEffectException;
import modules.exceptions.NotAutorizedException;
//github.com/FHast/INGhonours_GereonFritz.git
import modules.exceptions.OtherRpcException;

public class Peer {
	public String getResponse(String request) {
		JSONRPC2Request input = null;
		try {
			input = JSONRPC2Request.parse(request);
		} catch (JSONRPC2ParseException e) {
			e.printStackTrace();
		}

		String method = input.getMethod();
		Map<String, Object> params = input.getNamedParams();
		Object id = input.getID();
		JSONRPC2Error resultError = null;
		String resultString = "";
		Map<String, Object> resultMap = null;
		Boolean resultBoolean = null;

		try {
			switch (method) {
			case "openAccount":
				resultMap = AccountModule.openAccount(params);
				break;
			case "openAdditionalAccount":
				resultMap = AccountModule.openAdditionalAccount(params);
				break;
			case "closeAccount":
				resultBoolean = AccountModule.closeAccount(params);
				break;
			case "provideAccess":
				resultMap = AccessModule.provideAccess(params);
				break;
			case "revokeAccess":
				resultBoolean = AccessModule.revokeAccess(params);
				break;
			case "depositIntoAccount":
				break;
			case "payFromAccount":
				break;
			case "transferMoney":
				break;
			case "getAuthToken":
				break;
			case "getBalance":
				break;
			case "getTransactionsOverview":
				break;
			case "getUserAccess":
				break;
			case "getBankAccountAccess":
				break;
			default:
				// TODO;
			}
		} catch (

		InvalidParamValueException e) {
			resultError = new JSONRPC2Error(418, e.getMessage());
		} catch (InvalidParamsException e) {
			resultError = new JSONRPC2Error(-32602, e.getMessage());
		} catch (AuthenticationException e) {
			resultError = new JSONRPC2Error(419, e.getMessage());
		} catch (OtherRpcException e) {
			resultError = new JSONRPC2Error(500, e.getMessage());
		} catch (NoEffectException e) {
			resultError = new JSONRPC2Error(420, e.getMessage());
		} catch (NotAutorizedException e) {
			resultError = new JSONRPC2Error(419, e.getMessage());
		}

		// Checks, which type of response will be sent.
		if (resultError != null) {
			return new JSONRPC2Response(resultError, id).toString();
		} else if (resultBoolean != null) {
			return new JSONRPC2Response(resultBoolean, id).toString();
		} else if (!resultString.equals("")) {
			return new JSONRPC2Response(resultString, id).toString();
		}
		return new JSONRPC2Response(resultMap, id).toString();

	}
}
