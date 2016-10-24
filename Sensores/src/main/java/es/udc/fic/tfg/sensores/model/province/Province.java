package es.udc.fic.tfg.sensores.model.province;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "Provinces")
public class Province {

	@Column(name = "provinceId")
	private int id;
	@Column(name = "province")
	private String name;

	public Province(int provinceId, String name) {
		this.id = provinceId;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int provinceId) {
		this.id = provinceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
