package es.udc.citytrash.model.sensorValor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;

import es.udc.citytrash.model.sensor.Sensor;

/**
 * 
 * @author hmia
 *         https://jaehoo.wordpress.com/2012/03/25/hibernate-composite-keys-with-annotations/
 *         http://heinsohn.wikidot.com/articulos:llaves-primarias-compuestas-embebida
 */
@Entity
@Table(name = "TBL_VALORES_REALES")
public class Valor implements Serializable {

	/** @generated */
	private static final long serialVersionUID = 1L;

	/** Metodo constructor, no lleva argumentos */
	public Valor() {

	}

	public Valor(BigInteger sensorId, Calendar fechaHora, BigDecimal valor) {
		super();
		this.valor = valor;
		this.pk = new ValorPk(sensorId, fechaHora);
	}

	@EmbeddedId
	// metodo getter que representa la llave primaria compuesta
	public ValorPk getPk() {
		if (this.pk == null) {
			this.pk = new ValorPk();
		}
		return this.pk;
	}

	/** Metodo setter que representa la llave primaria compuesta */
	protected void setPk(ValorPk pk) {
		this.pk = pk;
	}

	@Column(name = "VALOR")
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "SENSOR_ID", insertable = false, updatable = false)
	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	private ValorPk pk;
	private BigDecimal valor;
	private Sensor sensor;

	@Override
	public String toString() {
		return "Valor [pk=" + pk + ", valor=" + valor + ", sensor=" + sensor + "]";
	}

}

