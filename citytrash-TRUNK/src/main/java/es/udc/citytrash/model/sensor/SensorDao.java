package es.udc.citytrash.model.sensor;

import java.util.List;

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericDAO;

public interface SensorDao extends GenericDAO<Sensor, Long> {

	/**
	 * Buscar sensores by contenedor id
	 * @param ContenedorId
	 * @return
	 */
	List<Sensor> buscarSensoresByContenedor(Long ContenedorId);

	/**
	 * Buscar sensor by nombre
	 * @param id
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Sensor buscarSensorByNombre(Long id) throws InstanceNotFoundException;

}
