package net.codejava;

import java.awt.Component;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
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

public class StrategiaZamowienia implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {

		JTextField pierwszePole = new JTextField(7);
		JTextField drugiePole = new JTextField(7);
		JTextField trzeciePole = new JTextField(7);
		JTextField czwartePole = new JTextField(7);

		JPanel panel = new JPanel();

		PolaczenieOracle oc = PolaczenieOracle.getInstance();
		oc.createDBSession();

		List<Obiekt_Do_Polecen> fData = null;

		try (Session session = oc.getDBSession()) {
			Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Stany_Zamowienia", Obiekt_Do_Polecen.class);
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
			nazwy[i] = ((Stany_Zamowienia) stan).getNazwa();
			i++;
		}

		JComboBox jombo = new JComboBox(nazwy);

		if (HibernateOracle.nazwaTypu.equals("Pracownik")) {
			panel.add(Box.createHorizontalStrut(5));
			panel.add(new JLabel("Stan zamówienia: "));
			panel.add(jombo);
		}

		else {
			panel.add(new JLabel("Miasto wysyłki"));
			panel.add(pierwszePole);
			panel.add(Box.createHorizontalStrut(5));
			panel.add(new JLabel("Ulica wysyłki: "));
			panel.add(drugiePole);
			panel.add(Box.createHorizontalStrut(5));
			panel.add(new JLabel("Stan zamówienia: "));
			panel.add(jombo);
		}

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Edytuj zamówienie", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				oc.createDBSession();
				Session session = oc.getDBSession();

				Zamowienia user = (Zamowienia) session
						.createQuery("select u from Zamowienia u where u.id_zamowienia = :id").setParameter("id", bt.id)
						.uniqueResult();
				// System.out.println(user.getId_uzytkownika());
				oc.closeDBSession();

				if (!pierwszePole.getText().isEmpty())
					user.setAdres_wysylki_miasto(pierwszePole.getText());
				if (!drugiePole.getText().isEmpty())
					user.setAdres_wysylki_ulica(drugiePole.getText());

				user.setId_stanu_zamowienia(
						((Stany_Zamowienia) fData.get(jombo.getSelectedIndex())).getId_Stanu_Zamowienia());

				// session.update(user);
				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));

				bt.tab.setValueAt(user.getAdres_wysylki_miasto(), bt.row, 1);
				bt.tab.setValueAt(user.getAdres_wysylki_ulica(), bt.row, 2);
				bt.tab.setValueAt(((Stany_Zamowienia) fData.get(jombo.getSelectedIndex())).getNazwa(), bt.row, 4);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować zamówienia. Błąd: " + e.getMessage());
		}

	}

	public void dodajLogikeUsuwania(ButtonEditor bt) {
		Zamowienia pr = new Zamowienia();
		pr.setId_zamowienia(bt.id);
		// session.delete(pr);
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));
		// oc.closeDBSession();

		List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Magazyny");
		lista.remove(bt.row);
		HibernateOracle.cache.put("Magazyny", lista);
		((DefaultTableModel) bt.tab.getModel()).removeRow(bt.row);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		JTextField pierwszePole = new JTextField(7);
		JTextField drugiePole = new JTextField(7);
		JTextField trzeciePole = new JTextField(7);
		JTextField czwartePole = new JTextField(7);
		JTextField piatyField = new JTextField(7);
		JPanel panel = new JPanel();

		panel.add(new JLabel("Id uzytkownika: "));
		panel.add(pierwszePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Adres wysyłki miasto: "));
		panel.add(trzeciePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Adres wysyłki ulica: "));
		panel.add(czwartePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Koszt: "));
		panel.add(piatyField);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Dodaj zamówienie", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

//oc.createDBSession();
//Session session = oc.getDBSession();

				if (pierwszePole.getText().isEmpty() || trzeciePole.getText().isEmpty()
						|| czwartePole.getText().isEmpty() || piatyField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Zamówienie nie zostało dodane");
					return;
				}

				double cena = Double.parseDouble(piatyField.getText());
				cena = Math.round(cena * 100.0) / 100.0;
//session.save(new Zamowienia(cena, trzeciePole.getText(), czwartePole.getText(), 1, Integer.parseInt(pierwszePole.getText()), null));
				Zamowienia noweZamowienie = new Zamowienia(cena, trzeciePole.getText(), czwartePole.getText(), 1,
						Integer.parseInt(pierwszePole.getText()), null);
				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Dodaj(noweZamowienie, HibernateOracle.idUzytkownika));

//oc.closeDBSession();
//pokazZamowieniaPrzycisk.doClick();

				Component[] komponenty = kontener.getComponents();
				JTable tab = null;

				JButton dodajPrzycisk = null;
				JButton eksportujDoDruku = null;
				for (Component komponent : komponenty) {
					if (komponent instanceof JScrollPane) {
						tab = (JTable) (((JScrollPane) komponent).getViewport().getView());
            	        dodajPrzycisk = (JButton)kontener.getComponent(1);
            	        eksportujDoDruku = (JButton)kontener.getComponent(2);
						kontener.removeAll();
						break;
					}
				}
				PolaczenieOracle oc = PolaczenieOracle.getInstance();
				if (!HibernateOracle.cache.containsKey("StanyZamowien")) {
					oc.createDBSession();
					try (Session session2 = oc.getDBSession()) {
						Query<Obiekt_Do_Polecen> query = session2.createQuery(
								"FROM Stany_Zamowienia order by id_stanu_zamowienia", Obiekt_Do_Polecen.class);
						HibernateOracle.cache.put("StanyZamowien", query.getResultList());
						oc.closeDBSession();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				List<Obiekt_Do_Polecen> cash = HibernateOracle.cache.get("StanyZamowien");
				String nazwa = "Default";

				int id = Integer.parseInt(((DefaultTableModel) tab.getModel())
						.getValueAt(((DefaultTableModel) tab.getModel()).getRowCount() - 1, 0).toString());
				noweZamowienie.setId_zamowienia(id + 1);

				for (Obiekt_Do_Polecen entities : cash) {
					Stany_Zamowienia ent = (Stany_Zamowienia) entities;

					if (ent.getId_Stanu_Zamowienia() == noweZamowienie.getId_stanu_zamowienia()) {
						nazwa = ent.getNazwa();
					}

				}

				((DefaultTableModel) tab.getModel())
						.addRow(new Object[] { Integer.toString(((Zamowienia) noweZamowienie).getId_zamowienia()),
								((Zamowienia) noweZamowienie).getAdres_wysylki_miasto(),
								((Zamowienia) noweZamowienie).getAdres_wysylki_ulica(),
								Double.toString(((Zamowienia) noweZamowienie).getKoszt()), nazwa,
								Integer.toString(((Zamowienia) noweZamowienie).getUzytkownicy_id_uzytkownika()), null,
								((Zamowienia) noweZamowienie).getOpis() });

				JScrollPane pane = new JScrollPane(tab);
				kontener.add(pane);
				kontener.add(dodajPrzycisk);
				kontener.add(eksportujDoDruku);
				kontener.repaint();
				kontener.revalidate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać zamówienia. Błąd: " + e.getMessage());
		}

	}

}
