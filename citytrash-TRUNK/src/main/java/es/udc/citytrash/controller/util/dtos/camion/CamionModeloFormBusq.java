package es.udc.citytrash.controller.util.dtos.camion;

import java.util.ArrayList;
import java.util.List;

public class CamionModeloFormBusq {

	public CamionModeloFormBusq() {

	}

	public CamionModeloFormBusq(List<Integer> tipos, String modelo) {
		if (tipos == null)
			tipos = new ArrayList<Integer>();
		else
			this.tipos = tipos;
		this.palabrasClaveModelo = modelo;
	}

	String palabrasClaveModelo;

	private List<Integer> tipos = new ArrayList<Integer>();

	public String getPalabrasClaveModelo() {
		return palabrasClaveModelo;
	}

	public void setPalabrasClaveModelo(String palabrasClaveModelo) {
		this.palabrasClaveModelo = palabrasClaveModelo;
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
		return "CamionModeloFormBusq [palabrasClaveModelo=" + palabrasClaveModelo + ", tipos=" + tipos + "]";
	}
}
