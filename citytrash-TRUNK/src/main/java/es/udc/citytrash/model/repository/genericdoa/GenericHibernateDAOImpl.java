package es.udc.citytrash.model.repository.genericdoa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
	public T findById(ID id) {
		T entity = getSession().get(getPersistentClass(), id);
		if (entity == null) {
			// throw new InstanceException("", id,
			// getPersistentClass().getName());
		}
		return entity;

	}

	@Override
	public boolean exists(ID id) {
		T entity = getSession().get(getPersistentClass(), id);
		if (entity == null) {
			return false;
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		String findAllQueryStr = "select p from " + persistentClass.getName() + " p";
		List<T> objects = new ArrayList<T>();
		List<T> resultList = getSession().createQuery(findAllQueryStr).getResultList();
		objects = resultList;
		return objects;
	}

	@Override
	public T save(T entity) {
		getSession().saveOrUpdate(entity);
		return entity;
	}

	@Override
	public void delete(T entity) {
		getSession().delete(entity);
	}
}