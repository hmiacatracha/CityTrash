package es.udc.citytrash.model.ruta;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;

@Entity
@Table(name = "TBL_RUTAS")
@BatchSize(size = 10)
public class Ruta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final Logger logger = LoggerFactory.getLogger(Ruta.class);

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

	@OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Contenedor> getContenedores() {
		return contenedores;
	}

	public void setContenedores(List<Contenedor> contenedores) {
		if (contenedores == null)
			this.contenedores = new ArrayList<Contenedor>();
		else
			this.contenedores = contenedores;
	}

	public void eliminarContenedor(Contenedor contenedor) {
		if (contenedor != null)
			this.contenedores.remove(contenedor);
	}

	public void addContenedor(Contenedor contenedor) {
		if (this.contenedores == null) {
			this.contenedores = new ArrayList<Contenedor>();
		}
		if (contenedor != null) {
			if (!this.contenedores.contains(contenedor)) {
				this.contenedores.add(contenedor);
			}

		}
	}

	public boolean containsContenedor(Contenedor contenedor) {
		if (contenedor == null)
			return false;
		if (this.contenedores == null)
			return false;
		else
			return this.contenedores.contains(contenedor);
	}

	/* http://www.baeldung.com/hibernate-many-to-many */
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "TBL_RU_TP", joinColumns = { @JoinColumn(name = "RUTA_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "TIPO_BASURA") })
	public List<TipoDeBasura> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<TipoDeBasura> tiposDeBasura) {
		if (tiposDeBasura == null)
			this.tiposDeBasura = new ArrayList<TipoDeBasura>();
		else
			this.tiposDeBasura = tiposDeBasura;
	}

	public void eliminarTipoDeBasura(TipoDeBasura tipo) {
		if (tipo != null)
			this.tiposDeBasura.remove(tipo);
	}

	public void addTipoDeBasura(TipoDeBasura t) {
		if (this.tiposDeBasura == null) {
			this.tiposDeBasura = new ArrayList<TipoDeBasura>();
		}

		if (t != null) {
			if (!this.tiposDeBasura.contains(t)) {
				this.tiposDeBasura.add(t);
			}

		}
	}

	public boolean containsTipoDeBasura(TipoDeBasura tipo) {
		if (tipo == null)
			return false;
		if (this.tiposDeBasura == null)
			return false;
		else
			return this.tiposDeBasura.contains(tipo);
	}

	int id;
	private Localizacion puntoInicio = new Localizacion();
	private Localizacion puntoFinal = new Localizacion();
	private boolean activo;
	private Camion camion;
	private List<Contenedor> contenedores;
	private List<TipoDeBasura> tiposDeBasura;

	@Override
	public String toString() {
		return "Ruta [id=" + id + ", puntoInicio=" + puntoInicio + ", puntoFinal=" + puntoFinal + ", activo=" + activo
				+ ", camion=" + camion + ", contenedores=" + contenedores + "]";
	}
}
