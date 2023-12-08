package net.codejava;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Uzytkownicy {
	private int id_uzytkownika;
	private String nazwa_uzytkownika;
	private String login;
	private String haslo;
	private String e_mail;
	
	@ManyToOne
	@JoinColumn(name = "id_typu_uzytkownika")
	private int id_typu_uzytkownika;
	
	public Uzytkownicy() {
	}
	
	public Uzytkownicy(String nazwa_uzytkownika, String login, String haslo, String e_mail, int id_typu_uzytkownika) {
		this.id_typu_uzytkownika = id_typu_uzytkownika;
		this.nazwa_uzytkownika = nazwa_uzytkownika;
		this.login = login;
		this.haslo = haslo;
		this.e_mail = e_mail;
	}
	@Id
	@GeneratedValue(generator = "incrementor")
	@GenericGenerator(name = "incrementor", strategy = "increment")
	public int getId_uzytkownika() {
		return id_uzytkownika;
	}

	public void setId_uzytkownika(int id_uzytkownika) {
		this.id_uzytkownika = id_uzytkownika;
	}

	public String getNazwa_uzytkownika() {
		return nazwa_uzytkownika;
	}

	public void setNazwa_uzytkownika(String nazwa_uzytkownika) {
		this.nazwa_uzytkownika = nazwa_uzytkownika;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getHaslo() {
		return haslo;
	}

	public void setHaslo(String haslo) {
		this.haslo = haslo;
	}

	public String getE_mail() {
		return e_mail;
	}

	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}

	public int getId_typu_uzytkownika() {
		return id_typu_uzytkownika;
	}

	public void setId_typu_uzytkownika(int id_typu_uzytkownika) {
		this.id_typu_uzytkownika = id_typu_uzytkownika;
	}
	
}
