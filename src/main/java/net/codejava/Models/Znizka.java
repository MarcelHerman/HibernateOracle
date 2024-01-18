package net.codejava.Models;

public class Znizka extends ZamowieniaDekorator
{
	public Znizka(IZamowienia zamowienie, double cena) {
		super(zamowienie);
		this.cena = cena;
		this.opis = "znizka-" + cena + "%";				
	}
	
	@Override
	public double getKoszt() {	
		return zamowienie.getKoszt() - (zamowienie.getKoszt()/100)*cena;
	}

	@Override
	public String getOpis() {
		return super.getOpis();
	}
}