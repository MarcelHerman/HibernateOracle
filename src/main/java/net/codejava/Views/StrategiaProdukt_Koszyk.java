package net.codejava.Views;

import net.codejava.Models.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.codejava.HibernateOracle;
import net.codejava.IStrategia;
import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaProdukt_Koszyk implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {

		JTextField pierwszePole = new JTextField(7);

		JPanel panel = new JPanel();
		panel.add(new JLabel("Ilosc: "));
		panel.add(pierwszePole);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Edytuj koszyk", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				if (!pierwszePole.getText().isEmpty() && Integer.parseInt(pierwszePole.getText()) > 0) {
					for (Obiekt_Do_Polecen pk : HibernateOracle.koszyk) {
						if (((Produkt_Koszyk) pk).getPr().getId_produktu() == bt.id) {
							((Produkt_Koszyk) pk).setIlosc(Integer.parseInt(pierwszePole.getText()));
							bt.tab.setValueAt(Integer.parseInt(pierwszePole.getText()), bt.row, 3);
							bt.tab.setValueAt(
									Integer.parseInt(pierwszePole.getText()) * ((Produkt_Koszyk) pk).getPr().getCena(),
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
