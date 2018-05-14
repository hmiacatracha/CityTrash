package es.udc.citytrash.controller.util.dtos.trabajador;

import java.math.BigDecimal;
import java.util.Calendar;

import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.util.enums.Idioma;
import es.udc.citytrash.util.enums.TipoTrabajador;

public class TrabajadorDto {

	/**
	 * Default
	 */
	public TrabajadorDto() {

	}

	/**
	 * 
	 * @param t
	 *            Trabajador
	 */
	public TrabajadorDto(Trabajador t) {
		this.id = t.getId();
		this.documento = t.getDocId();
		this.nombre = t.getNombre();
		this.apellidos = t.getApellidos();
		this.fechaNacimiento = t.getFecNac();
		this.email = t.getEmail();
		this.token = t.getToken();
		this.fechaExpiracionToken = t.getFechaExpiracionToken();
		this.idioma = t.getIdioma();
		this.nombreVia = t.getNombreVia();
		this.numero = t.getNumero();
		this.piso = t.getPiso();
		this.puerta = t.getPuerta();
		this.provincia = t.getProvincia();
		this.localidad = t.getLocalidad();
		this.cp = t.getCp();
		this.telefono = t.getTelefono();
		this.restoDireccion = t.getRestoDireccion();
		this.cuentaActiva = t.isEnabledCount();
		this.trabajadorActivo = t.isActiveWorker();
		this.fechaCreacion = t.getFechaCreacion();
		this.fechaActivacion = t.getFechaActivacion();
		this.rol = t.getRol();

		if (t.getTrabajadorType() != null) {
			switch (TipoTrabajador.valueOf(t.getTrabajadorType())) {
			case RECOLEC:
				this.tipo = TipoTrabajador.RECOLEC;
				break;
			case CONDUCT:
				this.tipo = TipoTrabajador.CONDUCT;
				break;
			case ADMIN:
				this.tipo = TipoTrabajador.ADMIN;
				break;
			default:
				this.tipo = TipoTrabajador.NONE;
				break;
			}
		} else
			this.tipo = TipoTrabajador.NONE;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Calendar getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Calendar fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Calendar getFechaExpiracionToken() {
		return fechaExpiracionToken;
	}

	public void setFechaExpiracionToken(Calendar fechaExpiracionToken) {
		this.fechaExpiracionToken = fechaExpiracionToken;
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}

	public String getNombreVia() {
		return nombreVia;
	}

	public void setNombreVia(String nombreVia) {
		this.nombreVia = nombreVia;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getPiso() {
		return piso;
	}

	public void setPiso(Integer piso) {
		this.piso = piso;
	}

	public String getPuerta() {
		return puerta;
	}

	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public BigDecimal getCp() {
		return cp;
	}

	public void setCp(BigDecimal cp) {
		this.cp = cp;
	}

	public BigDecimal getTelefono() {
		return telefono;
	}

	public void setTelefono(BigDecimal telefono) {
		this.telefono = telefono;
	}

	public String getRestoDireccion() {
		return restoDireccion;
	}

	public void setRestoDireccion(String restoDireccion) {
		this.restoDireccion = restoDireccion;
	}

	public Boolean getCuentaActiva() {
		return cuentaActiva;
	}

	public void setCuentaActiva(Boolean cuentaActiva) {
		this.cuentaActiva = cuentaActiva;
	}

	public Boolean getTrabajadorActivo() {
		return trabajadorActivo;
	}

	public void setTrabajadorActivo(Boolean trabajadorActivo) {
		this.trabajadorActivo = trabajadorActivo;
	}

	public Calendar getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Calendar fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Calendar getFechaActivacion() {
		return fechaActivacion;
	}

	public void setFechaActivacion(Calendar fechaActivacion) {
		this.fechaActivacion = fechaActivacion;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public TipoTrabajador getTipo() {
		return tipo;
	}

	public void setTipo(TipoTrabajador tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "TrabajadorDto [id=" + id + ", documento=" + documento + ", nombre=" + nombre + ", apellidos="
				+ apellidos + ", fechaNacimiento=" + fechaNacimiento + ", email=" + email + ", token=" + token
				+ ", fechaExpiracionToken=" + fechaExpiracionToken + ", idioma=" + idioma + ", nombreVia=" + nombreVia
				+ ", numero=" + numero + ", piso=" + piso + ", puerta=" + puerta + ", provincia=" + provincia
				+ ", localidad=" + localidad + ", cp=" + cp + ", telefono=" + telefono + ", restoDireccion="
				+ restoDireccion + ", cuentaActiva=" + cuentaActiva + ", trabajadorActivo=" + trabajadorActivo
				+ ", fechaCreacion=" + fechaCreacion + ", fechaActivacion=" + fechaActivacion + ", rol=" + rol
				+ ", tipo=" + tipo + "]";
	}

	private long id;
	private String documento;
	private String nombre;
	private String apellidos;
	private Calendar fechaNacimiento;
	private String email;
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
	private TipoTrabajador tipo;

}
