package es.udc.citytrash.model.util.genericdao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import es.udc.citytrash.model.trabajador.Trabajador;
import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;
import es.udc.citytrash.util.enums.TipoTrabajador;

public class GenericHibernateDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

	private Class<T> persistentClass;

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public GenericHibernateDAOImpl() {
		final Type genericSuperclass = getClass().getGenericSuperclass();
		final ParameterizedType parameterizedType = ((ParameterizedType) genericSuperclass);
		persistentClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFatory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@Override
	public T buscarById(ID id) throws InstanceNotFoundException {
		T entity = getSession().get(getPersistentClass(), id);
		if (entity == null) {
			throw new InstanceNotFoundException(id, persistentClass.getName().toString());
		}
		return entity;

	}

	@Override
	public boolean existe(ID id) {
		T entity = getSession().get(getPersistentClass(), id);
		if (entity == null) {
			return false;
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> buscarTodos() {
		String findAllQueryStr = "select p from " + persistentClass.getName() + " p";
		List<T> objects = new ArrayList<T>();
		List<T> resultList = getSession().createQuery(findAllQueryStr).getResultList();
		objects = resultList;
		return objects;
	}

	@Override
	public T guardar(T entity) {
		try {
			getSession().saveOrUpdate(entity);
			return entity;
		} catch (DataIntegrityViolationException ex) {
			throw ex;
		}
	}

	@Override
	public int createNativeQuery(String sql) throws Exception {
		try {
			return getSession().createNativeQuery(sql).executeUpdate();
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public void eliminar(T entity) {
		getSession().delete(entity);
	}
}