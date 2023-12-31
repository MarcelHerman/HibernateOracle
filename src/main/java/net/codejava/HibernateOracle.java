package net.codejava;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import java.util.List;

import javax.persistence.Entity;
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
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.service.spi.ServiceException;

import java.util.Map;
import java.util.HashMap;

public class HibernateOracle extends JFrame {
	
	public static String nazwaTypu = "null";
	public static Obiekt_Do_Polecen obj = null;
	public static List<Obiekt_Do_Polecen> koszyk = new ArrayList<Obiekt_Do_Polecen>();
	public static Map<String, List<Obiekt_Do_Polecen>> cache;
	
	public static int idUzytkownika;
	
	public static Repozytorium_Polecen repo_pol = new Repozytorium_Polecen();
	
	public static IStrategia wzorzec;

	public static void main(String[] args) {
		
		PolaczenieOracle oc =  PolaczenieOracle.getInstance();
		
		oc.createDBSession();
		cache = new HashMap<>();
		
		Session session = oc.getDBSession();		
		
		try
		{
			session = oc.getDBSession();
		}catch(Exception e)
		{
            System.out.println("Blad dodania tablicy");
		}
		
		List<Obiekt_Do_Polecen> entities = null;
		
		try (Session session2 = oc.getDBSession()) {
            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class);
            entities = query.getResultList();
            oc.closeDBSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
		//oc.closeDBSession();			 
		
		String placeholderLogin = "xd";
		String placeholderPassword = "1";
		
		final JFrame frame = new JFrame("Elektryka Prad Nie Tyka");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		
		frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!nazwaTypu.equals("Klient"))
                {
                	repo_pol.saveToFile();
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
		
		//budSwing.tworzTabeleProdukty(entities);
		dyrektor.tworzTabeleProdukty(entities, budSwing);
		
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
				                		
				                		idUzytkownika = uzytkownik.getId_uzytkownika();
				                		
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
			                            		nazwaTypu = typ.getNazwa();
			                            		break;
			                            	}
			                            }
				                		
				                		switch(nazwaTypu) {
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
	                		if(nazwaTypu.equals("null")) throw new Exception("");
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
				 List<Obiekt_Do_Polecen> entities = null;
				 nazwaTypu = "null";
				 koszyk.clear();
				 idUzytkownika = -1;
				 
				 kontener.removeAll();
				 
				 bar.removeAll();
				 bar.setVisible(true);

				 oc.createDBSession();
				 try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        
				 
				 //budSwing.tworzTabeleProdukty(entities);
			       
			     dyrektor.tworzTabeleProdukty(entities, budSwing);
			     
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
						user = (Uzytkownicy)session2.createQuery("select u from Uzytkownicy u where u.id_uzytkownika like "+ idUzytkownika)
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
										
					if(koszyk.size() != 0) {
						//budSwing.tworzTabeleKoszyk(koszyk); // <- zmienić na inną metodę
						dyrektor.tworzTabeleKoszyk(koszyk, budSwing);
						
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
		     	                		
		     	 	                	Uzytkownicy user = (Uzytkownicy)session.createQuery("select u from Uzytkownicy u where u.id_uzytkownika like "+Integer.toString(idUzytkownika))
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
				 	            		 int idtym = idUzytkownika;
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
		     	 	                			     	 	                	
		     	 	                	for(Obiekt_Do_Polecen produkt: koszyk) {
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
		     	 	                	for(Obiekt_Do_Polecen produkt : koszyk)
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
		     	 	                	
		     	 	                	Zamowienia zamowienie = new Zamowienia(koszt, pierwszyField.getText(), drugiField.getText(), 1, idUzytkownika,"");
		     	 	                	if(!czwartyField.getText().isEmpty())zamowienie = new Znizka(zamowienie, Double.parseDouble(czwartyField.getText()));		     	 	                		     	 	                	
		     	 	                			     	 	               
		     	 	                	if(drugiCheckbox.isSelected()==true)zamowienie = new Opakowanie(zamowienie);
		     	 	                	
		     	 	                	if(!piatyField.getText().isEmpty()) {
		     	 	                		if(piatyField.getText().length() < 150)zamowienie = new Notatka(zamowienie, piatyField.getText());
		     	 	                		else throw(new Exception("Podano zbyt długą notatkę - max 150 znaków."));
		     	 	                	}
		     	 	                				     	 	                	     	 	                	
		     	 	                	zamowienie = new Zamowienia(zamowienie.getId_zamowienia(), zamowienie.getKoszt(), pierwszyField.getText(), drugiField.getText(), 1, idUzytkownika, zamowienie.getOpis());
		     	 	                	
		     	 	                	double zaokr = Math.round(zamowienie.getKoszt()*100.0)/100.0;
		     	 	                	zamowienie.setKoszt(zaokr);
		     	 	                	
		     	 	                	session.save(zamowienie);		  	 	                	     	 	                	
		     	 	                	
		     	 	                	for(Obiekt_Do_Polecen odp : koszyk)		 
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
		     	                		
		     	                		koszyk.clear();
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
		     	                		koszyk.clear();
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

					List<Obiekt_Do_Polecen> entities = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
						
			            Query<Obiekt_Do_Polecen> query = null;
			            if((nazwaTypu.equals("Klient")))query = session2.createQuery("FROM Produkty where czy_usunieto = 0 order by id_produktu", Obiekt_Do_Polecen.class); 
			            else query = session2.createQuery("FROM Produkty order by id_produktu", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					//budSwing.tworzTabeleProdukty(entities);
					dyrektor.tworzTabeleProdukty(entities, budSwing);
					
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 
					 if(!(nazwaTypu.equals("Klient")))
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

					List<Obiekt_Do_Polecen> entities = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = null;
			            if(nazwaTypu.equals("Klient"))query = session2.createQuery("FROM Zamowienia z where z.uzytkownicy_id_uzytkownika = :id order by z.id_zamowienia", Obiekt_Do_Polecen.class).setParameter("id", idUzytkownika);
			            else query = session2.createQuery("FROM Zamowienia order by id_zamowienia", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					//budSwing.tworzTabeleZamowienia(entities);
					dyrektor.tworzTabeleZamowienia(entities, budSwing);
					
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 if(!(nazwaTypu.equals("Klient"))){
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

					List<Obiekt_Do_Polecen> entities = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Uzytkownicy order by id_uzytkownika", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					//budSwing.tworzTabeleUzytkownicy(entities);
					dyrektor.tworzTabeleUzytkownicy(entities, budSwing);
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

					if(!cache.containsKey("Kategorie")) {
						oc.createDBSession();						
						try (Session session2 = oc.getDBSession()) {
							Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Kategorie order by id_kategorii", Obiekt_Do_Polecen.class);
							cache.put("Kategorie", query.getResultList());
							oc.closeDBSession();
							//System.out.println("załadowano do cache");
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}
					/*else
						System.out.println("Załadowano z cache");*/
					
					//budSwing.tworzTabeleKategorie(entities);
					
					dyrektor.tworzTabeleKategorie(cache.get("Kategorie"), budSwing);
					
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

				 	if(!cache.containsKey("Producenci")) {
						oc.createDBSession();
						try (Session session2 = oc.getDBSession()) {
				            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Producenci order by id_producenta", Obiekt_Do_Polecen.class);
				            cache.put("Producenci", query.getResultList());
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				 	}
					
					//budSwing.tworzTabeleProducenci(entities);
					dyrektor.tworzTabeleProducenci(cache.get("Producenci"), budSwing);
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

					List<Obiekt_Do_Polecen> entities = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkt_Magazyn order by produkt_magazyn_id.magazyny_id_magazynu, produkt_magazyn_id.produkty_id_produktu", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					//budSwing.tworzTabeleProdukt_Magazyn(entities);
					dyrektor.tworzTabeleProdukt_Magazyn(entities, budSwing);
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

					List<Obiekt_Do_Polecen> entities = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkt_Zamowienia order by produkt_zamowienia_id.id_zamowienia, produkt_zamowienia_id.id_produktu", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					//budSwing.tworzTabeleProdukt_Zamowienia(entities);
					dyrektor.tworzTabeleProdukt_Zamowienia(entities, budSwing);
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

				 	if(!cache.containsKey("StanyZamowien")) {
						oc.createDBSession();
						try (Session session2 = oc.getDBSession()) {
				            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Stany_Zamowienia order by id_stanu_zamowienia", Obiekt_Do_Polecen.class);
				            cache.put("StanyZamowien",query.getResultList());
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				 	}
					
					//budSwing.tworzTabeleStany_Zamowienia(entities);
					dyrektor.tworzTabeleStany_Zamowienia(cache.get("StanyZamowien"), budSwing);
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

				 	if(!cache.containsKey("TypyUzytkownika")) {
						oc.createDBSession();
						try (Session session2 = oc.getDBSession()) {
				            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Typy_uzytkownika order by id_typu_uzytkownika", Obiekt_Do_Polecen.class);
				            cache.put("TypyUzytkownika", query.getResultList());
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				 	}
					
					//budSwing.tworzTabeleTypy_uzytkownika(entities);
					dyrektor.tworzTabeleTypy_uzytkownika(cache.get("TypyUzytkownika"), budSwing);
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

					if(!cache.containsKey("Magazyny")) {
						oc.createDBSession();
						try (Session session2 = oc.getDBSession()) {
				            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Magazyny order by id_magazynu", Obiekt_Do_Polecen.class);
				            cache.put("Magazyny", query.getResultList());
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
					}
					
					//budSwing.tworzTabeleMagazyny(entities);
					dyrektor.tworzTabeleMagazyny(cache.get("Magazyny"), budSwing);
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

					List<Obiekt_Do_Polecen> entities = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = null;
			            if(nazwaTypu.equals("Klient"))query = session2.createQuery("SELECT f FROM Faktury f, Zamowienia z, Uzytkownicy u where f.zamowienia_id_zamowienia=z.id_zamowienia and z.uzytkownicy_id_uzytkownika=u.id_uzytkownika and u.id_uzytkownika = :id order by f.id_faktury", Obiekt_Do_Polecen.class).setParameter("id", idUzytkownika);
			            else query = session2.createQuery("FROM Faktury order by id_faktury", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					
					oc.createDBSession();
					Session session = oc.getDBSession();
					try { session.doWork(connection -> { // Tutaj możesz bezpośrednio operować na obiekcie java.sql.Connection 
						  Connection connectionxd =	connection.unwrap(Connection.class); // ... // Wykonaj operacje na jdbcConnection
					  
					  DatabaseMetaData metaData = connectionxd.getMetaData();
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
					
					
					//budSwing.tworzTabeleFaktury(entities);
					dyrektor.tworzTabeleFaktury(entities, budSwing);
					 JTable tabSwing = (JTable)dyrektor.pobierzTabele();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 if(!(nazwaTypu.equals("Klient")))
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
				 	JTextField pierwszyField = new JTextField(7);
	                JTextField drugiField = new JTextField(7);
	                JTextField trzeciField = new JTextField(7);
	                JTextField czwartyField = new JTextField(7);
	                JTextField piatyField = new JTextField(7);
         		 
	                JPanel myPanel = new JPanel();
	                
	                if(obj instanceof Produkty) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();
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
		                
		                int i=0;
		                for(Obiekt_Do_Polecen stan: fData) {
		                	nazwy[i] = ((Producenci)stan).getNazwa();
		                	i++;
		                }
		                i=0;
		                for(Obiekt_Do_Polecen stan: fData2) {
		                	nazwy2[i] = ((Kategorie)stan).getNazwa();
		                	i++;
		                }
		                
		                JComboBox jombo = new JComboBox(nazwy);
		                JComboBox jombo2 = new JComboBox(nazwy2);
                		
	                	myPanel.add(new JLabel("Nazwa: "));
		         		myPanel.add(pierwszyField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Cena: "));
		         		myPanel.add(drugiField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Opis: "));
		         		myPanel.add(trzeciField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Id producenta: "));
		         		myPanel.add(jombo);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Id kategorii: "));
		         		myPanel.add(jombo2);
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj produkt", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {			                		
			 	                	//oc.createDBSession();
			 	                	//Session session = oc.getDBSession();
			                		
			 	                	if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty() || trzeciField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Produkt nie został dodany");
			 	                		return;
			 	                	}
			 	                	double cena = Double.parseDouble(drugiField.getText());
			 	                	cena = Math.round(cena*100.0)/100.0;
			 	                	//oc.closeDBSession();
			 	                	//session.save(new Produkty(pierwszyField.getText(), cena, trzeciField.getText(), ((Producenci)fData.get(jombo.getSelectedIndex())).getId_producenta(), ((Kategorie)fData2.get(jombo2.getSelectedIndex())).getId_Kategorii(), 0));
			 	                	
			 	                	Produkty nowyProdukt = new Produkty(pierwszyField.getText(), cena, trzeciField.getText(), ((Producenci)fData.get(jombo.getSelectedIndex())).getId_producenta(), ((Kategorie)fData2.get(jombo2.getSelectedIndex())).getId_Kategorii(), 0); 
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyProdukt, idUzytkownika));
			                		
			                		//pokazProduktPrzycisk.doClick();
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}		 	
			 	                	
			 					 	if(!cache.containsKey("Kategorie")) {
			 							oc.createDBSession();
			 							try (Session session2 = oc.getDBSession()) {
			 					            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Kategorie order by id_kategorii", Obiekt_Do_Polecen.class);
			 					            cache.put("Kategorie", query.getResultList());
			 					            oc.closeDBSession();
			 					        } catch (Exception e) {
			 					            e.printStackTrace();
			 					        }
			 					 	}
			 	                	
			 					 	List<Obiekt_Do_Polecen> cash = cache.get("Kategorie");
			 					 	String nazwa = "Default";

			 					 	
			 	                	int id = Integer.parseInt(((DefaultTableModel)tab.getModel()).getValueAt(((DefaultTableModel)tab.getModel()).getRowCount()-1, 0).toString());
			 	                	nowyProdukt.setId_produktu(id+1);
			 	                	
			 					 	for(Obiekt_Do_Polecen entity: cash) {
			 					 		Kategorie ent = (Kategorie)entity;
			 					 		if(ent.getId_Kategorii() == nowyProdukt.getKategorie_id_kategorii()) {
			 					 			nazwa =ent.getNazwa();
			 					 			break;
			 					 		}
			 					 	}
			 					 	
			 					 	
			 					 	if(!cache.containsKey("Producenci")) {
			 							oc.createDBSession();
			 							try (Session session2 = oc.getDBSession()) {
			 					            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Producenci order by id_producenta", Obiekt_Do_Polecen.class);
			 					            cache.put("Producenci", query.getResultList());
			 					            oc.closeDBSession();
			 					        } catch (Exception e) {
			 					            e.printStackTrace();
			 					        }
			 					 	}
			 	                	
			 					 	List<Obiekt_Do_Polecen> cash2 = cache.get("Producenci");
			 					 	String nazwa2 = "Default";
			 					 				 	                	
			 					 	for(Obiekt_Do_Polecen entity: cash2) {
			 					 		Producenci ent = (Producenci)entity;
			 					 		if(ent.getId_producenta() == nowyProdukt.getProducenci_id_producenta()) {
			 					 			nazwa2 =ent.getNazwa();
			 					 			break;
			 					 		}
			 					 	}
			 	                	if(nazwaTypu.equals("Klient"))((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Produkty)nowyProdukt).getId_produktu()), ((Produkty)nowyProdukt).getNazwa(), Double.toString(((Produkty)nowyProdukt).getCena()),  ((Produkty)nowyProdukt).getOpis(), nazwa2,  nazwa});
			 	                	else ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Produkty)nowyProdukt).getId_produktu()), ((Produkty)nowyProdukt).getNazwa(), Double.toString(((Produkty)nowyProdukt).getCena()),  ((Produkty)nowyProdukt).getOpis(), nazwa2,  nazwa,  ((((Produkty)nowyProdukt).getCzy_usunieto()) == 1)? "TAK":"NIE"});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			 	                	
			 	                	
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać produktu. Błąd: " + e.getMessage());
		         		 }
	                }
	                else if(obj instanceof Faktury) 
	                {
	                	myPanel.add(new JLabel("NIP: "));
		         		myPanel.add(pierwszyField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Id zamówienia: "));
		         		myPanel.add(drugiField);		         		
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj fakturę", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {
			                		
			                		//OracleConnection oc =  OracleConnection.getInstance();
			 	                	//oc.createDBSession();
			 	                	//Session session = oc.getDBSession();
			                		
			 	                	if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Faktura nie została dodana");
			 	                		return;
			 	                	}
			 	                	//oc.closeDBSession();
			 	                	
			 	                	//session.save(new Faktury(LocalDate.now(), pierwszyField.getText(), Integer.parseInt(drugiField.getText())));
			 	                	
			 	                	Faktury nowaFaktura = new Faktury(LocalDate.now(), pierwszyField.getText(), Integer.parseInt(drugiField.getText()));
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowaFaktura, idUzytkownika));
			                		
			                		//pokazFakturyPrzycisk.doClick();
			 	                
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}		
			 	                	
			 	                	int id = Integer.parseInt(((DefaultTableModel)tab.getModel()).getValueAt(((DefaultTableModel)tab.getModel()).getRowCount()-1, 0).toString());

			 	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Faktury)nowaFaktura).getId_faktury()), ((Faktury)nowaFaktura).getData_wystawienia(), ((Faktury)nowaFaktura).getNIP(), Integer.toString(((Faktury)nowaFaktura).getZamowienia_id_zamowienia())});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać faktury. Błąd: " + e.getMessage());
		         		 }
	                }
	                else if(obj instanceof Kategorie) 
	                {
	                	myPanel.add(new JLabel("Nazwa: "));
		         		myPanel.add(pierwszyField);		         			         		
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj kategorię", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {
			                		
			                		//OracleConnection oc =  OracleConnection.getInstance();
			 	                	//oc.createDBSession();
			 	                	//Session session = oc.getDBSession();
			                		
			 	                	if(pierwszyField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Kategoria nie została dodana");
			 	                		return;
			 	                	}
			 	                	
			 	                	//session.save(new Kategorie(pierwszyField.getText()));
			 	                	//oc.closeDBSession();
			 	                				 	                	
			 	                	//Polecenie_Dodaj pd = new Polecenie_Dodaj(new Kategorie(pierwszyField.getText()));
			 	                	//pd.Wykonaj();
			 	                	Kategorie nowaKategoria = new Kategorie(pierwszyField.getText());
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowaKategoria, idUzytkownika));
			 	                	
			 	                	List<Obiekt_Do_Polecen> lista = cache.get("Kategorie");
			 	                	lista.add(nowaKategoria);
			 	                	cache.put("Kategorie", lista);
			                			                		
			 	                	//pokazKategoriePrzycisk.doClick();
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	       System.out.print(kontener.getComponent(0) + "\n");
			 	                            System.out.print(kontener.getComponent(1)+ "\n");
			 	                            System.out.print(kontener.getComponent(2)+ "\n");
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}		 	                	
			 	                	
			 	                	nowaKategoria.setId_Kategorii(((Kategorie)lista.get(lista.size()-2)).getId_Kategorii()+1);
			 	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Kategorie)nowaKategoria).getId_Kategorii()), ((Kategorie)nowaKategoria).getNazwa()});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			 	                	
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać kategorii. Błąd: " + e.getMessage());
		         		 }
	                }
	                else if(obj instanceof Magazyny) 
	                {
	                	myPanel.add(new JLabel("Miasto: "));
		         		myPanel.add(pierwszyField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Ulica: "));
		         		myPanel.add(drugiField);		         		
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj magazyn", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {			                		
			 	                	if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Magazyn nie został dodany");
			 	                		return;
			 	                	}
			 	                	//session.save(new Magazyny(pierwszyField.getText(), drugiField.getText()));
			 	                	Magazyny nowyMagazyn = new Magazyny(pierwszyField.getText(), drugiField.getText());
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyMagazyn, idUzytkownika));
			 	                	List<Obiekt_Do_Polecen> lista = cache.get("Magazyny");
			 	                	lista.add(nowyMagazyn);
			 	                	cache.put("Magazyny", lista);
			                		
			                		//pokazMagazynyPrzycisk.doClick();
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}		 	                	
			 	                	
			 	                	nowyMagazyn.setId_magazynu(((Magazyny)lista.get(lista.size()-2)).getId_magazynu()+1);
			 	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Magazyny)nowyMagazyn).getId_magazynu()), ((Magazyny)nowyMagazyn).getMiasto(), ((Magazyny)nowyMagazyn).getUlica()});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać magazynu. Błąd: " + e.getMessage());
		         		 }
	                }
	                else if(obj instanceof Producenci) 
	                {
	                	myPanel.add(new JLabel("Nazwa: "));
		         		myPanel.add(pierwszyField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Kontakt: "));
		         		myPanel.add(drugiField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Miasto: "));
		         		myPanel.add(trzeciField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Ulica: "));
		         		myPanel.add(czwartyField);
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj producenta", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {			                					                			                		
			 	                	if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty() || trzeciField.getText().isEmpty() || czwartyField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Producent nie został dodany");
			 	                		return;
			 	                	}
			 	                	//session.save(new Producenci(pierwszyField.getText(), drugiField.getText(), trzeciField.getText(), czwartyField.getText(), 0));
			 	                	Producenci nowyProducent = new Producenci(pierwszyField.getText(), drugiField.getText(), trzeciField.getText(), czwartyField.getText(), 0);
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyProducent, idUzytkownika));
			 	                	List<Obiekt_Do_Polecen> lista = cache.get("Producenci");
			 	                	lista.add(nowyProducent);
			 	                	cache.put("Producenci", lista);
			                		
			                		
			                		//pokazProducentowPrzycisk.doClick();
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}		 	                	
			 	                	
			 	                	nowyProducent.setId_producenta(((Producenci)lista.get(lista.size()-2)).getId_producenta()+1);
			 	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Producenci)nowyProducent).getId_producenta()), ((Producenci)nowyProducent).getNazwa(), ((Producenci)nowyProducent).getKontakt(), ((Producenci)nowyProducent).getMiasto(), ((Producenci)nowyProducent).getUlica(), (((Producenci)nowyProducent).getCzy_usunieto() == 1)?"TAK":"NIE"});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać producenta. Błąd: " + e.getMessage());
		         		 }
	                }
	                else if(obj instanceof Produkt_Magazyn)
	                {
	                	myPanel.add(new JLabel("Id magazynu: "));
		         		myPanel.add(pierwszyField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Id produktu: "));
		         		myPanel.add(drugiField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Stan magazynowy: "));
		         		myPanel.add(trzeciField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Stan faktyczny: "));
		         		myPanel.add(czwartyField);
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj produkt do magazynu", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {
			                					                					                		
			 	                	if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty() || trzeciField.getText().isEmpty() || czwartyField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Produkt nie został dodany do magazynu");
			 	                		return;
			 	                	}
			 	                	
			 	                	if(Integer.parseInt(trzeciField.getText())<0)
			 	                		throw(new Exception("Stan magazynowy nie może być ujemny."));
			 	                	
			 	                	if(Integer.parseInt(czwartyField.getText())<=0)
			 	                		throw(new Exception("Stan faktyczny nie może być ujemny."));
			 	                			 	                	
			 	                	Produkt_Magazyn_Id idpm = new Produkt_Magazyn_Id(Integer.parseInt(pierwszyField.getText()), Integer.parseInt(drugiField.getText()));
			 	                	//session.save(new Produkt_Magazyn(idpm, Integer.parseInt(trzeciField.getText()), Integer.parseInt(czwartyField.getText())));
			 	                	
			 	                	Produkt_Magazyn nowyPM = new Produkt_Magazyn(idpm, Integer.parseInt(trzeciField.getText()), Integer.parseInt(czwartyField.getText()));
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyPM, idUzytkownika));

			                		
			                		//pokazProduktMagazynPrzycisk.doClick();
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}		 	                	
			 	                	
			 	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Produkt_Magazyn)nowyPM).getMagazyn_id()), Integer.toString(((Produkt_Magazyn)nowyPM).getProdukt_id()), Integer.toString(((Produkt_Magazyn)nowyPM).getStan_faktyczny()), Integer.toString(((Produkt_Magazyn)nowyPM).getStan_magazynowy())});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać produktu do magazynu. Błąd: " + e.getMessage());
		         		 }
	                }
	                else if(obj instanceof Produkt_Zamowienia) 
	                {
	                	myPanel.add(new JLabel("Id zamówienia: "));
		         		myPanel.add(pierwszyField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Id produktu: "));
		         		myPanel.add(drugiField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Ilość: "));
		         		myPanel.add(trzeciField);
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj produkt do zamówienia", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {
			                		
			                		//OracleConnection oc = OracleConnection.getInstance();
			 	                	//oc.createDBSession();
			 	                	//Session session = oc.getDBSession();
			                		
			 	                	if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty() || trzeciField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Produkt nie został dodany do zamówienia.");
			 	                		return;
			 	                	}
			 	                	
			 	                	if(Integer.parseInt(trzeciField.getText())<=0)
			 	                		throw(new Exception("Ilość nie może być ujemna ani równa 0."));
			 	                	
			 	                	Produkt_Zamowienia_Id idpz = new Produkt_Zamowienia_Id(Integer.parseInt(pierwszyField.getText()), Integer.parseInt(drugiField.getText()));
			 	                	
			 	                	Produkt_Zamowienia nowyPZ = new Produkt_Zamowienia(idpz, Integer.parseInt(trzeciField.getText()));
			 	                	
			 	                	//session.save(new Produkt_Zamowienia(idpz, Integer.parseInt(trzeciField.getText())));
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyPZ, idUzytkownika));

			                		
			                		//oc.closeDBSession();
			                		//pokazProduktZamowieniaPrzycisk.doClick();
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}		 	                	
			 	                	
			 	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Produkt_Zamowienia)nowyPZ).getZamowienieId()), Integer.toString(((Produkt_Zamowienia)nowyPZ).getProduktId()), Integer.toString(((Produkt_Zamowienia)nowyPZ).getIlosc())});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać produktu do zamówienia. Błąd: " + e.getMessage());
		         		 }
	                }
	                else if(obj instanceof Stany_Zamowienia) 
	                {
	                	myPanel.add(new JLabel("Nazwa: "));
		         		myPanel.add(pierwszyField);
		         		myPanel.add(Box.createHorizontalStrut(5));		         	
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj stan zamówienia", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {
			                		
			                		//OracleConnection oc =  OracleConnection.getInstance();
			 	                	//oc.createDBSession();
			 	                	//Session session = oc.getDBSession();
			                		
			 	                	if(pierwszyField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Stan zamówienia nie został dodany.");
			 	                		return;
			 	                	}
			 	                				 	                	
			 	                	//session.save(new Stany_Zamowienia(pierwszyField.getText()));
			 	                	Stany_Zamowienia nowyStan = new Stany_Zamowienia(pierwszyField.getText());
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyStan, idUzytkownika));
			 	                	List<Obiekt_Do_Polecen> lista = cache.get("StanyZamowien");
			 	                	lista.add(nowyStan);
			 	                	cache.put("StanyZamowien", lista);
			                		
			                		//oc.closeDBSession();
			                		//pokazStanyZamowienPrzycisk.doClick();
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}		 	                	
			 	                	
			 	                	nowyStan.setId_Stanu_Zamowienia(((Stany_Zamowienia)lista.get(lista.size()-2)).getId_Stanu_Zamowienia()+1);
			 	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Stany_Zamowienia)nowyStan).getId_Stanu_Zamowienia()), ((Stany_Zamowienia)nowyStan).getNazwa()});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać stanu zamówienia. Błąd: " + e.getMessage());
		         		 }
	                }
	                else if(obj instanceof Typy_uzytkownika) 
	                {
	                	myPanel.add(new JLabel("Nazwa: "));
		         		myPanel.add(pierwszyField);
		         		myPanel.add(Box.createHorizontalStrut(5));		         	
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj typ użytkownika", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {
			                		
			                		//OracleConnection oc =  OracleConnection.getInstance();
			 	                	//oc.createDBSession();
			 	                	//Session session = oc.getDBSession();
			                		
			 	                	if(pierwszyField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Typ użytkownika nie został dodany.");
			 	                		return;
			 	                	}
			 	                				 	                	
			 	                	//session.save(new Typy_uzytkownika(pierwszyField.getText()));
			 	                	Typy_uzytkownika nowyTyp = new Typy_uzytkownika(pierwszyField.getText());
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyTyp, idUzytkownika));
			 	                	List<Obiekt_Do_Polecen> lista = cache.get("StanyZamowien");
			 	                	lista.add(nowyTyp);
			 	                	cache.put("StanyZamowien", lista);

			                		
			                		//oc.closeDBSession();
			                		//pokazTypyUzytkownikaPrzycisk.doClick();
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}		 	                	
			 	                	
			 	                	nowyTyp.setId_typu_uzytkownika(((Typy_uzytkownika)lista.get(lista.size()-2)).getId_typu_uzytkownika()+1);
			 	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Typy_uzytkownika)nowyTyp).getId_typu_uzytkownika()), ((Typy_uzytkownika)nowyTyp).getNazwa()});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać typu użytkownika. Błąd: " + e.getMessage());
		         		 }
	                }
	                else if(obj instanceof Uzytkownicy) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();
		                oc.createDBSession();

		                List<Obiekt_Do_Polecen> fData = null;

		                try (Session session = oc.getDBSession()) {
		                    Query<Obiekt_Do_Polecen> query = session.createQuery("FROM Typy_uzytkownika", Obiekt_Do_Polecen.class);
		                    fData = query.getResultList();
		                    oc.closeDBSession();
		                } catch (Exception e) {
		                    e.printStackTrace();
		                    System.out.println(e);
		                }
		                
		                String nazwy[] = new String[fData.size()]; 
		                
		                int i=0;
		                for(Obiekt_Do_Polecen stan: fData) {
		                	nazwy[i] = ((Typy_uzytkownika)stan).getNazwa();
		                	i++;
		                }
		                
		                JComboBox jombo = new JComboBox(nazwy);
                		
                		//user.setId_stanu_zamowienia(((Typy_uzytkownika)fData.get(jombo.getSelectedIndex())).getId_typu_uzytkownika());

	                	myPanel.add(new JLabel("Nazwa uzytkownika: "));
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
		         		myPanel.add(new JLabel("Id typu użytkownika: "));
                		myPanel.add(jombo);
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj użytkownika", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {			                		
			 	                	//oc.createDBSession();
			 	                	//Session session = oc.getDBSession();
			                		
			 	                	if(pierwszyField.getText().isEmpty() || drugiField.getText().isEmpty() || trzeciField.getText().isEmpty() || czwartyField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Użytkownik nie został dodany");
			 	                		return;
			 	                	}
			 	                				 	                				 	                
			 	                	//session.save(new Uzytkownicy(pierwszyField.getText(), drugiField.getText(), trzeciField.getText(), czwartyField.getText(), ((Typy_uzytkownika)fData.get(jombo.getSelectedIndex())).getId_typu_uzytkownika(), 0));
			 	                	
			 	                	Uzytkownicy nowyUzytkownik = new Uzytkownicy(pierwszyField.getText(), drugiField.getText(), trzeciField.getText(), czwartyField.getText(), ((Typy_uzytkownika)fData.get(jombo.getSelectedIndex())).getId_typu_uzytkownika(), 0);
			 	                	
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(nowyUzytkownik, idUzytkownika));

			 	                	
			                		//oc.closeDBSession();
			                		//pokazUzytkownicyPrzycisk.doClick();
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}		 	
			 	                	
			 					 	if(!cache.containsKey("TypyUzytkownika")) {
			 							oc.createDBSession();
			 							try (Session session2 = oc.getDBSession()) {
			 					            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Typy_uzytkownika order by id_typu_uzytkownika", Obiekt_Do_Polecen.class);
			 					            cache.put("TypyUzytkownika", query.getResultList());
			 					            oc.closeDBSession();
			 					        } catch (Exception e) {
			 					            e.printStackTrace();
			 					        }
			 					 	}
			 	                	
			 					 	List<Obiekt_Do_Polecen> cash = cache.get("TypyUzytkownika");
			 					 	String nazwa = "Default";

			 					 	
			 	                	int id = Integer.parseInt(((DefaultTableModel)tab.getModel()).getValueAt(((DefaultTableModel)tab.getModel()).getRowCount()-1, 0).toString());
			 	                	nowyUzytkownik.setId_uzytkownika(id+1);
			 	                	
			 					 	for(Obiekt_Do_Polecen entity: cash) {
			 					 		Typy_uzytkownika ent = (Typy_uzytkownika)entity;
			 					 		if(ent.getId_typu_uzytkownika() == nowyUzytkownik.getId_typu_uzytkownika()) {
			 					 			nazwa =ent.getNazwa();
			 					 			break;
			 					 		}
			 					 	}
			 	                	
			 	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Uzytkownicy)nowyUzytkownik).getId_uzytkownika()), ((Uzytkownicy)nowyUzytkownik).getNazwa_uzytkownika(), ((Uzytkownicy)nowyUzytkownik).getLogin(),  ((Uzytkownicy)nowyUzytkownik).getHaslo(), ((Uzytkownicy)nowyUzytkownik).getE_mail(),  nazwa,  ((((Uzytkownicy)nowyUzytkownik).getCzy_usunieto()) == 1)? "TAK":"NIE"});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać użytkownika. Błąd: " + e.getMessage());
		         		 }
	                }
	                else if(obj instanceof Zamowienia) 
	                {		         			                	
	                	myPanel.add(new JLabel("Id uzytkownika: "));
		         		myPanel.add(pierwszyField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Adres wysyłki miasto: "));
		         		myPanel.add(trzeciField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Adres wysyłki ulica: "));
		         		myPanel.add(czwartyField);
		         		myPanel.add(Box.createHorizontalStrut(5));
		         		myPanel.add(new JLabel("Koszt: "));
		         		myPanel.add(piatyField);
		         	        
		         		
		         		int result = JOptionPane.showConfirmDialog(null, myPanel, 
		                         "Dodaj zamówienie", JOptionPane.OK_CANCEL_OPTION);
		         		 try {
			                	if (result == JOptionPane.OK_OPTION) {

			 	                	//oc.createDBSession();
			 	                	//Session session = oc.getDBSession();
			                		
			 	                	if(pierwszyField.getText().isEmpty() || trzeciField.getText().isEmpty() || czwartyField.getText().isEmpty() || piatyField.getText().isEmpty())
			 	                	{
			 	                		JOptionPane.showMessageDialog(null, "Nie podano wszystkich danych. Zamówienie nie zostało dodane");
			 	                		return;
			 	                	}
			 	                	
			 	                	double cena = Double.parseDouble(piatyField.getText());
			 	                	cena = Math.round(cena*100.0)/100.0;
			 	                	//session.save(new Zamowienia(cena, trzeciField.getText(), czwartyField.getText(), 1, Integer.parseInt(pierwszyField.getText()), null));
			 	                	Zamowienia noweZamowienie = new Zamowienia(cena, trzeciField.getText(), czwartyField.getText(), 1, Integer.parseInt(pierwszyField.getText()), null);
			 	                	repo_pol.dodajPolecenie(new Polecenie_Dodaj(noweZamowienie, idUzytkownika));

			 	                	
			                		//oc.closeDBSession();
			                		//pokazZamowieniaPrzycisk.doClick();
			 	                	
			 	                	Component[] components = kontener.getComponents();
			 	                	JTable tab = null;
			 	                	
			 	                	for(Component component : components)
			 	                	{
			 	                		if (component instanceof JScrollPane) {
			 	                	        tab = (JTable) (((JScrollPane)component).getViewport().getView());
			 	                	        kontener.removeAll();
			 	                	        break;
			 	                	    }
			 	                	}	
			 	                	
			 	                	if(!cache.containsKey("StanyZamowien")) {
			 							oc.createDBSession();
			 							try (Session session2 = oc.getDBSession()) {
			 					            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Stany_Zamowienia order by id_stanu_zamowienia", Obiekt_Do_Polecen.class);
			 					            cache.put("StanyZamowien",query.getResultList());
			 					            oc.closeDBSession();
			 					        } catch (Exception e) {
			 					            e.printStackTrace();
			 					        }
			 					 	}
			 	                	List<Obiekt_Do_Polecen> cash = cache.get("StanyZamowien");
			 	                	String nazwa = "Default";
			 	                	
			 	                	int id = Integer.parseInt(((DefaultTableModel)tab.getModel()).getValueAt(((DefaultTableModel)tab.getModel()).getRowCount()-1, 0).toString());
			 	                	noweZamowienie.setId_zamowienia(id+1);
			 	                	
			 	                	for(Obiekt_Do_Polecen entities: cash) {
			 	                		Stany_Zamowienia ent = (Stany_Zamowienia) entities;
			 	                		
			 	                		if(ent.getId_Stanu_Zamowienia() == noweZamowienie.getId_stanu_zamowienia())
			 	                		{
			 	                			nazwa = ent.getNazwa();
			 	                		}
			 	                				
			 	                	}
			 	                	
			 	           		    ((DefaultTableModel)tab.getModel()).addRow(new Object[] {Integer.toString(((Zamowienia)noweZamowienie).getId_zamowienia()), ((Zamowienia)noweZamowienie).getAdres_wysylki_miasto(), ((Zamowienia)noweZamowienie).getAdres_wysylki_ulica(),  Double.toString(((Zamowienia)noweZamowienie).getKoszt()), nazwa,  Integer.toString(((Zamowienia)noweZamowienie).getUzytkownicy_id_uzytkownika()), null,  ((Zamowienia)noweZamowienie).getOpis()});

			 	                	JScrollPane pane = new JScrollPane(tab);
			 	                	kontener.add(pane);
			 	                	kontener.add(dodajPrzycisk);
			 						kontener.add(eksportujDoDruku);			 	                	
			 	                	kontener.repaint();	
			 	                	kontener.revalidate();
			                	}
		         		 }
		         		 catch(Exception e) {
		         			 e.printStackTrace();
		         			 JOptionPane.showMessageDialog(null, "Nie udało się dodać zamówienia. Błąd: " + e.getMessage());
		         		 }
	                }
	             
	                			 
				}
		 });
        
        eksportujDoDruku.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {				                
	                if(obj instanceof Produkty) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();
		                oc.createDBSession();

		                List<Obiekt_Do_Polecen> entities = null;
		                
						try (Session session2 = oc.getDBSession()) {							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Produkty order by id_produktu", Obiekt_Do_Polecen.class);
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleProdukty(entities);
						dyrektor.tworzTabeleProdukty(entities, budDruk);
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
	                
	                else if(obj instanceof Faktury) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> entities = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Faktury order by id_faktury", Obiekt_Do_Polecen.class); 
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleFaktury(entities);
						dyrektor.tworzTabeleFaktury(entities, budDruk);
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
	                
	                else if(obj instanceof Kategorie) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> entities = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Kategorie order by id_kategorii", Obiekt_Do_Polecen.class); 
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //BudowniczyTabeliDruk budDruk = new BudowniczyTabeliDruk();
		                 //budDruk.tworzTabeleKategorie(entities);
						
		                 //String table = (String)budDruk.pobierzTabele(null);
						
						dyrektor.tworzTabeleKategorie(entities, budDruk);
		                 												 		   
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
	                else if(obj instanceof Magazyny) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> entities = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Magazyny order by id_magazynu", Obiekt_Do_Polecen.class); 
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleMagazyny(entities);
						dyrektor.tworzTabeleMagazyny(entities, budDruk);
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
	                else if(obj instanceof Producenci) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> entities = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Producenci order by id_producenta", Obiekt_Do_Polecen.class); 
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleProducenci(entities);
						dyrektor.tworzTabeleProducenci(entities, budDruk);
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
	                else if(obj instanceof Produkt_Magazyn)
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> entities = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Produkt_Magazyn order by produkt_magazyn_id.magazyny_id_magazynu, produkt_magazyn_id.produkty_id_produktu", Obiekt_Do_Polecen.class); //tututki
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleProdukt_Magazyn(entities);
						dyrektor.tworzTabeleProdukt_Magazyn(entities, budDruk);
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
	                else if(obj instanceof Produkt_Zamowienia) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> entities = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Produkt_Zamowienia  order by produkt_zamowienia_id.id_zamowienia, produkt_zamowienia_id.id_produktu", Obiekt_Do_Polecen.class); 
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleProdukt_Zamowienia(entities);
						dyrektor.tworzTabeleProdukt_Zamowienia(entities, budDruk);
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
	                else if(obj instanceof Stany_Zamowienia) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> entities = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Stany_Zamowienia order by id_stanu_zamowienia", Obiekt_Do_Polecen.class); 
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleStany_Zamowienia(entities);
						dyrektor.tworzTabeleStany_Zamowienia(entities, budDruk);
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
	                else if(obj instanceof Typy_uzytkownika) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> entities = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Typy_uzytkownika order by id_typu_uzytkownika", Obiekt_Do_Polecen.class); 
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }

		                 //budDruk.tworzTabeleTypy_uzytkownika(entities);
						dyrektor.tworzTabeleTypy_uzytkownika(entities, budDruk);
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
	                else if(obj instanceof Uzytkownicy) 
	                {
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> entities = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Uzytkownicy order by id_uzytkownika", Obiekt_Do_Polecen.class); 
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleUzytkownicy(entities);
						dyrektor.tworzTabeleUzytkownicy(entities, budDruk);
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
	                else if(obj instanceof Zamowienia) 
	                {		         			                	
	                	PolaczenieOracle oc =  PolaczenieOracle.getInstance();

		                List<Obiekt_Do_Polecen> entities = null;
						oc.createDBSession();
						
						try (Session session2 = oc.getDBSession()) {
							
				            Query<Obiekt_Do_Polecen> query = null;
				            query = session2.createQuery("FROM Zamowienia order by id_zamowienia", Obiekt_Do_Polecen.class); 
				            entities = query.getResultList();
				            oc.closeDBSession();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
		                
		                 //budDruk.tworzTabeleZamowienia(entities);
						dyrektor.tworzTabeleZamowienia(entities, budDruk);
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
