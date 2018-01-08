package es.udc.citytrash.business.util.excepciones;

public class TokenInvalidException extends InstanceException {

	private static final long serialVersionUID = 8813633570761819259L;

	public TokenInvalidException(Object key, String className) {
		super("Token Invalido", key, className);
	}
}
