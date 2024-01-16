package net.codejava.Controllers;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import net.codejava.Views.BudowniczyOkienka;

public class DyrektorOkienek {
	BudowniczyOkienka budowniczy = new BudowniczyOkienka();

	public void okienkoKategoriiEdytuj() {
		budowniczy.zresetuj();
		budowniczy.dodajLabel("Nazwa kategorii: ");
	}
	
	public void okienkoKategoriiDodaj()
	{
		budowniczy.zresetuj();
		budowniczy.dodajLabel("Nazwa");	
	}
	
	public void stworzOkno(String[][] tab, Object... args) {
        budowniczy.zresetuj();
        for (int i = 0; i < args.length-1; i += 2) {
        	if(args[i]==TypPola.label)
        		budowniczy.dodajLabel((String)args[i+1]);	
        	else if(args[i]==TypPola.checkbox)
        		budowniczy.dodajCheckBox((String)args[i+1]);	
        	else if(args[i]==TypPola.combobox) {
        		if((int)args[i+1]==1) budowniczy.dodajJComboBox(tab[0]);
        		else budowniczy.dodajJComboBox(tab[1]);
        	}
        }
    }
	
	public JPanel zwrocOkno() {
		return budowniczy.zwrocOkno();
	}
	
	public ArrayList<JTextField> zwrocPolaTekstowe(){
		return budowniczy.zwrocPolaTekstowe();
	}
}
