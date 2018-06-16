package es.udc.citytrash.controller.util.dtos.contenedor;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SensorValores {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	private Timestamp fechaHora;
	private BigDecimal valor;
	private String unidad;
	private String valorString;

	public SensorValores(Timestamp fechaHora, BigDecimal valor, String unidad) {
		this.fechaHora = fechaHora;
		this.valor = valor;
		this.unidad = unidad;
		this.valorString = valor.toString() + unidad;
	}

	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getValorString() {
		return valorString;
	}

	public void setValorString(String valorString) {
		this.valorString = valorString;
	}

	@Override
	public String toString() {
		return "SensorValores [fechaHora=" + fechaHora + ", valor=" + valor + ", unidad=" + unidad + "]";
	}

}
