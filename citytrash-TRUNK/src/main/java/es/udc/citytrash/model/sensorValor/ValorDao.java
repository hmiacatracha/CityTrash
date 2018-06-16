package es.udc.citytrash.model.sensorValor;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.model.util.genericdao.GenericDAO;

public interface ValorDao extends GenericDAO<Valor, ValorPk> {

	/**
	 * Buscar valores de un sensor by id
	 * @param pageable
	 * @param sensorId
	 * @return pageable
	 */
	Page<Valor> buscarValoresBySensor(Pageable pageable, Long sensorId);

	/**
	 * Buscar sensores by sensor
	 * @param sensorId
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	List<Valor> buscarValoresBySensor(Long sensorId, Date fechaInicio, Date fechaFin);

}
