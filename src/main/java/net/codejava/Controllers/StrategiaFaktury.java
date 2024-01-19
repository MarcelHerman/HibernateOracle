package net.codejava.Controllers;

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

public class StrategiaFaktury implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		dyrektorOkienek.edytowanieFaktury();
		JPanel okno = dyrektorOkienek.zwrocOkno();
		
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj fakturę", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				PolaczenieOracle oc = PolaczenieOracle.getInstance();
				oc.stworzSesjeBD();
				Session session = oc.pobierzSesjeBD();

				Faktury user = (Faktury) session.createQuery("select u from Faktury u where u.id_faktury = :id")
						.setParameter("id", bt.id).uniqueResult();
				oc.zamknijSesjeBD();

							
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (pola.get(0).getText().isEmpty())return;
				
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
		dyrektorOkienek.dodawanieFaktury();
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

				Object[] obiekty = pobierzModel(kontener);

				int id = Integer.parseInt(((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.getValueAt(((DefaultTableModel) ((JTable)obiekty[0]).getModel()).getRowCount() - 1, 0).toString()) + 1;

				nowaFaktura.setId_faktury(id);

				((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.addRow(new Object[] { Integer.toString(((Faktury) nowaFaktura).getId_faktury()),
								((Faktury) nowaFaktura).getData_wystawienia(), ((Faktury) nowaFaktura).getNIP(),
								Integer.toString(((Faktury) nowaFaktura).getZamowienia_id_zamowienia()) });

				odswiezModel(kontener, obiekty);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać faktury. Błąd: " + e.getMessage());
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

}
