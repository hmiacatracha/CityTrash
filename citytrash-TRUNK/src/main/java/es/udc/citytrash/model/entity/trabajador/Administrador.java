package es.udc.citytrash.model.entity.trabajador;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "ADMINISTRADOR")
public class Administrador extends Trabajador implements Serializable {
	private static final long serialVersionUID = 1L;

}
