package es.udc.fic.tfg.sensores.model.dumpster;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.fic.tfg.sensores.model.daos.genericdao.GenericDaoHibernate;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

@Repository("DumpsterDao")
public class DumpsterDaoHibernate extends GenericDaoHibernate<Dumpster, Long> implements DumpsterDao {

	@SuppressWarnings("unchecked")
	@Override
	public Dumpster findByPublicKey(String public_key, long townHallId) throws InstanceNotFoundException {

		List<Dumpster> dumpsterL = (List<Dumpster>) getCurrentSession()
				.createQuery("SELECT d from Dumpster d where d.key = :key and d.town.id = :id")
				.setParameter("key", public_key).setParameter("id", townHallId).getResultList();

		if (dumpsterL.isEmpty())
			throw new InstanceNotFoundException(public_key, Dumpster.class.getName());

		return dumpsterL.get(0);

	}
}
