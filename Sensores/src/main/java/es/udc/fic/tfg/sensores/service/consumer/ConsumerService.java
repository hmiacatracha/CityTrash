package es.udc.fic.tfg.sensores.service.consumer;

import java.util.Calendar;

import es.udc.fic.tfg.sensores.model.dumpster.Dumpster;
import es.udc.fic.tfg.sensores.model.record.Record;
import es.udc.fic.tfg.sensores.model.sensor.Sensor;
import es.udc.fic.tfg.sensores.model.townhall.TownHall;
import es.udc.fic.tfg.sensores.model.util.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

public interface ConsumerService {

	public Dumpster registerContenedor(String key, long townHallId, String type, float latitude, float longitude,
			float tons) throws DuplicateInstanceException, InstanceNotFoundException;

	public Record registerRecord(long sensorId, Calendar timestamp, float value)
			throws InstanceNotFoundException, DuplicateInstanceException;

	public TownHall registerTown(String name, String municipality, String province, float latitude, float longitude)
			throws DuplicateInstanceException, InstanceNotFoundException;

	public Sensor registerSensor(String key, long dumpsterId, String type_name, String unity)
			throws DuplicateInstanceException, InstanceNotFoundException;

	public TownHall findTown(String name) throws InstanceNotFoundException, DuplicateInstanceException;
}
