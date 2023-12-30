package net.codejava;

import java.util.List;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;
import org.hibernate.query.Query;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaZamowienia implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {

     	JTextField pierwszyField = new JTextField(7);
        JTextField drugiField = new JTextField(7);
        JTextField trzeciField = new JTextField(7);
        JTextField czwartyField = new JTextField(7);
		 
         JPanel myPanel = new JPanel();
         
		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
        oc.createDBSession();

        List<Obiekt_Do_Polecen> fData = null;

        try (Session session = oc.getDBSession()) {
            Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Stany_Zamowienia", Obiekt_Do_Polecen.class);
            fData = query.getResultList();
            oc.closeDBSession();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        
        String nazwy[] = new String[fData.size()]; 
        
        int i=0;
        for(Obiekt_Do_Polecen stan: fData) {
        	//nazwy[((Stany_Zamowienia)stan).getId_Stanu_Zamowienia()-1] = ((Stany_Zamowienia)stan).getNazwa();
        	nazwy[i] = ((Stany_Zamowienia)stan).getNazwa();
        	i++;
        }
        
        JComboBox jombo = new JComboBox(nazwy);
		
        if(HibernateOracle.nazwaTypu.equals("Pracownik"))
        {
    		myPanel.add(Box.createHorizontalStrut(5));
    		myPanel.add(new JLabel("Stan zamówienia: "));
    		myPanel.add(jombo);
        }
        
        else
        {
    		myPanel.add(new JLabel("Miasto wysyłki"));
    		myPanel.add(pierwszyField);	    
    		myPanel.add(Box.createHorizontalStrut(5));
    		myPanel.add(new JLabel("Ulica wysyłki: "));
    		myPanel.add(drugiField);
    		myPanel.add(Box.createHorizontalStrut(5));
    		myPanel.add(new JLabel("Stan zamówienia: "));
    		myPanel.add(jombo);
        }


		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Edytuj zamówienie", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (result == JOptionPane.OK_OPTION) {
         		
              	oc.createDBSession();
              	Session session = oc.getDBSession();
         		
              	Zamowienia user = (Zamowienia)session.createQuery("select u from Zamowienia u where u.id_zamowienia = :id")
              			.setParameter("id", bt.id)
              			.uniqueResult();
              	//System.out.println(user.getId_uzytkownika());
              	
              	if(!pierwszyField.getText().isEmpty())
              		user.setAdres_wysylki_miasto(pierwszyField.getText());
              	if(!drugiField.getText().isEmpty())
              		user.setAdres_wysylki_ulica(drugiField.getText());
              	
              	user.setId_stanu_zamowienia(((Stany_Zamowienia)fData.get(jombo.getSelectedIndex())).getId_Stanu_Zamowienia());
              	
         		session.update(user);
         		
         		oc.closeDBSession();
         		
         			bt.tab.setValueAt(user.getAdres_wysylki_miasto(), bt.row, 1); 	                		
         			bt.tab.setValueAt(user.getAdres_wysylki_ulica(), bt.row, 2);
         			bt.tab.setValueAt(((Stany_Zamowienia)fData.get(jombo.getSelectedIndex())).getNazwa(), bt.row, 4);
         	}
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Nie udało się edytować zamówienia. Błąd: " + e.getMessage());
		 }
     
		
	}

	@Override
	public void dodajLogikeUsuwania() {
		// TODO Auto-generated method stub
		
	}

}
