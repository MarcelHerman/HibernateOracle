package net.codejava;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaStany_Zamowienia implements IsmiesznyWzorzec {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {

     	JTextField pierwszyField = new JTextField(7);
        JTextField drugiField = new JTextField(7);
        JTextField trzeciField = new JTextField(7);
        JTextField czwartyField = new JTextField(7);
		 
         JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Nazwa: "));
		myPanel.add(pierwszyField);             		
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Edytuj stan zamówienia", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (result == JOptionPane.OK_OPTION) {
         		
         		OracleConnection oc =  OracleConnection.getInstance();
              	oc.createDBSession();
              	Session session = oc.getDBSession();  	 	                 
              	    	 	                	
              	Stany_Zamowienia user = (Stany_Zamowienia)session.createQuery("select u from Stany_Zamowienia u where u.id_Stanu_Zamowienia = :id")
              			.setParameter("id", bt.id)
              			.uniqueResult();	     	 	                	
              	
              	if(!pierwszyField.getText().isEmpty())
              		user.setNazwa(pierwszyField.getText());	 
         		session.update(user);

         		
              	
         		oc.closeDBSession();
         		
         	}
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Nie udało się edytować stanu zamówienia. Błąd: " + e.getMessage());
		 }
     
		
	}

	@Override
	public void dodajLogikeUsuwania() {
		// TODO Auto-generated method stub
		
	}

}
