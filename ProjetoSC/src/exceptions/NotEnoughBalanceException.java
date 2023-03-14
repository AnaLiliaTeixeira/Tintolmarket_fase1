package exceptions;

@SuppressWarnings("serial")
public class NotEnoughBalanceException extends Exception {

	public NotEnoughBalanceException(String msg) {
		super(msg);
	}
}
