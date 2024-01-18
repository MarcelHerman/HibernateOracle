package net.codejava.Models;


public class Notatka extends ZamowieniaDekorator
{
	public Notatka(IZamowienia zamowienie, String notka) {
		super(zamowienie);
		this.cena = 2.5;
		this.opis = "notatka+" + cena + "pln:'" + notka+"'";				
	}

	@Override
	public double getKoszt() {	
		return zamowienie.getKoszt() + cena;
	}

	@Override
	public String getOpis() {
		return super.getOpis();
	}
}