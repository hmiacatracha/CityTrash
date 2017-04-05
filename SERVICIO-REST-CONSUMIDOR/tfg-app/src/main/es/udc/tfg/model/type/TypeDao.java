package es.udc.tfg.model.type;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("TypeDao")
public interface TypeDao extends CrudRepository<Type, String> {

	Type findByName(String name);
}
