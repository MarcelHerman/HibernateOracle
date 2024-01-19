package net.codejava.Controllers;

import net.codejava.Models.*;
import net.codejava.Views.BudowniczyTabeliDruk;
import net.codejava.Views.BudowniczyTabeliSwing.ButtonEditor;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	public void dodajLogikeEdytowania(ButtonEditor be) {
		PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
		bd.stworzSesjeBD();
		List<Obiekt_Do_Polecen> daneZewn = null;
		try (Session sesja = bd.pobierzSesjeBD()) {
			Query<Obiekt_Do_Polecen> zapytanie = sesja.createQuery("FROM Typy_uzytkownika", Obiekt_Do_Polecen.class);
			daneZewn = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		String nazwy[] = new String[daneZewn.size()];
		
		int i = 0;
		for (Obiekt_Do_Polecen stan : daneZewn) {
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
				bd.stworzSesjeBD();
				Session sesja = bd.pobierzSesjeBD();

				Uzytkownicy rekord = (Uzytkownicy) sesja
						.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
						.setParameter("id", be.id).uniqueResult();
				bd.zamknijSesjeBD();

				rekord.setCzy_usunieto(((JCheckBox)okno.getComponent(14)).isSelected() ? 1 : 0);
				if (!pola.get(0).getText().isEmpty())
					rekord.setNazwa_uzytkownika(pola.get(0).getText());
				if (!pola.get(1).getText().isEmpty())
					rekord.setLogin(pola.get(1).getText());
				if (!pola.get(2).getText().isEmpty())
					rekord.setHaslo(pola.get(2).getText());
				if (!pola.get(3).getText().isEmpty())
					rekord.setE_mail(pola.get(3).getText());
				rekord.setId_typu_uzytkownika(
						((Typy_uzytkownika) daneZewn.get(((JComboBox)okno.getComponent(12)).getSelectedIndex())).getId_typu_uzytkownika());

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(rekord, HibernateOracle.idUzytkownika));

				be.tab.setValueAt(rekord.getNazwa_uzytkownika(), be.row, 1);
				be.tab.setValueAt(rekord.getLogin(), be.row, 2);
				be.tab.setValueAt(rekord.getHaslo(), be.row, 3);
				be.tab.setValueAt(rekord.getE_mail(), be.row, 4);
				be.tab.setValueAt(((Typy_uzytkownika) daneZewn.get(((JComboBox)okno.getComponent(12)).getSelectedIndex())).getNazwa(), be.row, 5);
				if (rekord.getCzy_usunieto() == 1)
					be.tab.setValueAt("TAK", be.row, 6);
				else
					be.tab.setValueAt("NIE", be.row, 6);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
		}

	}

	public void dodajLogikeUsuwania(ButtonEditor be) {
		PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
		bd.stworzSesjeBD();
		Session sesja = bd.pobierzSesjeBD();
		Uzytkownicy pr = (Uzytkownicy) sesja.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
				.setParameter("id", be.id).uniqueResult();
		bd.zamknijSesjeBD();

		pr.setCzy_usunieto(1);
		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(pr, HibernateOracle.idUzytkownika));

		be.tab.setValueAt("TAK", be.row, 6);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
		bd.stworzSesjeBD();
		List<Obiekt_Do_Polecen> daneZewn = null;
		try (Session sesja = bd.pobierzSesjeBD()) {
			Query<Obiekt_Do_Polecen> zapytanie = sesja.createQuery("FROM Typy_uzytkownika", Obiekt_Do_Polecen.class);
			daneZewn = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		String nazwy[] = new String[daneZewn.size()];
		
		int i = 0;
		for (Obiekt_Do_Polecen stan : daneZewn) {
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
						((Typy_uzytkownika) daneZewn.get(((JComboBox)okno.getComponent(12)).getSelectedIndex())).getId_typu_uzytkownika(), 0);

				HibernateOracle.repoPolecen
						.dodajPolecenie(new Polecenie_Dodaj(nowyUzytkownik, HibernateOracle.idUzytkownika));

				Object[] obiekty = pobierzModel(kontener);

				if (!HibernateOracle.cache.containsKey("TypyUzytkownika")) {
					bd.stworzSesjeBD();
					try (Session sesja2 = bd.pobierzSesjeBD()) {
						Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery(
								"FROM Typy_uzytkownika order by id_typu_uzytkownika", Obiekt_Do_Polecen.class);
						HibernateOracle.cache.put("TypyUzytkownika", zapytanie.getResultList());
						bd.zamknijSesjeBD();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Obiekt_Do_Polecen> cache = HibernateOracle.cache.get("TypyUzytkownika");
				String nazwa = "Default";

				int id = Integer.parseInt(((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.getValueAt(((DefaultTableModel) ((JTable)obiekty[0]).getModel()).getRowCount() - 1, 0).toString());
				nowyUzytkownik.setId_uzytkownika(id + 1);

				for (Obiekt_Do_Polecen entity : cache) {
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

	@Override
	public void dodajLogikeDruku(DyrektorTabel dyrektor) {
    	BudowniczyTabeliDruk budDruk = new BudowniczyTabeliDruk();

    	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

        List<Obiekt_Do_Polecen> obiekty = null;
		bd.stworzSesjeBD();
		
		try (Session sesja2 = bd.pobierzSesjeBD()) {
			
            Query<Obiekt_Do_Polecen> zapytanie = null;
            zapytanie = sesja2.createQuery("FROM Uzytkownicy order by id_uzytkownika", Obiekt_Do_Polecen.class); 
            obiekty = zapytanie.getResultList();
            bd.zamknijSesjeBD();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		dyrektor.tworzTabeleUzytkownicy(obiekty, budDruk);
         String table = (String)dyrektor.pobierzTabele();
         												 		                 
         String path = "wykaz_uzytkownicy.txt";
         File plik = new File(path);

        		                     
             try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
            	 pisarz.write(table);		                         
                 JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
             } catch (IOException e) {
                 e.printStackTrace();
                 JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
             }
    
		
	}

}
