package es.udc.citytrash.model.camionModeloTipoDeBasura;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;

import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.util.GlobalNames;

/**
 * Tabla ModeloCamionTipoDeBasura
 * 
 * @author hmia
 *         http://www.codejava.net/frameworks/hibernate/hibernate-many-to-many-association-with-extra-columns-in-join-table-example
 *
 */
@Entity
@Table(name = GlobalNames.TBL_MODELO_CAMION_TIPO_DE_BASURA)
@AssociationOverrides({ @AssociationOverride(name = "pk.modelo", joinColumns = @JoinColumn(name = "MODELO_CAMION")),
		@AssociationOverride(name = "pk.tipo", joinColumns = @JoinColumn(name = "TIPO_BASURA")) })
public class CamionModeloTipoDeBasura implements Serializable {

	/**
	 * 
	 */
	CamionModeloTipoDeBasura() {

	}

	/**
	 * 
	 * @param modelo
	 *            modelo camion
	 * @param tipo
	 *            tipo de basura
	 */
	public CamionModeloTipoDeBasura(CamionModelo modelo, TipoDeBasura tipo, BigDecimal capacidad) {
		this.pk.setModelo(modelo);
		this.pk.setTipo(tipo);
		this.capacidad = capacidad;
	}

	@EmbeddedId
	public CamionModeloTipoDeBasuraPK getPk() {
		return pk;
	}

	public void setPk(CamionModeloTipoDeBasuraPK pk) {
		this.pk = pk;
	}

	@Column(name = "CAPACIDAD", nullable = false)
	@Digits(integer = 17, fraction = 2)
	public BigDecimal getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(BigDecimal capacidad) {
		this.capacidad = capacidad;

	}

	@Transient
	public CamionModelo getModelo() {
		return getPk().getModelo();
	}

	public void setModelo(CamionModelo modelo) {
		getPk().setModelo(modelo);
	}

	@Transient
	public TipoDeBasura getTipo() {
		return getPk().getTipo();
	}

	public void setTipo(TipoDeBasura tipo) {
		getPk().setTipo(tipo);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		CamionModeloTipoDeBasura that = (CamionModeloTipoDeBasura) o;

		if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}

	/**
	 * atributos
	 */
	private static final long serialVersionUID = 1L;
	// composite-id key
	private CamionModeloTipoDeBasuraPK pk = new CamionModeloTipoDeBasuraPK();
	// additional fields
	private BigDecimal capacidad;

	@Override
	public String toString() {
		return "CamionModeloTipoDeBasura [pk=" + pk + ", capacidad=" + capacidad + "]";
	}
}
