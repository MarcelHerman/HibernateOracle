package net.codejava;

import java.awt.Component;
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

import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaUzytkownicy implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
		JTextField pierwszyField = new JTextField(7);
		JTextField drugiField = new JTextField(7);
		JTextField trzeciField = new JTextField(7);
		JTextField czwartyField = new JTextField(7);

		JPanel myPanel = new JPanel();

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
			// nazwy[((Stany_Zamowienia)stan).getId_Stanu_Zamowienia()-1] =
			// ((Stany_Zamowienia)stan).getNazwa();
			nazwy[i] = ((Typy_uzytkownika) stan).getNazwa();
			i++;
		}

		JComboBox jombo = new JComboBox(nazwy);
		JCheckBox czyUsunietyCheck = new JCheckBox("Czy usunięty: ");

		myPanel.add(new JLabel("Nazwa użytkownika: "));
		myPanel.add(pierwszyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Login: "));
		myPanel.add(drugiField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Hasło: "));
		myPanel.add(trzeciField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("E-mail: "));
		myPanel.add(czwartyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Typ użytkownika: "));
		myPanel.add(jombo);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(czyUsunietyCheck);

		int result = JOptionPane.showConfirmDialog(null, myPanel, "Edytuj użytkownika", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (result == JOptionPane.OK_OPTION) {

				oc.createDBSession();
				Session session = oc.getDBSession();

				Uzytkownicy user = (Uzytkownicy) session
						.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
						.setParameter("id", bt.id).uniqueResult();
				// System.out.println(user.getId_uzytkownika());
				oc.closeDBSession();

				user.setCzy_usunieto(czyUsunietyCheck.isSelected() ? 1 : 0);
				if (!pierwszyField.getText().isEmpty())
					user.setNazwa_uzytkownika(pierwszyField.getText());
				if (!drugiField.getText().isEmpty())
					user.setLogin(drugiField.getText());
				if (!trzeciField.getText().isEmpty())
					user.setHaslo(trzeciField.getText());
				if (!czwartyField.getText().isEmpty())
					user.setE_mail(czwartyField.getText());
				user.setId_typu_uzytkownika(
						((Typy_uzytkownika) fData.get(jombo.getSelectedIndex())).getId_typu_uzytkownika());

				// session.update(user);
				HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));

				bt.tab.setValueAt(user.getNazwa_uzytkownika(), bt.row, 1);
				bt.tab.setValueAt(user.getLogin(), bt.row, 2);
				bt.tab.setValueAt(user.getHaslo(), bt.row, 3);
				bt.tab.setValueAt(user.getE_mail(), bt.row, 4);
				bt.tab.setValueAt(((Typy_uzytkownika) fData.get(jombo.getSelectedIndex())).getNazwa(), bt.row, 5);
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
		// session.update(pr);
		HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Edytuj(pr, HibernateOracle.idUzytkownika));

		bt.tab.setValueAt("TAK", bt.row, 6);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		JTextField pierwszyField = new JTextField(7);
		JTextField drugiField = new JTextField(7);
		JTextField trzeciField = new JTextField(7);
		JTextField czwartyField = new JTextField(7);
		JPanel myPanel = new JPanel();

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

		JComboBox jombo = new JComboBox(nazwy);

		// user.setId_stanu_zamowienia(((Typy_uzytkownika)fData.get(jombo.getSelectedIndex())).getId_typu_uzytkownika());

		myPanel.add(new JLabel("Nazwa uzytkownika: "));
		myPanel.add(pierwszyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Login: "));
		myPanel.add(drugiField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Hasło: "));
		myPanel.add(trzeciField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("E-mail: "));
		myPanel.add(czwartyField);
		myPanel.add(Box.createHorizontalStrut(5));
		myPanel.add(new JLabel("Id typu użytkownika: "));
		myPanel.add(jombo);

		int result = JOptionPane.showConfirmDialog(null, myPanel, "Dodaj użytkownika", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (result == JOptionPane.OK_OPTION) {
				// oc.createDBSession();
				// Session session = oc.getDBSession();

				if (pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty()
						|| trzeciField.getText().isEmpty() || czwartyField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Użytkownik nie został dodany");
					return;
				}

				// session.save(new Uzytkownicy(pierwszyField.getText(), drugiField.getText(),
				// trzeciField.getText(), czwartyField.getText(),
				// ((Typy_uzytkownika)fData.get(jombo.getSelectedIndex())).getId_typu_uzytkownika(),
				// 0));

				Uzytkownicy nowyUzytkownik = new Uzytkownicy(pierwszyField.getText(), drugiField.getText(),
						trzeciField.getText(), czwartyField.getText(),
						((Typy_uzytkownika) fData.get(jombo.getSelectedIndex())).getId_typu_uzytkownika(), 0);

				HibernateOracle.repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyUzytkownik, HibernateOracle.idUzytkownika));

				// oc.closeDBSession();
				// pokazUzytkownicyPrzycisk.doClick();

				Component[] components = kontener.getComponents();
				JTable tab = null;

				JButton dodajPrzycisk = null;
				JButton eksportujDoDruku = null;
				for (Component component : components) {
					if (component instanceof JScrollPane) {
						tab = (JTable) (((JScrollPane) component).getViewport().getView());
            	        dodajPrzycisk = (JButton)kontener.getComponent(1);
            	        eksportujDoDruku = (JButton)kontener.getComponent(2);
						kontener.removeAll();
						break;
					}
				}

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

				int id = Integer.parseInt(((DefaultTableModel) tab.getModel())
						.getValueAt(((DefaultTableModel) tab.getModel()).getRowCount() - 1, 0).toString());
				nowyUzytkownik.setId_uzytkownika(id + 1);

				for (Obiekt_Do_Polecen entity : cash) {
					Typy_uzytkownika ent = (Typy_uzytkownika) entity;
					if (ent.getId_typu_uzytkownika() == nowyUzytkownik.getId_typu_uzytkownika()) {
						nazwa = ent.getNazwa();
						break;
					}
				}

				((DefaultTableModel) tab.getModel())
						.addRow(new Object[] { Integer.toString(((Uzytkownicy) nowyUzytkownik).getId_uzytkownika()),
								((Uzytkownicy) nowyUzytkownik).getNazwa_uzytkownika(),
								((Uzytkownicy) nowyUzytkownik).getLogin(), ((Uzytkownicy) nowyUzytkownik).getHaslo(),
								((Uzytkownicy) nowyUzytkownik).getE_mail(), nazwa,
								((((Uzytkownicy) nowyUzytkownik).getCzy_usunieto()) == 1) ? "TAK" : "NIE" });

				JScrollPane pane = new JScrollPane(tab);
				kontener.add(pane);
				kontener.add(dodajPrzycisk);
				kontener.add(eksportujDoDruku);
				kontener.repaint();
				kontener.revalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać użytkownika. Błąd: " + e.getMessage());
		}

	}

}
