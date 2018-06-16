package es.udc.citytrash.model.sensorValor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable // Indica que la clase es embebida
// Implementa la clase Serializable
public class ValorPk implements Serializable {

	public ValorPk() {

	}

	public ValorPk(BigInteger sensorId, Calendar fechaHora) {
		super();
		this.sensorId = sensorId;
		this.fechaHora = fechaHora;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2568547818499912901L;

	@Column(name = "SENSOR_ID")
	public BigInteger getSensorId() {
		return sensorId;
	}

	public void setSensorId(BigInteger sensorId) {
		this.sensorId = sensorId;
	}

	@Column(name = "FECHA_HORA")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Calendar fechaHora) {
		this.fechaHora = fechaHora;
	}

	private BigInteger sensorId;
	private Calendar fechaHora;

	@Override
	public int hashCode() {
		return (int) (fechaHora.hashCode() + sensorId.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ValorPk))
			return false;
		ValorPk pk = (ValorPk) obj;
		return pk.sensorId == sensorId && pk.fechaHora.equals(fechaHora);
	}

	@Override
	public String toString() {
		return "ValorPk [sensorId=" + sensorId + ", fechaHora=" + fechaHora + "]";
	}
}
