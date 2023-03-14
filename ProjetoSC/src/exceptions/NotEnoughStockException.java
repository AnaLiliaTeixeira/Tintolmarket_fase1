package exceptions;

@SuppressWarnings("serial")
public class NotEnoughStockException extends Exception {

	public NotEnoughStockException(String msg) {
		super(msg);
	}

}

