package net.codejava;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Kategorie {
	private int id_kategorii;
	private String nazwa;
	public Kategorie() {
	}
	public Kategorie(String nazwa) {
		this.nazwa = nazwa;
	}
	
	@Id
	@GeneratedValue(generator = "incrementor")
	@GenericGenerator(name = "incrementor", strategy = "increment")
	public int getId_Kategorii() {
		return id_kategorii;
	}
	public void setId_Kategorii(int id_kategorii) {
		this.id_kategorii = id_kategorii;
	}
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
}
