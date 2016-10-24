package es.udc.fic.tfg.sensores.model.daos.genericdao;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

@SuppressWarnings("unchecked")
@Repository
public class GenericDaoHibernate<Entity, PK extends Serializable> implements GenericDao<Entity, PK> {

	@Autowired
	private SessionFactory sessionFactory;
	private Class<Entity> entityClass;

	public GenericDaoHibernate() {
		entityClass = (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public void setCurretSession(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void save(Entity e) {
		getCurrentSession().saveOrUpdate(e);
	}

	@Override
	public Entity find(PK id) throws InstanceNotFoundException {
		Entity entity = (Entity) getCurrentSession().get(entityClass, id);
		if (entity == null) {
			throw new InstanceNotFoundException(id, entityClass.getName());
		}
		return entity;
	}

	@Override
	public void delete(PK id) throws InstanceNotFoundException {
		getCurrentSession().delete(find(id));
	}

	@Override
	public List<Entity> getAll() {
		return getCurrentSession().createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e").getResultList();
	}

	@Override
	public long count() {
		List<Entity> list = (List<Entity>) getCurrentSession()
				.createQuery("SELECT * FROM " + entityClass.getSimpleName() + " e").getResultList();
		return list.size();
	}
}
