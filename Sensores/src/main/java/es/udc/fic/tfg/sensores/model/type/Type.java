package es.udc.fic.tfg.sensores.model.type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "Types")
public class Type {

	@Column(name = "name")
	private String type;
	
	private String description;

	public Type() {

	}

	public Type(String type, String description) {
		this.type = type;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Type [type=" + type + ", description=" + description + "]";
	}
}
