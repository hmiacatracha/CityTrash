package es.udc.fic.tfg.sensores.model.dumpster;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import es.udc.fic.tfg.sensores.model.townhall.TownHall;
import es.udc.fic.tfg.sensores.model.type.Type;

@Entity
@Immutable
@Table(name = "Dumpsters")
public class Dumpster {

	private Long id;
	@Column(name = "foreign_key_dumpster_id")
	private String key;
	@Column(name = "townhall")
	private TownHall town;
	@Column(name="dumspter_type")
	private Type type;
	private float latitude;
	private float longitude;
	@Column(name = "tons_capacity")
	private float tons;

	public Dumpster() {

	}

	public Dumpster(String key, TownHall town, Type type, float latitude, float longitude, float tons) {
		this.key = key;
		this.town = town;
		this.type = type;
		this.latitude = latitude;
		this.longitude = longitude;
		this.tons = tons;
	}

	@Column(name = "dumpstersId")
	@SequenceGenerator(name = "DumpsterIdGenerator", sequenceName = "dumpsterSeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "DumpsterIdGenerator")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "foreign_key_dumpster_id")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	public TownHall getTown() {
		return town;
	}

	public void setTown(TownHall town) {
		this.town = town;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getTons() {
		return tons;
	}

	public void setTons(float tons) {
		this.tons = tons;
	}
}
