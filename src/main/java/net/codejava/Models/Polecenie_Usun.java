package net.codejava.Models;

public class Polecenie_Usun extends Polecenie {

	public Polecenie_Usun(Obiekt_Do_Polecen obiekt, int id_wykonawcy){
		super(obiekt, id_wykonawcy);
	}
	
	public void operacjaPolecenia() {		
		sesja.delete(this.obiekt);
	}

	@Override
    public String toString() {
        return "Polecenie Usuń: " + super.toString();
    }
}
