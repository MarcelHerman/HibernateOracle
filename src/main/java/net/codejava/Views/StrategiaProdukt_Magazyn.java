package net.codejava.Views;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

import java.awt.Component;
import java.util.ArrayList;

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

import net.codejava.HibernateOracle;
import net.codejava.Controllers.TypPola;

public class StrategiaProdukt_Magazyn implements IStrategia {

	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
		dyrektorOkienek.stworzOkno(null, TypPola.label, "Stan faktyczny: ", TypPola.label, "Stan magazynowy: ");
		JPanel okno = dyrektorOkienek.zwrocOkno();
		
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj produkt w magazynie",
				JOptionPane.OK_CANCEL_OPTION); // ??
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				PolaczenieOracle oc = PolaczenieOracle.getInstance();
				oc.createDBSession();
				Session session = oc.getDBSession();

				Produkt_Magazyn_Id pr = new Produkt_Magazyn_Id(bt.id, bt.id2);
				Produkt_Magazyn user = (Produkt_Magazyn) session
						.createQuery("select u from Produkt_Magazyn u where u.produkt_magazyn_id = :pr")
						.setParameter("pr", pr).uniqueResult();

				oc.closeDBSession();

				if (!pola.get(0).getText().isEmpty())
					if (Integer.parseInt(pola.get(0).getText()) < 0)
						throw (new Exception("Nie można dodać ujemnego stanu faktycznego."));
					else
						user.setStan_faktyczny(Integer.parseInt(pola.get(0).getText()));

				if (!pola.get(1).getText().isEmpty())
					if (Integer.parseInt(pola.get(1).getText()) < 0)
						throw (new Exception("Nie można dodać ujemnego stanu magazynowego."));
					else
						user.setStan_magazynowy(Integer.parseInt(pola.get(1).getText()));

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));

				System.out.println(user.getProdukt_magazyn_id() + " " + user.getStan_faktyczny() + " "
						+ user.getStan_magazynowy());

				bt.tab.setValueAt(user.getStan_faktyczny(), bt.row, 2);
				bt.tab.setValueAt(user.getStan_magazynowy(), bt.row, 3);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
		}

	}

	public void dodajLogikeUsuwania(ButtonEditor bt) {
		Produkt_Magazyn pr = new Produkt_Magazyn();
		pr.setProdukt_magazyn_id(new Produkt_Magazyn_Id(bt.id, bt.id2));
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
		((DefaultTableModel) bt.tab.getModel()).removeRow(bt.row);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		
		dyrektorOkienek.stworzOkno(null, TypPola.label, "Id magazynu: ", TypPola.label, "Id prouktu: ", TypPola.label, "Stan magazynowy: ", TypPola.label, "Stan faktyczny: ");
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

				Component[] komponenty = kontener.getComponents();
				JTable tab = null;

				JButton dodajPrzycisk = null;
				JButton eksportujDoDruku = null;
				for (Component komponent : komponenty) {
					if (komponent instanceof JScrollPane) {
						tab = (JTable) (((JScrollPane) komponent).getViewport().getView());
						dodajPrzycisk = (JButton) kontener.getComponent(1);
						eksportujDoDruku = (JButton) kontener.getComponent(2);
						kontener.removeAll();
						break;
					}
				}

				((DefaultTableModel) tab.getModel())
						.addRow(new Object[] { Integer.toString(((Produkt_Magazyn) nowyPM).getMagazyn_id()),
								Integer.toString(((Produkt_Magazyn) nowyPM).getProdukt_id()),
								Integer.toString(((Produkt_Magazyn) nowyPM).getStan_faktyczny()),
								Integer.toString(((Produkt_Magazyn) nowyPM).getStan_magazynowy()) });

				JScrollPane pane = new JScrollPane(tab);
				kontener.add(pane);
				kontener.add(dodajPrzycisk);
				kontener.add(eksportujDoDruku);
				kontener.repaint();
				kontener.revalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać produktu do magazynu. Błąd: " + e.getMessage());
		}

	}

}
