package net.codejava;
import net.codejava.Models.*;

public class Produkt_Koszyk implements Obiekt_Do_Polecen{
	
	Produkty produkt;
	private int ilosc;
	
	public Produkt_Koszyk() {}
	public Produkt_Koszyk(Produkty pr, int ilosc)
	{
		this.produkt=pr;
		this.ilosc=ilosc;
	}

	public Produkty getPr() {
		return produkt;
	}

	public void setPr(Produkty pr) {
		this.produkt = pr;
	}

	public int getIlosc() {
		return ilosc;
	}

	public void setIlosc(int ilosc) {
		this.ilosc = ilosc;
	}
	
	
}
