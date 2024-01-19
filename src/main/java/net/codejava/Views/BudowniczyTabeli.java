package net.codejava.Views;

public interface BudowniczyTabeli {
	
	public void dodajNaglowek();
	
	public void dodajKolumne(Object wartosc);
	
	public void dodajWiersz();
	
	public Object pobierzTabele();
	
	public void zresetuj();
	
	public void dodajKolumnePrzycisku();
}


