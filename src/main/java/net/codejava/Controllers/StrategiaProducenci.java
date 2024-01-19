package net.codejava.Controllers;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

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
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		
		//dyrektorOkienek.stworzOkno(null, TypPola.label, "Nazwa producenta: ", TypPola.label, "Kontakt: ", TypPola.label, "Miasto: ", TypPola.label, "Ulica: ", TypPola.checkbox, "Czy usunięty: ");
		
		dyrektorOkienek.edytowanieProducenci();
		JPanel okno = dyrektorOkienek.zwrocOkno();

		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj producenta", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				PolaczenieOracle oc = PolaczenieOracle.getInstance();
				oc.stworzSesjeBD();
				Session session = oc.pobierzSesjeBD();

				Producenci user = (Producenci) session
						.createQuery("select u from Producenci u where u.id_producenta = :id").setParameter("id", bt.id)
						.uniqueResult();

				int szukany = user.getId_producenta();
				oc.zamknijSesjeBD();

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				
				user.setCzy_usunieto(((JCheckBox)okno.getComponent(12)).isSelected() ? 1 : 0);
				if (!pola.get(0).getText().isEmpty())
					user.setNazwa(pola.get(0).getText());
				if (!pola.get(1).getText().isEmpty())
					user.setKontakt(pola.get(1).getText());
				if (!pola.get(2).getText().isEmpty())
					user.setMiasto(pola.get(2).getText());
				if (!pola.get(3).getText().isEmpty())
					user.setUlica(pola.get(3).getText());

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));
				List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Producenci");

				for (int i = 0; i < lista.size(); i++) {
					Obiekt_Do_Polecen element = lista.get(i);
					Producenci pom = (Producenci) element;

					if (pom.getId_producenta() == szukany) {
						pom.setNazwa(user.getNazwa());
						pom.setKontakt(user.getKontakt());
						pom.setMiasto(user.getMiasto());
						pom.setUlica(user.getUlica());
						pom.setCzy_usunieto(user.getCzy_usunieto());
						break;
					}
				}

				HibernateOracle.cache.put("Producenci", lista);

				bt.tab.setValueAt(user.getNazwa(), bt.row, 1);
				bt.tab.setValueAt(user.getKontakt(), bt.row, 2);
				bt.tab.setValueAt(user.getMiasto(), bt.row, 3);
				bt.tab.setValueAt(user.getUlica(), bt.row, 4);
				if (user.getCzy_usunieto() == 1)
					bt.tab.setValueAt("TAK", bt.row, 5);
				else
					bt.tab.setValueAt("NIE", bt.row, 5);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować producenta. Błąd: " + e.getMessage());
		}

	}

	@Override
	public void dodajLogikeUsuwania(ButtonEditor br) {

		PolaczenieOracle oc = PolaczenieOracle.getInstance();
		oc.stworzSesjeBD();
		Session session = oc.pobierzSesjeBD();
		Producenci pr = (Producenci) session.createQuery("select u from Producenci u where u.id_producenta = :id")
				.setParameter("id", br.id).uniqueResult();
		oc.zamknijSesjeBD();

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

		br.tab.setValueAt("TAK", br.row, 5);
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
