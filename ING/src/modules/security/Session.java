package modules.security;

import java.time.LocalDateTime;

public class Session {
	private static final int VALID_FOR_MIN = 5;

	private String username;
	private String token;
	private LocalDateTime expirationDate;

	public Session(String username, String token) {
		this.username = username;
		this.token = token;
		resetExpirationDate();
	}

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expirationDate);
	}

	public void resetExpirationDate() {
		expirationDate = LocalDateTime.now().plusMinutes(VALID_FOR_MIN);
	}

	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}
}
