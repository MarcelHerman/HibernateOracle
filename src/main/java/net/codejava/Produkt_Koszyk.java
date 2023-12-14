package net.codejava;

public class Produkt_Koszyk implements Obiekt_Do_Polecen{
	
	Produkty pr;
	private int ilosc;
	
	public Produkt_Koszyk() {}
	public Produkt_Koszyk(Produkty pr, int ilosc)
	{
		this.pr=pr;
		this.ilosc=ilosc;
	}

	public Produkty getPr() {
		return pr;
	}

	public void setPr(Produkty pr) {
		this.pr = pr;
	}

	public int getIlosc() {
		return ilosc;
	}

	public void setIlosc(int ilosc) {
		this.ilosc = ilosc;
	}
	
	
}
