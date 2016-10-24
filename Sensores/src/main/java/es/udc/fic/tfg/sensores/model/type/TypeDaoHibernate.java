package es.udc.fic.tfg.sensores.model.type;

import org.springframework.stereotype.Repository;

import es.udc.fic.tfg.sensores.model.daos.genericdao.GenericDaoHibernate;

import es.udc.fic.tfg.sensores.model.type.TypeDao;

@Repository("TypeDao")
public class TypeDaoHibernate extends GenericDaoHibernate<Type, String> implements TypeDao {

}
