package net.codejava;

import java.awt.Component;
import java.time.LocalDate;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaFaktury implements IStrategia {

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
         		
         		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
              	oc.createDBSession();
              	Session session = oc.getDBSession();
         		
              	 	 	                	
              	Faktury user = (Faktury)session.createQuery("select u from Faktury u where u.id_faktury = :id")
              			.setParameter("id", bt.id)
              			.uniqueResult();
         		oc.closeDBSession();

              	if(!pierwszyField.getText().isEmpty())
              		user.setNIP(pierwszyField.getText());	     	 	              	
         		//session.update(user);
         		HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));
         			  	 	                	
         		bt.tab.setValueAt(user.getNIP(),bt.row,2);
         	}
	 }
	 catch(Exception e) {
		 e.printStackTrace();
		 JOptionPane.showMessageDialog(null, "Nie udało się edytować faktury. Błąd: " + e.getMessage());
	 }	
		
	}

	@Override
	public void dodajLogikeUsuwania(ButtonEditor br) {

 		Faktury pr = new Faktury();
      	pr.setId_faktury(br.id);
      	//session.delete(pr);
      	HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
      	//oc.closeDBSession();
      	((DefaultTableModel)br.tab.getModel()).removeRow(br.row);
 	
	}
	
	public void dodajLogikeDodawania(JPanel kontener) {

	 	JTextField pierwszyField = new JTextField(7);
        JTextField drugiField = new JTextField(7);
		 
        JPanel myPanel = new JPanel();
		
    	myPanel.add(new JLabel("NIP: "));
 		myPanel.add(pierwszyField);
 		myPanel.add(Box.createHorizontalStrut(5));
 		myPanel.add(new JLabel("Id zamówienia: "));
 		myPanel.add(drugiField);		         		
 		
 		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Dodaj fakturę", JOptionPane.OK_CANCEL_OPTION);
 		 try {
            	if (result == JOptionPane.OK_OPTION) {
            		
            		//OracleConnection oc =  OracleConnection.getInstance();
	                	//oc.createDBSession();
	                	//Session session = oc.getDBSession();
            		
	                	if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty())
	                	{
	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Faktura nie została dodana");
	                		return;
	                	}
	                	//oc.closeDBSession();
	                	
	                	//session.save(new Faktury(LocalDate.now(), pierwszyField.getText(), Integer.parseInt(drugiField.getText())));
	                	
	                	Faktury nowaFaktura = new Faktury(LocalDate.now(), pierwszyField.getText(), Integer.parseInt(drugiField.getText()));
	                	HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowaFaktura, HibernateOracle.idUzytkownika));
            		
            		//pokazFakturyPrzycisk.doClick();
	                
	                	
	                	Component[] components = kontener.getComponents();
	                	JTable tab = null;
	                	JButton dodajPrzycisk = null;
	                	JButton eksportujDoDruku = null;
	                	
	                	for(Component component : components)
	                	{
	                		if (component instanceof JScrollPane) {
	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
	                	        dodajPrzycisk = (JButton) kontener.getComponent(1);
	                	        eksportujDoDruku = (JButton) kontener.getComponent(2);
	                	        kontener.removeAll();
	                	        break;
	                	    }
	                	}		
	                	
	                	int id = Integer.parseInt(((DefaultTableModel)tab.getModel()).getValueAt(((DefaultTableModel)tab.getModel()).getRowCount()-1, 0).toString());

	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Faktury)nowaFaktura).getId_faktury()), ((Faktury)nowaFaktura).getData_wystawienia(), ((Faktury)nowaFaktura).getNIP(), Integer.toString(((Faktury)nowaFaktura).getZamowienia_id_zamowienia())});

	                	JScrollPane pane = new JScrollPane(tab);
	                	kontener.add(pane);
	                	kontener.add(dodajPrzycisk);
						kontener.add(eksportujDoDruku);			 	                	
	                	kontener.repaint();	
	                	kontener.revalidate();
            	}
 		 }
 		 catch(Exception e) {
 			 e.printStackTrace();
 			 JOptionPane.showMessageDialog(null, "Nie udało się dodać faktury. Błąd: " + e.getMessage());
 		 }
    
	}

}
