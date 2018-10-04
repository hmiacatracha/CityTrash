package es.udc.citytrash.controller.util.dtos.estadisticas;

import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;

public class ComparativaPorTipoReciclado {

	TipoDeBasura tipoDeBasura;
	Double anterior;
	Double actual;

	public TipoDeBasura getTipoDeBasura() {
		return tipoDeBasura;
	}

	public void setTipoDeBasura(TipoDeBasura tipoDeBasura) {
		this.tipoDeBasura = tipoDeBasura;
	}

	public Double getAnterior() {
		return anterior;
	}

	public void setAnterior(Double anterior) {
		this.anterior = anterior;
	}

	public Double getActual() {
		return actual;
	}

	public void setActual(Double actual) {
		this.actual = actual;
	}

	@Override
	public String toString() {
		return "ComparativaPorTipoReciclado [tipoDeBasura=" + tipoDeBasura + ", anterior=" + anterior + ", actual="
				+ actual + "]";
	}
}
