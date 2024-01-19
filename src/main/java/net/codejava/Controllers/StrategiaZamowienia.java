package net.codejava.Controllers;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

import java.awt.Component;
import java.util.ArrayList;
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

import net.codejava.HibernateOracle;

public class StrategiaZamowienia implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor bt) {
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
			nazwy[i] = ((Stany_Zamowienia) stan).getNazwa();
			i++;
		}

		if (HibernateOracle.nazwaTypu.equals("Pracownik"))
			//dyrektorOkienek.stworzOkno(new String[][] {nazwy}, TypPola.combobox, 1);
			dyrektorOkienek.edytowanieZamowieniaPracownik(nazwy);
		else
			//dyrektorOkienek.stworzOkno(new String[][] {nazwy}, TypPola.label, "Ulica wysyłki: ", TypPola.label, "Miasto wysyłki: ", TypPola.combobox, 1);
			dyrektorOkienek.edytowanieZamowieniaAdministrator(nazwy);
		JPanel okno = dyrektorOkienek.zwrocOkno();

		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj zamówienie", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				oc.createDBSession();
				Session session = oc.getDBSession();

				Zamowienia user = (Zamowienia) session
						.createQuery("select u from Zamowienia u where u.id_zamowienia = :id").setParameter("id", bt.id)
						.uniqueResult();
				oc.closeDBSession();

				if (!pola.get(0).getText().isEmpty())
					user.setAdres_wysylki_miasto(pola.get(0).getText());
				if (!pola.get(1).getText().isEmpty())
					user.setAdres_wysylki_ulica(pola.get(1).getText());

				user.setId_stanu_zamowienia(
						((Stany_Zamowienia) fData.get(((JComboBox)okno.getComponent(6)).getSelectedIndex())).getId_Stanu_Zamowienia());

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(user, HibernateOracle.idUzytkownika));

				bt.tab.setValueAt(user.getAdres_wysylki_miasto(), bt.row, 1);
				bt.tab.setValueAt(user.getAdres_wysylki_ulica(), bt.row, 2);
				bt.tab.setValueAt(((Stany_Zamowienia) fData.get(((JComboBox)okno.getComponent(6)).getSelectedIndex())).getNazwa(), bt.row, 4);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować zamówienia. Błąd: " + e.getMessage());
		}

	}

	public void dodajLogikeUsuwania(ButtonEditor bt) {
		Zamowienia pr = new Zamowienia();
		pr.setId_zamowienia(bt.id);

		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Usun(pr, HibernateOracle.idUzytkownika));

		List<Obiekt_Do_Polecen> lista = HibernateOracle.cache.get("Magazyny");
		lista.remove(bt.row);
		HibernateOracle.cache.put("Magazyny", lista);
		((DefaultTableModel) bt.tab.getModel()).removeRow(bt.row);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		
		//dyrektorOkienek.stworzOkno(null, TypPola.label, "Id użytkownika: ", TypPola.label, "Adres wysyłki miasto: ", TypPola.label, "Adres wysyłki ulica: ", TypPola.label, "Koszt: ");
		
		dyrektorOkienek.dodawanieZamowienia();
		JPanel okno = dyrektorOkienek.zwrocOkno();

		int wynik = JOptionPane.showConfirmDialog(null, okno, "Dodaj zamówienie", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty()
						|| pola.get(2).getText().isEmpty() || pola.get(3).getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Zamówienie nie zostało dodane");
					return;
				}

				double cena = Double.parseDouble(pola.get(3).getText());
				cena = Math.round(cena * 100.0) / 100.0;
				Zamowienia noweZamowienie = new Zamowienia(cena, pola.get(1).getText(), pola.get(2).getText(), 1,
						Integer.parseInt(pola.get(0).getText()), null);
				HibernateOracle.repoPolecen
						.dodajPolecenie(new Polecenie_Dodaj(noweZamowienie, HibernateOracle.idUzytkownika));

				Object[] obiekty = pobierzModel(kontener);
				
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

				int id = Integer.parseInt(((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.getValueAt(((DefaultTableModel) ((JTable)obiekty[0]).getModel()).getRowCount() - 1, 0).toString());
				noweZamowienie.setId_zamowienia(id + 1);

				for (Obiekt_Do_Polecen entities : cash) {
					Stany_Zamowienia ent = (Stany_Zamowienia) entities;

					if (ent.getId_Stanu_Zamowienia() == noweZamowienie.getId_stanu_zamowienia()) {
						nazwa = ent.getNazwa();
					}

				}

				((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.addRow(new Object[] { Integer.toString(((Zamowienia) noweZamowienie).getId_zamowienia()),
								((Zamowienia) noweZamowienie).getAdres_wysylki_miasto(),
								((Zamowienia) noweZamowienie).getAdres_wysylki_ulica(),
								Double.toString(((Zamowienia) noweZamowienie).getKoszt()), nazwa,
								Integer.toString(((Zamowienia) noweZamowienie).getUzytkownicy_id_uzytkownika()), null,
								((Zamowienia) noweZamowienie).getOpis() });

				odswiezModel(kontener, obiekty);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać zamówienia. Błąd: " + e.getMessage());
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
