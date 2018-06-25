package es.udc.citytrash.model.rutaTipoDeBasura;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;

/**
 * 
 * @author hmia PRIMARY KEY de la tabla RutaTipoDeBasura
 *
 */
@Embeddable
public class RutaTipoDeBasuraPK implements Serializable {

	RutaTipoDeBasuraPK() {

	}

	public RutaTipoDeBasuraPK(Ruta ruta, TipoDeBasura tipo) {
		this.ruta = ruta;
		this.tipo = tipo;
	}

	// metodo getter del atributo que representa la PRIMERA llave primaria
	@ManyToOne(cascade = CascadeType.ALL)
	public TipoDeBasura getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeBasura tipo) {
		this.tipo = tipo;
	}

	// metodo getter del atributo que representa la SEGUNDA llave primaria
	@ManyToOne(cascade = CascadeType.ALL)
	public Ruta getRuta() {
		return ruta;
	}

	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}

	@Override
	public int hashCode() {
		int result;
		result = (tipo != null ? tipo.hashCode() : 0);
		result = 31 * result + (ruta != null ? ruta.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RutaTipoDeBasuraPK that = (RutaTipoDeBasuraPK) o;
		if (tipo != null ? !tipo.equals(that.tipo) : that.tipo != null)
			return false;
		if (ruta != null ? !ruta.equals(that.ruta) : that.ruta != null)
			return false;
		return true;
	}

	/**
	 * atributos
	 */
	private static final long serialVersionUID = 1L;
	private TipoDeBasura tipo;
	private Ruta ruta;
}
