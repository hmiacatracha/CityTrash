package es.udc.citytrash.model.contenedor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.sensor.Sensor;
import es.udc.citytrash.util.GlobalNames;

/**
 * Contenedor
 * 
 * @author hmia
 *
 */

@Entity
@Table(name = GlobalNames.TBL_CONTENEDOR)
@BatchSize(size = 20)
public class Contenedor implements Serializable {

	/**
	 * 
	 */
	public Contenedor() {

	}

	/**
	 * Construcor con campos obligatorios
	 * 
	 * @param nombre
	 * @param modelo
	 */
	public Contenedor(String nombre, ContenedorModelo modelo) {
		this.nombre = nombre;
		this.modelo = modelo;
		this.activo = true;
		this.setFechaBaja(null);
	}

	/**
	 * Constructor con todos los campos
	 * 
	 * @param nombre
	 * @param latitud
	 * @param longitud
	 * @param fechaAlta
	 * @param fechaBaja
	 * @param modelo
	 * @param activo
	 */
	public Contenedor(String nombre, BigDecimal latitud, BigDecimal longitud, Calendar fechaAlta, Calendar fechaBaja,
			ContenedorModelo modelo, Boolean activo) {
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
		this.fechaAlta = fechaAlta;
		this.fechaBaja = fechaBaja;
		this.modelo = modelo;
		this.activo = activo;
	}

	@Id
	@Column(name = "CONTENEDOR_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ContenedorIdGenerator")
	@GenericGenerator(name = "ContenedorIdGenerator", strategy = "native")
	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	@Size(min = 0, max = 255)
	@Column(name = "NOMBRE", unique = true, nullable = true)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre.toUpperCase().trim();
	}

	@Column(name = "FECHA_ALTA", updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Calendar fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Column(name = "FECHA_BAJA", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Calendar fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "MODELO_CONTENEDOR")
	public ContenedorModelo getModelo() {
		return modelo;
	}

	public void setModelo(ContenedorModelo modelo) {
		this.modelo = modelo;
	}

	@Column(name = "LOCALIZACION_LATITUD", nullable = true)
	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	@Column(name = "LOCALIZACION_LONGITUD", nullable = true)
	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	@Column(name = "ACTIVO", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	@OneToMany(mappedBy = "contenedor")
	public List<Sensor> getSensores() {
		return sensores;
	}

	public void setSensores(List<Sensor> sensores) {
		this.sensores = sensores;
	}

	public void addSensor(Sensor sensor) {
		this.sensores.add(sensor);
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "RUTA_ID")
	public Ruta getRuta() {
		return ruta;
	}
	
	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}

	/* Atributos */
	private static final long serialVersionUID = 1L;
	private long id;
	private String nombre;
	private BigDecimal latitud;
	private BigDecimal longitud;
	private Calendar fechaAlta;
	private Calendar fechaBaja;
	private ContenedorModelo modelo;
	private Boolean activo;
	private List<Sensor> sensores = new ArrayList<Sensor>();
	private Ruta ruta;
	
	@Override
	public String toString() {
		return "Contenedor [id=" + id + ", nombre=" + nombre + ", latitud=" + latitud + ", longitud=" + longitud
				+ ", fechaAlta=" + fechaAlta + ", fechaBaja=" + fechaBaja + ", modelo=" + modelo + ", activo=" + activo
				+ "]";
	}
}
