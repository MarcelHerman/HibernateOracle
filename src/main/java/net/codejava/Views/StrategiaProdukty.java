package net.codejava.Views;

import net.codejava.Models.*;
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

import net.codejava.HibernateOracle;
import net.codejava.IStrategia;
import net.codejava.BudowniczyTabeliSwing.ButtonEditor;

public class StrategiaProdukty implements IStrategia {

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
			Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Kategorie", Obiekt_Do_Polecen.class);
			fData = query.getResultList();
			oc.closeDBSession();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

		String nazwy[] = new String[fData.size()];

		int i = 0;
		for (Obiekt_Do_Polecen stan : fData) {

			nazwy[i] = ((Kategorie) stan).getNazwa();
			i++;
		}

		JComboBox jombo = new JComboBox(nazwy);
		JCheckBox czyUsunietyCheck = new JCheckBox("Czy usunięty: ");

		panel.add(new JLabel("Nazwa produktu: "));
		panel.add(pierwszePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Cena: "));
		panel.add(drugiePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Opis: "));
		panel.add(trzeciePole);
		panel.add(new JLabel("Kategoria: "));
		panel.add(jombo);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(czyUsunietyCheck);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Edytuj produkt", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				oc.createDBSession();
				Session session = oc.getDBSession();

				Produkty user = (Produkty) session.createQuery("select u from Produkty u where u.id_produktu = :id")
						.setParameter("id", bt.id).uniqueResult();

				oc.closeDBSession();

				user.setCzy_usunieto(czyUsunietyCheck.isSelected() ? 1 : 0);
				if (!pierwszePole.getText().isEmpty())
					user.setNazwa(pierwszePole.getText());
				if (!drugiePole.getText().isEmpty())
					if (Double.parseDouble(drugiePole.getText()) <= 0)
						throw (new Exception("Nie można dodać ujemnej ceny, ani ceny równej 0"));
					else
						user.setCena(Double.parseDouble(drugiePole.getText()));
				if (!trzeciePole.getText().isEmpty())
					user.setOpis(trzeciePole.getText());
				if (!czwartePole.getText().isEmpty())
					user.setKategorie_id_kategorii(Integer.parseInt(czwartePole.getText()));

				user.setKategorie_id_kategorii(((Kategorie) fData.get(jombo.getSelectedIndex())).getId_Kategorii());

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));

				bt.tab.setValueAt(user.getNazwa(), bt.row, 1);
				bt.tab.setValueAt(user.getCena(), bt.row, 2);
				bt.tab.setValueAt(user.getOpis(), bt.row, 3);
				bt.tab.setValueAt(((Kategorie) fData.get(jombo.getSelectedIndex())).getNazwa(), bt.row, 5);
				if (user.getCzy_usunieto() == 1)
					bt.tab.setValueAt("TAK", bt.row, 6);
				else
					bt.tab.setValueAt("NIE", bt.row, 6);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować produktu. Błąd: " + e.getMessage());
		}

	}

	public void dodajLogikeUsuwania(ButtonEditor bt) {
		PolaczenieOracle oc = PolaczenieOracle.getInstance();
		oc.createDBSession();
		Session session = oc.getDBSession();
		Produkty pr = (Produkty) session.createQuery("select u from Produkty u where u.id_produktu = :id")
				.setParameter("id", bt.id).uniqueResult();
		oc.closeDBSession();
		pr.setCzy_usunieto(1);

		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(pr, HibernateOracle.idUzytkownika));
		bt.tab.setValueAt("TAK", bt.row, 6);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		JTextField pierwszePole = new JTextField(7);
		JTextField drugiePole = new JTextField(7);
		JTextField trzeciePole = new JTextField(7);
		JPanel panel = new JPanel();

		PolaczenieOracle oc = PolaczenieOracle.getInstance();
		oc.createDBSession();

		List<Obiekt_Do_Polecen> fData = null;
		List<Obiekt_Do_Polecen> fData2 = null;

		try (Session session = oc.getDBSession()) {
			Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Producenci", Obiekt_Do_Polecen.class);
			fData = query.getResultList();
			query = session.createQuery("FROM Kategorie", Obiekt_Do_Polecen.class);
			fData2 = query.getResultList();
			oc.closeDBSession();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

		String nazwy[] = new String[fData.size()];
		String nazwy2[] = new String[fData2.size()];

		int i = 0;
		for (Obiekt_Do_Polecen stan : fData) {
			nazwy[i] = ((Producenci) stan).getNazwa();
			i++;
		}
		i = 0;
		for (Obiekt_Do_Polecen stan : fData2) {
			nazwy2[i] = ((Kategorie) stan).getNazwa();
			i++;
		}

		JComboBox jombo = new JComboBox(nazwy);
		JComboBox jombo2 = new JComboBox(nazwy2);

		panel.add(new JLabel("Nazwa: "));
		panel.add(pierwszePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Cena: "));
		panel.add(drugiePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Opis: "));
		panel.add(trzeciePole);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Id producenta: "));
		panel.add(jombo);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(new JLabel("Id kategorii: "));
		panel.add(jombo2);

		int wynik = JOptionPane.showConfirmDialog(null, panel, "Dodaj produkt", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				if (pierwszePole.getText().isEmpty() || drugiePole.getText().isEmpty()
						|| trzeciePole.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Produkt nie został dodany");
					return;
				}
				double cena = Double.parseDouble(drugiePole.getText());
				cena = Math.round(cena * 100.0) / 100.0;

				Produkty nowyProdukt = new Produkty(pierwszePole.getText(), cena, trzeciePole.getText(),
						((Producenci) fData.get(jombo.getSelectedIndex())).getId_producenta(),
						((Kategorie) fData2.get(jombo2.getSelectedIndex())).getId_Kategorii(), 0);
				HibernateOracle.repoPolecen
						.dodajPolecenie(new Polecenie_Dodaj(nowyProdukt, HibernateOracle.idUzytkownika));

				Component[] komponenty = kontener.getComponents();
				JTable tab = null;
				JButton dodajPrzycisk = null;
				JButton eksportujDoDruku = null;
				for (Component komponent : komponenty) {
					if (komponent instanceof JScrollPane) {
						tab = (JTable) (((JScrollPane) komponent).getViewport().getView());
						dodajPrzycisk = (JButton) kontener.getComponent(1);
						eksportujDoDruku = (JButton) kontener.getComponent(2);
						kontener.removeAll();
						break;
					}
				}

				if (!HibernateOracle.cache.containsKey("Kategorie")) {
					oc.createDBSession();
					try (Session session2 = oc.getDBSession()) {
						Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Kategorie order by id_kategorii",
								Obiekt_Do_Polecen.class);
						HibernateOracle.cache.put("Kategorie", query.getResultList());
						oc.closeDBSession();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Obiekt_Do_Polecen> cash = HibernateOracle.cache.get("Kategorie");
				String nazwa = "Default";

				int id = Integer.parseInt(((DefaultTableModel) tab.getModel())
						.getValueAt(((DefaultTableModel) tab.getModel()).getRowCount() - 1, 0).toString());
				nowyProdukt.setId_produktu(id + 1);

				for (Obiekt_Do_Polecen entity : cash) {
					Kategorie ent = (Kategorie) entity;
					if (ent.getId_Kategorii() == nowyProdukt.getKategorie_id_kategorii()) {
						nazwa = ent.getNazwa();
						break;
					}
				}

				if (!HibernateOracle.cache.containsKey("Producenci")) {
					oc.createDBSession();
					try (Session session2 = oc.getDBSession()) {
						Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Producenci order by id_producenta",
								Obiekt_Do_Polecen.class);
						HibernateOracle.cache.put("Producenci", query.getResultList());
						oc.closeDBSession();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Obiekt_Do_Polecen> cache2 = HibernateOracle.cache.get("Producenci");
				String nazwa2 = "Default";

				for (Obiekt_Do_Polecen entity : cache2) {
					Producenci ent = (Producenci) entity;
					if (ent.getId_producenta() == nowyProdukt.getProducenci_id_producenta()) {
						nazwa2 = ent.getNazwa();
						break;
					}
				}
				if (HibernateOracle.nazwaTypu.equals("Klient"))
					((DefaultTableModel) tab.getModel()).addRow(new Object[] {
							Integer.toString(((Produkty) nowyProdukt).getId_produktu()),
							((Produkty) nowyProdukt).getNazwa(), Double.toString(((Produkty) nowyProdukt).getCena()),
							((Produkty) nowyProdukt).getOpis(), nazwa2, nazwa });
				else
					((DefaultTableModel) tab.getModel()).addRow(new Object[] {
							Integer.toString(((Produkty) nowyProdukt).getId_produktu()),
							((Produkty) nowyProdukt).getNazwa(), Double.toString(((Produkty) nowyProdukt).getCena()),
							((Produkty) nowyProdukt).getOpis(), nazwa2, nazwa,
							((((Produkty) nowyProdukt).getCzy_usunieto()) == 1) ? "TAK" : "NIE" });

				JScrollPane pane = new JScrollPane(tab);
				kontener.add(pane);
				kontener.add(dodajPrzycisk);
				kontener.add(eksportujDoDruku);
				kontener.repaint();
				kontener.revalidate();

			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać produktu. Błąd: " + e.getMessage());
		}

	}

}
