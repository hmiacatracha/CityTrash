package es.udc.citytrash.controller.util.dtos.ruta;

import javax.validation.constraints.NotNull;

public class RutaTipoBasuraDto {

	public RutaTipoBasuraDto() {

	}

	public RutaTipoBasuraDto(int id) {
		this.id = id;
	}

	@NotNull
	public int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
