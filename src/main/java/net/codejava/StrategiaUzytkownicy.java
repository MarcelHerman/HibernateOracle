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

public class StrategiaUzytkownicy implements IStrategia{

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
            Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Typy_uzytkownika", Obiekt_Do_Polecen.class);
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
        	nazwy[i] = ((Typy_uzytkownika)stan).getNazwa();
        	i++;
        }
        
        JComboBox jombo = new JComboBox(nazwy);
        JCheckBox czyUsunietyCheck = new JCheckBox("Czy usunięty: ");
        
		myPanel.add(new JLabel("Nazwa użytkownika: "));
		myPanel.add(pierwszyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Login: "));
		myPanel.add(drugiField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Hasło: "));
		myPanel.add(trzeciField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("E-mail: "));
		myPanel.add(czwartyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Typ użytkownika: "));
		myPanel.add(jombo);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(czyUsunietyCheck);
		
		int result = JOptionPane.showConfirmDialog(null, myPanel, 
                "Edytuj użytkownika", JOptionPane.OK_CANCEL_OPTION);
		 try {
         	if (result == JOptionPane.OK_OPTION) {
         		
              	oc.createDBSession();
              	Session session = oc.getDBSession();
         		
              	Uzytkownicy user = (Uzytkownicy)session.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
              			.setParameter("id", bt.id)
              			.uniqueResult();
              	//System.out.println(user.getId_uzytkownika());
              	user.setCzy_usunieto(czyUsunietyCheck.isSelected()?1:0);
              	if(!pierwszyField.getText().isEmpty())
              		user.setNazwa_uzytkownika(pierwszyField.getText());
              	if(!drugiField.getText().isEmpty())
              		user.setLogin(drugiField.getText());
              	if(!trzeciField.getText().isEmpty())
              		user.setHaslo(trzeciField.getText());
              	if(!czwartyField.getText().isEmpty())
              		user.setE_mail(czwartyField.getText());
              	user.setId_typu_uzytkownika(((Typy_uzytkownika)fData.get(jombo.getSelectedIndex())).getId_typu_uzytkownika());
              	
         		session.update(user);
         		
         		oc.closeDBSession();
         		
         		bt.tab.setValueAt(user.getNazwa_uzytkownika(), bt.row, 1);
         		bt.tab.setValueAt(user.getLogin(), bt.row, 2);
         		bt.tab.setValueAt(user.getHaslo(), bt.row, 3);
         		bt.tab.setValueAt(user.getE_mail(), bt.row, 4);
         		bt.tab.setValueAt(((Typy_uzytkownika)fData.get(jombo.getSelectedIndex())).getNazwa(), bt.row, 5);
         		if(user.getCzy_usunieto() == 1)
         			bt.tab.setValueAt("TAK", bt.row, 6);
         		else   	                		
         			bt.tab.setValueAt("NIE", bt.row, 6);
         	}
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
		 }
		
	}

	@Override
	public void dodajLogikeUsuwania() {
		// TODO Auto-generated method stub
		
	}

}
