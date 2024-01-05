package net.codejava.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Producenci implements Obiekt_Do_Polecen {
	private int id_producenta;
	private String nazwa;
	private String kontakt;
	private String miasto;
	private String ulica;
	@Column(name = "czy_usunieto")
	private int czy_usunieto;
	
	public Producenci() {
	}
	public Producenci(String nazwa, String kontakt, String miasto, String ulica, int czy_usunieto) {
		this.nazwa = nazwa;
		this.kontakt = kontakt;
		this.miasto = miasto;
		this.ulica = ulica;
		this.czy_usunieto = czy_usunieto;
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
	
	public int getCzy_usunieto() {
		return czy_usunieto;
	}

	public void setCzy_usunieto(int czy_usunieto) {
		this.czy_usunieto = czy_usunieto;
	}

}
