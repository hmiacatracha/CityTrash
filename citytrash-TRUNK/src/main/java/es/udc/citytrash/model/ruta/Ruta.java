package es.udc.citytrash.model.ruta;

import java.io.Serializable;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.rutaTipoDeBasura.RutaTipoDeBasura;

@Entity
@Table(name = "TBL_RUTAS")
public class Ruta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Ruta() {
		/**
		 * A persistent class should has a empty constructor.
		 **/
	}

	public Ruta(Localizacion puntoInicio, Localizacion puntoFinal, boolean activo, Camion camion) {
		this.puntoInicio = puntoInicio;
		this.puntoFinal = puntoFinal;
		this.activo = activo;
		this.camion = camion;
	}

	@Id
	@Column(name = "RUTA_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "RutaIdGenerator")
	@GenericGenerator(name = "RutaIdGenerator", strategy = "native")
	public Integer getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "lat", column = @Column(name = "INICIO_LATITUD")),
			@AttributeOverride(name = "lng", column = @Column(name = "INICIO_LONGITUD")) })
	public Localizacion getPuntoInicio() {
		return puntoInicio;
	}

	public void setPuntoInicio(Localizacion puntoInicio) {
		this.puntoInicio = puntoInicio;
	}

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "lat", column = @Column(name = "FIN_LATITUD")),
			@AttributeOverride(name = "lng", column = @Column(name = "FIN_LONGITUD")) })
	public Localizacion getPuntoFinal() {
		return puntoFinal;
	}

	public void setPuntoFinal(Localizacion puntoFinal) {
		this.puntoFinal = puntoFinal;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CAMION_ID")
	public Camion getCamion() {
		return camion;
	}

	public void setCamion(Camion camion) {
		this.camion = camion;
	}

	@OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<Contenedor> getContenedores() {
		return contenedores;
	}

	public void setContenedores(List<Contenedor> contenedores) {
		this.contenedores = contenedores;
	}

	@OneToMany(mappedBy = "pk.ruta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<RutaTipoDeBasura> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<RutaTipoDeBasura> tiposDeBasura) {
		this.tiposDeBasura = tiposDeBasura;
	}

	int id;
	private Localizacion puntoInicio = new Localizacion();
	private Localizacion puntoFinal = new Localizacion();
	private boolean activo;
	private Camion camion;
	private List<Contenedor> contenedores;
	private List<RutaTipoDeBasura> tiposDeBasura;

	@Override
	public String toString() {
		return "Ruta [id=" + id + ", puntoInicio=" + puntoInicio + ", puntoFinal=" + puntoFinal + ", activo=" + activo
				+ ", camion=" + camion.getId() + ", contenedores=" + contenedores + "]";
	}
}
