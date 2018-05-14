package es.udc.citytrash.controller.util.dtos.camion;

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

import es.udc.citytrash.controller.util.anotaciones.NombreModeloCamionUnico;
import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModeloTipoDeBasura.CamionModeloTipoDeBasura;

public class CamionModeloDto {

	public CamionModeloDto() {

	}

	public CamionModeloDto(CamionModelo modelo) {
		this.id = modelo.getId();
		this.nombre = modelo.getModelo();
		this.volumenTolva = modelo.getVolumenTolva();
		this.ancho = modelo.getAncho();
		this.altura = modelo.getAltura();
		this.distancia = modelo.getDistancia();
		this.longitud = modelo.getLongitud();
		this.pma = modelo.getPma();
		listaTiposDeBasura = new ArrayList<CamionModeloTipoDeBasuraDto>();

		/*
		 * for (CamionModeloTipoDeBasura ct : modelo.getTiposDeBasura()) {
		 * CamionModeloTipoDeBasuraDto ctb = new
		 * CamionModeloTipoDeBasuraDto(ct.getTipo().getId(), ct.getCapacidad());
		 * listaTiposDeBasura.add(ctb); }
		 */
	}

	private Integer id = null;

	// @NombreModeloCamionUnico
	@Size(min = 2, max = 255)
	private String nombre;

	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal volumenTolva = new BigDecimal(0);

	// private String volumenTolva = "0";

	@NotNull
	@Min(value = 1000)
	@Max(value = 10000)
	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal ancho = new BigDecimal(0);

	@NotNull
	@Min(value = 1000)
	@Max(value = 10000)
	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal altura = new BigDecimal(0);

	@NotNull
	@Min(value = 1000)
	@Max(value = 10000)
	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal longitud = new BigDecimal(0);

	@Min(value = 1000)
	@Max(value = 10000)
	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal distancia = new BigDecimal(0);

	@Min(value = 0)
	@Max(value = 50)
	private Integer pma = 0;

	@Valid
	@NotNull
	// @Size(min = 1)
	private List<CamionModeloTipoDeBasuraDto> listaTiposDeBasura = new ArrayList<CamionModeloTipoDeBasuraDto>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre.toUpperCase();
	}

	public BigDecimal getVolumenTolva() {
		return volumenTolva;
	}

	public void setVolumenTolva(BigDecimal volumenTolva) {
		this.volumenTolva = volumenTolva;
	}

	public BigDecimal getAncho() {
		return ancho;
	}

	public void setAncho(BigDecimal ancho) {
		this.ancho = ancho;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public BigDecimal getDistancia() {
		return distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		this.distancia = distancia;
	}

	public Integer getPma() {
		return pma;
	}

	public void setPma(Integer pma) {
		this.pma = pma;
	}

	public List<CamionModeloTipoDeBasuraDto> getListaTiposDeBasura() {
		if (this.listaTiposDeBasura == null)
			this.listaTiposDeBasura = new ArrayList<CamionModeloTipoDeBasuraDto>(0);
		return listaTiposDeBasura;
	}

	public void setListaTiposDeBasura(List<CamionModeloTipoDeBasuraDto> listaTiposDeBasura) {
		this.listaTiposDeBasura = listaTiposDeBasura;
	}

	public void addTipo(CamionModeloTipoDeBasuraDto modeloTipo) {
		this.listaTiposDeBasura.add(modeloTipo);
	}

	@Override
	public String toString() {
		return "CamionModeloDto [id=" + id + ", nombre=" + nombre + ", volumenTolva=" + volumenTolva + ", ancho="
				+ ancho + ", altura=" + altura + ", longitud=" + longitud + ", distancia=" + distancia + ", pma=" + pma
				+ ", listaTiposDeBasura=" + listaTiposDeBasura + "]";
	}
}
