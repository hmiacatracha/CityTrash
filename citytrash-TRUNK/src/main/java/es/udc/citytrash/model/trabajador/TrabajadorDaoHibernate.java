package es.udc.citytrash.model.trabajador;

import java.util.ArrayList;
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

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.excepciones.TokenInvalidException;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;
import es.udc.citytrash.util.enums.TipoTrabajador;

@Repository("TrabajadorDao")
public class TrabajadorDaoHibernate extends GenericHibernateDAOImpl<Trabajador, Long> implements TrabajadorDao {

	final Logger logger = LoggerFactory.getLogger(TrabajadorDaoHibernate.class);

	public Trabajador buscarTrabajadorPorEmail(String email) throws InstanceNotFoundException {
		Trabajador trabajador = (Trabajador) getSession()
				.createQuery("SELECT t FROM Trabajador t WHERE TRIM(UPPER(t.email)) like TRIM(UPPER(:email))")
				.setParameter("email", email).uniqueResult();
		if (trabajador == null) {
			logger.info("buscarTrabajadorPorEMail: email no encontrado");
			throw new InstanceNotFoundException(email, Trabajador.class.getName());
		} else {
			logger.info("buscarTrabajadorPorEMail: email encontrado => " + email);
			logger.info("buscarTrabajadorPorEMail: trabajador encontrado => " + trabajador);
			return trabajador;
		}
	}

	public Trabajador buscarTrabajadorIdToken(long id, String token) throws TokenInvalidException {
		Trabajador trabajador = (Trabajador) getSession()
				.createQuery("SELECT t FROM Trabajador t WHERE t.id = :id and t.token like :token")
				.setParameter("token", token).setParameter("id", id).uniqueResult();
		if (trabajador == null) {
			logger.info("buscarTrabajadorPorEMail: token no encontrado =>" + token);
			throw new TokenInvalidException(token, Trabajador.class.getName());
		} else {
			logger.info("buscarTrabajadorPorToken: token encontrado");
			return trabajador;
		}
	}

	@Override
	public Trabajador buscarTrabajadorPorDocumentoId(String documentoId) throws InstanceNotFoundException {
		Trabajador trabajador = (Trabajador) getSession()
				.createQuery("SELECT t FROM Trabajador t WHERE TRIM(UPPER(t.docId)) like TRIM(UPPER(:documento))")
				.setParameter("documento", documentoId).uniqueResult();
		if (trabajador == null) {
			logger.info("buscarTrabajadorPorDocumento: documento no encontrado");
			throw new InstanceNotFoundException(documentoId, Trabajador.class.getName());
		} else {
			logger.info("buscarTrabajadorPorDocumento: documento encontrado => " + documentoId);
			logger.info("buscarTrabajadorPorDocumento: trabajador encontrado => " + trabajador);
			return trabajador;
		}
	}

	@Override
	public List<Trabajador> buscarTrabajadoresOrderByApellidos(boolean mostrarActivos) {
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		String alias = "t";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Trabajador " + alias);
		/* Filtro tipo de trabajador */
		if (mostrarActivos)
			hql.append(" WHERE (" + alias + ".activeWorker = :activo) ");
		hql.append(" ORDER BY " + alias + ".apellidos");
		trabajadores = getSession().createQuery(hql.toString(), Trabajador.class).list();
		return trabajadores;
	}

	@Override
	public Page<Trabajador> buscarTrabajadores(Pageable pageable, TipoTrabajador tipo, Boolean mostrarTodos) {

		Query<Trabajador> query;
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		Page<Trabajador> page = new PageImpl<Trabajador>(trabajadores, pageable, trabajadores.size());
		String alias = "t";
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Trabajador " + alias);
		/* Filtro tipo de trabajador */

		if (!mostrarTodos)
			hql.append(" WHERE (" + alias + ".activeWorker = :activo) ");

		if (tipo != TipoTrabajador.NONE && tipo != null) {
			if (!mostrarTodos)
				hql.append(" AND " + alias + ".trabajadorType = :tipo");
			else
				hql.append(" WHERE " + alias + ".trabajadorType = :tipo");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), Trabajador.class);

		if (!mostrarTodos)
			query.setParameter("activo", true);

		if (tipo != TipoTrabajador.NONE && tipo != null) {
			query.setParameter("tipo", tipo.name());
		}

		trabajadores = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > trabajadores.size() ? trabajadores.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (trabajadores.size() - start >= 0) && (trabajadores.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Trabajador>(trabajadores.subList(start, end), pageable, trabajadores.size());
		} else {
			List<Trabajador> trabajadoresAux = new ArrayList<Trabajador>();
			page = new PageImpl<Trabajador>(trabajadoresAux, pageable, trabajadoresAux.size());
		}
		return page;
	}

	@Override
	public Page<Trabajador> buscarTrabajadoresPorNombreApellidosYTipo(Pageable pageable, String palabrasClaves,
			TipoTrabajador tipo, Boolean mostrarTodos) {
		String[] palabras = palabrasClaves.split(" ");
		Query<Trabajador> query;
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		Page<Trabajador> page = new PageImpl<Trabajador>(trabajadores, pageable, trabajadores.size());
		String alias = "t";

		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Trabajador " + alias);

		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND (LOWER(" + alias + ".nombre) LIKE  LOWER (?) or LOWER(" + alias
						+ ".apellidos) LIKE  LOWER (?))");
			else
				hql.append(" WHERE (LOWER(" + alias + ".nombre) LIKE  LOWER (?) or LOWER(" + alias
						+ ".apellidos) LIKE  LOWER (?))");
		}

		// muestra solo los trabajadores de alta, los de baja no
		if (!mostrarTodos)
			if (palabras.length > 0)
				hql.append(" AND (" + alias + ".activeWorker = :activo)");
			else
				hql.append(" WHERE (" + alias + ".activeWorker = :activo)");

		/* Filtro tipo de trabajador */
		if ((palabras.length > 0 || mostrarTodos == false) && (tipo != null && tipo != TipoTrabajador.NONE)) {
			hql.append(" AND " + alias + ".trabajadorType = :tipo");
		} else if (tipo != null && tipo != TipoTrabajador.NONE) {
			hql.append(" WHERE " + alias + ".trabajadorType = :tipo");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), Trabajador.class);

		/* set parameters */
		for (int i = 0, j = 0; i < palabras.length; i++) {
			query.setParameter(j, "%" + palabras[i] + "%");
			j++;
			query.setParameter(j, "%" + palabras[i] + "%");
			j++;
		}

		if (!mostrarTodos)
			query.setParameter("activo", true);

		if (tipo != null && tipo != TipoTrabajador.NONE) {
			query.setParameter("tipo", tipo.name());
		}

		trabajadores = query.list();

		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > trabajadores.size() ? trabajadores.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (trabajadores.size() - start >= 0) && (trabajadores.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Trabajador>(trabajadores.subList(start, end), pageable, trabajadores.size());
		} else {
			List<Trabajador> trabajadoresAux = new ArrayList<Trabajador>();
			page = new PageImpl<Trabajador>(trabajadoresAux, pageable, trabajadoresAux.size());
		}
		return page;
	}

	@Override
	public Page<Trabajador> buscarTrabajadoresPorDocumentoYTipo(Pageable pageable, String documento,
			TipoTrabajador tipo, Boolean mostrarTodos) {

		String[] palabras = documento.split(" ");
		Query<Trabajador> query;
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		Page<Trabajador> page = new PageImpl<Trabajador>(trabajadores, pageable, trabajadores.size());
		String alias = "t";

		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Trabajador " + alias);

		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".docId) LIKE  LOWER (?)");
			else
				hql.append(" WHERE LOWER(" + alias + ".docId) LIKE  LOWER (?)");
		}

		if (!mostrarTodos)
			if (palabras.length > 0)
				hql.append(" AND (" + alias + ".activeWorker = :activo)");
			else
				hql.append(" WHERE (" + alias + ".activeWorker = :activo)");

		/* Filtro tipo de trabajador */
		if ((palabras.length > 0 || mostrarTodos == false) && (tipo != null && tipo != TipoTrabajador.NONE)) {
			hql.append(" AND " + alias + ".trabajadorType = :tipo");
		} else if (tipo != null && tipo != TipoTrabajador.NONE) {
			hql.append(" WHERE " + alias + ".trabajadorType = :tipo");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), Trabajador.class);

		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		if (!mostrarTodos)
			query.setParameter("activo", true);

		if (tipo != null && tipo != TipoTrabajador.NONE) {
			query.setParameter("tipo", tipo.name());
		}

		logger.info("QUERY BUSCAR TRABAJADORES X EMAIL =>" + query.getQueryString());
		trabajadores = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > trabajadores.size() ? trabajadores.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (trabajadores.size() - start >= 0) && (trabajadores.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Trabajador>(trabajadores.subList(start, end), pageable, trabajadores.size());
		} else {
			List<Trabajador> trabajadoresAux = new ArrayList<Trabajador>();
			page = new PageImpl<Trabajador>(trabajadoresAux, pageable, trabajadoresAux.size());
		}
		return page;

	}

	@Override
	public Page<Trabajador> buscarTrabajadoresPorApellidosYTipo(Pageable pageable, String apellidos,
			TipoTrabajador tipo, Boolean mostrarTodos) {
		String[] palabras = apellidos.split(" ");
		Query<Trabajador> query;
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		Page<Trabajador> page = new PageImpl<Trabajador>(trabajadores, pageable, trabajadores.size());
		String alias = "t";

		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Trabajador " + alias);

		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".apellidos) LIKE  LOWER (?)");
			else
				hql.append(" WHERE LOWER(" + alias + ".apellidos) LIKE  LOWER (?)");
		}

		if (!mostrarTodos)
			if (palabras.length > 0)
				hql.append(" AND (" + alias + ".activeWorker = :activo)");
			else
				hql.append(" WHERE (" + alias + ".activeWorker = :activo)");

		/* Filtro tipo de trabajador */
		if ((palabras.length > 0 || mostrarTodos == false) && (tipo != null && tipo != TipoTrabajador.NONE)) {
			hql.append(" AND " + alias + ".trabajadorType = :tipo");
		} else if (tipo != null && tipo != TipoTrabajador.NONE) {
			hql.append(" WHERE " + alias + ".trabajadorType = :tipo");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		logger.info("CONSULTA HQL =>" + hql);
		query = getSession().createQuery(hql.toString(), Trabajador.class);

		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		if (!mostrarTodos)
			query.setParameter("activo", true);

		if (tipo != TipoTrabajador.NONE && tipo != null) {
			query.setParameter("tipo", tipo.name());
		}

		trabajadores = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > trabajadores.size() ? trabajadores.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (trabajadores.size() - start >= 0) && (trabajadores.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Trabajador>(trabajadores.subList(start, end), pageable, trabajadores.size());
		} else {
			List<Trabajador> trabajadoresAux = new ArrayList<Trabajador>();
			page = new PageImpl<Trabajador>(trabajadoresAux, pageable, trabajadoresAux.size());
		}
		return page;

	}

	@Override
	public Page<Trabajador> buscarTrabajadoresPorTelefonoYTipo(Pageable pageable, String telefono, TipoTrabajador tipo,
			Boolean mostrarTodos) {
		String[] palabras = telefono.split(" ");
		Query<Trabajador> query;
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		Page<Trabajador> page = new PageImpl<Trabajador>(trabajadores, pageable, trabajadores.size());
		String alias = "t";

		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Trabajador " + alias);
		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".telefono) LIKE  LOWER (?)");
			else
				hql.append(" WHERE LOWER(" + alias + ".telefono) LIKE  LOWER (?)");
		}

		if (!mostrarTodos)
			if (palabras.length > 0)
				hql.append(" AND (" + alias + ".activeWorker = :activo)");
			else
				hql.append(" WHERE (" + alias + ".activeWorker = :activo)");

		/* Filtro tipo de trabajador */
		if ((palabras.length > 0 || mostrarTodos == false) && (tipo != null && tipo != TipoTrabajador.NONE)) {
			hql.append(" AND " + alias + ".trabajadorType = :tipo");
		} else if (tipo != null && tipo != TipoTrabajador.NONE) {
			hql.append(" WHERE " + alias + ".trabajadorType = :tipo");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		logger.info("CONSULTA HQL =>" + hql);
		query = getSession().createQuery(hql.toString(), Trabajador.class);

		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		if (!mostrarTodos)
			query.setParameter("activo", true);

		if (tipo != null && tipo != TipoTrabajador.NONE) {
			query.setParameter("tipo", tipo.name());
		}

		trabajadores = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > trabajadores.size() ? trabajadores.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (trabajadores.size() - start >= 0) && (trabajadores.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Trabajador>(trabajadores.subList(start, end), pageable, trabajadores.size());
		} else {
			List<Trabajador> trabajadoresAux = new ArrayList<Trabajador>();
			page = new PageImpl<Trabajador>(trabajadoresAux, pageable, trabajadoresAux.size());
		}
		return page;
	}

	@Override
	public Page<Trabajador> buscarTrabajadoresPorCpYTipo(Pageable pageable, String cp, TipoTrabajador tipo,
			Boolean mostrarTodos) {
		String[] palabras = cp.split(" ");
		Query<Trabajador> query;
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		Page<Trabajador> page = new PageImpl<Trabajador>(trabajadores, pageable, trabajadores.size());
		String alias = "t";

		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Trabajador " + alias);

		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".cp) LIKE  LOWER (?)");
			else
				hql.append(" WHERE LOWER(" + alias + ".cp) LIKE  LOWER (?)");
		}

		if (!mostrarTodos)
			if (palabras.length > 0)
				hql.append(" AND (" + alias + ".activeWorker = :activo)");
			else
				hql.append(" WHERE (" + alias + ".activeWorker = :activo)");

		/* Filtro tipo de trabajador */
		if ((palabras.length > 0 || mostrarTodos == false) && (tipo != null && tipo != TipoTrabajador.NONE)) {
			hql.append(" AND " + alias + ".trabajadorType = :tipo");
		} else if (tipo != null && tipo != TipoTrabajador.NONE) {
			hql.append(" WHERE " + alias + ".trabajadorType = :tipo");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		logger.info("CONSULTA HQL =>" + hql);
		query = getSession().createQuery(hql.toString(), Trabajador.class);

		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		if (!mostrarTodos)
			query.setParameter("activo", true);

		if (tipo != null && tipo != TipoTrabajador.NONE) {
			query.setParameter("tipo", tipo.name());
		}

		trabajadores = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > trabajadores.size() ? trabajadores.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (trabajadores.size() - start >= 0) && (trabajadores.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Trabajador>(trabajadores.subList(start, end), pageable, trabajadores.size());
		} else {
			List<Trabajador> trabajadoresAux = new ArrayList<Trabajador>();
			page = new PageImpl<Trabajador>(trabajadoresAux, pageable, trabajadoresAux.size());
		}
		return page;

	}

	@Override
	public Page<Trabajador> buscarTrabajadoresPorEmailYTipo(Pageable pageable, String email, TipoTrabajador tipo,
			Boolean mostrarTodos) {

		String[] palabras = email.split(" ");
		Query<Trabajador> query;
		List<Trabajador> trabajadores = new ArrayList<Trabajador>();
		Page<Trabajador> page = new PageImpl<Trabajador>(trabajadores, pageable, trabajadores.size());
		String alias = "t";

		StringBuilder hql = new StringBuilder("Select " + alias + " FROM Trabajador " + alias);

		/* Palabras claves */
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".email) LIKE  LOWER (?)");
			else
				hql.append(" WHERE LOWER(" + alias + ".email) LIKE  LOWER (?)");
		}

		if (!mostrarTodos)
			if (palabras.length > 0)
				hql.append(" AND (" + alias + ".activeWorker = :activo) ");
			else
				hql.append(" WHERE (" + alias + ".activeWorker = :activo) ");

		/* Filtro tipo de trabajador */
		if ((palabras.length > 0 || mostrarTodos == false) && (tipo != null && tipo != TipoTrabajador.NONE)) {
			hql.append(" AND " + alias + ".trabajadorType = :tipo");
		} else if (tipo != null && tipo != TipoTrabajador.NONE) {
			hql.append(" WHERE " + alias + ".trabajadorType = :tipo");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		logger.info("CONSULTA HQL =>" + hql);
		query = getSession().createQuery(hql.toString(), Trabajador.class);

		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		if (!mostrarTodos)
			query.setParameter("activo", true);

		if (tipo != null && tipo != TipoTrabajador.NONE) {
			query.setParameter("tipo", tipo.name());
		}

		trabajadores = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > trabajadores.size() ? trabajadores.size()
				: (start + pageable.getPageSize());
		boolean rangoExistente = (trabajadores.size() - start >= 0) && (trabajadores.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<Trabajador>(trabajadores.subList(start, end), pageable, trabajadores.size());
		} else {
			List<Trabajador> trabajadoresAux = new ArrayList<Trabajador>();
			page = new PageImpl<Trabajador>(trabajadoresAux, pageable, trabajadoresAux.size());
		}
		return page;
	}

	@Override
	public void cambiarTipoDeTrabajador(long trabajadorId, TipoTrabajador tipo) throws InstanceNotFoundException {
		String hql = "update Trabajador set trabajadorType = :tipo where id = :id";

		int rowsAffected = getSession().createQuery(hql).setParameter("tipo", tipo.name())
				.setParameter("id", trabajadorId).executeUpdate();

		if (rowsAffected > 0) {
			System.out.println("Updated " + rowsAffected + " rows.");
			throw new InstanceNotFoundException(trabajadorId, Trabajador.class.getName());
		}
	}
}
