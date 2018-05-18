package es.udc.citytrash.controller.util.dtos.contenedor;

import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.citytrash.controller.util.anotaciones.CamposNoIguales;
import es.udc.citytrash.controller.util.anotaciones.CoductorValido;
import es.udc.citytrash.controller.util.anotaciones.RecolectorValido;
import es.udc.citytrash.controller.util.anotaciones.TrabajadorInactivo;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;

/**
 * Dto para modificar/ver Contenedores
 * 
 * @author hmia
 *
 */

public class ContenedorDto {

	public ContenedorDto() {

	}

	public ContenedorDto(Contenedor c) {
		this.id = c.getId();
		this.nombre = c.getNombre();
		if (c.getLatitud() != null && c.getLongitud() != null)
			this.localizacion = new Localizacion(c.getLatitud(), c.getLatitud());
		this.fechaAlta = c.getFechaAlta() != null ? calendarToDate(c.getFechaAlta()) : null;
		this.fechaBaja = c.getFechaBaja() != null ? calendarToDate(c.getFechaBaja()) : null;
		this.activo = c.getActivo();
		this.modeloContenedor = c.getModelo();
	}

	@NotNull
	private long id;

	@NotBlank
	@Size(min = 2, max = 100)
	private String nombre = "";

	@NotNull
	private ContenedorModelo modeloContenedor;

	private Localizacion localizacion = new Localizacion();

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaAlta = null;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaBaja = null;

	private boolean activo = true;

	private static Date calendarToDate(Calendar calendar) {
		return calendar.getTime();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre.trim().toUpperCase();
	}

	public ContenedorModelo getModeloContenedor() {
		return modeloContenedor;
	}

	public void setModeloContenedor(ContenedorModelo modeloContenedor) {
		this.modeloContenedor = modeloContenedor;
	}

	public Localizacion getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
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

	@Override
	public String toString() {
		return "ContenedorDto [id=" + id + ", nombre=" + nombre + ", modeloContenedor=" + modeloContenedor
				+ ", localizacion=" + localizacion + ", fechaAlta=" + fechaAlta + ", fechaBaja=" + fechaBaja
				+ ", activo=" + activo + "]";
	}
}
