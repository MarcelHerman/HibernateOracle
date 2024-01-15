package net.codejava.Models;

import org.hibernate.Session;

public class Polecenie_Edytuj extends Polecenie {
	
	public Polecenie_Edytuj(Obiekt_Do_Polecen obiekt, int id_wykonawcy){
		super(obiekt, id_wykonawcy);
	}
	
	public void operacjaPolecenia() {
		session.update(this.obiekt);
	}

	@Override
    public String toString() {
        return "Polecenie Edytuj: " + super.toString();
    }
}
