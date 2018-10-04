package es.udc.citytrash.util.dtos.usuario;

import es.udc.citytrash.util.enums.Idioma;

public class Usuario {

	private long id;
	private String nombre;
	private String apellidos;
	private String email;
	private String password;
	private Idioma idioma;
	private Boolean cuentaActiva;
	private Boolean trabajadorActivo;

	public Usuario() {

	}

	public Usuario(Long id, String nombre, String apellidos, String email, String password, Idioma idioma,
			Boolean cuentaActiva, Boolean trabajadorActivo) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.password = password;
		this.cuentaActiva = cuentaActiva;
		this.trabajadorActivo = trabajadorActivo;
		this.idioma = idioma;
	}

	public Usuario(String nombre, String apellidos, String email, String password, Idioma idioma, Boolean cuentaActiva,
			Boolean trabajadorActivo) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.password = password;
		this.cuentaActiva = cuentaActiva;
		this.trabajadorActivo = trabajadorActivo;
		this.idioma = idioma;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isCuentaActiva() {
		return cuentaActiva;
	}

	public void setCuentaActiva(Boolean cuentaActiva) {
		this.cuentaActiva = cuentaActiva;
	}

	public Boolean getCuentaActiva() {
		return cuentaActiva;
	}

	public void setTrabajadorActivo(Boolean trabajadorActivo) {
		this.trabajadorActivo = trabajadorActivo;
	}

	public Boolean getTrabajadorActivo() {
		return trabajadorActivo;
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}

	@Override
	public String toString() {
		return "TrabajadoDto [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", email=" + email
				+ ", password=" + password + ", idioma=" + idioma + ", cuentaActiva=" + cuentaActiva
				+ ", trabajadorActivo=" + trabajadorActivo + "]";
	}
}
