package es.udc.fic.tfg.sensores.model.record;

import org.springframework.stereotype.Repository;

import es.udc.fic.tfg.sensores.model.daos.genericdao.GenericDaoHibernate;
import es.udc.fic.tfg.sensores.model.record.RecordSensordPK;

@Repository("RecordDao")
public class RecordDaoHibernate extends GenericDaoHibernate<Record,RecordSensordPK> implements RecordDao{

}


