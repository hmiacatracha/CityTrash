package es.udc.citytrash.model.camionModelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasura;
import es.udc.citytrash.util.GlobalNames;

/**
 * 
 * @author hmia
 * 
 */

@Entity
@Table(name = GlobalNames.TBL_MODELO_CAMION)
@BatchSize(size = 20)
public class CamionModelo implements Serializable {

	CamionModelo() {

	}

	/**
	 * 
	 * @param modelo
	 *            modelo
	 * @param ancho
	 *            ancho
	 * @param altura
	 *            altura
	 * @param longitud
	 *            longitud
	 */
	public CamionModelo(String modelo, BigDecimal ancho, BigDecimal altura, BigDecimal longitud) {
		this.modelo = modelo;
		this.ancho = ancho;
		this.altura = altura;
		this.longitud = longitud;
	}

	/**
	 * 
	 * @param modelo
	 *            nombre del modelo
	 * @param volumenTolva
	 *            volumen tolva
	 * @param ancho
	 *            ancho
	 * @param altura
	 *            altura
	 * @param longitud
	 *            longitud
	 * @param distancia
	 *            distancia
	 * @param pma
	 *            pma
	 */
	public CamionModelo(String modelo, BigDecimal volumenTolva, BigDecimal ancho, BigDecimal altura,
			BigDecimal longitud, BigDecimal distancia, Integer pma) {
		this.modelo = modelo;
		this.volumenTolva = volumenTolva;
		this.ancho = ancho;
		this.altura = altura;
		this.longitud = longitud;
		this.distancia = distancia;
		this.pma = pma;
	}

	@Id
	@Column(name = "MODELO_CAMION_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ModeloCamionIdGenerator")
	@GenericGenerator(name = "ModeloCamionIdGenerator", strategy = "native")
	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	@Column(name = "MODELO", unique = true)
	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "VOLUMEN_TOLVA", nullable = true)
	public BigDecimal getVolumenTolva() {
		return volumenTolva;
	}

	public void setVolumenTolva(BigDecimal volumenTolva) {
		this.volumenTolva = volumenTolva;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "ANCHO", nullable = false)
	public BigDecimal getAncho() {
		return ancho;
	}

	public void setAncho(BigDecimal ancho) {
		this.ancho = ancho;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "ALTURA", nullable = false)
	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "LONGITUD", nullable = false)
	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "DISTANCIA", nullable = true)
	public BigDecimal getDistancia() {
		return distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		this.distancia = distancia;
	}

	@Column(name = "PMA", nullable = true)
	public Integer getPma() {
		return pma;
	}

	public void setPma(Integer pma) {
		this.pma = pma;
	}

	@OneToMany(mappedBy = "pk.modelo", cascade = CascadeType.ALL)
	public List<CamionModeloTipoDeBasura> getTiposDeBasura() {
		return tiposDeBasura;
	}

	public void setTiposDeBasura(List<CamionModeloTipoDeBasura> tipoDeBasura) {
		this.tiposDeBasura = tipoDeBasura;
	}

	public void addTipoDeBasura(CamionModeloTipoDeBasura modeloCamionTipoDeBasura) {
		this.tiposDeBasura.add(modeloCamionTipoDeBasura);
	}

	/* Atributos */
	private static final long serialVersionUID = 1L;
	private int id;
	private String modelo;
	private BigDecimal volumenTolva;
	private BigDecimal ancho;
	private BigDecimal altura;
	private BigDecimal longitud;
	private BigDecimal distancia;
	private Integer pma;
	private List<CamionModeloTipoDeBasura> tiposDeBasura = new ArrayList<CamionModeloTipoDeBasura>();

	@Override
	public String toString() {
		return "CamionModelo [id=" + id + ", modelo=" + modelo + ", volumenTolva=" + volumenTolva + ", ancho=" + ancho
				+ ", altura=" + altura + ", longitud=" + longitud + ", distancia=" + distancia + ", pma=" + pma
				+ ", tiposDeBasura=" + tiposDeBasura + "]";
	}

}
