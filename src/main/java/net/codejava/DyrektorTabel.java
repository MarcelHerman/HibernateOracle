package net.codejava;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class DyrektorTabel {
	
	private BudowniczyTabeli builder;
	
	public void tworzTabeleMagazyny(List<Obiekt_Do_Polecen> entities,BudowniczyTabeli builder)
	{
		this.builder = builder;
		HibernateOracle.obj = new Magazyny();
		HibernateOracle.wzorzec = new StrategiaMagazyny();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Lp.");
		builder.dodajKolumne("Miasto");
		builder.dodajKolumne("Ulica");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Magazyny) entry).getId_magazynu()));
			builder.dodajKolumne(((Magazyny) entry).getMiasto().toString());
			builder.dodajKolumne(((Magazyny) entry).getUlica().toString());		
			switch(HibernateOracle.nazwaTypu)
			{
			case "Administrator":
				builder.dodajKolumnePrzycisku();
				builder.dodajKolumnePrzycisku();
				break;
			}

		}
	}
	
	void tworzTabeleProdukty(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
	{
		this.builder = builder;
		HibernateOracle.obj = new Produkty();
		HibernateOracle.wzorzec = new StrategiaProdukty();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Lp.");
		builder.dodajKolumne("Nazwa");
		builder.dodajKolumne("Cena");
		builder.dodajKolumne("Opis");	
		builder.dodajKolumne("Producent");
		builder.dodajKolumne("Kategoria");
		if((HibernateOracle.nazwaTypu.equals("Administrator")) || (HibernateOracle.nazwaTypu.equals("Pracownik")))builder.dodajKolumne("Usunięty");

		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
		oc.createDBSession();
		
		List<Obiekt_Do_Polecen> fData = null;
		List<Obiekt_Do_Polecen> fData2 = null;
		
		try (Session session = oc.getDBSession()) {
            Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Producenci", Obiekt_Do_Polecen.class);
            fData = query.getResultList();
            
            Query<Obiekt_Do_Polecen> query2 = session.createQuery("FROM Kategorie", Obiekt_Do_Polecen.class);
            fData2 = query2.getResultList();
   		 oc.closeDBSession();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Produkty) entry).getId_produktu()));
			builder.dodajKolumne(((Produkty) entry).getNazwa().toString());
			builder.dodajKolumne(Double.toString(((Produkty) entry).getCena()));
			builder.dodajKolumne(((Produkty) entry).getOpis().toString());
			
			for(Obiekt_Do_Polecen prod: fData) {
				if(((Producenci)prod).getId_producenta() == ((Produkty) entry).getProducenci_id_producenta())
				{
					builder.dodajKolumne(((Producenci)prod).getNazwa());
					break;
				}
			}
			
			for(Obiekt_Do_Polecen kat: fData2) {
				if(((Kategorie)kat).getId_Kategorii() == ((Produkty) entry).getKategorie_id_kategorii())
				{
					builder.dodajKolumne(((Kategorie)kat).getNazwa());
					break;
				}
			}
			
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				builder.dodajKolumne((((Produkty) entry).getCzy_usunieto()==1)? "TAK" : "NIE");
				builder.dodajKolumnePrzycisku();
				builder.dodajKolumnePrzycisku();
				break;
			case("Pracownik"):
				builder.dodajKolumne((((Produkty) entry).getCzy_usunieto()==1)? "TAK" : "NIE");
				builder.dodajKolumnePrzycisku();
				break;
			}
			if(!(HibernateOracle.nazwaTypu.equals("null")))builder.dodajKolumnePrzycisku();
		}
	}
	
	public void tworzTabeleKategorie(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
	{
		this.builder = builder;
		HibernateOracle.obj = new Kategorie();
		HibernateOracle.wzorzec = new StrategiaKategorie();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Lp.");
		builder.dodajKolumne("Nazwa");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Kategorie) entry).getId_Kategorii()));
			builder.dodajKolumne(((Kategorie) entry).getNazwa().toString());
			switch(HibernateOracle.nazwaTypu)
			{
			case "Administrator":
				builder.dodajKolumnePrzycisku();
				builder.dodajKolumnePrzycisku();
				break;
			}
		}
	}
	
	public void tworzTabeleFaktury(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
	{
		this.builder = builder;
		HibernateOracle.obj = new Faktury();
		HibernateOracle.wzorzec = new StrategiaFaktury();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Lp.");
		builder.dodajKolumne("Data wystawienia");
		builder.dodajKolumne("NIP");
		builder.dodajKolumne("Id zamówienia");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Faktury) entry).getId_faktury()));
			builder.dodajKolumne(((Faktury) entry).getData_wystawienia().toString());
			builder.dodajKolumne(((Faktury) entry).getNIP().toString());
			builder.dodajKolumne(Integer.toString(((Faktury) entry).getZamowienia_id_zamowienia()));
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				builder.dodajKolumnePrzycisku();
				builder.dodajKolumnePrzycisku();
				break;
			}
		}
	}
	
	public void tworzTabeleProdukt_Magazyn(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
	{
		this.builder = builder;
		HibernateOracle.obj = new Produkt_Magazyn();
		HibernateOracle.wzorzec = new StrategiaProdukt_Magazyn();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Id Magazynu");
		builder.dodajKolumne("Id Produktu");
		builder.dodajKolumne("Stan faktyczny");
		builder.dodajKolumne("Stan magazynowy");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Produkt_Magazyn) entry).getMagazyn_id()));
			builder.dodajKolumne(Integer.toString(((Produkt_Magazyn) entry).getProdukt_id()));			
			builder.dodajKolumne(Integer.toString(((Produkt_Magazyn) entry).getStan_faktyczny()));
			builder.dodajKolumne(Integer.toString(((Produkt_Magazyn) entry).getStan_magazynowy()));
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				builder.dodajKolumnePrzycisku();
				builder.dodajKolumnePrzycisku();
				break;
			}
		}
	}
	
	public void tworzTabeleProdukt_Zamowienia(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
	{
		this.builder = builder;
		HibernateOracle.obj = new Produkt_Zamowienia();
		HibernateOracle.wzorzec = new StrategiaProdukt_Zamowienia();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Id Zamowienia");
		builder.dodajKolumne("Id Produktu");
		builder.dodajKolumne("Ilosc");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Produkt_Zamowienia) entry).getZamowienieId()));
			builder.dodajKolumne(Integer.toString(((Produkt_Zamowienia) entry).getProduktId()));
			builder.dodajKolumne(Integer.toString(((Produkt_Zamowienia) entry).getIlosc()));
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				builder.dodajKolumnePrzycisku();
				builder.dodajKolumnePrzycisku();
				break;
			}
		}
	}
	
	public void tworzTabeleStany_Zamowienia(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
	{
		this.builder = builder;
		HibernateOracle.obj = new Stany_Zamowienia();
		HibernateOracle.wzorzec = new StrategiaStany_Zamowienia();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Id Stanu Zamówienia");
		builder.dodajKolumne("Nazwa");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Stany_Zamowienia) entry).getId_Stanu_Zamowienia()));
			builder.dodajKolumne(((Stany_Zamowienia) entry).getNazwa().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				builder.dodajKolumnePrzycisku();
				builder.dodajKolumnePrzycisku();
				break;
			}
		}
	}
	
	public void tworzTabeleProducenci(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
	{
		this.builder = builder;
		HibernateOracle.obj = new Producenci();
		HibernateOracle.wzorzec = new StrategiaProducenci();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Lp.");
		builder.dodajKolumne("Nazwa");
		builder.dodajKolumne("Kontakt");
		builder.dodajKolumne("Miasto");
		builder.dodajKolumne("Ulica");
		if((HibernateOracle.nazwaTypu.equals("Administrator")) ||  (HibernateOracle.nazwaTypu.equals("Pracownik")))builder.dodajKolumne("Usunięty");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Producenci) entry).getId_producenta()));
			builder.dodajKolumne(((Producenci) entry).getNazwa().toString());
			builder.dodajKolumne(((Producenci) entry).getKontakt().toString());
			builder.dodajKolumne(((Producenci) entry).getMiasto().toString());
			builder.dodajKolumne(((Producenci) entry).getUlica().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				builder.dodajKolumne((((Producenci)entry).getCzy_usunieto()==1)? "TAK" : "NIE");
				builder.dodajKolumnePrzycisku();
				builder.dodajKolumnePrzycisku();
				break;
			case("Pracownik"):
				builder.dodajKolumne((((Uzytkownicy)entry).getCzy_usunieto()==1)? "TAK" : "NIE");
				break;
			}
		}
	}
	
	public void tworzTabeleUzytkownicy(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
	{	
		this.builder = builder;
		HibernateOracle.obj = new Uzytkownicy();
		HibernateOracle.wzorzec = new StrategiaUzytkownicy();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Lp.");
		builder.dodajKolumne("Nazwa użytkownika");
		builder.dodajKolumne("Login");
		builder.dodajKolumne("Hasło");
		builder.dodajKolumne("E-mail");
		builder.dodajKolumne("Typ konta");
		if((HibernateOracle.nazwaTypu.equals("Administrator"))  ||  (HibernateOracle.nazwaTypu.equals("Pracownik")))builder.dodajKolumne("Usunięty");
		
		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
		oc.createDBSession();
		
		List<Obiekt_Do_Polecen> fData = null;
		
		try (Session session = oc.getDBSession()) {
            Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Typy_uzytkownika", Obiekt_Do_Polecen.class);
            fData = query.getResultList();
            oc.closeDBSession();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Uzytkownicy) entry).getId_uzytkownika()));
			builder.dodajKolumne(((Uzytkownicy) entry).getNazwa_uzytkownika().toString());
			builder.dodajKolumne(((Uzytkownicy) entry).getLogin().toString());
			builder.dodajKolumne(((Uzytkownicy) entry).getHaslo().toString());
			builder.dodajKolumne(((Uzytkownicy) entry).getE_mail().toString());
			
			for(Obiekt_Do_Polecen typ: fData) {
				if(((Typy_uzytkownika)typ).getId_typu_uzytkownika() == ((Uzytkownicy) entry).getId_typu_uzytkownika() ) {
					builder.dodajKolumne(((Typy_uzytkownika)typ).getNazwa());
				}
			}

			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				builder.dodajKolumne((((Uzytkownicy)entry).getCzy_usunieto()==1)? "TAK" : "NIE");
				builder.dodajKolumnePrzycisku();
				builder.dodajKolumnePrzycisku();
				break;
			case("Pracownik"):
				builder.dodajKolumne((((Uzytkownicy)entry).getCzy_usunieto()==1)? "TAK" : "NIE");
				break;
			}
		}
	}
	
	void tworzTabeleZamowienia(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
	{
		this.builder = builder;
		HibernateOracle.obj = new Zamowienia();
		HibernateOracle.wzorzec = new StrategiaZamowienia();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Lp.");
		builder.dodajKolumne("Miasto wysyłki");
		builder.dodajKolumne("Ulica wysyłki");
		builder.dodajKolumne("Koszt");
		builder.dodajKolumne("Stan zamowienia");
		if(!(HibernateOracle.nazwaTypu.equals("Klient")))builder.dodajKolumne("Id Użytkownika");
		builder.dodajKolumne("Zamówione produkty");
		builder.dodajKolumne("Notatka");
		
		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
		oc.createDBSession();
		
		List<Obiekt_Do_Polecen> fData = null;
		
		try (Session session = oc.getDBSession()) {
            Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Stany_Zamowienia", Obiekt_Do_Polecen.class);
            fData = query.getResultList();
            oc.closeDBSession();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Zamowienia) entry).getId_zamowienia()));
			builder.dodajKolumne(((Zamowienia) entry).getAdres_wysylki_miasto().toString());
			builder.dodajKolumne(((Zamowienia) entry).getAdres_wysylki_ulica().toString());
			builder.dodajKolumne(Double.toString(((Zamowienia) entry).getKoszt()));
			
			for(Obiekt_Do_Polecen stan: fData) {
				if(((Stany_Zamowienia)stan).getId_Stanu_Zamowienia() == ((Zamowienia)entry).getId_stanu_zamowienia() ) {
					builder.dodajKolumne(((Stany_Zamowienia)stan).getNazwa());
					break;
				}
			}
			List<String> nPr = null;
			oc.createDBSession();
			try (Session session = oc.getDBSession()) {
	            Query<String> query = session.createQuery("SELECT p.nazwa FROM Produkty p, Zamowienia z, Produkt_Zamowienia pz where p.id_produktu = pz.produkt_zamowienia_id.id_produktu and pz.produkt_zamowienia_id.id_zamowienia = z.id_zamowienia and z.id_zamowienia = :id", String.class).setParameter("id", ((Zamowienia) entry).getId_zamowienia());
	            nPr = query.getResultList();
	            oc.closeDBSession();
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println(e);
	        }
											
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				builder.dodajKolumne(Integer.toString(((Zamowienia) entry).getUzytkownicy_id_uzytkownika()));
				builder.dodajKolumne(nPr);
				builder.dodajKolumne(((Zamowienia) entry).getOpis());
				builder.dodajKolumnePrzycisku();
				break;	
			case("Klient"):
				builder.dodajKolumne(nPr);
				builder.dodajKolumne(((Zamowienia) entry).getOpis());
				break;
			case("Pracownik"):
			default:
				builder.dodajKolumne(Integer.toString(((Zamowienia) entry).getUzytkownicy_id_uzytkownika()));
				builder.dodajKolumne(nPr);
				builder.dodajKolumne(((Zamowienia) entry).getOpis());
				break;
			}
		}
	}
	
	public void tworzTabeleTypy_uzytkownika(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
    {
		this.builder = builder;
		HibernateOracle.obj = new Typy_uzytkownika();
		HibernateOracle.wzorzec = null;
		builder.refresh();
		builder.dodajNaglowek();

		builder.dodajKolumne("Lp.");
		builder.dodajKolumne("Nazwa");

        for(Obiekt_Do_Polecen entry: entities)
        {
        	builder.dodajWiersz();
        	builder.dodajKolumne(Integer.toString(((Typy_uzytkownika) entry).getId_typu_uzytkownika()));
        	builder.dodajKolumne(((Typy_uzytkownika) entry).getNazwa().toString());
			switch(HibernateOracle.nazwaTypu) {
			case("Administrator"):
				builder.dodajKolumnePrzycisku();
				builder.dodajKolumnePrzycisku();
				break;
			}
        }
    }
	
	public void tworzTabeleKoszyk(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
    {
		HibernateOracle.obj = new Produkt_Koszyk();
		this.builder = builder;
		builder.refresh();
		builder.dodajNaglowek();

		builder.dodajKolumne("Lp.");
		builder.dodajKolumne("Nazwa");
		builder.dodajKolumne("Cena za sztuke");
        builder.dodajKolumne("Ilość");
        builder.dodajKolumne("Łączna cena");

        for(Obiekt_Do_Polecen entry: entities)
        {
        	builder.dodajWiersz();
        	builder.dodajKolumne(Integer.toString(((Produkt_Koszyk) entry).getPr().getId_produktu()));
        	builder.dodajKolumne(((Produkt_Koszyk) entry).getPr().getNazwa());
        	builder.dodajKolumne(Double.toString(((Produkt_Koszyk) entry).getPr().getCena()));
        	builder.dodajKolumne(Integer.toString(((Produkt_Koszyk) entry).getIlosc()));
        	builder.dodajKolumne(Double.toString(((Produkt_Koszyk) entry).getPr().getCena() *((Produkt_Koszyk) entry).getIlosc()));
            
        	builder.dodajKolumnePrzycisku();
			builder.dodajKolumnePrzycisku();
        }
    }
	
	
	public Object pobierzTabele() {
		return builder.pobierzTabele(null);
	}
}
