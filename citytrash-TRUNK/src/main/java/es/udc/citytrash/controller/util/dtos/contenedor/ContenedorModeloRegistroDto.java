package es.udc.citytrash.controller.util.dtos.contenedor;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import es.udc.citytrash.controller.util.anotaciones.ContenedorNombreUnico;
import es.udc.citytrash.controller.util.anotaciones.ModeloContenedorNombreUnico;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;

public class ContenedorModeloRegistroDto {

	public ContenedorModeloRegistroDto() {

	}

	@NotNull
	@Size(min = 2, max = 100)
	@ModeloContenedorNombreUnico
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

	@NotNull
	private Integer tipo = null;

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
		return "ContenedorModeloRegistroDto [nombre=" + nombre + ", capacidadNominal=" + capacidadNominal
				+ ", cargaNominal=" + cargaNominal + ", profundidad=" + profundidad + ", altura=" + altura + ", ancho="
				+ ancho + ", pesoVacio=" + pesoVacio + ", tipo=" + tipo + "]";
	}

}
