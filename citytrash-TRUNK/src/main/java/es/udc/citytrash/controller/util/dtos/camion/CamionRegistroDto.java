package es.udc.citytrash.controller.util.dtos.camion;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import es.udc.citytrash.controller.util.anotaciones.CamionMatriculaUnico;
import es.udc.citytrash.controller.util.anotaciones.CamionNombreUnico;
import es.udc.citytrash.controller.util.anotaciones.CamionVinUnico;
import es.udc.citytrash.controller.util.anotaciones.CamposNoIguales;
import es.udc.citytrash.controller.util.anotaciones.CoductorValido;
import es.udc.citytrash.controller.util.anotaciones.RecolectorValido;
import es.udc.citytrash.controller.util.anotaciones.TrabajadorInactivo;
import es.udc.citytrash.model.camion.Camion;

/**
 * Dto para registrar camiones
 * 
 * @author hmia
 *
 */

@CamposNoIguales.List({
		@CamposNoIguales(primerCampo = "conductorPrincipal", segundoCampo = "conductorSuplente", message = "{constraints.fieldNotShouldmatch.conductores}"),
		@CamposNoIguales(primerCampo = "recogedorUno", segundoCampo = "recogedorDos", message = "{constraints.fieldNotShouldmatch.recolectores}") })
public class CamionRegistroDto {

	public CamionRegistroDto() {

	}

	@Size(min = 0, max = 17)
	@CamionVinUnico
	private String vin = "";

	@NotBlank
	@Size(min = 2, max = 100)
	@CamionNombreUnico
	private String nombre = "";

	@Size(min = 0, max = 20)
	@CamionMatriculaUnico
	private String matricula = "";

	@NotNull
	private int modeloCamion;

	@TrabajadorInactivo
	@RecolectorValido(allowNull = true)
	private Long recogedorUno = null;

	@TrabajadorInactivo
	@RecolectorValido(allowNull = true)
	private Long recogedorDos = null;

	@TrabajadorInactivo
	@CoductorValido(allowNull = false)
	private Long conductorPrincipal = null;

	@TrabajadorInactivo
	@CoductorValido(allowNull = true)
	private Long conductorSuplente = null;

	private boolean activo = true;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin.trim().toUpperCase();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre.trim().toUpperCase();
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula.trim().toUpperCase();
	}

	public int getModeloCamion() {
		return modeloCamion;
	}

	public void setModeloCamion(int modeloCamion) {
		this.modeloCamion = modeloCamion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Long getRecogedorUno() {
		return recogedorUno;
	}

	public void setRecogedorUno(Long recogedorUno) {
		this.recogedorUno = recogedorUno;
	}

	public Long getRecogedorDos() {
		return recogedorDos;
	}

	public void setRecogedorDos(Long recogedorDos) {
		this.recogedorDos = recogedorDos;
	}

	public Long getConductorPrincipal() {
		return conductorPrincipal;
	}

	public void setConductorPrincipal(Long conductorPrincipal) {
		this.conductorPrincipal = conductorPrincipal;
	}

	public Long getConductorSuplente() {
		return conductorSuplente;
	}

	public void setConductorSuplente(Long conductorSuplente) {
		this.conductorSuplente = conductorSuplente;
	}

	@Override
	public String toString() {
		return "CamionRegistroDto [vin=" + vin + ", nombre=" + nombre + ", matricula=" + matricula + ", modeloCamion="
				+ modeloCamion + ", recogedorUno=" + recogedorUno + ", recogedorDos=" + recogedorDos
				+ ", conductorPrincipal=" + conductorPrincipal + ", conductorSuplente=" + conductorSuplente
				+ ", activo=" + activo + "]";
	}
}
