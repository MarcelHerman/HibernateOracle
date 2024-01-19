package net.codejava.Controllers;

import net.codejava.HibernateOracle;
import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeli;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class DyrektorTabel {
	
	private BudowniczyTabeli budowniczy;
	
	public void tworzTabeleMagazyny(List<Obiekt_Do_Polecen> obiekty,BudowniczyTabeli budowniczy)
	{
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Magazyny();
		HibernateOracle.wzorzec = new StrategiaMagazyny();
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();
		
		budowniczy.dodajKolumne("Lp.");
		budowniczy.dodajKolumne("Miasto");
		budowniczy.dodajKolumne("Ulica");
		
		for(Obiekt_Do_Polecen rekord: obiekty)
		{
			budowniczy.dodajWiersz();			
			budowniczy.dodajKolumne(Integer.toString(((Magazyny) rekord).getId_magazynu()));
			budowniczy.dodajKolumne(((Magazyny) rekord).getMiasto().toString());
			budowniczy.dodajKolumne(((Magazyny) rekord).getUlica().toString());		
			switch(HibernateOracle.nazwaTypu)
			{
			case "Administrator":
				budowniczy.dodajKolumnePrzycisku();
				budowniczy.dodajKolumnePrzycisku();
				break;
			}

		}
	}
	
	public void tworzTabeleProdukty(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
	{
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Produkty();
		HibernateOracle.wzorzec = new StrategiaProdukty();
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();
		
		budowniczy.dodajKolumne("Lp.");
		budowniczy.dodajKolumne("Nazwa");
		budowniczy.dodajKolumne("Cena");
		budowniczy.dodajKolumne("Opis");	
		budowniczy.dodajKolumne("Producent");
		budowniczy.dodajKolumne("Kategoria");
		if((HibernateOracle.nazwaTypu.equals("Administrator")) || (HibernateOracle.nazwaTypu.equals("Pracownik")))budowniczy.dodajKolumne("Usunięty");

		PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();
		bd.stworzSesjeBD();
		
		List<Obiekt_Do_Polecen> daneZewn = null;
		List<Obiekt_Do_Polecen> daneZewn2 = null;
		
		try (Session sesja = bd.pobierzSesjeBD()) {
            Query<Obiekt_Do_Polecen> zapytanie = sesja.createQuery("FROM Producenci", Obiekt_Do_Polecen.class);
            daneZewn = zapytanie.getResultList();
            
            Query<Obiekt_Do_Polecen> zapytanie2 = sesja.createQuery("FROM Kategorie", Obiekt_Do_Polecen.class);
            daneZewn2 = zapytanie2.getResultList();
   		 bd.zamknijSesjeBD();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

		
		for(Obiekt_Do_Polecen rekord: obiekty)
		{
			budowniczy.dodajWiersz();			
			budowniczy.dodajKolumne(Integer.toString(((Produkty) rekord).getId_produktu()));
			budowniczy.dodajKolumne(((Produkty) rekord).getNazwa().toString());
			budowniczy.dodajKolumne(Double.toString(((Produkty) rekord).getCena()));
			budowniczy.dodajKolumne(((Produkty) rekord).getOpis().toString());
			
			for(Obiekt_Do_Polecen prod: daneZewn) {
				if(((Producenci)prod).getId_producenta() == ((Produkty) rekord).getProducenci_id_producenta())
				{
					budowniczy.dodajKolumne(((Producenci)prod).getNazwa());
					break;
				}
			}
			
			for(Obiekt_Do_Polecen kat: daneZewn2) {
				if(((Kategorie)kat).getId_Kategorii() == ((Produkty) rekord).getKategorie_id_kategorii())
				{
					budowniczy.dodajKolumne(((Kategorie)kat).getNazwa());
					break;
				}
			}
			
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				budowniczy.dodajKolumne((((Produkty) rekord).getCzy_usunieto()==1)? "TAK" : "NIE");
				budowniczy.dodajKolumnePrzycisku();
				budowniczy.dodajKolumnePrzycisku();
				break;
			case("Pracownik"):
				budowniczy.dodajKolumne((((Produkty) rekord).getCzy_usunieto()==1)? "TAK" : "NIE");
				budowniczy.dodajKolumnePrzycisku();
				break;
			}
			if(!(HibernateOracle.nazwaTypu.equals("null")))budowniczy.dodajKolumnePrzycisku();
		}
	}
	
	public void tworzTabeleKategorie(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
	{
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Kategorie();
		HibernateOracle.wzorzec = new StrategiaKategorie();
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();
		
		budowniczy.dodajKolumne("Lp.");
		budowniczy.dodajKolumne("Nazwa");
		
		for(Obiekt_Do_Polecen rekord: obiekty)
		{
			budowniczy.dodajWiersz();			
			budowniczy.dodajKolumne(Integer.toString(((Kategorie) rekord).getId_Kategorii()));
			budowniczy.dodajKolumne(((Kategorie) rekord).getNazwa().toString());
			switch(HibernateOracle.nazwaTypu)
			{
			case "Administrator":
				budowniczy.dodajKolumnePrzycisku();
				budowniczy.dodajKolumnePrzycisku();
				break;
			}
		}
	}
	
	public void tworzTabeleFaktury(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
	{
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Faktury();
		HibernateOracle.wzorzec = new StrategiaFaktury();
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();
		
		budowniczy.dodajKolumne("Lp.");
		budowniczy.dodajKolumne("Data wystawienia");
		budowniczy.dodajKolumne("NIP");
		budowniczy.dodajKolumne("Id zamówienia");
		
		for(Obiekt_Do_Polecen rekord: obiekty)
		{
			budowniczy.dodajWiersz();			
			budowniczy.dodajKolumne(Integer.toString(((Faktury) rekord).getId_faktury()));
			budowniczy.dodajKolumne(((Faktury) rekord).getData_wystawienia().toString());
			budowniczy.dodajKolumne(((Faktury) rekord).getNIP().toString());
			budowniczy.dodajKolumne(Integer.toString(((Faktury) rekord).getZamowienia_id_zamowienia()));
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				budowniczy.dodajKolumnePrzycisku();
				budowniczy.dodajKolumnePrzycisku();
				break;
			}
		}
	}
	
	public void tworzTabeleProdukt_Magazyn(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
	{
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Produkt_Magazyn();
		HibernateOracle.wzorzec = new StrategiaProdukt_Magazyn();
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();
		
		budowniczy.dodajKolumne("Id Magazynu");
		budowniczy.dodajKolumne("Id Produktu");
		budowniczy.dodajKolumne("Stan faktyczny");
		budowniczy.dodajKolumne("Stan magazynowy");
		
		for(Obiekt_Do_Polecen rekord: obiekty)
		{
			budowniczy.dodajWiersz();			
			budowniczy.dodajKolumne(Integer.toString(((Produkt_Magazyn) rekord).getMagazyn_id()));
			budowniczy.dodajKolumne(Integer.toString(((Produkt_Magazyn) rekord).getProdukt_id()));			
			budowniczy.dodajKolumne(Integer.toString(((Produkt_Magazyn) rekord).getStan_faktyczny()));
			budowniczy.dodajKolumne(Integer.toString(((Produkt_Magazyn) rekord).getStan_magazynowy()));
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				budowniczy.dodajKolumnePrzycisku();
				budowniczy.dodajKolumnePrzycisku();
				break;
			}
		}
	}
	
	public void tworzTabeleProdukt_Zamowienia(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
	{
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Produkt_Zamowienia();
		HibernateOracle.wzorzec = new StrategiaProdukt_Zamowienia();
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();
		
		budowniczy.dodajKolumne("Id Zamowienia");
		budowniczy.dodajKolumne("Id Produktu");
		budowniczy.dodajKolumne("Ilosc");
		
		for(Obiekt_Do_Polecen rekord: obiekty)
		{
			budowniczy.dodajWiersz();			
			budowniczy.dodajKolumne(Integer.toString(((Produkt_Zamowienia) rekord).getZamowienieId()));
			budowniczy.dodajKolumne(Integer.toString(((Produkt_Zamowienia) rekord).getProduktId()));
			budowniczy.dodajKolumne(Integer.toString(((Produkt_Zamowienia) rekord).getIlosc()));
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				budowniczy.dodajKolumnePrzycisku();
				budowniczy.dodajKolumnePrzycisku();
				break;
			}
		}
	}
	
	public void tworzTabeleStany_Zamowienia(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
	{
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Stany_Zamowienia();
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();
		
		budowniczy.dodajKolumne("Id Stanu Zamówienia");
		budowniczy.dodajKolumne("Nazwa");
		
		for(Obiekt_Do_Polecen rekord: obiekty)
		{
			budowniczy.dodajWiersz();			
			budowniczy.dodajKolumne(Integer.toString(((Stany_Zamowienia) rekord).getId_Stanu_Zamowienia()));
			budowniczy.dodajKolumne(((Stany_Zamowienia) rekord).getNazwa().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				budowniczy.dodajKolumnePrzycisku();
				budowniczy.dodajKolumnePrzycisku();
				break;
			}
		}
	}
	
	public void tworzTabeleProducenci(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
	{
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Producenci();
		HibernateOracle.wzorzec = new StrategiaProducenci();
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();
		
		budowniczy.dodajKolumne("Lp.");
		budowniczy.dodajKolumne("Nazwa");
		budowniczy.dodajKolumne("Kontakt");
		budowniczy.dodajKolumne("Miasto");
		budowniczy.dodajKolumne("Ulica");
		if((HibernateOracle.nazwaTypu.equals("Administrator")) ||  (HibernateOracle.nazwaTypu.equals("Pracownik")))budowniczy.dodajKolumne("Usunięty");
		
		for(Obiekt_Do_Polecen rekord: obiekty)
		{
			budowniczy.dodajWiersz();			
			budowniczy.dodajKolumne(Integer.toString(((Producenci) rekord).getId_producenta()));
			budowniczy.dodajKolumne(((Producenci) rekord).getNazwa().toString());
			budowniczy.dodajKolumne(((Producenci) rekord).getKontakt().toString());
			budowniczy.dodajKolumne(((Producenci) rekord).getMiasto().toString());
			budowniczy.dodajKolumne(((Producenci) rekord).getUlica().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				budowniczy.dodajKolumne((((Producenci)rekord).getCzy_usunieto()==1)? "TAK" : "NIE");
				budowniczy.dodajKolumnePrzycisku();
				budowniczy.dodajKolumnePrzycisku();
				break;
			case("Pracownik"):
				budowniczy.dodajKolumne((((Uzytkownicy)rekord).getCzy_usunieto()==1)? "TAK" : "NIE");
				break;
			}
		}
	}
	
	public void tworzTabeleUzytkownicy(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
	{	
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Uzytkownicy();
		HibernateOracle.wzorzec = new StrategiaUzytkownicy();
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();
		
		budowniczy.dodajKolumne("Lp.");
		budowniczy.dodajKolumne("Nazwa użytkownika");
		budowniczy.dodajKolumne("Login");
		budowniczy.dodajKolumne("Hasło");
		budowniczy.dodajKolumne("E-mail");
		budowniczy.dodajKolumne("Typ konta");
		if((HibernateOracle.nazwaTypu.equals("Administrator"))  ||  (HibernateOracle.nazwaTypu.equals("Pracownik")))budowniczy.dodajKolumne("Usunięty");
		
		PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();
		bd.stworzSesjeBD();
		
		List<Obiekt_Do_Polecen> daneZewn = null;
		
		try (Session sesja = bd.pobierzSesjeBD()) {
            Query<Obiekt_Do_Polecen> zapytanie = sesja.createQuery("FROM Typy_uzytkownika", Obiekt_Do_Polecen.class);
            daneZewn = zapytanie.getResultList();
            bd.zamknijSesjeBD();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
		
		for(Obiekt_Do_Polecen rekord: obiekty)
		{
			budowniczy.dodajWiersz();			
			budowniczy.dodajKolumne(Integer.toString(((Uzytkownicy) rekord).getId_uzytkownika()));
			budowniczy.dodajKolumne(((Uzytkownicy) rekord).getNazwa_uzytkownika().toString());
			budowniczy.dodajKolumne(((Uzytkownicy) rekord).getLogin().toString());
			budowniczy.dodajKolumne(((Uzytkownicy) rekord).getHaslo().toString());
			budowniczy.dodajKolumne(((Uzytkownicy) rekord).getE_mail().toString());
			
			for(Obiekt_Do_Polecen typ: daneZewn) {
				if(((Typy_uzytkownika)typ).getId_typu_uzytkownika() == ((Uzytkownicy) rekord).getId_typu_uzytkownika() ) {
					budowniczy.dodajKolumne(((Typy_uzytkownika)typ).getNazwa());
				}
			}

			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				budowniczy.dodajKolumne((((Uzytkownicy)rekord).getCzy_usunieto()==1)? "TAK" : "NIE");
				budowniczy.dodajKolumnePrzycisku();
				budowniczy.dodajKolumnePrzycisku();
				break;
			case("Pracownik"):
				budowniczy.dodajKolumne((((Uzytkownicy)rekord).getCzy_usunieto()==1)? "TAK" : "NIE");
				break;
			}
		}
	}
	
	public void tworzTabeleZamowienia(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
	{
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Zamowienia();
		HibernateOracle.wzorzec = new StrategiaZamowienia();
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();
		
		budowniczy.dodajKolumne("Lp.");
		budowniczy.dodajKolumne("Miasto wysyłki");
		budowniczy.dodajKolumne("Ulica wysyłki");
		budowniczy.dodajKolumne("Koszt");
		budowniczy.dodajKolumne("Stan zamowienia");
		if(!(HibernateOracle.nazwaTypu.equals("Klient")))budowniczy.dodajKolumne("Id Użytkownika");
		budowniczy.dodajKolumne("Zamówione produkty");
		budowniczy.dodajKolumne("Notatka");
		
		PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();
		bd.stworzSesjeBD();
		
		List<Obiekt_Do_Polecen> daneZewn = null;
		
		try (Session sesja = bd.pobierzSesjeBD()) {
            Query<Obiekt_Do_Polecen> zapytanie = sesja.createQuery("FROM Stany_Zamowienia", Obiekt_Do_Polecen.class);
            daneZewn = zapytanie.getResultList();
            bd.zamknijSesjeBD();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
		
		for(Obiekt_Do_Polecen rekord: obiekty)
		{
			budowniczy.dodajWiersz();			
			budowniczy.dodajKolumne(Integer.toString(((Zamowienia) rekord).getId_zamowienia()));
			budowniczy.dodajKolumne(((Zamowienia) rekord).getAdres_wysylki_miasto().toString());
			budowniczy.dodajKolumne(((Zamowienia) rekord).getAdres_wysylki_ulica().toString());
			budowniczy.dodajKolumne(Double.toString(((Zamowienia) rekord).getKoszt()));
			
			for(Obiekt_Do_Polecen stan: daneZewn) {
				if(((Stany_Zamowienia)stan).getId_Stanu_Zamowienia() == ((Zamowienia)rekord).getId_stanu_zamowienia() ) {
					budowniczy.dodajKolumne(((Stany_Zamowienia)stan).getNazwa());
					break;
				}
			}
			List<String> nPr = null;
			bd.stworzSesjeBD();
			try (Session sesja = bd.pobierzSesjeBD()) {
	            Query<String> zapytanie = sesja.createQuery("SELECT p.nazwa FROM Produkty p, Zamowienia z, Produkt_Zamowienia pz where p.id_produktu = pz.produkt_zamowienia_id.id_produktu and pz.produkt_zamowienia_id.id_zamowienia = z.id_zamowienia and z.id_zamowienia = :id", String.class).setParameter("id", ((Zamowienia) rekord).getId_zamowienia());
	            nPr = zapytanie.getResultList();
	            bd.zamknijSesjeBD();
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println(e);
	        }
											
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				budowniczy.dodajKolumne(Integer.toString(((Zamowienia) rekord).getUzytkownicy_id_uzytkownika()));
				budowniczy.dodajKolumne(nPr);
				budowniczy.dodajKolumne(((Zamowienia) rekord).getOpis());
				budowniczy.dodajKolumnePrzycisku();
				break;	
			case("Klient"):
				budowniczy.dodajKolumne(nPr);
				budowniczy.dodajKolumne(((Zamowienia) rekord).getOpis());
				break;
			case("Pracownik"):
			default:
				budowniczy.dodajKolumne(Integer.toString(((Zamowienia) rekord).getUzytkownicy_id_uzytkownika()));
				budowniczy.dodajKolumne(nPr);
				budowniczy.dodajKolumne(((Zamowienia) rekord).getOpis());
				break;
			}
		}
	}
	
	public void tworzTabeleTypy_uzytkownika(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
    {
		this.budowniczy = budowniczy;
		HibernateOracle.obiekt = new Typy_uzytkownika();
		HibernateOracle.wzorzec = null;
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();

		budowniczy.dodajKolumne("Lp.");
		budowniczy.dodajKolumne("Nazwa");

        for(Obiekt_Do_Polecen rekord: obiekty)
        {
        	budowniczy.dodajWiersz();
        	budowniczy.dodajKolumne(Integer.toString(((Typy_uzytkownika) rekord).getId_typu_uzytkownika()));
        	budowniczy.dodajKolumne(((Typy_uzytkownika) rekord).getNazwa().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				budowniczy.dodajKolumnePrzycisku();
				budowniczy.dodajKolumnePrzycisku();
				break;
			}
        }
    }
	
	public void tworzTabeleKoszyk(List<Obiekt_Do_Polecen> obiekty, BudowniczyTabeli budowniczy)
    {
		HibernateOracle.obiekt = new Produkt_Koszyk();
		HibernateOracle.wzorzec = new StrategiaProdukt_Koszyk();
		this.budowniczy = budowniczy;
		budowniczy.zresetuj();
		budowniczy.dodajNaglowek();

		budowniczy.dodajKolumne("Lp.");
		budowniczy.dodajKolumne("Nazwa");
		budowniczy.dodajKolumne("Cena za sztuke");
        budowniczy.dodajKolumne("Ilość");
        budowniczy.dodajKolumne("Łączna cena");

        for(Obiekt_Do_Polecen rekord: obiekty)
        {
        	budowniczy.dodajWiersz();
        	budowniczy.dodajKolumne(Integer.toString(((Produkt_Koszyk) rekord).getPr().getId_produktu()));
        	budowniczy.dodajKolumne(((Produkt_Koszyk) rekord).getPr().getNazwa());
        	budowniczy.dodajKolumne(Double.toString(((Produkt_Koszyk) rekord).getPr().getCena()));
        	budowniczy.dodajKolumne(Integer.toString(((Produkt_Koszyk) rekord).getIlosc()));
        	budowniczy.dodajKolumne(Double.toString(((Produkt_Koszyk) rekord).getPr().getCena() *((Produkt_Koszyk) rekord).getIlosc()));
            
        	budowniczy.dodajKolumnePrzycisku();
			budowniczy.dodajKolumnePrzycisku();
        }
    }
	
	
	public Object pobierzTabele() {
		return budowniczy.pobierzTabele(null);
	}
}
