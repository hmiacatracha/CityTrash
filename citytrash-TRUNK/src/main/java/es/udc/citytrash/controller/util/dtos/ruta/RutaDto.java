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
import es.udc.citytrash.model.rutaTipoDeBasura.RutaTipoDeBasura;
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
		List<RutaContenedorDto> contDtos = new ArrayList<RutaContenedorDto>();
		for (Contenedor c : r.getContenedores()) {
			RutaContenedorDto rc = new RutaContenedorDto();
			rc.setId(c.getId());
			contDtos.add(rc);
		}
		this.contenedores = contDtos;

		List<RutaTipoBasuraDto> tipos = new ArrayList<RutaTipoBasuraDto>();
		for (RutaTipoDeBasura t : r.getTiposDeBasura()) {
			RutaTipoBasuraDto rt = new RutaTipoBasuraDto();
			rt.setId(t.getPk().getTipo().getId());
			tipos.add(rt);
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
	private Long camion = null;

	@NotNull
	@Size(min = 5, max = 40)
	@UniqueElements
	private List<RutaContenedorDto> contenedores = new ArrayList<RutaContenedorDto>();

	@Size(min = 1, max = 3)
	@UniqueElements
	private List<RutaTipoBasuraDto> tiposDeBasura = new ArrayList<RutaTipoBasuraDto>();

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

	public List<RutaContenedorDto> getContenedores() {
		return contenedores;
	}

	public void setContenedores(List<RutaContenedorDto> contenedores) {
		this.contenedores = contenedores;
	}

	public List<RutaTipoBasuraDto> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<RutaTipoBasuraDto> tiposDeBasura) {
		this.tiposDeBasura = tiposDeBasura;
	}

	@Override
	public String toString() {
		return "RutaDto [id=" + id + ", puntoInicio=" + puntoInicio + ", puntoFinal=" + puntoFinal + ", activo="
				+ activo + ", camion=" + camion + ", contenedores=" + contenedores + ", tiposDeBasura=" + tiposDeBasura
				+ "]";
	}
}
