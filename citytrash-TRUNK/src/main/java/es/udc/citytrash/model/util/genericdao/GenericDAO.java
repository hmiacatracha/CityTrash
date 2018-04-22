package es.udc.citytrash.model.util.genericdao;

import java.io.Serializable;
import java.util.List;

import es.udc.citytrash.model.util.excepciones.InstanceNotFoundException;

public interface GenericDAO<T, ID extends Serializable> {

	T buscarById(ID id) throws InstanceNotFoundException;

	List<T> buscarTodos();

	T guardar(T entity);

	void eliminar(T entity);

	boolean existe(ID id);

	int createNativeQuery(String sql) throws Exception;

}