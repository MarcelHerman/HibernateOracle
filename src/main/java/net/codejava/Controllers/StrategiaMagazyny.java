package net.codejava.Controllers;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliSwing.EdytorPrzycisku;

import java.awt.Component;
import java.util.ArrayList;
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

public class StrategiaMagazyny implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(EdytorPrzycisku edytorPrzycisku) {
		
		dyrektorOkienek.edytowanieMagazyny();
		JPanel okno = dyrektorOkienek.zwrocOkno();

		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj magazyn", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
				bd.stworzSesjeBD();
				Session sesja = bd.pobierzSesjeBD();

				Magazyny rekord = (Magazyny) sesja.createQuery("select u from Magazyny u where u.id_magazynu = :id")
						.setParameter("id", edytorPrzycisku.id).uniqueResult();

				int szukany = rekord.getId_magazynu();

				bd.zamknijSesjeBD();
				if (!pola.get(0).getText().isEmpty())
					rekord.setMiasto(pola.get(0).getText());
				if (!pola.get(1).getText().isEmpty())
					rekord.setUlica(pola.get(1).getText());

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(rekord, HibernateOracle.idUzytkownika));
				List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Magazyny");

				for (int i = 0; i < lista.size(); i++) {
					Obiekt_Do_Polecen element = lista.get(i);
					Magazyny pom = (Magazyny) element;

					if (pom.getId_magazynu() == szukany) {
						pom.setMiasto(rekord.getMiasto());
						pom.setUlica(rekord.getUlica());
						break;
					}
				}

				HibernateOracle.cache.put("Magazyny", lista);

				edytorPrzycisku.tabela.setValueAt(rekord.getMiasto(), edytorPrzycisku.wiersz, 1);
				edytorPrzycisku.tabela.setValueAt(rekord.getUlica(), edytorPrzycisku.wiersz, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować magazynu. Błąd: " + e.getMessage());
		}

	}

	@Override
	public void dodajLogikeUsuwania(EdytorPrzycisku br) {

		Magazyny pr = new Magazyny();
		pr.setId_magazynu(br.id);
		List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Magazyny");
		lista.remove(br.wiersz);
		HibernateOracle.cache.put("Magazyny", lista);
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));

		((DefaultTableModel) br.tabela.getModel()).removeRow(br.wiersz);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		//dyrektorOkienek.stworzOkno(null, TypPola.label, "Miasto: ", TypPola.label, "Ulica: ");
		
		dyrektorOkienek.dodawanieMagazyny();
		JPanel okno = dyrektorOkienek.zwrocOkno();

		int wynik = JOptionPane.showConfirmDialog(null, okno, "Dodaj magazyn", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Magazyn nie został dodany");
					return;
				}
				Magazyny nowyMagazyn = new Magazyny(pola.get(0).getText(), pola.get(1).getText());
				HibernateOracle.repoPolecen
						.dodajPolecenie(new Polecenie_Dodaj(nowyMagazyn, HibernateOracle.idUzytkownika));
				List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Magazyny");
				lista.add(nowyMagazyn);
				HibernateOracle.cache.put("Magazyny", lista);

				Object[] obiekty = pobierzModel(kontener);

				nowyMagazyn.setId_magazynu(((Magazyny) lista.get(lista.size() - 2)).getId_magazynu() + 1);
				((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.addRow(new Object[] { Integer.toString(((Magazyny) nowyMagazyn).getId_magazynu()),
								((Magazyny) nowyMagazyn).getMiasto(), ((Magazyny) nowyMagazyn).getUlica() });

				odswiezModel(kontener, obiekty);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać magazynu. Błąd: " + e.getMessage());
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
