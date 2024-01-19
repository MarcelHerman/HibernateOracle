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

public class StrategiaProdukty implements IStrategia {

	@Override
	public void dodajLogikeEdytowania(ButtonEditor be) {
		
		PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
		bd.stworzSesjeBD();
		List<Obiekt_Do_Polecen> daneZewn = null;

		try (Session sesja = bd.pobierzSesjeBD()) {
			Query<Obiekt_Do_Polecen> zapytanie = sesja.createQuery("FROM Kategorie", Obiekt_Do_Polecen.class);
			daneZewn = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

		String nazwy[] = new String[daneZewn.size()];

		int i = 0;
		for (Obiekt_Do_Polecen stan : daneZewn) {
			nazwy[i] = ((Kategorie) stan).getNazwa();
			i++;
		}
		
		//o = pobierzObiektDoEdycji();
		//defo = pobierzDef()
		//dyrektorOkienek.stworzOkno(new String[][] {nazwy}, TypPola.label, "Nazwa produktu: ", TypPola.label, "Cena: ", TypPola.label, "Opis: ", TypPola.combobox, 1, TypPola.checkbox, "Czy usunięty: ");		
		//dyrektorOkienek.stworzOkno(defonew String[][] {nazwy}, TypPola.label, "Nazwa produktu: ", TypPola.label, "Cena: ", TypPola.label, "Opis: ", TypPola.combobox, 1, TypPola.checkbox, "Czy usunięty: ");
		
		dyrektorOkienek.edytowanieProdukty(nazwy);
		JPanel okno = dyrektorOkienek.zwrocOkno();
		
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Edytuj produkt", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {
				//if waliduj(okno) {
					//zapisz(okno)
				//}
				bd.stworzSesjeBD();
				Session sesja = bd.pobierzSesjeBD();

				Produkty rekord = (Produkty) sesja.createQuery("select u from Produkty u where u.id_produktu = :id")
						.setParameter("id", be.id).uniqueResult();

				bd.zamknijSesjeBD();

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				
				rekord.setCzy_usunieto(((JCheckBox)okno.getComponent(11)).isSelected() ? 1 : 0);
				if (!pola.get(0).getText().isEmpty())
					rekord.setNazwa(pola.get(0).getText());
				if (!pola.get(1).getText().isEmpty())
					if (Double.parseDouble(pola.get(1).getText()) <= 0)
						throw (new Exception("Nie można dodać ujemnej ceny, ani ceny równej 0"));
					else
						rekord.setCena(Double.parseDouble(pola.get(1).getText()));
				if (!pola.get(2).getText().isEmpty())
					rekord.setOpis(pola.get(2).getText());

				rekord.setKategorie_id_kategorii(((Kategorie) daneZewn.get(((JComboBox)okno.getComponent(9)).getSelectedIndex())).getId_Kategorii());

				HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(rekord, HibernateOracle.idUzytkownika));

				be.tab.setValueAt(rekord.getNazwa(), be.row, 1);
				be.tab.setValueAt(rekord.getCena(), be.row, 2);
				be.tab.setValueAt(rekord.getOpis(), be.row, 3);
				be.tab.setValueAt(((Kategorie) daneZewn.get(((JComboBox)okno.getComponent(9)).getSelectedIndex())).getNazwa(), be.row, 5);
				if (rekord.getCzy_usunieto() == 1)
					be.tab.setValueAt("TAK", be.row, 6);
				else
					be.tab.setValueAt("NIE", be.row, 6);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się edytować produktu. Błąd: " + e.getMessage());
		}

	}

	public void dodajLogikeUsuwania(ButtonEditor be) {
		PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
		bd.stworzSesjeBD();
		Session sesja = bd.pobierzSesjeBD();
		Produkty pr = (Produkty) sesja.createQuery("select u from Produkty u where u.id_produktu = :id")
				.setParameter("id", be.id).uniqueResult();
		bd.zamknijSesjeBD();
		pr.setCzy_usunieto(1);

		HibernateOracle.repoPolecen.dodajPolecenie(new Polecenie_Edytuj(pr, HibernateOracle.idUzytkownika));
		be.tab.setValueAt("TAK", be.row, 6);
	}

	public void dodajLogikeDodawania(JPanel kontener) {
		List<Obiekt_Do_Polecen> daneZewn = null;
		List<Obiekt_Do_Polecen> daneZewn2 = null;
		
		PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
		bd.stworzSesjeBD();
		try (Session sesja = bd.pobierzSesjeBD()) {
			Query<Obiekt_Do_Polecen> zapytanie = sesja.createQuery("FROM Producenci", Obiekt_Do_Polecen.class);
			daneZewn = zapytanie.getResultList();
			zapytanie = sesja.createQuery("FROM Kategorie", Obiekt_Do_Polecen.class);
			daneZewn2 = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		String nazwy[] = new String[daneZewn.size()];
		String nazwy2[] = new String[daneZewn2.size()];

		int i = 0;
		for (Obiekt_Do_Polecen stan : daneZewn) {
			nazwy[i] = ((Producenci) stan).getNazwa();
			i++;
		}
		i = 0;
		for (Obiekt_Do_Polecen stan : daneZewn2) {
			nazwy2[i] = ((Kategorie) stan).getNazwa();
			i++;
		}
		
		//dyrektorOkienek.stworzOkno(new String[][] {nazwy, nazwy2}, TypPola.label, "Nazwa produktu: ", TypPola.label, "Cena: ", TypPola.label, "Opis: ", TypPola.combobox, 1, TypPola.combobox, 2);
		
		dyrektorOkienek.dodawanieProdukty(nazwy, nazwy2);
		JPanel okno = dyrektorOkienek.zwrocOkno();
		
		int wynik = JOptionPane.showConfirmDialog(null, okno, "Dodaj produkt", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (wynik == JOptionPane.OK_OPTION) {

				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				
				if (pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty()
						|| pola.get(2).getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Produkt nie został dodany");
					return;
				}
				double cena = Double.parseDouble(pola.get(1).getText());
				cena = Math.round(cena * 100.0) / 100.0;

				Produkty nowyProdukt = new Produkty(pola.get(1).getText(), cena, pola.get(2).getText(),
						((Producenci) daneZewn.get(((JComboBox)okno.getComponent(9)).getSelectedIndex())).getId_producenta(),
						((Kategorie) daneZewn2.get(((JComboBox)okno.getComponent(11)).getSelectedIndex())).getId_Kategorii(), 0);
				HibernateOracle.repoPolecen
						.dodajPolecenie(new Polecenie_Dodaj(nowyProdukt, HibernateOracle.idUzytkownika));

				Object[] obiekty = pobierzModel(kontener);

				if (!HibernateOracle.cache.containsKey("Kategorie")) {
					bd.stworzSesjeBD();
					try (Session sesja2 = bd.pobierzSesjeBD()) {
						Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Kategorie order by id_kategorii",
								Obiekt_Do_Polecen.class);
						HibernateOracle.cache.put("Kategorie", zapytanie.getResultList());
						bd.zamknijSesjeBD();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				List<Obiekt_Do_Polecen> cache = HibernateOracle.cache.get("Kategorie");
				String nazwa = "Default";

				int id = Integer.parseInt(((DefaultTableModel) ((JTable)obiekty[0]).getModel())
						.getValueAt(((DefaultTableModel) ((JTable)obiekty[0]).getModel()).getRowCount() - 1, 0).toString());
				nowyProdukt.setId_produktu(id + 1);

				for (Obiekt_Do_Polecen entity : cache) {
					Kategorie ent = (Kategorie) entity;
					if (ent.getId_Kategorii() == nowyProdukt.getKategorie_id_kategorii()) {
						nazwa = ent.getNazwa();
						break;
					}
				}

				if (!HibernateOracle.cache.containsKey("Producenci")) {
					bd.stworzSesjeBD();
					try (Session sesja2 = bd.pobierzSesjeBD()) {
						Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Producenci order by id_producenta",
								Obiekt_Do_Polecen.class);
						HibernateOracle.cache.put("Producenci", zapytanie.getResultList());
						bd.zamknijSesjeBD();
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
					((DefaultTableModel) ((JTable)obiekty[0]).getModel()).addRow(new Object[] {
							Integer.toString(((Produkty) nowyProdukt).getId_produktu()),
							((Produkty) nowyProdukt).getNazwa(), Double.toString(((Produkty) nowyProdukt).getCena()),
							((Produkty) nowyProdukt).getOpis(), nazwa2, nazwa });
				else
					((DefaultTableModel) ((JTable)obiekty[0]).getModel()).addRow(new Object[] {
							Integer.toString(((Produkty) nowyProdukt).getId_produktu()),
							((Produkty) nowyProdukt).getNazwa(), Double.toString(((Produkty) nowyProdukt).getCena()),
							((Produkty) nowyProdukt).getOpis(), nazwa2, nazwa,
							((((Produkty) nowyProdukt).getCzy_usunieto()) == 1) ? "TAK" : "NIE" });

				odswiezModel(kontener, obiekty);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Nie udało się dodać produktu. Błąd: " + e.getMessage());
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
        bd.stworzSesjeBD();

        List<Obiekt_Do_Polecen> obiekty = null;
        
		try (Session sesja2 = bd.pobierzSesjeBD()) {							
            Query<Obiekt_Do_Polecen> zapytanie = null;
            zapytanie = sesja2.createQuery("FROM Produkty order by id_produktu", Obiekt_Do_Polecen.class);
            obiekty = zapytanie.getResultList();
            bd.zamknijSesjeBD();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		dyrektor.tworzTabeleProdukty(obiekty, budDruk);
         String table = (String)dyrektor.pobierzTabele();
         												 		                 
         String path = "wykaz_produktow.txt";
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
