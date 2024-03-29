package es.udc.citytrash.model.util.excepciones;

public class PasswordInvalidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String loginName;

	public PasswordInvalidException(String loginName) {
		super(loginName);
		this.loginName = loginName;
	}

	public String getLoginName() {
		return loginName;
	}
}
