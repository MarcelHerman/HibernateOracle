package net.codejava;

import org.hibernate.Session;

public class Polecenie_Dodaj extends Polecenie {
	
	Polecenie_Dodaj(Obiekt_Do_Polecen obiekt){
		super.obiekt = obiekt;
		//Repozytorium_Polecen.polecenie_Dodaj(obiekt);
	}
	
	public void Wykonaj() {
		OracleConnection oc = OracleConnection.getInstance();
		oc.createDBSession();
		Session session = oc.getDBSession();
		
		session.createQuery("INSERT INTO " + this.obiekt.getClass().getSimpleName() + " VALUES " + this.obiekt);
		
		oc.closeDBSession();
	}
}
