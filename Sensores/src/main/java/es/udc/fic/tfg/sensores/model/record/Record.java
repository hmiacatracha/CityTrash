package es.udc.fic.tfg.sensores.model.record;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;

import es.udc.fic.tfg.sensores.model.sensor.Sensor;
//https://jaehoo.wordpress.com/2012/03/25/hibernate-composite-keys-with-annotations/

@Entity
@Immutable
@Table(name = "Records")
@IdClass(RecordSensordPK.class)
public class Record {	
	@Id
	@Column(name = "sensorId")	
	private Sensor sensor;
	
	@Id
	@Column(name = "timestamp_sample")
	private Calendar timestamp;
	private float value;
	
	
	public Record() {
		
	}

	public Record(Sensor sensor, Calendar timestamp, float value) {		
		this.sensor = sensor;
		this.timestamp = timestamp;
		this.value = value;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "sensorId")	
	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp_sample) {
		this.timestamp = timestamp_sample;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Record [sensor=" + sensor + ", timestamp_sample=" + timestamp + ", value=" + value + "]";
	}	
}
