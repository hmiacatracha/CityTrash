package es.udc.tfg.model.type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "Types")
public class Type {

	@Column(name = "masadasd")
	private String name;

	private String description;

	public Type() {

	}

	public Type(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Id
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Type [name=" + name + ", description=" + description + "]";
	}
}
