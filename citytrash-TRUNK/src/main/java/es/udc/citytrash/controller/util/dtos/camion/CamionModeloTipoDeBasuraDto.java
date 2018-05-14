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

	public int getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(int idTipo) {
		this.idTipo = idTipo;
	}

	public BigDecimal getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(BigDecimal capacidad) {
		this.capacidad = capacidad;
	}

	@Override
	public String toString() {
		return "CamionModeloTipoDeBasuraDto [idTipo=" + idTipo + ", capacidad=" + capacidad + "]";
	}
}
