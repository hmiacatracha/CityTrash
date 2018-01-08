package es.udc.citytrash.business.util.excepciones;

public class PasswordInvalidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String loginName;

	public PasswordInvalidException(String loginName) {
		super("Incorrect password exception => loginName = " + loginName);
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}
}
