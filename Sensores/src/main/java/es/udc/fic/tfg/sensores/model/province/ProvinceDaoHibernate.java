package es.udc.fic.tfg.sensores.model.province;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.fic.tfg.sensores.model.daos.genericdao.GenericDaoHibernate;
import es.udc.fic.tfg.sensores.model.dumpster.Dumpster;
import es.udc.fic.tfg.sensores.model.util.exceptions.DuplicateInstanceException;
import es.udc.fic.tfg.sensores.model.util.exceptions.InstanceNotFoundException;

@Repository("ProvinceDao")
public class ProvinceDaoHibernate extends GenericDaoHibernate<Province, Integer> implements ProvinceDao {

	@SuppressWarnings("unchecked")
	@Override
	public Province findByName(String name) throws InstanceNotFoundException, DuplicateInstanceException {
		List<Province> province = getCurrentSession()
				.createQuery("SELECT p from Province t where upper(p.name) = :name")
				.setParameter("name", name.toUpperCase()).getResultList();
		if (province.isEmpty())
			throw new InstanceNotFoundException(name, Dumpster.class.getName());
		return province.get(0);
	}

}
