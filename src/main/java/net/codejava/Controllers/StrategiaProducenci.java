package net.codejava.Controllers;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliSwing.EdytorPrzycisku;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;

import net.codejava.HibernateOracle;

public class StrategiaProducenci implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(EdytorPrzycisku be) {
		
		//dyrektorOkienek.stworzOkno(null, TypPola.label, "Nazwa producenta: ", TypPola.label, "Kontakt: ", TypPola.label, "Miasto: ", TypPola.label, "Ulica: ", TypPola.checkbox, "Czy usunięty: ");
		
		dyrektorOkienek.edytowanieProducenci();
		JPanel okno = dyrektorOkienek.zwrocOkno();

		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj producenta", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
				bd.stworzSesjeBD();
				Session session = bd.pobierzSesjeBD();

				Producenci rekord = (Producenci) session
						.createQuery("select u from Producenci u where u.id_producenta = :id").setParameter("id", be.id)
						.uniqueResult();

				int szukany = rekord.getId_producenta();
				bd.zamknijSesjeBD();

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				
				rekord.setCzy_usunieto(((JCheckBox)okno.getComponent(12)).isSelected() ? 1 : 0);
				if (!pola.get(0).getText().isEmpty())
					rekord.setNazwa(pola.get(0).getText());
				if (!pola.get(1).getText().isEmpty())
					rekord.setKontakt(pola.get(1).getText());
				if (!pola.get(2).getText().isEmpty())
					rekord.setMiasto(pola.get(2).getText());
				if (!pola.get(3).getText().isEmpty())
					rekord.setUlica(pola.get(3).getText());

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(rekord, HibernateOracle.idUzytkownika));
				List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Producenci");

				for (int i = 0; i < lista.size(); i++) {
					Obiekt_Do_Polecen element = lista.get(i);
					Producenci pom = (Producenci) element;

					if (pom.getId_producenta() == szukany) {
						pom.setNazwa(rekord.getNazwa());
						pom.setKontakt(rekord.getKontakt());
						pom.setMiasto(rekord.getMiasto());
						pom.setUlica(rekord.getUlica());
						pom.setCzy_usunieto(rekord.getCzy_usunieto());
						break;
					}
				}

				HibernateOracle.cache.put("Producenci", lista);

				be.tabela.setValueAt(rekord.getNazwa(), be.wiersz, 1);
				be.tabela.setValueAt(rekord.getKontakt(), be.wiersz, 2);
				be.tabela.setValueAt(rekord.getMiasto(), be.wiersz, 3);
				be.tabela.setValueAt(rekord.getUlica(), be.wiersz, 4);
				if (rekord.getCzy_usunieto() == 1)
					be.tabela.setValueAt("TAK", be.wiersz, 5);
				else
					be.tabela.setValueAt("NIE", be.wiersz, 5);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować producenta. Błąd: " + e.getMessage());
		}

	}

	@Override
	public void dodajLogikeUsuwania(EdytorPrzycisku br) {

		PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
		bd.stworzSesjeBD();
		Session session = bd.pobierzSesjeBD();
		Producenci pr = (Producenci) session.createQuery("select u from Producenci u where u.id_producenta = :id")
				.setParameter("id", br.id).uniqueResult();
		bd.zamknijSesjeBD();

		List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Producenci");

		for (int i = 0; i < lista.size(); i++) {
			Obiekt_Do_Polecen element = lista.get(i);
			Producenci pom = (Producenci) element;

			if (pom.getId_producenta() == pr.getId_producenta()) {
				pom.setCzy_usunieto(1);
				break;
			}
		}

		HibernateOracle.cache.put("Producenci", lista);
		pr.setCzy_usunieto(1);
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(pr, HibernateOracle.idUzytkownika));

		br.tabela.setValueAt("TAK", br.wiersz, 5);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		//dyrektorOkienek.stworzOkno(null, TypPola.label, "Nazwa producenta: ", TypPola.label, "Kontakt: ", TypPola.label, "Miasto: ", TypPola.label, "Ulica");
		
		dyrektorOkienek.dodawanieProducenci();
		JPanel okno = dyrektorOkienek.zwrocOkno();
		
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Dodaj producenta", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty()
						|| pola.get(2).getText().isEmpty() || pola.get(3).getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Producent nie został dodany");
					return;
				}

				Producenci nowyProducent = new Producenci(pola.get(0).getText(), pola.get(1).getText(),
						pola.get(2).getText(), pola.get(3).getText(), 0);
				HibernateOracle.repoPolecen
						.dodajPolecenie(new Polecenie_Dodaj(nowyProducent, HibernateOracle.idUzytkownika));
				List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Producenci");
				lista.add(nowyProducent);
				HibernateOracle.cache.put("Producenci", lista);

				Object[] obiekty = pobierzModel(kontener);

				nowyProducent.setId_producenta(((Producenci) lista.get(lista.size() - 2)).getId_producenta() + 1);
				((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.addRow(new Object[] { Integer.toString(((Producenci) nowyProducent).getId_producenta()),
								((Producenci) nowyProducent).getNazwa(), ((Producenci) nowyProducent).getKontakt(),
								((Producenci) nowyProducent).getMiasto(), ((Producenci) nowyProducent).getUlica(),
								(((Producenci) nowyProducent).getCzy_usunieto() == 1) ? "TAK" : "NIE" });

				odswiezModel(kontener, obiekty);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać producenta. Błąd: " + e.getMessage());
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
