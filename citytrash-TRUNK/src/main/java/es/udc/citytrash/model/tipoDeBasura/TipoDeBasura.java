package es.udc.citytrash.model.tipoDeBasura;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.concurrent.Immutable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasura;
import es.udc.citytrash.util.GlobalNames;

/**
 * Tipos de basura
 * 
 * @author hmia
 *
 */

@Entity
@Immutable
@BatchSize(size = 5)
@Table(name = GlobalNames.TBL_TIPO_BASURA)
public class TipoDeBasura implements Serializable {

	TipoDeBasura() {

	}

	public TipoDeBasura(String tipo) {
		this.tipo = tipo;
	}

	public TipoDeBasura(String tipo, String color) {
		this.tipo = tipo;
		this.color = color;
	}

	@Id
	@Column(name = "TIPO_BASURA_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "TiposDeBasuraIdGenerator")
	@GenericGenerator(name = "TiposDeBasuraIdGenerator", strategy = "native")
	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	@Size(min = 0, max = 6)
	@Column(name = "COLOR", unique = true)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "TIPO", unique = true)
	@Size(min = 3, max = 100)
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * atributos
	 */
	private static final long serialVersionUID = 1L;
	private String color;
	private String tipo;
	private int id;

	@Override
	public String toString() {
		return "TipoDeBasura [color=" + color + ", tipo=" + tipo + ", id=" + id + "]";
	}

}
