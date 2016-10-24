package es.udc.fic.tfg.sensores.model.record;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;

@SuppressWarnings("serial")
public class RecordSensordPK implements Serializable {

	@Column(name = "sensorId")
	private long sensorId;

	@Column(name = "timestamp_sample")
	private Calendar timestamp;

	public long getSensorId() {
		return sensorId;
	}

	public void setSensorId(long sensorId) {
		this.sensorId = sensorId;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public String getKeys() {
		return " Id: " + sensorId + " time: " + timestamp.getTime().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (sensorId ^ (sensorId >>> 32));
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordSensordPK other = (RecordSensordPK) obj;
		if (sensorId != other.sensorId)
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
}
