package net.codejava;

import org.hibernate.Session;

public class Polecenie_Edytuj extends Polecenie {
	
	Polecenie_Edytuj(Obiekt_Do_Polecen obiekt){
		super(obiekt);
	}
	
	void /*??*/ Wykonaj() {
		OracleConnection oc = OracleConnection.getInstance();
		oc.createDBSession();
		Session session = oc.getDBSession();
		
		session.update(this.obiekt);
		oc.closeDBSession();
	}

}
