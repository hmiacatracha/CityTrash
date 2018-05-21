package es.udc.citytrash.controller.util.dtos.contenedor;

import java.math.BigDecimal;
import java.util.Calendar;

import es.udc.citytrash.model.contenedor.Contenedor;

public class ContenedorDto {

	public ContenedorDto() {

	}

	public ContenedorDto(Contenedor c) {
		this.id = c.getId();
		this.activo = c.getActivo();
		this.fechaAlta = c.getFechaAlta();
		this.fechaBaja = c.getFechaBaja();
		this.latitud = c.getLatitud();
		this.latitud = c.getLatitud();
		this.modelo = new ContenedorModeloDto(c.getModelo());
		this.nombre = c.getNombre();
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
		this.nombre = nombre;
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

	public Calendar getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Calendar fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Calendar getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Calendar fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public ContenedorModeloDto getModelo() {
		return modelo;
	}

	public void setModelo(ContenedorModeloDto modelo) {
		this.modelo = modelo;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	private long id;
	private String nombre;
	private BigDecimal latitud;
	private BigDecimal longitud;
	private Calendar fechaAlta;
	private Calendar fechaBaja;
	private ContenedorModeloDto modelo;
	private Boolean activo;
}
