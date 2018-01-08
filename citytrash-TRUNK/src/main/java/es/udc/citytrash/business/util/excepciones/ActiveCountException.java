package es.udc.citytrash.business.util.excepciones;

public class ActiveCountException extends Exception {

	private static final long serialVersionUID = 881366370761819259L;

	public ActiveCountException(String email) {
		super(email);
	}
}
