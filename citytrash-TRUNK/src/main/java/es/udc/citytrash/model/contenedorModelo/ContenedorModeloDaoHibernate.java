package es.udc.citytrash.model.contenedorModelo;

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

import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

@Repository("contenedorModeloDao")
public class ContenedorModeloDaoHibernate extends GenericHibernateDAOImpl<ContenedorModelo, Integer>
		implements ContenedorModeloDao {
	final Logger logger = LoggerFactory.getLogger(ContenedorModeloDaoHibernate.class);

	@Override
	public ContenedorModelo buscarModeloPorNombre(String nombre) throws InstanceNotFoundException {
		String alias = "m";

		String hql = String.format("Select " + alias + " FROM ContenedorModelo " + alias + " WHERE UPPER(" + alias
				+ ".modelo) LIKE UPPER(:nombre)");

		ContenedorModelo modelo = getSession().createQuery(hql, ContenedorModelo.class).setParameter("nombre", nombre)
				.uniqueResult();
		if (modelo == null) {
			throw new InstanceNotFoundException(nombre, ContenedorModelo.class.getName());

		}
		return modelo;
	}

	@Override
	public List<ContenedorModelo> buscarTodosOrderByModelo() {
		String alias = "m";
		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		String hql = String.format("Select " + alias + " FROM ContenedorModelo " + alias + " ORDER BY " + alias
				+ ".modelo, " + alias + ".tipo.tipo");
		modelos = getSession().createQuery(hql, ContenedorModelo.class).list();
		return modelos;
	}

	@Override
	public List<ContenedorModelo> buscarTodosOrderByModelo(List<Integer> tipos) {
		String alias = "m";
		Query<ContenedorModelo> query;
		List<Integer> tiposAux = tipos != null ? tipos : new ArrayList<Integer>();
		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		String hql = "";

		if (tiposAux.size() > 0) {
			hql = String.format("Select " + alias + " FROM ContenedorModelo " + alias + " WHERE " + alias
					+ ".tipo.id in (:tipos) ORDER BY " + alias + ".modelo, " + alias + ".tipo.tipo");

		} else {
			hql = String.format("Select " + alias + " FROM ContenedorModelo " + alias + " ORDER BY " + alias
					+ ".modelo, " + alias + ".tipo.tipo");
		}

		query = getSession().createQuery(hql.toString(), ContenedorModelo.class);
		if (tiposAux.size() > 0)
			query.setParameter("tipos", tiposAux);
		modelos = query.list();
		return modelos;
	}

	@Override
	public Page<ContenedorModelo> buscarContenedorModelo(Pageable pageable) {
		String alias = "m";
		Query<ContenedorModelo> query;
		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		Page<ContenedorModelo> page = new PageImpl<ContenedorModelo>(modelos, pageable, modelos.size());
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM ContenedorModelo " + alias);

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), ContenedorModelo.class);

		modelos = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > modelos.size() ? modelos.size() : (start + pageable.getPageSize());
		boolean rangoExistente = (modelos.size() - start >= 0) && (modelos.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<ContenedorModelo>(modelos.subList(start, end), pageable, modelos.size());
		} else {
			List<ContenedorModelo> modeloAux = new ArrayList<ContenedorModelo>();
			page = new PageImpl<ContenedorModelo>(modeloAux, pageable, modeloAux.size());
		}
		return page;
	}

	@Override
	public Page<ContenedorModelo> buscarContenedorModelo(Pageable pageable, String palabrasClaveModelo,
			List<TipoDeBasura> tipos) {
		String alias = "m";

		logger.info("buscarContenedorModelo paso1");
		Query<ContenedorModelo> query;
		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		Page<ContenedorModelo> page = new PageImpl<ContenedorModelo>(modelos, pageable, modelos.size());
		logger.info("buscarContenedorModelo paso2");
		String[] palabras = palabrasClaveModelo != null ? palabrasClaveModelo.split(" ") : new String[0];
		logger.info("buscarContenedorModelo paso3");
		List<TipoDeBasura> tiposAux = tipos != null ? tipos : new ArrayList<TipoDeBasura>();

		logger.info("buscarContenedorModelo paso4");
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM ContenedorModelo " + alias);

		logger.info("buscarContenedorModelo paso5");
		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".modelo) LIKE  LOWER (?) ");
			else
				hql.append(" WHERE LOWER(" + alias + ".modelo) LIKE  LOWER (?) ");
		}
		if ((palabras.length > 0) && (tiposAux.size() > 0)) {
			hql.append(" AND " + alias + ".tipo in (:tipos) ");
		} else if (tipos.size() > 0) {
			hql.append(" WHERE " + alias + ".tipo in (:tipos)");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		logger.info("buscarContenedorModelo paso6");
		query = getSession().createQuery(hql.toString(), ContenedorModelo.class);

		logger.info("buscarContenedorModelo paso7 => " + hql.toString());
		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		if (tiposAux.size() > 0)
			query.setParameter("tipos", tipos);

		logger.info("buscarContenedorModelo paso9");
		modelos = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > modelos.size() ? modelos.size() : (start + pageable.getPageSize());
		boolean rangoExistente = (modelos.size() - start >= 0) && (modelos.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<ContenedorModelo>(modelos.subList(start, end), pageable, modelos.size());
		} else {
			List<ContenedorModelo> modeloAux = new ArrayList<ContenedorModelo>();
			page = new PageImpl<ContenedorModelo>(modeloAux, pageable, modeloAux.size());
		}
		return page;
	}

}