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
              	
              	int szukany = user.getId_producenta();
              	oc.closeDBSession();
              	
              	user.setCzy_usunieto(czyUsunietyCheck.isSelected()?1:0);
              	if(!pierwszyField.getText().isEmpty())
              		user.setNazwa(pierwszyField.getText());
              	if(!drugiField.getText().isEmpty())
              		user.setKontakt(drugiField.getText());
              	if(!trzeciField.getText().isEmpty())
              		user.setMiasto(trzeciField.getText());
              	if(!czwartyField.getText().isEmpty())
              		user.setUlica(czwartyField.getText());
         		//session.update(user);
         		HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));
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
         HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(pr, HibernateOracle.idUzytkownika));

        br.tab.setValueAt("TAK", br.row, 5);
      //oc.closeDBSession();
	}
	
	public void dodajLogikeDodawania(JPanel kontener) {
		
     	JTextField pierwszyField = new JTextField(7);
        JTextField drugiField = new JTextField(7);
        JTextField trzeciField = new JTextField(7);
        JTextField czwartyField = new JTextField(7);
		 
         JPanel myPanel = new JPanel();

    	myPanel.add(new JLabel("Nazwa: "));
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
 		
 		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                 "Dodaj producenta", JOptionPane.OK_CANCEL_OPTION);
 		 try {
            	if (result == JOptionPane.OK_OPTION) {			                					                			                		
	                	if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty() || trzeciField.getText().isEmpty() || czwartyField.getText().isEmpty())
	                	{
	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Producent nie został dodany");
	                		return;
	                	}
	                	//session.save(new Producenci(pierwszyField.getText(), drugiField.getText(), trzeciField.getText(), czwartyField.getText(), 0));
	                	Producenci nowyProducent = new Producenci(pierwszyField.getText(), drugiField.getText(), trzeciField.getText(), czwartyField.getText(), 0);
	                	HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyProducent, HibernateOracle.idUzytkownika));
	                	List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Producenci");
	                	lista.add(nowyProducent);
	                	HibernateOracle.cache.put("Producenci", lista);
            		
            		
            		//pokazProducentowPrzycisk.doClick();
	                	
	                	Component[] components = kontener.getComponents();
	                	JTable tab = null;
	                	JButton dodajPrzycisk = null;
	                	JButton eksportujDoDruku = null;
	                			
	                	
	                	for(Component component : components)
	                	{
	                		if (component instanceof JScrollPane) {
	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
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
