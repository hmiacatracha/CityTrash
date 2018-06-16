package es.udc.citytrash.model.sensor;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.util.GlobalNames;

@Entity
@DiscriminatorValue(value = GlobalNames.DISCRIMINADOR_TEMPERATURA)
public class Temperatura extends Sensor implements Serializable {

	private static final long serialVersionUID = 1L;

	Temperatura() {
		super();
	}

	/**
	 * 
	 * @param nombre
	 * @param contenedor
	 * @param activo
	 */
	public Temperatura(String nombre, Contenedor contenedor, boolean activo) {
		super(nombre, contenedor, activo);
	}

	@Column(name = "TE_CENTIGRADOS")
	public BigDecimal getGradosCentigrados() {
		return gradosCentigrados;
	}

	public void setGradosCentigrados(BigDecimal porcentaje) {
		this.gradosCentigrados = porcentaje;
	}

	private BigDecimal gradosCentigrados;
}
