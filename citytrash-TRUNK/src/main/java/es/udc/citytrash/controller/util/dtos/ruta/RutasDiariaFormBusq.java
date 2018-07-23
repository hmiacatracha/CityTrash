package es.udc.citytrash.controller.util.dtos.ruta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class RutasDiariaFormBusq {

	public RutasDiariaFormBusq() {

	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param rutas
	 * @param trabajadores
	 * @param camiones
	 * @param contenedores
	 */
	public RutasDiariaFormBusq(Date from, Date to, List<Integer> rutas, List<Long> trabajadores, List<Long> camiones,
			List<Long> contenedores) {
		Date hoy = Calendar.getInstance().getTime();
		/*
		 * this.from = from != null ? from : hoy; this.to = to != null ? to :
		 * hoy;
		 */
		this.from = from;
		this.to = to;
		this.rutas = rutas != null ? rutas : new ArrayList<Integer>();
		this.trabajadores = trabajadores != null ? trabajadores : new ArrayList<Long>();
		this.camiones = camiones != null ? camiones : new ArrayList<Long>();
		this.contenedores = contenedores != null ? contenedores : new ArrayList<Long>();
	}

	public List<Long> getTrabajadores() {
		return trabajadores;
	}

	public void setTrabajadores(List<Long> trabajadores) {
		if (trabajadores != null)
			this.trabajadores = trabajadores;
	}

	public List<Long> getCamiones() {
		return camiones;
	}

	public void setCamiones(List<Long> camiones) {
		if (camiones != null)
			this.camiones = camiones;
	}

	public List<Long> getContenedores() {
		return contenedores;
	}

	public void setContenedores(List<Long> contenedores) {
		if (contenedores != null)
			this.contenedores = contenedores;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public List<Integer> getRutas() {
		return rutas;
	}

	public void setRutas(List<Integer> rutas) {
		this.rutas = rutas;
	}

	@DateTimeFormat(pattern = "dd/MM/YYYY")
	private Date from = Calendar.getInstance().getTime();
	@DateTimeFormat(pattern = "dd/MM/YYYY")
	private Date to = Calendar.getInstance().getTime();
	private List<Integer> rutas = new ArrayList<Integer>();
	private List<Long> trabajadores = new ArrayList<Long>();
	private List<Long> camiones = new ArrayList<Long>();
	private List<Long> contenedores = new ArrayList<Long>();

}
