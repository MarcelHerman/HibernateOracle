package net.codejava;

import java.util.ArrayList;

public class Repozytorium_Polecen {
	private ArrayList<Polecenie> polecenia = new ArrayList<Polecenie>();
	
	public void wykonajPolecenie(Polecenie polecenie) {
		polecenie.Wykonaj();
		polecenia.add(polecenie);
	}
	
	public ArrayList<Polecenie> getListaPolecen(){
		return polecenia;
	}

	public void setPolecenia(ArrayList<Polecenie> polecenia) {
		this.polecenia = polecenia;
	}
	
	
}
