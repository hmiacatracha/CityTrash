package es.udc.citytrash.business.entity.trabajador;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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
	 */
	Trabajador(String documento, String nombre, String apellidos, String rol, String email, String token) {
		this.docId = documento;
		this.nombre = nombre;
		this.rol = rol;
		this.email = email;
		this.apellidos = apellidos;
		this.token = token;
		this.activeWorker = true;
		this.enableCount = false;
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

	@Column(name = "CODIGO_RECUPERACION")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "IDIOMA")
	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
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
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
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
	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	@Column(name = "CUENTA_HABILITADA", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isEnabledCount() {
		return enableCount;
	}

	public void setEnabledCount(boolean enabled) {
		this.enableCount = enabled;
	}

	@Column(name = "TRABAJADOR_ACTIVO", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isActiveWorker() {
		return activeWorker;
	}

	public void setActiveWorker(boolean activeWorker) {
		this.activeWorker = activeWorker;
	}

	@Column(name = "ROL")
	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	@Column(name = "PISO")
	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	@Column(name = "RESTO_DIRECCION")
	public String getRestoDireccion() {
		return restoDireccion;
	}

	public void setRestoDireccion(String resto_direccion) {
		this.restoDireccion = resto_direccion;
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
	private String idioma;
	private String nombreVia;
	private int numero;
	private String piso;
	private String puerta;
	private int cp;
	private String restoDireccion;
	private boolean enableCount;
	private boolean activeWorker;
	private String rol;
}
