package net.codejava;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Magazyny {
	private int id_magazynu;
	private String miasto;
	private String ulica;
	
	public Magazyny() {
	}

	public Magazyny(String miasto, String ulica) {
		this.miasto = miasto;
		this.ulica = ulica;
	}

	@Id
	@GeneratedValue(generator = "incrementor")
	@GenericGenerator(name = "incrementor", strategy = "increment")
	public int getId_magazynu() {
		return id_magazynu;
	}

	public void setId_magazynu(int id_magazynu) {
		this.id_magazynu = id_magazynu;
	}

	public String getMiasto() {
		return miasto;
	}

	public void setMiasto(String miasto) {
		this.miasto = miasto;
	}

	public String getUlica() {
		return ulica;
	}

	public void setUlica(String ulica) {
		this.ulica = ulica;
	}
	

}
