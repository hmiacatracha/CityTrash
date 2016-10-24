package es.udc.fic.tfg.sensores.model.sensor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import es.udc.fic.tfg.sensores.model.dumpster.Dumpster;
import es.udc.fic.tfg.sensores.model.type.Type;

@Entity
@Immutable
@Table(name = "Sensors")
public class Sensor {

	@Column(name="sensorId")
	private Long id;
	@Column(name="foreign_key_sensor_id")
	private String key;
	private Dumpster container;
	@Column(name="sensor_type")
	private Type type;
	private String unity;
	
	public Sensor() {
		
	}
	
	public Sensor(String key, Dumpster container, Type type, String unity) {
		super();
		this.key = key;
		this.container = container;
		this.type = type;
		this.unity = unity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="containerId")
	public Dumpster getContainer() {
		return container;
	}

	public void setContainer(Dumpster container) {
		this.container = container;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getUnity() {
		return unity;
	}

	public void setUnity(String unity) {
		this.unity = unity;
	}

	@Override
	public String toString() {
		return "Sensor [id=" + id + ", key=" + key + ", container=" + container + ", type=" + type + ", unity=" + unity
				+ "]";
	}		
}
