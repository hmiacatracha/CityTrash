package es.udc.fic.tfg.sensores.model.daos.genericdao;

import java.io.Serializable;
import java.util.List;

import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

public interface GenericDao<Entity, PK extends Serializable> {

	void save(Entity t);

	Entity find(PK id) throws InstanceNotFoundException;

	void delete(PK id) throws InstanceNotFoundException;
	
	 public List<Entity> getAll() ;
	 
	 public long count();
}
