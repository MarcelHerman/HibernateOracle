package net.codejava.Models;

public class Polecenie_Dodaj extends Polecenie {
	
	public Polecenie_Dodaj(Obiekt_Do_Polecen obiekt, int id_wykonawcy){
		super(obiekt, id_wykonawcy);
	}
	
	public void operacjaPolecenia() {
		sesja.save(this.obiekt);		
	}
	
	@Override
    public String toString() {
        return "Polecenie Dodaj: " + super.toString();
    }
}
