package net.codejava;

public abstract class Polecenie {
	protected Obiekt_Do_Polecen obiekt;
	
	/*Polecenie(){
		
	}*/
	abstract void /*??*/ Wykonaj();
	private void Polecenie(Obiekt_Do_Polecen obiekt) {
		this.obiekt = obiekt;
	}
}
