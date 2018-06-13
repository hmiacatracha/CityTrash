package es.udc.citytrash.controller.util.dtos.trabajador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.citytrash.model.telefono.Telefono;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.util.enums.Idioma;
import es.udc.citytrash.util.enums.TipoTrabajador;

/*https://spring.io/guides/gs/validating-form-input/*/

/**
 * Formulario del Trabajador para la actualización de un trabajador
 * 
 * @author hmia
 *
 */

public class TrabajadorUpdateFormDto {

	final Logger logger = LoggerFactory.getLogger(TrabajadorUpdateFormDto.class);

	public TrabajadorUpdateFormDto() {

	}

	public TrabajadorUpdateFormDto(Trabajador t) {

		this.id = t.getId();
		this.documento = t.getDocId() != null ? t.getDocId().toUpperCase() : "";
		this.nombre = t.getNombre() != null ? t.getNombre().toUpperCase() : "";
		this.apellidos = t.getApellidos() != null ? t.getApellidos().toUpperCase().trim() : "";
		this.fechaNacimiento = t.getFecNac() != null ? t.getFecNac().getTime() : null;
		this.idioma = t.getIdioma() != null ? t.getIdioma() : Idioma.es;
		this.estaDeBaja = !t.isActiveWorker();
		this.via = t.getNombreVia() != null ? t.getNombreVia().toUpperCase() : "";
		this.numero = t.getNumero() != null ? t.getNumero().toString() : "";
		this.piso = t.getPiso() != null ? t.getPiso().toString() : "";
		this.puerta = t.getPuerta() != null ? t.getPuerta().toUpperCase() : "";
		this.restoDireccion = t.getRestoDireccion() != null ? t.getRestoDireccion().toUpperCase() : "";
		this.cp = t.getCp() != null ? t.getCp().toString() : "";
		this.localidad = t.getLocalidad() != null ? t.getLocalidad().toUpperCase() : "";
		this.provincia = t.getProvincia() != null ? t.getProvincia().toUpperCase() : "";
		this.email = t.getEmail() != null ? t.getEmail().toUpperCase().trim() : "";
		this.confirmarEmail = t.getEmail() != null ? t.getEmail().toUpperCase().trim() : "";
		this.telefonos = t.getTelefonos() != null ? t.getTelefonos() : new ArrayList<Telefono>();

		String tipo = t.getTrabajadorType();

		if (tipo == null || tipo.isEmpty())
			tipo = "NONE";

		switch (TipoTrabajador.valueOf((tipo))) {
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
	}

	@NotNull
	private long id;

	@Size(min = 9, max = 9)
	@Pattern(regexp = "[0-9X-Z][0-9]{7}[A-Z]", message = "{constraints.pattern.documentoId}")
	private String documento;

	@NotBlank
	@Size(min = 2, max = 255)
	private String nombre;

	@NotBlank
	@Size(min = 2, max = 255)
	private String apellidos;

	private String email;

	private String confirmarEmail;

	@NotNull
	private TipoTrabajador tipo;

	@Size(min = 0, max = 255)
	private String via;

	@Pattern(regexp = "^(?:[0-9]{1}|[0-9]{2}|[0-9]{3}|)$", message = "{constraints.pattern}")
	private String numero;

	@Pattern(regexp = "^(?:[0-9]{1}|[0-9]{2}|[0-9]{3}|)$", message = "{constraints.pattern}")
	private String piso;

	@Size(min = 0, max = 50)
	private String puerta;

	@Size(min = 0, max = 5)
	@Pattern(regexp = "^(0[1-9][0-9]{3}|[1-4][0-9]{4}|5[0-2][0-9]{3})?", message = "{constraints.pattern.cp}")
	private String cp;

	@Pattern(regexp = "^([9|6|7][0-9]{8})?", message = "{constraints.pattern.telefono}")
	@Size(min = 0, max = 9)
	private String telefono;

	@Valid
	// @Size(min = 1, max = 5)
	private List<Telefono> telefonos = new ArrayList<Telefono>();

	private List<Integer> enteros = new ArrayList<Integer>();

	@NotNull
	@Past
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaNacimiento;

	@Size(min = 0, max = 255)
	private String localidad;

	@Size(min = 0, max = 255)
	private String provincia;

	@Size(min = 0, max = 100)
	private String restoDireccion;

	@NotNull
	private Idioma idioma;

	@NotNull
	private boolean estaDeBaja;

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento.toUpperCase().trim();
	}

	public boolean isEstaDeBaja() {
		return estaDeBaja;
	}

	public void setEstaDeBaja(boolean estaDeBaja) {
		this.estaDeBaja = estaDeBaja;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre.toUpperCase();
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos.toUpperCase();
	}

	public TipoTrabajador getTipo() {
		return tipo;
	}

	public void setTipo(TipoTrabajador tipo) {
		this.tipo = tipo;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via.toUpperCase();
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getPuerta() {
		return puerta;
	}

	public void setPuerta(String puerta) {
		this.puerta = puerta.toUpperCase();
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad.toUpperCase();
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia.toUpperCase();
	}

	public String getRestoDireccion() {
		return restoDireccion;
	}

	public void setRestoDireccion(String restoDireccion) {
		this.restoDireccion = restoDireccion.toUpperCase();
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase().trim();
	}

	public String getConfirmarEmail() {
		return confirmarEmail;
	}

	public void setConfirmarEmail(String confirmarEmail) {
		this.confirmarEmail = confirmarEmail.toLowerCase().trim();
	}

	public List<Telefono> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(List<Telefono> telefonos) {
		if (telefonos == null)
			telefonos = new ArrayList<Telefono>();
		this.telefonos = telefonos;
	}

	public List<Integer> getEnteros() {
		return enteros;
	}

	public void setEnteros(List<Integer> enteros) {
		this.enteros = enteros;
	}

	@Override
	public String toString() {
		return "TrabajadorUpdateFormDto [id=" + id + ", documento=" + documento + ", nombre=" + nombre + ", apellidos="
				+ apellidos + ", email=" + email + ", confirmarEmail=" + confirmarEmail + ", tipo=" + tipo + ", via="
				+ via + ", numero=" + numero + ", piso=" + piso + ", puerta=" + puerta + ", cp=" + cp + ", telefono="
				+ telefono + ", telefonos=" + telefonos + ", fechaNacimiento=" + fechaNacimiento + ", localidad="
				+ localidad + ", provincia=" + provincia + ", restoDireccion=" + restoDireccion + ", idioma=" + idioma
				+ ", estaDeBaja=" + estaDeBaja + "]";
	}

}
