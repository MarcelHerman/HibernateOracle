package net.codejava.Controllers;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import net.codejava.Models.TypPola;
import net.codejava.Views.BudowniczyOkienka;

public class DyrektorOkienek {
	private BudowniczyOkienka budowniczy = new BudowniczyOkienka();

	public void stworzOkno(String[][] tab, Object... args) {
        budowniczy.zresetuj();
        for (int i = 0; i < args.length-1; i += 2) {
        	if(args[i]==TypPola.label)
        		budowniczy.dodajJTextField((String)args[i+1]);	
        	else if(args[i]==TypPola.checkbox)
        		budowniczy.dodajCheckBox((String)args[i+1]);	
        	else if(args[i]==TypPola.combobox) {
        		if((int)args[i+1]==1) budowniczy.dodajJComboBox(tab[0]);
        		else budowniczy.dodajJComboBox(tab[1]);
        	}
        }
    }
	
	public void edytowanieZamowieniaPracownik(String[] nazwy) {
		budowniczy.zresetuj();
		budowniczy.dodajJComboBox(nazwy);
	}
		
	public void edytowanieZamowieniaAdministrator(String[] nazwy) {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Ulica wysyłki: ");
		budowniczy.dodajJTextField("Miasto wysyłki: ");
		budowniczy.dodajJComboBox(nazwy);
	}
		
	public void dodawanieZamowienia() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Id użytkownika: ");
		budowniczy.dodajJTextField("Adres wysyłki miasto: ");
		budowniczy.dodajJTextField("Adres wysyłki ulica: ");
		budowniczy.dodajJTextField("Koszt: ");
	}
		
	public void edytowanieUzytkownicy(String[] nazwy) {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Nazwa użytkownika: ");
		budowniczy.dodajJTextField("Login: ");
		budowniczy.dodajJTextField("Hasło: ");
		budowniczy.dodajJTextField("E-mail: ");
		budowniczy.dodajJComboBox(nazwy);
		budowniczy.dodajCheckBox("Czy usunięty: ");
	}
	
	public void dodawanieUzytkownicy(String[] nazwy) {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Nazwa użytkownika: ");
		budowniczy.dodajJTextField("Login: ");
		budowniczy.dodajJTextField("Hasło: ");
		budowniczy.dodajJTextField("E-mail: ");
		budowniczy.dodajJComboBox(nazwy);
		budowniczy.dodajCheckBox("Czy usunięty: ");
	}
	
	public void edytowanieProdukty(String[] nazwy) {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Nazwa produktu: ");
		budowniczy.dodajJTextField("Cena: ");
		budowniczy.dodajJTextField("Opis: ");
		budowniczy.dodajJComboBox(nazwy);
		budowniczy.dodajCheckBox("Czy usunięty: ");
	}
	
	public void dodawanieProdukty(String[] nazwy, String[] nazwy2) {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Nazwa produktu: ");
		budowniczy.dodajJTextField("Cena: ");
		budowniczy.dodajJTextField("Opis: ");
		budowniczy.dodajJComboBox(nazwy);
		budowniczy.dodajJComboBox(nazwy2);
	}
	
	public void edytowanieProdukt_Zamowienia() {}
	
	public void dodawanieProdukt_Zamowienia() {	
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Id zamowienia: ");
		budowniczy.dodajJTextField("Id produktu: ");
		budowniczy.dodajJTextField("Ilość: ");	
	}
	
	public void edytowanieProdukt_Magazyn() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Stan faktyczny: ");
		budowniczy.dodajJTextField("Stan magazynowy: ");
	}
	
	public void dodawanieProdukt_Magazyn() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Id magazynu: ");
		budowniczy.dodajJTextField("Id produktu: ");
		budowniczy.dodajJTextField("Stan faktyczny: ");
		budowniczy.dodajJTextField("Stan magazynowy: ");
	}
	
	public void edytowaniePodukt_Koszyk() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Ilość: ");
	}
	
	public void dodawaniePodukt_Koszyk() {
	}
	
	public void edytowanieProducenci() {	
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Nazwa producenta: ");
		budowniczy.dodajJTextField("Kontakt: ");
		budowniczy.dodajJTextField("Miasto: ");
		budowniczy.dodajJTextField("Ulica: ");
		budowniczy.dodajCheckBox("Czy usunięty: ");
	}
	
	public void dodawanieProducenci() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Nazwa producenta: ");
		budowniczy.dodajJTextField("Kontakt: ");
		budowniczy.dodajJTextField("Miasto: ");
		budowniczy.dodajJTextField("Ulica: ");
	}
	
	public void edytowanieMagazyny() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Miasto: ");
		budowniczy.dodajJTextField("Ulica: ");
	}
	
	public void dodawanieMagazyny() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Miasto: ");
		budowniczy.dodajJTextField("Ulica: ");
	}
	
	public void edytowanieFaktury() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("NIP: ");
	}
	
	public void dodawanieFaktury() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("NIP: ");
		budowniczy.dodajJTextField("ID zamówienia: ");
	}
	
	public void edytowanieKategorie() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Nazwa kategorii: ");
	}
	
	public void dodawanieKategorie() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Nazwa: ");
	}
	
	public void zalozKontoOkienko() {
		budowniczy.zresetuj();
		budowniczy.dodajJTextField("Nazwa użytkownika: ");
		budowniczy.dodajJTextField("Login: ");
		budowniczy.dodajJTextField("Hasło: ");
		budowniczy.dodajJTextField("E-mail: ");
	}
	
	public JPanel zwrocOkno() {
		return budowniczy.zwrocOkno();
	}
	
	public ArrayList<JTextField> zwrocPolaTekstowe(){
		return budowniczy.zwrocPolaTekstowe();
	}
}
