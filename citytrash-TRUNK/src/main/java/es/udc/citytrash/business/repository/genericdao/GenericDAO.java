package es.udc.citytrash.business.repository.genericdao;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable> {

	T findById(ID id);

	List<T> findAll();

	T save(T entity);

	void delete(T entity);

	boolean exists(ID id);

}