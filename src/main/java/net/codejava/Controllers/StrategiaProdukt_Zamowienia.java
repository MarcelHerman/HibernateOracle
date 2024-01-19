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
import org.hibernate.query.Query;

import net.codejava.HibernateOracle;

public class StrategiaProdukt_Zamowienia implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor be) {
		dyrektorOkienek.edytowanieProdukt_Zamowienia();
	}

	@Override
	public void dodajLogikeUsuwania(ButtonEditor be) {
		Produkt_Zamowienia pr = new Produkt_Zamowienia();
		pr.setProdukt_zamowienia_id(new Produkt_Zamowienia_Id(be.id, be.id2));
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
		((DefaultTableModel) be.tab.getModel()).removeRow(be.row);

	}

	@Override
	public void dodajLogikeDodawania(JPanel kontener) {

		//dyrektorOkienek.stworzOkno(null, TypPola.label, "Id zamowienia: ", TypPola.label, "Id produktu: ", TypPola.label, "Ilość: ");
		
		dyrektorOkienek.dodawanieProdukt_Zamowienia();
		JPanel okno = dyrektorOkienek.zwrocOkno();
		
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Dodaj produkt do zamówienia",
				JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty()
						|| pola.get(2).getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Nie podano wszystkich danych. Produkt nie został dodany do zamówienia.");
					return;
				}

				if (Integer.parseInt(pola.get(2).getText()) <= 0)
					throw (new Exception("Ilość nie może być ujemna ani równa 0."));

				Produkt_Zamowienia_Id idpz = new Produkt_Zamowienia_Id(Integer.parseInt(pola.get(0).getText()),
						Integer.parseInt(pola.get(1).getText()));

				Produkt_Zamowienia nowyPZ = new Produkt_Zamowienia(idpz, Integer.parseInt(pola.get(2).getText()));

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Dodaj(nowyPZ, HibernateOracle.idUzytkownika));

				Object[] obiekty = pobierzModel(kontener);

				((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.addRow(new Object[] { Integer.toString(((Produkt_Zamowienia) nowyPZ).getZamowienieId()),
								Integer.toString(((Produkt_Zamowienia) nowyPZ).getProduktId()),
								Integer.toString(((Produkt_Zamowienia) nowyPZ).getIlosc()) });

				odswiezModel(kontener, obiekty);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać produktu do zamówienia. Błąd: " + e.getMessage());
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
            zapytanie = sesja2.createQuery("FROM Produkt_Zamowienia  order by produkt_zamowienia_id.id_zamowienia, produkt_zamowienia_id.id_produktu", Obiekt_Do_Polecen.class); 
            obiekty = zapytanie.getResultList();
            bd.zamknijSesjeBD();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		dyrektor.tworzTabeleProdukt_Zamowienia(obiekty, budDruk);
         String table = (String)dyrektor.pobierzTabele();
         												 		                 
         String path = "wykaz_produkt_zamowienia.txt";
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
