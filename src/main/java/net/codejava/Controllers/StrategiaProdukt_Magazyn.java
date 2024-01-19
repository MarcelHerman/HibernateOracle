package net.codejava.Controllers;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliDruk;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.query.Query;

import net.codejava.HibernateOracle;

public class StrategiaProdukt_Magazyn implements IStrategia {

	public void dodajLogikeEdytowania(ButtonEditor be) {
				
		dyrektorOkienek.edytowanieProdukt_Magazyn();
		JPanel okno = dyrektorOkienek.zwrocOkno();
		
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj produkt w magazynie",
				JOptionPane.OK_CANCEL_OPTION); // ??
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
				bd.stworzSesjeBD();
				Session sesja = bd.pobierzSesjeBD();

				Produkt_Magazyn_Id pr = new Produkt_Magazyn_Id(be.id, be.id2);
				Produkt_Magazyn rekord = (Produkt_Magazyn) sesja
						.createQuery("select u from Produkt_Magazyn u where u.produkt_magazyn_id = :pr")
						.setParameter("pr", pr).uniqueResult();

				bd.zamknijSesjeBD();

				if (!pola.get(0).getText().isEmpty())
					if (Integer.parseInt(pola.get(0).getText()) < 0)
						throw (new Exception("Nie można dodać ujemnego stanu faktycznego."));
					else
						rekord.setStan_faktyczny(Integer.parseInt(pola.get(0).getText()));

				if (!pola.get(1).getText().isEmpty())
					if (Integer.parseInt(pola.get(1).getText()) < 0)
						throw (new Exception("Nie można dodać ujemnego stanu magazynowego."));
					else
						rekord.setStan_magazynowy(Integer.parseInt(pola.get(1).getText()));

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(rekord, HibernateOracle.idUzytkownika));

				be.tab.setValueAt(rekord.getStan_faktyczny(), be.row, 2);
				be.tab.setValueAt(rekord.getStan_magazynowy(), be.row, 3);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
		}

	}

	public void dodajLogikeUsuwania(ButtonEditor be) {
		Produkt_Magazyn pr = new Produkt_Magazyn();
		pr.setProdukt_magazyn_id(new Produkt_Magazyn_Id(be.id, be.id2));
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
		((DefaultTableModel) be.tab.getModel()).removeRow(be.row);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
				
		dyrektorOkienek.dodawanieProdukt_Magazyn();
		JPanel okno = dyrektorOkienek.zwrocOkno();

		int wynik = JOptionPane.showConfirmDialog(null, okno, "Dodaj produkt do magazynu",
				JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty()
						|| pola.get(2).getText().isEmpty() || pola.get(3).getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Nie podano wszystkich danych. Produkt nie został dodany do magazynu");
					return;
				}

				if (Integer.parseInt(pola.get(2).getText()) < 0)
					throw (new Exception("Stan magazynowy nie może być ujemny."));

				if (Integer.parseInt(pola.get(3).getText()) <= 0)
					throw (new Exception("Stan faktyczny nie może być ujemny."));

				Produkt_Magazyn_Id idpm = new Produkt_Magazyn_Id(Integer.parseInt(pola.get(0).getText()),
						Integer.parseInt(pola.get(1).getText()));


				Produkt_Magazyn nowyPM = new Produkt_Magazyn(idpm, Integer.parseInt(pola.get(2).getText()),
						Integer.parseInt(pola.get(3).getText()));
				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Dodaj(nowyPM, HibernateOracle.idUzytkownika));

				Object[] obiekty = pobierzModel(kontener);

				((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.addRow(new Object[] { Integer.toString(((Produkt_Magazyn) nowyPM).getMagazyn_id()),
								Integer.toString(((Produkt_Magazyn) nowyPM).getProdukt_id()),
								Integer.toString(((Produkt_Magazyn) nowyPM).getStan_faktyczny()),
								Integer.toString(((Produkt_Magazyn) nowyPM).getStan_magazynowy()) });

				odswiezModel(kontener, obiekty);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać produktu do magazynu. Błąd: " + e.getMessage());
		}

	}

	public Object[] pobierzModel(JPanel kontener)
	{
		Component[] komponenty = kontener.getComponents();
		Object[] obiekty = new Object[3];

		for (Component komponent : komponenty) {
			if (komponent instanceof JScrollPane) {
				obiekty[0] = (JTable) (((JScrollPane) komponent).getViewport().getView());
				obiekty[1] = (JButton) kontener.getComponent(1);
				obiekty[2] = (JButton) kontener.getComponent(2);
				kontener.removeAll();
				break;
			}
		}
		return obiekty;
	}
	
	public void odswiezModel(JPanel kontener, Object[] obiekty)
	{
		JScrollPane pane = new JScrollPane((JTable)obiekty[0]);
		kontener.add(pane);
		kontener.add((JButton)obiekty[1]);
		kontener.add((JButton)obiekty[2]);
		kontener.repaint();
		kontener.revalidate();
	}

	@Override
	public void dodajLogikeDruku(DyrektorTabel dyrektor) {
    	BudowniczyTabeliDruk budDruk = new BudowniczyTabeliDruk();
    	
    	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

        List<Obiekt_Do_Polecen> obiekty = null;
		bd.stworzSesjeBD();
		
		try (Session sesja2 = bd.pobierzSesjeBD()) {
			
            Query<Obiekt_Do_Polecen> zapytanie = null;
            zapytanie = sesja2.createQuery("FROM Produkt_Magazyn order by produkt_magazyn_id.magazyny_id_magazynu, produkt_magazyn_id.produkty_id_produktu", Obiekt_Do_Polecen.class); //tututki
            obiekty = zapytanie.getResultList();
            bd.zamknijSesjeBD();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		dyrektor.tworzTabeleProdukt_Magazyn(obiekty, budDruk);
         String table = (String)dyrektor.pobierzTabele();
         												 		                 
         String path = "wykaz_produkty_magazyn.txt";
         File plik = new File(path);

        		                     
             try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
            	 pisarz.write(table);		                         
                 JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
             } catch (IOException e) {
                 e.printStackTrace();
                 JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
             }
    
		
	}

}
