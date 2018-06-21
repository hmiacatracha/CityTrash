package es.udc.citytrash.controller.util.dtos.cuenta;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import es.udc.citytrash.controller.util.anotaciones.CamposIguales;

@CamposIguales(primerCampo = "password", segundoCampo = "repetirPassword", message = "{constraints.fieldmatch.password}")
public class CambiarPasswordFormDto {

	@NotNull
	private String passwordAntigua = "";

	@Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "{constraints.pattern.password}")
	@NotNull
	private String password;

	@NotNull
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

	public String getPasswordAntigua() {
		return passwordAntigua;
	}

	public void setPasswordAntigua(String passwordAntigua) {
		this.passwordAntigua = passwordAntigua;
	}

	@Override
	public String toString() {
		return "ActualizarPasswordFormDto [passwordAntigua=" + passwordAntigua + ", password=" + password
				+ ", repetirPassword=" + repetirPassword + "]";
	}
}
