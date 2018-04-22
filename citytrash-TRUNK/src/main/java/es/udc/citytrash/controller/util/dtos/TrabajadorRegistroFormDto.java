package es.udc.citytrash.controller.util.dtos;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.citytrash.controller.util.anotaciones.CamposIguales;
import es.udc.citytrash.controller.util.anotaciones.DocumentoNoDuplicado;
import es.udc.citytrash.controller.util.anotaciones.EmailNoDuplicado;
import es.udc.citytrash.util.enums.Idioma;
import es.udc.citytrash.util.enums.TipoTrabajador;

/*https://spring.io/guides/gs/validating-form-input/*/
/*https://developer.mozilla.org/es/docs/HTML/HTML5/Validacion_de_restricciones*/

/**
 * Formulario del Trabajador (Utilizado para el registro y para la modificación
 * de un trabajador
 * 
 * @author hmia
 *
 */
@CamposIguales(primerCampo = "email", segundoCampo = "confirmarEmail", message = "{constraints.fieldmatch.email}")
public class TrabajadorRegistroFormDto {

	public TrabajadorRegistroFormDto() {

	}

	/**
	 * Formulario de registro => para pruebas
	 * 
	 * @param documento
	 * @param nombre
	 * @param apellidos
	 * @param email
	 * @param fechaNacimiento
	 * @param tipo
	 * @param idioma
	 */
	public TrabajadorRegistroFormDto(String documento, String nombre, String apellidos, String email,
			Date fechaNacimiento, TipoTrabajador tipo, Idioma idioma) {
		this.documento = documento;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.confirmarEmail = email;
		this.tipo = tipo;
		this.fechaNacimiento = fechaNacimiento;
		this.idioma = idioma;
	}

	@NotBlank
	@Size(min = 2, max = 255)
	private String nombre;

	@NotBlank
	@Size(min = 2, max = 255)
	private String apellidos;

	@NotBlank
	@Email
	@EmailNoDuplicado
	@Size(max = 100)
	private String email;

	@NotBlank
	@Email
	private String confirmarEmail;

	@NotNull
	private TipoTrabajador tipo;

	@Size(min = 9, max = 9)
	@DocumentoNoDuplicado
	@Pattern(regexp = "[0-9X-Z][0-9]{7}[A-Z]", message = "{constraints.pattern.documentoId}")
	private String documento;

	@Size(min = 0, max = 255)
	private String via;

	@Pattern(regexp = "^(?:[0-9]{1}|[0-9]{2}|[0-9]{3}|)$", message = "{constraints.pattern}")
	private String numero;

	@Pattern(regexp = "^(?:[0-9]{1}|[0-9]{2}|[0-9]{3}|)$", message = "{constraints.pattern}")
	private String piso;

	@Size(min = 0, max = 50)
	private String puerta;

	@Size(min = 0, max = 5)
	// https://igochan.wordpress.com/2013/03/27/expresiones-regulares-utiles
	@Pattern(regexp = "(0[1-9][0-9]{3}|[1-4][0-9]{4}|5[0-2][0-9]{3})", message = "{constraints.pattern.cp}")
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

	private String restoDireccion;

	@NotNull
	private Idioma idioma;

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

	public TipoTrabajador getTipo() {
		return tipo;
	}

	public void setTipo(TipoTrabajador tipo) {
		this.tipo = tipo;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento.toUpperCase().trim();
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
		this.localidad = localidad;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia.toUpperCase();
	}

	@Size(min=0, max=100)
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

	@Override
	public String toString() {
		return "TrabajadorFormDto [nombre=" + nombre + ", apellidos=" + apellidos + ", email=" + email
				+ ", confirmarEmail=" + confirmarEmail + ", telefono=" + telefono + ", tipo=" + tipo + ", documento="
				+ documento + ", via=" + via + ", numero=" + numero + ", piso=" + piso + ", puerta=" + puerta + ", cp="
				+ cp + ", fechaNacimiento=" + fechaNacimiento + ", localidad=" + localidad + ", provincia=" + provincia
				+ ", restoDireccion=" + restoDireccion + ", idioma=" + idioma + "]";
	}
}
