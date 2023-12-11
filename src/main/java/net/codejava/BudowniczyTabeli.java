package net.codejava;

import java.util.LinkedList;

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
	

	void tworzTabele(BudowniczyTabeli budowniczy)
	{
		budowniczy.dodajNaglowek();
		budowniczy.dodajKolumne("Lp.");
		budowniczy.dodajKolumne("Imię");
		budowniczy.dodajKolumne("Nazwisko");
		budowniczy.dodajWiersz();
		budowniczy.dodajKolumne("1");
		budowniczy.dodajKolumne("Jan");
		budowniczy.dodajKolumne("xd");
		budowniczy.dodajWiersz();
		budowniczy.dodajKolumne("2");
		budowniczy.dodajKolumne("Jan");
		budowniczy.dodajKolumne("xd");
	}
}