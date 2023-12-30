package net.codejava;

import org.hibernate.Session;

public class Polecenie_Dodaj extends Polecenie {
	
	Polecenie_Dodaj(Obiekt_Do_Polecen obiekt, int id_wykonawcy){
		super(obiekt, id_wykonawcy);
	}
	
	public void Wykonaj() {
		PolaczenieOracle oc = PolaczenieOracle.getInstance();
		oc.createDBSession();
		Session session = oc.getDBSession();
		
		session.save(this.obiekt);
		
		oc.closeDBSession();
	}
	
	@Override
    public String toString() {
        return "Polecenie Dodaj: " + super.toString();
    }
}
