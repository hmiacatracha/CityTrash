package es.udc.citytrash.business.entity.provincia;


import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;

@Entity
@Immutable
@Table(name = "TBL_PROVINCIA")
@BatchSize(size = 10)
public class Provincia {
	
}
