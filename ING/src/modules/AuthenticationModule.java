package modules;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Map;

import modules.exceptions.AuthenticationException;
import modules.exceptions.InvalidParamValueException;
import modules.exceptions.InvalidParamsException;
import modules.exceptions.NotAuthenticatedException;
import modules.exceptions.OtherRpcException;
import modules.security.HashService;
import modules.security.Session;
import sql.actors.SQLCustomerService;
import sql.exceptions.SQLLayerException;

public class AuthenticationModule {
	private static ArrayList<Session> sessions;
	static {
		new Thread(new ExpiryChecker()).run();
	}

	public String getAuthToken(Map<String, Object> params)
			throws InvalidParamValueException, InvalidParamsException, AuthenticationException, OtherRpcException {
		if (params == null || params.size() != 2) {
			throw new InvalidParamsException("Either no or not enough params given.");
		}

		/**
		 * Check name: not empty
		 */
		String username = (String) params.get("username");
		if (username == null || username.equals("")) {
			throw new InvalidParamValueException("Name must not be empty or null");
		}

		/**
		 * Check password: not empty
		 */
		String password = (String) params.get("password");
		if (password == null || password.equals("")) {
			throw new InvalidParamValueException("Name must not be empty or null");
		}

		try {
			ResultSet cust = SQLCustomerService.getCustomerByUsername(username);
			String storedPass = cust.getString(cust.findColumn("Password"));
			String storedSalt = storedPass.split(":")[1];
			String givenPassHashed = HashService.hash(password, storedSalt);

			// Check if correct

			if (storedPass.equals(givenPassHashed)) {
				String token = createToken();
				sessions.add(new Session(username, token));
				return token;
			} else {
				throw new AuthenticationException("password is incorrect");
			}

		} catch (SQLLayerException e) {
			throw new AuthenticationException("username is incorrect");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new OtherRpcException("unexpected error");
	}

	private static String createToken() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		String token = bytes.toString();
		return token;
	}

	public static String checkToken(String token) throws NotAuthenticatedException {
		for (Session s : sessions) {
			if (s.getToken().equals(token)) {
				s.resetExpirationDate();
				return s.getUsername();
			}
		}
		throw new NotAuthenticatedException("Token is invalid.");
	}

	private static class ExpiryChecker implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					for (Session s : sessions) {
						if (s.isExpired()) {
							sessions.remove(s);
						}
					}
				} catch (ConcurrentModificationException e) {

				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
