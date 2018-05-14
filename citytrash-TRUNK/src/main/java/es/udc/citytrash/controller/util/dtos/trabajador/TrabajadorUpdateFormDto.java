package es.udc.citytrash.controller.util.dtos.trabajador;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.citytrash.controller.util.anotaciones.DocumentoNoDuplicado;
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

	public TrabajadorUpdateFormDto(long id, String docId, String nombre, String apellidos, Calendar fechaNacimiento,
			String email, String token, Calendar fechaExpiracionToken, Idioma idioma, String nombreVia, Integer numero,
			Integer piso, String puerta, String provincia, String localidad, BigDecimal cp, BigDecimal telefono,
			String restoDireccion, Boolean trabajadorActivo, String trabajadorType) {

		this.id = id;
		this.documento = docId != null ? docId.toUpperCase() : "";
		this.nombre = nombre != null ? nombre.toUpperCase() : "";
		this.apellidos = apellidos != null ? apellidos.toUpperCase().trim() : "";
		this.fechaNacimiento = fechaNacimiento != null ? fechaNacimiento.getTime() : null;
		this.idioma = idioma != null ? idioma : Idioma.es;
		this.estaDeBaja = !trabajadorActivo;
		this.via = nombreVia != null ? nombreVia.toUpperCase() : "";
		this.numero = numero != null ? numero.toString() : "";
		this.piso = piso != null ? piso.toString() : "";
		this.puerta = puerta != null ? puerta.toUpperCase() : "";
		this.restoDireccion = restoDireccion != null ? restoDireccion.toUpperCase() : "";
		this.cp = cp != null ? cp.toString() : "";
		this.localidad = localidad != null ? provincia.toUpperCase() : "";
		this.provincia = provincia != null ? provincia.toUpperCase() : "";
		this.email = email != null ? email.toUpperCase().trim() : "";
		this.confirmarEmail = email != null ? email.toUpperCase().trim() : "";

		String tipo = trabajadorType;

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

	@Override
	public String toString() {
		return "TrabajadorUpdateFormDto [documento=" + documento + ", nombre=" + nombre + ", apellidos=" + apellidos
				+ ", tipo=" + tipo + ", via=" + via + ", numero=" + numero + ", piso=" + piso + ", puerta=" + puerta
				+ ", cp=" + cp + ", telefono=" + telefono + ", fechaNacimiento=" + fechaNacimiento + ", localidad="
				+ localidad + ", provincia=" + provincia + ", restoDireccion=" + restoDireccion + ", idioma=" + idioma
				+ ", estaDeBaja=" + estaDeBaja + "]";
	}
}
