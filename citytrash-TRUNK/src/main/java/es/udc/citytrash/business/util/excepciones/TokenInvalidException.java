package es.udc.citytrash.business.util.excepciones;

public class TokenInvalidException extends Exception {

	private static final long serialVersionUID = 8813633570761819259L;

	Object key = "";

	public TokenInvalidException(Object key, String className) {
		super("Token Invalido => " + key);
		this.key = key;
	}

	public Object getKey() {
		return key;
	}
}
