package net.codejava;

import java.util.LinkedList;
import java.util.List;

public class DyrektorTabel {
	
	private BudowniczyTabeli builder;
	
	public DyrektorTabel(BudowniczyTabeli builder) {
		this.builder = builder;
	}
	
	public void zmienBuildera(BudowniczyTabeli builder) {
		this.builder = builder;
	}
	
	public void tworzTabeleKategorie(List<Obiekt_Do_Polecen> entities, BudowniczyTabeli builder)
	{
		HibernateOracle.obj = new Kategorie();
		builder.refresh();
		builder.dodajNaglowek();
		
		builder.dodajKolumne("Lp.");
		builder.dodajKolumne("Nazwa");
		
		for(Obiekt_Do_Polecen entry: entities)
		{
			builder.dodajWiersz();			
			builder.dodajKolumne(Integer.toString(((Kategorie) entry).getId_Kategorii()));
			builder.dodajKolumne(((Kategorie) entry).getNazwa().toString());
			builder.dodajKolumnyPrzyciskow();
		}
	}
	
	public Object pobierzTabele() {
		return builder.pobierzTabele(null);
	}
}
