package es.udc.citytrash.controller.util.dtos;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.citytrash.business.entity.idioma.Idioma;
import es.udc.citytrash.business.entity.trabajador.TipoTrabajador;
import es.udc.citytrash.controller.util.anotaciones.CamposIguales;
import es.udc.citytrash.controller.util.anotaciones.DocumentoNoDuplicado;
import es.udc.citytrash.controller.util.anotaciones.EmailNoDuplicado;

/*https://spring.io/guides/gs/validating-form-input/*/
@CamposIguales(primerCampo = "email", segundoCampo = "confirmarEmail", message = "{fieldMatch_email_alerta}")
public class TrabajadorFormDto {

	private static final String NO_BLACK_MENSAJE = "{notBlank_alerta}";
	private static final String SIZE_MENSAJE = "{size_alerta}";
	private static final String EMAIL_MENSAJE = "{email_alerta}";
	private static final String CP_MENSAJE = "{frm_registro_cp_alerta}";
	private static final String FECHA_NACIMIENTO_MENSAJE = "{frm_registro_fecha_nac_pas_alerta}";
	private static final String DATE_FORMAT_MENSAJE = "dd/mm/yyyy";
	private static final String DNI_NIE_MENSAJE = "{dni_nie_invalido}";
	private static final String EMAIL_DUPLICADO_MENSAJE = "{frm_registro_email_duplicado}";
	private static final String DOCUMENTO_DUPLICADO_MENSAJE = "{frm_registro_documento_duplicado}";
	private static final String NUMERO_MENSAJE = "{frm_registro_numero_calle}";
	private static final String NUMERO_PISO_MENSAJE = "{frm_registro_numero_piso}";

	@NotBlank(message = TrabajadorFormDto.NO_BLACK_MENSAJE)
	@Size(min = 2, max = 50, message = TrabajadorFormDto.SIZE_MENSAJE)
	private String nombre;

	@NotBlank(message = TrabajadorFormDto.NO_BLACK_MENSAJE)
	@Size(min = 2, max = 50, message = TrabajadorFormDto.SIZE_MENSAJE)
	private String apellidos;

	@NotBlank(message = TrabajadorFormDto.NO_BLACK_MENSAJE)
	@Email(message = TrabajadorFormDto.EMAIL_MENSAJE)
	@EmailNoDuplicado(message = TrabajadorFormDto.EMAIL_DUPLICADO_MENSAJE)
	private String email;

	@NotBlank(message = TrabajadorFormDto.NO_BLACK_MENSAJE)
	@Email(message = TrabajadorFormDto.EMAIL_MENSAJE)
	private String confirmarEmail;

	private TipoTrabajador tipo;

	@Size(min = 9, max = 9, message = TrabajadorFormDto.SIZE_MENSAJE)
	@DocumentoNoDuplicado(message = TrabajadorFormDto.DOCUMENTO_DUPLICADO_MENSAJE)
	@Pattern(regexp = "[0-9X-Z][0-9]{7}[A-Z]", message = TrabajadorFormDto.DNI_NIE_MENSAJE)
	private String documento;

	@Size(min = 0, max = 50, message = TrabajadorFormDto.SIZE_MENSAJE)
	private String via;

	@Pattern(regexp = "^(?:[0-9]{1}|[0-9]{2}|[0-9]{3}|)$", message = TrabajadorFormDto.NUMERO_MENSAJE)
	private String numero;

	@Pattern(regexp = "^(?:[0-9]{1}|[0-9]{2}|[0-9]{3}|)$", message = TrabajadorFormDto.NUMERO_PISO_MENSAJE)
	private String piso;

	@Size(min = 0, max = 50, message = TrabajadorFormDto.SIZE_MENSAJE)
	private String puerta;

	// El código postal en España son cinco números. Los dos primeros van del 01
	// al 52 y los tres restantes pueden ser cualquier valor numérico
	@Pattern(regexp = "0[1-9][0-9]{3}|[1-4][0-9]{4}|5[0-2][0-9]{3}", message = TrabajadorFormDto.CP_MENSAJE)
	private String cp;

	@NotBlank(message = TrabajadorFormDto.NO_BLACK_MENSAJE)
	@Past(message = TrabajadorFormDto.FECHA_NACIMIENTO_MENSAJE)
	@DateTimeFormat(pattern = TrabajadorFormDto.DATE_FORMAT_MENSAJE)
	private Date fechaNacimiento;

	@Size(min = 0, max = 255, message = TrabajadorFormDto.SIZE_MENSAJE)
	private String localidad;

	@Size(min = 0, max = 255, message = TrabajadorFormDto.SIZE_MENSAJE)
	private String provincia;

	private String restoDireccion;

	@NotNull(message = TrabajadorFormDto.NO_BLACK_MENSAJE)
	private Idioma idioma;

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

	public String getConfirmarEmail() {
		return confirmarEmail;
	}

	public void setConfirmarEmail(String confirmarEmail) {
		this.confirmarEmail = confirmarEmail;
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
		this.documento = documento;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
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
		this.puerta = puerta;
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
		this.provincia = provincia;
	}

	public String getRestoDireccion() {
		return restoDireccion;
	}

	public void setRestoDireccion(String restoDireccion) {
		this.restoDireccion = restoDireccion;
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}

	@Override
	public String toString() {
		return "RegistroForm [nombre=" + nombre + ", apellidos=" + apellidos + ", email=" + email + ", confirmarEmail="
				+ confirmarEmail + ",lang = " + idioma + " tipo=" + tipo + ", documento=" + documento + ", via=" + via
				+ ", numero=" + numero + ", piso=" + piso + ", puerta=" + puerta + ", cp=" + cp + ", fechaNacimiento="
				+ fechaNacimiento + ", localidad=" + localidad + ", provincia=" + provincia + ", restoDireccion="
				+ restoDireccion + "]";
	}
}
