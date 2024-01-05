package net.codejava;

import net.codejava.Models.*;
import org.hibernate.Session;

public class Polecenie_Edytuj extends Polecenie {
	
	Polecenie_Edytuj(Obiekt_Do_Polecen obiekt, int id_wykonawcy){
		super(obiekt, id_wykonawcy);
	}
	
	void Wykonaj() {
		PolaczenieOracle oc = PolaczenieOracle.getInstance();
		oc.createDBSession();
		Session session = oc.getDBSession();
		
		session.update(this.obiekt);
		oc.closeDBSession();
	}

	@Override
    public String toString() {
        return "Polecenie Edytuj: " + super.toString();
    }
}
