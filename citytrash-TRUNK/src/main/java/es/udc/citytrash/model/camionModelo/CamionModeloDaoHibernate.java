package es.udc.citytrash.model.camionModelo;

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

import es.udc.citytrash.model.camion.Camion;
import es.udc.citytrash.model.tipoDeBasura.TipoDeBasura;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.model.util.genericdao.GenericHibernateDAOImpl;

@Repository("camionModeloDao")
public class CamionModeloDaoHibernate extends GenericHibernateDAOImpl<CamionModelo, Integer>
		implements CamionModeloDao {
	final Logger logger = LoggerFactory.getLogger(CamionModeloDaoHibernate.class);

	@Override
	public CamionModelo buscarModeloPorNombre(String nombre) throws InstanceNotFoundException {
		String alias = "m";

		String hql = String.format("Select " + alias + " FROM CamionModelo " + alias + " WHERE UPPER(" + alias
				+ ".modelo) LIKE UPPER(:nombre)");

		CamionModelo modelo = getSession().createQuery(hql, CamionModelo.class).setParameter("nombre", nombre)
				.uniqueResult();
		if (modelo == null) {
			throw new InstanceNotFoundException(nombre, CamionModelo.class.getName());

		}
		return modelo;
	}

	@Override
	public List<CamionModelo> buscarTodosOrderByModelo() {
		String alias = "m";
		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		String hql = String
				.format("Select " + alias + " FROM CamionModelo " + alias + " ORDER BY " + alias + ".modelo");
		logger.info("HQL buscarCamionModelosOrderByModelo => " + hql);
		modelos = getSession().createQuery(hql, CamionModelo.class).list();
		return modelos;
	}

	@Override
	public Page<CamionModelo> buscarCamionModelo(Pageable pageable) {
		String alias = "m";
		Query<CamionModelo> query;
		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		Page<CamionModelo> page = new PageImpl<CamionModelo>(modelos, pageable, modelos.size());
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM CamionModelo " + alias);

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), CamionModelo.class);

		modelos = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > modelos.size() ? modelos.size() : (start + pageable.getPageSize());
		boolean rangoExistente = (modelos.size() - start >= 0) && (modelos.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<CamionModelo>(modelos.subList(start, end), pageable, modelos.size());
		} else {
			List<CamionModelo> modeloAux = new ArrayList<CamionModelo>();
			page = new PageImpl<CamionModelo>(modeloAux, pageable, modeloAux.size());
		}
		return page;
	}

	@Override
	public Page<CamionModelo> buscarCamionModelo(Pageable pageable, String palabrasClaveModelo) {
		String alias = "m";
		Query<CamionModelo> query;
		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		Page<CamionModelo> page = new PageImpl<CamionModelo>(modelos, pageable, modelos.size());
		StringBuilder hql = new StringBuilder("Select " + alias + " FROM CamionModelo " + alias);
		String[] palabras = palabrasClaveModelo.length() > 0 ? palabrasClaveModelo.split(" ") : new String[0];

		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				hql.append(" AND LOWER(" + alias + ".modelo) LIKE  LOWER (?) ");
			else
				hql.append(" WHERE LOWER(" + alias + ".modelo) LIKE  LOWER (?) ");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		hql.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), CamionModelo.class);

		/* set parameters */
		for (int i = 0; i < palabras.length; i++) {
			query.setParameter(i, "%" + palabras[i] + "%");
		}

		modelos = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > modelos.size() ? modelos.size() : (start + pageable.getPageSize());
		boolean rangoExistente = (modelos.size() - start >= 0) && (modelos.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<CamionModelo>(modelos.subList(start, end), pageable, modelos.size());
		} else {
			List<CamionModelo> modeloAux = new ArrayList<CamionModelo>();
			page = new PageImpl<CamionModelo>(modeloAux, pageable, modeloAux.size());
		}
		return page;
	}

	@Override
	public Page<CamionModelo> buscarCamionModelo(Pageable pageable, String palabrasClaveModelo, TipoDeBasura tipo) {
		String alias = "m";
		Query<CamionModelo> query;
		List<CamionModelo> modelos = new ArrayList<CamionModelo>();
		Page<CamionModelo> page = new PageImpl<CamionModelo>(modelos, pageable, modelos.size());
		String[] palabras = palabrasClaveModelo.length() > 0 ? palabrasClaveModelo.split(" ") : new String[0];

		StringBuilder hql = new StringBuilder("Select " + alias + " FROM CamionModelo " + alias);
		StringBuilder condicion = new StringBuilder();
		StringBuilder orden = new StringBuilder();

		for (int i = 0; i < palabras.length; i++) {
			if (i != 0)
				condicion.append(" AND LOWER(" + alias + ".modelo) LIKE  LOWER (?) ");
			else
				condicion.append(" WHERE LOWER(" + alias + ".modelo) LIKE  LOWER (?) ");
		}

		if ((palabras.length > 0) && tipo != null) {
			hql.append(" AND " + alias + ".t = :modelo");
		} else if (tipo != null) {
			hql.append(" WHERE " + alias + ".modeloCamion = :modelo");
		}

		/* Sorted */
		String order = StringUtils
				.collectionToCommaDelimitedString(StreamSupport.stream(pageable.getSort().spliterator(), false)
						.map(o -> alias + "." + o.getProperty() + " " + o.getDirection()).collect(Collectors.toList()));
		orden.append(" ORDER BY " + order);

		query = getSession().createQuery(hql.toString(), CamionModelo.class);

		modelos = query.list();
		int start = pageable.getOffset();
		int end = (start + pageable.getPageSize()) > modelos.size() ? modelos.size() : (start + pageable.getPageSize());
		boolean rangoExistente = (modelos.size() - start >= 0) && (modelos.size() - end >= 0);

		if (rangoExistente) {
			page = new PageImpl<CamionModelo>(modelos.subList(start, end), pageable, modelos.size());
		} else {
			List<CamionModelo> modeloAux = new ArrayList<CamionModelo>();
			page = new PageImpl<CamionModelo>(modeloAux, pageable, modeloAux.size());
		}
		return page;
	}

}