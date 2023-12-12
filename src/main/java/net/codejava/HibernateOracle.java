package net.codejava;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.persistence.Entity;
import javax.swing.Box;
import javax.swing.JButton;
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

public class HibernateOracle {
	
	public static String nazwaTypu = "null";

	public static void main(String[] args) {
		
		OracleConnection oc =  OracleConnection.getInstance();
		
		oc.createDBSession();
		
		Session session = oc.getDBSession();
		
		//session.save(new Kategorie("Plytki"));
		//session.save(new Zamowienia(15.25,"Wiej","Hallera",1,3));
		//session.save(new Producenci("Biedronka","238989","Bialystok","Pogodna"));
		//session.save(new Produkty("wiadro 2000",52.32,"jeszcze fajniejsze",1,1));
		//session.save(new Produkt_Zamowienia(new Produkt_Zamowienia_Id(2,3),3));
		//session.save(new Magazyny("Skieblewo","Piotrkowa"));
		//session.save(new Produkt_Magazyn(new Produkt_Magazyn_Id(1,3),2,2));
		//session.save(new Faktury("05-10-23","232312",3));
		
		//session.save(new Typy_uzytkownika("Administrator"));
		//session.save(new Typy_uzytkownika("Pracownik"));
		//session.save(new Typy_uzytkownika("Klient"));
		
		//session.save(new Uzytkownicy("Mariusz", "admin","admin", "admin@wp.pl", 2));
		//session.save(new Uzytkownicy("Pawel", "pracownik","pracownik", "pracownik@wp.pl", 3));
		//session.save(new Uzytkownicy("Leokadia", "uzytkownik","uzytkownik", "admin@wp.pl", 4));
		
		
		try
		{
			session = oc.getDBSession();
		}catch(Exception e)
		{
            System.out.println("Blad dodania tablicy");
		}
		
		List<Obiekt_Do_Polecen> entities = null;
		
		try (Session session2 = oc.getDBSession()) {
            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkty", Obiekt_Do_Polecen.class);
            entities = query.getResultList();
            oc.closeDBSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
		//oc.closeDBSession();			 
		
		String placeholderLogin = "xd";
		String placeholderPassword = "1";
		
		final JFrame frame = new JFrame("Elektryka Prad Nie Tyka");
		
		JPanel kontener = new JPanel();
		frame.setContentPane(kontener);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        JMenuBar bar = new JMenuBar();
        
		JButton pokazZalogujPrzycisk = new JButton("Zaloguj sie");
		JButton pokazProduktPrzycisk = new JButton("Produkty");
		JButton pokazZamowieniaPrzycisk = new JButton("Zamowienia");
		JButton pokazMagazynyPrzycisk = new JButton("Magazyny");
		JButton pokazFakturyPrzycisk = new JButton("Faktury");
		JButton pokazUzytkownicyPrzycisk = new JButton("Uzytkownicy");
		JButton pokazWylogujPrzycisk = new JButton("Wyloguj");
		
		JLabel nazwaUzytkownika = new JLabel();
		
		Component glue = Box.createHorizontalGlue();
		bar.add(glue);
		bar.add(pokazZalogujPrzycisk);
		
		BudowniczyTabeliSwing budSwing = new BudowniczyTabeliSwing();		 
		
		budSwing.tworzTabeleProdukty(entities);
		        
        JTable tabSwing = budSwing.pobierzTabeleSwing();
        
        JScrollPane pane = new JScrollPane(tabSwing);
               
        kontener.add(pane);
        
	
		frame.setJMenuBar(bar);
	
		frame.setSize(600, 450);
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
	                            Query<Uzytkownicy> query = session2.createQuery("FROM Uzytkownicy", Uzytkownicy.class);
	                            uzytkownicy = query.getResultList();
	                            oc.closeDBSession();
	                        } catch (Exception e1) {
	                            e1.printStackTrace();
	                        }
	                		for(Uzytkownicy uzytkownik: uzytkownicy) {
			                	if(loginField.getText().equals(uzytkownik.getLogin())) {
			                		if(hasloField.getText().equals(uzytkownik.getHaslo()))
			                		{
				                		System.out.println("zalogowano");
				                		pokazZalogujPrzycisk.setVisible(false);
				                		bar.remove(pokazZalogujPrzycisk);
				                		bar.remove(glue);
				                		
				                		oc.createDBSession();
				                		Session session2 = oc.getDBSession();
				                		Query<Typy_uzytkownika> query = session2.createQuery("FROM Typy_uzytkownika", Typy_uzytkownika.class);
			                            List<Typy_uzytkownika> typyUzytkownika = query.getResultList();                           
			                                
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
				                				break;
				                			case "Pracownik":
				                				bar.add(pokazProduktPrzycisk);
						                		bar.add(pokazZamowieniaPrzycisk);
				                				bar.add(pokazFakturyPrzycisk);
				                				break;
				                			case "Klient":
				                				bar.add(pokazProduktPrzycisk);
				                				bar.add(pokazZamowieniaPrzycisk);
				                			default:
				                				break;
				                		}
				                		
				                		bar.add(glue);
				                		
				                		nazwaUzytkownika.setText(uzytkownik.getNazwa_uzytkownika());				         
				                		
				                		bar.add(nazwaUzytkownika);
				                		bar.add(pokazWylogujPrzycisk);	
				                		
				                		bar.revalidate();
				                		bar.repaint();

				                		break;
			                		}
			                		else
			                		{
			                			System.out.println("Podano zle haslo");
			                			break;
			                		}
			                	}
	                		}
		                }
	                    
	                } catch (Exception ex) {
	                    JOptionPane.showMessageDialog(null, "Podano zle dane logowania.");
	                }
	                ;
	            }
	        });
		 
		 pokazWylogujPrzycisk.addActionListener(new ActionListener() {
			 
			 public void actionPerformed(ActionEvent a)
			 {
				 List<Obiekt_Do_Polecen> entities = null;
				 nazwaTypu = "null";
				 
				 kontener.removeAll();
				 
				 bar.removeAll();
				 bar.setVisible(true);

				 oc.createDBSession();
				 try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkty", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
				 
				 budSwing.tworzTabeleProdukty(entities);
			        
			     JTable tabSwing = budSwing.pobierzTabeleSwing();
			        
			     JScrollPane pane = new JScrollPane(tabSwing);
			               
			     kontener.add(pane);
			     bar.add(glue);
				 pokazZalogujPrzycisk.setVisible(true);
				 bar.add(pokazZalogujPrzycisk);
				 
				 bar.revalidate();
				 bar.repaint();
				
				 
				 frame.revalidate();
				 frame.repaint();
				 
				 try {
					 
				 } catch(Exception e3) {
					 System.out.println("Wylogowanie sie nie powiodlo");
				 }
			 }
		 });
		 

        pokazProduktPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {				 	
				 	kontener.removeAll();			 	

					List<Obiekt_Do_Polecen> entities = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Produkty", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					budSwing.tworzTabeleProdukty(entities);
					 JTable tabSwing = budSwing.pobierzTabeleSwing();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazZamowieniaPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();		 	

					List<Obiekt_Do_Polecen> entities = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Zamowienia", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					budSwing.tworzTabeleZamowienia(entities);
					 JTable tabSwing = budSwing.pobierzTabeleSwing();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 frame.revalidate();
					 frame.repaint();
				}
		 });
        
        pokazUzytkownicyPrzycisk.addActionListener(new ActionListener() 
		 {
			 @Override
				public void actionPerformed(ActionEvent a) {
				 	kontener.removeAll();			 	

					List<Obiekt_Do_Polecen> entities = null;
					oc.createDBSession();
					
					try (Session session2 = oc.getDBSession()) {
			            Query<Obiekt_Do_Polecen> query = session2.createQuery("FROM Uzytkownicy", Obiekt_Do_Polecen.class);
			            entities = query.getResultList();
			            oc.closeDBSession();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
					
					budSwing.tworzTabeleUzytkownicy(entities);
					 JTable tabSwing = budSwing.pobierzTabeleSwing();
					 JScrollPane pane = new JScrollPane(tabSwing);					 
					 
					 kontener.add(pane);
					 frame.revalidate();
					 frame.repaint();
				}
		 });
       	     
    }
}
