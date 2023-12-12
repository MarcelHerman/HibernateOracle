package net.codejava;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;

public interface BudowniczyTabeli {
	
	public void dodajNaglowek();
	
	public void dodajKolumne(String wartosc);
	
	public void dodajWiersz();
}


class BudowniczyTabeliSwing implements BudowniczyTabeli
{
	private LinkedList<String> naglowek;
	private LinkedList<LinkedList<String>> dane = new LinkedList<LinkedList<String>>();
	private LinkedList<String> wiersz;

	public void dodajNaglowek() {
		naglowek = new LinkedList<String>();
	}

	public void dodajKolumne(String wartosc) {
		if(wiersz==null)naglowek.addLast(wartosc);
		else wiersz.addLast(wartosc);
	}

	public void dodajWiersz() {
		if(wiersz!=null)dane.addLast(wiersz);
		wiersz = new LinkedList<String>();
	}
	
	public void dodajPrzycisk(BudowniczyTabeli budowniczy)
	{
		
	}
	
	
	public JTable pobierzTabeleSwing()
	{
		if(wiersz!=null) dane.addLast(wiersz);
		Object[] nagl = naglowek.toArray();
		Object[][] dan = new Object[dane.size()][];
		int i = 0;
		for(LinkedList<String> w:dane) dan[i++]=w.toArray();
		return new JTable(dan, nagl);
	}
	

	void tworzTabeleMagazyn(List<Obiekt_Do_Polecen> entities)
	{
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Miasto");
		this.dodajKolumne("Ulica");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Magazyny) entry).getId_magazynu()));
			this.dodajKolumne(((Magazyny) entry).getMiasto().toString());
			this.dodajKolumne(((Magazyny) entry).getUlica().toString());
		}
	}
	
	void tworzTabeleProdukty(List<Obiekt_Do_Polecen> entities)
	{
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Miasto");
		this.dodajKolumne("Ulica");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Magazyny) entry).getId_magazynu()));
			this.dodajKolumne(((Magazyny) entry).getMiasto().toString());
			this.dodajKolumne(((Magazyny) entry).getUlica().toString());
		}
	}
	
	void tworzTabeleKategorie(List<Obiekt_Do_Polecen> entities)
	{
		this.dodajNaglowek();
		
		this.dodajKolumne("Lp.");
		this.dodajKolumne("Nazwa");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			this.dodajWiersz();			
			this.dodajKolumne(Integer.toString(((Kategorie) entry).getId_Kategorii()));
			this.dodajKolumne(((Kategorie) entry).getNazwa().toString());
		}
	}
}