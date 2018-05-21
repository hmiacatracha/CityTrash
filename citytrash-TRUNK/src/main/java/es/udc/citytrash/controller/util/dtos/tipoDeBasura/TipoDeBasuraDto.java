package es.udc.citytrash.controller.util.dtos.tipoDeBasura;

import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;

public class TipoDeBasuraDto {

	public TipoDeBasuraDto() {
	}

	public TipoDeBasuraDto(TipoDeBasura t) {
		this.color = t.getColor();
		this.tipo = t.getTipo();
		this.id = t.getId();
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String color;
	private String tipo;
	private int id;

}
