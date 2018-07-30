package es.udc.citytrash.controller.util.dtos.ruta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import es.udc.citytrash.controller.util.anotaciones.CamposNoIguales;
import es.udc.citytrash.model.ruta.Localizacion;
import es.udc.citytrash.model.rutaDiaria.RutaDiaria;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;

public class RutaDiariaDto {

	final Logger logger = LoggerFactory.getLogger(RutaDiariaDto.class);

	public RutaDiariaDto() {

	}

	public RutaDiariaDto(RutaDiaria rd) {
		logger.info("paso 1");
		this.rutaId = rd.getRuta().getId();
		this.fecha = rd.getFecha() != null ? calendarToDate(rd.getFecha()) : null;
		this.fechaHoraInicio = fechaHoraInicio != null ? calendarToDate(rd.getFechaHoraInicio()) : null;
		this.fechaHoraFin = fechaHoraFin != null ? calendarToDate(rd.getFechaHoraFin()) : null;
		this.fechaHoraUltimaActualizacion = fechaHoraUltimaActualizacion != null
				? calendarToDate(rd.getfHUltimaActualizacion()) : null;
		this.id = rd.getId();
		this.puntoInicio = rd.getRuta().getPuntoInicio() != null ? rd.getRuta().getPuntoInicio() : null;
		this.puntoFinal = rd.getRuta().getPuntoFinal() != null ? rd.getRuta().getPuntoFinal() : null;
		this.camion = rd.getCamion() != null ? rd.getCamion().getId() : null;
		List<Long> lRecolectores = new ArrayList<Long>();
		if (rd.getRecogedor1() != null)
			lRecolectores.add(rd.getRecogedor1().getId());
		if (rd.getRecogedor2() != null)
			lRecolectores.add(rd.getRecogedor2().getId());
		this.recolectores = lRecolectores;
		this.conductor = rd.getConductor() != null ? rd.getConductor().getId() : null;
		this.tiposDeBasura = rd.getTiposDeBasura();
	}

	@NotNull
	long id;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull
	Date fecha;

	Date fechaHoraInicio;

	Date fechaHoraFin;

	Date fechaHoraUltimaActualizacion;

	long trabajadorActualiza;

	@NotNull
	int rutaId;

	@NotNull
	private Long camion = null;

	private String puntoInicioDir = "";

	private String puntoFinalDir = "";

	private Localizacion puntoInicio = new Localizacion();

	private Localizacion puntoFinal = new Localizacion();

	@NotNull
	@Size(min = 1, max = 2, message = "{validador.rutas.size.recolectores}")
	@UniqueElements(message = "{validador.rutas.unique.recolectores}")
	List<Long> recolectores = new ArrayList<Long>();

	@NotNull
	Long conductor;
	
	@NotNull
	private List<TipoDeBasura> tiposDeBasura = new ArrayList<TipoDeBasura>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	public void setFechaHoraInicio(Date fechaHoraInicio) {
		this.fechaHoraInicio = fechaHoraInicio;
	}

	public Date getFechaHoraFin() {
		return fechaHoraFin;
	}

	public void setFechaHoraFin(Date fechaHoraFin) {
		this.fechaHoraFin = fechaHoraFin;
	}

	public Date getFechaHoraUltimaActualizacion() {
		return fechaHoraUltimaActualizacion;
	}

	public void setFechaHoraUltimaActualizacion(Date fechaHoraUltimaActualizacion) {
		this.fechaHoraUltimaActualizacion = fechaHoraUltimaActualizacion;
	}

	public long getTrabajadorActualiza() {
		return trabajadorActualiza;
	}

	public void setTrabajadorActualiza(long trabajadorActualiza) {
		this.trabajadorActualiza = trabajadorActualiza;
	}

	public int getRutaId() {
		return rutaId;
	}

	public void setRutaId(int rutaId) {
		this.rutaId = rutaId;
	}

	public Long getCamion() {
		return camion;
	}

	public void setCamion(Long camion) {
		this.camion = camion;
	}

	public Localizacion getPuntoInicio() {
		return puntoInicio;
	}

	public void setPuntoInicio(Localizacion puntoInicio) {
		this.puntoInicio = puntoInicio;
	}

	public Localizacion getPuntoFinal() {
		return puntoFinal;
	}

	public void setPuntoFinal(Localizacion puntoFinal) {
		this.puntoFinal = puntoFinal;
	}

	public List<Long> getRecolectores() {
		return recolectores;
	}

	public void setRecolectores(List<Long> recolectores) {
		this.recolectores = recolectores;
	}

	public Long getConductor() {
		return conductor;
	}

	public void setConductor(Long conductor) {
		this.conductor = conductor;
	}

	private static Date calendarToDate(Calendar calendar) {
		return calendar.getTime();
	}

	public List<TipoDeBasura> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<TipoDeBasura> tiposDeBasura) {
		this.tiposDeBasura = tiposDeBasura;
	}

	public String getPuntoInicioDir() {
		return puntoInicioDir;
	}

	public void setPuntoInicioDir(String puntoInicioDir) {
		this.puntoInicioDir = puntoInicioDir;
	}

	public String getPuntoFinalDir() {
		return puntoFinalDir;
	}

	public void setPuntoFinalDir(String puntoFinalDir) {
		this.puntoFinalDir = puntoFinalDir;
	}

}
