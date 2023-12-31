package net.codejava;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.hibernate.Session;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaKategorie implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
     	JTextField pierwszyField = new JTextField(7);
         JPanel myPanel = new JPanel();
		
		myPanel.add(new JLabel("Nazwa kategorii: "));
		myPanel.add(pierwszyField);
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Edytuj kategorie", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (result == JOptionPane.OK_OPTION) {         		
         		Kategorie kat = new Kategorie(pierwszyField.getText());
         		
         		kat.setId_Kategorii(bt.id);
         		
         		if(!pierwszyField.getText().isEmpty())
         		{
         			//session.update(kat);
         			HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(kat, HibernateOracle.idUzytkownika));
         			//dodać cache
             		List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Kategorie");

             		for (int i = 0; i < lista.size(); i++) {
             		    Obiekt_Do_Polecen element = lista.get(i);
             		   Kategorie pom = (Kategorie) element;

             		    if (pom.getId_Kategorii() == bt.id) {
             		        pom.setNazwa(kat.getNazwa());
             		        break;
             		    }
             		}

             		HibernateOracle.cache.put("Kategorie", lista);
         		}
         		
         		//oc.closeDBSession();
         		bt.tab.setValueAt(kat.getNazwa(), bt.row, 1);
         	}
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Nie udało się edytować kategorii. Błąd: " + e.getMessage());
		 }	        
	}

	@Override
	public void dodajLogikeUsuwania() {
		// TODO Auto-generated method stub
		
	}

}
