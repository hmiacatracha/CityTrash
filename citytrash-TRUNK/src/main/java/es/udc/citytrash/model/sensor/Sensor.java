package es.udc.citytrash.model.sensor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import es.udc.citytrash.model.sensorValor.*;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.util.GlobalNames;

/**
 * 
 * @author hmia
 *
 */
@Entity
@Table(name = "TBL_SENSORES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = GlobalNames.CAMPO_SENSOR_DISCRIMINADOR, discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("NONE")
@BatchSize(size = 10)
public abstract class Sensor implements Serializable {
	/**
	 * 
	 */
	Sensor() {
	}

	/**
	 * 
	 * @param nombre
	 *            nombre
	 * @param contenedor
	 *            contenedor
	 * @param activo
	 *            activo
	 */
	public Sensor(String nombre, Contenedor contenedor, boolean activo) {
		this.nombre = nombre;
		this.contenedor = contenedor;
	}

	@Id
	@Column(name = "SENSOR_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SensorIdGenerator")
	@GenericGenerator(name = "SensorIdGenerator", strategy = "native")
	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NOMBRE")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTENEDOR")
	public Contenedor getContenedor() {
		return contenedor;
	}

	public void setContenedor(Contenedor contenedor) {
		this.contenedor = contenedor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ULTIMA_ACTUALIZACION")
	public Calendar getUltimaActualizacion() {
		return ultimaActualizacion;
	}

	public void setUltimaActualizacion(Calendar ultimaActualizacion) {
		this.ultimaActualizacion = ultimaActualizacion;
	}

	@Column(name = "ACTIVO")
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@Column(name = "SENSOR_TIPO", nullable = false, updatable = false, insertable = false)
	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

	@OneToMany(mappedBy = "sensor")
	public List<Valor> getValores() {
		return valores;
	}

	public void setValores(List<Valor> valores) {
		this.valores = valores;
	}

	private static final long serialVersionUID = 1L;
	private Long id;
	private String nombre;
	private Contenedor contenedor;
	private Calendar ultimaActualizacion;
	private boolean activo;
	private String sensorType;
	private List<Valor> valores;

	@Override
	public String toString() {
		return "Sensor [id=" + id + ", nombre=" + nombre + ", contenedor=" + contenedor + ", ultimaActualizacion="
				+ ultimaActualizacion + ", activo=" + activo + ", sensorType=" + sensorType + "]";
	}
}
