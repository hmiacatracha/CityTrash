package es.udc.citytrash.business.util.excepciones;

public class InactiveCountException extends Exception {

	private static final long serialVersionUID = 881363370761819259L;

	public InactiveCountException(String email) {
		super(email);
	}
}
