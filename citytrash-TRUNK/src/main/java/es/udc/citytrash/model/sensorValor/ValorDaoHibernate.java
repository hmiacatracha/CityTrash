package es.udc.citytrash.model.sensorValor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

@Repository("ValoresDao")
public class ValorDaoHibernate extends GenericHibernateDAOImpl<Valor, ValorPk> implements ValorDao {

	final Logger logger = LoggerFactory.getLogger(ValorDaoHibernate.class);

	@Override
	public List<Valor> buscarValoresBySensor(Long sensorId, Date fechaInicio, Date fechaFin) {
		Query<Valor> query;
		List<Valor> valores = new ArrayList<Valor>();
		logger.info("BUSCAR VALORES BY SENSOR VALORDAO 1");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = null, toDate = null;

		String alias = "v";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Valor " + alias);

		if (sensorId != null)
			hql.append(" WHERE " + alias + ".sensor.id = :id");

		if (sensorId != null && (fechaInicio != null || fechaFin != null))
			hql.append(" AND ");
		else if (sensorId == null && (fechaInicio != null || fechaFin != null))
			hql.append(" WHERE ");

		logger.info("BUSCAR VALORES BY SENSOR VALORDAO 2");
		if (fechaInicio != null && fechaFin != null) {
			hql.append(" DATE(" + alias + ".pk.fechaHora) >= DATE(:fechaInicio) AND DATE(" + alias
					+ ".pk.fechaHora) <= DATE(:fechaFin) ");

		} else if (fechaInicio != null) {
			hql.append(" DATE(" + alias + ".pk.fechaHora) >= DATE(:fechaInicio)");

		} else if (fechaFin != null) {
			hql.append(" DATE(" + alias + ".pk.fechaHora) <= DATE(:fechaInicio)");
		}

		hql.append(" ORDER BY " + alias + ".pk.fechaHora Asc");

		logger.info("Consulta =>" + hql.toString());
		query = getSession().createQuery(hql.toString(), Valor.class);
		logger.info("BUSCAR VALORES BY SENSOR VALORDAO 3");

		if (sensorId != null)
			query.setParameter("id", sensorId);

		logger.info("BUSCAR VALORES BY SENSOR VALORDAO 4");

		if (fechaInicio != null && fechaFin != null) {
			logger.info("parametro1");
			query.setParameter("fechaInicio", fechaInicio);
			query.setParameter("fechaFin", fechaFin);
		} else if (fechaInicio != null) {
			logger.info("parametro2");
			query.setParameter("fechaInicio", fechaInicio);
			logger.info("parametro2");
		} else if (fechaFin != null) {
			logger.info("parametro3");
			query.setParameter("fechaFin", fechaFin);
			logger.info("parametro3");
		}

		logger.info("BUSCAR VALORES BY SENSOR VALORDAO FIN");
		valores = query.list();
		return valores;
	}

	@Override
	public Page<Valor> buscarValoresBySensor(Pageable pageable, Long sensorId) {
		Query<Valor> query;
		List<Valor> valores = new ArrayList<Valor>();
		logger.info("BUSCAR VALORES BY SENSOR VALORDAO");

		Page<Valor> page = new PageImpl<Valor>(valores, pageable, valores.size());
		String alias = "v";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Valor " + alias);

		if (sensorId != null)
			hql.append(" WHERE " + alias + ".sensor.id = :id)");

		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		logger.info("Consulta =>" + hql.toString());
		query = getSession().createQuery(hql.toString(), Valor.class);

		if (sensorId != null)
			query.setParameter("id", sensorId);

		valores = query.list();
		logger.info("valores =>" + valores.toString());
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > valores.size() ? valores.size() : (start + pageable.getPageSize());
		boolean rangoExistente = (valores.size() - start >= 0) && (valores.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Valor>(valores.subList(start, end), pageable, valores.size());
		} else {
			List<Valor> contenedoresAux = new ArrayList<Valor>();
			page = new PageImpl<Valor>(contenedoresAux, pageable, contenedoresAux.size());
		}
		return page;

	}
}
