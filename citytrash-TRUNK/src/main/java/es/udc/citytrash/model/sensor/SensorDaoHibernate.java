package es.udc.citytrash.model.sensor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.trabajador.Administrador;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

@Repository("SensorDao")
public class SensorDaoHibernate extends GenericHibernateDAOImpl<Sensor, Long> implements SensorDao {

	final Logger logger = LoggerFactory.getLogger(SensorDaoHibernate.class);

	@Override
	public List<Sensor> buscarSensoresByContenedor(Long ContenedorId) {
		String alias = "s";
		List<Sensor> sensores = new ArrayList<Sensor>();
		String hql = String.format("SELECT " + alias + " FROM Sensor " + alias + " WHERE " + alias
				+ ".contenedor.id = :contenedorId ORDER BY " + alias + ".id");
		logger.info("HQL buscarCamionModelosOrderByModelo => " + hql);
		sensores = getSession().createQuery(hql, Sensor.class).setParameter("contenedorId", ContenedorId).list();
		//logger.info("buscar sensores by contenedor dao");
		//logger.info("buscar sensores by contenedor dao => " + sensores.toString());
		return sensores;
	}

	@Override
	public Sensor buscarSensorByNombre(Long id) throws InstanceNotFoundException {
		Sensor sensor = (Sensor) getSession().createQuery("SELECT s FROM Sensor a WHERE s.id = (:id)")
				.setParameter("id", id).uniqueResult();
		if (sensor == null)
			throw new InstanceNotFoundException(id, Sensor.class.getName());
		return sensor;
	}
}
