package net.codejava.Views;

import net.codejava.Models.*;
import java.awt.Component;
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

import net.codejava.HibernateOracle;
import net.codejava.IStrategia;
import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaMagazyny implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {

		JTextField pierwszePole = new JTextField(7);
		JTextField drugiePole = new JTextField(7);

		JPanel panel = new JPanel();

		panel.add(new JLabel("Miasto: "));
		panel.add(pierwszePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Ulica: "));
		panel.add(drugiePole);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Edytuj magazyn", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				PolaczenieOracle oc = PolaczenieOracle.getInstance();
				oc.createDBSession();
				Session session = oc.getDBSession();

				Magazyny user = (Magazyny) session.createQuery("select u from Magazyny u where u.id_magazynu = :id")
						.setParameter("id", bt.id).uniqueResult();

				int szukany = user.getId_magazynu();

				oc.closeDBSession();
				if (!pierwszePole.getText().isEmpty())
					user.setMiasto(pierwszePole.getText());
				if (!drugiePole.getText().isEmpty())
					user.setUlica(drugiePole.getText());

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));
				List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Magazyny");

				for (int i = 0; i < lista.size(); i++) {
					Obiekt_Do_Polecen element = lista.get(i);
					Magazyny pom = (Magazyny) element;

					if (pom.getId_magazynu() == szukany) {
						pom.setMiasto(user.getMiasto());
						pom.setUlica(user.getUlica());
						break;
					}
				}

				HibernateOracle.cache.put("Magazyny", lista);

				bt.tab.setValueAt(user.getMiasto(), bt.row, 1);
				bt.tab.setValueAt(user.getUlica(), bt.row, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować magazynu. Błąd: " + e.getMessage());
		}

	}

	@Override
	public void dodajLogikeUsuwania(ButtonEditor br) {

		Magazyny pr = new Magazyny();
		pr.setId_magazynu(br.id);
		List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Magazyny");
		lista.remove(br.row);
		HibernateOracle.cache.put("Magazyny", lista);
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));

		((DefaultTableModel) br.tab.getModel()).removeRow(br.row);
	}

	public void dodajLogikeDodawania(JPanel kontener) {

		JTextField pierwszePole = new JTextField(7);
		JTextField drugiePole = new JTextField(7);
		JTextField trzeciePole = new JTextField(7);
		JTextField czwartePole = new JTextField(7);

		JPanel panel = new JPanel();

		panel.add(new JLabel("Miasto: "));
		panel.add(pierwszePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Ulica: "));
		panel.add(drugiePole);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Dodaj magazyn", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				if (pierwszePole.getText().isEmpty() || drugiePole.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Magazyn nie został dodany");
					return;
				}
				Magazyny nowyMagazyn = new Magazyny(pierwszePole.getText(), drugiePole.getText());
				HibernateOracle.repoPolecen
						.dodajPolecenie(new Polecenie_Dodaj(nowyMagazyn, HibernateOracle.idUzytkownika));
				List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Magazyny");
				lista.add(nowyMagazyn);
				HibernateOracle.cache.put("Magazyny", lista);

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

				nowyMagazyn.setId_magazynu(((Magazyny) lista.get(lista.size() - 2)).getId_magazynu() + 1);
				((DefaultTableModel) tab.getModel())
						.addRow(new Object[] { Integer.toString(((Magazyny) nowyMagazyn).getId_magazynu()),
								((Magazyny) nowyMagazyn).getMiasto(), ((Magazyny) nowyMagazyn).getUlica() });

				JScrollPane pane = new JScrollPane(tab);
				kontener.add(pane);
				kontener.add(dodajPrzycisk);
				kontener.add(eksportujDoDruku);
				kontener.repaint();
				kontener.revalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać magazynu. Błąd: " + e.getMessage());
		}

	}

}
