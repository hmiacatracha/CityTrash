package es.udc.citytrash.model.contenedorModelo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.util.GlobalNames;

/**
 * 
 * @author hmia
 * 
 */

@Entity
@Table(name = GlobalNames.TBL_MODELO_CONTENEDOR)
@BatchSize(size = 20)
public class ContenedorModelo implements Serializable {

	/**
	 * 
	 */
	ContenedorModelo() {

	}

	/**
	 * Constructor con los campos obligatorios
	 * 
	 * @param modelo
	 * @param capacidadNomimal
	 * @param cargaNomimal
	 */
	public ContenedorModelo(String modelo, BigDecimal capacidadNomimal, BigDecimal cargaNomimal) {
		this.modelo = modelo;
		this.capacidadNominal = capacidadNomimal;
		this.cargaNominal = cargaNomimal;
	}

	@Id
	@Column(name = "MODELO_CONTENEDOR_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ModeloContenedorIdGenerator")
	@GenericGenerator(name = "ModeloContenedorIdGenerator", strategy = "native")
	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	@Column(name = "NOMBRE", unique = true)
	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "CAPACIDAD_NOMINAL", nullable = false)
	public BigDecimal getCapacidadNominal() {
		return capacidadNominal;
	}

	public void setCapacidadNominal(BigDecimal capacidadNomimal) {
		this.capacidadNominal = capacidadNomimal;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "CARGA_NOMINAL", nullable = false)
	public BigDecimal getCargaNominal() {
		return cargaNominal;
	}

	public void setCargaNominal(BigDecimal cargaNomimal) {
		this.cargaNominal = cargaNomimal;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "PROFUNDIDAD", nullable = true)
	public BigDecimal getProfundidad() {
		return profundidad;
	}

	public void setProfundidad(BigDecimal profuncidad) {
		this.profundidad = profuncidad;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "ALTURA", nullable = true)
	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "ANCHURA", nullable = true)
	public BigDecimal getAnchura() {
		return anchura;
	}

	public void setAnchura(BigDecimal anchura) {
		this.anchura = anchura;
	}

	@Digits(integer = 17, fraction = 2)
	@Column(name = "PESO_VACIO", nullable = true)
	public BigDecimal getPesoVacio() {
		return pesoVacio;
	}

	public void setPesoVacio(BigDecimal pesoVacio) {
		this.pesoVacio = pesoVacio;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "TIPO_BASURA")
	public TipoDeBasura getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeBasura tipo) {
		this.tipo = tipo;
	}

	/* Atributos */
	private static final long serialVersionUID = 1L;
	private int id;
	private String modelo;
	private BigDecimal capacidadNominal;
	private BigDecimal cargaNominal;
	private BigDecimal profundidad;
	private BigDecimal altura;
	private BigDecimal anchura;
	private BigDecimal pesoVacio;
	private TipoDeBasura tipo;

}
