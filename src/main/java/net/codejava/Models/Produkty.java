package net.codejava.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Produkty implements Obiekt_Do_Polecen{
	private int id_produktu;
	private String nazwa;
	private double cena;
	private String opis;
	@Column(name = "czy_usunieto")
	private int czy_usunieto;
	
	public int getCzy_usunieto() {
		return czy_usunieto;
	}

	public void setCzy_usunieto(int czy_usunieto) {
		this.czy_usunieto = czy_usunieto;
	}

	@ManyToOne
	@JoinColumn(name = "Producenci_id_producenta")
	private int Producenci_id_producenta;
	
	@ManyToOne
	@JoinColumn(name = "Kategorie_id_kategorii")
	private int Kategorie_id_kategorii;

	public Produkty() {
	}
	
	public Produkty(String nazwa, double cena, String opis, int producenci_id_producenta,
			int kategorie_id_kategorii, int czy_usunieto) {
		this.nazwa = nazwa;
		this.cena = cena;
		this.opis = opis;
		Producenci_id_producenta = producenci_id_producenta;
		Kategorie_id_kategorii = kategorie_id_kategorii;
		this.czy_usunieto=czy_usunieto;
	}
	
	public Produkty(String nazwa, double cena, String opis, int producenci_id_producenta,
			int kategorie_id_kategorii) {
		this.nazwa = nazwa;
		this.cena = cena;
		this.opis = opis;
		Producenci_id_producenta = producenci_id_producenta;
		Kategorie_id_kategorii = kategorie_id_kategorii;
	}

	@Id
	@GeneratedValue(generator = "incrementor")
	@GenericGenerator(name = "incrementor", strategy = "increment")
	public int getId_produktu() {
		return id_produktu;
	}

	public void setId_produktu(int id_produktu) {
		this.id_produktu = id_produktu;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public int getProducenci_id_producenta() {
		return Producenci_id_producenta;
	}

	public void setProducenci_id_producenta(int producenci_id_producenta) {
		Producenci_id_producenta = producenci_id_producenta;
	}

	public int getKategorie_id_kategorii() {
		return Kategorie_id_kategorii;
	}

	public void setKategorie_id_kategorii(int kategorie_id_kategorii) {
		Kategorie_id_kategorii = kategorie_id_kategorii;
	}
	

}
