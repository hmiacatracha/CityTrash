package es.udc.fic.tfg.sensores.model.province;

import es.udc.fic.tfg.sensores.model.daos.genericdao.GenericDao;
import es.udc.fic.tfg.sensores.model.util.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

public interface ProvinceDao extends GenericDao<Province, Integer> {

	public Province findByName(String name) throws InstanceNotFoundException, DuplicateInstanceException;
}
