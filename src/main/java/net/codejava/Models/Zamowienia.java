package net.codejava.Models;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

interface IZamowienia extends Obiekt_Do_Polecen
{
	 public String getOpis();
	 public double getKoszt();
}

interface IZamowieniaI extends IZamowienia
{
	public int getId_zamowienia();

	public void setId_zamowienia(int id_zamowienia);

	public double getKoszt();

	public void setKoszt(double koszt);

	public String getAdres_wysylki_miasto();

	public void setAdres_wysylki_miasto(String adres_wysylki_miasto);

	public String getAdres_wysylki_ulica();

	public void setAdres_wysylki_ulica(String adres_wysylki_ulica);

	public int getId_stanu_zamowienia();

	public void setId_stanu_zamowienia(int id_stanu_zamowienia);

	public int getUzytkownicy_id_uzytkownika();

	public void setUzytkownicy_id_uzytkownika(int uzytkownicy_id_uzytkownika);

	public void setOpis(String opis);
}


@Entity
public class Zamowienia implements IZamowieniaI {
	private int id_zamowienia;
	private double koszt;
	private String adres_wysylki_miasto;
	private String adres_wysylki_ulica;
	private String opis = "";

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	@ManyToOne
	@JoinColumn(name = "id_stanu_zamowienia")
	private int id_stanu_zamowienia;
	
	@ManyToOne
	@JoinColumn(name = "uzytkownicy_id_uzytkownika")
	private int uzytkownicy_id_uzytkownika;
	
	public Zamowienia() {}
	public Zamowienia(double koszt, String adres_wysylki_miasto, String adres_wysylki_ulica, int id_stanu_zamowienia,
			int uzytkownicy_id_uzytkownika, String opis) {
		this.koszt = koszt;
		this.adres_wysylki_miasto = adres_wysylki_miasto;
		this.adres_wysylki_ulica = adres_wysylki_ulica;
		this.id_stanu_zamowienia = id_stanu_zamowienia;
		this.uzytkownicy_id_uzytkownika = uzytkownicy_id_uzytkownika;
		this.opis = opis;
	}
	
	public Zamowienia(int id, double koszt, String adres_wysylki_miasto, String adres_wysylki_ulica, int id_stanu_zamowienia,
			int uzytkownicy_id_uzytkownika, String opis) {
		this.id_zamowienia = id;
		this.koszt = koszt;
		this.adres_wysylki_miasto = adres_wysylki_miasto;
		this.adres_wysylki_ulica = adres_wysylki_ulica;
		this.id_stanu_zamowienia = id_stanu_zamowienia;
		this.uzytkownicy_id_uzytkownika = uzytkownicy_id_uzytkownika;
		this.opis = opis;
	}
	
	@Id
	@GeneratedValue(generator = "incrementor")
	@GenericGenerator(name = "incrementor", strategy = "increment")
	public int getId_zamowienia() {
		return id_zamowienia;
	}
	public void setId_zamowienia(int id_zamowienia) {
		this.id_zamowienia = id_zamowienia;
	}
	public double getKoszt() {
		return koszt;
	}
	public void setKoszt(double koszt) {
		this.koszt = koszt;
	}
	public String getAdres_wysylki_miasto() {
		return adres_wysylki_miasto;
	}
	public void setAdres_wysylki_miasto(String adres_wysylki_miasto) {
		this.adres_wysylki_miasto = adres_wysylki_miasto;
	}
	public String getAdres_wysylki_ulica() {
		return adres_wysylki_ulica;
	}
	public void setAdres_wysylki_ulica(String adres_wysylki_ulica) {
		this.adres_wysylki_ulica = adres_wysylki_ulica;
	}
	public int getId_stanu_zamowienia() {
		return id_stanu_zamowienia;
	}
	public void setId_stanu_zamowienia(int id_stanu_zamowienia) {
		this.id_stanu_zamowienia = id_stanu_zamowienia;
	}
	public int getUzytkownicy_id_uzytkownika() {
		return uzytkownicy_id_uzytkownika;
	}
	public void setUzytkownicy_id_uzytkownika(int uzytkownicy_id_uzytkownika) {
		this.uzytkownicy_id_uzytkownika = uzytkownicy_id_uzytkownika;
	}
}

abstract class ZamowieniaDekorator extends Zamowienia
{
	Zamowienia zamowienie;
	double cena;
	String opis;

	public ZamowieniaDekorator(Zamowienia zamowienie)
	{
		this.zamowienie = zamowienie;
	}

	public double getKoszt()
	{
		return zamowienie.getKoszt()+cena;
	}

	public String getOpis()
	{
		return zamowienie.getOpis() + opis + ";";
	}
}

