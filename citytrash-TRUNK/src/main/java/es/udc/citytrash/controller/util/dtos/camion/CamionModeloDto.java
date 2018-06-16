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

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import es.udc.citytrash.controller.util.anotaciones.ModeloCamionNombreUnico;
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
		this.listaTiposDeBasura = new ArrayList<CamionModeloTipoDeBasuraDto>();
	}

	private Integer id = null;

	// @NombreModeloCamionUnico
	@Size(min = 2, max = 100)
	private String nombre;

	@NumberFormat(style = Style.NUMBER, pattern = "###.###")
	private BigDecimal volumenTolva = new BigDecimal(0);

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
	@Size(min = 1)
	@UniqueElements(message = "{constraints.tipos.basura.duplicados}")
	private List<CamionModeloTipoDeBasuraDto> listaTiposDeBasura = new ArrayList<CamionModeloTipoDeBasuraDto>();

	private boolean modificado = false;

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
		if (!nombre.equalsIgnoreCase(this.nombre))
			modificado = true;
		this.nombre = nombre.toUpperCase();
	}

	public BigDecimal getVolumenTolva() {
		return volumenTolva;
	}

	public void setVolumenTolva(BigDecimal volumenTolva) {
		if (!volumenTolva.equals(this.volumenTolva))
			modificado = true;
		this.volumenTolva = volumenTolva;
	}

	public BigDecimal getAncho() {
		return ancho;
	}

	public void setAncho(BigDecimal ancho) {
		if (!ancho.equals(this.ancho))
			modificado = true;
		this.ancho = ancho;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		if (!altura.equals(this.altura))
			modificado = true;
		this.altura = altura;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		if (!longitud.equals(this.longitud))
			modificado = true;
		this.longitud = longitud;
	}

	public BigDecimal getDistancia() {
		return distancia;
	}

	public void setDistancia(BigDecimal distancia) {
		if (!distancia.equals(this.distancia))
			modificado = true;
		this.distancia = distancia;
	}

	public Integer getPma() {
		return pma;
	}

	public void setPma(Integer pma) {
		if (pma != this.pma)
			modificado = true;
		this.pma = pma;
	}

	public boolean isModificado() {
		return modificado;
	}

	public void setModificado(boolean modificado) {
		this.modificado = modificado;
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
		if (!listaTiposDeBasura.contains(modeloTipo))
			this.listaTiposDeBasura.add(modeloTipo);
	}

	public void eliminarTipo(CamionModeloTipoDeBasuraDto modeloTipo) {
		if (listaTiposDeBasura.contains(modeloTipo))
			this.listaTiposDeBasura.remove(modeloTipo);
	}

	@Override
	public String toString() {
		return "CamionModeloDto [id=" + id + ", nombre=" + nombre + ", volumenTolva=" + volumenTolva + ", ancho="
				+ ancho + ", altura=" + altura + ", longitud=" + longitud + ", distancia=" + distancia + ", pma=" + pma
				+ ", listaTiposDeBasura=" + listaTiposDeBasura + "]";
	}
}
