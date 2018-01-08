package es.udc.citytrash.business.entity.prioridad;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "TBL_PRIORIDADES")
public class Prioridad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Prioridad() {
		/**
		 * A persistent class should has a empty constructor.
		 **/
	}

	@Id
	@Column(name = "TIPO")
	private String tipo;

	@Column(name = "DESCRIPCION")
	private String es;

	public String getTipo() {
		return tipo;
	}

	protected void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEs() {
		return es;
	}

	protected void setEs(String es) {
		this.es = es;
	}
}
