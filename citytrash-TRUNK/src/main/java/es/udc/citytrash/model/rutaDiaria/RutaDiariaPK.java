package es.udc.citytrash.model.rutaDiaria;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import es.udc.citytrash.model.ruta.Ruta;

@Embeddable
public class RutaDiariaPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 75482644168028482L;

	RutaDiariaPK() {

	}

	public RutaDiariaPK(Ruta ruta, Calendar fecha) {
		this.ruta = ruta;
		this.fecha = fecha;
	}

	@Column(name = "RUTA_ID")
	public Ruta getRuta() {
		return ruta;
	}

	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}

	@Column(name = "FECHA")
	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	@Override
	public int hashCode() {
		int result;
		result = (ruta != null ? ruta.hashCode() : 0);
		result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RutaDiariaPK that = (RutaDiariaPK) o;
		if (ruta != null ? !ruta.equals(that.ruta) : that.ruta != null)
			return false;
		if (fecha != null ? !fecha.equals(that.fecha) : that.fecha != null)
			return false;

		return true;
	}

	private Ruta ruta;
	private Calendar fecha;

}
