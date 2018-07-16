package es.udc.citytrash.model.contenedor;

import java.util.ArrayList;
import java.util.Calendar;
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

import es.udc.citytrash.model.contenedorModelo.ContenedorModelo;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

/**
 * 
 * @author hmia
 *
 */
@Repository("contenedorDao")
public class ContenedorDaoHibernate extends GenericHibernateDAOImpl<Contenedor, Long> implements ContenedorDao {

	final Logger logger = LoggerFactory.getLogger(ContenedorDaoHibernate.class);

	@Override
	public Contenedor buscarByNombre(String nombre) throws InstanceNotFoundException {
		String alias = "c";
		Query<Contenedor> query;
		String hql = String.format("Select " + alias + " FROM Contenedor " + alias + " WHERE UPPER(TRIM(" + alias
				+ ".nombre)) LIKE UPPER(TRIM(:nombre))");
		query = getSession().createQuery(hql.toString(), Contenedor.class);
		query.setParameter("nombre", nombre);
		Contenedor contenedor = query.uniqueResult();

		if (contenedor == null) {
			throw new InstanceNotFoundException(nombre, Contenedor.class.getName());
		}
		return contenedor;
	}

	@Override
	public List<Contenedor> buscarContenedores(boolean mostrarSoloActivos, boolean mostrarSoloContenedoresDeAlta) {
		Query<Contenedor> query;
		List<Contenedor> contenedores = new ArrayList<Contenedor>();

		String alias = "c";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Contenedor " + alias);

		if (mostrarSoloActivos)
			hql.append(" WHERE (" + alias + ".activo = :activo) ");

		if (mostrarSoloActivos && mostrarSoloContenedoresDeAlta)
			hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		else if (mostrarSoloContenedoresDeAlta)
			hql.append(" WHERE ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja is null))");

		query = getSession().createQuery(hql.toString(), Contenedor.class);

		if (mostrarSoloActivos)
			query.setParameter("activo", mostrarSoloActivos);

		if (mostrarSoloContenedoresDeAlta)
			query.setParameter("fecha", Calendar.getInstance().getTime());

		contenedores = query.list();
		return contenedores;
	}

	@Override
	public Page<Contenedor> buscarContenedores(Pageable pageable, boolean mostrarSoloActivos,
			boolean mostrarSoloContenedoresDeAlta) {
		Query<Contenedor> query;
		List<Contenedor> contenedores = new ArrayList<Contenedor>();

		Page<Contenedor> page = new PageImpl<Contenedor>(contenedores, pageable, contenedores.size());
		String alias = "c";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Contenedor " + alias);

		if (mostrarSoloActivos)
			hql.append(" WHERE (" + alias + ".activo = :activo) ");

		if (mostrarSoloActivos && mostrarSoloContenedoresDeAlta)
			hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		else if (mostrarSoloContenedoresDeAlta)
			hql.append(" WHERE ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja is null))");

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), Contenedor.class);

		if (mostrarSoloActivos)
			query.setParameter("activo", mostrarSoloActivos);

		if (mostrarSoloContenedoresDeAlta)
			query.setParameter("fecha", Calendar.getInstance().getTime());

		contenedores = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > contenedores.size() ? contenedores.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (contenedores.size() - start >= 0) && (contenedores.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Contenedor>(contenedores.subList(start, end), pageable, contenedores.size());
		} else {
			List<Contenedor> contenedoresAux = new ArrayList<Contenedor>();
			page = new PageImpl<Contenedor>(contenedoresAux, pageable, contenedoresAux.size());
		}
		return page;
	}

	@Override
	public Page<Contenedor> buscarContenedores(Pageable pageable, String palabrasClaves, ContenedorModelo modelo,
			List<TipoDeBasura> tiposDeBasura, boolean mostrarSoloActivos, boolean mostrarSoloContenedoresDeAlta) {
		Query<Contenedor> query;
		List<Contenedor> contenedores = new ArrayList<Contenedor>();
		String[] palabras = palabrasClaves != null ? palabrasClaves.split(" ") : new String[0];
		Page<Contenedor> page = new PageImpl<Contenedor>(contenedores, pageable, contenedores.size());
		List<TipoDeBasura> tipos = tiposDeBasura != null ? tiposDeBasura : new ArrayList<TipoDeBasura>();
		String alias = "c";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Contenedor " + alias);

		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".nombre) LIKE  LOWER (?) ");
			else
				hql.append(" WHERE LOWER(" + alias + ".nombre) LIKE  LOWER (?) ");
		}

		// mostrar por tipos
		if (palabras.length > 0 && tipos.size() > 0) {
			hql.append(" AND (" + alias + ".modelo.tipo) in   (:tipos) ");
		} else if (tipos.size() > 0) {
			hql.append(" WHERE (" + alias + ".modelo.tipo) in   (:tipos) ");
		}

		// muestra solo los trabajadores de alta, los de baja no
		if (mostrarSoloActivos)
			if (palabras.length > 0 || tipos.size() > 0)
				hql.append(" AND " + alias + ".activo = :activo");
			else
				hql.append(" WHERE " + alias + ".activo = :activo ");

		if ((palabras.length > 0 || tipos.size() > 0 || mostrarSoloActivos) && modelo != null) {
			hql.append(" AND " + alias + ".modelo = :modelo");
		} else if (modelo != null) {
			hql.append(" WHERE " + alias + ".modelo = :modelo");
		}

		if ((palabras.length > 0 || tipos.size() > 0 || mostrarSoloActivos || modelo != null)
				&& mostrarSoloContenedoresDeAlta) {
			hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		} else if (mostrarSoloContenedoresDeAlta) {
			hql.append(" WHERE ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), Contenedor.class);

		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		if (tipos.size() > 0)
			query.setParameter("tipos", tipos);

		if (mostrarSoloActivos)
			query.setParameter("activo", mostrarSoloActivos);

		if (modelo != null) {
			query.setParameter("modelo", modelo);
		}

		if (mostrarSoloContenedoresDeAlta)
			query.setParameter("fecha", Calendar.getInstance().getTime());

		contenedores = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > contenedores.size() ? contenedores.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (contenedores.size() - start >= 0) && (contenedores.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Contenedor>(contenedores.subList(start, end), pageable, contenedores.size());
		} else {
			List<Contenedor> contenedoresAux = new ArrayList<Contenedor>();
			page = new PageImpl<Contenedor>(contenedoresAux, pageable, contenedoresAux.size());
		}
		return page;

	}

	@Override
	public List<Contenedor> buscarContenedores(String palabrasClaves, ContenedorModelo modelo,
			List<TipoDeBasura> tiposDeBasura, boolean mostrarSoloActivos, boolean mostrarSoloContenedoresDeAlta) {
		Query<Contenedor> query;
		String[] palabras = palabrasClaves != null ? palabrasClaves.split(" ") : new String[0];
		List<TipoDeBasura> tipos = tiposDeBasura != null ? tiposDeBasura : new ArrayList<TipoDeBasura>();
		String alias = "c";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Contenedor " + alias);

		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".nombre) LIKE  LOWER (?) ");
			else
				hql.append(" WHERE LOWER(" + alias + ".nombre) LIKE  LOWER (?) ");
		}

		// mostrar por tipos
		if (palabras.length > 0 && tipos.size() > 0) {
			hql.append(" AND (" + alias + ".modelo.tipo) in   (:tipos) ");
		} else if (tipos.size() > 0) {
			hql.append(" WHERE (" + alias + ".modelo.tipo) in   (:tipos) ");
		}

		// muestra solo los trabajadores de alta, los de baja no
		if (mostrarSoloActivos)
			if (palabras.length > 0 || tipos.size() > 0)
				hql.append(" AND " + alias + ".activo = :activo");
			else
				hql.append(" WHERE " + alias + ".activo = :activo ");

		if ((palabras.length > 0 || tipos.size() > 0 || mostrarSoloActivos) && modelo != null) {
			hql.append(" AND " + alias + ".modelo = :modelo");
		} else if (modelo != null) {
			hql.append(" WHERE " + alias + ".modelo = :modelo");
		}

		if ((palabras.length > 0 || tipos.size() > 0 || mostrarSoloActivos || modelo != null)
				&& mostrarSoloContenedoresDeAlta) {
			hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		} else if (mostrarSoloContenedoresDeAlta) {
			hql.append(" WHERE ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		}

		hql.append(" ORDER BY " + alias + ".nombre");
		query = getSession().createQuery(hql.toString(), Contenedor.class);
		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}
		if (tipos.size() > 0)
			query.setParameter("tipos", tipos);
		if (mostrarSoloActivos)
			query.setParameter("activo", mostrarSoloActivos);
		if (modelo != null) {
			query.setParameter("modelo", modelo);
		}
		if (mostrarSoloContenedoresDeAlta)
			query.setParameter("fecha", Calendar.getInstance().getTime());
		return query.list();
	}

	@Override
	public List<Contenedor> buscarContenedores(List<Long> ids) {
		Query<Contenedor> query;
		List<Long> listaIds = ids != null ? ids : new ArrayList<Long>();

		if (listaIds.size() > 0) {
			String alias = "c";

			StringBuilder hql = new StringBuilder("Select " + alias + " FROM Contenedor " + alias);

			hql.append(" WHERE " + alias + ".id in (:lista)");
			hql.append(" ORDER BY " + alias + ".id");
			query = getSession().createQuery(hql.toString(), Contenedor.class);

			query.setParameter("lista", listaIds);
			return query.list();
		} else {
			return new ArrayList<Contenedor>();
		}
	}

	@Override
	public List<Contenedor> buscarContenedoresDisponilesParaUnaRutaByTipoDeBasura(Integer rutaId,
			List<TipoDeBasura> tiposDeBasura) {
		Query<Contenedor> query;
		String alias = "c";
		List<TipoDeBasura> tipos = tiposDeBasura != null ? tiposDeBasura : new ArrayList<TipoDeBasura>();
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Contenedor " + alias);
		List<Contenedor> contenedores = new ArrayList<Contenedor>();
		// muestra solo los contenedores de alta, los de baja no
		if (rutaId != null)
			hql.append(" WHERE (" + alias + ".ruta.id =  (:rutaId) OR " + alias + ".ruta is null) and " + alias
					+ ".activo = :activo ");
		else
			hql.append(" WHERE " + alias + ".ruta is null and " + alias + ".activo = :activo ");

		// mostrar por tipos
		if (tipos.size() > 0) {
			hql.append(" AND (" + alias + ".modelo.tipo) in   (:tipos) ");
		}

		hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
				+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
				+ ".fechaBaja  is null))");

		hql.append(" ORDER BY " + alias + ".nombre");

		query = getSession().createQuery(hql.toString(), Contenedor.class);
		if (tipos.size() > 0)
			query.setParameter("tipos", tipos);
		query.setParameter("activo", true);
		query.setParameter("fecha", Calendar.getInstance().getTime());
		if (rutaId != null)
			query.setParameter("rutaId", rutaId);

		if (tipos.size() > 0) {
			contenedores = query.list();
		}
		return contenedores;
	}

	@Override
	public List<Contenedor> buscarContenedoresByTiposDeBasura(List<Integer> tiposDeBasura) {
		Query<Contenedor> query;
		String alias = "c";
		List<Integer> tipos = tiposDeBasura != null ? tiposDeBasura : new ArrayList<Integer>();
		logger.info("buscarContenedoresByTiposDeBasura dao paso 1");
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Contenedor " + alias);
		List<Contenedor> contenedores = new ArrayList<Contenedor>();

		logger.info("buscarContenedoresByTiposDeBasura dao paso 2");
		// mostrar por tipos
		if (tipos.size() > 0) {
			hql.append(" WHERE (" + alias + ".modelo.tipo.id) in   (:tipos) ");
		}

		logger.info("buscarContenedoresByTiposDeBasura dao paso 3");
		hql.append(" ORDER BY " + alias + ".nombre");

		query = getSession().createQuery(hql.toString(), Contenedor.class);
		if (tipos.size() > 0)
			query.setParameter("tipos", tipos);

		logger.info("buscarContenedoresByTiposDeBasura dao paso 4");
		contenedores = query.list();
		logger.info("buscarContenedoresByTiposDeBasura dao paso 5");
		return contenedores;
	}

}