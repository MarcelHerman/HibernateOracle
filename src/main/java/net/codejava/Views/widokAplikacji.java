package net.codejava.Views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import org.hibernate.Session;
import org.hibernate.query.Query;

import net.codejava.HibernateOracle;
import net.codejava.Controllers.DyrektorTabel;
import net.codejava.Controllers.ObslugaPrzyciskow;
import net.codejava.Models.*;

public class widokAplikacji {

	private JButton dodajPrzycisk = new JButton("Dodaj rekord");
	private JButton eksportujDoDruku = new JButton("Drukuj");
	private JButton pokazZalogujPrzycisk = new JButton("Zaloguj się");
	private JButton pokazProduktPrzycisk = new JButton("Produkty");
	private JButton pokazZamowieniaPrzycisk = new JButton("Zamówienia");
	private JButton pokazMagazynyPrzycisk = new JButton("Magazyny");
	private JButton pokazFakturyPrzycisk = new JButton("Faktury");
	private JButton pokazUzytkownicyPrzycisk = new JButton("Użytkownicy");
	private JButton pokazWylogujPrzycisk = new JButton("Wyloguj");
	private JButton pokazKategoriePrzycisk = new JButton("Kategorie");
	private JButton pokazProducentowPrzycisk = new JButton("Producenci");
	private JButton pokazProduktMagazynPrzycisk = new JButton("Produkty w magazynach");
	private JButton pokazProduktZamowieniaPrzycisk = new JButton("Produkty w zamowieniach");
	private JButton pokazStanyZamowienPrzycisk = new JButton("Stany zamówień");
	private JButton pokazTypyUzytkownikaPrzycisk = new JButton("Typy użytkownika");
	private JButton kontoPrzycisk = new JButton(" ");
	private JButton zalozKontoPrzycisk = new JButton("Zalóż konto");
	
	public void Inicjalizuj() {

		PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
		
		ObslugaPrzyciskow akcja = new ObslugaPrzyciskow(this);
		bd.stworzSesjeBD();
		HibernateOracle.cache = new HashMap<>();
		
		List<Obiekt_Do_Polecen> obiekty = null;

		try (Session sesja = bd.pobierzSesjeBD()) {
			Query<Obiekt_Do_Polecen> zapytanie = sesja
					.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class);
			obiekty = zapytanie.getResultList();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// bd.zamknijSesjeBD();

		final JFrame frame = new JFrame("Elektryka Prad Nie Tyka");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!HibernateOracle.nazwaTypu.equals("Klient")) {
					HibernateOracle.repoPolecen.zapiszDoPliku();
				}
			}
		});

		JPanel kontener = new JPanel();
		frame.setContentPane(kontener);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar bar = new JMenuBar();

		Component glue = Box.createHorizontalGlue();
		bar.add(glue);
		bar.add(zalozKontoPrzycisk);
		bar.add(pokazZalogujPrzycisk);

		BudowniczyTabeliSwing budSwing = new BudowniczyTabeliSwing();
		DyrektorTabel dyrektor = new DyrektorTabel();
		//DyrektorOkienek dyrektorOkienek = new DyrektorOkienek();
		
		akcja.inicjalizujWidok(frame, kontener, bar, glue);

		// budSwing.tworzTabeleProdukty(obiekty);
		dyrektor.tworzTabeleProdukty(obiekty, budSwing);

		JTable tabSwing = (JTable) dyrektor.pobierzTabele();

		JScrollPane pane = new JScrollPane(tabSwing);

		kontener.add(pane);

		frame.setJMenuBar(bar);

		// Ustaw preferowany rozmiar dla JFrame
		frame.setPreferredSize(new Dimension(1400, 800));

		// Spakuj ramkę
		frame.pack();

		frame.setVisible(true);

		dodajPrzycisk.addActionListener(e -> HibernateOracle.obecnaStrategia.dodajLogikeDodawania(kontener));

		eksportujDoDruku.addActionListener(e -> HibernateOracle.obecnaStrategia.dodajLogikeDruku(dyrektor));
	}
	
	public JButton getDodajPrzycisk() {
		return dodajPrzycisk;
	}
	
	public JButton getEksportujDoDruku() {
		return eksportujDoDruku;
	}
	
	public JButton getPokazZalogujPrzycisk() {
        return pokazZalogujPrzycisk;
    }

    public JButton getPokazProduktPrzycisk() {
        return pokazProduktPrzycisk;
    }

    public JButton getPokazZamowieniaPrzycisk() {
        return pokazZamowieniaPrzycisk;
    }

    public JButton getPokazMagazynyPrzycisk() {
        return pokazMagazynyPrzycisk;
    }

    public JButton getPokazFakturyPrzycisk() {
        return pokazFakturyPrzycisk;
    }

    public JButton getPokazUzytkownicyPrzycisk() {
        return pokazUzytkownicyPrzycisk;
    }

    public JButton getPokazWylogujPrzycisk() {
        return pokazWylogujPrzycisk;
    }

    public JButton getPokazKategoriePrzycisk() {
        return pokazKategoriePrzycisk;
    }

    public JButton getPokazProducentowPrzycisk() {
        return pokazProducentowPrzycisk;
    }

    public JButton getPokazProduktMagazynPrzycisk() {
        return pokazProduktMagazynPrzycisk;
    }

    public JButton getPokazProduktZamowieniaPrzycisk() {
        return pokazProduktZamowieniaPrzycisk;
    }

    public JButton getPokazStanyZamowienPrzycisk() {
        return pokazStanyZamowienPrzycisk;
    }

    public JButton getPokazTypyUzytkownikaPrzycisk() {
        return pokazTypyUzytkownikaPrzycisk;
    }

    public JButton getKontoPrzycisk() {
        return kontoPrzycisk;
    }

    public JButton getZalozKontoPrzycisk() {
        return zalozKontoPrzycisk;
    }

}
