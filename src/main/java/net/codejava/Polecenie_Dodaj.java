package net.codejava;

import org.hibernate.Session;

public class Polecenie_Dodaj extends Polecenie {
	
	Polecenie_Dodaj(Obiekt_Do_Polecen obiekt){
		super(obiekt);
	}
	
	public void Wykonaj() {
		OracleConnection oc = OracleConnection.getInstance();
		oc.createDBSession();
		Session session = oc.getDBSession();
		
		session.save(this.obiekt);
		
		oc.closeDBSession();
	}
}
