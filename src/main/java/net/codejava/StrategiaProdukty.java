package net.codejava;

import java.util.List;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;
import org.hibernate.query.Query;

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaProdukty implements IsmiesznyWzorzec {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
     	JTextField pierwszyField = new JTextField(7);
        JTextField drugiField = new JTextField(7);
        JTextField trzeciField = new JTextField(7);
        JTextField czwartyField = new JTextField(7);
		 
         JPanel myPanel = new JPanel();

		OracleConnection oc =  OracleConnection.getInstance();
        oc.createDBSession();

        List<Obiekt_Do_Polecen> fData = null;

        try (Session session = oc.getDBSession()) {
            Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Kategorie", Obiekt_Do_Polecen.class);
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
        	nazwy[i] = ((Kategorie)stan).getNazwa();
        	i++;
        }
        
        JComboBox jombo = new JComboBox(nazwy);
        JCheckBox czyUsunietyCheck = new JCheckBox("Czy usunięty: ");
        
		myPanel.add(new JLabel("Nazwa produktu: "));
		myPanel.add(pierwszyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Cena: "));
		myPanel.add(drugiField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Opis: "));
		myPanel.add(trzeciField);
		myPanel.add(new JLabel("Kategoria: "));
		myPanel.add(jombo);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(czyUsunietyCheck);

		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Edytuj produkt", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (result == JOptionPane.OK_OPTION) {
         		
              	oc.createDBSession();
              	Session session = oc.getDBSession();
         		
              	Produkty user = (Produkty)session.createQuery("select u from Produkty u where u.id_produktu = :id")
              			.setParameter("id", bt.id)
              			.uniqueResult();
              	//System.out.println(user.getId_uzytkownika());
              	user.setCzy_usunieto(czyUsunietyCheck.isSelected()?1:0);
              	if(!pierwszyField.getText().isEmpty())
              		user.setNazwa(pierwszyField.getText());
              	if(!drugiField.getText().isEmpty())
              		if(Double.parseDouble(drugiField.getText())<=0)
              			throw(new Exception("Nie można dodać ujemnej ceny, ani ceny równej 0"));
              		else
              			user.setCena(Double.parseDouble(drugiField.getText()));
              	if(!trzeciField.getText().isEmpty())
              		user.setOpis(trzeciField.getText());
              	if(!czwartyField.getText().isEmpty())
              		user.setKategorie_id_kategorii(Integer.parseInt(czwartyField.getText()));
              	
              	user.setKategorie_id_kategorii(((Kategorie)fData.get(jombo.getSelectedIndex())).getId_Kategorii());
         		session.update(user);
         		
         		oc.closeDBSession();
         		
         		bt.tab.setValueAt(user.getNazwa(), bt.row, 1);
         		bt.tab.setValueAt(user.getCena(), bt.row, 2);
         		bt.tab.setValueAt(user.getOpis(), bt.row, 3);
         		bt.tab.setValueAt(((Kategorie)fData.get(jombo.getSelectedIndex())).getNazwa() ,bt.row, 5);
         		if(user.getCzy_usunieto() == 1)
         			bt.tab.setValueAt("TAK", bt.row, 6);
         		else   	                		
         			bt.tab.setValueAt("NIE", bt.row, 6);
         	}
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Nie udało się edytować produktu. Błąd: " + e.getMessage());
		 }
     
		
	}

	@Override
	public void dodajLogikeUsuwania() {
		// TODO Auto-generated method stub
		
	}

}
