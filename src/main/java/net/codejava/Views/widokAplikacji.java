package net.codejava.Views;

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
import net.codejava.Controllers.DyrektorOkienek;
import net.codejava.Controllers.DyrektorTabel;
import net.codejava.Models.Faktury;
import net.codejava.Models.IZamowienia;
import net.codejava.Models.Kategorie;
import net.codejava.Models.Magazyny;
import net.codejava.Models.Notatka;
import net.codejava.Models.Obiekt_Do_Polecen;
import net.codejava.Models.Opakowanie;
import net.codejava.Models.PolaczenieOracle;
import net.codejava.Models.PolaczenieProxy;
import net.codejava.Models.Producenci;
import net.codejava.Models.Produkt_Koszyk;
import net.codejava.Models.Produkt_Magazyn;
import net.codejava.Models.Produkt_Zamowienia;
import net.codejava.Models.Produkt_Zamowienia_Id;
import net.codejava.Models.Produkty;
import net.codejava.Models.Repozytorium_Polecen;
import net.codejava.Models.Stany_Zamowienia;
import net.codejava.Models.Typy_uzytkownika;
import net.codejava.Models.Uzytkownicy;
import net.codejava.Models.Zamowienia;
import net.codejava.Models.Znizka;

public class widokAplikacji {
	
	public void Inicjalizuj() {
		
		PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();
		
		bd.stworzSesjeBD();
		HibernateOracle.cache = new HashMap<>();
		
		Session sesja = bd.pobierzSesjeBD();		
		
		try
		{
			sesja = bd.pobierzSesjeBD();
		}catch(Exception e)
		{
            System.out.println("Blad dodania tablicy: " + e.toString());
		}
		
		List<Obiekt_Do_Polecen> obiekty = null;
		
		try (Session sesja2 = bd.pobierzSesjeBD()) {
            Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class);
            obiekty = zapytanie.getResultList();
            bd.zamknijSesjeBD();
        } catch (Exception e) {
            e.printStackTrace();
        }
		//bd.zamknijSesjeBD();			 
			
		final JFrame frame = new JFrame("Elektryka Prad Nie Tyka");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		
		frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!HibernateOracle.nazwaTypu.equals("Klient"))
                {
                	HibernateOracle.repoPolecen.zapiszDoPliku();
                }            	
            }
        });
		
		JPanel kontener = new JPanel();
		frame.setContentPane(kontener);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        JMenuBar bar = new JMenuBar();
        
		JButton pokazZalogujPrzycisk = new JButton("Zaloguj się");
		JButton pokazProduktPrzycisk = new JButton("Produkty");
		JButton pokazZamowieniaPrzycisk = new JButton("Zamówienia");
		JButton pokazMagazynyPrzycisk = new JButton("Magazyny");
		JButton pokazFakturyPrzycisk = new JButton("Faktury");
		JButton pokazUzytkownicyPrzycisk = new JButton("Użytkownicy");
		JButton pokazWylogujPrzycisk = new JButton("Wyloguj");
		JButton pokazKategoriePrzycisk = new JButton("Kategorie");
		JButton pokazProducentowPrzycisk = new JButton("Producenci");
		JButton pokazProduktMagazynPrzycisk = new JButton("Produkty w magazynach");
		JButton pokazProduktZamowieniaPrzycisk = new JButton("Produkty w zamowieniach");
		JButton pokazStanyZamowienPrzycisk = new JButton("Stany zamówień");
		JButton pokazTypyUzytkownikaPrzycisk = new JButton("Typy użytkownika");
		JButton kontoPrzycisk = new JButton(" ");
		JButton dodajPrzycisk = new JButton("Dodaj rekord");
		JButton zalozKontoPrzycisk = new JButton("Zalóż konto");
		JButton eksportujDoDruku = new JButton("Drukuj");
		
		Component glue = Box.createHorizontalGlue();
		bar.add(glue);
		bar.add(zalozKontoPrzycisk);
		bar.add(pokazZalogujPrzycisk);
		
		BudowniczyTabeliDruk budDruk = new BudowniczyTabeliDruk();
		BudowniczyTabeliSwing budSwing = new BudowniczyTabeliSwing();		 
		DyrektorTabel dyrektor = new DyrektorTabel();
		DyrektorOkienek dyrektorOkienek = new DyrektorOkienek();
		
		//budSwing.tworzTabeleProdukty(obiekty);
		dyrektor.tworzTabeleProdukty(obiekty, budSwing);
		
        JTable tabSwing = (JTable)dyrektor.pobierzTabele();
        
        JScrollPane pane = new JScrollPane(tabSwing);
        
        kontener.add(pane);
        	
		frame.setJMenuBar(bar);
	
		// Ustaw preferowany rozmiar dla JFrame
		frame.setPreferredSize(new Dimension(1400, 800));

		// Spakuj ramkę
		frame.pack();

        frame.setVisible(true);
		
		 pokazZalogujPrzycisk.addActionListener(new ActionListener() {

	            public void actionPerformed(ActionEvent e) {
	            	
	            	dyrektorOkienek.zalogujOkienko();
	                int result = JOptionPane.showConfirmDialog(null, dyrektorOkienek.zwrocOkno(), 
	                         "Podaj login i haslo", JOptionPane.OK_CANCEL_OPTION);

	                
	                try {
	                	if (result == JOptionPane.OK_OPTION) {
	                		ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
	                		bd.stworzSesjeBD();
	                		List<Uzytkownicy> uzytkownicy = null;
	                		try (Session sesja2 = bd.pobierzSesjeBD()) {
	                            Query<Uzytkownicy> zapytanie = sesja2.createQuery("FROM Uzytkownicy order by id_uzytkownika", Uzytkownicy.class);
	                            uzytkownicy = zapytanie.getResultList();
	                            bd.zamknijSesjeBD();
	                        } catch (Exception e1) {
	                            e1.printStackTrace();
	                            throw new Exception("Nie udalo polaczyc sie z baza danych");
	                        }
	                		for(Uzytkownicy uzytkownik: uzytkownicy) {
			                	if(pola.get(0).getText().equals(uzytkownik.getLogin())) {
			                		if(pola.get(1).getText().equals(uzytkownik.getHaslo()) && uzytkownik.getCzy_usunieto()==0)
			                		{				                		
				                		HibernateOracle.idUzytkownika = uzytkownik.getId_uzytkownika();
				                		
				                		bd.stworzSesjeBD();
				                		Session sesja2 = bd.pobierzSesjeBD();
				                		Query<Typy_uzytkownika> zapytanie = sesja2.createQuery("FROM Typy_uzytkownika order by id_typu_uzytkownika", Typy_uzytkownika.class);
			                            List<Typy_uzytkownika> typyUzytkownika = zapytanie.getResultList();                           
			                                
			                            pokazZalogujPrzycisk.setVisible(false);
			                            zalozKontoPrzycisk.setVisible(false);
				                		bar.remove(pokazZalogujPrzycisk);
				                		bar.remove(zalozKontoPrzycisk);
				                		bar.remove(glue);
			                            
			                            for(Typy_uzytkownika typ: typyUzytkownika) {
			                            	if(typ.getId_typu_uzytkownika() == uzytkownik.getId_typu_uzytkownika()) {
			                            		HibernateOracle.nazwaTypu = typ.getNazwa();
			                            		break;
			                            	}
			                            }
				                		
				                		switch(HibernateOracle.nazwaTypu) {
				                			case "Administrator":
						                		bar.add(pokazProduktPrzycisk);
						                		bar.add(pokazZamowieniaPrzycisk);
						                		bar.add(pokazUzytkownicyPrzycisk);
						                		bar.add(pokazKategoriePrzycisk);
						                		bar.add(pokazProducentowPrzycisk);
						                		bar.add(pokazProduktMagazynPrzycisk);
						                		bar.add(pokazProduktZamowieniaPrzycisk);
						                		bar.add(pokazStanyZamowienPrzycisk);
						                		bar.add(pokazTypyUzytkownikaPrzycisk);
						                		bar.add(pokazMagazynyPrzycisk);
						                		bar.add(pokazFakturyPrzycisk);
				                				break;
				                			case "Pracownik":
				                				bar.add(pokazProduktPrzycisk);
						                		bar.add(pokazZamowieniaPrzycisk);
				                				bar.add(pokazFakturyPrzycisk);
						                		bar.add(pokazProduktMagazynPrzycisk);
				                				break;
				                			case "Klient":
				                				bar.add(pokazProduktPrzycisk);
				                				bar.add(pokazZamowieniaPrzycisk);
				                				bar.add(pokazFakturyPrzycisk);
				                			default:
				                				break;
				                		}
				                		
				                		bar.add(glue);
				                		
				                		kontoPrzycisk.setText(uzytkownik.getNazwa_uzytkownika());				                		
				                		//nazwaUzytkownika.setText(uzytkownik.getNazwa_uzytkownika());				         
				                		
				                		//bar.add(nazwaUzytkownika);
				                		bar.add(kontoPrzycisk);
				                		bar.add(pokazWylogujPrzycisk);	
				                		
				                		bar.revalidate();
				                		bar.repaint();
				                		
				                		kontener.removeAll();
				                		
				                		frame.revalidate();
				                		frame.repaint();
				                		bd.zamknijSesjeBD();
				                		break;
			                		}
			                		else
			                		{
			                			throw new Exception("");
			                		}
			                	}
	                		}
	                		if(HibernateOracle.nazwaTypu.equals("null")) throw new Exception("");
		                }
	                    
	                } catch (Exception ex) {
	                	if(ex instanceof ServiceException)
	                		JOptionPane.showMessageDialog(null, "Nie udalo polaczyc sie z baza danych. Spróbuj później");
	                	else
	                		JOptionPane.showMessageDialog(null, "Podano złe dane logowania.");
	                }
	                ;
	            }
	        });
		 
		 ActionListener akcjaWylogowania = new ActionListener() {
			 
			 public void actionPerformed(ActionEvent a)
			 {
				 List<Obiekt_Do_Polecen> obiekty = null;
				 HibernateOracle.nazwaTypu = "null";
				 HibernateOracle.koszyk.clear();
				 HibernateOracle.idUzytkownika = -1;
				 
				 kontener.removeAll();
				 
				 bar.removeAll();
				 bar.setVisible(true);

				 bd.stworzSesjeBD();
				 try (Session sesja2 = bd.pobierzSesjeBD()) {
			            Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class);
			            obiekty = zapytanie.getResultList();
			            bd.zamknijSesjeBD();
			        							       
			     dyrektor.tworzTabeleProdukty(obiekty, budSwing);
			     
			     JTable tabSwing = (JTable)dyrektor.pobierzTabele();
			        
			     JScrollPane pane = new JScrollPane(tabSwing);
			               
			     kontener.add(pane);
			     bar.add(glue);
				 pokazZalogujPrzycisk.setVisible(true);
				 zalozKontoPrzycisk.setVisible(true);				 
				 bar.add(zalozKontoPrzycisk);
				 bar.add(pokazZalogujPrzycisk);

				 
				 bar.revalidate();
				 bar.repaint();
				
				 
				 frame.revalidate();
				 frame.repaint();
				 }
				 catch (Exception e) {
			            e.printStackTrace();
			        }
				 
			 }
		 };
		 
		 pokazWylogujPrzycisk.addActionListener(akcjaWylogowania);
		 
		 kontoPrzycisk.addActionListener(new  ActionListener() {
			 
			 @Override
				public void actionPerformed(ActionEvent a) {				 	
				 	kontener.removeAll();

					Uzytkownicy rekord = new Uzytkownicy();
					bd.stworzSesjeBD();
					
					try (Session sesja2 = bd.pobierzSesjeBD()) {
						rekord = (Uzytkownicy)sesja2.createQuery("select u from Uzytkownicy u where u.id_uzytkownika like "+ HibernateOracle.idUzytkownika)
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
					JButton oproznijkoszykPrzycisk = new JButton("Opróżnij koszyk");
					
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
										
					if(HibernateOracle.koszyk.size() != 0) {
						dyrektor.tworzTabeleKoszyk(HibernateOracle.koszyk, budSwing);
						
						 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
						 
						 JScrollPane pane = new JScrollPane(tabSwing);
						 pane.setAlignmentX(Component.CENTER_ALIGNMENT);
					       kontener.add(pane);
					       zlozZamowieniePrzycisk.setAlignmentX(Component.CENTER_ALIGNMENT);
					       oproznijkoszykPrzycisk.setAlignmentX(Component.CENTER_ALIGNMENT);
					       kontener.add(zlozZamowieniePrzycisk);
					       kontener.add(oproznijkoszykPrzycisk);
					}
				 
					 frame.revalidate();
					 frame.repaint();
					 
					 edytujPrzycisk.addActionListener(new ActionListener() 
					 {
						 @Override
							public void actionPerformed(ActionEvent a) {	
							 
							 dyrektorOkienek.zalozKontoOkienko();
		                		int result = JOptionPane.showConfirmDialog(null, dyrektorOkienek.zwrocOkno(), 
		   	                         "Edytuj użytkownika", JOptionPane.OK_CANCEL_OPTION);
		                		 try {
		     	                	if (result == JOptionPane.OK_OPTION) {
		     	                		ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
		     	                		PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();
		     	 	                	bd.stworzSesjeBD();
		     	 	                	Session sesja = bd.pobierzSesjeBD();
		     	                		
		     	 	                	Uzytkownicy rekord = (Uzytkownicy)sesja.createQuery("select u from Uzytkownicy u where u.id_uzytkownika like "+Integer.toString(HibernateOracle.idUzytkownika))
		     	 	                			.uniqueResult();
		     	 	                	
		     	 	                	if(!pola.get(0).getText().isEmpty())
		     	 	                		rekord.setNazwa_uzytkownika(pola.get(0).getText());
		     	 	                	if(!pola.get(1).getText().isEmpty())
		     	 	                		rekord.setLogin(pola.get(1).getText());
		     	 	                	if(!pola.get(2).getText().isEmpty())
		     	 	                		rekord.setHaslo(pola.get(2).getText());
		     	 	                	if(!pola.get(3).getText().isEmpty())
		     	 	                		rekord.setE_mail(pola.get(3).getText());
		     	 	                	
		     	                		sesja.update(rekord);
		     	                		
		     	                		bd.zamknijSesjeBD();
		     	                		
		     	                		kontoPrzycisk.setText(rekord.getNazwa_uzytkownika());
		     	                		kontoPrzycisk.doClick();
		     	                	}
		                		 }
		                		 catch(Exception e) {
		                			 e.printStackTrace();
		                			 JOptionPane.showMessageDialog(null, "Nie udało się edytować użytkownika. Błąd: " + e.getMessage());
		                		 }
						 }
					 });
					 
					 usunPrzycisk.addActionListener(new ActionListener() 
					 {
						 @Override
							public void actionPerformed(ActionEvent a) {
							 
							 	JPanel myPanel = new JPanel();					
			 	                myPanel.add(new JLabel("Czy na pewno chcesz usunąć dany rekord?"));
			 	                
			 	                int result = JOptionPane.showConfirmDialog(null, myPanel, 
				                         "Usuwanie", JOptionPane.OK_CANCEL_OPTION);
			                			
				 	            try {
				 	            	 if (result == JOptionPane.OK_OPTION) {		
				 	            		 int idtym = HibernateOracle.idUzytkownika;
				 	            		 akcjaWylogowania.actionPerformed(a);		 	            		 
				 	            		 PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();
				 	            		 bd.stworzSesjeBD();	                			
				 	            		 Session sesja = bd.pobierzSesjeBD();
				 	            		Uzytkownicy pr = (Uzytkownicy)sesja.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
			 	 	                			.setParameter("id", idtym)
			 	 	                			.uniqueResult();
			 	                		
				 	            		pr.setCzy_usunieto(1);
				 	 	                sesja.update(pr);
				 	            	 }
				 	            }catch(Exception e)
				 	            	 {
				 	            	System.out.println("Wystapil blad podczas usuwania konta: "+ e.toString());
				 	            		 
				 	            }
		 	 	 	                
				 	            	bd.zamknijSesjeBD();							 		 	 	 	                	 	 	 	                	 	 	                
						 }	 	 	 	             
					 });
					 
					 zlozZamowieniePrzycisk.addActionListener(new ActionListener() 
					 {
						 @Override
							public void actionPerformed(ActionEvent a) {		
							 
							 dyrektorOkienek.zlozZamowienieOkienko();
		                		JPanel glowneOkno = dyrektorOkienek.zwrocOkno();
		                		dyrektorOkienek.edytowanieFaktury();
		                		JPanel drugieOkno = dyrektorOkienek.zwrocOkno();
		                		
		                		int result = JOptionPane.showConfirmDialog(null, glowneOkno, 
		   	                         "Złóż zamówienie", JOptionPane.OK_CANCEL_OPTION);
		                				                		
		                		 try {	
		                			 if (result == JOptionPane.OK_OPTION) {
		                				 ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
		                				 if(pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty())
					 	                	{
					 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Zamówienie nie zostało złożone.");
					 	                		return;
					 	                	}
		                				 if(((JCheckBox)glowneOkno.getComponent(7)).isSelected()==true)
		                				 {
		                					 
		        		                	 int result2 = JOptionPane.showConfirmDialog(null, drugieOkno, 
			    		   	                         "Złóż fakturę", JOptionPane.OK_CANCEL_OPTION);
			                				 
			                				 if (result2 == JOptionPane.CANCEL_OPTION || ((JTextField)drugieOkno.getComponent(2)).getText().isEmpty()) {
			                					 JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Faktura nie zostanie dodana.");
			                					 throw new Exception("Zamówienie nie zostało złożone");
			                				 }
		                				 }
		                				 
		     	                		PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();
		     	 	                	bd.stworzSesjeBD();
		     	 	                	Session sesja = bd.pobierzSesjeBD();
		     	 	                			     	 	                	
		     	 	                	for(Obiekt_Do_Polecen produkt: HibernateOracle.koszyk) {
			     	 	                		int id = ((Produkt_Koszyk)produkt).getPr().getId_produktu();
			     	 	                	
			     	 	                	if(sesja.createQuery("select p.czy_usunieto from Produkty p where p.id_produktu = :idP", Integer.class)
		     		 	                			.setParameter("idP", id)
		     		 	                			.uniqueResult()==1? true:false)throw new Exception("Przykro nam ale jeden z produktów został usunięty z naszej oferty.");
			     	 	                	
			     		                	Query<Integer> zapytanie = sesja.createQuery("select pd.stan_magazynowy from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP", Integer.class)
		     		 	                			.setParameter("idP", id);
			     		                	List<Integer> stanMagazynow = zapytanie.getResultList();
			     		                	
			     		                	int stanMag = 0;
			     		                	
			     		                	for(Integer stan: stanMagazynow) {
			     		                		if(stanMag + stan > ((Produkt_Koszyk)produkt).getIlosc() )
			     		                		{
			     		                			stanMag = ((Produkt_Koszyk)produkt).getIlosc();
			     		                		}else
			     		                		{
			     		                			stanMag += stan;
			     		                		}
			     		                	}
			     		                	
			     		                	if(((Produkt_Koszyk)produkt).getIlosc() > stanMag)
			     		                		throw new Exception("Niestety nie posiadamy takiej ilości produktu "
			     		                				+ ((Produkt_Koszyk)produkt).getPr().getNazwa()
			     		                				+ " w magazynie."
			     		                				+ "Obecny stan: " +Integer.toString(stanMag));
		     	 	                	}
		     	                		
		     	 	                	double koszt = 0;
		     	 	                	for(Obiekt_Do_Polecen produkt : HibernateOracle.koszyk)
		     	 	                	{
		     	 	                		int id = ((Produkt_Koszyk)produkt).getPr().getId_produktu();
			     	 	                	
			     	 	                	
			     		                	Query<Integer> zapytanie = sesja.createQuery("select pd.stan_magazynowy from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP", Integer.class)
		     		 	                			.setParameter("idP", id);
			     		                	List<Integer> stanMagazynow = zapytanie.getResultList();
			     		                	
			     		                	int stanMag = 0;
			     		                	
			     		                	for(Integer stan: stanMagazynow) {
			     		                		if(stanMag + stan > ((Produkt_Koszyk)produkt).getIlosc() )
			     		                		{
			     		                			stanMag = ((Produkt_Koszyk)produkt).getIlosc();
			     		                		}else
			     		                		{
			     		                			stanMag += stan;
			     		                		}
			     		                	}
			     		                	
			     		                	if(((Produkt_Koszyk)produkt).getIlosc() > stanMag)
			     		                		throw new Exception("Niestety nie posiadamy takiej ilości produktu "
			     		                				+ ((Produkt_Koszyk)produkt).getPr().getNazwa()
			     		                				+ " w magazynie."
			     		                				+ "Obecny stan: " +Integer.toString(stanMag));
		     	 	                		
		    	 	                		koszt+=((Produkt_Koszyk) produkt).getPr().getCena() *((Produkt_Koszyk) produkt).getIlosc() ;
		     	 	                	}
		     		     	 	                	
		     	 	                	IZamowienia zamowienie = new Zamowienia(koszt, pola.get(0).getText(), pola.get(1).getText(), 1, HibernateOracle.idUzytkownika,"");
		     	 	                	if(!pola.get(2).getText().isEmpty())zamowienie = new Znizka(zamowienie, Double.parseDouble(pola.get(2).getText()));		     	 	                		     	 	                	
		     	 	                			     	 	               
		     	 	                	if(((JCheckBox)glowneOkno.getComponent(9)).isSelected()==true)zamowienie = new Opakowanie(zamowienie);
		     	 	                	
		     	 	                	if(!pola.get(3).getText().isEmpty()) {
		     	 	                		if(pola.get(3).getText().length() < 150)zamowienie = new Notatka(zamowienie, pola.get(3).getText());
		     	 	                		else throw(new Exception("Podano zbyt długą notatkę - max 150 znaków."));
		     	 	                	}
		     	 	                	
		     	 	                				     	 	                	     	 	                	
		     	 	                	zamowienie = new Zamowienia(zamowienie.getKoszt(), pola.get(0).getText(), pola.get(1).getText(), 1, HibernateOracle.idUzytkownika, zamowienie.getOpis());
		     	 	                	
		     	 	                	double zaokr = Math.round(zamowienie.getKoszt()*100.0)/100.0;
		     	 	                	((Zamowienia) zamowienie).setKoszt(zaokr);
		     	 	                	
		     	 	                	sesja.save(zamowienie);	
		     	 	                	
		     	 	                	for(Obiekt_Do_Polecen odp : HibernateOracle.koszyk)		 
		     	 	                	{
		     	 	                		int id = ((Produkt_Koszyk)odp).getPr().getId_produktu();
		     	 	                		
					     	 	            Query<Produkt_Magazyn> zapytanie = sesja.createQuery("select pd from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP", Produkt_Magazyn.class)
			 		 	                			.setParameter("idP", id);
			     		                	List<Produkt_Magazyn> stanMagazynow = zapytanie.getResultList();
			     		                	
			     		                	int iloscProdKoszyk = ((Produkt_Koszyk)odp).getIlosc();
			     		                	
			     		                	for(Produkt_Magazyn stan: stanMagazynow) {
			     		                		if(stan.getStan_magazynowy() >= ((Produkt_Koszyk)odp).getIlosc() )
			     		                		{	
			     		                			stan.setStan_magazynowy(stan.getStan_magazynowy() - ((Produkt_Koszyk)odp).getIlosc());
			     		                		}else
			     		                		{
			     		                			((Produkt_Koszyk)odp).setIlosc(((Produkt_Koszyk)odp).getIlosc() - stan.getStan_magazynowy());
			     		                			stan.setStan_magazynowy(0);
			     		                		}
			     		                		sesja.update(stan);
			     		                	}
		     	 	                		
		     	 	                		sesja.save(new Produkt_Zamowienia(new Produkt_Zamowienia_Id(((Zamowienia) zamowienie).getId_zamowienia(), (((Produkt_Koszyk)odp).getPr().getId_produktu())), iloscProdKoszyk));
		     	 	                	}
		     	 	                	if(!(((JTextField)drugieOkno.getComponent(2)).getText().isEmpty()))	sesja.save(new Faktury(LocalDate.now(), ((JTextField)drugieOkno.getComponent(2)).getText(), ((Zamowienia) zamowienie).getId_zamowienia()));  
		     	                		bd.zamknijSesjeBD();
		     	                		
		     	                		HibernateOracle.koszyk.clear();
		                			 }
		                		 }
		                		 catch(Exception e) {
		                			 e.printStackTrace();
		                			 JOptionPane.showMessageDialog(null, "Nie udało się złożyć zamówienia. Błąd: " + e.getMessage());
		                		 }	 
						 }
					 });
					 
					 oproznijkoszykPrzycisk.addActionListener(new ActionListener() 
					 {
						 @Override
							public void actionPerformed(ActionEvent a) {							 
		                			
							 JPanel myPanel = new JPanel();
							 myPanel.add(new JLabel("Czy na pewno chcesz opróżnić cały koszyk?"));
							 int result = JOptionPane.showConfirmDialog(null, myPanel, 
		   	                         "Opróżnianie koszyka", JOptionPane.OK_CANCEL_OPTION);
		                		 try {
		                			 if (result == JOptionPane.OK_OPTION) {
		                				 HibernateOracle.koszyk.clear();
		     	                		kontoPrzycisk.doClick();
		                			 }
		                		 } catch(Exception e) {
		                			 e.printStackTrace();
		                			 JOptionPane.showMessageDialog(null, "Nie udało się opróżnić koszyka. Błąd: " + e.getMessage());
		                		 }	 
						 }
					 });
					 
				}
		 });
		 
		 zalozKontoPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 dyrektorOkienek.zalozKontoOkienko();
				 
 	                int result = JOptionPane.showConfirmDialog(null, dyrektorOkienek.zwrocOkno(), 
	                         "Zakładanie konta", JOptionPane.OK_CANCEL_OPTION);
                			
	 	            try {
	 	            	 if (result == JOptionPane.OK_OPTION) {
	 	            		ArrayList<JTextField> pola = dyrektorOkienek.zwrocPolaTekstowe();
	 	            		if(pola.get(0).getText().isEmpty() || pola.get(1).getText().isEmpty() || pola.get(2).getText().isEmpty() || pola.get(3).getText().isEmpty())
	 	            			throw(new Exception("Nie podano wszystkich danych. Konto nie zostało utworzone."));
	 	                		 	                		 	                	
	 	                	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();
	 	                	bd.stworzSesjeBD();
	 	                	Session sesja = bd.pobierzSesjeBD();
	 	                	
	 	                	sesja.save(new Uzytkownicy(pola.get(0).getText(), pola.get(1).getText(), pola.get(2).getText(), pola.get(3).getText(), 3, 0));	 
	 	                	bd.zamknijSesjeBD();               		
	 	            	 }
	 	            } catch(Exception e)
	 	            	 {
	 	            	JOptionPane.showMessageDialog(null, "Wystapil bład podczas dodawania konta: " + e.getMessage());	 	            		 
	 	            }	 	 	                
	 	            	
			 }	 	 	 	             
		 });
		 
        pokazProduktPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {				 	
				 	kontener.removeAll();			 	
				 	 PolaczenieProxy pc = new PolaczenieProxy();
					 pc.zamknijSesjeBD();

					List<Obiekt_Do_Polecen> obiekty = null;
					bd.stworzSesjeBD();
					
					try (Session sesja2 = bd.pobierzSesjeBD()) {
						
			            Query<Obiekt_Do_Polecen> zapytanie = null;
			            if((HibernateOracle.nazwaTypu.equals("Klient")))zapytanie = sesja2.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class); 
			            else zapytanie = sesja2.createQuery("FROM Produkty order by id_produktu", Obiekt_Do_Polecen.class);
			            obiekty = zapytanie.getResultList();
			            bd.zamknijSesjeBD();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					dyrektor.tworzTabeleProdukty(obiekty, budSwing);
					
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 
					 if(!(HibernateOracle.nazwaTypu.equals("Klient")))
					 {
						 kontener.add(dodajPrzycisk);	
						 kontener.add(eksportujDoDruku);
					 }
					 frame.revalidate();
					 frame.repaint();										
				}
		 });
        
        pokazZamowieniaPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();	
				 	PolaczenieProxy pc = new PolaczenieProxy();
					pc.zamknijSesjeBD();

					List<Obiekt_Do_Polecen> obiekty = null;
					bd.stworzSesjeBD();
					
					try (Session sesja2 = bd.pobierzSesjeBD()) {
			            Query<Obiekt_Do_Polecen> zapytanie = null;
			            if(HibernateOracle.nazwaTypu.equals("Klient"))zapytanie = sesja2.createQuery("FROM Zamowienia z where z.uzytkownicy_id_uzytkownika = :id order by z.id_zamowienia", Obiekt_Do_Polecen.class).setParameter("id", HibernateOracle.idUzytkownika);
			            else zapytanie = sesja2.createQuery("FROM Zamowienia order by id_zamowienia", Obiekt_Do_Polecen.class);
			            obiekty = zapytanie.getResultList();
			            bd.zamknijSesjeBD();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					dyrektor.tworzTabeleZamowienia(obiekty, budSwing);
					
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 if(!(HibernateOracle.nazwaTypu.equals("Klient"))){
						 kontener.add(dodajPrzycisk);	
						 kontener.add(eksportujDoDruku);
					 }					 
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazUzytkownicyPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();	
				 	PolaczenieProxy pc = new PolaczenieProxy();
					pc.zamknijSesjeBD();

					List<Obiekt_Do_Polecen> obiekty = null;
					bd.stworzSesjeBD();
					
					try (Session sesja2 = bd.pobierzSesjeBD()) {
			            Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Uzytkownicy order by id_uzytkownika", Obiekt_Do_Polecen.class);
			            obiekty = zapytanie.getResultList();
			            bd.zamknijSesjeBD();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					dyrektor.tworzTabeleUzytkownicy(obiekty, budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 kontener.add(dodajPrzycisk);	
					 kontener.add(eksportujDoDruku);					 
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazKategoriePrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();	
				 	PolaczenieProxy pc = new PolaczenieProxy();
					pc.zamknijSesjeBD();

					if(!HibernateOracle.cache.containsKey("Kategorie")) {
						bd.stworzSesjeBD();						
						try (Session sesja2 = bd.pobierzSesjeBD()) {
							Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Kategorie order by id_kategorii", Obiekt_Do_Polecen.class);
							HibernateOracle.cache.put("Kategorie", zapytanie.getResultList());
							bd.zamknijSesjeBD();
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}
					
					dyrektor.tworzTabeleKategorie(HibernateOracle.cache.get("Kategorie"), budSwing);
					
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 kontener.add(dodajPrzycisk);
					 kontener.add(eksportujDoDruku);
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazProducentowPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();
				 	PolaczenieProxy pc = new PolaczenieProxy();
					pc.zamknijSesjeBD();

				 	if(!HibernateOracle.cache.containsKey("Producenci")) {
						bd.stworzSesjeBD();
						try (Session sesja2 = bd.pobierzSesjeBD()) {
				            Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Producenci order by id_producenta", Obiekt_Do_Polecen.class);
				            HibernateOracle.cache.put("Producenci", zapytanie.getResultList());
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				 	}
					
					dyrektor.tworzTabeleProducenci(HibernateOracle.cache.get("Producenci"), budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 kontener.add(dodajPrzycisk);
					 kontener.add(eksportujDoDruku);
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazProduktMagazynPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();
				 	PolaczenieProxy pc = new PolaczenieProxy();
					pc.zamknijSesjeBD();

					List<Obiekt_Do_Polecen> obiekty = null;
					bd.stworzSesjeBD();
					
					try (Session sesja2 = bd.pobierzSesjeBD()) {
			            Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Produkt_Magazyn order by produkt_magazyn_id.magazyny_id_magazynu, produkt_magazyn_id.produkty_id_produktu", Obiekt_Do_Polecen.class);
			            obiekty = zapytanie.getResultList();
			            bd.zamknijSesjeBD();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					dyrektor.tworzTabeleProdukt_Magazyn(obiekty, budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 kontener.add(dodajPrzycisk);
					 kontener.add(eksportujDoDruku);
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazProduktZamowieniaPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();
				 	PolaczenieProxy pc = new PolaczenieProxy();
					pc.zamknijSesjeBD();

					List<Obiekt_Do_Polecen> obiekty = null;
					bd.stworzSesjeBD();
					
					try (Session sesja2 = bd.pobierzSesjeBD()) {
			            Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Produkt_Zamowienia order by produkt_zamowienia_id.id_zamowienia, produkt_zamowienia_id.id_produktu", Obiekt_Do_Polecen.class);
			            obiekty = zapytanie.getResultList();
			            bd.zamknijSesjeBD();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					dyrektor.tworzTabeleProdukt_Zamowienia(obiekty, budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 kontener.add(dodajPrzycisk);
					 kontener.add(eksportujDoDruku);
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazStanyZamowienPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();	
				 	PolaczenieProxy pc = new PolaczenieProxy();
					pc.zamknijSesjeBD();

				 	if(!HibernateOracle.cache.containsKey("StanyZamowien")) {
						bd.stworzSesjeBD();
						try (Session sesja2 = bd.pobierzSesjeBD()) {
				            Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Stany_Zamowienia order by id_stanu_zamowienia", Obiekt_Do_Polecen.class);
				            HibernateOracle.cache.put("StanyZamowien",zapytanie.getResultList());
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				 	}
					
					dyrektor.tworzTabeleStany_Zamowienia(HibernateOracle.cache.get("StanyZamowien"), budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
 					 kontener.add(eksportujDoDruku);
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazTypyUzytkownikaPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();
				 	PolaczenieProxy pc = new PolaczenieProxy();
					pc.zamknijSesjeBD();

				 	if(!HibernateOracle.cache.containsKey("TypyUzytkownika")) {
						bd.stworzSesjeBD();
						try (Session sesja2 = bd.pobierzSesjeBD()) {
				            Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Typy_uzytkownika order by id_typu_uzytkownika", Obiekt_Do_Polecen.class);
				            HibernateOracle.cache.put("TypyUzytkownika", zapytanie.getResultList());
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				 	}
					
					dyrektor.tworzTabeleTypy_uzytkownika(HibernateOracle.cache.get("TypyUzytkownika"), budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 kontener.add(eksportujDoDruku);
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazMagazynyPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();
				 	PolaczenieProxy pc = new PolaczenieProxy();
					pc.zamknijSesjeBD();

					if(!HibernateOracle.cache.containsKey("Magazyny")) {
						bd.stworzSesjeBD();
						try (Session sesja2 = bd.pobierzSesjeBD()) {
				            Query<Obiekt_Do_Polecen> zapytanie = sesja2.createQuery("FROM Magazyny order by id_magazynu", Obiekt_Do_Polecen.class);
				            HibernateOracle.cache.put("Magazyny", zapytanie.getResultList());
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
					}
					
					dyrektor.tworzTabeleMagazyny(HibernateOracle.cache.get("Magazyny"), budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 kontener.add(dodajPrzycisk);
					 kontener.add(eksportujDoDruku);
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazFakturyPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();
				 	PolaczenieProxy pc = new PolaczenieProxy();
					pc.zamknijSesjeBD();		 	

					List<Obiekt_Do_Polecen> obiekty = null;
					bd.stworzSesjeBD();
					
					try (Session sesja2 = bd.pobierzSesjeBD()) {
			            Query<Obiekt_Do_Polecen> zapytanie = null;
			            if(HibernateOracle.nazwaTypu.equals("Klient"))zapytanie = sesja2.createQuery("SELECT f FROM Faktury f, Zamowienia z, Uzytkownicy u where f.zamowienia_id_zamowienia=z.id_zamowienia and z.uzytkownicy_id_uzytkownika=u.id_uzytkownika and u.id_uzytkownika = :id order by f.id_faktury", Obiekt_Do_Polecen.class).setParameter("id", HibernateOracle.idUzytkownika);
			            else zapytanie = sesja2.createQuery("FROM Faktury order by id_faktury", Obiekt_Do_Polecen.class);
			            obiekty = zapytanie.getResultList();
			            bd.zamknijSesjeBD();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
																			
					dyrektor.tworzTabeleFaktury(obiekty, budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 if(!(HibernateOracle.nazwaTypu.equals("Klient")))
					 {
						 kontener.add(dodajPrzycisk);		
						 kontener.add(eksportujDoDruku);
					 }
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        dodajPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 HibernateOracle.wzorzec.dodajLogikeDodawania(kontener);
			 }
		 });
        
        eksportujDoDruku.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {				                
	                if(HibernateOracle.obiekt instanceof Produkty) 
	                {
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
	                
	                else if(HibernateOracle.obiekt instanceof Faktury) 
	                {
	                	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

		                List<Obiekt_Do_Polecen> obiekty = null;
						bd.stworzSesjeBD();
						
						try (Session sesja2 = bd.pobierzSesjeBD()) {
							
				            Query<Obiekt_Do_Polecen> zapytanie = null;
				            zapytanie = sesja2.createQuery("FROM Faktury order by id_faktury", Obiekt_Do_Polecen.class); 
				            obiekty = zapytanie.getResultList();
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
						dyrektor.tworzTabeleFaktury(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_faktur.txt";
		                 File plik = new File(path);

		                		                     
		                     try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
		                    	 pisarz.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                
	                else if(HibernateOracle.obiekt instanceof Kategorie) 
	                {
	                	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

		                List<Obiekt_Do_Polecen> obiekty = null;
						bd.stworzSesjeBD();
						
						try (Session sesja2 = bd.pobierzSesjeBD()) {
							
				            Query<Obiekt_Do_Polecen> zapytanie = null;
				            zapytanie = sesja2.createQuery("FROM Kategorie order by id_kategorii", Obiekt_Do_Polecen.class); 
				            obiekty = zapytanie.getResultList();
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
						dyrektor.tworzTabeleKategorie(obiekty, budDruk);
		                 												 		   
						String table = (String)dyrektor.pobierzTabele();
						
		                 String path = "wykaz_katgorii.txt";
		                 File plik = new File(path);

		                		                     
		                     try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
		                    	 pisarz.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obiekt instanceof Magazyny) 
	                {
	                	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

		                List<Obiekt_Do_Polecen> obiekty = null;
						bd.stworzSesjeBD();
						
						try (Session sesja2 = bd.pobierzSesjeBD()) {
							
				            Query<Obiekt_Do_Polecen> zapytanie = null;
				            zapytanie = sesja2.createQuery("FROM Magazyny order by id_magazynu", Obiekt_Do_Polecen.class); 
				            obiekty = zapytanie.getResultList();
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
						dyrektor.tworzTabeleMagazyny(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_magazynow.txt";
		                 File plik = new File(path);

		                		                     
		                     try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
		                    	 pisarz.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obiekt instanceof Producenci) 
	                {
	                	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

		                List<Obiekt_Do_Polecen> obiekty = null;
						bd.stworzSesjeBD();
						
						try (Session sesja2 = bd.pobierzSesjeBD()) {
							
				            Query<Obiekt_Do_Polecen> zapytanie = null;
				            zapytanie = sesja2.createQuery("FROM Producenci order by id_producenta", Obiekt_Do_Polecen.class); 
				            obiekty = zapytanie.getResultList();
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
						dyrektor.tworzTabeleProducenci(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_producentow.txt";
		                 File plik = new File(path);

		                		                     
		                     try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
		                    	 pisarz.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obiekt instanceof Produkt_Magazyn)
	                {
	                	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

		                List<Obiekt_Do_Polecen> obiekty = null;
						bd.stworzSesjeBD();
						
						try (Session sesja2 = bd.pobierzSesjeBD()) {
							
				            Query<Obiekt_Do_Polecen> zapytanie = null;
				            zapytanie = sesja2.createQuery("FROM Produkt_Magazyn order by produkt_magazyn_id.magazyny_id_magazynu, produkt_magazyn_id.produkty_id_produktu", Obiekt_Do_Polecen.class); //tututki
				            obiekty = zapytanie.getResultList();
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
						dyrektor.tworzTabeleProdukt_Magazyn(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_produkty_magazyn.txt";
		                 File plik = new File(path);

		                		                     
		                     try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
		                    	 pisarz.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obiekt instanceof Produkt_Zamowienia) 
	                {
	                	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

		                List<Obiekt_Do_Polecen> obiekty = null;
						bd.stworzSesjeBD();
						
						try (Session sesja2 = bd.pobierzSesjeBD()) {
							
				            Query<Obiekt_Do_Polecen> zapytanie = null;
				            zapytanie = sesja2.createQuery("FROM Produkt_Zamowienia  order by produkt_zamowienia_id.id_zamowienia, produkt_zamowienia_id.id_produktu", Obiekt_Do_Polecen.class); 
				            obiekty = zapytanie.getResultList();
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
						dyrektor.tworzTabeleProdukt_Zamowienia(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_produkt_zamowienia.txt";
		                 File plik = new File(path);

		                		                     
		                     try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
		                    	 pisarz.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obiekt instanceof Stany_Zamowienia) 
	                {
	                	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

		                List<Obiekt_Do_Polecen> obiekty = null;
						bd.stworzSesjeBD();
						
						try (Session sesja2 = bd.pobierzSesjeBD()) {
							
				            Query<Obiekt_Do_Polecen> zapytanie = null;
				            zapytanie = sesja2.createQuery("FROM Stany_Zamowienia order by id_stanu_zamowienia", Obiekt_Do_Polecen.class); 
				            obiekty = zapytanie.getResultList();
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
						dyrektor.tworzTabeleStany_Zamowienia(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_stany_zamowienia.txt";
		                 File plik = new File(path);

		                		                     
		                     try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
		                    	 pisarz.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obiekt instanceof Typy_uzytkownika) 
	                {
	                	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

		                List<Obiekt_Do_Polecen> obiekty = null;
						bd.stworzSesjeBD();
						
						try (Session sesja2 = bd.pobierzSesjeBD()) {
							
				            Query<Obiekt_Do_Polecen> zapytanie = null;
				            zapytanie = sesja2.createQuery("FROM Typy_uzytkownika order by id_typu_uzytkownika", Obiekt_Do_Polecen.class); 
				            obiekty = zapytanie.getResultList();
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }

						dyrektor.tworzTabeleTypy_uzytkownika(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_typy_uzytkownika.txt";
		                 File plik = new File(path);

		                		                     
		                     try (BufferedWriter pisarz = new BufferedWriter(new FileWriter(plik))) {
		                    	 pisarz.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obiekt instanceof Uzytkownicy) 
	                {
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
	                else if(HibernateOracle.obiekt instanceof Zamowienia) 
	                {		         			                	
	                	PolaczenieOracle bd =  PolaczenieOracle.pobierzInstancje();

		                List<Obiekt_Do_Polecen> obiekty = null;
						bd.stworzSesjeBD();
						
						try (Session sesja2 = bd.pobierzSesjeBD()) {
							
				            Query<Obiekt_Do_Polecen> zapytanie = null;
				            zapytanie = sesja2.createQuery("FROM Zamowienia order by id_zamowienia", Obiekt_Do_Polecen.class); 
				            obiekty = zapytanie.getResultList();
				            bd.zamknijSesjeBD();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
						dyrektor.tworzTabeleZamowienia(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_zamowienia.txt";
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
		 });       	        
	}

}
