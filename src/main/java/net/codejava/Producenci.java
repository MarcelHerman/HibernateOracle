package net.codejava;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Producenci {
	private int id_producenta;
	private String nazwa;
	private String kontakt;
	private String miasto;
	private String ulica;
	public Producenci() {
	}
	public Producenci(String nazwa, String kontakt, String miasto, String ulica) {
		this.nazwa = nazwa;
		this.kontakt = kontakt;
		this.miasto = miasto;
		this.ulica = ulica;
	}
	
	@Id
	@GeneratedValue(generator = "incrementor")
	@GenericGenerator(name = "incrementor", strategy = "increment")
	public int getId_producenta() {
		return id_producenta;
	}
	public void setId_producenta(int id_producenta) {
		this.id_producenta = id_producenta;
	}
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	public String getKontakt() {
		return kontakt;
	}
	public void setKontakt(String kontakt) {
		this.kontakt = kontakt;
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
