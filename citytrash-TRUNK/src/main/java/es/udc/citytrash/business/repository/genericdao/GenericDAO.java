package es.udc.citytrash.business.repository.genericdao;

import java.io.Serializable;
import java.util.List;

import es.udc.citytrash.business.util.excepciones.InstanceNotFoundException;

public interface GenericDAO<T, ID extends Serializable> {

	T buescarById(ID id) throws InstanceNotFoundException;

	List<T> buscarTodos();

	T guardar(T entity);

	void eliminar(T entity);

	boolean existe(ID id);

}