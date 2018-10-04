package es.udc.citytrash.controller.util.dtos.contenedor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.citytrash.controller.util.anotaciones.CamionNombreUnico;
import es.udc.citytrash.controller.util.anotaciones.CamposNoIguales;
import es.udc.citytrash.controller.util.anotaciones.CoductorValido;
import es.udc.citytrash.controller.util.anotaciones.ContenedorNombreUnico;
import es.udc.citytrash.controller.util.anotaciones.RecolectorValido;
import es.udc.citytrash.controller.util.anotaciones.TrabajadorActivo;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;

/**
 * Dto para registrar contenedores
 * 
 * @author hmia
 *
 */

public class ContenedorRegistroDto {

	public ContenedorRegistroDto() {

	}

	@NotBlank
	@ContenedorNombreUnico
	@Size(min = 2, max = 100)
	private String nombre = "";

	@NotNull
	private Integer modeloContenedor;

	private String localizacion = "";

	private BigDecimal longitud = new BigDecimal(0);

	private BigDecimal latitud = new BigDecimal(0);

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaAlta = null;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaBaja = null;

	private boolean activo = true;

	private static Date calendarToDate(Calendar calendar) {
		return calendar.getTime();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre.trim().toUpperCase();
	}

	public Integer getModeloContenedor() {
		return modeloContenedor;
	}

	public void setModeloContenedor(Integer modeloContenedor) {
		this.modeloContenedor = modeloContenedor;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	@Override
	public String toString() {
		return "ContenedorRegistroDto [nombre=" + nombre + ", modeloContenedor=" + modeloContenedor + ", longitud="
				+ longitud + ", latitud=" + latitud + ", fechaAlta=" + fechaAlta + ", fechaBaja=" + fechaBaja
				+ ", activo=" + activo + "]";
	}

}
