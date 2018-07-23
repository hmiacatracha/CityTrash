package es.udc.citytrash.controller.util.dtos.ruta;

import java.util.List;

import es.udc.citytrash.model.ruta.Ruta;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;

/**
 *
 * 
 * @author hmia
 *
 */

public class GenerarRutaDto {

	public GenerarRutaDto() {

	}

	public GenerarRutaDto(Ruta r) {
		this.id = r.getId();
		this.generar = false;
		this.tiposDeBasura = r.getTiposDeBasura();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isGenerar() {
		return generar;
	}

	public void setGenerar(boolean generar) {
		this.generar = generar;
	}

	public List<TipoDeBasura> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<TipoDeBasura> tiposDeBasura) {
		this.tiposDeBasura = tiposDeBasura;
	}

	int id;
	boolean generar = false;
	List<TipoDeBasura> tiposDeBasura;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenerarRutaDto other = (GenerarRutaDto) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GenerarRutaDto [id=" + id + ", generar=" + generar + "]";
	}

}
