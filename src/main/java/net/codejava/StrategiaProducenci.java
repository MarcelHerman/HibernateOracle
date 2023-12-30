package net.codejava;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaProducenci implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
     	JTextField pierwszyField = new JTextField(7);
        JTextField drugiField = new JTextField(7);
        JTextField trzeciField = new JTextField(7);
        JTextField czwartyField = new JTextField(7);
		 
         JPanel myPanel = new JPanel();
		
		JCheckBox czyUsunietyCheck = new JCheckBox("Czy usunięty: ");
		
		myPanel.add(new JLabel("Nazwa producenta: "));
		myPanel.add(pierwszyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Kontakt: "));
		myPanel.add(drugiField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Miasto: "));
		myPanel.add(trzeciField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Ulica: "));
		myPanel.add(czwartyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(czyUsunietyCheck);
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Edytuj producenta", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (result == JOptionPane.OK_OPTION) {
         		
         		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
              	oc.createDBSession();
              	Session session = oc.getDBSession();
         		
              	Producenci user = (Producenci)session.createQuery("select u from Producenci u where u.id_producenta = :id")
              			.setParameter("id", bt.id)
              			.uniqueResult();
              	//System.out.println(user.getId_uzytkownika());
              	user.setCzy_usunieto(czyUsunietyCheck.isSelected()?1:0);
              	if(!pierwszyField.getText().isEmpty())
              		user.setNazwa(pierwszyField.getText());
              	if(!drugiField.getText().isEmpty())
              		user.setKontakt(drugiField.getText());
              	if(!trzeciField.getText().isEmpty())
              		user.setMiasto(trzeciField.getText());
              	if(!czwartyField.getText().isEmpty())
              		user.setUlica(czwartyField.getText());
         		session.update(user);
         		
         		oc.closeDBSession();
         		
         		bt.tab.setValueAt(user.getNazwa(), bt.row, 1); 	                		
     			bt.tab.setValueAt(user.getKontakt(), bt.row, 2);
     			bt.tab.setValueAt(user.getMiasto(), bt.row, 3); 	                		
     			bt.tab.setValueAt(user.getUlica(), bt.row, 4);
     			if(user.getCzy_usunieto() == 1)
         			bt.tab.setValueAt("TAK", bt.row, 5);
         		else   	                		
         			bt.tab.setValueAt("NIE", bt.row, 5);
         	}
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Nie udało się edytować producenta. Błąd: " + e.getMessage());
		 }
     
		
	}

	@Override
	public void dodajLogikeUsuwania() {
		// TODO Auto-generated method stub
		
	}

}
