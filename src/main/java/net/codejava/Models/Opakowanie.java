package net.codejava.Models;


public class Opakowanie extends ZamowieniaDekorator
{
 	public Opakowanie(IZamowienia zamowienie) {
		super(zamowienie);
		this.cena = 5;
		this.opis = "opakowanie+" + cena + "%";				
	}

	@Override
	public double getKoszt() {	
		return zamowienie.getKoszt() + (zamowienie.getKoszt()/100)*cena;
	}

	@Override
	public String getOpis() {
		return super.getOpis();
	}
}