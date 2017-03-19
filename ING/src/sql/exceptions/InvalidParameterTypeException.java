package sql.exceptions;

import services.exceptions.InvalidParameterException;

public class InvalidParameterTypeException extends InvalidParameterException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8451204481971265685L;
	
	public InvalidParameterTypeException(String s) {
		super(s);
	}

}
