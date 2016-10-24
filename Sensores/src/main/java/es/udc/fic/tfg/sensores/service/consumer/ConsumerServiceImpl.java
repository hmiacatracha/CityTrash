package es.udc.fic.tfg.sensores.service.consumer;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fic.tfg.sensores.model.dumpster.Dumpster;
import es.udc.fic.tfg.sensores.model.dumpster.DumpsterDao;
import es.udc.fic.tfg.sensores.model.province.Province;
import es.udc.fic.tfg.sensores.model.province.ProvinceDao;
import es.udc.fic.tfg.sensores.model.record.Record;
import es.udc.fic.tfg.sensores.model.record.RecordDao;
import es.udc.fic.tfg.sensores.model.record.RecordSensordPK;
import es.udc.fic.tfg.sensores.model.sensor.Sensor;
import es.udc.fic.tfg.sensores.model.sensor.SensorDao;
import es.udc.fic.tfg.sensores.model.townhall.TownHall;
import es.udc.fic.tfg.sensores.model.townhall.TownHallDao;
import es.udc.fic.tfg.sensores.model.type.Type;
import es.udc.fic.tfg.sensores.model.type.TypeDao;
import es.udc.fic.tfg.sensores.model.util.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

@Service("consumerService")
@Transactional
public class ConsumerServiceImpl implements ConsumerService {

	@Autowired
	private DumpsterDao dumpsterDao;

	@Autowired
	private RecordDao recordDao;

	@Autowired
	private TownHallDao townDao;

	@Autowired
	private TypeDao typeDao;

	@Autowired
	private SensorDao sensorDao;

	@Autowired
	private ProvinceDao pronviceDao;

	@Override
	public Dumpster registerContenedor(String key, long townHallId, String type_id, float latitude, float longitude,
			float tons) throws DuplicateInstanceException, InstanceNotFoundException {
		Dumpster dumpster;
		TownHall town;
		Type type;
		try {
			// Buscamos a ver si existe este contenedor por ciudad y por key
			dumpster = dumpsterDao.findByPublicKey(key, townHallId);
			throw new DuplicateInstanceException(dumpster.getId(), dumpster.getClass().getName());
		} catch (InstanceNotFoundException e) {
			town = townDao.find(townHallId);
			type = typeDao.find(type_id);
			dumpster = new Dumpster(key, town, type, latitude, longitude, tons);
			dumpsterDao.save(dumpster);
			return dumpster;
		}
	}

	@Override
	public Record registerRecord(long sensorId, Calendar timestamp, float value)
			throws DuplicateInstanceException, InstanceNotFoundException {
		Record record;
		// Buscamos si existe el registro
		RecordSensordPK id = new RecordSensordPK();
		id.setSensorId(sensorId);
		id.setTimestamp(timestamp);
		try {
			record = recordDao.find(id);
			throw new DuplicateInstanceException(id.getKeys(), Record.class.getName());

		} catch (InstanceNotFoundException e) {
			Sensor sensor = sensorDao.find(sensorId);
			record = new Record(sensor, timestamp, value);
			recordDao.save(record);
			return record;
		}
	}

	@Override
	public TownHall registerTown(String name, String municipality, String province, float latitude, float longitude)
			throws DuplicateInstanceException, InstanceNotFoundException {
		TownHall town;
		Province pv = null;
		try {

			pv = pronviceDao.findByName(province);
			town = townDao.findByName(name);
			throw new DuplicateInstanceException(town.getId(), TownHall.class.getName());

		} catch (InstanceNotFoundException e) {
			// Province province, String name, float latitude, float longitude
			town = new TownHall(pv, name, latitude, longitude);
			townDao.save(town);
			return town;
		}
	}

	@Override
	public Sensor registerSensor(String key, long dumpsterId, String type_name, String unity)
			throws DuplicateInstanceException, InstanceNotFoundException {

		Dumpster dumpster = dumpsterDao.find(dumpsterId);
		Type type = typeDao.find(type_name);
		Sensor sensor;

		try {
			sensor = sensorDao.findByPublicKey(key, dumpsterId);
			throw new DuplicateInstanceException(sensor.getId(), TownHall.class.getName());
		} catch (InstanceNotFoundException e) {
			sensor = new Sensor(key, dumpster, type, unity);
			sensorDao.save(sensor);
			return sensor;
		}
	}

	@Override
	public TownHall findTown(String name) throws InstanceNotFoundException, DuplicateInstanceException {
		return townDao.findByName(name);
	}
}
