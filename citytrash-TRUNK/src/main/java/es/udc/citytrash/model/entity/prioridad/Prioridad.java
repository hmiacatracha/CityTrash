package es.udc.citytrash.model.entity.prioridad;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
/* @Immutable */
@Table(name = "TBL_PRIORIDADES")
public class Prioridad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Prioridad() {
		super();
	}

	public Prioridad(String tipo, String es) {
		super();
		this.tipo = tipo;
		this.es = es;
	}

	@Id
	@Column(name = "TIPO")
	public String tipo;

	@Column(name = "DESCRIPCION")
	public String es;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEs() {
		return es;
	}

	public void setEs(String es) {
		this.es = es;
	}
}
