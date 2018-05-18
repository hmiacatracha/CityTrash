package es.udc.citytrash.model.camion;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.util.GlobalNames;

/**
 * Camion
 * 
 * @author hmia
 *
 */

@Entity
@Table(name = GlobalNames.TBL_CAMION)
@BatchSize(size = 10)
public class Camion implements Serializable {

	/**
	 * 
	 */
	Camion() {

	}

	/**
	 * 
	 * @param nombre
	 *            nombre
	 * @param modelo
	 *            modelo
	 */
	public Camion(String nombre, CamionModelo modelo) {
		this.nombre = nombre;
		this.modeloCamion = modelo;
		this.setActivo(true);
		this.setFechaBaja(null);
	}

	@Id
	@Column(name = "CAMION_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "CamionIdGenerator")
	@GenericGenerator(name = "CamionIdGenerator", strategy = "native")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Size(min = 0, max = 17)
	@Column(name = "VIN", unique = true, nullable = true)
	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	@Size(min = 0, max = 255)
	@Column(name = "NOMBRE", unique = true, nullable = true)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre.toUpperCase().trim();
	}

	@Size(min = 0, max = 20)
	@Column(name = "MATRICULA", unique = true, nullable = true)
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
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
	@JoinColumn(name = "MODELO_CAMION")
	public CamionModelo getModeloCamion() {
		return modeloCamion;
	}

	public void setModeloCamion(CamionModelo modeloCamion) {
		this.modeloCamion = modeloCamion;
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "RECOGEDOR_ASIGNADO1")
	public Trabajador getRecogedor1() {
		return recogedor1;
	}

	public void setRecogedor1(Trabajador recogedor) {
		this.recogedor1 = recogedor;
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "RECOGEDOR_ASIGNADO2")
	public Trabajador getRecogedor2() {
		return recogedor2;
	}

	public void setRecogedor2(Trabajador recogedor) {
		this.recogedor2 = recogedor;
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CODUCTOR_ASIGNADO")
	public Trabajador getConductor() {
		return conductor;
	}

	public void setConductor(Trabajador conductor) {
		this.conductor = conductor;
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CONDUCTOR_SUPLENTE")
	public Trabajador getConductorSuplente() {
		return conductorSuplente;
	}

	public void setConductorSuplente(Trabajador conductorSuplente) {
		this.conductorSuplente = conductorSuplente;
	}

	@Column(name = "ACTIVO", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	/* Atributos */
	private static final long serialVersionUID = 1L;
	private long id;
	private String vin;
	private String nombre;
	private String matricula;
	private Calendar fechaAlta;
	private Calendar fechaBaja;
	private CamionModelo modeloCamion;
	private Trabajador recogedor1;
	private Trabajador recogedor2;
	private Trabajador conductor;
	private Trabajador conductorSuplente;
	private Boolean activo;
}
