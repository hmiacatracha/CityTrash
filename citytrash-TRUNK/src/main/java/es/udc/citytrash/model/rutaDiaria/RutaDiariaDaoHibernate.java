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

import es.udc.citytrash.model.contenedor.Contenedor;
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
	public Page<RutaDiaria> buscarRutasDiariasByTrabajador(long trabajadorId, Pageable pageable) {
		logger.info("buscarRutasDiarias 1");
		Query<RutaDiaria> query;
		List<RutaDiaria> rutasDiarias = new ArrayList<RutaDiaria>();
		Page<RutaDiaria> page = new PageImpl<RutaDiaria>(rutasDiarias, pageable, rutasDiarias.size());

		logger.info("buscarRutasDiarias 2");

		StringBuilder hql = new StringBuilder(
				"Select distinct r FROM RutaDiaria r left join r.recogedor1 t1 left join r.recogedor2 t2 left join "
						+ " r.conductor t3 left join r.camion cam");
		hql.append(" WHERE ( t1.id = :trabajadorId OR t2.id = :trabajadorId OR t3.id = :trabajadorId )");

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> "r." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		logger.info("HQL=>" + hql.toString());
		query = getSession().createQuery(hql.toString(), RutaDiaria.class);

		query.setParameter("trabajadorId", trabajadorId);
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
		StringBuilder hql = new StringBuilder("Select distinct " + alias + " FROM RutaDiaria " + alias);

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
			hql.append(" AND " + alias + ".id in (Select distinct(r1.id) "
					+ "	from RutaDiaria r1 left join r1.recogedor1 t1 left join r1.recogedor2 t2 left join r1.conductor t3 	"
					+ " where  " + alias
					+ ".id = r1.id  and t1.id in (:trabajadores) OR t2.id in (:trabajadores) OR t3.id in (:trabajadores) )");

		}

		logger.info("buscarRutasDiarias 4");
		if (camionList.size() > 0) {
			hql.append(" AND " + alias + ".id in ( select distinct(r2.id) from RutaDiaria r2 inner join r2.camion cam "
					+ " where " + alias + ".id = r2.id and cam.id in (:camiones) )");
		}

		logger.info("buscarRutasDiarias 5");
		if (contenList.size() > 0) {
			hql.append(" AND " + alias + ".id in ( select distinct(r3.id)  "
					+ " from RutaDiaria r3 inner join r3.rutaDiariaContenedores rdc inner join rdc.pk rdcpk inner join rdcpk.contenedor cont "
					+ " where " + alias + ".id = r3.id and cont.id in (:contenedores) )");
		}

		logger.info("buscarRutasDiarias 5");
		if (rutasList.size() > 0) {
			hql.append(
					" AND " + alias + ".id in (Select distinct(r4.id) from RutaDiaria r4 inner join r4.ruta ru where "
							+ alias + ".id = r4.id and ru.id in (:rutas) )");
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

	@Override
	public List<Contenedor> buscarContenedores(long rutaDiariaId) {
		logger.info("buscar rutas diarias contenedores 1");
		Query<Contenedor> query;
		List<Contenedor> rutasDiariaContenedores = new ArrayList<Contenedor>();

		logger.info("buscar rutas diarias contenedores 2");

		StringBuilder hql = new StringBuilder(
				"Select distinct c FROM RutaDiaria rd inner join rd.rutaDiariaContenedores rdc "
						+ " inner join rdc.pk pk inner join pk.rutaDiaria rd inner join pk.contenedor c");

		hql.append(
				" WHERE rd.id = :rutaDiariaId and ((rd.fechaHoraInicio is null and c.activo = (:activo)) OR (rd.fechaHoraInicio is not null)) ");

		hql.append(" ORDER BY rdc.id");

		logger.info("buscar rutas diarias contenedores 3");
		logger.info("HQL=>" + hql.toString());
		logger.info("buscar rutas diarias contenedores 4");
		query = getSession().createQuery(hql.toString(), Contenedor.class);
		logger.info("buscar rutas diarias contenedores 5");
		query.setParameter("rutaDiariaId", rutaDiariaId);
		query.setParameter("activo", true);
		rutasDiariaContenedores = query.list();
		return rutasDiariaContenedores;
	}
}