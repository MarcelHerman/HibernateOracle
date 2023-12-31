package net.codejava;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaProdukt_Koszyk implements IStrategia{

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
     	JTextField pierwszyField = new JTextField(7);
		 
         JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Ilosc: "));
		myPanel.add(pierwszyField);             		
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Edytuj typ użytkownika", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (result == JOptionPane.OK_OPTION) {
              	if(!pierwszyField.getText().isEmpty() && Integer.parseInt(pierwszyField.getText()) > 0)
              	{
              		for(Obiekt_Do_Polecen pk: HibernateOracle.koszyk) {
              			if(((Produkt_Koszyk)pk).getPr().getId_produktu() == bt.id) {
              				((Produkt_Koszyk)pk).setIlosc(Integer.parseInt(pierwszyField.getText()));
              				bt.tab.setValueAt(Integer.parseInt(pierwszyField.getText()), bt.row, 3); 	  
              				bt.tab.setValueAt(Integer.parseInt(pierwszyField.getText()) *((Produkt_Koszyk)pk).getPr().getCena() , bt.row, 4);
              				break;
              			}
              		}
              	}
              	else
              		throw new Exception("Nie podano wartosci lub podano niedopuszczalna liczbe");
              		 	 	              	
         	}
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Nie udało się edytować wartosci. Błąd: " + e.getMessage());
		 }
     
		
	}

	@Override
	public void dodajLogikeUsuwania() {
		// TODO Auto-generated method stub
		
	}

}
