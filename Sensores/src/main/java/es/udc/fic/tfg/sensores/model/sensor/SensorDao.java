package es.udc.fic.tfg.sensores.model.sensor;

import org.springframework.stereotype.Repository;

import es.udc.fic.tfg.sensores.model.daos.genericdao.GenericDao;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

@Repository("SensorDao")
public interface SensorDao extends GenericDao<Sensor, Long> {

	Sensor findByPublicKey(String public_key, long dumspterId) throws InstanceNotFoundException;
}
