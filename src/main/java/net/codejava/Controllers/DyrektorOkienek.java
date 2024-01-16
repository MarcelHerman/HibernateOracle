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
	
	public void stworzOkno(Object... args) {
        if (args.length % 2 != 0) {
            System.out.println("czegoś brakuje xd");
            //return;
        }

        budowniczy.zresetuj();
        for (int i = 0; i < args.length-1; i += 2) {
        	if(args[i]==TypPola.label)
        		budowniczy.dodajLabel((String)args[i]);	
        	else if(args[i]==TypPola.checkbox)
        		budowniczy.dodajCheckBox((String)args[i]);	
        	else if(args[i]==TypPola.combobox) {
        		int x = (int) args[i+1];
        		String[] nazwy = new String[x]; 
        		for (int j=1; j<x; j++)
        			nazwy[j]=(String)args[i+j+1];
        		budowniczy.dodajJComboBox(nazwy);	
        		i+=x;
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
