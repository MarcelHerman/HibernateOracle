package net.codejava.Views;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;

import net.codejava.HibernateOracle;

public class BudowniczyTabeliDruk implements BudowniczyTabeli
{
	private LinkedList<String> naglowek;
	private LinkedList<LinkedList<Object>> dane = new LinkedList<LinkedList<Object>>();
	private LinkedList<Object> wiersz;

	public void zresetuj() {
		this.wiersz = null;
		this.dane =  new LinkedList<LinkedList<Object>>();
	}
	
	public void dodajKolumnePrzycisku() {}
	
	@Override
	public void dodajNaglowek() {
		this.naglowek = new LinkedList<String>();	
	}

	@Override
	public void dodajKolumne(Object wartosc) {
		if(this.wiersz==null)this.naglowek.addLast(wartosc.toString());
		else this.wiersz.addLast(wartosc);		
	}

	@Override
	public void dodajWiersz() {
		if(this.wiersz!=null)this.dane.addLast(wiersz);
		this.wiersz = new LinkedList<Object>();
	} 

	@Override
	public Object pobierzTabele(Object ob) {
		if (this.wiersz != null) {
	        this.dane.addLast(wiersz);
	    }

	    Object[] naglowek = this.naglowek.toArray();
	    Object[][] dane = new Object[this.dane.size()][];
	    int i = 0;
	    for (LinkedList<Object> w : this.dane) {
	        dane[i++] = w.toArray();
	    }

	    // Tworzenie łańcucha znaków dla nagłówków
	    String naglowki = String.join(", ", (Iterable<? extends CharSequence>) Arrays.asList(naglowek)) + "\n";

	    // Tworzenie łańcucha znaków dla danych
	    String daneString = "Wykaz " +HibernateOracle.obiekt.getClass().getSimpleName() + " z dnia " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +"\n"+ naglowki;
	    for (Object[] row : dane) {
	        String rowString = String.join(", ", (Iterable<? extends CharSequence>) Arrays.asList(row)) + "\n";
	        daneString += rowString;
	    }

	    return daneString;
	}
}