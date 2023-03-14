package exceptions;

@SuppressWarnings("serial")
public class WrongCredentialsException extends Exception {

	public WrongCredentialsException(String message) {
		super(message);
	}
}