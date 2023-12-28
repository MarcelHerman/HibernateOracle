package net.codejava;

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
	private String opis;
	
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
			int uzytkownicy_id_uzytkownika) {
		this.koszt = koszt;
		this.adres_wysylki_miasto = adres_wysylki_miasto;
		this.adres_wysylki_ulica = adres_wysylki_ulica;
		this.id_stanu_zamowienia = id_stanu_zamowienia;
		this.uzytkownicy_id_uzytkownika = uzytkownicy_id_uzytkownika;
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

abstract class ZamowieniaDekorator implements IZamowieniaI
{
	IZamowieniaI zamowienie;
	double cena;
	String opis;
	
	public ZamowieniaDekorator(IZamowieniaI zamowienie)
	{
		this.zamowienie = zamowienie;
	}
	
	public double getKoszt()
	{
		return zamowienie.getKoszt()+cena;
	}
	
	public String getOpis()
	{
		return zamowienie.getOpis() + opis;
	}
}

class Znizka extends ZamowieniaDekorator
{

	public Znizka(IZamowieniaI zamowienie, double cena, String opis) {
		super(zamowienie);
		this.cena = cena;
		this.opis = opis;				
	}

	@Override
	public int getId_zamowienia() {
		return 0;
	}

	@Override
	public void setId_zamowienia(int id_zamowienia) {		
	}

	@Override
	public void setKoszt(double koszt) {		
	}

	@Override
	public String getAdres_wysylki_miasto() {
		return null;
	}

	@Override
	public void setAdres_wysylki_miasto(String adres_wysylki_miasto) {		
	}

	@Override
	public String getAdres_wysylki_ulica() {
		return null;
	}

	@Override
	public void setAdres_wysylki_ulica(String adres_wysylki_ulica) {		
	}

	@Override
	public int getId_stanu_zamowienia() {
		return 0;
	}

	@Override
	public void setId_stanu_zamowienia(int id_stanu_zamowienia) {		
	}

	@Override
	public int getUzytkownicy_id_uzytkownika() {
		return 0;
	}

	@Override
	public void setUzytkownicy_id_uzytkownika(int uzytkownicy_id_uzytkownika) {		
	}

	@Override
	public void setOpis(String opis) {	
	}
	
}

class Notatka extends ZamowieniaDekorator
{

	public Notatka(IZamowieniaI zamowienie, double cena, String opis) {
		super(zamowienie);
		this.cena = 2.5;
		this.opis = opis;				
	}

	@Override
	public int getId_zamowienia() {
		return 0;
	}

	@Override
	public void setId_zamowienia(int id_zamowienia) {		
	}

	@Override
	public void setKoszt(double koszt) {		
	}

	@Override
	public String getAdres_wysylki_miasto() {
		return null;
	}

	@Override
	public void setAdres_wysylki_miasto(String adres_wysylki_miasto) {		
	}

	@Override
	public String getAdres_wysylki_ulica() {
		return null;
	}

	@Override
	public void setAdres_wysylki_ulica(String adres_wysylki_ulica) {		
	}

	@Override
	public int getId_stanu_zamowienia() {
		return 0;
	}

	@Override
	public void setId_stanu_zamowienia(int id_stanu_zamowienia) {		
	}

	@Override
	public int getUzytkownicy_id_uzytkownika() {
		return 0;
	}

	@Override
	public void setUzytkownicy_id_uzytkownika(int uzytkownicy_id_uzytkownika) {		
	}

	@Override
	public void setOpis(String opis) {	
	}
	
}

class Opakowanie extends ZamowieniaDekorator
{

	public Opakowanie(IZamowieniaI zamowienie, double cena, String opis) {
		super(zamowienie);
		this.cena = 5;
		this.opis = opis;				
	}

	@Override
	public int getId_zamowienia() {
		return 0;
	}

	@Override
	public void setId_zamowienia(int id_zamowienia) {		
	}

	@Override
	public void setKoszt(double koszt) {		
	}

	@Override
	public String getAdres_wysylki_miasto() {
		return null;
	}

	@Override
	public void setAdres_wysylki_miasto(String adres_wysylki_miasto) {		
	}

	@Override
	public String getAdres_wysylki_ulica() {
		return null;
	}

	@Override
	public void setAdres_wysylki_ulica(String adres_wysylki_ulica) {		
	}

	@Override
	public int getId_stanu_zamowienia() {
		return 0;
	}

	@Override
	public void setId_stanu_zamowienia(int id_stanu_zamowienia) {		
	}

	@Override
	public int getUzytkownicy_id_uzytkownika() {
		return 0;
	}

	@Override
	public void setUzytkownicy_id_uzytkownika(int uzytkownicy_id_uzytkownika) {		
	}

	@Override
	public void setOpis(String opis) {	
	}
	
}






