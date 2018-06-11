package es.udc.citytrash.model.telefonos;

import javax.validation.constraints.Pattern;

public class TelefonoDto {

	@Pattern(regexp = "^(?=(?:[8-9]){1})(?=[0-9]{8}).*", message = "{error.telefono}")
	String telephoneNr = "";

	public String getTelephoneNr() {
		return telephoneNr;
	}

	public void setTelephoneNr(String telephoneNr) {
		this.telephoneNr = telephoneNr;
	}
	
}
