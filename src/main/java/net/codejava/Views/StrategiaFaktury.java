package net.codejava.Views;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

import java.awt.Component;
import java.time.LocalDate;

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

public class StrategiaFaktury implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {

		JTextField pierwszePole = new JTextField(7);

		JPanel panel = new JPanel();

		panel.add(new JLabel("NIP: "));
		panel.add(pierwszePole);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Edytuj fakturę", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				PolaczenieOracle oc = PolaczenieOracle.getInstance();
				oc.createDBSession();
				Session session = oc.getDBSession();

				Faktury user = (Faktury) session.createQuery("select u from Faktury u where u.id_faktury = :id")
						.setParameter("id", bt.id).uniqueResult();
				oc.closeDBSession();

				if (!pierwszePole.getText().isEmpty())
					user.setNIP(pierwszePole.getText());
				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));

				bt.tab.setValueAt(user.getNIP(), bt.row, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować faktury. Błąd: " + e.getMessage());
		}

	}

	@Override
	public void dodajLogikeUsuwania(ButtonEditor br) {

		Faktury pr = new Faktury();
		pr.setId_faktury(br.id);
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
		((DefaultTableModel) br.tab.getModel()).removeRow(br.row);

	}

	public void dodajLogikeDodawania(JPanel kontener) {

		JTextField pierwszePole = new JTextField(7);
		JTextField drugiePole = new JTextField(7);

		JPanel panel = new JPanel();

		panel.add(new JLabel("NIP: "));
		panel.add(pierwszePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Id zamówienia: "));
		panel.add(drugiePole);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Dodaj fakturę", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				if (pierwszePole.getText().isEmpty() || drugiePole.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Faktura nie została dodana");
					return;
				}

				Faktury nowaFaktura = new Faktury(LocalDate.now(), pierwszePole.getText(),
						Integer.parseInt(drugiePole.getText()));
				HibernateOracle.repoPolecen
						.dodajPolecenie(new Polecenie_Dodaj(nowaFaktura, HibernateOracle.idUzytkownika));

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

				int id = Integer.parseInt(((DefaultTableModel) tab.getModel())
						.getValueAt(((DefaultTableModel) tab.getModel()).getRowCount() - 1, 0).toString()) + 1;

				nowaFaktura.setId_faktury(id);

				((DefaultTableModel) tab.getModel())
						.addRow(new Object[] { Integer.toString(((Faktury) nowaFaktura).getId_faktury()),
								((Faktury) nowaFaktura).getData_wystawienia(), ((Faktury) nowaFaktura).getNIP(),
								Integer.toString(((Faktury) nowaFaktura).getZamowienia_id_zamowienia()) });

				JScrollPane pane = new JScrollPane(tab);
				kontener.add(pane);
				kontener.add(dodajPrzycisk);
				kontener.add(eksportujDoDruku);
				kontener.repaint();
				kontener.revalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać faktury. Błąd: " + e.getMessage());
		}

	}

}