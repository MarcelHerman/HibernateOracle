package net.codejava.Views;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

import java.awt.Component;
import java.time.LocalDate;
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

public class StrategiaFaktury implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		dyrektorOkienek.stworzOkno(null, TypPola.label, "NIP: ");
		JPanel okno = dyrektorOkienek.zwrocOkno();
		
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj fakturę", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				PolaczenieOracle oc = PolaczenieOracle.getInstance();
				oc.createDBSession();
				Session session = oc.getDBSession();

				Faktury user = (Faktury) session.createQuery("select u from Faktury u where u.id_faktury = :id")
						.setParameter("id", bt.id).uniqueResult();
				oc.closeDBSession();

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				
				if (!pola.get(0).getText().isEmpty())
					user.setNIP(pola.get(0).getText());
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
		dyrektorOkienek.stworzOkno(null, TypPola.label, "NIP: ", TypPola.label, "Id zamówienia");
		JPanel okno = dyrektorOkienek.zwrocOkno();

		int wynik = JOptionPane.showConfirmDialog(null, okno, "Dodaj fakturę", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Faktura nie została dodana");
					return;
				}

				Faktury nowaFaktura = new Faktury(LocalDate.now(), pola.get(0).getText(),
						Integer.parseInt(pola.get(1).getText()));
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
