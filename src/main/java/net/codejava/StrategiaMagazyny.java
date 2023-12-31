package net.codejava;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaMagazyny implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
     	JTextField pierwszyField = new JTextField(7);
        JTextField drugiField = new JTextField(7);
		 
         JPanel myPanel = new JPanel();

		myPanel.add(new JLabel("Miasto: "));
		myPanel.add(pierwszyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Ulica: "));
		myPanel.add(drugiField);
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Edytuj magazyn", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (result == JOptionPane.OK_OPTION) {
         		
         		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
              	oc.createDBSession();
              	Session session = oc.getDBSession();
         		
              	Magazyny user = (Magazyny)session.createQuery("select u from Magazyny u where u.id_magazynu = :id")
              			.setParameter("id", bt.id)
              			.uniqueResult();
              	//System.out.println(user.getId_uzytkownika());
              	oc.closeDBSession();
              	if(!pierwszyField.getText().isEmpty())
              		user.setMiasto(pierwszyField.getText());
              	if(!drugiField.getText().isEmpty())
              		user.setUlica(drugiField.getText());
              	
         		//session.update(user);
         		HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));
         		
         		bt.tab.setValueAt(user.getMiasto(), bt.row, 1); 	                		
     			bt.tab.setValueAt(user.getUlica(), bt.row, 2);
         	}
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Nie udało się edytować magazynu. Błąd: " + e.getMessage());
		 }
     
		
	}

	@Override
	public void dodajLogikeUsuwania() {
		// TODO Auto-generated method stub
		
	}

}
