package net.codejava.Controllers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.service.spi.ServiceException;

import net.codejava.HibernateOracle;
import net.codejava.Views.BudowniczyTabeliSwing;
import net.codejava.Views.widokAplikacji;
import net.codejava.Controllers.*;
import net.codejava.Models.*;

public class ObslugaPrzyciskow {
    public final widokAplikacji widok;
    private PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
    private DyrektorOkienek dyrektorOkienek = new DyrektorOkienek();
    private JFrame frame = new JFrame("Elektryka Prad Nie Tyka");
    private JPanel kontener = new JPanel();
    private JMenuBar bar = new JMenuBar();
    private Component glue = Box.createHorizontalGlue();
	BudowniczyTabeliSwing budSwing = new BudowniczyTabeliSwing();
	DyrektorTabel dyrektor = new DyrektorTabel();
    
    public ObslugaPrzyciskow(widokAplikacji widok) {
        this.widok = widok;
        inicjalizujPrzyciski();
    }
    
    public void inicjalizujWidok(JFrame frame, JPanel kontener, JMenuBar bar, Component glue) {
    	this.frame = frame;
        this.kontener = kontener;
        this.bar = bar;
        this.glue = glue;
    }

    private void inicjalizujPrzyciski() {
        /*widok.getPokazZalogujPrzycisk().addActionListener(e -> wykonajZalogujAkcje());
        widok.getPokazProduktPrzycisk().addActionListener(e -> wykonajProduktAkcje());
        widok.getPokazZamowieniaPrzycisk().addActionListener(e -> wykonajZamowieniaAkcje());
        widok.getPokazMagazynyPrzycisk().addActionListener(e -> wykonajMagazynyAkcje());
        widok.getPokazFakturyPrzycisk().addActionListener(e -> wykonajFakturyAkcje());
        widok.getPokazUzytkownicyPrzycisk().addActionListener(e -> wykonajUzytkownicyAkcje());
        widok.getPokazWylogujPrzycisk().addActionListener(e -> wykonajWylogujAkcje());
        widok.getPokazKategoriePrzycisk().addActionListener(e -> wykonajKategorieAkcje());
        widok.getPokazProducentowPrzycisk().addActionListener(e -> wykonajProducentowAkcje());
        widok.getPokazProduktMagazynPrzycisk().addActionListener(e -> wykonajProduktMagazynAkcje());
        widok.getPokazProduktZamowieniaPrzycisk().addActionListener(e -> wykonajProduktZamowieniaAkcje());
        widok.getPokazStanyZamowienPrzycisk().addActionListener(e -> wykonajStanyZamowienAkcje());
        widok.getPokazTypyUzytkownikaPrzycisk().addActionListener(e -> wykonajTypyUzytkownikaAkcje());
        widok.getKontoPrzycisk().addActionListener(e -> wykonajKontoAkcje());
        widok.getDodajPrzycisk().addActionListener(e -> wykonajDodajAkcje());
        widok.getZalozKontoPrzycisk().addActionListener(e -> wykonajZalozKontoAkcje());
        widok.getEksportujDoDrukuPrzycisk().addActionListener(e -> wykonajEksportujDoDrukuAkcje());*/
    }

    public void wykonajZalogujAkcje() {
    	
	}

    public void wykonajProduktAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

		List<Obiekt_Do_Polecen> obiekty = null;
		bd.stworzSesjeBD();

		try (Session sesja2 = bd.pobierzSesjeBD()) {

			Query<Obiekt_Do_Polecen> zapytanie = null;
			if ((HibernateOracle.nazwaTypu.equals("Klient")))
				zapytanie = sesja2.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu",
						Obiekt_Do_Polecen.class);
			else
				zapytanie = sesja2.createQuery("FROM Produkty order by id_produktu", Obiekt_Do_Polecen.class);
			obiekty = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
		}

		dyrektor.tworzTabeleProdukty(obiekty, budSwing);

		JTable tabSwing = (JTable) dyrektor.pobierzTabele();

		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);

		if (!(HibernateOracle.nazwaTypu.equals("Klient"))) {
			kontener.add(widok.getDodajPrzycisk());
			kontener.add(widok.getEksportujDoDruku());
		}
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajZamowieniaAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

		List<Obiekt_Do_Polecen> obiekty = null;
		bd.stworzSesjeBD();

		try (Session sesja2 = bd.pobierzSesjeBD()) {
			Query<Obiekt_Do_Polecen> zapytanie = null;
			if (HibernateOracle.nazwaTypu.equals("Klient"))
				zapytanie = sesja2.createQuery(
						"FROM Zamowienia z where z.uzytkownicy_id_uzytkownika = :id order by z.id_zamowienia",
						Obiekt_Do_Polecen.class).setParameter("id", HibernateOracle.idUzytkownika);
			else
				zapytanie = sesja2.createQuery("FROM Zamowienia order by id_zamowienia",
						Obiekt_Do_Polecen.class);
			obiekty = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
		}

		dyrektor.tworzTabeleZamowienia(obiekty, budSwing);

		JTable tabSwing = (JTable) dyrektor.pobierzTabele();
		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);
		if (!(HibernateOracle.nazwaTypu.equals("Klient"))) {
			kontener.add(widok.getDodajPrzycisk());
			kontener.add(widok.getEksportujDoDruku());
		}
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajMagazynyAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

		if (!HibernateOracle.cache.containsKey("Magazyny")) {
			bd.stworzSesjeBD();
			try (Session sesja2 = bd.pobierzSesjeBD()) {
				Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Magazyny order by id_magazynu",
						Obiekt_Do_Polecen.class);
				HibernateOracle.cache.put("Magazyny", zapytanie.getResultList());
				bd.zamknijSesjeBD();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		dyrektor.tworzTabeleMagazyny(HibernateOracle.cache.get("Magazyny"), budSwing);
		JTable tabSwing = (JTable) dyrektor.pobierzTabele();
		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);
		kontener.add(widok.getDodajPrzycisk());
		kontener.add(widok.getEksportujDoDruku());
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajFakturyAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

		List<Obiekt_Do_Polecen> obiekty = null;
		bd.stworzSesjeBD();

		try (Session sesja2 = bd.pobierzSesjeBD()) {
			Query<Obiekt_Do_Polecen> zapytanie = null;
			if (HibernateOracle.nazwaTypu.equals("Klient"))
				zapytanie = sesja2.createQuery(
						"SELECT f FROM Faktury f, Zamowienia z, Uzytkownicy u where f.zamowienia_id_zamowienia=z.id_zamowienia and z.uzytkownicy_id_uzytkownika=u.id_uzytkownika and u.id_uzytkownika = :id order by f.id_faktury",
						Obiekt_Do_Polecen.class).setParameter("id", HibernateOracle.idUzytkownika);
			else
				zapytanie = sesja2.createQuery("FROM Faktury order by id_faktury", Obiekt_Do_Polecen.class);
			obiekty = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
		}

		dyrektor.tworzTabeleFaktury(obiekty, budSwing);
		JTable tabSwing = (JTable) dyrektor.pobierzTabele();
		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);
		if (!(HibernateOracle.nazwaTypu.equals("Klient"))) {
			kontener.add(widok.getDodajPrzycisk());
			kontener.add(widok.getEksportujDoDruku());
		}
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajUzytkownicyAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

		List<Obiekt_Do_Polecen> obiekty = null;
		bd.stworzSesjeBD();

		try (Session sesja2 = bd.pobierzSesjeBD()) {
			Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Uzytkownicy order by id_uzytkownika",
					Obiekt_Do_Polecen.class);
			obiekty = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
		}

		dyrektor.tworzTabeleUzytkownicy(obiekty, budSwing);
		JTable tabSwing = (JTable) dyrektor.pobierzTabele();
		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);
		kontener.add(widok.getDodajPrzycisk());
		kontener.add(widok.getEksportujDoDruku());
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajWylogujAkcje() {
        // Logika dla przycisku "Wyloguj"
        // ...
    }

    public void wykonajKategorieAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

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

		dyrektor.tworzTabeleKategorie(HibernateOracle.cache.get("Kategorie"), budSwing);

		JTable tabSwing = (JTable) dyrektor.pobierzTabele();
		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);
		kontener.add(widok.getDodajPrzycisk());
		kontener.add(widok.getEksportujDoDruku());
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajProducentowAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

		if (!HibernateOracle.cache.containsKey("Producenci")) {
			bd.stworzSesjeBD();
			try (Session sesja2 = bd.pobierzSesjeBD()) {
				Query<Obiekt_Do_Polecen> zapytanie = sesja2
						.createQuery("FROM Producenci order by id_producenta", Obiekt_Do_Polecen.class);
				HibernateOracle.cache.put("Producenci", zapytanie.getResultList());
				bd.zamknijSesjeBD();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		dyrektor.tworzTabeleProducenci(HibernateOracle.cache.get("Producenci"), budSwing);
		JTable tabSwing = (JTable) dyrektor.pobierzTabele();
		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);
		kontener.add(widok.getDodajPrzycisk());
		kontener.add(widok.getEksportujDoDruku());
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajProduktMagazynAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

		List<Obiekt_Do_Polecen> obiekty = null;
		bd.stworzSesjeBD();

		try (Session sesja2 = bd.pobierzSesjeBD()) {
			Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery(
					"FROM Produkt_Magazyn order by produkt_magazyn_id.magazyny_id_magazynu, produkt_magazyn_id.produkty_id_produktu",
					Obiekt_Do_Polecen.class);
			obiekty = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
		}

		dyrektor.tworzTabeleProdukt_Magazyn(obiekty, budSwing);
		JTable tabSwing = (JTable) dyrektor.pobierzTabele();
		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);
		kontener.add(widok.getDodajPrzycisk());
		kontener.add(widok.getEksportujDoDruku());
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajProduktZamowieniaAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

		List<Obiekt_Do_Polecen> obiekty = null;
		bd.stworzSesjeBD();

		try (Session sesja2 = bd.pobierzSesjeBD()) {
			Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery(
					"FROM Produkt_Zamowienia order by produkt_zamowienia_id.id_zamowienia, produkt_zamowienia_id.id_produktu",
					Obiekt_Do_Polecen.class);
			obiekty = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
		}

		dyrektor.tworzTabeleProdukt_Zamowienia(obiekty, budSwing);
		JTable tabSwing = (JTable) dyrektor.pobierzTabele();
		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);
		kontener.add(widok.getDodajPrzycisk());
		kontener.add(widok.getEksportujDoDruku());
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajStanyZamowienAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

		if (!HibernateOracle.cache.containsKey("StanyZamowien")) {
			bd.stworzSesjeBD();
			try (Session sesja2 = bd.pobierzSesjeBD()) {
				Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery(
						"FROM Stany_Zamowienia order by id_stanu_zamowienia", Obiekt_Do_Polecen.class);
				HibernateOracle.cache.put("StanyZamowien", zapytanie.getResultList());
				bd.zamknijSesjeBD();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		dyrektor.tworzTabeleStany_Zamowienia(HibernateOracle.cache.get("StanyZamowien"), budSwing);
		JTable tabSwing = (JTable) dyrektor.pobierzTabele();
		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);
		kontener.add(widok.getEksportujDoDruku());
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajTypyUzytkownikaAkcje() {
    	kontener.removeAll();
		PolaczenieProxy pc = new PolaczenieProxy();
		pc.zamknijSesjeBD();

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

		dyrektor.tworzTabeleTypy_uzytkownika(HibernateOracle.cache.get("TypyUzytkownika"), budSwing);
		JTable tabSwing = (JTable) dyrektor.pobierzTabele();
		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);
		kontener.add(widok.getEksportujDoDruku());
		frame.revalidate();
		frame.repaint();
    }

    public void wykonajKontoAkcje() {
        // Logika dla przycisku " "
        // ...
    }

    public void wykonajDodajAkcje() {
        // Logika dla przycisku "Dodaj rekord"
        // ...
    }

    public void wykonajZalozKontoAkcje() {
    	dyrektorOkienek.zalozKontoOkienko();

		int result = JOptionPane.showConfirmDialog(null, dyrektorOkienek.zwrocOkno(), "Zakładanie konta",
				JOptionPane.OK_CANCEL_OPTION);

		try {
			if (result == JOptionPane.OK_OPTION) {
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty()
						|| pola.get(2).getText().isEmpty() || pola.get(3).getText().isEmpty())
					throw (new Exception("Nie podano wszystkich danych. Konto nie zostało utworzone."));

				PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
				bd.stworzSesjeBD();
				Session sesja = bd.pobierzSesjeBD();

				sesja.save(new Uzytkownicy(pola.get(0).getText(), pola.get(1).getText(), pola.get(2).getText(),
						pola.get(3).getText(), 3, 0));
				bd.zamknijSesjeBD();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Wystapil bład podczas dodawania konta: " + e.getMessage());
		}
    }

    public void wykonajEksportujDoDrukuAkcje() {
        // Logika dla przycisku "Drukuj"
        // ...
    }
}
