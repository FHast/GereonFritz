package sql.exceptions;

import modules.exceptions.InvalidParamValueException;

public class InvalidParameterTypeException extends InvalidParamValueException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8451204481971265685L;
	
	public InvalidParameterTypeException(String s) {
		super(s);
	}

}
