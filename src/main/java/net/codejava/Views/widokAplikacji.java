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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.swing.WindowConstants;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.service.spi.ServiceException;

import net.codejava.HibernateOracle;
import net.codejava.Controllers.DyrektorTabel;
import net.codejava.Models.Faktury;
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
		
		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
		
		oc.createDBSession();
		HibernateOracle.cache = new HashMap<>();
		
		Session session = oc.getDBSession();		
		
		try
		{
			session = oc.getDBSession();
		}catch(Exception e)
		{
            System.out.println("Blad dodania tablicy");
		}
		
		List<Obiekt_Do_Polecen> obiekty = null;
		
		try (Session session2 = oc.getDBSession()) {
            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class);
            obiekty = query.getResultList();
            oc.closeDBSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
		//oc.closeDBSession();			 
			
		final JFrame frame = new JFrame("Elektryka Prad Nie Tyka");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		
		frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!HibernateOracle.nazwaTypu.equals("Klient"))
                {
                	HibernateOracle.repoPolecen.saveToFile();
                	System.out.println("Okno zostaje zamknięte");
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
	            	JTextField loginField = new JTextField(5);
	                JTextField hasloField = new JTextField(5);

	                JPanel myPanel = new JPanel();
	                myPanel.add(new JLabel("Login:"));
	                myPanel.add(loginField);
	                myPanel.add(Box.createHorizontalStrut(15));
	                myPanel.add(new JLabel("Haslo:"));
	                myPanel.add(hasloField);

	                int result = JOptionPane.showConfirmDialog(null, myPanel, 
	                         "Podaj login i haslo", JOptionPane.OK_CANCEL_OPTION);

	                
	                try {
	                	if (result == JOptionPane.OK_OPTION) {
	                		
	                		oc.createDBSession();
	                		List<Uzytkownicy> uzytkownicy = null;
	                		try (Session session2 = oc.getDBSession()) {
	                            Query<Uzytkownicy> query = session2.createQuery("FROM Uzytkownicy order by id_uzytkownika", Uzytkownicy.class);
	                            uzytkownicy = query.getResultList();
	                            oc.closeDBSession();
	                        } catch (Exception e1) {
	                            e1.printStackTrace();
	                            throw new Exception("Nie udalo polaczyc sie z baza danych");
	                        }
	                		for(Uzytkownicy uzytkownik: uzytkownicy) {
			                	if(loginField.getText().equals(uzytkownik.getLogin())) {
			                		if(hasloField.getText().equals(uzytkownik.getHaslo()) && uzytkownik.getCzy_usunieto()==0)
			                		{
				                		System.out.println("zalogowano");
				                		
				                		HibernateOracle.idUzytkownika = uzytkownik.getId_uzytkownika();
				                		
				                		oc.createDBSession();
				                		Session session2 = oc.getDBSession();
				                		Query<Typy_uzytkownika> query = session2.createQuery("FROM Typy_uzytkownika order by id_typu_uzytkownika", Typy_uzytkownika.class);
			                            List<Typy_uzytkownika> typyUzytkownika = query.getResultList();                           
			                                
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

				 oc.createDBSession();
				 try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class);
			            obiekty = query.getResultList();
			            oc.closeDBSession();
			        
				 
				 //budSwing.tworzTabeleProdukty(obiekty);
			       
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

					Uzytkownicy user = new Uzytkownicy();
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
						user = (Uzytkownicy)session2.createQuery("select u from Uzytkownicy u where u.id_uzytkownika like "+ HibernateOracle.idUzytkownika)
	 	                			.uniqueResult();			            			            
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
															
					JLabel szczegoly = new JLabel("Szczególy");
					JLabel nazwa = new JLabel("Nazwa użytkownika:  " + user.getNazwa_uzytkownika());
					JLabel login = new JLabel("Login:  " + user.getLogin());
					JLabel haslo = new JLabel("Hasło:  " + "*".repeat(user.getHaslo().length()));
					JLabel email = new JLabel("E-mail:  " + user.getE_mail());
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
						//budSwing.tworzTabeleKoszyk(koszyk); // <- zmienić na inną metodę
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
							 JTextField pierwszyField = new JTextField(7);
				                JTextField drugiField = new JTextField(7);
				                JTextField trzeciField = new JTextField(7);
				                JTextField czwartyField = new JTextField(7);
				                //JTextField piatyField = new JTextField(7);
			            		 
			 	                JPanel myPanel = new JPanel();
							 myPanel.add(new JLabel("Nazwa użytkownika: "));
		                		myPanel.add(pierwszyField);
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel("Login: "));
		                		myPanel.add(drugiField);
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel("Hasło: "));
		                		myPanel.add(trzeciField);
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel("E-mail: "));
		                		myPanel.add(czwartyField);
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		/*myPanel.add(new JLabel("Id typu użytkownika: "));
		                		myPanel.add(piatyField);*/
		                		
		                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		   	                         "Edytuj użytkownika", JOptionPane.OK_CANCEL_OPTION);
		                		 try {
		     	                	if (result == JOptionPane.OK_OPTION) {
		     	                		
		     	                		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
		     	 	                	oc.createDBSession();
		     	 	                	Session session = oc.getDBSession();
		     	                		
		     	 	                	Uzytkownicy user = (Uzytkownicy)session.createQuery("select u from Uzytkownicy u where u.id_uzytkownika like "+Integer.toString(HibernateOracle.idUzytkownika))
		     	 	                			.uniqueResult();
		     	 	                	//System.out.println(user.getId_uzytkownika());
		     	 	                	
		     	 	                	if(!pierwszyField.getText().isEmpty())
		     	 	                		user.setNazwa_uzytkownika(pierwszyField.getText());
		     	 	                	if(!drugiField.getText().isEmpty())
		     	 	                		user.setLogin(drugiField.getText());
		     	 	                	if(!trzeciField.getText().isEmpty())
		     	 	                		user.setHaslo(trzeciField.getText());
		     	 	                	if(!czwartyField.getText().isEmpty())
		     	 	                		user.setE_mail(czwartyField.getText());
		     	 	                	/*if(!piatyField.getText().isEmpty())
		     	 	                		user.setId_typu_uzytkownika(Integer.parseInt(piatyField.getText()));*/
		     	 	                	
		     	                		session.update(user);
		     	                		
		     	                		oc.closeDBSession();
		     	                		
		     	                		kontoPrzycisk.setText(user.getNazwa_uzytkownika());
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
				 	            		 PolaczenieOracle oc =  PolaczenieOracle.getInstance();
				 	            		 oc.createDBSession();	                			
				 	            		 Session session = oc.getDBSession();
				 	            		Uzytkownicy pr = (Uzytkownicy)session.createQuery("select u from Uzytkownicy u where u.id_uzytkownika = :id")
			 	 	                			.setParameter("id", idtym)
			 	 	                			.uniqueResult();
			 	                		
				 	            		pr.setCzy_usunieto(1);
				 	 	                session.update(pr);
				 	            	 }
				 	            }catch(Exception e)
				 	            	 {
				 	            	System.out.println("Wystapil blad podczas usuwania konta: "+ e.toString());
				 	            		 
				 	            }
		 	 	 	                
				 	            	oc.closeDBSession();							 		 	 	 	                	 	 	 	                	 	 	                
						 }	 	 	 	             
					 });
					 
					 zlozZamowieniePrzycisk.addActionListener(new ActionListener() 
					 {
						 @Override
							public void actionPerformed(ActionEvent a) {		
							 JTextField pierwszyField = new JTextField(7);
				                JTextField drugiField = new JTextField(7);
				                JTextField trzeciField = new JTextField(7);
				                JCheckBox checkbox = new JCheckBox("Faktura");
				                JCheckBox drugiCheckbox = new JCheckBox("Opakowanie prezentowe");
				                JTextField czwartyField = new JTextField(7);
				                JTextField piatyField = new JTextField(20);
			            		 
			 	                JPanel myPanel = new JPanel();
							 myPanel.add(new JLabel("Miasto wysyłki: "));
		                		myPanel.add(pierwszyField);
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel("Ulica: "));
		                		myPanel.add(drugiField);
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel(" "));
		                		myPanel.add(checkbox);
		                		checkbox.setSelected(false);
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel(" "));
		                		myPanel.add(drugiCheckbox);
		                		checkbox.setSelected(false);	
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel("Wpisz zniżkę: "));
		                		myPanel.add(czwartyField);
		                		myPanel.add(Box.createHorizontalStrut(5));
		                		myPanel.add(new JLabel("Notatka: "));
		                		myPanel.add(piatyField);
		                		
		                		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		   	                         "Złóż zamówienie", JOptionPane.OK_CANCEL_OPTION);
		                				                		
		                		 try {	
		                			 if (result == JOptionPane.OK_OPTION) {
		                				 
		                				 if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty())
					 	                	{
					 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Zamówienie nie zostało złożone.");
					 	                		return;
					 	                	}
		                				 if(checkbox.isSelected()==true)
		                				 {
		                					 JPanel myPanel2 = new JPanel();
		        							 myPanel2.add(new JLabel("NIP: "));
		        		                	 myPanel2.add(trzeciField);
		        		                	 int result2 = JOptionPane.showConfirmDialog(null, myPanel2, 
			    		   	                         "Złóż fakturę", JOptionPane.OK_CANCEL_OPTION);
			                				 
			                				 if (result2 == JOptionPane.CANCEL_OPTION || trzeciField.getText().isEmpty()) {
			                					 JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Faktura nie zostanie dodana.");
			                					 throw new Exception("Zamówienie nie zostało złożone");
			                				 }
		                				 }
		                				 
		     	                		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
		     	 	                	oc.createDBSession();
		     	 	                	Session session = oc.getDBSession();
		     	 	                			     	 	                	
		     	 	                	for(Obiekt_Do_Polecen produkt: HibernateOracle.koszyk) {
			     	 	                		int id = ((Produkt_Koszyk)produkt).getPr().getId_produktu();
			     	 	                	
			     	 	                	if(session.createQuery("select p.czy_usunieto from Produkty p where p.id_produktu = :idP", Integer.class)
		     		 	                			.setParameter("idP", id)
		     		 	                			.uniqueResult()==1? true:false)throw new Exception("Przykro nam ale jeden z produktów został usunięty z naszej oferty.");
			     	 	                	
			     		                	Query<Integer> query = session.createQuery("select pd.stan_magazynowy from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP", Integer.class)
		     		 	                			.setParameter("idP", id);
			     		                	List<Integer> stanMagazynow = query.getResultList();
			     		                	
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
			     	 	                	
			     	 	                	
			     		                	Query<Integer> query = session.createQuery("select pd.stan_magazynowy from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP", Integer.class)
		     		 	                			.setParameter("idP", id);
			     		                	List<Integer> stanMagazynow = query.getResultList();
			     		                	
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
		     
		     	 	                	System.out.println("trzecia");	 
		     	 	                	
		     	 	                	Zamowienia zamowienie = new Zamowienia(koszt, pierwszyField.getText(), drugiField.getText(), 1, HibernateOracle.idUzytkownika,"");
		     	 	                	if(!czwartyField.getText().isEmpty())zamowienie = new Znizka(zamowienie, Double.parseDouble(czwartyField.getText()));		     	 	                		     	 	                	
		     	 	                			     	 	               
		     	 	                	if(drugiCheckbox.isSelected()==true)zamowienie = new Opakowanie(zamowienie);
		     	 	                	
		     	 	                	if(!piatyField.getText().isEmpty()) {
		     	 	                		if(piatyField.getText().length() < 150)zamowienie = new Notatka(zamowienie, piatyField.getText());
		     	 	                		else throw(new Exception("Podano zbyt długą notatkę - max 150 znaków."));
		     	 	                	}
		     	 	                				     	 	                	     	 	                	
		     	 	                	zamowienie = new Zamowienia(zamowienie.getId_zamowienia(), zamowienie.getKoszt(), pierwszyField.getText(), drugiField.getText(), 1, HibernateOracle.idUzytkownika, zamowienie.getOpis());
		     	 	                	
		     	 	                	double zaokr = Math.round(zamowienie.getKoszt()*100.0)/100.0;
		     	 	                	zamowienie.setKoszt(zaokr);
		     	 	                	
		     	 	                	session.save(zamowienie);		  	 	                	     	 	                	
		     	 	                	
		     	 	                	for(Obiekt_Do_Polecen odp : HibernateOracle.koszyk)		 
		     	 	                	{
		     	 	                		int id = ((Produkt_Koszyk)odp).getPr().getId_produktu();
		     	 	                		
					     	 	            Query<Produkt_Magazyn> query = session.createQuery("select pd from Produkt_Magazyn pd where pd.produkt_magazyn_id.produkty_id_produktu = :idP", Produkt_Magazyn.class)
			 		 	                			.setParameter("idP", id);
			     		                	List<Produkt_Magazyn> stanMagazynow = query.getResultList();
			     		                	
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
			     		                		session.update(stan);
			     		                	}
		     	 	                		
		     	 	                		session.save(new Produkt_Zamowienia(new Produkt_Zamowienia_Id(zamowienie.getId_zamowienia(), (((Produkt_Koszyk)odp).getPr().getId_produktu())), iloscProdKoszyk));
		     	 	                	}
		     	 	                	if(!(trzeciField.getText().isEmpty()))	session.save(new Faktury(LocalDate.now(), trzeciField.getText(), zamowienie.getId_zamowienia()));  
		     	 	                	System.out.println("czwarty");
		     	                		oc.closeDBSession();
		     	                		
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
		                		 }
		                		 catch(Exception e) {
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
				 
				 JTextField pierwszyField = new JTextField(7);
	                JTextField drugiField = new JTextField(7);
	                JTextField trzeciField = new JTextField(7);
	                JTextField czwartyField = new JTextField(7);
	                JTextField piatyField = new JTextField(7);
				 	JPanel myPanel = new JPanel();					
 	                myPanel.add(new JLabel("Nazwa użytkownika: "));
	           		myPanel.add(pierwszyField);
	           		myPanel.add(Box.createHorizontalStrut(5));
	           		myPanel.add(new JLabel("Login: "));
	           		myPanel.add(drugiField);
	           		myPanel.add(Box.createHorizontalStrut(5));
	           		myPanel.add(new JLabel("Hasło: "));
	           		myPanel.add(trzeciField);
	           		myPanel.add(Box.createHorizontalStrut(5));
	           		myPanel.add(new JLabel("E-mail: "));
	           		myPanel.add(czwartyField);
 	                int result = JOptionPane.showConfirmDialog(null, myPanel, 
	                         "Zakładanie konta", JOptionPane.OK_CANCEL_OPTION);
                			
	 	            try {
	 	            	 if (result == JOptionPane.OK_OPTION) {
	 	            		if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty() || trzeciField.getText().isEmpty() || czwartyField.getText().isEmpty())
	 	            			throw(new Exception("Nie podano wszystkich danych. Konto nie zostało utworzone."));
	 	                		 	                		 	                	
	 	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();
	 	                	oc.createDBSession();
	 	                	Session session = oc.getDBSession();
	 	                	
	 	                	session.save(new Uzytkownicy(pierwszyField.getText(), drugiField.getText(), trzeciField.getText(), czwartyField.getText(), 3, 0));	 
	 	                	oc.closeDBSession();               		
	 	            	 }
	 	            }catch(Exception e)
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
					 pc.closeDBSession();

					List<Obiekt_Do_Polecen> obiekty = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
						
			            Query<Obiekt_Do_Polecen> query = null;
			            if((HibernateOracle.nazwaTypu.equals("Klient")))query = session2.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class); 
			            else query = session2.createQuery("FROM Produkty order by id_produktu", Obiekt_Do_Polecen.class);
			            obiekty = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					//budSwing.tworzTabeleProdukty(obiekty);
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
					pc.closeDBSession();

					List<Obiekt_Do_Polecen> obiekty = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = null;
			            if(HibernateOracle.nazwaTypu.equals("Klient"))query = session2.createQuery("FROM Zamowienia z where z.uzytkownicy_id_uzytkownika = :id order by z.id_zamowienia", Obiekt_Do_Polecen.class).setParameter("id", HibernateOracle.idUzytkownika);
			            else query = session2.createQuery("FROM Zamowienia order by id_zamowienia", Obiekt_Do_Polecen.class);
			            obiekty = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					//budSwing.tworzTabeleZamowienia(obiekty);
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
					pc.closeDBSession();

					List<Obiekt_Do_Polecen> obiekty = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Uzytkownicy order by id_uzytkownika", Obiekt_Do_Polecen.class);
			            obiekty = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					//budSwing.tworzTabeleUzytkownicy(obiekty);
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
					pc.closeDBSession();

					if(!HibernateOracle.cache.containsKey("Kategorie")) {
						oc.createDBSession();						
						try (Session session2 = oc.getDBSession()) {
							Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Kategorie order by id_kategorii", Obiekt_Do_Polecen.class);
							HibernateOracle.cache.put("Kategorie", query.getResultList());
							oc.closeDBSession();
							//System.out.println("załadowano do cache");
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}
					/*else
						System.out.println("Załadowano z cache");*/
					
					//budSwing.tworzTabeleKategorie(obiekty);
					
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
					pc.closeDBSession();

				 	if(!HibernateOracle.cache.containsKey("Producenci")) {
						oc.createDBSession();
						try (Session session2 = oc.getDBSession()) {
				            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Producenci order by id_producenta", Obiekt_Do_Polecen.class);
				            HibernateOracle.cache.put("Producenci", query.getResultList());
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				 	}
					
					//budSwing.tworzTabeleProducenci(obiekty);
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
					pc.closeDBSession();

					List<Obiekt_Do_Polecen> obiekty = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkt_Magazyn order by produkt_magazyn_id.magazyny_id_magazynu, produkt_magazyn_id.produkty_id_produktu", Obiekt_Do_Polecen.class);
			            obiekty = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					//budSwing.tworzTabeleProdukt_Magazyn(obiekty);
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
					pc.closeDBSession();

					List<Obiekt_Do_Polecen> obiekty = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkt_Zamowienia order by produkt_zamowienia_id.id_zamowienia, produkt_zamowienia_id.id_produktu", Obiekt_Do_Polecen.class);
			            obiekty = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					//budSwing.tworzTabeleProdukt_Zamowienia(obiekty);
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
					pc.closeDBSession();

				 	if(!HibernateOracle.cache.containsKey("StanyZamowien")) {
						oc.createDBSession();
						try (Session session2 = oc.getDBSession()) {
				            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Stany_Zamowienia order by id_stanu_zamowienia", Obiekt_Do_Polecen.class);
				            HibernateOracle.cache.put("StanyZamowien",query.getResultList());
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				 	}
					
					//budSwing.tworzTabeleStany_Zamowienia(obiekty);
					dyrektor.tworzTabeleStany_Zamowienia(HibernateOracle.cache.get("StanyZamowien"), budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 //kontener.add(dodajPrzycisk);  // ustalone że nie dodajemy
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
					pc.closeDBSession();

				 	if(!HibernateOracle.cache.containsKey("TypyUzytkownika")) {
						oc.createDBSession();
						try (Session session2 = oc.getDBSession()) {
				            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Typy_uzytkownika order by id_typu_uzytkownika", Obiekt_Do_Polecen.class);
				            HibernateOracle.cache.put("TypyUzytkownika", query.getResultList());
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				 	}
					
					//budSwing.tworzTabeleTypy_uzytkownika(obiekty);
					dyrektor.tworzTabeleTypy_uzytkownika(HibernateOracle.cache.get("TypyUzytkownika"), budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 //kontener.add(dodajPrzycisk);  // ustalone że nie dodajemy
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
					pc.closeDBSession();

					if(!HibernateOracle.cache.containsKey("Magazyny")) {
						oc.createDBSession();
						try (Session session2 = oc.getDBSession()) {
				            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Magazyny order by id_magazynu", Obiekt_Do_Polecen.class);
				            HibernateOracle.cache.put("Magazyny", query.getResultList());
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
					}
					
					//budSwing.tworzTabeleMagazyny(obiekty);
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
					pc.closeDBSession();		 	

					List<Obiekt_Do_Polecen> obiekty = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = null;
			            if(HibernateOracle.nazwaTypu.equals("Klient"))query = session2.createQuery("SELECT f FROM Faktury f, Zamowienia z, Uzytkownicy u where f.zamowienia_id_zamowienia=z.id_zamowienia and z.uzytkownicy_id_uzytkownika=u.id_uzytkownika and u.id_uzytkownika = :id order by f.id_faktury", Obiekt_Do_Polecen.class).setParameter("id", HibernateOracle.idUzytkownika);
			            else query = session2.createQuery("FROM Faktury order by id_faktury", Obiekt_Do_Polecen.class);
			            obiekty = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					
					oc.createDBSession();
					Session session = oc.getDBSession();
					try { session.doWork(connection -> { // Tutaj możesz bezpośrednio operować na obiekcie java.sql.Connection 
						  Connection connection2 =	connection.unwrap(Connection.class); // ... // Wykonaj operacje na jdbcConnection
					  
					  DatabaseMetaData metaData = connection2.getMetaData();
					  System.out.println(metaData);
					  
					  // Podaj nazwę tabeli, dla której chcesz uzyskać metadane
					  String tableName = "PRODUKT_M%";
					  
					  // Uzyskaj informacje o kolumnach dla danej tabeli 
					  ResultSet resultSet =	 metaData.getColumns(null, null, tableName, null);
					  System.out.println(resultSet);
					  
					  // Przejdź przez wyniki i wydrukuj nazwy kolumn 
					  while (resultSet.next()) {
					  String columnName = resultSet.getString("COLUMN_NAME");
					  System.out.println("Nazwa kolumny: " + columnName); // Możesz zebrać te nazwy do listy lub innej struktury danych, aby je wykorzystać później
					  } }); } catch	(Exception e) 
					  {
						  e.printStackTrace(); JOptionPane.showMessageDialog(null,"Nie udało się edytować produktu w magazynie. Błąd: " + e.getMessage());
					  }
					  //
					  //u.produkt_magazyn_id like "+Integer.toString(this.id) + " and u.produkty_Id_Produktu like " + Integer.toString(this.id2) }
					oc.closeDBSession();
					
					
					//budSwing.tworzTabeleFaktury(obiekty);
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
	                if(HibernateOracle.obj instanceof Produkty) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();
		                oc.createDBSession();

		                List<Obiekt_Do_Polecen> obiekty = null;
		                
						try (Session session2 = oc.getDBSession()) {							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Produkty order by id_produktu", Obiekt_Do_Polecen.class);
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleProdukty(obiekty);
						dyrektor.tworzTabeleProdukty(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_produktow.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
		                	            		         		 
	                }
	                
	                else if(HibernateOracle.obj instanceof Faktury) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> obiekty = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Faktury order by id_faktury", Obiekt_Do_Polecen.class); 
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleFaktury(obiekty);
						dyrektor.tworzTabeleFaktury(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_faktur.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                
	                else if(HibernateOracle.obj instanceof Kategorie) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> obiekty = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Kategorie order by id_kategorii", Obiekt_Do_Polecen.class); 
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //BudowniczyTabeliDruk budDruk = new BudowniczyTabeliDruk();
		                 //budDruk.tworzTabeleKategorie(obiekty);
						
		                 //String table = (String)budDruk.pobierzTabele(null);
						
						dyrektor.tworzTabeleKategorie(obiekty, budDruk);
		                 												 		   
						String table = (String)dyrektor.pobierzTabele();
						
		                 String path = "wykaz_katgorii.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obj instanceof Magazyny) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> obiekty = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Magazyny order by id_magazynu", Obiekt_Do_Polecen.class); 
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleMagazyny(obiekty);
						dyrektor.tworzTabeleMagazyny(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_magazynow.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obj instanceof Producenci) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> obiekty = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Producenci order by id_producenta", Obiekt_Do_Polecen.class); 
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleProducenci(obiekty);
						dyrektor.tworzTabeleProducenci(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_producentow.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obj instanceof Produkt_Magazyn)
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> obiekty = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Produkt_Magazyn order by produkt_magazyn_id.magazyny_id_magazynu, produkt_magazyn_id.produkty_id_produktu", Obiekt_Do_Polecen.class); //tututki
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleProdukt_Magazyn(obiekty);
						dyrektor.tworzTabeleProdukt_Magazyn(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_produkty_magazyn.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obj instanceof Produkt_Zamowienia) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> obiekty = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Produkt_Zamowienia  order by produkt_zamowienia_id.id_zamowienia, produkt_zamowienia_id.id_produktu", Obiekt_Do_Polecen.class); 
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleProdukt_Zamowienia(obiekty);
						dyrektor.tworzTabeleProdukt_Zamowienia(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_produkt_zamowienia.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obj instanceof Stany_Zamowienia) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> obiekty = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Stany_Zamowienia order by id_stanu_zamowienia", Obiekt_Do_Polecen.class); 
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleStany_Zamowienia(obiekty);
						dyrektor.tworzTabeleStany_Zamowienia(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_stany_zamowienia.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obj instanceof Typy_uzytkownika) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> obiekty = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Typy_uzytkownika order by id_typu_uzytkownika", Obiekt_Do_Polecen.class); 
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }

		                 //budDruk.tworzTabeleTypy_uzytkownika(obiekty);
						dyrektor.tworzTabeleTypy_uzytkownika(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_typy_uzytkownika.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obj instanceof Uzytkownicy) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> obiekty = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Uzytkownicy order by id_uzytkownika", Obiekt_Do_Polecen.class); 
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleUzytkownicy(obiekty);
						dyrektor.tworzTabeleUzytkownicy(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_uzytkownicy.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
		                         JOptionPane.showMessageDialog(null, "Powstał plik: " + path);
		                     } catch (IOException e) {
		                         e.printStackTrace();
		                         JOptionPane.showMessageDialog(null, "Błąd podczas zapisu do pliku: " + e.getMessage());
		                     }
	                }
	                else if(HibernateOracle.obj instanceof Zamowienia) 
	                {		         			                	
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> obiekty = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Zamowienia order by id_zamowienia", Obiekt_Do_Polecen.class); 
				            obiekty = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleZamowienia(obiekty);
						dyrektor.tworzTabeleZamowienia(obiekty, budDruk);
		                 String table = (String)dyrektor.pobierzTabele();
		                 												 		                 
		                 String path = "wykaz_zamowienia.txt";
		                 File file = new File(path);

		                		                     
		                     // Używamy konstruktora FileWriter z trybem append (dopisywania)
		                     try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		                         // Kod zapisu do pliku
		                         writer.write(table);		                         
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
