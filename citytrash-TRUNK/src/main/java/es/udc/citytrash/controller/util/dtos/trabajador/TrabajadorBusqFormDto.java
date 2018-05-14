package es.udc.citytrash.controller.util.dtos.trabajador;

import javax.validation.constraints.NotNull;

import es.udc.citytrash.controller.util.anotaciones.StringEnum;
import es.udc.citytrash.util.enums.CampoBusqTrabajador;
import es.udc.citytrash.util.enums.TipoTrabajador;

/**
 * Formulario de búsqueda de trabajadores
 * 
 * @author hmia
 *
 */
public class TrabajadorBusqFormDto {

	/* Muestra no solo los trabajaores de alta, sino tambien los de baja */
	private Boolean mostrarTodosLosTrabajadores = true;

	@NotNull
	@StringEnum(enumClass = CampoBusqTrabajador.class)
	private String campo;

	private String buscar = "";

	@NotNull
	@StringEnum(enumClass = TipoTrabajador.class)
	private String tipo;

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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getMostrarTodosLosTrabajadores() {
		return mostrarTodosLosTrabajadores;
	}

	public void setMostrarTodosLosTrabajadores(Boolean mostrarTodosLosTrabajadores) {
		this.mostrarTodosLosTrabajadores = mostrarTodosLosTrabajadores;
	}

	@Override
	public String toString() {
		return "TrabajadorBusqFormDto [mostrarTrabajadoresDeBaja=" + mostrarTodosLosTrabajadores + ", campo=" + campo
				+ ", buscar=" + buscar + ", tipo=" + tipo + "]";
	}
}
