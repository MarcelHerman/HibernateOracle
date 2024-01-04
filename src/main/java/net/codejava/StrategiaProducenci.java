package net.codejava;

import java.awt.Component;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaProducenci implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
     	JTextField pierwszePole = new JTextField(7);
        JTextField drugiePole = new JTextField(7);
        JTextField trzeciePole = new JTextField(7);
        JTextField czwartePole = new JTextField(7);
		 
         JPanel panel = new JPanel();
		
		JCheckBox czyUsunietyCheck = new JCheckBox("Czy usunięty: ");
		
		panel.add(new JLabel("Nazwa producenta: "));
		panel.add(pierwszePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Kontakt: "));
		panel.add(drugiePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Miasto: "));
		panel.add(trzeciePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Ulica: "));
		panel.add(czwartePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(czyUsunietyCheck);
		
		int wynik = JOptionPane.showConfirmDialog(null, panel, 
                "Edytuj producenta", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (wynik == JOptionPane.OK_OPTION) {
         		
         		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
              	oc.createDBSession();
              	Session session = oc.getDBSession();
         		
              	Producenci user = (Producenci)session.createQuery("select u from Producenci u where u.id_producenta = :id")
              			.setParameter("id", bt.id)
              			.uniqueResult();
              	//System.out.println(user.getId_uzytkownika());
              	
              	int szukany = user.getId_producenta();
              	oc.closeDBSession();
              	
              	user.setCzy_usunieto(czyUsunietyCheck.isSelected()?1:0);
              	if(!pierwszePole.getText().isEmpty())
              		user.setNazwa(pierwszePole.getText());
              	if(!drugiePole.getText().isEmpty())
              		user.setKontakt(drugiePole.getText());
              	if(!trzeciePole.getText().isEmpty())
              		user.setMiasto(trzeciePole.getText());
              	if(!czwartePole.getText().isEmpty())
              		user.setUlica(czwartePole.getText());
         		//session.update(user);
         		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));
     			//dodać cache
         		List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Producenci");

         		for (int i = 0; i < lista.size(); i++) {
         		    Obiekt_Do_Polecen element = lista.get(i);
         		    Producenci pom = (Producenci) element;

         		    if (pom.getId_producenta() == szukany) {
         		        pom.setNazwa(user.getNazwa());
         		        pom.setKontakt(user.getKontakt());
                  		pom.setMiasto(user.getMiasto());
                      	pom.setUlica(user.getUlica());
                      	pom.setCzy_usunieto(user.getCzy_usunieto());
         		        break;
         		    }
         		}

         		HibernateOracle.cache.put("Producenci", lista);
         		
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
	public void dodajLogikeUsuwania(ButtonEditor br) {
		
		PolaczenieOracle oc = PolaczenieOracle.getInstance();
 		oc.createDBSession();	                			
      	Session session = oc.getDBSession();
 		Producenci pr = (Producenci)session.createQuery("select u from Producenci u where u.id_producenta = :id")
      			.setParameter("id", br.id)
      			.uniqueResult();
 		oc.closeDBSession();
 		
 		List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Producenci");

 		for (int i = 0; i < lista.size(); i++) {
 		    Obiekt_Do_Polecen element = lista.get(i);
 		    Producenci pom = (Producenci) element;
 		    //System.out.println("przed modyfikacją:" + pom.getCzy_usunieto() + "\n");

 		    if (pom.getId_producenta() == pr.getId_producenta()) {
 		        pom.setCzy_usunieto(1);
 		        //System.out.println("po modyfikacji:" + pom.getCzy_usunieto() + "\n");
 		        break;
 		    }
 		}

 		/*for (Obiekt_Do_Polecen element : lista) {
 		    Producenci pom = (Producenci) element;
 		    System.out.println("po wyjściu z pętli:" + pom.getCzy_usunieto() + "\n");
 		}*/

 		HibernateOracle.cache.put("Producenci", lista);
        pr.setCzy_usunieto(1);
          //session.update(pr);
         HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(pr, HibernateOracle.idUzytkownika));

        br.tab.setValueAt("TAK", br.row, 5);
      //oc.closeDBSession();
	}
	
	public void dodajLogikeDodawania(JPanel kontener) {
		
     	JTextField pierwszePole = new JTextField(7);
        JTextField drugiePole = new JTextField(7);
        JTextField trzeciePole = new JTextField(7);
        JTextField czwartePole = new JTextField(7);
		 
         JPanel panel = new JPanel();

    	panel.add(new JLabel("Nazwa: "));
 		panel.add(pierwszePole);
 		panel.add(Box.createHorizontalStrut(5));
 		panel.add(new JLabel("Kontakt: "));
 		panel.add(drugiePole);
 		panel.add(Box.createHorizontalStrut(5));
 		panel.add(new JLabel("Miasto: "));
 		panel.add(trzeciePole);
 		panel.add(Box.createHorizontalStrut(5));
 		panel.add(new JLabel("Ulica: "));
 		panel.add(czwartePole);
 		
 		int wynik = JOptionPane.showConfirmDialog(null, panel, 
                 "Dodaj producenta", JOptionPane.OK_CANCEL_OPTION);
 		 try {
            	if (wynik == JOptionPane.OK_OPTION) {			                					                			                		
	                	if(pierwszePole.getText().isEmpty() || drugiePole.getText().isEmpty() || trzeciePole.getText().isEmpty() || czwartePole.getText().isEmpty())
	                	{
	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Producent nie został dodany");
	                		return;
	                	}
	                	//session.save(new Producenci(pierwszePole.getText(), drugiePole.getText(), trzeciePole.getText(), czwartePole.getText(), 0));
	                	Producenci nowyProducent = new Producenci(pierwszePole.getText(), drugiePole.getText(), trzeciePole.getText(), czwartePole.getText(), 0);
	                	HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Dodaj(nowyProducent, HibernateOracle.idUzytkownika));
	                	List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Producenci");
	                	lista.add(nowyProducent);
	                	HibernateOracle.cache.put("Producenci", lista);
            		
            		
            		//pokazProducentowPrzycisk.doClick();
	                	
	                	Component[] komponenty = kontener.getComponents();
	                	JTable tab = null;
	                	JButton dodajPrzycisk = null;
	                	JButton eksportujDoDruku = null;
	                			
	                	
	                	for(Component komponent : komponenty)
	                	{
	                		if (komponent instanceof JScrollPane) {
	                	        tab = (JTable) (((JScrollPane)komponent).getViewport().getView());
	                	        dodajPrzycisk = (JButton)kontener.getComponent(1);
	                	        eksportujDoDruku = (JButton)kontener.getComponent(2);
	                	        kontener.removeAll();
	                	        break;
	                	    }
	                	}		 	                	
	                	
	                	nowyProducent.setId_producenta(((Producenci)lista.get(lista.size()-2)).getId_producenta()+1);
	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Producenci)nowyProducent).getId_producenta()), ((Producenci)nowyProducent).getNazwa(), ((Producenci)nowyProducent).getKontakt(), ((Producenci)nowyProducent).getMiasto(), ((Producenci)nowyProducent).getUlica(), (((Producenci)nowyProducent).getCzy_usunieto() == 1)?"TAK":"NIE"});

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
 			 JOptionPane.showMessageDialog(null, "Nie udało się dodać producenta. Błąd: " + e.getMessage());
 		 }
    
	}

}
