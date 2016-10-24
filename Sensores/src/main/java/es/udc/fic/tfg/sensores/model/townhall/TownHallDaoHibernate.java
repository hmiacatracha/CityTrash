package es.udc.fic.tfg.sensores.model.townhall;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.fic.tfg.sensores.model.daos.genericdao.GenericDaoHibernate;
import es.udc.fic.tfg.sensores.model.dumpster.Dumpster;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

@Repository("TownHallDao")
public class TownHallDaoHibernate extends GenericDaoHibernate<TownHall, Long> implements TownHallDao {

	@SuppressWarnings("unchecked")
	@Override
	public TownHall findByName(String name) throws InstanceNotFoundException {

		List<TownHall> towns = getCurrentSession().createQuery("SELECT t from TownHall t where upper(t.name) = :name")
				.setParameter("name", name.toUpperCase()).getResultList();
		if (towns.isEmpty())
			throw new InstanceNotFoundException(name, Dumpster.class.getName());
		return towns.get(0);
	}

}
