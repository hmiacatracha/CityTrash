package es.udc.citytrash.controller.util.dtos.camion;

import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.citytrash.controller.util.anotaciones.CamposNoIguales;
import es.udc.citytrash.controller.util.anotaciones.CoductorValido;
import es.udc.citytrash.controller.util.anotaciones.RecolectorValido;
import es.udc.citytrash.controller.util.anotaciones.TrabajadorInactivo;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.contenedor.Contenedor;

/**
 * Dto para modificar/ver camiones
 * 
 * @author hmia
 *
 */

@CamposNoIguales.List({
		@CamposNoIguales(primerCampo = "conductorPrincipal", segundoCampo = "conductorSuplente", message = "{constraints.fieldNotShouldmatch.conductores}"),
		@CamposNoIguales(primerCampo = "recogedorUno", segundoCampo = "recogedorDos", message = "{constraints.fieldNotShouldmatch.recolectores}") })
public class CamionDto {

	public CamionDto() {
	}

	public CamionDto(Camion c) {
		this.activo = c.getActivo();
		this.conductorPrincipal = c.getConductor() != null ? c.getConductor().getId() : null;
		this.conductorSuplente = c.getConductorSuplente() != null ? c.getConductorSuplente().getId() : null;
		this.recogedorUno = c.getRecogedor1() != null ? c.getRecogedor1().getId() : null;
		this.recogedorDos = c.getRecogedor2() != null ? c.getRecogedor2().getId() : null;
		this.fechaAlta = calendarToDate(c.getFechaAlta());
		this.fechaBaja = c.getFechaBaja() != null ? calendarToDate(c.getFechaBaja()) : null;
		this.id = c.getId();
		this.matricula = c.getMatricula();
		this.modeloCamion = c.getModeloCamion() != null ? c.getModeloCamion().getId() : null;
		this.nombre = c.getNombre();
		this.vin = c.getVin();
	}

	private Long id;

	@Size(min = 0, max = 17)
	private String vin = "";

	@NotBlank
	@Size(min = 2, max = 100)
	private String nombre = "";

	@Size(min = 0, max = 20)
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

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaAlta = null;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date fechaBaja = null;

	private boolean activo = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	private static Date calendarToDate(Calendar calendar) {
		return calendar.getTime();
	}

	@Override
	public String toString() {
		return "CamionDto [id=" + id + ", vin=" + vin + ", nombre=" + nombre + ", matricula=" + matricula
				+ ", modeloCamion=" + modeloCamion + ", recogedorUno=" + recogedorUno + ", recogedorDos=" + recogedorDos
				+ ", conductorPrincipal=" + conductorPrincipal + ", conductorSuplente=" + conductorSuplente
				+ ", fechaAlta=" + fechaAlta + ", fechaBaja=" + fechaBaja + ", activo=" + activo + "]";
	}
}
