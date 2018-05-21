package es.udc.citytrash.controller.util.dtos.contenedor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.citytrash.model.contenedor.Contenedor;

/**
 * Dto para modificar/ver Contenedores
 * 
 * @author hmia
 *
 */

public class ContenedorEditarDto {

	public ContenedorEditarDto() {

	}

	public ContenedorEditarDto(Contenedor c) {
		this.id = c.getId();
		this.nombre = c.getNombre();
		if (c.getLatitud() != null && c.getLongitud() != null) {
			this.latitud = c.getLatitud();
			this.longitud = c.getLongitud();
		}
		this.fechaAlta = c.getFechaAlta() != null ? calendarToDate(c.getFechaAlta()) : null;
		this.fechaBaja = c.getFechaBaja() != null ? calendarToDate(c.getFechaBaja()) : null;
		this.activo = c.getActivo();
		this.modeloContenedor = c.getModelo().getId();
	}

	@NotNull
	private long id;

	@NotBlank
	@Size(min = 2, max = 100)
	private String nombre = "";

	@NotNull
	Integer modeloContenedor = null;

	private String localizacion = "";

	@NotNull
	private BigDecimal latitud = new BigDecimal(0);

	@NotNull
	private BigDecimal longitud = new BigDecimal(0);

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

	public Integer getModeloContenedor() {
		return modeloContenedor;
	}

	public void Integer(Integer modeloContenedor) {
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

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public void setModeloContenedor(Integer modeloContenedor) {
		this.modeloContenedor = modeloContenedor;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String direccion) {
		this.localizacion = direccion;
	}

	@Override
	public String toString() {
		return "ContenedorDto [id=" + id + ", nombre=" + nombre + ", modeloContenedor=" + modeloContenedor
				+ ", latitud=" + latitud + ", longitud=" + longitud + ", fechaAlta=" + fechaAlta + ", fechaBaja="
				+ fechaBaja + ", activo=" + activo + "]";
	}
}
