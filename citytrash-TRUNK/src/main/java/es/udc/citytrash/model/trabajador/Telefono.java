package es.udc.citytrash.model.trabajador;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;

import es.udc.citytrash.util.enums.TipoTelefono;

// https://marcin-chwedczuk.github.io/introduction-to-hibernate-embeddable-types
/**
 * 
 * @author hmia
 *
 */

@Embeddable
public class Telefono implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -6550595058583546765L;

	public Telefono() {

	}

	public Telefono(String numero, TipoTelefono tipo) {
		// You can add validation here
		this.numero = numero;
		this.tipo = tipo;
	}

	@Pattern(regexp = "^([9|6|7][0-9]{8})?", message = "{constraints.pattern.telefono}")
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		if (numero != null)
			this.numero = numero.trim();
	}

	public TipoTelefono getTipo() {
		return tipo;
	}

	public void setTipo(TipoTelefono tipo) {
		this.tipo = tipo;
	}

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
		Telefono other = (Telefono) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Telefono [numero=" + numero + ", tipo=" + tipo + "]";
	}

	private String numero = "";

	@Enumerated(EnumType.STRING)
	private TipoTelefono tipo;

}
