package es.udc.citytrash.model.contenedorModelo;

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
		String hql = String
				.format("Select " + alias + " FROM ContenedorModelo " + alias + " ORDER BY " + alias + ".modelo");
		modelos = getSession().createQuery(hql, ContenedorModelo.class).list();
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
		Query<ContenedorModelo> query;
		List<ContenedorModelo> modelos = new ArrayList<ContenedorModelo>();
		Page<ContenedorModelo> page = new PageImpl<ContenedorModelo>(modelos, pageable, modelos.size());
		String[] palabras = palabrasClaveModelo.length() > 0 ? palabrasClaveModelo.split(" ") : new String[0];

		StringBuilder hql = new StringBuilder("Select " + alias + " FROM ContenedorModelo " + alias);
		StringBuilder condicion = new StringBuilder();
		StringBuilder orden = new StringBuilder();

		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				condicion.append(" AND LOWER(" + alias + ".modelo) LIKE  LOWER (?) ");
			else
				condicion.append(" WHERE LOWER(" + alias + ".modelo) LIKE  LOWER (?) ");
		}

		if ((palabras.length > 0) && tipos != null) {
			hql.append(" AND " + alias + ".t.modelo = :modelo");
		} else if (tipos != null) {
			hql.append(" WHERE " + alias + ".modelo = :modelo");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		orden.append(" ORDER BY " + order);

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

}