package net.codejava;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Faktury implements Obiekt_Do_Polecen{
	private int id_faktury;
	private LocalDate  data_wystawienia;
	private String NIP;
	
	@ManyToOne
	@JoinColumn(name = "Zamowienia_id_zamowienia")
	private int zamowienia_id_zamowienia;

	public Faktury() {
	}

	public Faktury(LocalDate  data_wystawienia, String nIP, int zamowienia_id_zamowienia) {
		super();
		this.data_wystawienia = data_wystawienia;
		NIP = nIP;
		this.zamowienia_id_zamowienia = zamowienia_id_zamowienia;
	}

	@Id
	@GeneratedValue(generator = "incrementor")
	@GenericGenerator(name = "incrementor", strategy = "increment")
	public int getId_faktury() {
		return id_faktury;
	}

	public void setId_faktury(int id_faktury) {
		this.id_faktury = id_faktury;
	}

	public LocalDate  getData_wystawienia() {
		return data_wystawienia;
	}

	public void setData_wystawienia(LocalDate  data_wystawienia) {
		this.data_wystawienia = data_wystawienia;
	}

	public String getNIP() {
		return NIP;
	}

	public void setNIP(String nIP) {
		NIP = nIP;
	}

	public int getZamowienia_id_zamowienia() {
		return zamowienia_id_zamowienia;
	}

	public void setZamowienia_id_zamowienia(int zamowienia_id_zamowienia) {
		this.zamowienia_id_zamowienia = zamowienia_id_zamowienia;
	}
	
	
}
