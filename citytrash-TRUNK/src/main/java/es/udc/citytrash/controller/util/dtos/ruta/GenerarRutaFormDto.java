package es.udc.citytrash.controller.util.dtos.ruta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

/**
 *
 * 
 * @author hmia
 *
 */

public class GenerarRutaFormDto {

	public GenerarRutaFormDto() {

	}

	/**
	 * Generar rutas
	 * 
	 * @param fecha
	 *            fecha
	 * @param tiposDeBasura
	 * @param camiones
	 * @param rutas
	 */
	public GenerarRutaFormDto(Date fecha, List<Integer> tiposDeBasura, List<Long> camiones, List<Integer> rutas) {
		this.rutas = rutas;
		this.tiposDeBasura = tiposDeBasura;
		this.camiones = camiones;
		this.fecha = fecha;
	}

	public List<Integer> getRutas() {
		return rutas;
	}

	public void setRutas(List<Integer> rutas) {
		this.rutas = rutas;
	}

	public List<Integer> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<Integer> tiposDeBasura) {
		this.tiposDeBasura = tiposDeBasura;
	}

	public List<Long> getCamiones() {
		return camiones;
	}

	public void setCamiones(List<Long> camiones) {
		this.camiones = camiones;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@UniqueElements(message = "{validador.rutas.unique}")
	@Size(min = 1, max = 5, message = "{validador.rutas.size.rutaAGenerar}")
	private List<Integer> rutas = new ArrayList<Integer>();
	private List<Integer> tiposDeBasura = new ArrayList<Integer>();
	private List<Long> camiones = new ArrayList<Long>();

	@NotNull
	private Date fecha = Calendar.getInstance().getTime();
	
	@Override
	public String toString() {
		return "GenerarRutaFormDto [rutas=" + rutas + ", tiposDeBasura=" + tiposDeBasura + ", camiones=" + camiones
				+ ", fecha=" + fecha + "]";
	}
}
