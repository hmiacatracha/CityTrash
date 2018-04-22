package es.udc.citytrash.model.trabajador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import es.udc.citytrash.util.GlobalNames;
import es.udc.citytrash.util.enums.Idioma;

/**
 * 
 * @author hmia documentacion => http://www.baeldung.com/hibernate-inheritance
 *         http://www.javatutoriales.com/2010/08/hibernate-parte-10-herencia.html
 *         https://www.adictosaltrabajo.com/tutoriales/hib-inheritance/
 *         https://www.arquitecturajava.com/jpa-single-table-inheritance/
 *         https://vladmihalcea.com/the-best-way-to-map-the-single_table-inheritance-with-jpa-and-hibernate/
 *         https://www.dineshonjava.com/hibernate/implementing-inheritance-in-hibernate/
 *         http://chuwiki.chuidiang.org/index.php?title=Herencia_con_Hibernate
 */

@Entity
@Table(name = GlobalNames.TBL_TRABAJ)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = GlobalNames.CAMPO_DISCRIMINADOR_BD, discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("NONE")
public abstract class Trabajador implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	Trabajador() {

	}

	/**
	 * 
	 * @param documento
	 * @param nombre
	 * @param apellidos
	 * @param email
	 */
	Trabajador(String documento, String nombre, String apellidos, String email) {
		this.docId = documento;
		this.nombre = nombre;
		this.email = email;
		this.apellidos = apellidos;
		this.trabajadorActivo = true;
		this.cuentaActiva = false;
	}

	/**
	 * Trabajador
	 * 
	 * @param id
	 * @param docId
	 * @param nombre
	 * @param apellidos
	 * @param fechaNacimiento
	 * @param email
	 * @param token
	 * @param fechaExpiracionToken
	 * @param idioma
	 * @param nombreVia
	 * @param numero
	 * @param piso
	 * @param puerta
	 * @param provincia
	 * @param localidad
	 * @param cp
	 * @param telefono
	 * @param restoDireccion
	 */
	protected Trabajador(String docId, String nombre, String apellidos, Calendar fechaNacimiento, String email,
			String token, Calendar fechaExpiracionToken, Idioma idioma, String nombreVia, Integer numero, Integer piso,
			String puerta, String provincia, String localidad, BigDecimal cp, BigDecimal telefono,
			String restoDireccion) {
		this.docId = docId;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.email = email;
		this.token = token;
		this.fechaExpiracionToken = fechaExpiracionToken;
		this.idioma = idioma;
		this.nombreVia = nombreVia;
		this.numero = numero;
		this.piso = piso;
		this.puerta = puerta;
		this.provincia = provincia;
		this.localidad = localidad;
		this.cp = cp;
		this.telefono = telefono;
		this.restoDireccion = restoDireccion;
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
	@Size(min = 0, max = 15)
	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	@NotBlank
	@Column(name = "NOMBRE")
	@Size(min = 1, max = 255)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@NotBlank
	@Column(name = "APELLIDOS")
	@Size(min = 1, max = 255)
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FEC_NAC")
	public Calendar getFecNac() {
		return fechaNacimiento;
	}

	public void setFecNac(Calendar fecNac) {
		this.fechaNacimiento = fecNac;
	}

	@NotBlank
	@Size(min = 1, max = 100)
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Size(max = 60)
	@Column(name = "CONTRASENA")
	public String getPassword() {
		return password;
	}

	public void setPassword(String encryptedPassword) {
		this.password = encryptedPassword;
	}

	@Size(max = 200)
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

	@Size(max = 255)
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

	@Digits(integer = 5, fraction = 0)
	@Column(name = "CP")
	public BigDecimal getCp() {
		return cp;
	}

	public void setCp(BigDecimal cp) {
		this.cp = cp;
	}

	@Digits(integer = 9, fraction = 0)
	@Column(name = "TELEFONO")
	public BigDecimal getTelefono() {
		return telefono;
	}

	public void setTelefono(BigDecimal telefono) {
		this.telefono = telefono;
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

	@Column(name = GlobalNames.CAMPO_ROL_BD)
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

	@Size(max = 100)
	@Column(name = "RESTO_DIRECCION")
	public String getRestoDireccion() {
		return restoDireccion;
	}

	public void setRestoDireccion(String resto_direccion) {
		this.restoDireccion = resto_direccion;
	}

	@Column(name = "PROVINCIA")
	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	@Column(name = "LOCALIDAD")
	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	@Column(name = "FECHA_ACTIVACION")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaActivacion() {
		return fechaActivacion;
	}

	public void setFechaActivacion(Calendar activacionCuenta) {
		this.fechaActivacion = activacionCuenta;
	}

	@Column(name = "FECHA_CREACION", updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Calendar fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Column(name = "FECHA_EXPIRACION_TOKEN")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaExpiracionToken() {
		return fechaExpiracionToken;
	}

	public void setFechaExpiracionToken(Calendar fechaToken) {
		this.fechaExpiracionToken = fechaToken;
	}

	@Column(name = "TRABAJADOR_TYPE", nullable = false, updatable = false, insertable = false)
	public String getTrabajadorType() {
		return trabajadorType;
	}

	public void setTrabajadorType(String trabajadorType) {
		this.trabajadorType = trabajadorType;
	}

	@Override
	public String toString() {
		return "Trabajador [id=" + id + ", docId=" + docId + ", nombre=" + nombre + ", apellidos=" + apellidos
				+ ", fechaNacimiento=" + fechaNacimiento + ", email=" + email + ", password=" + password + ", token="
				+ token + ", fechaExpiracionToken=" + fechaExpiracionToken + ", idioma=" + idioma + ", nombreVia="
				+ nombreVia + ", numero=" + numero + ", piso=" + piso + ", puerta=" + puerta + ", provincia="
				+ provincia + ", localidad=" + localidad + ", cp=" + cp + ", telefono=" + telefono + ", restoDireccion="
				+ restoDireccion + ", cuentaActiva=" + cuentaActiva + ", trabajadorActivo=" + trabajadorActivo
				+ ", fechaCreacion=" + fechaCreacion + ", fechaActivacion=" + fechaActivacion + ", rol=" + rol
				+ ", trabajadorType=" + trabajadorType + "]";
	}

	/* Atributos */
	private long id;
	private String docId;
	private String nombre;
	private String apellidos;
	private Calendar fechaNacimiento;
	private String email;
	private String password;
	private String token;
	private Calendar fechaExpiracionToken;
	private Idioma idioma;
	private String nombreVia;
	private Integer numero;
	private Integer piso;
	private String puerta;
	private String provincia;
	private String localidad;
	private BigDecimal cp;
	private BigDecimal telefono;
	private String restoDireccion;
	private Boolean cuentaActiva;
	private Boolean trabajadorActivo;
	public Calendar fechaCreacion;
	public Calendar fechaActivacion;
	private String rol;
	private String trabajadorType;

}
