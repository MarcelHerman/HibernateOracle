package net.codejava;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.hibernate.Session;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StanKategorii implements IsmiesznyWzorzec {

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
         		
         		OracleConnection oc =  OracleConnection.getInstance();
              	oc.createDBSession();
              	Session session = oc.getDBSession();
         		
         		Kategorie kat = new Kategorie(pierwszyField.getText());
         		kat.setId_Kategorii(bt.id);
         		oc.closeDBSession();
         		if(!pierwszyField.getText().isEmpty())
         		{
         			//session.update(kat);
         			HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(kat, HibernateOracle.idUzytkownika));
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
