package net.codejava;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Typy_uzytkownika {
	private int id_typu_uzytkownika;
	private String nazwa;
	public Typy_uzytkownika() {
	}
	public Typy_uzytkownika(String nazwa) {
		this.nazwa = nazwa;
	}
	@Id
	@GeneratedValue(generator = "incrementor")
	@GenericGenerator(name = "incrementor", strategy = "increment")
	public int getId_typu_uzytkownika() {
		return id_typu_uzytkownika;
	}
	public void setId_typu_uzytkownika(int id_typu_uzytkownika) {
		this.id_typu_uzytkownika = id_typu_uzytkownika;
	}
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	
}
