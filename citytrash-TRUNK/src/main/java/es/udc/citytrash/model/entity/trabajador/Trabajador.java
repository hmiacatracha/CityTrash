package es.udc.citytrash.model.entity.trabajador;

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

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TBL_TRABAJADORES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TRABAJADOR_TYPE", discriminatorType = DiscriminatorType.STRING)
@BatchSize(size = 10)
public abstract class Trabajador implements Serializable {

	private static final long serialVersionUID = 1L;

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

	@Column(name = "DOC_TIPO")
	public String getDocTipo() {
		return docTipo;
	}

	public void setDocTipo(String docTipo) {
		this.docTipo = docTipo;
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
	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	@Column(name = "CODIGO_RECUPERACION")
	public String getCodigoRec() {
		return codigoRec;
	}

	public void setCodigoRec(String codigoRec) {
		this.codigoRec = codigoRec;
	}

	@Column(name = "IDIOMA")
	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	@Column(name = "TIPO_VIA")
	public String getTipoVia() {
		return tipoVia;
	}

	public void setTipoVia(String tipoVia) {
		this.tipoVia = tipoVia;
	}

	@Column(name = "NOMBRE_VIA")
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

	@Column(name = "HABILITADA")
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/* Atributos */
	private long id;
	private String docTipo;
	private String docId;
	private String nombre;
	private String apellidos;
	private Calendar fecNac;
	private String email;
	private String encryptedPassword;
	private String codigoRec;
	private String idioma;
	private String tipoVia;
	private String nombreVia;
	private int numero;
	private String puerta;
	private int cp;
	private boolean enabled;
}
