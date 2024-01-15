package net.codejava.Models;

import org.hibernate.Session;

public class Polecenie_Dodaj extends Polecenie {
	
	public Polecenie_Dodaj(Obiekt_Do_Polecen obiekt, int id_wykonawcy){
		super(obiekt, id_wykonawcy);
	}
	
	public void operacjaPolecenia() {
		session.save(this.obiekt);		
	}
	
	@Override
    public String toString() {
        return "Polecenie Dodaj: " + super.toString();
    }
}
