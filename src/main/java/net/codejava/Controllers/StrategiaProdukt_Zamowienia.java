package net.codejava.Controllers;

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

import net.codejava.HibernateOracle;

public class StrategiaProdukt_Zamowienia implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {

	}

	@Override
	public void dodajLogikeUsuwania(ButtonEditor bt) {
		Produkt_Zamowienia pr = new Produkt_Zamowienia();
		pr.setProdukt_zamowienia_id(new Produkt_Zamowienia_Id(bt.id, bt.id2));
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
		((DefaultTableModel) bt.tab.getModel()).removeRow(bt.row);

	}

	@Override
	public void dodajLogikeDodawania(JPanel kontener) {

		dyrektorOkienek.stworzOkno(null, TypPola.label, "Id zamowienia: ", TypPola.label, "Id produktu: ", TypPola.label, "Ilość: ");
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
						.addRow(new Object[] { Integer.toString(((Produkt_Zamowienia) nowyPZ).getZamowienieId()),
								Integer.toString(((Produkt_Zamowienia) nowyPZ).getProduktId()),
								Integer.toString(((Produkt_Zamowienia) nowyPZ).getIlosc()) });

				JScrollPane pane = new JScrollPane(tab);
				kontener.add(pane);
				kontener.add(dodajPrzycisk);
				kontener.add(eksportujDoDruku);
				kontener.repaint();
				kontener.revalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać produktu do zamówienia. Błąd: " + e.getMessage());
		}

	}

}