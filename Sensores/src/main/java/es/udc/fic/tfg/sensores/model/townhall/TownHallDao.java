package es.udc.fic.tfg.sensores.model.townhall;

import es.udc.fic.tfg.sensores.model.daos.genericdao.GenericDao;
import es.udc.fic.tfg.sensores.model.util.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

public interface TownHallDao extends GenericDao<TownHall, Long> {

	TownHall findByName(String name) throws InstanceNotFoundException, DuplicateInstanceException;
}
