package es.udc.citytrash.controller.util.dtos.contenedor;

import java.util.ArrayList;
import java.util.List;

public class ContenedorFormBusq {

	public ContenedorFormBusq() {

	}

	public ContenedorFormBusq(String palabrasClaves, Integer modelo, boolean mostrarSoloContenedoresActivos,
			boolean mostrarSoloContenedoresDeAlta, List<Integer> tiposDeBasura) {
		this.buscar = palabrasClaves;
		this.modelo = modelo;
		this.mostrarSoloContenedoresActivos = mostrarSoloContenedoresActivos;
		this.setMostrarSoloContenedoresDeAlta(mostrarSoloContenedoresDeAlta);
		this.tiposDeBasura = tiposDeBasura;
	}

	String buscar = "";

	private Integer modelo = null;

	List<Integer> tiposDeBasura = new ArrayList<Integer>();

	private boolean mostrarSoloContenedoresActivos = false;

	private boolean mostrarSoloContenedoresDeAlta = false;

	public String getBuscar() {
		return buscar;
	}

	public void setBuscar(String buscar) {
		this.buscar = buscar;
	}

	public Integer getModelo() {
		return modelo;
	}

	public void setModelo(Integer modelo) {
		this.modelo = modelo;
	}

	public boolean getMostrarSoloContenedoresActivos() {
		return mostrarSoloContenedoresActivos;
	}

	public void setMostrarSoloContenedoresActivos(boolean mostrarSoloContenedoresActivos) {
		this.mostrarSoloContenedoresActivos = mostrarSoloContenedoresActivos;
	}

	public boolean getMostrarSoloContenedoresDeAlta() {
		return mostrarSoloContenedoresDeAlta;
	}

	public void setMostrarSoloContenedoresDeAlta(boolean mostrarSoloContenedoresDeAlta) {
		this.mostrarSoloContenedoresDeAlta = mostrarSoloContenedoresDeAlta;
	}

	public List<Integer> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<Integer> tiposDeBasura) {
		this.tiposDeBasura = tiposDeBasura;
	}

	@Override
	public String toString() {
		return "ContenedorFormBusq [buscar=" + buscar + ", modelo=" + modelo + ", tiposDeBasura=" + tiposDeBasura
				+ ", mostrarSoloContenedoresActivos=" + mostrarSoloContenedoresActivos
				+ ", mostrarSoloConectoresDeAlta=" + mostrarSoloContenedoresDeAlta + "]";
	}
}
