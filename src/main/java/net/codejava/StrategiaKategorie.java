package net.codejava;

import net.codejava.Models.*;
import java.awt.Component;
import java.util.List;

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

public class StrategiaKategorie implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
     	JTextField pierwszePole = new JTextField(7);
         JPanel panel = new JPanel();
		
		panel.add(new JLabel("Nazwa kategorii: "));
		panel.add(pierwszePole);
		
		int wynik = JOptionPane.showConfirmDialog(null, panel, 
                "Edytuj kategorie", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (wynik == JOptionPane.OK_OPTION) {         		
         		Kategorie kat = new Kategorie(pierwszePole.getText());
         		
         		kat.setId_Kategorii(bt.id);
         		
         		if(!pierwszePole.getText().isEmpty())
         		{
         			//session.update(kat);
         			HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(kat, HibernateOracle.idUzytkownika));
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
	public void dodajLogikeUsuwania(ButtonEditor br) {

 		Kategorie pr = new Kategorie();
      	pr.setId_Kategorii(br.id);
      	//session.delete(pr);
      	//oc.closeDBSession();
      	List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Kategorie");
      	lista.remove(br.row);
      	HibernateOracle.cache.put("Kategorie", lista);
      	HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
      	((DefaultTableModel)br.tab.getModel()).removeRow(br.row);
	}
	
	public void dodajLogikeDodawania(JPanel kontener) {

	 	JTextField pierwszePole = new JTextField(7);
		 
        JPanel panel = new JPanel();
		
    	panel.add(new JLabel("Nazwa: "));
 		panel.add(pierwszePole);		         			         		
 		
 		int wynik = JOptionPane.showConfirmDialog(null, panel, 
                 "Dodaj kategorię", JOptionPane.OK_CANCEL_OPTION);
 		 try {
            	if (wynik == JOptionPane.OK_OPTION) {
            		
            		//OracleConnection oc =  OracleConnection.getInstance();
	                	//oc.createDBSession();
	                	//Session session = oc.getDBSession();
            		
	                	if(pierwszePole.getText().isEmpty())
	                	{
	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Kategoria nie została dodana");
	                		return;
	                	}
	                	
	                	//session.save(new Kategorie(pierwszePole.getText()));
	                	//oc.closeDBSession();
	                				 	                	
	                	//Polecenie_Dodaj pd = new Polecenie_Dodaj(new Kategorie(pierwszePole.getText()));
	                	//pd.Wykonaj();
	                	Kategorie nowaKategoria = new Kategorie(pierwszePole.getText());
	                	HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Dodaj(nowaKategoria, HibernateOracle.idUzytkownika));
	                	
	                	List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Kategorie");
	                	lista.add(nowaKategoria);
	                	HibernateOracle.cache.put("Kategorie", lista);
            			                		
	                	//pokazKategoriePrzycisk.doClick();
	                	
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
	                	
	                	nowaKategoria.setId_Kategorii(((Kategorie)lista.get(lista.size()-2)).getId_Kategorii()+1);
	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Kategorie)nowaKategoria).getId_Kategorii()), ((Kategorie)nowaKategoria).getNazwa()});

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
 			 JOptionPane.showMessageDialog(null, "Nie udało się dodać kategorii. Błąd: " + e.getMessage());
 		 }
		
	}

}
