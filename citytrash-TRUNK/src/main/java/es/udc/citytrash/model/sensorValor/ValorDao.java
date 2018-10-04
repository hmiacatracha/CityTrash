package es.udc.citytrash.model.sensorValor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.udc.citytrash.controller.util.dtos.estadisticas.ComparativaPorTipoReciclado;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericDAO;
import es.udc.citytrash.util.enums.TipoComparativa;

public interface ValorDao extends GenericDAO<Valor, ValorPk> {

	/**
	 * Buscar sensores by sensor
	 * 
	 * @param sensorId
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	List<Valor> buscarValoresBySensor(Long sensorId, Date fechaInicio, Date fechaFin);

	/**
	 * Obtenenr los ultimos dos valores de un sensor ordenado por fecha
	 * 
	 * @param sensorId
	 * @return
	 */
	List<Valor> obtenerLosDosUltimosValoresDeUnSensor(long sensorId);

	/**
	 * Obtener el último valor de un sensor
	 * 
	 * @param sensorId
	 * @return
	 * @throws InstanceNotFoundException
	 */
	Valor obtenerElUltimoValorDeUnSensor(long sensorId) throws InstanceNotFoundException;

	/**
	 * 
	 * @param tipoBasura
	 * @param tipoComparativa
	 * @return
	 */
	Double buscarMediaDeVolumen(int tipoBasura, TipoComparativa tipoComparativa, Date fecha);

}
