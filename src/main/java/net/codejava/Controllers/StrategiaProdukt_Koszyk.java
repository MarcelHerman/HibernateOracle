package net.codejava.Controllers;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.codejava.HibernateOracle;

public class StrategiaProdukt_Koszyk implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
		dyrektorOkienek.stworzOkno(null, TypPola.label, "Ilość: ");
		JPanel okno = dyrektorOkienek.zwrocOkno();
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj koszyk", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (!pola.get(0).getText().isEmpty() && Integer.parseInt(pola.get(0).getText()) > 0) {
					for (Obiekt_Do_Polecen pk : HibernateOracle.koszyk) {
						if (((Produkt_Koszyk) pk).getPr().getId_produktu() == bt.id) {
							((Produkt_Koszyk) pk).setIlosc(Integer.parseInt(pola.get(0).getText()));
							bt.tab.setValueAt(Integer.parseInt(pola.get(0).getText()), bt.row, 3);
							bt.tab.setValueAt(
									Integer.parseInt(pola.get(0).getText()) * ((Produkt_Koszyk) pk).getPr().getCena(),
									bt.row, 4);
							break;
						}
					}
				} else
					throw new Exception("Nie podano wartosci lub podano niedopuszczalna liczbe");

			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować wartosci. Błąd: " + e.getMessage());
		}

	}

	@Override
	public void dodajLogikeUsuwania(ButtonEditor br) {
		for (Obiekt_Do_Polecen pk : HibernateOracle.koszyk) {
			if (((Produkt_Koszyk) pk).getPr().getId_produktu() == br.id) {
				HibernateOracle.koszyk.remove(pk);
				break;
			}
		}
		((DefaultTableModel) br.tab.getModel()).removeRow(br.row);
	}

	public void dodajLogikeDodawania(JPanel kontener) {

	}
}
