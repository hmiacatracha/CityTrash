package es.udc.fic.tfg.sensores.model.townhall;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.udc.fic.tfg.sensores.model.province.Province;

@Table(name = "TownHalls")
public class TownHall {

	private Long id;
	private Province province;
	@Column(name = "townHallCode")
	private int townCode;
	@Column(name = "DC")
	private int dc;
	private String name;
	private float latitude;
	private float longitude;

	public TownHall() {

	}

	public TownHall(Province province, int townCode, int dc, String name, float latitude, float longitude) {
		this.province = province;
		this.townCode = townCode;
		this.dc = dc;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public TownHall(Province province, String name, float latitude, float longitude) {
		this.province = province;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@SequenceGenerator(name = "TownhallIdGenerator", sequenceName = "townhallSeq")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "TownhallIdGenerator")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public int getTownCode() {
		return townCode;
	}

	public void setTownCode(int townCode) {
		this.townCode = townCode;
	}

	public int getDc() {
		return dc;
	}

	public void setDc(int dc) {
		this.dc = dc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "TownHall [id=" + id + ", province=" + province + ", townCode=" + townCode + ", dc=" + dc + ", name="
				+ name + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
}
