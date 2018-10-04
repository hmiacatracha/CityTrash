package es.udc.citytrash.model.sensorValor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;
import es.udc.citytrash.util.enums.TipoComparativa;

@Repository("ValoresDao")
public class ValorDaoHibernate extends GenericHibernateDAOImpl<Valor, ValorPk> implements ValorDao {

	final Logger logger = LoggerFactory.getLogger(ValorDaoHibernate.class);

	@Override
	public List<Valor> buscarValoresBySensor(Long sensorId, Date fechaInicio, Date fechaFin) {
		Query<Valor> query;
		List<Valor> valores = new ArrayList<Valor>();
		// logger.info("BUSCAR VALORES BY SENSOR VALORDAO 1");
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

		// logger.info("BUSCAR VALORES BY SENSOR VALORDAO 2");
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
	public List<Valor> obtenerLosDosUltimosValoresDeUnSensor(long sensorId) {
		Query<Valor> query;
		List<Valor> valores = new ArrayList<Valor>();
		logger.info("obtenerLosDosUltimosValoresDeUnSensor");
		StringBuilder hql = new StringBuilder("Select v FROM Valor v  inner join v.pk key  WHERE v.sensor.id = :id"
				+ " ORDER BY key.fechaHora DESC ");

		logger.info("Consulta =>" + hql.toString());
		query = getSession().createQuery(hql.toString(), Valor.class);
		query.setMaxResults(2);
		query.setParameter("id", sensorId);
		valores = query.list();
		return valores;
	}

	@Override
	public Valor obtenerElUltimoValorDeUnSensor(long sensorId) throws InstanceNotFoundException {
		logger.info("obtenerElUltimoValorDeUnSensor");
		Query<Valor> query;
		StringBuilder hql = new StringBuilder(
				"Select v FROM Valor v  inner join v.pk key  WHERE v.sensor.id = :id ORDER BY key.fechaHora DESC ");
		query = getSession().createQuery(hql.toString(), Valor.class).setMaxResults(1).setParameter("id", sensorId);
		List<Valor> valor = query.list();
		if (valor.isEmpty())
			throw new InstanceNotFoundException(sensorId, Valor.class.getName());
		return valor.get(0);
	}

	@Override
	public Double buscarMediaDeVolumen(int tipoBasura, TipoComparativa tipoComparativa, Date fecha) {
		StringBuilder hql = new StringBuilder("");
		logger.info("BUSCAR MEDIA DE VOLUMEN 1");

		switch (tipoComparativa) {
		case YEAR:
			hql = new StringBuilder("Select AVG(v.valor) FROM Valor v inner join v.sensor s inner join s.contenedor c "
					+ " inner join c.modelo m inner join m.tipo tipo WHERE YEAR(v.pk.fechaHora) = YEAR(:fecha)  "
					+ " and tipo.id = :tipoBasura and TYPE(s) = Volumen");
			break;
		case MONTH:
			hql = new StringBuilder("Select AVG(v.valor) FROM Valor v inner join v.sensor s inner join s.contenedor c "
					+ " inner join c.modelo m inner join m.tipo tipo WHERE MONTH(v.pk.fechaHora) = MONTH(:fecha)  "
					+ " and tipo.id = :tipoBasura and TYPE(s) = Volumen");
			break;
		default:
			hql = new StringBuilder("Select AVG(v.valor) FROM Valor v inner join v.sensor s inner join s.contenedor c "
					+ " inner join c.modelo m inner join m.tipo tipo WHERE DAY(v.pk.fechaHora) = DAY(:fecha)  "
					+ " and tipo.id = :tipoBasura and TYPE(s) = Volumen");
			break;
		}

		logger.info("BUSCAR MEDIA DE VOLUMEN 2");
		Object resultado = getSession().createQuery(hql.toString()).setParameter("fecha", fecha)
				.setParameter("tipoBasura", tipoBasura).getSingleResult();
		logger.info("BUSCAR MEDIA DE VOLUMEN 3");
		System.out.println("AVG VOLUMEN -> " + resultado);
		Double valor;
		if (resultado != null)
			valor = new Double(resultado.toString());
		else
			valor = new Double(0);
		logger.info("BUSCAR MEDIA DE VOLUMEN 4");
		return valor;
	}
}
