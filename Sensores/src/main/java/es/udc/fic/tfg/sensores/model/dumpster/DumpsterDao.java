package es.udc.fic.tfg.sensores.model.dumpster;

import es.udc.fic.tfg.sensores.model.daos.genericdao.GenericDao;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;


public interface DumpsterDao extends GenericDao<Dumpster, Long> {

	public Dumpster findByPublicKey(String public_key, long townHallId) throws InstanceNotFoundException;

}
