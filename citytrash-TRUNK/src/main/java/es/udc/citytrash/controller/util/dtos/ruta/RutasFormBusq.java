package es.udc.citytrash.controller.util.dtos.ruta;

import java.util.ArrayList;
import java.util.List;

public class RutasFormBusq {

	public RutasFormBusq() {

	}

	public RutasFormBusq(List<Integer> tiposDeBasura, List<Long> trabajadores, List<Long> camiones,
			List<Long> contenedores, boolean filtroRutasActivas) {
		this.tiposDeBasura = tiposDeBasura != null ? tiposDeBasura : new ArrayList<Integer>();
		this.trabajadores = trabajadores != null ? trabajadores : new ArrayList<Long>();
		this.camiones = camiones != null ? camiones : new ArrayList<Long>();
		this.contenedores = contenedores != null ? contenedores : new ArrayList<Long>();
	}

	public List<Integer> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<Integer> tiposDeBasura) {
		if (tiposDeBasura != null)
			this.tiposDeBasura = tiposDeBasura;
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

	public boolean isMostrarSoloRutasActivas() {
		return mostrarSoloRutasActivas;
	}

	public void setMostrarSoloRutasActivas(boolean filtroRutasActivas) {
		this.mostrarSoloRutasActivas = filtroRutasActivas;
	}

	private List<Integer> tiposDeBasura = new ArrayList<Integer>();
	private List<Long> trabajadores = new ArrayList<Long>();
	private List<Long> camiones = new ArrayList<Long>();
	private List<Long> contenedores = new ArrayList<Long>();
	private boolean mostrarSoloRutasActivas = false;

}
