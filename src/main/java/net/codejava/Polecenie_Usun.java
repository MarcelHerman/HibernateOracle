package net.codejava;

import org.hibernate.Session;

public class Polecenie_Usun extends Polecenie {

	Polecenie_Usun(Obiekt_Do_Polecen obiekt, int id_wykonawcy){
		super(obiekt, id_wykonawcy);
	}
	
	void Wykonaj() {
		PolaczenieOracle oc = PolaczenieOracle.getInstance();
		oc.createDBSession();
		Session session = oc.getDBSession();
		
		session.delete(this.obiekt);
		oc.closeDBSession();
	}

	@Override
    public String toString() {
        return "Polecenie Usuń: " + super.toString();
    }
}
