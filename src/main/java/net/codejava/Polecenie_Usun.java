package net.codejava;

import org.hibernate.Session;

public class Polecenie_Usun extends Polecenie {

	Polecenie_Usun(Obiekt_Do_Polecen obiekt){
		super(obiekt);
	}
	
	void Wykonaj() {
		OracleConnection oc = OracleConnection.getInstance();
		oc.createDBSession();
		Session session = oc.getDBSession();
		
		session.delete(this.obiekt);
		oc.closeDBSession();
	}

}
