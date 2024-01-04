package net.codejava;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaProdukt_Zamowienia implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
	}

	@Override
	public void dodajLogikeUsuwania(ButtonEditor bt) {
		Produkt_Zamowienia pr = new Produkt_Zamowienia();
           pr.setProdukt_zamowienia_id(new Produkt_Zamowienia_Id(bt.id, bt.id2));
           //session.delete(pr);
      	HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
       // oc.closeDBSession();
      	((DefaultTableModel)bt.tab.getModel()).removeRow(bt.row);
		
	}

	@Override
	public void dodajLogikeDodawania(JPanel kontener) {
		
	 	JTextField pierwszePole = new JTextField(7);
        JTextField drugiePole = new JTextField(7);
        JTextField trzeciePole = new JTextField(7);
        JTextField czwartePole = new JTextField(7);
        JTextField piatePole = new JTextField(7);
		 
        JPanel panel = new JPanel();
        
		panel.add(new JLabel("Id zamówienia: "));
 		panel.add(pierwszePole);
 		panel.add(Box.createHorizontalStrut(5));
 		panel.add(new JLabel("Id produktu: "));
 		panel.add(drugiePole);
 		panel.add(Box.createHorizontalStrut(5));
 		panel.add(new JLabel("Ilość: "));
 		panel.add(trzeciePole);
 		
 		int wynik = JOptionPane.showConfirmDialog(null, panel, 
                 "Dodaj produkt do zamówienia", JOptionPane.OK_CANCEL_OPTION);
 		 try {
            	if (wynik == JOptionPane.OK_OPTION) {
            		
            		//OracleConnection oc = OracleConnection.getInstance();
	                	//oc.createDBSession();
	                	//Session session = oc.getDBSession();
            		
	                	if(pierwszePole.getText().isEmpty() || drugiePole.getText().isEmpty() || trzeciePole.getText().isEmpty())
	                	{
	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Produkt nie został dodany do zamówienia.");
	                		return;
	                	}
	                	
	                	if(Integer.parseInt(trzeciePole.getText())<=0)
	                		throw(new Exception("Ilość nie może być ujemna ani równa 0."));
	                	
	                	Produkt_Zamowienia_Id idpz = new Produkt_Zamowienia_Id(Integer.parseInt(pierwszePole.getText()), Integer.parseInt(drugiePole.getText()));
	                	
	                	Produkt_Zamowienia nowyPZ = new Produkt_Zamowienia(idpz, Integer.parseInt(trzeciePole.getText()));
	                	
	                	//session.save(new Produkt_Zamowienia(idpz, Integer.parseInt(trzeciePole.getText())));
	                	HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Dodaj(nowyPZ, HibernateOracle.idUzytkownika));

            		
            		//oc.closeDBSession();
            		//pokazProduktZamowieniaPrzycisk.doClick();
	                	
	                	Component[] komponenty = kontener.getComponents();
	                	JTable tab = null;
	                	JButton dodajPrzycisk = null;
	                	JButton eksportujDoDruku = null;
	                	
	                	for(Component komponent : komponenty)
	                	{
	                		if (komponent instanceof JScrollPane) {
	                	        tab = (JTable) (((JScrollPane)komponent).getViewport().getView());
	                	        dodajPrzycisk = (JButton) kontener.getComponent(1);
	                	        eksportujDoDruku = (JButton) kontener.getComponent(2);
	                	        kontener.removeAll();
	                	        break;
	                	    }
	                	}		 	                	
	                	
	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Produkt_Zamowienia)nowyPZ).getZamowienieId()), Integer.toString(((Produkt_Zamowienia)nowyPZ).getProduktId()), Integer.toString(((Produkt_Zamowienia)nowyPZ).getIlosc())});

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
 			 JOptionPane.showMessageDialog(null, "Nie udało się dodać produktu do zamówienia. Błąd: " + e.getMessage());
 		 }
   
		
	}

}
