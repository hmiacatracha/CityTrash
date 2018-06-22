package es.udc.citytrash.model.camionModeloTipoDeBasura;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;

/**
 * 
 * @author hmia PRIMARY KEY de la tabla ModeloCamionTipoDeBasura
 *
 */
@Embeddable
public class CamionModeloTipoDeBasuraPK implements Serializable {

	CamionModeloTipoDeBasuraPK() {

	}

	public CamionModeloTipoDeBasuraPK(CamionModelo modelo, TipoDeBasura tipo) {
		this.modelo = modelo;
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
	public CamionModelo getModelo() {
		return modelo;
	}

	public void setModelo(CamionModelo modeloCamion) {
		this.modelo = modeloCamion;
	}

	@Override
	public int hashCode() {
		int result;
		result = (tipo != null ? tipo.hashCode() : 0);
		result = 31 * result + (modelo != null ? modelo.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CamionModeloTipoDeBasuraPK that = (CamionModeloTipoDeBasuraPK) o;
		if (tipo != null ? !tipo.equals(that.tipo) : that.tipo != null)
			return false;
		if (modelo != null ? !modelo.equals(that.modelo) : that.modelo != null)
			return false;
		return true;
	}

	/**
	 * atributos
	 */
	private static final long serialVersionUID = 1L;

	private TipoDeBasura tipo;
	private CamionModelo modelo;

}
