package es.udc.citytrash.controller.util.dtos.ruta;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.ruta.Localizacion;
import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;

/**
 *
 * 
 * @author hmia
 *
 */

public class RutaDto {

	public RutaDto() {

	}

	public RutaDto(Ruta r) {
		this.id = r.getId();
		this.puntoInicio = r.getPuntoInicio();
		this.puntoFinal = r.getPuntoFinal();
		this.activo = r.isActivo();
		this.camion = r.getCamion().getId();
		List<Long> contDtos = new ArrayList<Long>();
		for (Contenedor c : r.getContenedores()) {
			contDtos.add(c.getId());
		}
		this.contenedores = contDtos;

		List<Integer> tipos = new ArrayList<Integer>();
		for (TipoDeBasura t : r.getTiposDeBasura()) {
			tipos.add(t.getId());
		}
		this.tiposDeBasura = tipos;
	}

	Integer id = null;

	@Valid
	private Localizacion puntoInicio = new Localizacion();

	@Valid
	private Localizacion puntoFinal = new Localizacion();

	private boolean activo = true;

	@NotNull
	@Size(min = 2, max = 40, message = "{validador.rutas.size.contenedores}")
	@UniqueElements
	private List<Long> contenedores = new ArrayList<Long>();

	@NotNull
	@Size(min = 1, max = 3, message = "{validador.rutas.size.tipoDeBasura}")
	@UniqueElements
	private List<Integer> tiposDeBasura = new ArrayList<Integer>();

	@NotNull
	private Long camion = null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Localizacion getPuntoInicio() {
		return puntoInicio;
	}

	public void setPuntoInicio(Localizacion puntoInicio) {
		this.puntoInicio = puntoInicio;
	}

	public Localizacion getPuntoFinal() {
		return puntoFinal;
	}

	public void setPuntoFinal(Localizacion puntoFinal) {
		this.puntoFinal = puntoFinal;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Long getCamion() {
		return camion;
	}

	public void setCamion(Long camion) {
		this.camion = camion;
	}

	public List<Long> getContenedores() {
		return contenedores;
	}

	public void setContenedores(List<Long> contenedores) {
		this.contenedores = contenedores;
	}

	public List<Integer> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<Integer> tiposDeBasura) {
		this.tiposDeBasura = tiposDeBasura;
	}

	@Override
	public String toString() {
		return "RutaDto [id=" + id + ", puntoInicio=" + puntoInicio + ", puntoFinal=" + puntoFinal + ", activo="
				+ activo + ", camion=" + camion + ", contenedores=" + contenedores + ", tiposDeBasura=" + tiposDeBasura
				+ "]";
	}
}
