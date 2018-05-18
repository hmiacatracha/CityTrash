package es.udc.citytrash.controller.util.dtos.contenedor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import es.udc.citytrash.controller.util.anotaciones.ModeloCamionNombreUnico;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasura;
import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;

public class ContenedorModeloDto {

	public ContenedorModeloDto() {

	}

	public ContenedorModeloDto(ContenedorModelo modelo) {
		this.id = modelo.getId();
		this.nombre = modelo.getModelo();
		this.capacidadNominal = modelo.getCapacidadNomimal();
		this.cargaNominal = modelo.getCargaNomimal();
		this.profundidad = modelo.getProfuncidad() != null ? modelo.getProfuncidad() : new BigDecimal(0);
		this.altura = modelo.getAltura() != null ? modelo.getAltura() : new BigDecimal(0);
		this.ancho = modelo.getAnchura() != null ? modelo.getAnchura() : new BigDecimal(0);
		this.pesoVacio = modelo.getPesoVacio() != null ? modelo.getPesoVacio() : new BigDecimal(0);
		this.tipo = modelo.getTipo().getId();
	}

	private int id = -1;

	@Size(min = 2, max = 100)
	private String nombre;

	@NotNull
	@Min(value = 1)
	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal capacidadNominal = new BigDecimal(0);

	@NotNull
	@Min(value = 1)
	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal cargaNominal = new BigDecimal(0);

	@NotNull
	@Min(value = 1)
	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal profundidad = new BigDecimal(0);

	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal altura = new BigDecimal(0);

	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal ancho = new BigDecimal(0);

	@Min(value = 1)
	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal pesoVacio = new BigDecimal(0);

	// private TipoDeBasura tipo = null;*/

	private Integer tipo = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre.trim().toUpperCase();
	}

	public BigDecimal getCapacidadNominal() {
		return capacidadNominal;
	}

	public void setCapacidadNominal(BigDecimal capacidadNominal) {
		this.capacidadNominal = capacidadNominal;
	}

	public BigDecimal getCargaNominal() {
		return cargaNominal;
	}

	public void setCargaNominal(BigDecimal cargaNominal) {
		this.cargaNominal = cargaNominal;
	}

	public BigDecimal getProfundidad() {
		return profundidad;
	}

	public void setProfundidad(BigDecimal profundidad) {
		this.profundidad = profundidad;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	public BigDecimal getAncho() {
		return ancho;
	}

	public void setAncho(BigDecimal ancho) {
		this.ancho = ancho;
	}

	public BigDecimal getPesoVacio() {
		return pesoVacio;
	}

	public void setPesoVacio(BigDecimal pesoVacio) {
		this.pesoVacio = pesoVacio;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "ContenedorModeloDto [id=" + id + ", nombre=" + nombre + ", capacidadNomial=" + capacidadNominal
				+ ", cargaNominal=" + cargaNominal + ", profundidad=" + profundidad + ", altura=" + altura + ", ancho="
				+ ancho + ", pesoVacio=" + pesoVacio + ", tipo=" + tipo + "]";
	}
}
