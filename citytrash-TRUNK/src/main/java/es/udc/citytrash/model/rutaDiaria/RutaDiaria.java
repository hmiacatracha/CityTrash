package es.udc.citytrash.model.rutaDiaria;

import java.util.Calendar;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.UpdateTimestamp;
import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.trabajador.Trabajador;

@Entity
@Table(name = "TBL_RUTAS_DIARIAS")
public class RutaDiaria {

	@Column(name = "FECHA_HORA_INICIO")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	public void setFechaHoraInicio(Calendar fechaHoraInicio) {
		this.fechaHoraInicio = fechaHoraInicio;
	}

	@Column(name = "FECHA_HORA_FIN")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaHoraFin() {
		return fechaHoraFin;
	}

	public void setFechaHoraFin(Calendar fechaHoraFin) {
		this.fechaHoraFin = fechaHoraFin;
	}

	@Column(name = "RECOGEDOR1")
	public Trabajador getRecogedor1() {
		return recogedor1;
	}

	public void setRecogedor1(Trabajador recogedor1) {
		this.recogedor1 = recogedor1;
	}

	@Column(name = "RECOGEDOR2")
	public Trabajador getRecogedor2() {
		return recogedor2;
	}

	public void setRecogedor2(Trabajador recogedor2) {
		this.recogedor2 = recogedor2;
	}

	@Column(name = "CONDUCTOR_ASIGNADO")
	public Trabajador getConductor() {
		return conductor;
	}

	public void setConductor(Trabajador conductor) {
		this.conductor = conductor;
	}

	@Column(name = "TRABAJADOR_ACTUALIZA")
	public Trabajador getTrabajadorActualiza() {
		return trabajadorActualiza;
	}

	public void setTrabajadorActualiza(Trabajador trabajadorActualiza) {
		this.trabajadorActualiza = trabajadorActualiza;
	}

	@Column(name = "ULTIMA_ACTUALIZACION")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	public Calendar getfHUltimaActualizacion() {
		return fHUltimaActualizacion;
	}

	public void setfHUltimaActualizacion(Calendar fHUltimaActualizacion) {
		this.fHUltimaActualizacion = fHUltimaActualizacion;
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CAMION_ID")
	public Camion getCamion() {
		return camion;
	}

	public void setCamion(Camion camion) {
		this.camion = camion;
	}

	@EmbeddedId
	public RutaDiariaPK getPk() {
		return pk;
	}

	public void setPk(RutaDiariaPK pk) {
		this.pk = pk;
	}

	private RutaDiariaPK pk;
	private Calendar fechaHoraInicio;
	private Calendar fechaHoraFin;
	private Trabajador recogedor1;
	private Trabajador recogedor2;
	private Trabajador conductor;
	private Trabajador trabajadorActualiza;
	private Calendar fHUltimaActualizacion;
	private Camion camion;
}
