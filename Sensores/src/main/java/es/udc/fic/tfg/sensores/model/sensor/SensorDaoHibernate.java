package es.udc.fic.tfg.sensores.model.sensor;

import java.util.List;
import org.springframework.stereotype.Repository;

import es.udc.fic.tfg.sensores.model.daos.genericdao.GenericDaoHibernate;
import es.udc.fic.tfg.sensores.model.dumpster.Dumpster;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

@Repository("SensorDao")
public class SensorDaoHibernate extends GenericDaoHibernate<Sensor, Long> implements SensorDao {

	@SuppressWarnings("unchecked")
	@Override
	public Sensor findByPublicKey(String key, long dumspterId) throws InstanceNotFoundException {
		List<Sensor> sensorL = (List<Sensor>) getCurrentSession()
				.createQuery("SELECT s from Sensor s" + " where s.key= :key and s.dumpster.id = :townId")
				.setParameter("key", key).setParameter("id", dumspterId).getResultList();
		if (sensorL.isEmpty())
			throw new InstanceNotFoundException(key, Dumpster.class.getName());
		return sensorL.get(0);
	}
}
