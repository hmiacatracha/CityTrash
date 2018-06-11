package es.udc.citytrash.model.sensorService;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.citytrash.model.contenedor.ContenedorDao;
import es.udc.citytrash.model.sensor.SensorDao;
import es.udc.citytrash.model.sensorValor.Valor;
import es.udc.citytrash.model.sensorValor.ValorDao;

@Service("sensorService")
@Transactional
public class SensorServiceImpl implements SensorService {

	@Autowired
	SensorDao sensorDao;

	@Autowired
	ValorDao valorDao;

	@Autowired
	ContenedorDao cDao;

	final Logger logger = LoggerFactory.getLogger(SensorServiceImpl.class);

	/**
	 * metodo privado => cambia de fecha a calendar
	 * 
	 * @param date
	 * @return
	 */

	private static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public Page<Valor> buscarValoresBySensorId(Long sensorId, Pageable pageable) {
		return valorDao.buscarValoresBySensor(pageable, sensorId);
	}

}
