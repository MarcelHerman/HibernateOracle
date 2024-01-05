package net.codejava.Views;

public interface BudowniczyTabeli {
	
	public void dodajNaglowek();
	
	public void dodajKolumne(Object wartosc);
	
	public void dodajWiersz();
	
	public Object pobierzTabele(Object ob);
	
	public void refresh();
	
	public void dodajKolumnePrzycisku();
}


