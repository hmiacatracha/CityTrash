package es.udc.citytrash.model.alerta;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import es.udc.citytrash.util.enums.Mensaje;
import es.udc.citytrash.util.enums.Prioridad;
import es.udc.citytrash.util.enums.TipoDeAlerta;

/**
 * Camion
 * 
 * @author hmia
 *
 */

@Entity
@Table(name = "TBL_ALERTAS")
@BatchSize(size = 10)
public class Alerta implements Serializable {

	/**
	 * 
	 */
	Alerta() {

	}

	/**
	 * 
	 * @param prioridad
	 * @param tipoDeAlerta
	 * @param mensaje
	 * @param key
	 */
	public Alerta(Prioridad prioridad, TipoDeAlerta tipoDeAlerta, Mensaje mensaje, String key) {
		this.prioridad = prioridad;
		this.tipoDeAlerta = tipoDeAlerta;
		this.mensaje = mensaje;
		this.key = key;
	}

	@Id
	@Column(name = "ALERTA_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "AlertaIdGenerator")
	@GenericGenerator(name = "AlertaIdGenerator", strategy = "native")
	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	@Column(name = "MENSAJE", nullable = false)
	@Enumerated(EnumType.STRING)
	public Mensaje getMensaje() {
		return mensaje;
	}

	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}

	@CreationTimestamp
	@Column(name = "FECHA_HORA")
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Calendar fechaHora) {
		this.fechaHora = fechaHora;
	}

	@Column(name = "PRIORIDAD", nullable = false)
	@Enumerated(EnumType.STRING)
	public Prioridad getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(Prioridad prioridad) {
		this.prioridad = prioridad;
	}

	@Column(name = "TIPO", nullable = false)
	@Enumerated(EnumType.STRING)
	public TipoDeAlerta getTipoDeAlerta() {
		return tipoDeAlerta;
	}

	public void setTipoDeAlerta(TipoDeAlerta tipoDeAlerta) {
		this.tipoDeAlerta = tipoDeAlerta;
	}

	@Column(name = "RECURSO", nullable = false)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/* Atributos */
	private static final long serialVersionUID = 1L;
	private long id;
	private Calendar fechaHora;
	private Prioridad prioridad;
	private TipoDeAlerta tipoDeAlerta;
	private Mensaje mensaje;
	private String key;

	@Override
	public String toString() {
		return "Alerta [id=" + id + ", fechaHora=" + fechaHora + ", prioridad=" + prioridad + ", tipoDeAlerta="
				+ tipoDeAlerta + ", mensaje=" + mensaje + ", key=" + key + "]";
	}

}
