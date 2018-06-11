package es.udc.citytrash.model.sensor;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.util.GlobalNames;

/**
 * 
 * @author hmia
 *
 */
@Entity
@DiscriminatorValue(value = GlobalNames.DISCRIMINADOR_BATERIA)
public class Bateria extends Sensor implements Serializable {

	private static final long serialVersionUID = 1L;

	Bateria() {
		super();
	}

	/**
	 * 
	 * @param nombre
	 * @param contenedor
	 * @param activo
	 */
	public Bateria(String nombre, Contenedor contenedor, boolean activo) {
		super(nombre, contenedor, activo);
	}

	@Column(name = "BT_PORCENTAJE")
	public BigDecimal getPorcentajeBateria() {
		return porcentajeBateria;
	}

	public void setPorcentajeBateria(BigDecimal porcentaje) {
		this.porcentajeBateria = porcentaje;
	}

	private BigDecimal porcentajeBateria;
}
