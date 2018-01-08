package es.udc.citytrash.controller.util.formularios;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class LoginForm {

	private static final String NO_BLACK_MENSAJE = "{notBlank_alerta}";
	private static final String EMAIL_MENSAJE = "{email_alerta}";

	@NotBlank(message = NO_BLACK_MENSAJE)
	@Email(message = EMAIL_MENSAJE)
	private String email;

	@NotBlank(message = NO_BLACK_MENSAJE)
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "loginForm [email=" + email + ", password=" + password + "]";
	}
}
