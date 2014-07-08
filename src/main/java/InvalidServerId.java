public class InvalidServerId extends RuntimeException {

	public InvalidServerId(String message) {
		super(message);
	}

	public InvalidServerId(String message, NumberFormatException e) {
		super(message, e);
	}
	
}
