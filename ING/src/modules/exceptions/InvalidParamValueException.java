package modules.exceptions;

public class InvalidParamValueException extends JsonRpcException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3518419486007409961L;

	public InvalidParamValueException(String s) {
		super(s);
	}
}
