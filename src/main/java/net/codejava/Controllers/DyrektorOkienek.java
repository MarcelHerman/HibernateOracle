package net.codejava.Controllers;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import net.codejava.Views.BudowniczyOkienka;

public class DyrektorOkienek {
	BudowniczyOkienka budowniczy = new BudowniczyOkienka();
	

	public void okienkoKategoriiEdytuj() {
		budowniczy.refresh();
		budowniczy.dodajLabel("Nazwa kategorii: ");
	}
	
	public JPanel zwrocOkno() {
		return budowniczy.zwrocOkno();
	}
	
	public ArrayList<JTextField> zwrocPolaTekstowe(){
		return budowniczy.zwrocPolaTekstowe();
	}
}
