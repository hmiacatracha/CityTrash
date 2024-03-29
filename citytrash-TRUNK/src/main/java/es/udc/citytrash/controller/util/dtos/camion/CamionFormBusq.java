package es.udc.citytrash.controller.util.dtos.camion;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import es.udc.citytrash.controller.util.anotaciones.StringEnum;
import es.udc.citytrash.util.enums.CampoBusqPalabrasClavesCamion;

public class CamionFormBusq {

	public CamionFormBusq() {

	}

	public CamionFormBusq(String campo, String palabrasClaves, Integer modelo, boolean mostrarSoloCamionesActivos,
			boolean mostrarSoloCamionesDeAlta, List<Integer> tipos) {
		this.campo = campo;
		this.buscar = palabrasClaves;
		this.modelo = modelo;
		this.mostrarSoloCamionesActivos = mostrarSoloCamionesActivos;
		this.setMostrarSoloCamionesDeAlta(mostrarSoloCamionesDeAlta);
		if (tipos == null)
			tipos = new ArrayList<Integer>();
		else
			this.tipos = tipos;
	}

	@NotNull
	@StringEnum(enumClass = CampoBusqPalabrasClavesCamion.class)
	private String campo = CampoBusqPalabrasClavesCamion.matricula.toString();

	String buscar = "";

	private Integer modelo = null;

	private boolean mostrarSoloCamionesActivos = false;

	private boolean mostrarSoloCamionesDeAlta = false;

	private List<Integer> tipos = new ArrayList<Integer>();

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

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

	public boolean getMostrarSoloCamionesActivos() {
		return mostrarSoloCamionesActivos;
	}

	public void setMostrarSoloCamionesActivos(boolean mostrarSoloCamionesActivos) {
		this.mostrarSoloCamionesActivos = mostrarSoloCamionesActivos;
	}

	public boolean getMostrarSoloCamionesDeAlta() {
		return mostrarSoloCamionesDeAlta;
	}

	public void setMostrarSoloCamionesDeAlta(boolean mostrarSoloCamionesDeAlta) {
		this.mostrarSoloCamionesDeAlta = mostrarSoloCamionesDeAlta;
	}

	public List<Integer> getTipos() {
		return tipos;
	}

	public void setTipos(List<Integer> tipos) {
		if (tipos == null)
			tipos = new ArrayList<Integer>();
		else
			this.tipos = tipos;
	}

	@Override
	public String toString() {
		return "CamionFormBusq [campo=" + campo + ", buscar=" + buscar + ", modelo=" + modelo
				+ ", mostrarSoloCamionesActivos=" + mostrarSoloCamionesActivos + ", mostrarSoloCamionesDeAlta="
				+ mostrarSoloCamionesDeAlta + "]";
	}
}
