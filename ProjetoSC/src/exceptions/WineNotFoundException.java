package exceptions;

@SuppressWarnings("serial")
public class WineNotFoundException extends Exception {

	public WineNotFoundException(String msg) {
		super(msg);
	}

}
