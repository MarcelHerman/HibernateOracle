package net.codejava;

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

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaProdukt_Magazyn implements IStrategia {

	public void dodajLogikeEdytowania(ButtonEditor bt) {

		JTextField pierwszyField = new JTextField(7);
		JTextField drugiField = new JTextField(7);
		JTextField trzeciField = new JTextField(7);
		JTextField czwartyField = new JTextField(7);

		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Stan faktyczny: "));
		myPanel.add(pierwszyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Stan magazynowy: "));
		myPanel.add(drugiField);

		int result = JOptionPane.showConfirmDialog(null, myPanel, "Edytuj produkt w magazynie",
				JOptionPane.OK_CANCEL_OPTION); // ??
		try {
			if (result == JOptionPane.OK_OPTION) {

				PolaczenieOracle oc = PolaczenieOracle.getInstance();
				oc.createDBSession();
				Session session = oc.getDBSession();

				Produkt_Magazyn_Id pr = new Produkt_Magazyn_Id(bt.id, bt.id2);
				Produkt_Magazyn user = (Produkt_Magazyn) session
						.createQuery("select u from Produkt_Magazyn u where u.produkt_magazyn_id = :pr")
						.setParameter("pr", pr).uniqueResult();
				// System.out.println(user.getId_uzytkownika());

				oc.closeDBSession();

				if (!pierwszyField.getText().isEmpty())
					if (Integer.parseInt(pierwszyField.getText()) < 0)
						throw (new Exception("Nie można dodać ujemnego stanu faktycznego."));
					else
						user.setStan_faktyczny(Integer.parseInt(pierwszyField.getText()));

				if (!drugiField.getText().isEmpty())
					if (Integer.parseInt(drugiField.getText()) < 0)
						throw (new Exception("Nie można dodać ujemnego stanu magazynowego."));
					else
						user.setStan_magazynowy(Integer.parseInt(drugiField.getText()));

				// session.update(user);
				HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));

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
		// session.delete(pr);
		HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
		// oc.closeDBSession();
		((DefaultTableModel) bt.tab.getModel()).removeRow(bt.row);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		JTextField pierwszyField = new JTextField(7);
		JTextField drugiField = new JTextField(7);
		JTextField trzeciField = new JTextField(7);
		JTextField czwartyField = new JTextField(7);
		JPanel myPanel = new JPanel();

		myPanel.add(new JLabel("Id magazynu: "));
		myPanel.add(pierwszyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Id produktu: "));
		myPanel.add(drugiField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Stan magazynowy: "));
		myPanel.add(trzeciField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Stan faktyczny: "));
		myPanel.add(czwartyField);

		int result = JOptionPane.showConfirmDialog(null, myPanel, "Dodaj produkt do magazynu",
				JOptionPane.OK_CANCEL_OPTION);
		try {
			if (result == JOptionPane.OK_OPTION) {

				if (pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty()
						|| trzeciField.getText().isEmpty() || czwartyField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Nie podano wszystkich danych. Produkt nie został dodany do magazynu");
					return;
				}

				if (Integer.parseInt(trzeciField.getText()) < 0)
					throw (new Exception("Stan magazynowy nie może być ujemny."));

				if (Integer.parseInt(czwartyField.getText()) <= 0)
					throw (new Exception("Stan faktyczny nie może być ujemny."));

				Produkt_Magazyn_Id idpm = new Produkt_Magazyn_Id(Integer.parseInt(pierwszyField.getText()),
						Integer.parseInt(drugiField.getText()));
				// session.save(new Produkt_Magazyn(idpm,
				// Integer.parseInt(trzeciField.getText()),
				// Integer.parseInt(czwartyField.getText())));

				Produkt_Magazyn nowyPM = new Produkt_Magazyn(idpm, Integer.parseInt(trzeciField.getText()),
						Integer.parseInt(czwartyField.getText()));
				HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyPM, HibernateOracle.idUzytkownika));

				// pokazProduktMagazynPrzycisk.doClick();

				Component[] components = kontener.getComponents();
				JTable tab = null;

				JButton dodajPrzycisk = null;
				JButton eksportujDoDruku = null;
				for (Component component : components) {
					if (component instanceof JScrollPane) {
						tab = (JTable) (((JScrollPane) component).getViewport().getView());
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
