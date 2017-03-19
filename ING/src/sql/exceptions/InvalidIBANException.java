package sql.exceptions;

public class InvalidIBANException extends InvalidParameterTypeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2313635040848708306L;

	public InvalidIBANException(String s) {
		super(s);
	}
}
