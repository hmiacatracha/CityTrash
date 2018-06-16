package es.udc.citytrash.controller.util.dtos.camion;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class CamionModeloTipoDeBasuraDto {

	public CamionModeloTipoDeBasuraDto() {

	}

	/**
	 * @param tipo
	 *            id del tipo
	 * @param capacidad
	 *            capacidad
	 */
	public CamionModeloTipoDeBasuraDto(int tipo, BigDecimal capacidad) {
		this.idTipo = tipo;
		this.capacidad = capacidad;
	}
		
	@NotNull
	int idTipo;

	@DecimalMin("0.01")
	BigDecimal capacidad;

	boolean nuevo = true;

	boolean modificado = false;

	public int getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(int idTipo) {
		if (idTipo != this.idTipo)
			modificado = true;
		this.idTipo = idTipo;
	}

	public BigDecimal getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(BigDecimal capacidad) {
		if (!capacidad.equals(this.capacidad))
			modificado = true;
		this.capacidad = capacidad;
	}

	public boolean isNuevo() {
		return nuevo;
	}

	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
	}

	public boolean isModificado() {
		return modificado;
	}

	public void setModificado(boolean modificado) {
		this.modificado = modificado;
	}

	@Override
	public String toString() {
		return "CamionModeloTipoDeBasuraDto [idTipo=" + idTipo + ", capacidad=" + capacidad + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idTipo;
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
		CamionModeloTipoDeBasuraDto other = (CamionModeloTipoDeBasuraDto) obj;
		if (idTipo != other.idTipo)
			return false;
		return true;
	}
}
