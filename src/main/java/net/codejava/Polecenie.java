package net.codejava;

public abstract class Polecenie {
	protected Obiekt_Do_Polecen obiekt;
	
	abstract void /*??*/ Wykonaj();
	
	public Polecenie(Obiekt_Do_Polecen obiekt) {
		this.obiekt = obiekt;
	}
}
