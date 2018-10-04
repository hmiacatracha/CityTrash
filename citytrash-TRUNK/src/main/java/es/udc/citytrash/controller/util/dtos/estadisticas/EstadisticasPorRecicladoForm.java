package es.udc.citytrash.controller.util.dtos.estadisticas;

import java.util.ArrayList;
import java.util.List;

import es.udc.citytrash.util.enums.TipoComparativa;

public class EstadisticasPorRecicladoForm {

	private TipoComparativa tipoComparativa = TipoComparativa.DAY;
	private List<Integer> tiposDeBasura = new ArrayList<Integer>();

	public EstadisticasPorRecicladoForm(TipoComparativa tipoComparativa, List<Integer> tiposDeBasura) {
		this.tipoComparativa = tipoComparativa;
		this.tiposDeBasura = tiposDeBasura;
	}

	public EstadisticasPorRecicladoForm() {
		this.tipoComparativa = tipoComparativa.DAY;
		this.tiposDeBasura.add(1);
	}

	public TipoComparativa getTipoComparativa() {
		return tipoComparativa;
	}

	public void setTipoComparativa(TipoComparativa tipo) {
		this.tipoComparativa = tipo;
	}

	public List<Integer> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<Integer> tiposDeBasura) {
		this.tiposDeBasura = tiposDeBasura;
	}

}
