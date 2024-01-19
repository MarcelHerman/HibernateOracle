package net.codejava.Controllers;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.query.Query;

import net.codejava.HibernateOracle;

public class StrategiaUzytkownicy implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		PolaczenieOracle oc = PolaczenieOracle.getInstance();
		oc.createDBSession();
		List<Obiekt_Do_Polecen> fData = null;
		try (Session session = oc.getDBSession()) {
			Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Typy_uzytkownika", Obiekt_Do_Polecen.class);
			fData = query.getResultList();
			oc.closeDBSession();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		String nazwy[] = new String[fData.size()];
		
		int i = 0;
		for (Obiekt_Do_Polecen stan : fData) {
			nazwy[i] = ((Typy_uzytkownika) stan).getNazwa();
			i++;
		}
		
		//dyrektorOkienek.stworzOkno(new String[][] {nazwy}, TypPola.label, "Nazwa użytkownika: ", TypPola.label, "Login: ", TypPola.label, "Hasło: ", TypPola.label, "E-mail: ", TypPola.combobox, 1, TypPola.checkbox, "Czy usunięty: ");
		
		dyrektorOkienek.edytowanieUzytkownicy(nazwy);
		JPanel okno = dyrektorOkienek.zwrocOkno();
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj użytkownika", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				oc.createDBSession();
				Session session = oc.getDBSession();

				Uzytkownicy user = (Uzytkownicy) session
						.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
						.setParameter("id", bt.id).uniqueResult();
				oc.closeDBSession();

				user.setCzy_usunieto(((JCheckBox)okno.getComponent(14)).isSelected() ? 1 : 0);
				if (!pola.get(0).getText().isEmpty())
					user.setNazwa_uzytkownika(pola.get(0).getText());
				if (!pola.get(1).getText().isEmpty())
					user.setLogin(pola.get(1).getText());
				if (!pola.get(2).getText().isEmpty())
					user.setHaslo(pola.get(2).getText());
				if (!pola.get(3).getText().isEmpty())
					user.setE_mail(pola.get(3).getText());
				user.setId_typu_uzytkownika(
						((Typy_uzytkownika) fData.get(((JComboBox)okno.getComponent(12)).getSelectedIndex())).getId_typu_uzytkownika());

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));

				bt.tab.setValueAt(user.getNazwa_uzytkownika(), bt.row, 1);
				bt.tab.setValueAt(user.getLogin(), bt.row, 2);
				bt.tab.setValueAt(user.getHaslo(), bt.row, 3);
				bt.tab.setValueAt(user.getE_mail(), bt.row, 4);
				bt.tab.setValueAt(((Typy_uzytkownika) fData.get(((JComboBox)okno.getComponent(12)).getSelectedIndex())).getNazwa(), bt.row, 5);
				if (user.getCzy_usunieto() == 1)
					bt.tab.setValueAt("TAK", bt.row, 6);
				else
					bt.tab.setValueAt("NIE", bt.row, 6);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
		}

	}

	public void dodajLogikeUsuwania(ButtonEditor bt) {
		PolaczenieOracle oc = PolaczenieOracle.getInstance();
		oc.createDBSession();
		Session session = oc.getDBSession();
		Uzytkownicy pr = (Uzytkownicy) session.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
				.setParameter("id", bt.id).uniqueResult();
		oc.closeDBSession();

		pr.setCzy_usunieto(1);
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(pr, HibernateOracle.idUzytkownika));

		bt.tab.setValueAt("TAK", bt.row, 6);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		PolaczenieOracle oc = PolaczenieOracle.getInstance();
		oc.createDBSession();
		List<Obiekt_Do_Polecen> fData = null;
		try (Session session = oc.getDBSession()) {
			Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Typy_uzytkownika", Obiekt_Do_Polecen.class);
			fData = query.getResultList();
			oc.closeDBSession();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		String nazwy[] = new String[fData.size()];
		
		int i = 0;
		for (Obiekt_Do_Polecen stan : fData) {
			nazwy[i] = ((Typy_uzytkownika) stan).getNazwa();
			i++;
		}
		
		//dyrektorOkienek.stworzOkno(new String[][] {nazwy}, TypPola.label, "Nazwa użytkownika: ", TypPola.label, "Login: ", TypPola.label, "Hasło: ", TypPola.label, "E-mail: ", TypPola.combobox, 1);
		
		dyrektorOkienek.dodawanieUzytkownicy(nazwy);
		JPanel okno = dyrektorOkienek.zwrocOkno();
		
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Dodaj użytkownika", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty()
						|| pola.get(2).getText().isEmpty() || pola.get(3).getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Użytkownik nie został dodany");
					return;
				}

				Uzytkownicy nowyUzytkownik = new Uzytkownicy(pola.get(0).getText(), pola.get(1).getText(),
						pola.get(2).getText(), pola.get(3).getText(),
						((Typy_uzytkownika) fData.get(((JComboBox)okno.getComponent(12)).getSelectedIndex())).getId_typu_uzytkownika(), 0);

				HibernateOracle.repoPolecen
						.dodajPolecenie(new Polecenie_Dodaj(nowyUzytkownik, HibernateOracle.idUzytkownika));

				Object[] obiekty = pobierzModel(kontener);

				if (!HibernateOracle.cache.containsKey("TypyUzytkownika")) {
					oc.createDBSession();
					try (Session session2 = oc.getDBSession()) {
						Query<Obiekt_Do_Polecen> query = session2.createQuery(
								"FROM Typy_uzytkownika order by id_typu_uzytkownika", Obiekt_Do_Polecen.class);
						HibernateOracle.cache.put("TypyUzytkownika", query.getResultList());
						oc.closeDBSession();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Obiekt_Do_Polecen> cash = HibernateOracle.cache.get("TypyUzytkownika");
				String nazwa = "Default";

				int id = Integer.parseInt(((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.getValueAt(((DefaultTableModel) ((JTable)obiekty[0]).getModel()).getRowCount() - 1, 0).toString());
				nowyUzytkownik.setId_uzytkownika(id + 1);

				for (Obiekt_Do_Polecen entity : cash) {
					Typy_uzytkownika ent = (Typy_uzytkownika) entity;
					if (ent.getId_typu_uzytkownika() == nowyUzytkownik.getId_typu_uzytkownika()) {
						nazwa = ent.getNazwa();
						break;
					}
				}

				((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.addRow(new Object[] { Integer.toString(((Uzytkownicy) nowyUzytkownik).getId_uzytkownika()),
								((Uzytkownicy) nowyUzytkownik).getNazwa_uzytkownika(),
								((Uzytkownicy) nowyUzytkownik).getLogin(), ((Uzytkownicy) nowyUzytkownik).getHaslo(),
								((Uzytkownicy) nowyUzytkownik).getE_mail(), nazwa,
								((((Uzytkownicy) nowyUzytkownik).getCzy_usunieto()) == 1) ? "TAK" : "NIE" });

				odswiezModel(kontener, obiekty);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać użytkownika. Błąd: " + e.getMessage());
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
