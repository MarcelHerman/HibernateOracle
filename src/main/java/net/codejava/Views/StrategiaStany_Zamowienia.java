package net.codejava.Views;

import net.codejava.Models.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;

import net.codejava.HibernateOracle;
import net.codejava.IStrategia;
import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaStany_Zamowienia implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {

		JTextField pierwszePole = new JTextField(7);

		JPanel panel = new JPanel();
		panel.add(new JLabel("Nazwa: "));
		panel.add(pierwszePole);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Edytuj stan zamówienia", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				PolaczenieOracle oc = PolaczenieOracle.getInstance();
				oc.createDBSession();
				Session session = oc.getDBSession();

				Stany_Zamowienia user = (Stany_Zamowienia) session
						.createQuery("select u from Stany_Zamowienia u where u.id_Stanu_Zamowienia = :id")
						.setParameter("id", bt.id).uniqueResult();
				oc.closeDBSession();
				if (!pierwszePole.getText().isEmpty())
					user.setNazwa(pierwszePole.getText());

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));

			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować stanu zamówienia. Błąd: " + e.getMessage());
		}

	}

	public void dodajLogikeUsuwania(ButtonEditor bt) {
		// brak implementacji
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		// brak implementacji
	}

}
