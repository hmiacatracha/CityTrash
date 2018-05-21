package es.udc.citytrash.controller.util.dtos.contenedor;

import java.math.BigDecimal;

import es.udc.citytrash.controller.util.dtos.tipoDeBasura.TipoDeBasuraDto;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;

public class ContenedorModeloDto {

	
	
	public ContenedorModeloDto() {
	}

	public ContenedorModeloDto(ContenedorModelo m) {
		this.id = m.getId();
		this.modelo = m.getModelo();
		this.capacidadNomimal = m.getCapacidadNominal();
		this.cargaNomimal = m.getCapacidadNominal();
		this.profuncidad = m.getProfundidad();
		this.altura = m.getAltura();
		this.anchura = m.getAnchura();
		this.pesoVacio = m.getPesoVacio();
		this.tipo = new TipoDeBasuraDto(m.getTipo());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public BigDecimal getCapacidadNomimal() {
		return capacidadNomimal;
	}

	public void setCapacidadNomimal(BigDecimal capacidadNomimal) {
		this.capacidadNomimal = capacidadNomimal;
	}

	public BigDecimal getCargaNomimal() {
		return cargaNomimal;
	}

	public void setCargaNomimal(BigDecimal cargaNomimal) {
		this.cargaNomimal = cargaNomimal;
	}

	public BigDecimal getProfuncidad() {
		return profuncidad;
	}

	public void setProfuncidad(BigDecimal profuncidad) {
		this.profuncidad = profuncidad;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	public BigDecimal getAnchura() {
		return anchura;
	}

	public void setAnchura(BigDecimal anchura) {
		this.anchura = anchura;
	}

	public BigDecimal getPesoVacio() {
		return pesoVacio;
	}

	public void setPesoVacio(BigDecimal pesoVacio) {
		this.pesoVacio = pesoVacio;
	}

	public TipoDeBasuraDto getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeBasuraDto tipo) {
		this.tipo = tipo;
	}

	private int id;
	private String modelo;
	private BigDecimal capacidadNomimal;
	private BigDecimal cargaNomimal;
	private BigDecimal profuncidad;
	private BigDecimal altura;
	private BigDecimal anchura;
	private BigDecimal pesoVacio;
	private TipoDeBasuraDto tipo;

}
