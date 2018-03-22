package es.udc.citytrash.business.repository.genericdao;

import java.io.Serializable;
import java.util.List;

import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;

public interface GenericDAO<T, ID extends Serializable> {

	T findById(ID id) throws InstanceNotFoundException;

	List<T> findAll();

	T save(T entity);

	void delete(T entity);

	boolean exists(ID id);

}