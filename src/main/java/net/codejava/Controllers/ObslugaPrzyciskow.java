package net.codejava.Controllers;

import java.awt.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.service.spi.ServiceException;
import net.codejava.HibernateOracle;
import net.codejava.Views.BudowniczyTabeliSwing;
import net.codejava.Views.widokAplikacji;
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
        widok.getPokazZalogujPrzycisk().addActionListener(e -> wykonajZalogujAkcje());

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
        
        widok.getZalozKontoPrzycisk().addActionListener(e -> wykonajZalozKontoAkcje());
    }

    public void wykonajZalogujAkcje() {
    	dyrektorOkienek.zalogujOkienko();
		int result = JOptionPane.showConfirmDialog(null, dyrektorOkienek.zwrocOkno(), "Podaj login i haslo",
				JOptionPane.OK_CANCEL_OPTION);

		try {
			if (result == JOptionPane.OK_OPTION) {
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				bd.stworzSesjeBD();
				List<Uzytkownicy> uzytkownicy = null;
				try (Session sesja2 = bd.pobierzSesjeBD()) {
					Query<Uzytkownicy> zapytanie = sesja2
							.createQuery("FROM Uzytkownicy order by id_uzytkownika", Uzytkownicy.class);
					uzytkownicy = zapytanie.getResultList();
					bd.zamknijSesjeBD();
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new Exception("Nie udalo polaczyc sie z baza danych");
				}
				for (Uzytkownicy uzytkownik : uzytkownicy) {
					if (pola.get(0).getText().equals(uzytkownik.getLogin())) {
						if (pola.get(1).getText().equals(uzytkownik.getHaslo())
								&& uzytkownik.getCzy_usunieto() == 0) {
							HibernateOracle.idUzytkownika = uzytkownik.getId_uzytkownika();

							bd.stworzSesjeBD();
							Session sesja2 = bd.pobierzSesjeBD();
							Query<Typy_uzytkownika> zapytanie = sesja2.createQuery(
									"FROM Typy_uzytkownika order by id_typu_uzytkownika",
									Typy_uzytkownika.class);
							List<Typy_uzytkownika> typyUzytkownika = zapytanie.getResultList();

							widok.getPokazZalogujPrzycisk().setVisible(false);
							widok.getZalozKontoPrzycisk().setVisible(false);
							bar.remove(widok.getPokazZalogujPrzycisk());
							bar.remove(widok.getZalozKontoPrzycisk());
							bar.remove(glue);

							for (Typy_uzytkownika typ : typyUzytkownika) {
								if (typ.getId_typu_uzytkownika() == uzytkownik.getId_typu_uzytkownika()) {
									HibernateOracle.nazwaTypu = typ.getNazwa();
									break;
								}
							}

							switch (HibernateOracle.nazwaTypu) {
							case "Administrator":
								bar.add(widok.getPokazProduktPrzycisk());
								bar.add(widok.getPokazZamowieniaPrzycisk());
								bar.add(widok.getPokazUzytkownicyPrzycisk());
								bar.add(widok.getPokazKategoriePrzycisk());
								bar.add(widok.getPokazProducentowPrzycisk());
								bar.add(widok.getPokazProduktMagazynPrzycisk());
								bar.add(widok.getPokazProduktZamowieniaPrzycisk());
								bar.add(widok.getPokazStanyZamowienPrzycisk());
								bar.add(widok.getPokazTypyUzytkownikaPrzycisk());
								bar.add(widok.getPokazMagazynyPrzycisk());
								bar.add(widok.getPokazFakturyPrzycisk());
								break;
							case "Pracownik":
								bar.add(widok.getPokazProduktPrzycisk());
								bar.add(widok.getPokazZamowieniaPrzycisk());
								bar.add(widok.getPokazFakturyPrzycisk());
								bar.add(widok.getPokazProduktMagazynPrzycisk());
								break;
							case "Klient":
								bar.add(widok.getPokazProduktPrzycisk());
								bar.add(widok.getPokazZamowieniaPrzycisk());
								bar.add(widok.getPokazFakturyPrzycisk());
							default:
								break;
							}

							bar.add(glue);

							widok.getKontoPrzycisk().setText(uzytkownik.getNazwa_uzytkownika());
							// nazwaUzytkownika.setText(uzytkownik.getNazwa_uzytkownika());

							// bar.add(nazwaUzytkownika);
							bar.add(widok.getKontoPrzycisk());
							bar.add(widok.getPokazWylogujPrzycisk());

							bar.revalidate();
							bar.repaint();

							kontener.removeAll();

							frame.revalidate();
							frame.repaint();
							bd.zamknijSesjeBD();
							break;
						} else {
							throw new Exception("");
						}
					}
				}
				if (HibernateOracle.nazwaTypu.equals("null"))
					throw new Exception("");
			}

		} catch (Exception ex) {
			if (ex instanceof ServiceException)
				JOptionPane.showMessageDialog(null, "Nie udalo polaczyc sie z baza danych. Spróbuj później");
			else
				JOptionPane.showMessageDialog(null, "Podano złe dane logowania.");
		}
		;
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
			List<Obiekt_Do_Polecen> obiekty = null;
			HibernateOracle.nazwaTypu = "null";
			HibernateOracle.koszyk.clear();
			HibernateOracle.idUzytkownika = -1;

			kontener.removeAll();

			bar.removeAll();
			bar.setVisible(true);

			bd.stworzSesjeBD();
			try (Session sesja2 = bd.pobierzSesjeBD()) {
				Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery(
						"FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class);
				obiekty = zapytanie.getResultList();
				bd.zamknijSesjeBD();

				dyrektor.tworzTabeleProdukty(obiekty, budSwing);

				JTable tabSwing = (JTable) dyrektor.pobierzTabele();

				JScrollPane pane = new JScrollPane(tabSwing);

				kontener.add(pane);
				bar.add(glue);
				widok.getPokazZalogujPrzycisk().setVisible(true);
				widok.getZalozKontoPrzycisk().setVisible(true);
				bar.add(widok.getZalozKontoPrzycisk());
				bar.add(widok.getPokazZalogujPrzycisk());

				bar.revalidate();
				bar.repaint();

				frame.revalidate();
				frame.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
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

		kontener.removeAll();

		Uzytkownicy rekord = new Uzytkownicy();
		bd.stworzSesjeBD();

		try (Session sesja2 = bd.pobierzSesjeBD()) {
			rekord = (Uzytkownicy) sesja2.createQuery(
					"select u from Uzytkownicy u where u.id_uzytkownika like " + HibernateOracle.idUzytkownika)
					.uniqueResult();
			bd.zamknijSesjeBD();
		} catch (Exception e) {
			e.printStackTrace();
		}

		JLabel szczegoly = new JLabel("Szczególy");
		JLabel nazwa = new JLabel("Nazwa użytkownika:  " + rekord.getNazwa_uzytkownika());
		JLabel login = new JLabel("Login:  " + rekord.getLogin());
		JLabel haslo = new JLabel("Hasło:  " + "*".repeat(rekord.getHaslo().length()));
		JLabel email = new JLabel("E-mail:  " + rekord.getE_mail());
		JButton edytujPrzycisk = new JButton("Edytuj konto");
		JButton usunPrzycisk = new JButton("Usuń konto");
		JButton zlozZamowieniePrzycisk = new JButton("Złóż zamówienie");
		JButton oproznijKoszykPrzycisk = new JButton("Opróżnij koszyk");

		JPanel jp = new JPanel();

		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

		szczegoly.setAlignmentX(Component.CENTER_ALIGNMENT);
		nazwa.setAlignmentX(Component.CENTER_ALIGNMENT);
		login.setAlignmentX(Component.CENTER_ALIGNMENT);
		haslo.setAlignmentX(Component.CENTER_ALIGNMENT);
		email.setAlignmentX(Component.CENTER_ALIGNMENT);

		edytujPrzycisk.setAlignmentX(Component.CENTER_ALIGNMENT);
		usunPrzycisk.setAlignmentX(Component.CENTER_ALIGNMENT);

		jp.add(szczegoly);
		jp.add(nazwa);
		jp.add(login);
		jp.add(haslo);
		jp.add(email);
		jp.add(edytujPrzycisk);
		jp.add(usunPrzycisk);
		kontener.add(jp);

		if (HibernateOracle.koszyk.size() != 0) {
			dyrektor.tworzTabeleKoszyk(HibernateOracle.koszyk, budSwing);

			JTable tabSwing = (JTable) dyrektor.pobierzTabele();

			JScrollPane pane = new JScrollPane(tabSwing);
			pane.setAlignmentX(Component.CENTER_ALIGNMENT);
			kontener.add(pane);
			zlozZamowieniePrzycisk.setAlignmentX(Component.CENTER_ALIGNMENT);
			oproznijKoszykPrzycisk.setAlignmentX(Component.CENTER_ALIGNMENT);
			kontener.add(zlozZamowieniePrzycisk);
			kontener.add(oproznijKoszykPrzycisk);
		}

		frame.revalidate();
		frame.repaint();

		edytujPrzycisk.addActionListener(e -> wykonajEdytujAkcje());

		usunPrzycisk.addActionListener(e -> wykonajUsunAkcje());

		zlozZamowieniePrzycisk.addActionListener(e -> wykonajZlozZamowienieAkcje());

		oproznijKoszykPrzycisk.addActionListener(e -> wykonajOproznijKoszykAkcje());
    }

    private void wykonajEdytujAkcje() {
		dyrektorOkienek.zalozKontoOkienko();
		int result = JOptionPane.showConfirmDialog(null, dyrektorOkienek.zwrocOkno(),
				"Edytuj użytkownika", JOptionPane.OK_CANCEL_OPTION);
		try {
			if (result == JOptionPane.OK_OPTION) {
				ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
				bd.stworzSesjeBD();
				Session sesja = bd.pobierzSesjeBD();

				Uzytkownicy rekord = (Uzytkownicy) sesja
						.createQuery("select u from Uzytkownicy u where u.id_uzytkownika like "
								+ Integer.toString(HibernateOracle.idUzytkownika))
						.uniqueResult();

				if (!pola.get(0).getText().isEmpty())
					rekord.setNazwa_uzytkownika(pola.get(0).getText());
				if (!pola.get(1).getText().isEmpty())
					rekord.setLogin(pola.get(1).getText());
				if (!pola.get(2).getText().isEmpty())
					rekord.setHaslo(pola.get(2).getText());
				if (!pola.get(3).getText().isEmpty())
					rekord.setE_mail(pola.get(3).getText());

				sesja.update(rekord);

				bd.zamknijSesjeBD();

				widok.getKontoPrzycisk().setText(rekord.getNazwa_uzytkownika());
				widok.getKontoPrzycisk().doClick();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
		}
	
    }
    
    private void wykonajUsunAkcje() {
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Czy na pewno chcesz usunąć dany rekord?"));

		int result = JOptionPane.showConfirmDialog(null, myPanel, "Usuwanie",
				JOptionPane.OK_CANCEL_OPTION);

		try {
			if (result == JOptionPane.OK_OPTION) {
				int idtym = HibernateOracle.idUzytkownika;
				wykonajWylogujAkcje();
				PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
				bd.stworzSesjeBD();
				Session sesja = bd.pobierzSesjeBD();
				Uzytkownicy pr = (Uzytkownicy) sesja
						.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
						.setParameter("id", idtym).uniqueResult();

				pr.setCzy_usunieto(1);
				sesja.update(pr);
			}
		} catch (Exception e) {
			System.out.println("Wystąpił błąd podczas usuwania konta: " + e.toString());
		}

		bd.zamknijSesjeBD();
	
    }
    
    private void wykonajZlozZamowienieAkcje() {
		dyrektorOkienek.zlozZamowienieOkienko();
		JPanel glowneOkno = dyrektorOkienek.zwrocOkno();
		dyrektorOkienek.edytowanieFaktury();
		JPanel drugieOkno = dyrektorOkienek.zwrocOkno();

		int result = JOptionPane.showConfirmDialog(null, glowneOkno, "Złóż zamówienie",
				JOptionPane.OK_CANCEL_OPTION);

		try {
			if (result == JOptionPane.OK_OPTION) {
				//ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
				if (((JTextField)glowneOkno.getComponent(1)).getText().isEmpty() || ((JTextField)glowneOkno.getComponent(4)).getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Nie podano wszystkich danych. Zamówienie nie zostało złożone.");
					return;
				}
				if (((JCheckBox) glowneOkno.getComponent(6)).isSelected() == true) {

					int result2 = JOptionPane.showConfirmDialog(null, drugieOkno, "Złóż fakturę",
							JOptionPane.OK_CANCEL_OPTION);

					if (result2 == JOptionPane.CANCEL_OPTION
							|| ((JTextField) drugieOkno.getComponent(1)).getText().isEmpty()) {
						JOptionPane.showMessageDialog(null,
								"Nie podano wszystkich danych. Faktura nie zostanie dodana.");
						throw new Exception("Zamówienie nie zostało złożone");
					}
				}

				PolaczenieOracle bd = PolaczenieOracle.pobierzInstancje();
				bd.stworzSesjeBD();
				Session sesja = bd.pobierzSesjeBD();

				for (Obiekt_Do_Polecen produkt : HibernateOracle.koszyk) {
					int id = ((Produkt_Koszyk) produkt).getPr().getId_produktu();

					if (sesja.createQuery(
							"select p.czy_usunieto from Produkty p where p.id_produktu = :idP",
							Integer.class).setParameter("idP", id).uniqueResult() == 1 ? true : false)
						throw new Exception(
								"Przykro nam ale jeden z produktów został usunięty z naszej oferty.");

					Query<Integer> zapytanie = sesja.createQuery(
							"select pd.stan_magazynowy from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP",
							Integer.class).setParameter("idP", id);
					List<Integer> stanMagazynow = zapytanie.getResultList();

					int stanMag = 0;

					for (Integer stan : stanMagazynow) {
						if (stanMag + stan > ((Produkt_Koszyk) produkt).getIlosc()) {
							stanMag = ((Produkt_Koszyk) produkt).getIlosc();
						} else {
							stanMag += stan;
						}
					}

					if (((Produkt_Koszyk) produkt).getIlosc() > stanMag)
						throw new Exception("Niestety nie posiadamy takiej ilości produktu "
								+ ((Produkt_Koszyk) produkt).getPr().getNazwa() + " w magazynie."
								+ "Obecny stan: " + Integer.toString(stanMag));
				}

				double koszt = 0;
				for (Obiekt_Do_Polecen produkt : HibernateOracle.koszyk) {
					int id = ((Produkt_Koszyk) produkt).getPr().getId_produktu();

					Query<Integer> zapytanie = sesja.createQuery(
							"select pd.stan_magazynowy from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP",
							Integer.class).setParameter("idP", id);
					List<Integer> stanMagazynow = zapytanie.getResultList();

					int stanMag = 0;

					for (Integer stan : stanMagazynow) {
						if (stanMag + stan > ((Produkt_Koszyk) produkt).getIlosc()) {
							stanMag = ((Produkt_Koszyk) produkt).getIlosc();
						} else {
							stanMag += stan;
						}
					}

					if (((Produkt_Koszyk) produkt).getIlosc() > stanMag)
						throw new Exception("Niestety nie posiadamy takiej ilości produktu "
								+ ((Produkt_Koszyk) produkt).getPr().getNazwa() + " w magazynie."
								+ "Obecny stan: " + Integer.toString(stanMag));

					koszt += ((Produkt_Koszyk) produkt).getPr().getCena()
							* ((Produkt_Koszyk) produkt).getIlosc();
				}

				IZamowienia zamowienie = new Zamowienia(koszt, ((JTextField)glowneOkno.getComponent(1)).getText(),
						((JTextField)glowneOkno.getComponent(4)).getText(), 1, HibernateOracle.idUzytkownika, "");
				if (!((JTextField)glowneOkno.getComponent(11)).getText().isEmpty())
					zamowienie = new Znizka(zamowienie, Double.parseDouble(((JTextField)glowneOkno.getComponent(11)).getText()));
				
				if (((JCheckBox) glowneOkno.getComponent(8)).isSelected() == true)
					zamowienie = new Opakowanie(zamowienie);
				
				if (!((JTextField)glowneOkno.getComponent(14)).getText().isEmpty()) {
					if (((JTextField)glowneOkno.getComponent(14)).getText().length() < 150)
						zamowienie = new Notatka(zamowienie, ((JTextField)glowneOkno.getComponent(14)).getText());
					else
						throw (new Exception("Podano zbyt długą notatkę - max 150 znaków."));
				}

				zamowienie = new Zamowienia(zamowienie.getKoszt(), ((JTextField)glowneOkno.getComponent(1)).getText(),
						((JTextField)glowneOkno.getComponent(4)).getText(), 1, HibernateOracle.idUzytkownika, zamowienie.getOpis());

				double zaokr = Math.round(zamowienie.getKoszt() * 100.0) / 100.0;
				((Zamowienia) zamowienie).setKoszt(zaokr);

				sesja.save(zamowienie);

				for (Obiekt_Do_Polecen odp : HibernateOracle.koszyk) {
					int id = ((Produkt_Koszyk) odp).getPr().getId_produktu();

					Query<Produkt_Magazyn> zapytanie = sesja.createQuery(
							"select pd from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP",
							Produkt_Magazyn.class).setParameter("idP", id);
					List<Produkt_Magazyn> stanMagazynow = zapytanie.getResultList();

					int iloscProdKoszyk = ((Produkt_Koszyk) odp).getIlosc();

					for (Produkt_Magazyn stan : stanMagazynow) {
						if (stan.getStan_magazynowy() >= ((Produkt_Koszyk) odp).getIlosc()) {
							stan.setStan_magazynowy(
									stan.getStan_magazynowy() - ((Produkt_Koszyk) odp).getIlosc());
						} else {
							((Produkt_Koszyk) odp).setIlosc(
									((Produkt_Koszyk) odp).getIlosc() - stan.getStan_magazynowy());
							stan.setStan_magazynowy(0);
						}
						sesja.update(stan);
					}

					sesja.save(new Produkt_Zamowienia(
							new Produkt_Zamowienia_Id(((Zamowienia) zamowienie).getId_zamowienia(),
									(((Produkt_Koszyk) odp).getPr().getId_produktu())),
							iloscProdKoszyk));
				}
				if (!(((JTextField) drugieOkno.getComponent(1)).getText().isEmpty()))
					sesja.save(new Faktury(LocalDate.now(),
							((JTextField) drugieOkno.getComponent(1)).getText(),
							((Zamowienia) zamowienie).getId_zamowienia()));
				bd.zamknijSesjeBD();

				HibernateOracle.koszyk.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Nie udało się złożyć zamówienia. Błąd: " + e.getMessage());
		}
    }
    
    private void wykonajOproznijKoszykAkcje() {
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Czy na pewno chcesz opróżnić cały koszyk?"));
		int result = JOptionPane.showConfirmDialog(null, myPanel, "Opróżnianie koszyka",
				JOptionPane.OK_CANCEL_OPTION);
		try {
			if (result == JOptionPane.OK_OPTION) {
				HibernateOracle.koszyk.clear();
				widok.getKontoPrzycisk().doClick();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Nie udało się opróżnić koszyka. Błąd: " + e.getMessage());
		}
	
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
}