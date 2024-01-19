package net.codejava.Controllers;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliSwing.EdytorPrzycisku;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.codejava.HibernateOracle;

public class StrategiaProdukt_Koszyk implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(EdytorPrzycisku edytorPrzycisku) {
		
		//dyrektorOkienek.stworzOkno(null, TypPola.label, "Ilość: ");
		
		dyrektorOkienek.edytowaniePodukt_Koszyk();
		JPanel okno = dyrektorOkienek.zwrocOkno();
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj koszyk", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (!pola.get(0).getText().isEmpty() && Integer.parseInt(pola.get(0).getText()) > 0) {
					for (Obiekt_Do_Polecen pk : HibernateOracle.koszyk) {
						if (((Produkt_Koszyk) pk).getPr().getId_produktu() == edytorPrzycisku.id) {
							((Produkt_Koszyk) pk).setIlosc(Integer.parseInt(pola.get(0).getText()));
							edytorPrzycisku.tabela.setValueAt(Integer.parseInt(pola.get(0).getText()), edytorPrzycisku.wiersz, 3);
							edytorPrzycisku.tabela.setValueAt(
									Integer.parseInt(pola.get(0).getText()) * ((Produkt_Koszyk) pk).getPr().getCena(),
									edytorPrzycisku.wiersz, 4);
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
	public void dodajLogikeUsuwania(EdytorPrzycisku br) {
		for (Obiekt_Do_Polecen pk : HibernateOracle.koszyk) {
			if (((Produkt_Koszyk) pk).getPr().getId_produktu() == br.id) {
				HibernateOracle.koszyk.remove(pk);
				break;
			}
		}
		((DefaultTableModel) br.tabela.getModel()).removeRow(br.wiersz);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		dyrektorOkienek.dodawaniePodukt_Koszyk();
	}

	@Override
	public Object[] pobierzModel(JPanel kontener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void odswiezModel(JPanel kontener, Object[] obiekty) {
		// TODO Auto-generated method stub
		
	}
}
