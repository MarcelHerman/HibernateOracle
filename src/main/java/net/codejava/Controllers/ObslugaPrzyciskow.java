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
import net.codejava.Views.widokAplikacji;
import net.codejava.Controllers.DyrektorOkienek;
import net.codejava.Controllers.DyrektorTabel;
import net.codejava.Models.*;

public class ObslugaPrzyciskow {
    public final widokAplikacji widok;
    private PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
    private DyrektorOkienek dyrektorOkienek = new DyrektorOkienek();
    private final JFrame frame = new JFrame("Elektryka Prad Nie Tyka");
    private JPanel kontener = new JPanel();
    private JMenuBar bar = new JMenuBar();
    private Component glue = Box.createHorizontalGlue();
    
    public ObslugaPrzyciskow(widokAplikacji widok) {
        this.widok = widok;
        inicjalizujPrzyciski();
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
        // Logika dla przycisku "Produkty"
        // ...
    }

    public void wykonajZamowieniaAkcje() {
        // Logika dla przycisku "Zamówienia"
        // ...
    }

    public void wykonajMagazynyAkcje() {
        // Logika dla przycisku "Magazyny"
        // ...
    }

    public void wykonajFakturyAkcje() {
        // Logika dla przycisku "Faktury"
        // ...
    }

    public void wykonajUzytkownicyAkcje() {
        // Logika dla przycisku "Użytkownicy"
        // ...
    }

    public void wykonajWylogujAkcje() {
        // Logika dla przycisku "Wyloguj"
        // ...
    }

    public void wykonajKategorieAkcje() {
        // Logika dla przycisku "Kategorie"
        // ...
    }

    public void wykonajProducentowAkcje() {
        // Logika dla przycisku "Producenci"
        // ...
    }

    public void wykonajProduktMagazynAkcje() {
        // Logika dla przycisku "Produkty w magazynach"
        // ...
    }

    public void wykonajProduktZamowieniaAkcje() {
        // Logika dla przycisku "Produkty w zamówieniach"
        // ...
    }

    public void wykonajStanyZamowienAkcje() {
        // Logika dla przycisku "Stany zamówień"
        // ...
    }

    public void wykonajTypyUzytkownikaAkcje() {
        // Logika dla przycisku "Typy użytkownika"
        // ...
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
