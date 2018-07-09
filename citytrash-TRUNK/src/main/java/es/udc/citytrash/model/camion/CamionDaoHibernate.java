package es.udc.citytrash.model.camion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
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

import es.udc.citytrash.model.camionModelo.CamionModelo;
import es.udc.citytrash.model.camionModelo.CamionModeloDaoHibernate;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

/**
 * 
 * @author hmia
 *
 */
@Repository("camionDao")
public class CamionDaoHibernate extends GenericHibernateDAOImpl<Camion, Long> implements CamionDao {

	final Logger logger = LoggerFactory.getLogger(CamionModeloDaoHibernate.class);

	@Override
	public Camion buscarByMatricula(String matricula) throws InstanceNotFoundException {
		String alias = "c";
		String hql = String.format("Select " + alias + " FROM Camion " + alias + " WHERE UPPER(" + alias
				+ ".matricula) LIKE UPPER(:matricula)");

		logger.info("HQL CamionDaoHibernate matriculaExistente => " + hql);
		Camion camion = getSession().createQuery(hql, Camion.class).setParameter("matricula", matricula).uniqueResult();

		if (camion == null)
			throw new InstanceNotFoundException(matricula, Camion.class.getName());
		return camion;
	}

	@Override
	public Camion buscarByNombre(String nombre) throws InstanceNotFoundException {
		String alias = "c";
		String hql = String.format(
				"Select " + alias + " FROM Camion " + alias + " WHERE UPPER(" + alias + ".nombre) LIKE UPPER(:nombre)");
		logger.info("paso1 buscarByNombre");
		Camion camion = getSession().createQuery(hql, Camion.class).setParameter("nombre", nombre).uniqueResult();
		logger.info("paso2 buscarByNombre");
		if (camion == null) {
			logger.info("paso3 buscarByNombre");
			throw new InstanceNotFoundException(nombre, Camion.class.getName());
		}
		logger.info("paso4 buscarByNombre");
		return camion;
	}

	@Override
	public Camion buscarByVin(String vin) throws InstanceNotFoundException {
		String alias = "c";
		String hql = String.format(
				"Select " + alias + " FROM Camion " + alias + " WHERE UPPER(" + alias + ".vin) LIKE UPPER(:vin)");
		Camion camion = getSession().createQuery(hql, Camion.class).setParameter("vin", vin).uniqueResult();

		if (camion == null)
			throw new InstanceNotFoundException(vin, Camion.class.getName());
		return camion;
	}

	@Override
	public List<Camion> buscarCamionesDisponiblesParaUnaRutaByTipo(List<TipoDeBasura> tipos) {
		HashSet<TipoDeBasura> tiposAux = tipos != null ? new HashSet<TipoDeBasura>(tipos) : new HashSet<TipoDeBasura>();
		Query<Camion> query;
		List<Camion> camiones = new ArrayList<Camion>();

		String alias = "c";

		StringBuilder hql = new StringBuilder(
				"Select " + alias + " FROM Camion " + alias + " inner join " + alias + ".modeloCamion mc ");

		hql.append(" WHERE (" + alias + ".activo = :activo) ");

		hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
				+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
				+ ".fechaBaja  is null))");

		/*
		 * if (tiposAux.size() > 0) { hql.append(" AND pk.tipo in (:tipos) "); }
		 */

		if (tiposAux.size() > 0) {
			hql.append(" AND :items = (select count(distinct pk.tipo) "
					+ "			from CamionModelo mc2 inner join mc2.tiposDeBasura t inner join t.pk pk"
					+ "  		where mc2.id = mc.id and pk.tipo in (:tipos)" + ")");
		}
		
		hql.append(" ORDER BY " + alias + ".nombre");
		query = getSession().createQuery(hql.toString(), Camion.class);
		query.setParameter("activo", true);
		query.setParameter("fecha", Calendar.getInstance().getTime());

		if (tiposAux.size() > 0) {
			long items = tiposAux.size();
			query.setParameter("items", items);
			query.setParameter("tipos", tiposAux);
		}
		// logger.info("fin camionDaoHibernate camiones 1");
		if (tiposAux.size() > 0)
			camiones = query.list();
		return camiones;
	}

	@Override
	public List<Camion> buscarCamiones(boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta) {
		Query<Camion> query;
		List<Camion> camiones = new ArrayList<Camion>();

		String alias = "c";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Camion " + alias);

		if (mostrarSoloActivos)
			hql.append(" WHERE (" + alias + ".activo = :activo) ");

		if (mostrarSoloActivos && mostrarSoloCamionesDeAlta)
			hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		else if (mostrarSoloCamionesDeAlta)
			hql.append(" WHERE ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja is null))");

		query = getSession().createQuery(hql.toString(), Camion.class);

		if (mostrarSoloActivos)
			query.setParameter("activo", mostrarSoloActivos);

		if (mostrarSoloCamionesDeAlta)
			query.setParameter("fecha", Calendar.getInstance().getTime());

		camiones = query.list();
		return camiones;
	}

	@Override
	public Page<Camion> buscarCamiones(Pageable pageable, boolean mostrarSoloActivos,
			boolean mostrarSoloCamionesDeAlta) {
		Query<Camion> query;
		List<Camion> camiones = new ArrayList<Camion>();

		Page<Camion> page = new PageImpl<Camion>(camiones, pageable, camiones.size());
		String alias = "c";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Camion " + alias);

		if (mostrarSoloActivos)
			hql.append(" WHERE (" + alias + ".activo = :activo) ");

		if (mostrarSoloActivos && mostrarSoloCamionesDeAlta)
			hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		else if (mostrarSoloCamionesDeAlta)
			hql.append(" WHERE ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja is null))");

		logger.info("IMPRIMIENDO 1 => " + hql.toString());
		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), Camion.class);

		if (mostrarSoloActivos)
			query.setParameter("activo", mostrarSoloActivos);

		if (mostrarSoloCamionesDeAlta)
			query.setParameter("fecha", Calendar.getInstance().getTime());

		logger.info("IMPRIMIENDO 2 => " + query.getQueryString());
		logger.info("IMPRIMIENDO 3 => " + query.getParameters().toString());

		camiones = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > camiones.size() ? camiones.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (camiones.size() - start >= 0) && (camiones.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Camion>(camiones.subList(start, end), pageable, camiones.size());
		} else {
			List<Camion> camionesAux = new ArrayList<Camion>();
			page = new PageImpl<Camion>(camionesAux, pageable, camionesAux.size());
		}
		return page;
	}

	@Override
	public Page<Camion> buscarCamionesByModeloTiposDeBasura(Pageable pageable, CamionModelo modelo,
			boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta, List<TipoDeBasura> tipos) {
		Query<Camion> query;
		List<Camion> camiones = new ArrayList<Camion>();
		Page<Camion> page = new PageImpl<Camion>(camiones, pageable, camiones.size());
		List<TipoDeBasura> tiposAux = tipos != null ? tipos : new ArrayList<TipoDeBasura>();
		String alias = "c";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Camion " + alias + " inner join " + alias
				+ ".modeloCamion mc " + "inner join mc.tiposDeBasura t inner join t.pk pk");

		// muestra solo los trabajadores de alta, los de baja no
		if (mostrarSoloActivos) {
			hql.append(" WHERE " + alias + ".activo = :activo ");
		}

		logger.info("buscarCamionesByModelo paso22");
		if ((mostrarSoloActivos) && modelo != null) {
			hql.append(" AND " + alias + ".modeloCamion = :modelo");
		} else if (modelo != null) {
			hql.append(" WHERE " + alias + ".modeloCamion = :modelo");
		}

		if ((mostrarSoloActivos || modelo != null) && tiposAux.size() > 0) {
			hql.append(" AND pk.tipo in (:tipos) ");
		} else if (tiposAux.size() > 0) {
			hql.append(" WHERE pk.tipo in (:tipos)");
		}

		if ((mostrarSoloActivos || modelo != null || tiposAux.size() > 0) && mostrarSoloCamionesDeAlta) {
			hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		} else if (mostrarSoloCamionesDeAlta) {
			hql.append(" WHERE ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja is null))");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);
		logger.info("buscarCamionesByModelo paso8");

		query = getSession().createQuery(hql.toString(), Camion.class);

		if (tiposAux.size() > 0)
			query.setParameter("tipos", tiposAux);

		if (mostrarSoloActivos)
			query.setParameter("activo", mostrarSoloActivos);

		if (modelo != null) {
			query.setParameter("modelo", modelo);
		}

		if (mostrarSoloCamionesDeAlta)
			query.setParameter("fecha", Calendar.getInstance().getTime());

		logger.info("IMPRIMIENDO 2 => " + query.getQueryString());
		logger.info("IMPRIMIENDO 3 => " + query.getParameters().toString());
		logger.info("IMPRIMIENDO 4 => " + pageable.toString());

		camiones = query.list();
		logger.info("IMPRIMIENDO 4.1 => " + pageable.toString());
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > camiones.size() ? camiones.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (camiones.size() - start >= 0) && (camiones.size() - end >= 0);

		logger.info("IMPRIMIENDO 5 => " + pageable.toString());
		if (rangoExistente) {
			page = new PageImpl<Camion>(camiones.subList(start, end), pageable, camiones.size());
			logger.info("IMPRIMIENDO 6 => " + pageable.toString());
		} else {
			List<Camion> camionesAux = new ArrayList<Camion>();
			page = new PageImpl<Camion>(camionesAux, pageable, camionesAux.size());
			logger.info("IMPRIMIENDO 7 => " + pageable.toString());
		}
		logger.info("IMPRIMIENDO 8 => " + pageable.toString());
		return page;
	}

	@Override
	public Page<Camion> buscarCamionesByModeloTiposDeBasuraYNombre(Pageable pageable, String palabrasClaves,
			CamionModelo modelo, boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta,
			List<TipoDeBasura> tipos) {
		Query<Camion> query;
		List<Camion> camiones = new ArrayList<Camion>();
		String[] palabras = palabrasClaves != null ? palabrasClaves.split(" ") : new String[0];
		Page<Camion> page = new PageImpl<Camion>(camiones, pageable, camiones.size());
		List<TipoDeBasura> tiposAux = tipos != null ? tipos : new ArrayList<TipoDeBasura>();
		String alias = "c";
		// StringBuilder hql = new StringBuilder("Select " + alias + " FROM
		// Camion " + alias);
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Camion " + alias + " inner join " + alias
				+ ".modeloCamion mc " + "inner join mc.tiposDeBasura t inner join t.pk pk");

		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".nombre) LIKE  LOWER (?) ");
			else
				hql.append(" WHERE LOWER(" + alias + ".nombre) LIKE  LOWER (?) ");
		}

		// muestra solo los trabajadores de alta, los de baja no
		if (mostrarSoloActivos)
			if (palabras.length > 0)
				hql.append(" AND " + alias + ".activo = :activo");
			else
				hql.append(" WHERE " + alias + ".activo = :activo ");

		if ((palabras.length > 0 || mostrarSoloActivos) && modelo != null) {
			hql.append(" AND " + alias + ".modeloCamion = :modelo");
		} else if (modelo != null) {
			hql.append(" WHERE " + alias + ".modeloCamion = :modelo");
		}

		if ((palabras.length > 0 || mostrarSoloActivos || modelo != null) && mostrarSoloCamionesDeAlta) {
			hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		} else if (mostrarSoloCamionesDeAlta) {
			hql.append(" WHERE ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		}

		if ((palabras.length > 0 || mostrarSoloActivos || modelo != null || mostrarSoloCamionesDeAlta)
				&& tiposAux.size() > 0) {
			hql.append(" AND pk.tipo in (:tipos) ");
		} else if (tiposAux.size() > 0) {
			hql.append(" WHERE pk.tipo in (:tipos)");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), Camion.class);

		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		if (mostrarSoloActivos)
			query.setParameter("activo", mostrarSoloActivos);

		if (modelo != null) {
			query.setParameter("modelo", modelo);
		}

		if (mostrarSoloCamionesDeAlta)
			query.setParameter("fecha", Calendar.getInstance().getTime());

		if (tiposAux.size() > 0)
			query.setParameter("tipos", tiposAux);

		camiones = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > camiones.size() ? camiones.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (camiones.size() - start >= 0) && (camiones.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Camion>(camiones.subList(start, end), pageable, camiones.size());
		} else {
			List<Camion> camionesAux = new ArrayList<Camion>();
			page = new PageImpl<Camion>(camionesAux, pageable, camionesAux.size());
		}
		return page;

	}

	@Override
	public Page<Camion> buscarCamionesByModeloTiposDeBasuraYMatricula(Pageable pageable, String palabrasClaves,
			CamionModelo modelo, boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta,
			List<TipoDeBasura> tipos) {
		logger.info("PAS01 HIBERNATE");
		Query<Camion> query;
		List<Camion> camiones = new ArrayList<Camion>();
		String[] palabras = palabrasClaves != null ? palabrasClaves.split(" ") : new String[0];
		Page<Camion> page = new PageImpl<Camion>(camiones, pageable, camiones.size());
		List<TipoDeBasura> tiposAux = tipos != null ? tipos : new ArrayList<TipoDeBasura>();
		String alias = "c";
		// StringBuilder hql = new StringBuilder("Select " + alias + " FROM
		// Camion " + alias);

		StringBuilder hql = new StringBuilder("Select distinct " + alias + " FROM CamionModelo " + alias
				+ " inner join " + alias + ".tiposDeBasura t inner join t.pk pk");

		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".matricula) LIKE  LOWER (?) ");
			else
				hql.append(" WHERE LOWER(" + alias + ".matricula) LIKE  LOWER (?) ");
		}

		// muestra solo los trabajadores de alta, los de baja no
		if (mostrarSoloActivos)
			if (palabras.length > 0)
				hql.append(" AND " + alias + ".activo = :activo");
			else
				hql.append(" WHERE " + alias + ".activo = :activo ");

		if ((palabras.length > 0 || mostrarSoloActivos) && modelo != null) {
			hql.append(" AND " + alias + ".modeloCamion = :modelo");
		} else if (modelo != null) {
			hql.append(" WHERE " + alias + ".modeloCamion = :modelo");
		}

		if ((palabras.length > 0 || mostrarSoloActivos || modelo != null) && mostrarSoloCamionesDeAlta) {
			hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		} else if (mostrarSoloCamionesDeAlta) {
			hql.append(" WHERE ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		}

		if ((palabras.length > 0 || mostrarSoloActivos || modelo != null || mostrarSoloCamionesDeAlta)
				&& tiposAux.size() > 0) {
			hql.append(" AND pk.tipo in (:tipos) ");
		} else if (tiposAux.size() > 0) {
			hql.append(" WHERE pk.tipo in (:tipos)");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), Camion.class);

		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		if (mostrarSoloActivos)
			query.setParameter("activo", mostrarSoloActivos);

		if (modelo != null) {
			query.setParameter("modelo", modelo);
		}

		if (mostrarSoloCamionesDeAlta)
			query.setParameter("fecha", Calendar.getInstance().getTime());

		if (tiposAux.size() > 0)
			query.setParameter("tipos", tiposAux);

		logger.info("IMPRIMIENDO PARAMETERS => " + query.getParameters().toString());
		camiones = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > camiones.size() ? camiones.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (camiones.size() - start >= 0) && (camiones.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Camion>(camiones.subList(start, end), pageable, camiones.size());
		} else {
			List<Camion> camionesAux = new ArrayList<Camion>();
			page = new PageImpl<Camion>(camionesAux, pageable, camionesAux.size());
		}
		return page;

	}

	@Override
	public Page<Camion> buscarCamionesByModeloTiposDeBasuraYVin(Pageable pageable, String palabrasClaves,
			CamionModelo modelo, boolean mostrarSoloActivos, boolean mostrarSoloCamionesDeAlta,
			List<TipoDeBasura> tipos) {
		Query<Camion> query;
		List<Camion> camiones = new ArrayList<Camion>();
		String[] palabras = palabrasClaves != null ? palabrasClaves.split(" ") : new String[0];
		List<TipoDeBasura> tiposAux = tipos != null ? tipos : new ArrayList<TipoDeBasura>();
		Page<Camion> page = new PageImpl<Camion>(camiones, pageable, camiones.size());
		String alias = "c";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Camion " + alias);

		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".vin) LIKE  LOWER (?) ");
			else
				hql.append(" WHERE LOWER(" + alias + ".vin) LIKE  LOWER (?) ");
		}

		// muestra solo los trabajadores de alta, los de baja no
		if (mostrarSoloActivos)
			if (palabras.length > 0)
				hql.append(" AND " + alias + ".activo = :activo");
			else
				hql.append(" WHERE " + alias + ".activo = :activo ");

		if ((palabras.length > 0 || mostrarSoloActivos) && modelo != null) {
			hql.append(" AND " + alias + ".modeloCamion = :modelo");
		} else if (modelo != null) {
			hql.append(" WHERE " + alias + ".modeloCamion = :modelo");
		}

		if ((palabras.length > 0 || mostrarSoloActivos || modelo != null) && mostrarSoloCamionesDeAlta) {
			hql.append(" AND ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		} else if (mostrarSoloCamionesDeAlta) {
			hql.append(" WHERE ((DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and DATE(" + alias
					+ ".fechaBaja)  > DATE(:fecha)) OR  ( DATE(" + alias + ".fechaAlta)  <= DATE(:fecha) and " + alias
					+ ".fechaBaja  is null))");
		}

		if ((palabras.length > 0 || mostrarSoloActivos || modelo != null || mostrarSoloCamionesDeAlta)
				&& tiposAux.size() > 0) {
			hql.append(" AND pk.tipo in (:tipos) ");
		} else if (tiposAux.size() > 0) {
			hql.append(" WHERE pk.tipo in (:tipos)");
		}
		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), Camion.class);

		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		if (mostrarSoloActivos)
			query.setParameter("activo", mostrarSoloActivos);

		if (modelo != null) {
			query.setParameter("modelo", modelo);
		}

		if (mostrarSoloCamionesDeAlta)
			query.setParameter("fecha", Calendar.getInstance().getTime());

		if (tiposAux.size() > 0)
			query.setParameter("tipos", tiposAux);

		camiones = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > camiones.size() ? camiones.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (camiones.size() - start >= 0) && (camiones.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Camion>(camiones.subList(start, end), pageable, camiones.size());
		} else {
			List<Camion> camionesAux = new ArrayList<Camion>();
			page = new PageImpl<Camion>(camionesAux, pageable, camionesAux.size());
		}
		return page;
	}
}