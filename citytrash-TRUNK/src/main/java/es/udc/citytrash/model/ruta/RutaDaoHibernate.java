package es.udc.citytrash.model.ruta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.contenedor.Contenedor;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;
import org.hibernate.query.Query;

/**
 * 
 * @author hmia
 *
 */
@Repository("rutaDao")
public class RutaDaoHibernate extends GenericHibernateDAOImpl<Ruta, Integer> implements RutaDao {

	final Logger logger = LoggerFactory.getLogger(RutaDaoHibernate.class);

	@Override
	public List<Ruta> buscarRutas(boolean mostrarSoloRutasActivas) {
		Query<Ruta> query;
		List<Ruta> rutas = new ArrayList<Ruta>();

		StringBuilder hql = new StringBuilder("Select r FROM Ruta r ");

		if (mostrarSoloRutasActivas)
			hql.append(" WHERE r.activo = (:soloActivas) ");

		query = getSession().createQuery(hql.toString(), Ruta.class);

		if (mostrarSoloRutasActivas)
			query.setParameter("soloActivas", mostrarSoloRutasActivas);

		rutas = query.list();
		return rutas;
	}

	@Override
	public Page<Ruta> buscarRutas(Pageable pageable, List<Integer> tiposDeBasura, List<Long> trabajadores,
			List<Long> contenedores, List<Long> camiones, boolean mostrarSoloRutasActivas) {

		logger.info("buscarRutasDao2 1");
		Query<Ruta> query;
		List<Ruta> rutas = new ArrayList<Ruta>();
		Page<Ruta> page = new PageImpl<Ruta>(rutas, pageable, rutas.size());
		List<Integer> tiposList = tiposDeBasura != null ? tiposDeBasura : new ArrayList<Integer>();
		List<Long> trabajList = trabajadores != null ? trabajadores : new ArrayList<Long>();
		List<Long> contenList = contenedores != null ? contenedores : new ArrayList<Long>();
		List<Long> camionList = camiones != null ? camiones : new ArrayList<Long>();

		logger.info("buscarRutasDao2 2");
		String alias = "r";
		// http://www.sergiy.ca/how-to-write-many-to-many-search-queries-in-mysql-and-hibernate/

		StringBuilder hql = new StringBuilder("Select distinct " + alias + " FROM Ruta " + alias + " left join " + alias
				+ ".camion cam " + " left join " + alias + ".contenedores cont " + " left join " + alias
				+ ".tiposDeBasura tbs " + " left join cam.recogedor1 t1 " + "	left join cam.recogedor2 t2 "
				+ " left join cam.conductor t3 " + " left join cam.conductorSuplente t4");

		logger.info("buscarRutasDao 3");
		// mostrar rutas activas
		if (mostrarSoloRutasActivas)
			hql.append(" WHERE " + alias + ".activo = :activo ");

		logger.info("buscarRutasDao 4");
		// mostrar por tipos de basura
		if (mostrarSoloRutasActivas && tiposList.size() > 0) {
			hql.append(" AND tbs.id in   (:tiposDeBasura) ");
		} else if (tiposList.size() > 0) {
			hql.append(" WHERE tbs.id in   (:tiposDeBasura) ");
		}

		logger.info("buscarRutasDao 5");
		if ((mostrarSoloRutasActivas || tiposList.size() > 0) && trabajList.size() > 0) {
			hql.append(
					" AND ( t1.id in   (:trabajadores) OR t2.id in (:trabajadores) OR t3.id in (:trabajadores) OR t4.id in (:trabajadores)) ");
		} else if (trabajList.size() > 0) {
			hql.append(
					" WHERE ( t1.id in   (:trabajadores) OR t2.id in (:trabajadores) OR t3.id in (:trabajadores) OR t4.id in (:trabajadores)) ");
		}

		logger.info("buscarRutasDao 6");
		if ((mostrarSoloRutasActivas || tiposList.size() > 0 || trabajList.size() > 0) && camionList.size() > 0) {
			hql.append(" AND cam.id in (:camiones) ");
		} else if (camionList.size() > 0) {
			hql.append(" WHERE cam.id in (:camiones) ");
		}

		logger.info("buscarRutasDao 7");
		if ((mostrarSoloRutasActivas || tiposList.size() > 0 || trabajList.size() > 0 || camionList.size() > 0)
				&& contenList.size() > 0) {
			hql.append(" AND cont.id in (:contenedores) ");
		} else if (contenList.size() > 0) {
			hql.append(" WHERE cont.id in (:contenedores) ");
		}

		logger.info("buscarRutasDao 8");
		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		logger.info("buscarRutasDao 9");
		logger.info("HQL=>" + hql.toString());
		query = getSession().createQuery(hql.toString(), Ruta.class);

		logger.info("buscarRutasDao 10");
		/* set parameters */

		logger.info("buscarRutasDao 11");
		// mostrar rutas activas
		if (mostrarSoloRutasActivas)
			query.setParameter("activo", mostrarSoloRutasActivas);

		logger.info("buscarRutasDao 12");
		// parameter tipos de basura
		if (tiposList.size() > 0)
			query.setParameter("tiposDeBasura", tiposList);

		logger.info("buscarRutasDao 13");
		if (trabajList.size() > 0)
			query.setParameter("trabajadores", trabajList);

		logger.info("buscarRutasDao 14");
		// parameter camiones
		if (camionList.size() > 0)
			query.setParameter("camiones", camionList);

		logger.info("buscarRutasDao 15");
		if (contenList.size() > 0)
			query.setParameter("contenedores", contenList);

		logger.info("buscarRutasDao 16");
		rutas = query.list();
		logger.info("buscarRutasDao 17");
		// logger.info("Econtrados =>" + rutas.toString());
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > rutas.size() ? rutas.size() : (start + pageable.getPageSize());
		boolean rangoExistente = (rutas.size() - start >= 0) && (rutas.size() - end >= 0);

		logger.info("buscarRutasDao 18");
		if (rangoExistente) {
			page = new PageImpl<Ruta>(rutas.subList(start, end), pageable, rutas.size());
		} else {
			List<Ruta> rutasAux = new ArrayList<Ruta>();
			page = new PageImpl<Ruta>(rutasAux, pageable, rutasAux.size());
		}
		logger.info("buscarRutasDao 19");
		return page;
	}

	@Override
	public List<Ruta> buscarRutasSinGenerar(Date fecha, List<Integer> tiposDeBasura, List<Long> camiones) {
		logger.info("buscarRutasAGenerar 1");
		Query<Ruta> query;
		List<Ruta> rutas = new ArrayList<Ruta>();
		HashSet<Integer> tiposList = tiposDeBasura != null ? new HashSet<Integer>(tiposDeBasura)
				: new HashSet<Integer>();
		List<Long> camionList = camiones != null ? camiones : new ArrayList<Long>();

		logger.info("buscarRutasAGenerar 2");
		String alias = "r";
		// http://www.sergiy.ca/how-to-write-many-to-many-search-queries-in-mysql-and-hibernate/

		StringBuilder select = new StringBuilder("Select distinct " + alias + " FROM Ruta " + alias);

		StringBuilder condicion = new StringBuilder(" WHERE " + alias + ".activo = (:activo) and " + alias
				+ ".id not in (Select r1.id from RutaDiaria rd inner join rd.ruta r1 " + " where r1.id = " + alias
				+ ".id and DATE(rd.fecha) = DATE(:fecha))");

		logger.info("buscarRutasAGenerar 4");
		// mostrar por tipos de basuraF
		if (tiposList.size() > 0) {
			condicion.append(" AND :items  = (SELECT COUNT(distinct tb2.id)"
					+ "	From Ruta r1 inner join r1.tiposDeBasura tb2 Where r1.id = " + alias
					+ ".id and tb2.id in (:tiposDeBasura) " + ")");
		}

		logger.info("buscarRutasAGenerar 6");
		if (camionList.size() > 0) {
			select.append(" left join " + alias + ".camion cam ");
			condicion.append(" AND cam.id in (:camiones) ");
		}

		condicion.append(" ORDER BY " + alias + ".id");
		String querySql = select.append(" " + condicion).toString();
		logger.info("HQL=>" + querySql);
		query = getSession().createQuery(querySql, Ruta.class);

		logger.info("buscarRutasAGenerar 10");
		/* set parameters */
		query.setParameter("fecha", fecha != null ? fecha : Calendar.getInstance().getTime());
		query.setParameter("activo", true);

		logger.info("buscarRutasAGenerar 12");
		// parameter tipos de basura
		if (tiposList.size() > 0) {
			long items = tiposList.size();
			query.setParameter("items", items);
			query.setParameter("tiposDeBasura", tiposList);
		}

		logger.info("buscarRutasAGenerar 14");
		// parameter camiones
		if (camionList.size() > 0)
			query.setParameter("camiones", camionList);

		logger.info("buscarRutasAGenerar 16");
		rutas = query.list();
		logger.info("buscarRutasAGenerar 17");

		return rutas;
	}
}