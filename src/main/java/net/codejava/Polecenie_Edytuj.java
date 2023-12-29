package net.codejava;

import org.hibernate.Session;

public class Polecenie_Edytuj extends Polecenie {
	
	Polecenie_Edytuj(Obiekt_Do_Polecen obiekt, int id_wykonawcy){
		super(obiekt, id_wykonawcy);
	}
	
	void Wykonaj() {
		OracleConnection oc = OracleConnection.getInstance();
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
