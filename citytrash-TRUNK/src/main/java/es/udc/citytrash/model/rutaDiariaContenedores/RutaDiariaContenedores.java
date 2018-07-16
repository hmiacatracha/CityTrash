package es.udc.citytrash.model.rutaDiariaContenedores;

import java.util.Calendar;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;

import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.rutaDiaria.RutaDiaria;
import es.udc.citytrash.model.trabajador.Trabajador;

@BatchSize(size = 10)
@Entity
@Table(name = "TBL_RD_CONT")
public class RutaDiariaContenedores {

	public RutaDiariaContenedores(RutaDiaria ruta, Contenedor contenedor, boolean sugerencia, boolean recogio,
			Trabajador trabajadorActualiza, Calendar fHUltimaActualizacion) {
		this.pk = new RutaDiariaContenedoresPK(ruta, contenedor);
		this.sugerencia = sugerencia;
		this.recogio = recogio;
		this.trabajadorActualiza = trabajadorActualiza;
		this.fHUltimaActualizacion = fHUltimaActualizacion;
	}

	@EmbeddedId
	@AssociationOverrides({
			@AssociationOverride(name = "pk.rutaDiaria", joinColumns = @JoinColumn(name = "RUTA_DIARIA_ID")),
			@AssociationOverride(name = "pk.contenedor", joinColumns = @JoinColumn(name = "CONTENEDOR_ID")) })
	public RutaDiariaContenedoresPK getPk() {
		return pk;
	}

	public void setPk(RutaDiariaContenedoresPK pk) {
		this.pk = pk;
	}

	@Column(name = "SUGERENCIA")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isSugerencia() {
		return sugerencia;
	}

	public void setSugerencia(boolean sugerencia) {
		this.sugerencia = sugerencia;
	}

	@Column(name = "REGOGIO")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isRecogio() {
		return recogio;
	}

	public void setRecogio(boolean recogio) {
		this.recogio = recogio;
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "TRABAJADOR_ACTUALIZA")
	public Trabajador getTrabajadorActualiza() {
		return trabajadorActualiza;
	}

	public void setTrabajadorActualiza(Trabajador trabajadorActualiza) {
		this.trabajadorActualiza = trabajadorActualiza;
	}

	@Column(name = "FECHA_HORA")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getfHUltimaActualizacion() {
		return fHUltimaActualizacion;
	}

	public void setfHUltimaActualizacion(Calendar fHUltimaActualizacion) {
		this.fHUltimaActualizacion = fHUltimaActualizacion;
	}

	@Transient
	public RutaDiaria getRutaDiaria() {
		return this.pk.getRutaDiaria();
	}

	public void setRutaDiaria(RutaDiaria rutaDiaria) {
		this.pk.setRutaDiaria(rutaDiaria);
	}

	@Transient
	public Contenedor getContenedor() {
		return this.pk.getContenedor();
	}

	public void setContenedor(Contenedor contenedor) {
		this.pk.setContenedor(contenedor);
	}

	private RutaDiariaContenedoresPK pk = new RutaDiariaContenedoresPK();
	private boolean sugerencia;
	private boolean recogio;
	private Trabajador trabajadorActualiza;
	private Calendar fHUltimaActualizacion;

	@Override
	public String toString() {
		return "RutaDiariaContenedores [pk=" + pk + ", sugerencia=" + sugerencia + ", recogio=" + recogio
				+ ", trabajadorActualiza=" + trabajadorActualiza + ", fHUltimaActualizacion=" + fHUltimaActualizacion
				+ "]";
	}
}
