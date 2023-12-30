package net.codejava;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaFaktury implements IsmiesznyWzorzec {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
     	JTextField pierwszyField = new JTextField(7);
		 
         JPanel myPanel = new JPanel();
		
    	myPanel.add(new JLabel("NIP: "));
		myPanel.add(pierwszyField);             		
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Edytuj fakturę", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (result == JOptionPane.OK_OPTION) {
         		
         		OracleConnection oc =  OracleConnection.getInstance();
              	oc.createDBSession();
              	Session session = oc.getDBSession();
         		
              	 	 	                	
              	Faktury user = (Faktury)session.createQuery("select u from Faktury u where u.id_faktury = :id")
              			.setParameter("id", bt.id)
              			.uniqueResult();
              	//System.out.println(user.getId_uzytkownika());
              	
              	if(!pierwszyField.getText().isEmpty())
              		user.setNIP(pierwszyField.getText());	     	 	              	
         		session.update(user);
         			  	 	                	
         		oc.closeDBSession();
         		
         		bt.tab.setValueAt(user.getNIP(),bt.row,2);
         	}
	 }
	 catch(Exception e) {
		 e.printStackTrace();
		 JOptionPane.showMessageDialog(null, "Nie udało się edytować faktury. Błąd: " + e.getMessage());
	 }	
		
	}

	@Override
	public void dodajLogikeUsuwania() {
		// TODO Auto-generated method stub
		
	}

}
