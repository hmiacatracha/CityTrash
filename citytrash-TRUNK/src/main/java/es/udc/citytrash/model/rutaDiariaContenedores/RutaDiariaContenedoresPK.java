package es.udc.citytrash.model.rutaDiariaContenedores;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.rutaDiaria.RutaDiaria;

@Embeddable
public class RutaDiariaContenedoresPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 75482644168028482L;

	RutaDiariaContenedoresPK() {

	}

	/**
	 * 
	 * @param ruta
	 * @param contenedor
	 */
	public RutaDiariaContenedoresPK(RutaDiaria ruta, Contenedor contenedor) {
		this.rutaDiaria = ruta;
		this.contenedor = contenedor;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	public RutaDiaria getRutaDiaria() {
		return rutaDiaria;
	}

	public void setRutaDiaria(RutaDiaria rutaDiaria) {
		this.rutaDiaria = rutaDiaria;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	public Contenedor getContenedor() {
		return contenedor;
	}

	public void setContenedor(Contenedor contenedor) {
		this.contenedor = contenedor;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contenedor == null) ? 0 : contenedor.hashCode());
		result = prime * result + ((rutaDiaria == null) ? 0 : rutaDiaria.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RutaDiariaContenedoresPK that = (RutaDiariaContenedoresPK) o;
		if (rutaDiaria != null ? !rutaDiaria.equals(that.rutaDiaria) : that.rutaDiaria != null)
			return false;
		if (contenedor != null ? !contenedor.equals(that.contenedor) : that.contenedor != null)
			return false;
		return true;
	}

	private RutaDiaria rutaDiaria;
	private Contenedor contenedor;
}
