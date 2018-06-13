package es.udc.citytrash.controller.util.dtos.trabajador;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;

import es.udc.citytrash.model.telefono.Telefono;

/**
 * 
 * @author hmia
 *
 */

public class TelefonoDto {

	/**
	 */
	private static final long serialVersionUID = -6550595058583546765L;

	public TelefonoDto(Telefono t) {

	}

	public TelefonoDto(String numero) {
		// You can add validation here
		this.numero = numero;
		// this.tipo = tipo;
	}

	@Pattern(regexp = "^(?=(?:[8-9]){1})(?=[0-9]{8}).*", message = "{error.telefono}")
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	/*
	 * public TipoTelefono getTipo() { return tipo; }
	 * 
	 * public void setTipo(TipoTelefono tipo) { this.tipo = tipo; }
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TelefonoDto other = (TelefonoDto) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Telefono [numero=" + numero + "]";
	}

	private String numero = "";

	/*
	 * @Enumerated(EnumType.STRING) private TipoTelefono tipo =
	 * TipoTelefono.HOME;
	 */
}
