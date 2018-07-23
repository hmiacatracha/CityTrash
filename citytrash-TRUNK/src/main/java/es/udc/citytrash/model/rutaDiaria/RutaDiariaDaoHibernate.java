package es.udc.citytrash.model.rutaDiaria;

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

/**
 * 
 * @author hmia
 *
 */
@Repository("rutaDiariaDao")
public class RutaDiariaDaoHibernate extends GenericHibernateDAOImpl<RutaDiaria, Long> implements RutaDiariaDao {

	final Logger logger = LoggerFactory.getLogger(RutaDiariaDaoHibernate.class);

	@Override
	public Page<RutaDiaria> buscarRutasDiarias(Pageable pageable, Date desde, Date hasta, List<Integer> rutas,
			List<Long> trabajadores, List<Long> contenedores, List<Long> camiones) {

		logger.info("buscarRutasDiarias 1");
		Query<RutaDiaria> query;
		List<RutaDiaria> rutasDiarias = new ArrayList<RutaDiaria>();
		Page<RutaDiaria> page = new PageImpl<RutaDiaria>(rutasDiarias, pageable, rutasDiarias.size());
		List<Integer> rutasList = rutas != null ? rutas : new ArrayList<Integer>();
		List<Long> trabajList = trabajadores != null ? trabajadores : new ArrayList<Long>();
		List<Long> contenList = contenedores != null ? contenedores : new ArrayList<Long>();
		List<Long> camionList = contenedores != null ? camiones : new ArrayList<Long>();

		logger.info("buscarRutasDiarias 2");
		String alias = "rd";

		// StringBuilder hql = new StringBuilder("Select distinct " + alias + "
		// FROM RutaDiaria " + alias + " left join "
		// + alias + ".ruta r " + " left join " + alias
		// + ".rutaDiariaContenedores rdc left join rdc.pk rdcpk left join
		// rdcpk.contenedor cont" + " left join "
		// + alias + ".recogedor1 t1 left join " + alias + ".recogedor2 t2 left
		// join " + alias
		// + ".conductor t3 left join " + alias + ".camion cam");

		StringBuilder hql = new StringBuilder(
				"Select distinct " + alias + " FROM RutaDiaria " + alias + " left join " + alias + ".ruta r ");

		logger.info("buscarRutasDiarias 2.1");
		if (desde != null && hasta != null) {
			logger.info("buscarRutasDiarias 2.2");
			// hql.append(" WHERE DATE(" + alias + ".fecha) between
			// DATE(:fechaInicio) and DATE(:fechaFin) ");
			hql.append(" WHERE DATE(" + alias + ".fecha) >= DATE(:fechaInicio) and DATE(" + alias
					+ ".fecha) <= DATE(:fechaFin)");
		} else if (desde != null) {
			logger.info("buscarRutasDiarias 2.3");
			hql.append(" WHERE DATE(" + alias + ".fecha) >= DATE(:fechaInicio)");
		} else if (hasta != null) {
			logger.info("buscarRutasDiarias 2.4");
			hql.append(" WHERE DATE(" + alias + ".fecha) <= DATE(:fechaFin)");
		} else {
			logger.info("buscarRutasDiarias 2.5");
			hql.append(" WHERE DATE(" + alias + ".fecha) >= DATE(:fechaInicio) and DATE(" + alias
					+ ".fecha) <= DATE(:fechaFin)");
		}

		logger.info("buscarRutasDiarias 3");
		if (trabajList.size() > 0) {
			hql.append(" AND ( t1.id in (:trabajadores) OR t2.id in (:trabajadores) OR t3.id in (:trabajadores)) ");
		}

		logger.info("buscarRutasDiarias 4");
		if (camionList.size() > 0) {
			hql.append(" AND cam.id in (:camiones) ");
		}

		logger.info("buscarRutasDiarias 5");
		if (contenList.size() > 0) {
			hql.append(" AND cont.id in (:contenedores) ");
		}

		logger.info("buscarRutasDiarias 5");
		if (rutasList.size() > 0) {
			hql.append(" AND r.id in (:rutas) ");
		}

		logger.info("buscarRutasDiarias 6");
		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		logger.info("buscarRutasDiarias 7");
		logger.info("HQL=>" + hql.toString());
		query = getSession().createQuery(hql.toString(), RutaDiaria.class);

		logger.info("buscarRutasDiarias 8");
		/* set parameters */
		/* Fecha de inicio y fecha de fin */
		if (desde != null && hasta != null) {
			query.setParameter("fechaInicio", desde);
			query.setParameter("fechaFin", hasta);
		} else if (desde != null) {
			query.setParameter("fechaInicio", desde);
		} else if (hasta != null) {
			query.setParameter("fechaFin", hasta);
		} else {
			query.setParameter("fechaInicio", Calendar.getInstance().getTime());
			query.setParameter("fechaFin", Calendar.getInstance().getTime());
		}

		logger.info("buscarRutasDiarias 9");
		if (rutasList.size() > 0) {
			query.setParameter("rutas", rutasList);
		}

		logger.info("buscarRutasDiarias 10");
		if (trabajList.size() > 0)
			query.setParameter("trabajadores", trabajList);

		logger.info("buscarRutasDiarias 11");
		// parameter camiones
		if (camionList.size() > 0)
			query.setParameter("camiones", camionList);

		logger.info("buscarRutasDiarias 12");
		if (contenList.size() > 0)
			query.setParameter("contenedores", contenList);

		logger.info("buscarRutasDiarias 13");
		rutasDiarias = query.list();
		logger.info("buscarRutasDao 14");
		// logger.info("Econtrados =>" + rutas.toString());
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > rutasDiarias.size() ? rutasDiarias.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (rutasDiarias.size() - start >= 0) && (rutasDiarias.size() - end >= 0);

		logger.info("buscarRutasDiarias 15");
		if (rangoExistente) {
			page = new PageImpl<RutaDiaria>(rutasDiarias.subList(start, end), pageable, rutasDiarias.size());
		} else {
			List<RutaDiaria> rutasAux = new ArrayList<RutaDiaria>();
			page = new PageImpl<RutaDiaria>(rutasAux, pageable, rutasAux.size());
		}
		logger.info("buscarRutasDiarias 16");
		return page;
	}

	@Override
	public List<RutaDiaria> buscarRutasDiarias(Date fechaInicio, Date fechaFin, int rutaId) {
		logger.info("buscarRutasDiarias 1");
		Query<RutaDiaria> query;
		List<RutaDiaria> rutasDiarias = new ArrayList<RutaDiaria>();
		String alias = "rd";

		StringBuilder hql = new StringBuilder("Select distinct " + alias + " FROM RutaDiaria " + alias + " inner join "
				+ alias + ".ruta r WHERE r.id = (:rutaId) ");

		if (fechaInicio != null && fechaFin != null)
			hql.append(" AND DATE(" + alias + ".fecha) between DATE(:fechaInicio) and DATE(:fechaFin)");

		hql.append(" ORDER BY " + alias + ".id");
		logger.info("HQL=>" + hql.toString());
		query = getSession().createQuery(hql.toString(), RutaDiaria.class);
		query.setParameter("rutaId", rutaId);

		if (fechaInicio != null && fechaFin != null) {
			query.setParameter("fechaInicio", fechaInicio);
			query.setParameter("fechaFin", fechaInicio);
		}

		rutasDiarias = query.list();
		return rutasDiarias;
	}
}