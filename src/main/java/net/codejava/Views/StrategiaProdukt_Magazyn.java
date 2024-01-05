package net.codejava.Views;

import net.codejava.Models.*;
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

import org.hibernate.Session;

import net.codejava.HibernateOracle;
import net.codejava.IStrategia;
import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaProdukt_Magazyn implements IStrategia {

	public void dodajLogikeEdytowania(ButtonEditor bt) {

		JTextField pierwszePole = new JTextField(7);
		JTextField drugiePole = new JTextField(7);
		JTextField trzeciePole = new JTextField(7);
		JTextField czwartePole = new JTextField(7);

		JPanel panel = new JPanel();
		panel.add(new JLabel("Stan faktyczny: "));
		panel.add(pierwszePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Stan magazynowy: "));
		panel.add(drugiePole);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Edytuj produkt w magazynie",
				JOptionPane.OK_CANCEL_OPTION); // ??
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				PolaczenieOracle oc = PolaczenieOracle.getInstance();
				oc.createDBSession();
				Session session = oc.getDBSession();

				Produkt_Magazyn_Id pr = new Produkt_Magazyn_Id(bt.id, bt.id2);
				Produkt_Magazyn user = (Produkt_Magazyn) session
						.createQuery("select u from Produkt_Magazyn u where u.produkt_magazyn_id = :pr")
						.setParameter("pr", pr).uniqueResult();

				oc.closeDBSession();

				if (!pierwszePole.getText().isEmpty())
					if (Integer.parseInt(pierwszePole.getText()) < 0)
						throw (new Exception("Nie można dodać ujemnego stanu faktycznego."));
					else
						user.setStan_faktyczny(Integer.parseInt(pierwszePole.getText()));

				if (!drugiePole.getText().isEmpty())
					if (Integer.parseInt(drugiePole.getText()) < 0)
						throw (new Exception("Nie można dodać ujemnego stanu magazynowego."));
					else
						user.setStan_magazynowy(Integer.parseInt(drugiePole.getText()));

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
		JTextField pierwszePole = new JTextField(7);
		JTextField drugiePole = new JTextField(7);
		JTextField trzeciePole = new JTextField(7);
		JTextField czwartePole = new JTextField(7);
		JPanel panel = new JPanel();

		panel.add(new JLabel("Id magazynu: "));
		panel.add(pierwszePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Id produktu: "));
		panel.add(drugiePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Stan magazynowy: "));
		panel.add(trzeciePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Stan faktyczny: "));
		panel.add(czwartePole);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Dodaj produkt do magazynu",
				JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				if (pierwszePole.getText().isEmpty() || drugiePole.getText().isEmpty()
						|| trzeciePole.getText().isEmpty() || czwartePole.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Nie podano wszystkich danych. Produkt nie został dodany do magazynu");
					return;
				}

				if (Integer.parseInt(trzeciePole.getText()) < 0)
					throw (new Exception("Stan magazynowy nie może być ujemny."));

				if (Integer.parseInt(czwartePole.getText()) <= 0)
					throw (new Exception("Stan faktyczny nie może być ujemny."));

				Produkt_Magazyn_Id idpm = new Produkt_Magazyn_Id(Integer.parseInt(pierwszePole.getText()),
						Integer.parseInt(drugiePole.getText()));


				Produkt_Magazyn nowyPM = new Produkt_Magazyn(idpm, Integer.parseInt(trzeciePole.getText()),
						Integer.parseInt(czwartePole.getText()));
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
