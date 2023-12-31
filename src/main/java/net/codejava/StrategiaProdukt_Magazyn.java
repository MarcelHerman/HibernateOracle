package net.codejava;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaProdukt_Magazyn implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {

     	JTextField pierwszyField = new JTextField(7);
        JTextField drugiField = new JTextField(7);
        JTextField trzeciField = new JTextField(7);
        JTextField czwartyField = new JTextField(7);
		 
         JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Stan faktyczny: "));
		myPanel.add(pierwszyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Stan magazynowy: "));
		myPanel.add(drugiField);	                		
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Edytuj produkt w magazynie", JOptionPane.OK_CANCEL_OPTION); //??
		 try {
         	if (result == JOptionPane.OK_OPTION) {
         		
         		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
              	oc.createDBSession();
              	Session session = oc.getDBSession();                     															  
				 	     	                		
              	Produkt_Magazyn_Id pr = new Produkt_Magazyn_Id(bt.id, bt.id2);	 	                	
              	Produkt_Magazyn user = (Produkt_Magazyn)session.createQuery("select u from Produkt_Magazyn u where u.produkt_magazyn_id = :pr")
              			.setParameter("pr", pr)
              			.uniqueResult();
              	//System.out.println(user.getId_uzytkownika());
              	
              	oc.closeDBSession();
              	
              	if(!pierwszyField.getText().isEmpty())
              		if(Integer.parseInt(pierwszyField.getText())<0)
              			throw(new Exception("Nie można dodać ujemnego stanu faktycznego."));
              		else
              			user.setStan_faktyczny(Integer.parseInt(pierwszyField.getText()));
              		
              	if(!drugiField.getText().isEmpty())
              		if(Integer.parseInt(drugiField.getText())<0)
              			throw(new Exception("Nie można dodać ujemnego stanu magazynowego."));
              		else
              			user.setStan_magazynowy(Integer.parseInt(drugiField.getText()));
              	
         		//session.update(user);
         		HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));
              	
              	
              	System.out.println(user.getProdukt_magazyn_id() + " " + user.getStan_faktyczny() +  " " + user.getStan_magazynowy());
              	
         		
         		
         		bt.tab.setValueAt(user.getStan_faktyczny(), bt.row, 2); 	                		
     			bt.tab.setValueAt(user.getStan_magazynowy(), bt.row, 3);
         	}
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
		 }
     
		
	}

	@Override
	public void dodajLogikeUsuwania() {
		// TODO Auto-generated method stub
		
	}

}
