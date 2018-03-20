package es.udc.citytrash.business.entity.trabajador;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import es.udc.citytrash.business.entity.idioma.Idioma;

@Entity
@Table(name = "TBL_TRABAJADORES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TRABAJADOR_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Trabajador implements Serializable {

	private static final long serialVersionUID = 1L;

	Trabajador() {

	}

	/***
	 * Trabajador
	 * 
	 * @param documento
	 * @param nombre
	 * @param apellidos
	 * @param rol
	 * @param email
	 * @param token
	 * @param fechaExpiracionToken
	 */
	Trabajador(String documento, String nombre, String apellidos, String rol, String email, String token,
			Calendar fechaExpiracionToken) {
		this.docId = documento;
		this.nombre = nombre;
		this.rol = rol;
		this.email = email;
		this.apellidos = apellidos;
		this.token = token;
		this.trabajadorActivo = true;
		this.cuentaActiva = false;
		this.fechaExpiracionToken = fechaExpiracionToken;
	}

	Trabajador(String documento, String nombre, String apellidos, String rol, String email) {
		this.docId = documento;
		this.nombre = nombre;
		this.rol = rol;
		this.email = email;
		this.apellidos = apellidos;
		this.trabajadorActivo = true;
		this.cuentaActiva = false;
	}

	@Id
	@Column(name = "TRABAJADOR_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "TrabajadorIdGenerator")
	@GenericGenerator(name = "TrabajadorIdGenerator", strategy = "native")
	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	@Column(name = "DOC_ID")
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	@Column(name = "NOMBRE")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "APELLIDOS")
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FEC_NAC")
	public Calendar getFecNac() {
		return fecNac;
	}

	public void setFecNac(Calendar fecNac) {
		this.fecNac = fecNac;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "CONTRASENA")
	public String getPassword() {
		return password;
	}

	public void setPassword(String encryptedPassword) {
		this.password = encryptedPassword;
	}

	@Column(name = "TOKEN")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/*
	 * @Enumerated =>
	 * http://tomee.apache.org/examples-trunk/jpa-enumerated/README.html
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "IDIOMA")
	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}

	@Column(name = "VIA")
	public String getNombreVia() {
		return nombreVia;
	}

	public void setNombreVia(String nombreVia) {
		this.nombreVia = nombreVia;
	}

	@Column(name = "NUMERO")
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Column(name = "PUERTA")
	public String getPuerta() {
		return puerta;
	}

	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}

	@Column(name = "CP")
	public Integer getCp() {
		return cp;
	}

	public void setCp(Integer cp) {
		this.cp = cp;
	}

	@Column(name = "CUENTA_HABILITADA", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean isEnabledCount() {
		return cuentaActiva;
	}

	public void setEnabledCount(Boolean enabled) {
		this.cuentaActiva = enabled;
	}

	@Column(name = "TRABAJADOR_ACTIVO", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean isActiveWorker() {
		return trabajadorActivo;
	}

	public void setActiveWorker(Boolean activeWorker) {
		this.trabajadorActivo = activeWorker;
	}

	@Column(name = "ROL")
	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	@Column(name = "PISO")
	public Integer getPiso() {
		return piso;
	}

	public void setPiso(Integer piso) {
		this.piso = piso;
	}

	@Column(name = "RESTO_DIRECCION")
	public String getRestoDireccion() {
		return restoDireccion;
	}

	public void setRestoDireccion(String resto_direccion) {
		this.restoDireccion = resto_direccion;
	}

	@Column(name = "PROVINCIA")
	public String getProvincia() {
		return Provincia;
	}

	public void setProvincia(String provincia) {
		Provincia = provincia;
	}

	@Column(name = "LOCALIDAD")
	public String getLocalidad() {
		return Localidad;
	}

	public void setLocalidad(String localidad) {
		Localidad = localidad;
	}

	@Column(name = "FECHA_ACTIVACION", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaActivacion() {
		return fechaActivacion;
	}

	public void setFechaActivacion(Calendar activacionCuenta) {
		this.fechaActivacion = activacionCuenta;
	}

	@Column(name = "FECHA_CREACION", updatable = false)
	@CreationTimestamp
	public Calendar getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Calendar fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Column(name = "FECHA_EXPIRACION_TOKEN", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaExpiracionToken() {
		return fechaExpiracionToken;
	}

	public void setFechaExpiracionToken(Calendar fechaToken) {
		this.fechaExpiracionToken = fechaToken;
	}

	/* Atributos */
	private long id;
	private String docId;
	private String nombre;
	private String apellidos;
	private Calendar fecNac;
	private String email;
	private String password;
	private String token;
	private Calendar fechaExpiracionToken;
	private Idioma idioma;
	private String nombreVia;
	private Integer numero;
	private Integer piso;
	private String puerta;
	private String Provincia;
	private String Localidad;
	private Integer cp;
	private String restoDireccion;
	private Boolean cuentaActiva;
	private Boolean trabajadorActivo;
	public Calendar fechaCreacion;
	public Calendar fechaActivacion;
	private String rol;
}
