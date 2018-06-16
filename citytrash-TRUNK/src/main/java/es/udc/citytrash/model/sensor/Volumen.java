package es.udc.citytrash.model.sensor;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.util.GlobalNames;

@Entity
@DiscriminatorValue(value = GlobalNames.DISCRIMINADOR_VOLUMEN)
public class Volumen extends Sensor implements Serializable {

	private static final long serialVersionUID = 1L;

	Volumen() {
		super();
	}

	/**
	 * 
	 * @param nombre
	 * @param contenedor
	 * @param activo
	 */
	public Volumen(String nombre, Contenedor contenedor, boolean activo) {
		super(nombre, contenedor, activo);
	}

	@Column(name = "VO_PORCENTAJE")
	public BigDecimal getPorcentajeVolumen() {
		return porcentajeVolumen;
	}

	public void setPorcentajeVolumen(BigDecimal porcentaje) {
		this.porcentajeVolumen = porcentaje;
	}

	private BigDecimal porcentajeVolumen;
}
