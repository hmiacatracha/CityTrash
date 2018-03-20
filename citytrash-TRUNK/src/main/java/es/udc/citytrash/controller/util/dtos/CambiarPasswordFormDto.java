package es.udc.citytrash.controller.util.dtos;

import org.hibernate.validator.constraints.NotBlank;

import es.udc.citytrash.controller.util.anotaciones.CamposIguales;

@CamposIguales(primerCampo = "password", segundoCampo = "repetirPassword", message = "{fieldMatch_password_alerta}")
public class CambiarPasswordFormDto {

	private static final String NO_BLACK_MENSAJE = "{notBlank_alerta}";

	@NotBlank(message = NO_BLACK_MENSAJE)
	private String password;

	@NotBlank(message = NO_BLACK_MENSAJE)
	private String repetirPassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String email) {
		this.password = email;
	}

	public String getRepetirPassword() {
		return repetirPassword;
	}

	public void setRepetirPassword(String password) {
		this.repetirPassword = password;
	}

	@Override
	public String toString() {
		return "ActivarCuentaForm [password=" + password + ", repetirPassword=" + repetirPassword + "]";
	}
}
