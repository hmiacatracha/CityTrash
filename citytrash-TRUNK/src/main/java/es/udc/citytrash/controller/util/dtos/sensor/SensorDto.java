package es.udc.citytrash.controller.util.dtos.sensor;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.udc.citytrash.model.sensor.Bateria;
import es.udc.citytrash.model.sensor.Sensor;
import es.udc.citytrash.model.sensor.Temperatura;
import es.udc.citytrash.util.enums.TipoSensor;

/**
 * 
 * @author hmia
 *
 */
public class SensorDto {

	final Logger logger = LoggerFactory.getLogger(SensorDto.class);

	/**
	 * 
	 */
	public SensorDto() {

	}

	public SensorDto(Sensor sensor) {
		this.id = sensor.getId();
		this.activo = sensor.isActivo();
		this.actualizoLista = true;
		this.nombre = sensor.getNombre();
		this.nuevo = false;
		this.ultimaActualizacion = sensor.getUltimaActualizacion() != null ? sensor.getUltimaActualizacion().getTime()
				: null;
		if (sensor instanceof Bateria) {
			this.sensorType = TipoSensor.BATERIA;
		} else if (sensor instanceof Temperatura) {
			this.sensorType = TipoSensor.TEMPERATURA;
		} else {
			this.sensorType = TipoSensor.VOLUMEN;
		}
	}

	private Long id;

	@NotNull(message = "{constraints.sensor.nombre}")
	@Size(min = 5, max = 100, message = "{constraints.sensor.nombre}")
	private String nombre;
	private Date ultimaActualizacion;
	private boolean activo;
	private boolean nuevo = true;
	private boolean actualizoLista = true;
	private TipoSensor sensorType = TipoSensor.VOLUMEN;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getUltimaActualizacion() {
		return ultimaActualizacion;
	}

	public void setUltimaActualizacion(Date ultimaActualizacion) {
		this.ultimaActualizacion = ultimaActualizacion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isNuevo() {
		return nuevo;
	}

	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
	}

	public boolean isActualizoLista() {
		return actualizoLista;
	}

	public void setActualizoLista(boolean actualizoLista) {
		this.actualizoLista = actualizoLista;
	}

	
	public TipoSensor getSensorType() {
		return sensorType;
	}

	public void setSensorType(TipoSensor sensorType) {
		this.sensorType = sensorType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sensorType == null) ? 0 : sensorType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		logger.info("pasa por el equals SensorDto");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorDto other = (SensorDto) obj;
		if (sensorType != other.sensorType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SensorDto [id=" + id + ", nombre=" + nombre + ", ultimaActualizacion=" + ultimaActualizacion
				+ ", activo=" + activo + ", nuevo=" + nuevo + ", actualizoLista=" + actualizoLista + ", sensorType="
				+ sensorType + "]";
	}
}
